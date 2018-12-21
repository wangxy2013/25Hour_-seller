package com.jyq.wm.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;


import com.google.gson.Gson;
import com.jyq.wm.R;
import com.jyq.wm.bean.VersionInfo;
import com.jyq.wm.http.DataRequest;
import com.jyq.wm.http.HttpRequest;
import com.jyq.wm.http.IRequestListener;
import com.jyq.wm.json.VersionInfoHandler;
import com.jyq.wm.listener.MyOnClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class VersionManager implements IRequestListener
{
    private Context mContext;

    // 返回的安装包url
    private String apkUrl = "";

    private Dialog downloadDialog;
    /* 下载包安装路径 */
    private static final String savePath = "/sdcard/seller/";

    private static final String saveFileName = savePath + "seller.apk";

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    private static final int DOWN_UPDATE = 0x01;
    private static final int DOWN_OVER = 0x02;
    private static final int REQUEST_SUCCESS = 0x03;
    private static final int REQUEST_FAIL = 0x04;
    private static final String GET_VERSION = "get_version";


    private int progress;

    private Thread downLoadThread;

    private boolean interceptFlag = false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    if (null != downloadDialog)
                    {
                        downloadDialog.dismiss();
                    }


                    installApk();
                    break;
                case REQUEST_SUCCESS:


                    VersionInfoHandler mVersionInfoHandler = (VersionInfoHandler) msg.obj;
                    VersionInfo mVersionInfo = mVersionInfoHandler.getVersionInfo();


                    if (null != mVersionInfo)
                    {


                        if (mVersionInfo.getVersion().compareTo(APPUtils.getVersionName(mContext)) > 0)
                        {
                            showNoticeDialog(mVersionInfo);
                        }

                    }

                    break;

                case REQUEST_FAIL:
                    break;

            }
        }

    };


    public VersionManager(Context context)
    {
        this.mContext = context;
    }


    public void init()
    {

        if (!NetWorkUtil.isConn(mContext))
        {
            return;
        }
        Map<String, Object> valuePairs = new HashMap<>();
        valuePairs.put("appType", "android");
        valuePairs.put("type", 2);
        Gson gson = new Gson();
        Map<String, String> postMap = new HashMap<>();
        postMap.put("json", gson.toJson(valuePairs));

        DataRequest.instance().request(mContext, Urls.getVersionUrl(), this, HttpRequest.POST, GET_VERSION, postMap, new VersionInfoHandler());
    }

    private void showNoticeDialog(final VersionInfo mVersionBean)
    {

        if (mVersionBean.getVersion().compareTo(APPUtils.getVersionName(mContext)) > 0)
        {
            apkUrl = mVersionBean.getLink();
            DialogUtils.showVersionUpdateDialog(mContext, mVersionBean.getVersion_desc(), new MyOnClickListener.OnSubmitListener()
            {

                @Override
                public void onSubmit(String content)
                {
                    if ("1".equals(content))
                    {
                        showDownloadDialog();
                    }
                    else if ("3".equals(content))
                    {
                        Uri uri = Uri.parse(apkUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        mContext.startActivity(intent);
                    }
                    else
                    {
                        if ("1".equals(mVersionBean.getForcedup()))
                        {
                            System.exit(0);
                        }
                    }
                }
            });
        }
    }

    private void showDownloadDialog()
    {

        downloadDialog = new Dialog(mContext, R.style.dialogNoAnimation);
        downloadDialog.setCancelable(false);
        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_progress, null);
        downloadDialog.setContentView(view);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        //Dialog部分
        Window mWindow = downloadDialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.width = (int) (dm.widthPixels * 0.75);
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setAttributes(lp);
        downloadDialog.setCancelable(false);
        downloadDialog.show();


        downloadApk();
    }

    private Runnable mdownApkRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            try
            {
                URL url = new URL(apkUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists())
                {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do
                {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0)
                    {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                }
                while (!interceptFlag);// 点击取消就停止下载.

                fos.close();
                is.close();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    };

    /**
     * 下载apk
     */

    private void downloadApk()
    {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }


    /**
     * 安装apk
     */
    private void installApk()
    {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists())
        {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            Uri apkUri = FileProvider.getUriForFile(mContext, "com.zb.wyd.fileprovider", apkfile);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }
        else
        {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android" + ".package-archive");
            mContext.startActivity(i);
        }

    }


    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity()
    {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (GET_VERSION.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }

    }
}

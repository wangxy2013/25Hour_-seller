package com.jyq.wm.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.jyq.wm.R;
import com.jyq.wm.activity.BaseHandler;
import com.jyq.wm.activity.LoginActivity;
import com.jyq.wm.activity.MainActivity;
import com.jyq.wm.activity.ModifyPwdActivity;
import com.jyq.wm.activity.UserDetailActivity;
import com.jyq.wm.http.DataRequest;
import com.jyq.wm.http.HttpRequest;
import com.jyq.wm.http.IRequestListener;
import com.jyq.wm.json.LoginHandler;
import com.jyq.wm.json.ResultHandler;
import com.jyq.wm.utils.ConfigManager;
import com.jyq.wm.utils.ConstantUtil;
import com.jyq.wm.utils.NetWorkUtil;
import com.jyq.wm.utils.ToastUtil;
import com.jyq.wm.utils.Urls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettingFragment extends BaseFragment implements View.OnClickListener, IRequestListener
{

    @BindView(R.id.rl_user)
    RelativeLayout rlUser;
    @BindView(R.id.rl_pwd)
    RelativeLayout rlPwd;
    @BindView(R.id.rl_cache)
    RelativeLayout rlCache;
    @BindView(R.id.iv_switch)
    ImageView ivSwitch;
    private View rootView = null;
    private Unbinder unbinder;
    private boolean isOpened = false;

    private static final String STORE_OPERATE = "store_operate";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(getActivity())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    if (isOpened)
                    {
                        isOpened = false;
                        ivSwitch.setImageResource(R.drawable.ic_switch_off);
                        ConfigManager.instance().setIsClose("0");
                    }
                    else
                    {
                        isOpened = true;
                        ivSwitch.setImageResource(R.drawable.ic_switch_on);
                        ConfigManager.instance().setIsClose("1");
                    }
                    ToastUtil.show(getActivity(), "操作成功");

                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(getActivity(), msg.obj.toString());
                    break;


            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_setting, null);
            unbinder = ButterKnife.bind(this, rootView);
            initData();
            initViews();
            initViewData();
            initEvent();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews()
    {

    }

    @Override
    protected void initEvent()
    {
        rlUser.setOnClickListener(this);
        rlPwd.setOnClickListener(this);
        ivSwitch.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {


    }

    @Override
    public void onResume()
    {
        super.onResume();
        if ("0".equals(ConfigManager.instance().getIsClose()))
        {
            isOpened = false;
            ivSwitch.setImageResource(R.drawable.ic_switch_off);
        }
        else
        {
            isOpened = true;
            ivSwitch.setImageResource(R.drawable.ic_switch_on);
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v == rlUser)
        {
            startActivity(new Intent(getActivity(), UserDetailActivity.class));
        }
        else if (v == rlPwd)
        {
            startActivityForResult(new Intent(getActivity(), ModifyPwdActivity.class), 9001);
        }
        else if (v == ivSwitch)
        {
            if (isOpened)
            {
                //                isOpened = false;
                //                ivSwitch.setImageResource(R.drawable.ic_switch_off);
                operate("2");
            }
            else
            {
                //                isOpened = true;
                //                ivSwitch.setImageResource(R.drawable.ic_switch_on);
                operate("1");
            }
        }
    }


    private void operate(String operateType)
    {
        if (!NetWorkUtil.isConn(getActivity()))
        {
            NetWorkUtil.showNoNetWorkDlg(getActivity());
            return;
        }
        showProgressDialog(getActivity());
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("storeId", ConfigManager.instance().getUserID());
        valuePairs.put("operateType", operateType);
        Gson gson = new Gson();
        Map<String, String> postMap = new HashMap<>();
        postMap.put("json", gson.toJson(valuePairs));
        DataRequest.instance().request(getActivity(), Urls.getStoreOperateUrl(), this, HttpRequest.POST, STORE_OPERATE, postMap, new ResultHandler());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == 9001)
            {
                ((MainActivity) getActivity()).finish();
            }
        }

    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog(getActivity());
        if (STORE_OPERATE.equals(action))
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

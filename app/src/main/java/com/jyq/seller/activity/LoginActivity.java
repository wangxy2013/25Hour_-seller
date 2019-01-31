package com.jyq.seller.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyq.seller.R;
import com.jyq.seller.http.DataRequest;
import com.jyq.seller.http.HttpRequest;
import com.jyq.seller.http.IRequestListener;
import com.jyq.seller.json.LoginHandler;
import com.jyq.seller.json.UserInfoHandler;
import com.jyq.seller.utils.ConfigManager;
import com.jyq.seller.utils.ConstantUtil;
import com.jyq.seller.utils.LogUtil;
import com.jyq.seller.utils.NetWorkUtil;
import com.jyq.seller.utils.ToastUtil;
import com.jyq.seller.utils.Urls;
import com.jyq.seller.widget.statusbar.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    private String account;
    private String pwd;
    private static final String USER_LOGIN = "user_login";
    private static final String GET_USER_INFO = "get_user_info";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int GET_USER_INFO_SUCCESS = 0x03;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(LoginActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    ConfigManager.instance().setUserName(account);
                    ConfigManager.instance().setUserPwd(pwd);
                    getUserInfo();

                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(LoginActivity.this, msg.obj.toString());
                    break;

                case GET_USER_INFO_SUCCESS:
                    ToastUtil.show(LoginActivity.this, "登录成功");

                    if(ConstantUtil.DEFAULT_PWD.equals(ConfigManager.instance().getUserPwd()))
                    {
                        startActivity(new Intent(LoginActivity.this, ModifyPwdActivity.class));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    break;

            }
        }
    };

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_login);
        StatusBarUtil.transparencyBar(LoginActivity.this);
        StatusBarUtil.StatusBarLightMode(LoginActivity.this, false);
    }

    @Override
    protected void initEvent()
    {
        tvLogin.setOnClickListener(this);
    }

    String rid;
    @Override
    protected void initViewData()
    {
        etPhone.setText(ConfigManager.instance().getUserName());
        etPwd.setText(ConfigManager.instance().getUserPwd());
         rid = JPushInterface.getRegistrationID(getApplicationContext());
        LogUtil.e("TAG","rid--->" +rid);
    }


    private void getUserInfo()
    {
        Map<String, String> postMap = new HashMap<>();
        DataRequest.instance().request(LoginActivity.this, Urls.getUserInfoUrl(), this, HttpRequest.GET, GET_USER_INFO, postMap, new
                UserInfoHandler());
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == tvLogin)
        {

            account = etPhone.getText().toString();
            pwd = etPwd.getText().toString();
            if (TextUtils.isEmpty(account) || account.length() < 11)
            {
                ToastUtil.show(LoginActivity.this, "请输入正确的手机号");
                return;
            }

            if (TextUtils.isEmpty(pwd))
            {
                ToastUtil.show(LoginActivity.this, "密码不能为空");
                return;
            }
            if (!NetWorkUtil.isConn(this))
            {
                NetWorkUtil.showNoNetWorkDlg(this);
                return;
            }
            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("password", pwd);
            valuePairs.put("username", account);
            valuePairs.put("registrationID", rid);
            valuePairs.put("userType", "6");
            Gson gson = new Gson();
            Map<String, String> postMap = new HashMap<>();
            postMap.put("json", gson.toJson(valuePairs));
            DataRequest.instance().request(LoginActivity.this, Urls.getLoginUrl(), this, HttpRequest.POST, USER_LOGIN, postMap, new LoginHandler());

        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {

        if (USER_LOGIN.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else
            {
                hideProgressDialog();
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_USER_INFO.equals(action))
        {
            hideProgressDialog();
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_USER_INFO_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }
}

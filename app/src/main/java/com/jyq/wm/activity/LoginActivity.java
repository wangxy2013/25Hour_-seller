package com.jyq.wm.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jyq.wm.R;
import com.jyq.wm.http.DataRequest;
import com.jyq.wm.http.HttpRequest;
import com.jyq.wm.http.IRequestListener;
import com.jyq.wm.json.LoginHandler;
import com.jyq.wm.utils.ConfigManager;
import com.jyq.wm.utils.ConstantUtil;
import com.jyq.wm.utils.StringUtils;
import com.jyq.wm.utils.ToastUtil;
import com.jyq.wm.utils.Urls;
import com.jyq.wm.utils.statusbar.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

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
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
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
                    ToastUtil.show(LoginActivity.this, "登录成功");
                    ConfigManager.instance().setUserName(account);
                    ConfigManager.instance().setUserPwd(pwd);

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(LoginActivity.this, msg.obj.toString());
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

    @Override
    protected void initViewData()
    {
        etPhone.setText(ConfigManager.instance().getUserName());
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

            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("password", pwd);
            valuePairs.put("username", account);
            valuePairs.put("userType", "7");
            DataRequest.instance().request(LoginActivity.this, Urls.getLoginUrl(), this, HttpRequest.POST, USER_LOGIN, valuePairs, new LoginHandler
                    ());

        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (USER_LOGIN.equals(action))
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

package com.jyq.wm.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.jyq.wm.http.DataRequest;
import com.jyq.wm.http.HttpRequest;
import com.jyq.wm.http.IRequestListener;
import com.jyq.wm.json.UserInfoHandler;
import com.jyq.wm.utils.ConfigManager;
import com.jyq.wm.utils.ToastUtil;
import com.jyq.wm.utils.Urls;

import java.util.HashMap;
import java.util.Map;

public class UserDetailActivity extends BaseActivity implements IRequestListener
{


    private static final String GET_USER_INFO = "get_user_info";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(UserDetailActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:

                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(UserDetailActivity.this, msg.obj.toString());
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

    }

    @Override
    protected void initEvent()
    {

    }

    @Override
    protected void initViewData()
    {
//        Map<String, String> postMap = new HashMap<>();
//        DataRequest.instance().request(UserDetailActivity.this, Urls.getUserDetailUrl(), this, HttpRequest.GET, GET_USER_INFO, postMap, new UserInfoHandler());

    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {

    }
}

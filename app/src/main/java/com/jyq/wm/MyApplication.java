package com.jyq.wm;

import android.app.Application;
import android.os.StrictMode;


import com.jyq.wm.utils.APPUtils;
import com.jyq.wm.utils.ConfigManager;
import com.jyq.wm.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：一句话简单描述
 */
public class MyApplication extends Application
{
    private static MyApplication instance;


    public static MyApplication getInstance()
    {
        return instance;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        APPUtils.configImageLoader(getApplicationContext());
        ConfigManager.instance().init(this);
    }


    public boolean isLogin()
    {
        if (StringUtils.stringIsEmpty(ConfigManager.instance().getUniqueCode()))
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    public static MyApplication getContext()
    {
        return instance;
    }


}

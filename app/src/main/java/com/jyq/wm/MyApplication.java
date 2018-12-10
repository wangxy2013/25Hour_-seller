package com.jyq.wm;

import android.app.Application;
import android.app.Service;
import android.os.StrictMode;
import android.os.Vibrator;


import com.baidu.mapapi.SDKInitializer;
import com.jyq.wm.map.LocationService;
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

    public LocationService locationService;
    public Vibrator mVibrator;
    public static MyApplication getInstance()
    {
        return instance;
    }


    private  boolean isOnline;

    public boolean isOnline()
    {
        return isOnline;
    }

    public void setOnline(boolean online)
    {
        isOnline = online;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        APPUtils.configImageLoader(getApplicationContext());
        ConfigManager.instance().init(this);
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
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

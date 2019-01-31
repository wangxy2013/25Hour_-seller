package com.jyq.seller;

import android.app.Application;
import android.os.Vibrator;


import com.baidu.location.BDLocation;
import com.jyq.seller.map.LocationService;
import com.jyq.seller.utils.APPUtils;
import com.jyq.seller.utils.ConfigManager;
import com.jyq.seller.utils.StringUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * 描述：一句话简单描述
 */
public class MyApplication extends Application
{
    private static MyApplication instance;
    private  BDLocation location;
//    public LocationService locationService;
//    public Vibrator mVibrator;
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
      //  locationService = new LocationService(getApplicationContext());
      //  mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
       // SDKInitializer.initialize(getApplicationContext());
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
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


    public BDLocation getLocation()
    {
        return location;
    }

    public void setLocation(BDLocation location)
    {
        this.location = location;
    }
}

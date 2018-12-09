package com.jyq.wm.utils;

/**
 * URL管理类
 *
 * @since[产品/模块版本]
 * @seejlj
 */
public class Urls
{
    public static final String HTTP_IP = "http://www.883974.com/";


    //获取版本信息
    public static String getVersionUrl()
    {
        return ConfigManager.instance().getDomainName() + "/index/config";
    }


    //用戶登录
    public static String getLoginUrl()
    {
        return ConfigManager.instance().getDomainName() + "/user/login";
    }


    //用戶注册
    public static String getRegisterUrl()
    {
        return ConfigManager.instance().getDomainName() + "/user/register";
    }


}


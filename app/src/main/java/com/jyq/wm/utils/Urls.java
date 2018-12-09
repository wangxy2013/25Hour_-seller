package com.jyq.wm.utils;

/**
 * URL管理类
 *
 * @since[产品/模块版本]
 * @seejlj
 */
public class Urls
{
    public static final String HTTP_IP = "http://47.100.20.214:8081";


    //获取版本信息
    public static String getVersionUrl()
    {
        return ConfigManager.instance().getDomainName() + "/index/config";
    }


    //用戶登录
    public static String getLoginUrl()
    {
        return HTTP_IP + "/app/auth/login";
    }


    //operateType (string, optional): 1-上线0-下线
    //骑手上线或者下线
    public static String getOnOfflinUrl()
    {
        return HTTP_IP + "/app/deliverUserOperater/onOfflin";
    }

    public static String getUserInfoUrl()
    {
        return HTTP_IP + "/app/auth/userInfo";
    }
    public static String getOrderListUrl()
    {
        return HTTP_IP + "/deliverUserOperater/robbing/query";
    }


}


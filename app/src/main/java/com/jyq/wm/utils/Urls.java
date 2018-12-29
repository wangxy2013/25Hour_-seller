package com.jyq.wm.utils;

/**
 * URL管理类
 *
 * @since[产品/模块版本]
 * @seejlj
 */
public class Urls
{
    public static final String HTTP_IP = "http://www.ershiwu.cn:8082";


    //获取版本信息
    public static String getVersionUrl()
    {
        return HTTP_IP + "/app/appInfo/obtain";
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
        return HTTP_IP + "/app/store/order/query";
    }

    //商户接单
    public static String getReceiptUrl()
    {
        return HTTP_IP + "/app/store/order/confirm";
    }


    //上传定位信息
    public static String getUplaodLocationUrl()
    {
        return HTTP_IP + "/app/deliverUserOperater/location";
    }


    //店铺开关操作
    public static String getStoreOperateUrl()
    {
        return HTTP_IP + "/app/store/operate";
    }


    //骑手确认取餐
    public static String getTakemealConfirmUrl()
    {
        return HTTP_IP + "/app/deliverUserOperater/takemeal/confirm";
    }

    //骑手确认完成送单
    public static String getRobbingConfirmUrl()
    {
        return HTTP_IP + "/app/deliverUserOperater/robbing/confirm";
    }


    //修改密码
    public static String getModifyPwdUrl()
    {
        return HTTP_IP + "/app/auth/modify/password";
    }

    //获取订单详情
    public static String getOrederDetail()
    {
        return HTTP_IP + "/app/deliverUserOperater/info";
    }

    //商家催单
    public static String getOrederReminderUrl()
    {
        return HTTP_IP + "/app/store/order/reminder";
    }

    //完成备餐
    public static String getOrederFinishUrl()
    {
        return HTTP_IP + "/app/store/order/finish/meal";
    }


    //店铺界面查询
    public static String getQuerySellerUrl()
    {
        return "http://www.ershiwu.cn:8081/settlementser/providerSettlement/summary";
    }

}


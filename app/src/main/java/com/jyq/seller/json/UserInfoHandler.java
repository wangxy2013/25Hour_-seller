package com.jyq.seller.json;


import com.jyq.seller.bean.UserInfo;
import com.jyq.seller.utils.ConfigManager;

import org.json.JSONObject;

/**
 * 描述：一句话简单描述
 */
public class UserInfoHandler extends JsonHandler
{
    private UserInfo mUserInfo;

    public UserInfo getUserInfo()
    {
        return mUserInfo;
    }

    @Override
    protected void parseJson(JSONObject jsonObject) throws Exception
    {
        try
        {

            if (null != jsonObject)
            {
                 mUserInfo = new UserInfo(jsonObject);

                if (null != mUserInfo)
                {
                    ConfigManager.instance().setUserId(mUserInfo.getId());
                    ConfigManager.instance().setIsClose(mUserInfo.getIsClosed());
                    ConfigManager.instance().setIsCloseOffline(mUserInfo.getCloseOffline());
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
package com.jyq.wm.json;


import org.json.JSONObject;

/**
 * 描述：一句话简单描述
 */
public class LoginHandler extends JsonHandler
{


    @Override
    protected void parseJson(JSONObject jsonObject) throws Exception
    {
        try
        {
            JSONObject obj = jsonObject.optJSONObject("data");

            if (null != obj)
            {

            }


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
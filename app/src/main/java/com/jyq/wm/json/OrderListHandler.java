package com.jyq.wm.json;


import com.jyq.wm.bean.OrderInfo;
import com.jyq.wm.utils.ConfigManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：一句话简单描述
 */
public class OrderListHandler extends JsonHandler
{

    private List<OrderInfo> orderInfoList = new ArrayList<>();

    public List<OrderInfo> getOrderInfoList()
    {
        return orderInfoList;
    }

    @Override
    protected void parseJson(JSONObject jsonObject) throws Exception
    {
        try
        {

            if (null != jsonObject)
            {
                JSONArray arr = jsonObject.optJSONArray("list");

                for (int i = 0; i < arr.length(); i++)
                {
                    orderInfoList.add(new OrderInfo(arr.optJSONObject(i)));
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
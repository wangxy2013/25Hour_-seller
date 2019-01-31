package com.jyq.seller.bean;

import org.json.JSONObject;

public class DeliverSupply
{
    private String name;//许许多多",
    private String phone;//18876582097",
    private String type;//0"

    public DeliverSupply(JSONObject obj)
    {
        this.name = obj.optString("name");
        this.phone = obj.optString("phone");
        this.type = obj.optString("type");

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}

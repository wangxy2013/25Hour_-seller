package com.jyq.wm.bean;

import org.json.JSONObject;

public class UserInfo
{

    private String id;
    private String name;
    private  String phone;
    private String isClosed;
    private Boolean isCloseOffline;



    public  UserInfo(JSONObject obj)
    {
        this.id = obj.optString("id");
        this.name = obj.optString("name");
        this.phone = obj.optString("phone");
        this.isClosed = obj.optString("isClose");
        this.isCloseOffline=obj.optBoolean("isCloseOffline");
    }


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
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

    public String getIsClosed()
    {
        return isClosed;
    }

    public void setIsClosed(String isClosed)
    {
        this.isClosed = isClosed;
    }

    public Boolean getCloseOffline()
    {
        return isCloseOffline;
    }

    public void setCloseOffline(Boolean closeOffline)
    {
        isCloseOffline = closeOffline;
    }
}

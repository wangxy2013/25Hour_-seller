package com.jyq.wm.bean;

import org.json.JSONObject;

public class UserInfo
{

    private String id;

    private String name;

    private boolean modifyPwdFlag;



    public  UserInfo(JSONObject obj)
    {
        this.id = obj.optString("id");
        this.name = obj.optString("name");
        this.modifyPwdFlag = obj.optBoolean("modifyPwdFlag");
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

    public boolean isModifyPwdFlag()
    {
        return modifyPwdFlag;
    }

    public void setModifyPwdFlag(boolean modifyPwdFlag)
    {
        this.modifyPwdFlag = modifyPwdFlag;
    }
}

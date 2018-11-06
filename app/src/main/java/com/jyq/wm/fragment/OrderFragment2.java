package com.jyq.wm.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyq.wm.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrderFragment2 extends BaseFragment
{

    private View rootView = null;
    private Unbinder unbinder;
    private List<String> tabs = new ArrayList<>(); //标签名称

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_order1, null);
            unbinder = ButterKnife.bind(this, rootView);
            initData();
            initViews();
            initViewData();
            initEvent();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }
        return rootView;
    }
    private static OrderFragment2 instance = null;

    public static OrderFragment2 newInstance()
    {
        if (instance == null)
        {
            instance = new OrderFragment2();
        }
        return instance;
    }
    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews()
    {

    }

    @Override
    protected void initEvent()
    {

    }

    @Override
    protected void initViewData()
    {

    }
}

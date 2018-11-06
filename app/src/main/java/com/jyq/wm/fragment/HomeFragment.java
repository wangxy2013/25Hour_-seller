package com.jyq.wm.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyq.wm.R;
import com.jyq.wm.adapter.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment
{
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    private View rootView = null;
    private Unbinder unbinder;
    private List<String> tabs = new ArrayList<>(); //标签名称

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_home, null);
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

    @Override
    protected void initData()
    {
        tabs.add("待抢");
        tabs.add("取货");
        tabs.add("配送");
        tabs.add("成功");
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
        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(OrderFragment1.newInstance(), "待抢");//添加Fragment
        viewPagerAdapter.addFragment(OrderFragment2.newInstance(), "取货");
        viewPagerAdapter.addFragment(OrderFragment3.newInstance(), "配送");
        viewPagerAdapter.addFragment(OrderFragment4.newInstance(), "成功");
        mViewPager.setAdapter(viewPagerAdapter);//设置适配器
        mViewPager.setOffscreenPageLimit(1);
        mTabLayout.addTab(mTabLayout.newTab().setText("热门直播"));//给TabLayout添加Tab
        mTabLayout.addTab(mTabLayout.newTab().setText("附近美女"));
        // mTabLayout.addTab(mTabLayout.newTab().setText("抖音"));
        mTabLayout.setupWithViewPager(mViewPager);
        //给TabLayout设置关联ViewPager，如果设置了ViewPager，那么ViewPagerAdapter中的getPageTitle()方法返回的就是Tab上的标题

    }
}

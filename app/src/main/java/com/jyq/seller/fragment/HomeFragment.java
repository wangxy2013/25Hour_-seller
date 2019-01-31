package com.jyq.seller.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyq.seller.MyApplication;
import com.jyq.seller.R;
import com.jyq.seller.activity.BaseHandler;
import com.jyq.seller.adapter.MyViewPagerAdapter;
import com.jyq.seller.http.DataRequest;
import com.jyq.seller.http.HttpRequest;
import com.jyq.seller.http.IRequestListener;
import com.jyq.seller.json.ResultHandler;
import com.jyq.seller.utils.ConfigManager;
import com.jyq.seller.utils.ConstantUtil;
import com.jyq.seller.utils.NetWorkUtil;
import com.jyq.seller.utils.ToastUtil;
import com.jyq.seller.utils.Urls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment implements View.OnClickListener, IRequestListener
{
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.tv_status)
    TextView mStatusTv;


    private View rootView = null;
    private Unbinder unbinder;
    private List<String> tabs = new ArrayList<>(); //标签名称
    private boolean isOpened = false;
    private boolean isOnline;
    private static final String ONLINE_REQUEST = "online_request";
    private static final String OFFLINE_REQUEST = "offline_request";
    private static final String STORE_OPERATE = "store_operate";
    private static final int ONLINE_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int OFF_LINE_SUCCESS = 0x03;
    private static final int STORE_OPERATE_SUCCESS = 0x04;


    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(getActivity())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case ONLINE_SUCCESS:
                    isOnline = true;
                    MyApplication.getInstance().setOnline(isOnline);
                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(getActivity(), msg.obj.toString());
                    break;

                case OFF_LINE_SUCCESS:
                    isOnline = false;
                    MyApplication.getInstance().setOnline(isOnline);
                    break;

                case STORE_OPERATE_SUCCESS:
                    if (isOpened)
                    {
                        isOpened = false;
                        mStatusTv.setText("休息中");
                        ConfigManager.instance().setIsClose("0");
                        mStatusTv.setBackgroundResource(R.drawable.common_gray_5dp);
                    }
                    else
                    {
                        isOpened = true;
                        mStatusTv.setText("营业中");
                        ConfigManager.instance().setIsClose("1");
                        mStatusTv.setBackgroundResource(R.drawable.common_yellow_5dp);
                    }
                    ToastUtil.show(getActivity(), "操作成功");
                    break;

            }
        }
    };

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
        tabs.add("待接单");
        tabs.add("已接单");
        tabs.add("催单");
        tabs.add("已完成");
    }

    @Override
    protected void initViews()
    {

    }

    @Override
    protected void initEvent()
    {
        mStatusTv.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(OrderFragment1.newInstance(), "待接单");//添加Fragment
        viewPagerAdapter.addFragment(OrderFragment2.newInstance(), "已接单");
        viewPagerAdapter.addFragment(OrderFragment3.newInstance(), "催单");
        viewPagerAdapter.addFragment(OrderFragment4.newInstance(), "已完成");
        mViewPager.setAdapter(viewPagerAdapter);//设置适配器
        mViewPager.setOffscreenPageLimit(1);
        mTabLayout.addTab(mTabLayout.newTab().setText("待接单"));//给TabLayout添加Tab
        mTabLayout.addTab(mTabLayout.newTab().setText("已接单"));
        mTabLayout.addTab(mTabLayout.newTab().setText("催单"));
        mTabLayout.addTab(mTabLayout.newTab().setText("已完成"));
        mTabLayout.setupWithViewPager(mViewPager);
        //给TabLayout设置关联ViewPager，如果设置了ViewPager，那么ViewPagerAdapter中的getPageTitle()方法返回的就是Tab上的标题


    }

    @Override
    public void onClick(View v)
    {
        if (v == mStatusTv)
        {
            if (isOpened)
            {
                operate("2");
            }
            else
            {
                operate("1");
            }
        }

    }


    @Override
    public void onResume()
    {
        super.onResume();

        if ("0".equals(ConfigManager.instance().getIsClose()))
        {
            isOpened = false;
            mStatusTv.setText("休息中");
            mStatusTv.setBackgroundResource(R.drawable.common_gray_5dp);
        }
        else
        {
            isOpened = true;
            mStatusTv.setText("营业中");
            mStatusTv.setBackgroundResource(R.drawable.common_yellow_5dp);
        }
    }

    private void operate(String operateType)
    {
        if (!NetWorkUtil.isConn(getActivity()))
        {
            NetWorkUtil.showNoNetWorkDlg(getActivity());
            return;
        }
        showProgressDialog(getActivity());
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("storeId", ConfigManager.instance().getUserID());
        valuePairs.put("operateType", operateType);
        Gson gson = new Gson();
        Map<String, String> postMap = new HashMap<>();
        postMap.put("json", gson.toJson(valuePairs));
        DataRequest.instance().request(getActivity(), Urls.getStoreOperateUrl(), this, HttpRequest.POST, STORE_OPERATE, postMap, new ResultHandler());
    }

    private void setOnFffLine(String operateType)
    {
        showProgressDialog(getActivity());
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("deliverUserId ", ConfigManager.instance().getUserID());
        valuePairs.put("operateType ", operateType);
        Gson gson = new Gson();
        Map<String, String> postMap = new HashMap<>();
        postMap.put("json", gson.toJson(valuePairs));
        //operateType  1-上线0-下线
        if ("1".equals(operateType))
        {
            DataRequest.instance().request(getActivity(), Urls.getOnOfflinUrl(), this, HttpRequest.POST, ONLINE_REQUEST, postMap, new ResultHandler
                    ());
        }
        else
        {
            DataRequest.instance().request(getActivity(), Urls.getOnOfflinUrl(), this, HttpRequest.POST, OFFLINE_REQUEST, postMap, new
                    ResultHandler());
        }

    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog(getActivity());

        if (ONLINE_REQUEST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(ONLINE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (OFFLINE_REQUEST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(OFF_LINE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (STORE_OPERATE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(STORE_OPERATE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }
}

package com.jyq.wm.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.jyq.wm.MyApplication;
import com.jyq.wm.R;
import com.jyq.wm.activity.OrderDetailActivity;
import com.jyq.wm.adapter.OrderAdapter2;
import com.jyq.wm.adapter.OrderAdapter3;
import com.jyq.wm.bean.OrderInfo;
import com.jyq.wm.http.DataRequest;
import com.jyq.wm.http.HttpRequest;
import com.jyq.wm.http.IRequestListener;
import com.jyq.wm.json.OrderListHandler;
import com.jyq.wm.json.ResultHandler;
import com.jyq.wm.listener.MyItemClickListener;
import com.jyq.wm.listener.MyOnClickListener;
import com.jyq.wm.utils.ConfigManager;
import com.jyq.wm.utils.ConstantUtil;
import com.jyq.wm.utils.NetWorkUtil;
import com.jyq.wm.utils.ToastUtil;
import com.jyq.wm.utils.Urls;
import com.jyq.wm.widget.VerticalSwipeRefreshLayout;
import com.jyq.wm.widget.list.refresh.PullToRefreshBase;
import com.jyq.wm.widget.list.refresh.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrderFragment3 extends BaseFragment implements IRequestListener, PullToRefreshBase.OnRefreshListener<RecyclerView>, SwipeRefreshLayout
        .OnRefreshListener
{
    @BindView(R.id.swipeRefreshLayout)
    VerticalSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.refreshRecyclerView)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;

    @BindView(R.id.ll_no_order)
    LinearLayout mNoOrderLayout;
    private View rootView = null;
    private Unbinder unbinder;
    private RecyclerView mRecyclerView;

    private int pn = 1;
    private int mRefreshStatus;

    private List<OrderInfo> orderInfoList = new ArrayList<>();

    private OrderAdapter3 mAdapter;

    private static final String PICK_UP_REQUEST = "pick_up_request";
    private static final String GET_ORDER_REQUEST = "get_order_request_9";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int ROB_ORDER_SUCCESS = 0x03;
    private static final int ROB_ORDER_FAIL = 0x04;
    private static final int GET_ORDER_LIST = 0x05;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {

                case REQUEST_SUCCESS:

                    OrderListHandler mOrderListHandler = (OrderListHandler) msg.obj;


                    List<OrderInfo> newOrderInfoList = mOrderListHandler.getOrderInfoList();

                    if (pn == 1)
                    {
                        orderInfoList.clear();
                    }
                    orderInfoList.addAll(newOrderInfoList);
                    mAdapter.notifyDataSetChanged();

                    if (orderInfoList.isEmpty())
                    {
                        mNoOrderLayout.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        mNoOrderLayout.setVisibility(View.GONE);
                    }


                    break;

                case REQUEST_FAIL:
                    if (orderInfoList.isEmpty())
                    {
                        mNoOrderLayout.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        mNoOrderLayout.setVisibility(View.GONE);
                    }


                    break;

                case ROB_ORDER_SUCCESS:

                    ToastUtil.show(getActivity(), "操作成功");
                    pn = 1;
                    mRefreshStatus = 0;
                    loadData();
                    break;

                case ROB_ORDER_FAIL:
                    ToastUtil.show(getActivity(), msg.obj.toString());
                    break;

                case GET_ORDER_LIST:
                    pn = 1;
                    mRefreshStatus = 0;
                    loadData();

                    mHandler.sendEmptyMessageDelayed(GET_ORDER_LIST, 30 * 1000);


                    break;


            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_order2, null);
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

    private static OrderFragment3 instance = null;

    public static OrderFragment3 newInstance()
    {
        if (instance == null)
        {
            instance = new OrderFragment3();
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
        mNoOrderLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showProgressDialog(getActivity());
                mHandler.sendEmptyMessage(GET_ORDER_LIST);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initViewData()
    {
        mPullToRefreshRecyclerView.setPullLoadEnabled(true);
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        mPullToRefreshRecyclerView.setOnRefreshListener(this);
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mAdapter = new OrderAdapter3(orderInfoList, getActivity(), new MyOnClickListener.OnClickCallBackListener()
        {
            @Override
            public void onSubmit(int p, int i)
            {
                if (i == 0)
                {
                    if ("0".equals(ConfigManager.instance().getIsClose()))
                    {
                        ToastUtil.show(getActivity(), "请先进行开店铺操作");
                    }
                    else
                    {
                        orederReminder(orderInfoList.get(p).getId());
                    }

                }
                else
                {
                    startActivity(new Intent(getActivity(), OrderDetailActivity.class).putExtra("ORDER_ID", orderInfoList.get(p).getId()));
                }


            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        mHandler.sendEmptyMessage(GET_ORDER_LIST);
    }


    private void loadData()
    {
        if (!NetWorkUtil.isConn(getActivity()))
        {
            hideProgressDialog(getActivity());
            if (mRefreshStatus == 1)
            {
                mPullToRefreshRecyclerView.onPullUpRefreshComplete();
            }
            else
            {
                mPullToRefreshRecyclerView.onPullDownRefreshComplete();
            }

//            NetWorkUtil.showNoNetWorkDlg(getActivity());
            ToastUtil.show(getActivity(),"请检查网络是否可用");
            return;
        }
        Map<String, Object> valuePairs = new HashMap<>();
        valuePairs.put("pageNum", pn);
        valuePairs.put("pageSize", 15);
        valuePairs.put("status", 9);
        valuePairs.put("storeId", ConfigManager.instance().getUserID());
        Gson gson = new Gson();
        Map<String, String> postMap = new HashMap<>();
        postMap.put("json", gson.toJson(valuePairs));
        DataRequest.instance().request(getActivity(), Urls.getOrderListUrl(), this, HttpRequest.POST, GET_ORDER_REQUEST, postMap, new OrderListHandler());
    }


    private void orederReminder(String orderId)
    {
        if (!NetWorkUtil.isConn(getActivity()))
        {
            NetWorkUtil.showNoNetWorkDlg(getActivity());
            return;
        }
        showProgressDialog(getActivity());
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("operateId", ConfigManager.instance().getUserID());
        valuePairs.put("orderId", orderId);
        valuePairs.put("type", "1");
        Gson gson = new Gson();
        Map<String, String> postMap = new HashMap<>();
        postMap.put("json", gson.toJson(valuePairs));
        DataRequest.instance().request(getActivity(), Urls.getOrederReminderUrl(), this, HttpRequest.POST, PICK_UP_REQUEST, postMap, new ResultHandler());
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            mHandler.sendEmptyMessage(GET_ORDER_LIST);
        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (null != unbinder)
        {
            unbinder.unbind();
            unbinder = null;
        }

        mHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog(getActivity());
        if (mRefreshStatus == 1)
        {
            mPullToRefreshRecyclerView.onPullUpRefreshComplete();
        }
        else
        {
            mPullToRefreshRecyclerView.onPullDownRefreshComplete();
        }

        if (GET_ORDER_REQUEST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (PICK_UP_REQUEST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(ROB_ORDER_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(ROB_ORDER_FAIL, resultMsg));
            }
        }

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        pn = 1;
        mRefreshStatus = 0;
        loadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView)
    {
        pn += 1;
        mRefreshStatus = 1;
        loadData();
    }

    @Override
    public void onRefresh()
    {
        if (mSwipeRefreshLayout != null)
        {
            pn = 1;
            mRefreshStatus = 0;
            loadData();
            mSwipeRefreshLayout.post(new Runnable()
            {
                @Override
                public void run()
                {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }
}

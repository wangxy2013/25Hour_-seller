package com.jyq.wm.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyq.wm.R;
import com.jyq.wm.http.DataRequest;
import com.jyq.wm.http.HttpRequest;
import com.jyq.wm.http.IRequestListener;
import com.jyq.wm.json.OrderDetailHandler;
import com.jyq.wm.utils.ConstantUtil;
import com.jyq.wm.utils.ToastUtil;
import com.jyq.wm.utils.Urls;
import com.jyq.wm.widget.MaxRecyclerView;
import com.jyq.wm.widget.statusbar.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_orderInterId)
    TextView tvOrderInterId;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_dateline)
    TextView tvDateline;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.rv_goods)
    MaxRecyclerView rvGoods;
    @BindView(R.id.tv_deposit)
    TextView tvDeposit;
    @BindView(R.id.tv_deliveryFee)
    TextView tvDeliveryFee;
    @BindView(R.id.tv_minusPrice)
    TextView tvMinusPrice;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_payType)
    TextView tvPayType;
    @BindView(R.id.tv_payStatus)
    TextView tvPayStatus;
    @BindView(R.id.tv_note)
    TextView tvNote;
    private String orderId;


    private static final String GET_ORDER_DETAIL = "get_order_detail";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(OrderDetailActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:

                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(OrderDetailActivity.this, msg.obj.toString());
                    break;


            }
        }
    };

    @Override
    protected void initData()
    {
        orderId = getIntent().getStringExtra("ORDER_ID");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_order_detail);
        StatusBarUtil.setStatusBarBackground(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(OrderDetailActivity.this, false);
    }

    @Override
    protected void initEvent()
    {

    }

    @Override
    protected void initViewData()
    {


        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("id", orderId);
        Gson gson = new Gson();
        Map<String, String> postMap = new HashMap<>();
        postMap.put("json", gson.toJson(valuePairs));
        DataRequest.instance().request(OrderDetailActivity.this, Urls.getOrederDetail(), this, HttpRequest.POST, GET_ORDER_DETAIL, postMap, new
                OrderDetailHandler());

    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_ORDER_DETAIL.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else
            {
                hideProgressDialog();
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

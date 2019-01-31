package com.jyq.seller.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyq.seller.R;
import com.jyq.seller.adapter.GoodsAdapter;
import com.jyq.seller.bean.DeliverSupply;
import com.jyq.seller.bean.GoodsInfo;
import com.jyq.seller.bean.OrderDetailInfo;
import com.jyq.seller.http.DataRequest;
import com.jyq.seller.http.HttpRequest;
import com.jyq.seller.http.IRequestListener;
import com.jyq.seller.json.OrderDetailHandler;
import com.jyq.seller.utils.ConstantUtil;
import com.jyq.seller.utils.NetWorkUtil;
import com.jyq.seller.utils.ToastUtil;
import com.jyq.seller.utils.Urls;
import com.jyq.seller.widget.FullyLinearLayoutManager;
import com.jyq.seller.widget.MaxRecyclerView;
import com.jyq.seller.widget.statusbar.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

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
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_store_address)
    TextView tvStoreAddress;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_store_phone)
    TextView tvStorePhone;
    @BindView(R.id.tv_qs_name)
    TextView tvQsName;
    @BindView(R.id.tv_qs_phone)
    TextView tvQsPhone;
    private String orderId;

    private GoodsAdapter mGoodsAdapter;
    private List<GoodsInfo> goodsInfoList = new ArrayList<>();
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
                    OrderDetailHandler mOrderDetailHandler = (OrderDetailHandler) msg.obj;
                    OrderDetailInfo mOrderDetailInfo = mOrderDetailHandler.getOrderDetailInfo();

                    if (null != mOrderDetailInfo)
                    {
                        DeliverSupply mDeliverSupply = mOrderDetailInfo.getDeliverSupply();
                        if (null != mDeliverSupply)
                        {
                            tvQsName.setText(mDeliverSupply.getName());
                            tvQsPhone.setText(mDeliverSupply.getPhone());
                        }

                        tvOrderInterId.setText(mOrderDetailInfo.getId());
                        tvName.setText(mOrderDetailInfo.getName());

                        String phone = mOrderDetailInfo.getPhone();

                        if (!TextUtils.isEmpty(phone) && phone.length() > 6)
                        {
                            // String phoneNumber = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})",   "$1****$2");
                            tvPhone.setText(phone);
                        }
                        else
                        {
                            tvPhone.setText(phone);
                        }

                        tvStorePhone.setText(mOrderDetailInfo.getStorePhone());
                        tvStoreName.setText(mOrderDetailInfo.getStoreName());
                        tvStoreAddress.setText(mOrderDetailInfo.getStoreAddress());
                        tvDateline.setText(mOrderDetailInfo.getSubmitOrderTime());
                        tvAddress.setText(mOrderDetailInfo.getAddress());
                        tvDeposit.setText("¥:" + mOrderDetailInfo.getDeposit());
                        tvDeliveryFee.setText("¥:" + mOrderDetailInfo.getDeliveryFee());
                        tvMinusPrice.setText("¥:" + mOrderDetailInfo.getMinusPrice());
                        tvTotalPrice.setText("¥:" + mOrderDetailInfo.getTotalPrice());
                        tvPrice.setText("¥:" + mOrderDetailInfo.getPrice());

                        if("online".equals(mOrderDetailInfo.getPayType()))
                        {
                            tvPayType.setText("微信支付");
                            tvPayType.setTextColor(ContextCompat.getColor(OrderDetailActivity.this,R.color.black));
                        }
                        else

                        {
                            tvPayType.setText("货到付款");
                            tvPayType.setTextColor(ContextCompat.getColor(OrderDetailActivity.this,R.color.redA));
                        }

                        tvPayStatus.setText(("1".equals(mOrderDetailInfo.getPayStatue())) ? "已支付" : "未支付");
                        tvNote.setText(mOrderDetailInfo.getNote());
                        goodsInfoList.clear();
                        goodsInfoList.addAll(mOrderDetailInfo.getGoodsInfoList());
                        mGoodsAdapter.notifyDataSetChanged();
                    }

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
        ivBack.setOnClickListener(this);
        tvQsPhone.setOnClickListener(this);
        tvStorePhone.setOnClickListener(this);
        tvPhone.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("订单详情");
        rvGoods.setLayoutManager(new FullyLinearLayoutManager(OrderDetailActivity.this));
        mGoodsAdapter = new GoodsAdapter(goodsInfoList, this);
        rvGoods.setAdapter(mGoodsAdapter);


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (!NetWorkUtil.isConn(this))
        {
            NetWorkUtil.showNoNetWorkDlg(this);
            return;
        }
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
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == tvQsPhone)
        {
            if (!TextUtils.isEmpty(tvQsPhone.getText().toString()))
            {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + tvQsPhone.getText().toString());
                intent.setData(data);
                startActivity(intent);

            }
        }
        else if (v == tvStorePhone)
        {
            if (!TextUtils.isEmpty(tvStorePhone.getText().toString()))
            {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + tvStorePhone.getText().toString());
                intent.setData(data);
                startActivity(intent);

            }
        }
        else if (v == tvPhone)
        {
            if (!TextUtils.isEmpty(tvPhone.getText().toString()))
            {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + tvPhone.getText().toString());
                intent.setData(data);
                startActivity(intent);

            }
        }


    }


}

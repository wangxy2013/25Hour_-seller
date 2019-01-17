package com.jyq.wm.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyq.wm.R;
import com.jyq.wm.activity.BNaviMainActivity;
import com.jyq.wm.bean.OrderInfo;
import com.jyq.wm.listener.MyItemClickListener;
import com.jyq.wm.listener.MyOnClickListener;
import com.jyq.wm.utils.DialogUtils;
import com.jyq.wm.utils.StringUtils;
import com.jyq.wm.utils.ToastUtil;


/**
 */
public class OrderHolder2 extends RecyclerView.ViewHolder
{
    private TextView mShopNameTv;
    private TextView mTimeTv;
    private TextView mNumberTv;
    private TextView mPhoneTv;
    private TextView mNameTv;
    private TextView mAddressTv;
    private TextView mGetTv;
    private TextView mPayStyleTv;
    private LinearLayout mItemLayout;
    private MyOnClickListener.OnClickCallBackListener listener;
    private Context context;


    public OrderHolder2(View rootView, Context context, MyOnClickListener.OnClickCallBackListener listener)
    {
        super(rootView);
        this.listener = listener;
        this.context = context;
        mNumberTv = (TextView) rootView.findViewById(R.id.tv_code);
        mShopNameTv = (TextView) rootView.findViewById(R.id.tv_shop_name);
        mTimeTv = (TextView) rootView.findViewById(R.id.tv_time);
        mPhoneTv = (TextView) rootView.findViewById(R.id.tv_customer_phone);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_customer_name);
        mAddressTv = (TextView) rootView.findViewById(R.id.tv_customer_address);
        mGetTv = (TextView) rootView.findViewById(R.id.tv_submit);
        mPayStyleTv = (TextView) rootView.findViewById(R.id.tv_pay_style);
        mItemLayout= (LinearLayout) rootView.findViewById(R.id.ll_item);
    }


    public void setOrderInfo(OrderInfo mOrderInfo, final int p)
    {

        mNumberTv.setText(mOrderInfo.getId());
        mShopNameTv.setText(mOrderInfo.getStoreName());
        mTimeTv.setText(mOrderInfo.getAddTime());
        mPhoneTv.setText("客户电话:" + mOrderInfo.getPhone());
        mNameTv.setText("客户姓名:" + mOrderInfo.getName());
        mAddressTv.setText("客户地址:" + mOrderInfo.getAddress());
        mPayStyleTv.setText("offline".equals(mOrderInfo.getPayType()) ? "货到付款" : "微信支付");
        if ("offline".equals(mOrderInfo.getPayType()))
        {
            mPayStyleTv.setTextColor(ContextCompat.getColor(context,R.color.redA));
        }
        else
        {
            mPayStyleTv.setTextColor(ContextCompat.getColor(context,R.color.green));
        }

        mGetTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onSubmit(p, 0);
            }
        });
        mItemLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onSubmit(p, 1);
            }
        });
    }


}

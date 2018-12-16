package com.jyq.wm.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyq.wm.R;
import com.jyq.wm.bean.OrderInfo;
import com.jyq.wm.listener.MyItemClickListener;
import com.jyq.wm.listener.MyOnClickListener;
import com.jyq.wm.utils.StringUtils;


/**
 */
public class OrderHolder1 extends RecyclerView.ViewHolder
{
    private TextView mShopNameTv;
    private TextView mTimeTv;
    private TextView mNumberTv;
    private TextView mPhoneTv;
    private TextView mNameTv;
    private TextView mAddressTv;
    private TextView mGetTv;
    private TextView mPayStyleTv;
    private TextView mRemainingTimeTv;
    private MyOnClickListener.OnClickCallBackListener listener;
    private Context context;


    public OrderHolder1(View rootView, Context context, MyOnClickListener.OnClickCallBackListener listener)
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
        mRemainingTimeTv = (TextView) rootView.findViewById(R.id.tv_remaining_time);
    }


    public void setOrderInfo(OrderInfo mOrderInfo, final int p)
    {

        mNumberTv.setText(mOrderInfo.getId());
        mShopNameTv.setText(mOrderInfo.getStoreName());
        mTimeTv.setText(mOrderInfo.getAddTime());
        mPhoneTv.setText("客户电话:" + mOrderInfo.getPhone());
        mNameTv.setText("客户姓名:" + mOrderInfo.getName());
        mAddressTv.setText("客户地址:" + mOrderInfo.getAddress());
        mPayStyleTv.setText("offline".endsWith(mOrderInfo.getPayType()) ? "货到付款" : "微信支付");

        String currentTime = StringUtils.getTimestamp();
        String addTime = mOrderInfo.getAddTime();
       // addTime = "2018-12-16 21:10:50";
        if (!StringUtils.stringIsEmpty(addTime))
        {
            int time = StringUtils.differentDaysByMillisecond(currentTime, StringUtils.getOrderEndTime(addTime));

            if (time <= 0)
            {
                mRemainingTimeTv.setText("订单已失效");
            }
            else
            {
                mRemainingTimeTv.setText(StringUtils.formatTime(time));
            }


            //mRemainingTimeTv.setText();

        }


        mGetTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onSubmit(p, 0);
            }
        });
    }


}

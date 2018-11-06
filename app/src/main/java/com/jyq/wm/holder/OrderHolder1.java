package com.jyq.wm.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyq.wm.R;
import com.jyq.wm.bean.OrderInfo;
import com.jyq.wm.listener.MyItemClickListener;


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
    private MyItemClickListener listener;
    private Context context;


    public OrderHolder1(View rootView, Context context, MyItemClickListener listener)
    {
        super(rootView);
        this.listener = listener;
        this.context = context;
        mNumberTv = (TextView) rootView.findViewById(R.id.tv_code);
        mShopNameTv = (TextView) rootView.findViewById(R.id.tv_shop_name);
        mTimeTv = (TextView) rootView.findViewById(R.id.tv_time);
        mPhoneTv = (TextView) rootView.findViewById(R.id.tv_customer_phone);
        mNameTv = (TextView) rootView.findViewById(R.id.tv_customer_name);
        mAddressTv= (TextView) rootView.findViewById(R.id.tv_customer_address);
        mGetTv= (TextView) rootView.findViewById(R.id.tv_get);
        mPayStyleTv= (TextView) rootView.findViewById(R.id.tv_pay_style);
    }


    public void setOrderInfo(OrderInfo mLiveInfo, final int p)
    {

        mGetTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, p);
            }
        });
    }


}

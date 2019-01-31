package com.jyq.seller.holder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyq.seller.R;
import com.jyq.seller.bean.OrderInfo;
import com.jyq.seller.listener.MyItemClickListener;


/**
 */
public class OrderHolder4 extends RecyclerView.ViewHolder
{
    private TextView mShopNameTv;
    private TextView mTimeTv;
    private TextView mNumberTv;
    private TextView mPhoneTv;
    private TextView mNameTv;
    private TextView mAddressTv;
    private TextView mPayStyleTv;
    private LinearLayout mItemLayout;
    private MyItemClickListener listener;
    private Context context;


    public OrderHolder4(View rootView, Context context, MyItemClickListener listener)
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
        mPayStyleTv = (TextView) rootView.findViewById(R.id.tv_pay_style);
        mItemLayout= (LinearLayout) rootView.findViewById(R.id.ll_item);
    }


    public void setOrderInfo(final OrderInfo mOrderInfo, final int p)
    {

        mNumberTv.setText(mOrderInfo.getId());
        mShopNameTv.setText(mOrderInfo.getStoreName());
        mTimeTv.setText(mOrderInfo.getAddTime());
        mPhoneTv.setText("客户电话:" + mOrderInfo.getPhone());
        mNameTv.setText("客户姓名:" + mOrderInfo.getName());
        mAddressTv.setText("客户地址:"+mOrderInfo.getAddress());
        mPayStyleTv.setText("offline".equals(mOrderInfo.getPayType()) ? "货到付款" : "微信支付");
        if ("offline".equals(mOrderInfo.getPayType()))
        {
            mPayStyleTv.setTextColor(ContextCompat.getColor(context,R.color.redA));
        }
        else
        {
            mPayStyleTv.setTextColor(ContextCompat.getColor(context,R.color.green));
        }
        mItemLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onItemClick(v, p);
            }
        });

        mPhoneTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!TextUtils.isEmpty(mOrderInfo.getPhone()))
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + mOrderInfo.getPhone());
                    intent.setData(data);
                    context. startActivity(intent);

                }
            }
        });
//        mNavigationTv.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if (mOrderInfo.getStoreLnt() == 0)
//                {
//                    ToastUtil.show(context, "客户坐标缺失！");
//                }
//                else
//                {
//                    Intent mItent = new Intent(context, BNaviMainActivity.class);
//                    mItent.putExtra("endLnt", mOrderInfo.getUserLnt());
//                    mItent.putExtra("endLat", mOrderInfo.getUserLat());
//                    context.startActivity(mItent);
//                }
//
//            }
//        });
    }


}

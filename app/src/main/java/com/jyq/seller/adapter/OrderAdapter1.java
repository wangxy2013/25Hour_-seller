package com.jyq.seller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyq.seller.R;
import com.jyq.seller.bean.OrderInfo;
import com.jyq.seller.holder.OrderHolder1;
import com.jyq.seller.listener.MyOnClickListener;

import java.util.List;

/**
 */
public class OrderAdapter1 extends RecyclerView.Adapter<OrderHolder1>
{

    private  MyOnClickListener.OnClickCallBackListener listener;
    private List<OrderInfo> list;
    private Context mContext;

    public OrderAdapter1(List<OrderInfo> list, Context mContext,  MyOnClickListener.OnClickCallBackListener listener)
    {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public OrderHolder1 onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order1, parent, false);
        OrderHolder1 mHolder = new OrderHolder1(itemView, parent.getContext(), listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(OrderHolder1 holder, int position)
    {
        OrderInfo mOrderInfo = list.get(position);
        holder.setOrderInfo(mOrderInfo, position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}

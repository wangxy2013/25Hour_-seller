package com.jyq.wm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyq.wm.R;
import com.jyq.wm.bean.OrderInfo;
import com.jyq.wm.holder.OrderHolder2;
import com.jyq.wm.holder.OrderHolder3;
import com.jyq.wm.listener.MyItemClickListener;
import com.jyq.wm.listener.MyOnClickListener;

import java.util.List;

/**
 * 配送列表
 */
public class OrderAdapter3 extends RecyclerView.Adapter<OrderHolder3>
{
    private  MyOnClickListener.OnClickCallBackListener listener;
    private List<OrderInfo> list;
    private Context mContext;

    public OrderAdapter3(List<OrderInfo> list, Context mContext,  MyOnClickListener.OnClickCallBackListener listener)
    {
        this.list = list;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public OrderHolder3 onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order3, parent, false);
        OrderHolder3 mHolder = new OrderHolder3(itemView, parent.getContext(), listener);
        return mHolder;
    }


    @Override
    public void onBindViewHolder(OrderHolder3 holder, int position)
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

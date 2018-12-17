package com.jyq.wm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyq.wm.R;
import com.jyq.wm.bean.GoodsInfo;
import com.jyq.wm.bean.OrderInfo;
import com.jyq.wm.holder.GoodsHolder;
import com.jyq.wm.holder.OrderHolder1;
import com.jyq.wm.listener.MyOnClickListener;

import java.util.List;

/**
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsHolder>
{

    private List<GoodsInfo> list;
    private Context mContext;

    public GoodsAdapter(List<GoodsInfo> list, Context mContext)
    {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public GoodsHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
        GoodsHolder mHolder = new GoodsHolder(itemView, parent.getContext());
        return mHolder;
    }


    @Override
    public void onBindViewHolder(GoodsHolder holder, int position)
    {
        GoodsInfo mGoodsInfo = list.get(position);
        holder.setGoodsInfo(mGoodsInfo,list.size(),position);
    }

    @Override
    public int getItemCount()
    {

        return list.size();


    }
}

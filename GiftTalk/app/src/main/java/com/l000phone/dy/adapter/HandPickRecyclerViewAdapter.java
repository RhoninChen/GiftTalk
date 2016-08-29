package com.l000phone.dy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.l000phone.dy.bean.HandPickRecyclerViewInfo;
import com.l000phone.dy.dygifttalk.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HP on 2016/7/12.
 */
public class HandPickRecyclerViewAdapter extends RecyclerView.Adapter<HandPickRecyclerViewAdapter.ViewHolder> {
    private List<HandPickRecyclerViewInfo.DataEntity.SecondaryBannersEntity> recyclerViewList;
    private Context context;
    private LayoutInflater layoutInflater;

    public HandPickRecyclerViewAdapter(List<HandPickRecyclerViewInfo.DataEntity.SecondaryBannersEntity> recyclerViewList, Context context) {
        this.recyclerViewList = recyclerViewList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public HandPickRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.hand_pick_recycler_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(context).load(recyclerViewList.get(position).getImage_url()).into(holder.ivIcon);
    }

    @Override
    public int getItemCount() {
        return recyclerViewList == null ? 0 : recyclerViewList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_hand_pick_recycler_item)
        RoundedImageView ivIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

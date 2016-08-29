package com.l000phone.dy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.l000phone.dy.bean.HomeOthersListViewInfo;
import com.l000phone.dy.dygifttalk.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HP on 2016/7/12.
 */
public class HomeOthersListViewAdapter extends BaseAdapter {
    private Context context;
    private List<HomeOthersListViewInfo.DataEntity.ItemsEntity> itemsEntityList;
    private LayoutInflater inflater;

    public HomeOthersListViewAdapter(Context context, List<HomeOthersListViewInfo.DataEntity
            .ItemsEntity> itemsEntityList) {
        this.context = context;
        this.itemsEntityList = itemsEntityList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemsEntityList == null ? 0 : itemsEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsEntityList == null ? null : itemsEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.hand_pick_listview_child_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        HomeOthersListViewInfo.DataEntity.ItemsEntity itemsEntity = itemsEntityList.get(position);
        viewHolder.tvLikeCount.setText(itemsEntity.getLikes_count() + "");
        viewHolder.tvTitle.setText(itemsEntity.getTitle());
        Picasso.with(context).load(itemsEntity.getCover_image_url()).into(viewHolder.rivIcon);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.riv_hand_pick_list_view_child_item)
        RoundedImageView rivIcon;
        @BindView(R.id.tv_hand_pick_list_view_child_item_count)
        TextView tvLikeCount;
        @BindView(R.id.tv_hand_pick_list_view_child_item)
        TextView tvTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

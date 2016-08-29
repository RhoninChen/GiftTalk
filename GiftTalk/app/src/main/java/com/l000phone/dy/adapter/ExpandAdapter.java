package com.l000phone.dy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.l000phone.dy.bean.HandPickContentProductInfo;
import com.l000phone.dy.dygifttalk.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HP on 2016/7/12.
 */
public class ExpandAdapter extends BaseExpandableListAdapter {
    private Context context;
    private Map<String, List<HandPickContentProductInfo.DataEntity.ItemsEntity>> itemsMap;
    private List<String> groupTitleList;
    private LayoutInflater inflater;

    public ExpandAdapter(Context context, Map<String, List<HandPickContentProductInfo.DataEntity
            .ItemsEntity>> itemsMap, List<String> groupTitleList) {
        this.context = context;
        this.itemsMap = itemsMap;
        this.groupTitleList = groupTitleList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return groupTitleList == null ? 0 : groupTitleList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupTitleList != null && itemsMap != null && groupTitleList.size() > groupPosition
                && itemsMap.get(groupTitleList.get(groupPosition)) != null) {
            return itemsMap.get(groupTitleList.get(groupPosition)).size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupTitleList == null ? null : groupTitleList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupTitleList != null && itemsMap != null && groupTitleList.size() > groupPosition
                && itemsMap.get(groupTitleList.get(groupPosition)) != null) {
            return itemsMap.get(groupTitleList.get(groupPosition)).get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup
            parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.hand_pick_listview_group_item, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvGroupItem.setText(groupTitleList.get(groupPosition));
        return convertView;
    }

    class GroupViewHolder {
        @BindView(R.id.tv_hand_pick_list_view_group_item)
        TextView tvGroupItem;

        public GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView != null) {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.hand_pick_listview_child_item, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        }
        HandPickContentProductInfo.DataEntity.ItemsEntity item = itemsMap.get(groupTitleList.get
                (groupPosition)).get(childPosition);

        childViewHolder.tvLikeCount.setText(item.getLikes_count() + "");
        childViewHolder.tvTitle.setText(item.getTitle());
        Picasso.with(context).load(item.getCover_image_url()).into(childViewHolder.rivIcon);
        return convertView;
    }

    class ChildViewHolder {
        @BindView(R.id.riv_hand_pick_list_view_child_item)
        RoundedImageView rivIcon;
        @BindView(R.id.tv_hand_pick_list_view_child_item_count)
        TextView tvLikeCount;
        @BindView(R.id.tv_hand_pick_list_view_child_item)
        TextView tvTitle;

        public ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * ExpandableListView 如果子条目需要响应click事件,必需返回true
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

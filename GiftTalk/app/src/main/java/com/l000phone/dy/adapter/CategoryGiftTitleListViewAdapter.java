package com.l000phone.dy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.l000phone.dy.bean.CategoryGiftInfo;
import com.l000phone.dy.dygifttalk.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HP on 2016/7/11.
 */
public class CategoryGiftTitleListViewAdapter extends BaseAdapter {
    private List<CategoryGiftInfo.DataEntity.CategoriesEntity> categoriesEntityList;
    private Context context;
    private LayoutInflater layoutInflater;

    private int selectPosition;//被选中的位置

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public CategoryGiftTitleListViewAdapter(List<CategoryGiftInfo.DataEntity.CategoriesEntity> categoriesEntityList, Context context) {
        this.categoriesEntityList = categoriesEntityList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categoriesEntityList == null ? 0 : categoriesEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoriesEntityList == null ? null : categoriesEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = layoutInflater.inflate(R.layout.category_gift_listview_title_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        if (selectPosition == position) {
            convertView.setBackgroundColor(Color.WHITE);
            holder.tvChecked.setVisibility(View.VISIBLE);
            holder.tvChecked.setText(categoriesEntityList.get(position).getName());
            holder.tvUnchecked.setVisibility(View.GONE);
        } else {
            convertView.setBackgroundColor(0xfff2f2f2);
            holder.tvChecked.setVisibility(View.GONE);
            holder.tvUnchecked.setVisibility(View.VISIBLE);
            holder.tvUnchecked.setText(categoriesEntityList.get(position).getName());
        }

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.tv_category_gift_title_item_checked)
        TextView tvChecked;
        @BindView(R.id.tv_category_gift_title_item_unchecked)
        TextView tvUnchecked;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

package com.l000phone.dy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.l000phone.dy.bean.CategoryGiftInfo;
import com.l000phone.dy.customview.MeasureGridView;
import com.l000phone.dy.dygifttalk.CategoryGiftDetailActivity;
import com.l000phone.dy.dygifttalk.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HP on 2016/7/11.
 */
public class CategoryGiftDetailListViewAdapter extends BaseAdapter {
    private Context context;
    private List<CategoryGiftInfo.DataEntity.CategoriesEntity> categoriesEntityList;
    private final LayoutInflater inflater;

    public CategoryGiftDetailListViewAdapter(Context context, List<CategoryGiftInfo.DataEntity.CategoriesEntity> categoriesEntityList) {
        this.context = context;
        this.categoriesEntityList = categoriesEntityList;
        inflater = LayoutInflater.from(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView != null){
            holder = (ViewHolder) convertView.getTag();
        }else {
            convertView = inflater.inflate(R.layout.category_gift_listview_detail_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.tvDetailItem.setText(categoriesEntityList.get(position).getName());

        CategoryGiftRightGridViewAdapter categoryGiftRightGridViewAdapter = new CategoryGiftRightGridViewAdapter(context, categoriesEntityList.get(position).getSubcategories());
        holder.gvDetailItem.setAdapter(categoryGiftRightGridViewAdapter);
        holder.gvDetailItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                Intent intent = new Intent(context, CategoryGiftDetailActivity.class);
                intent.putExtra("id", categoriesEntityList.get(position).getSubcategories().get(position2).getId() + "");
                intent.putExtra("name", categoriesEntityList.get(position).getSubcategories().get(position2).getName());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_category_gift_lv_detail_item)
        TextView tvDetailItem;
        @BindView(R.id.gv_category_gift_detail)
        MeasureGridView gvDetailItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

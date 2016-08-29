package com.l000phone.dy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.l000phone.dy.bean.CategoryGiftInfo;
import com.l000phone.dy.dygifttalk.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HP on 2016/7/10.
 */
public class CategoryGiftRightGridViewAdapter extends BaseAdapter {

    private List<CategoryGiftInfo.DataEntity.CategoriesEntity.SubcategoriesEntity> subcategoriesEntityList;
    private Context context;
    private LayoutInflater layoutInflater;

    public CategoryGiftRightGridViewAdapter(Context context, List<CategoryGiftInfo.DataEntity.CategoriesEntity.SubcategoriesEntity> subcategoriesEntityList) {
        this.context = context;
        this.subcategoriesEntityList = subcategoriesEntityList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return subcategoriesEntityList == null ? 0 : subcategoriesEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return subcategoriesEntityList == null ? null : subcategoriesEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView != null){
            holder = (ViewHolder) convertView.getTag();
        }else {
            convertView = layoutInflater.inflate(R.layout.category_gift_right_grid_view_item , null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.tvName.setText(subcategoriesEntityList.get(position).getName());

        Picasso.with(context).load(subcategoriesEntityList.get(position).getIcon_url()).into(holder.ivIcon);

        return convertView;
    }

    class ViewHolder{

        @BindView(R.id.tv_gv_category_gift_right_name)
        TextView tvName;
        @BindView(R.id.iv_gv_category_gift_right_icon)
        ImageView ivIcon;

        public ViewHolder(View view) {
            ButterKnife.bind(this , view);
        }
    }
}

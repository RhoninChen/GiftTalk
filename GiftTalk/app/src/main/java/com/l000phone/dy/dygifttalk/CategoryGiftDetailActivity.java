package com.l000phone.dy.dygifttalk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.l000phone.dy.adapter.CategoryGiftDetailAdapter;
import com.l000phone.dy.bean.CategoryGiftDetailInfo;
import com.l000phone.dy.common.UrlConfig;
import com.l000phone.dy.interf.IOkCallBack;
import com.l000phone.dy.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HP on 2016/7/11
 */
public class CategoryGiftDetailActivity extends BaseActivity {

    @BindView(R.id.gv_category_gift_click_detail)
    GridView mGridView;
    private List<CategoryGiftDetailInfo.DataEntity.ItemsEntity> dataEntity1List = new ArrayList<>();
    private CategoryGiftDetailAdapter gridViewAdapter;

    @BindView(R.id.ib_category_gift_back)
    ImageButton ibBack;
    @BindView(R.id.ib_ib_category_gift_sort)
    ImageButton ibSort;
    @BindView(R.id.tv_category_gift_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_gift_detail);
        ButterKnife.bind(this);

        gridViewAdapter = new CategoryGiftDetailAdapter(this, dataEntity1List);
        mGridView.setAdapter(gridViewAdapter);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        tvTitle.setText(name);
        String url = UrlConfig.CATEGORY_GIFT_DETAIL_URL_1 + id + UrlConfig.CATEGORY_GIFT_DETAIL_URL_2;

        Log.d("heyang3", "onCreate: url----------->" + url);
        OkHttpUtils.newInstance().doGet(url, CategoryGiftDetailInfo.class, new IOkCallBack<CategoryGiftDetailInfo>() {
            @Override
            public void onSuccess(CategoryGiftDetailInfo resultInfo, int requestCode) {
                List<CategoryGiftDetailInfo.DataEntity.ItemsEntity> items = resultInfo.getData().getItems();
                dataEntity1List.addAll(items);
                gridViewAdapter.notifyDataSetChanged();
            }
        },1);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategoryGiftDetailActivity.this, SelectDetailActivity.class);
                intent.putExtra("id", dataEntity1List.get(position).getId() + "");
                startActivity(intent);
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

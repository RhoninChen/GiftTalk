package com.l000phone.dy.dygifttalk;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.l000phone.dy.adapter.CategoryGiftDetailAdapter;
import com.l000phone.dy.adapter.PopupWindowGridViewAdapter;
import com.l000phone.dy.bean.CategoryGiftDetailInfo;
import com.l000phone.dy.bean.SelectGiftPopupWindowInfo;
import com.l000phone.dy.common.UrlConfig;
import com.l000phone.dy.interf.IOkCallBack;
import com.l000phone.dy.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectGiftGodActivity extends BaseActivity {

    @BindView(R.id.gv_select_gift_god)
    GridView mGridView;
    private List<CategoryGiftDetailInfo.DataEntity.ItemsEntity> dataEntity1List =
            new ArrayList<>();
    private CategoryGiftDetailAdapter gridViewAdapter;

    @BindView(R.id.ib_select_gift_back)
    ImageButton ibBack;
    private PopupWindow popupWindow;
    private List<String> targetList = new ArrayList<>();
    private List<String> sceneList = new ArrayList<>();
    private List<String> personalityList = new ArrayList<>();
    private List<String> priceList = new ArrayList<>();
    private PopupWindowGridViewAdapter popupWindowGridViewAdapter;
    private GridView mPopupGridView1;
    private GridView mPopupGridView2;
    private GridView mPopupGridView3;
    private GridView mPopupGridView4;
    private Map<String, String> paramMap = new HashMap<>();
    private List<SelectGiftPopupWindowInfo.DataEntity.FiltersEntity> filtersEntityList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_gift_god);
        ButterKnife.bind(this);

        gridViewAdapter = new CategoryGiftDetailAdapter(this, dataEntity1List);
        mGridView.setAdapter(gridViewAdapter);

        OkHttpUtils.newInstance().doGet(UrlConfig.SELECT_GIFT_GOD_URL, CategoryGiftDetailInfo.class
                , new IOkCallBack<CategoryGiftDetailInfo>() {
                    @Override
                    public void onSuccess(CategoryGiftDetailInfo resultInfo, int requestCode) {
                        List<CategoryGiftDetailInfo.DataEntity.ItemsEntity> items = resultInfo.getData().getItems();
                        dataEntity1List.addAll(items);
                        gridViewAdapter.notifyDataSetChanged();
                    }
                }, 1);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectGiftGodActivity.this, SelectDetailActivity.class);
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void click(View view) {

        View popView = LayoutInflater.from(this).inflate(R.layout.popup_window_select_gift, null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(false);
        popupWindow.showAsDropDown(view, 0, 0, Gravity.CENTER);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.setFocusable(true);
                return false;
            }
        });
        Button btn = (Button) view;
        final String url = "http://api.liwushuo.com/v2/search/item_by_type?limit=20&offset=0";
        switch (view.getId()) {
            case R.id.btn_select_gift_god_target:
                mPopupGridView1 = (GridView) popView.findViewById(R.id.mgv_popup_window_select_gift);
                popupWindowGridViewAdapter = new PopupWindowGridViewAdapter(this, targetList);
                mPopupGridView1.setAdapter(popupWindowGridViewAdapter);
                notifyUI(mPopupGridView1, url, btn);
                break;
            case R.id.btn_select_gift_god_scene:
                mPopupGridView2 = (GridView) popView.findViewById(R.id.mgv_popup_window_select_gift);
                popupWindowGridViewAdapter = new PopupWindowGridViewAdapter(this, sceneList);
                mPopupGridView2.setAdapter(popupWindowGridViewAdapter);
//                notifyUI(mPopupGridView2, url);
                break;
            case R.id.btn_select_gift_god_personality:
                mPopupGridView3 = (GridView) popView.findViewById(R.id.mgv_popup_window_select_gift);
                popupWindowGridViewAdapter = new PopupWindowGridViewAdapter(this, personalityList);
                mPopupGridView3.setAdapter(popupWindowGridViewAdapter);
//                notifyUI(mPopupGridView3, url);
                break;
            case R.id.btn_select_gift_god_price:
                mPopupGridView4 = (GridView) popView.findViewById(R.id.mgv_popup_window_select_gift);
                popupWindowGridViewAdapter = new PopupWindowGridViewAdapter(this, priceList);
                mPopupGridView4.setAdapter(popupWindowGridViewAdapter);
//                notifyUI(mPopupGridView4, url);
                break;
        }


        OkHttpUtils.newInstance().doGet(UrlConfig.POPUP_WINDOW_URL, SelectGiftPopupWindowInfo.class
                , new IOkCallBack<SelectGiftPopupWindowInfo>() {
                    @Override
                    public void onSuccess(SelectGiftPopupWindowInfo resultInfo, int requestCode) {
                        filtersEntityList = resultInfo.getData().getFilters();
                        for (int i = 0; i < filtersEntityList.size(); i++) {
                            List<SelectGiftPopupWindowInfo.DataEntity.FiltersEntity.ChannelsEntity> channels = filtersEntityList.get(i).getChannels();
                            switch (filtersEntityList.get(i).getKey()) {
                                case "target":
                                    targetList.clear();
                                    targetList.add("全部");
                                    for (int j = 0; j < channels.size(); j++) {
                                        targetList.add(channels.get(j).getName());
                                        Log.d("heyang5", "onSuccess: ----------------->" + channels.get(j).getName());
                                    }
                                    popupWindowGridViewAdapter.notifyDataSetChanged();
                                    break;
                                case "scene":
                                    sceneList.clear();
                                    sceneList.add("全部");
                                    for (int j = 0; j < channels.size(); j++) {
                                        sceneList.add(channels.get(j).getName());
                                    }
                                    popupWindowGridViewAdapter.notifyDataSetChanged();
                                    break;
                                case "personality":
                                    personalityList.clear();
                                    personalityList.add("全部");
                                    for (int j = 0; j < channels.size(); j++) {
                                        personalityList.add(channels.get(j).getName());
                                    }
                                    popupWindowGridViewAdapter.notifyDataSetChanged();
                                    break;
                                case "price":
                                    priceList.clear();
                                    priceList.add("全部");
                                    for (int j = 0; j < channels.size(); j++) {
                                        priceList.add(channels.get(j).getName());
                                    }
                                    popupWindowGridViewAdapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                    }
                }, 1);
    }

    private void notifyUI(GridView mPopupGridView, final String url, final Button btn) {
        mPopupGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StringBuffer sb = new StringBuffer();
                popupWindow.dismiss();
                if (position != 0) {
                    paramMap.put("target", filtersEntityList.get(0).getChannels().get(position - 1).getId() + "");
                    sb.append(url).append("&").append("target").append("=").append(paramMap.get("target"));
                    btn.setText(filtersEntityList.get(0).getChannels().get(position - 1).getName());
                } else {
                    btn.setText("全部");
                    sb.append(url);
                }

                OkHttpUtils.newInstance().doGet(sb.toString(), CategoryGiftDetailInfo.class
                        , new IOkCallBack<CategoryGiftDetailInfo>() {
                            @Override
                            public void onSuccess(CategoryGiftDetailInfo resultInfo, int requestCode) {
                                List<CategoryGiftDetailInfo.DataEntity.ItemsEntity> items = resultInfo.getData().getItems();
                                dataEntity1List.clear();
                                dataEntity1List.addAll(items);
                                gridViewAdapter.notifyDataSetChanged();
                            }
                        }, 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

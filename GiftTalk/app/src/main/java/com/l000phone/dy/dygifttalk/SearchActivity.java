package com.l000phone.dy.dygifttalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.l000phone.dy.adapter.CategoryFragmentPagerAdapter;
import com.l000phone.dy.bean.SearchFlowLayoutInfo;
import com.l000phone.dy.common.UrlConfig;
import com.l000phone.dy.customview.FlowLayout;
import com.l000phone.dy.fragment.SearchGiftFragment;
import com.l000phone.dy.fragment.SearchStrategyFragment;
import com.l000phone.dy.interf.IOkCallBack;
import com.l000phone.dy.utils.OkHttpUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.ib_search_back)
    ImageButton ibBack;
    @BindView(R.id.et_search)
    EditText etSearchContent;
    @BindView(R.id.btn_search)
    Button btnSearch;

    @BindView(R.id.ll_search_flow)
    LinearLayout llSearchFlow;

    @BindView(R.id.ll_search_content)
    LinearLayout llSearchContent;
    @BindView(R.id.vp_search_content)
    ViewPager vpSearchContent;
    private List<Fragment> fragmentList = new ArrayList<>();
    private CategoryFragmentPagerAdapter fragmentPagerAdapter;
    private SearchStrategyFragment searchStrategyFragment;
    private SearchGiftFragment searchGiftFragment;

    @BindView(R.id.rg_search)
    RadioGroup mRadioGroup;

    @BindView(R.id.tv_search_gift_line)
    TextView tvGiftLine;
    @BindView(R.id.tv_search_strategy_line)
    TextView tvStrategyLine;

    @BindView(R.id.btn_search_gift_god)
    Button btnGiftGod;

    @BindView(R.id.fl_search_gift_god)
    FlowLayout flowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentList.clear();
                try {
                    String searchContent = etSearchContent.getEditableText().toString();
                    if (searchContent.equals("")) {
                        Toast.makeText(SearchActivity.this, "请输入关键字进行搜索", Toast.LENGTH_SHORT).show();
                    } else {
                        // 这句不能忘了
                        String encode = URLEncoder.encode(searchContent, "UTF-8");
                        String giftUrl = UrlConfig.SEARCH_GIFT_URL + encode;
                        String strategyUrl = UrlConfig.SEARCH_STRATEGY_URL + encode;

                        llSearchContent.setVisibility(View.VISIBLE);
                        llSearchFlow.setVisibility(View.GONE);

                        searchGiftFragment = SearchGiftFragment.newInstance(giftUrl, null);
                        searchStrategyFragment = SearchStrategyFragment.newInstance(strategyUrl, null);
                        fragmentList.add(searchGiftFragment);
                        fragmentList.add(searchStrategyFragment);
//                        fragmentPagerAdapter.notifyDataSetChanged();
                        fragmentPagerAdapter = new CategoryFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
                        vpSearchContent.setAdapter(fragmentPagerAdapter);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        etSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    llSearchContent.setVisibility(View.GONE);
                    llSearchFlow.setVisibility(View.VISIBLE);
//                    fragmentList.clear();
//                    fragmentPagerAdapter.notifyDataSetChanged();
//                    searchStrategyFragment.onDestroy();
//                    searchGiftFragment.onDestroy();

                }
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_search_gift:
                        tvGiftLine.setVisibility(View.VISIBLE);
                        tvStrategyLine.setVisibility(View.INVISIBLE);
                        vpSearchContent.setCurrentItem(0);
                        break;
                    case R.id.rb_search_strategy:
                        tvStrategyLine.setVisibility(View.VISIBLE);
                        tvGiftLine.setVisibility(View.INVISIBLE);
                        vpSearchContent.setCurrentItem(1);
                        break;
                }
            }
        });

//        fragmentPagerAdapter = new CategoryFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
//        vpSearchContent.setAdapter(fragmentPagerAdapter);
        vpSearchContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
                        break;
                    case 1:
                        ((RadioButton) mRadioGroup.getChildAt(2)).setChecked(true);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnGiftGod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, SelectGiftGodActivity.class);
                startActivity(intent);
            }
        });

        OkHttpUtils.newInstance().doGet(UrlConfig.SEARCH_FLOW_LAYOUT_URL, SearchFlowLayoutInfo.class, new IOkCallBack<SearchFlowLayoutInfo>() {
            @Override
            public void onSuccess(SearchFlowLayoutInfo resultInfo, int requestCode) {
                List<String> hot_words = resultInfo.getData().getHot_words();
                for (int i = 0; i < hot_words.size(); i++) {
                    final TextView textView = new TextView(SearchActivity.this);
                    textView.setText(hot_words.get(i));
                    textView.setBackgroundResource(R.drawable.popup_window_item_text_view);
                    textView.setPadding(50, 20, 50, 20);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etSearchContent.setText(textView.getText());
                            String encode = null;
                            try {
                                encode = URLEncoder.encode(textView.getText().toString(), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            String giftUrl = UrlConfig.SEARCH_GIFT_URL + encode;
                            String strategyUrl = UrlConfig.SEARCH_STRATEGY_URL + encode;

                            llSearchContent.setVisibility(View.VISIBLE);
                            llSearchFlow.setVisibility(View.GONE);
                            // 注意要先清除数据源
                            fragmentList.clear();
                            searchGiftFragment = SearchGiftFragment.newInstance(giftUrl, null);
                            searchStrategyFragment = SearchStrategyFragment.newInstance(strategyUrl, null);
                            fragmentList.add(searchGiftFragment);
                            fragmentList.add(searchStrategyFragment);
//                            fragmentPagerAdapter.notifyDataSetChanged();
                            fragmentPagerAdapter = new CategoryFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
                            vpSearchContent.setAdapter(fragmentPagerAdapter);
                        }
                    });
                    flowLayout.addView(textView);
                }
            }
        },1);
    }

    @Override
    protected void onDestroy() {
        Log.d("Kevin","SearchActivity == onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

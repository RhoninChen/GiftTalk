package com.l000phone.dy.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.l000phone.dy.adapter.ExpandAdapter;
import com.l000phone.dy.adapter.HandPickRecyclerViewAdapter;
import com.l000phone.dy.bean.HandPickContentProductInfo;
import com.l000phone.dy.bean.HandPickRecyclerViewInfo;
import com.l000phone.dy.bean.HandPickViewPagerProductInfo;
import com.l000phone.dy.common.UrlConfig;
import com.l000phone.dy.dygifttalk.R;
import com.l000phone.dy.dygifttalk.WebActivity;
import com.l000phone.dy.interf.IOkCallBack;
import com.l000phone.dy.utils.DateFormatUtils;
import com.l000phone.dy.utils.OkHttpUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 指南页第一屏精选页
 */
public class HandPickFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.elv_fg_hand_pick)
    PullToRefreshExpandableListView mRefreshExpandableListView;
    private ExpandableListView mExpandableListView;
    private HearderViewHolder hearderViewHolder;

    private List<HandPickRecyclerViewInfo.DataEntity.SecondaryBannersEntity> recyclerViewList;
    private List<String> mViewPagerUrlList;
    private HandPickRecyclerViewAdapter handPickRecyclerViewAdapter;

    //分组数据
    private Map<String, List<HandPickContentProductInfo.DataEntity.ItemsEntity>> itemsMap;
    private List<String> groupTitleList;
    private ExpandAdapter expandAdapter;
    private List<HandPickContentProductInfo.DataEntity.PagingEntity> pageEntityList;//存储下一页的url

    public HandPickFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HandPickFragment.
     */
    public static HandPickFragment newInstance(String param1, String param2) {
        HandPickFragment fragment = new HandPickFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hand_pick, container, false);
        ButterKnife.bind(this, view);

        mExpandableListView = mRefreshExpandableListView.getRefreshableView();
        addHeaderView();//为mExpandableListView添加头部

        aboutExpandableListView();//关于ExpandableListView的操作
        aboutPullToRefresh();//关于下拉刷新的操作
        return view;
    }

    /**
     * 关于下拉刷新的操作
     */
    private void aboutPullToRefresh() {
        mRefreshExpandableListView.setMode(PullToRefreshBase.Mode.BOTH);
        mRefreshExpandableListView.setOnRefreshListener(new PullToRefreshBase
                .OnRefreshListener2<ExpandableListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshExpandableListView.onRefreshComplete();
                    }
                }, 2000);

                OkHttpUtils.newInstance().doGet(UrlConfig.HAND_PICK_LIST_VIEW,
                        HandPickContentProductInfo.class, new
                                IOkCallBack<HandPickContentProductInfo>() {

                                    @Override
                                    public void onSuccess(HandPickContentProductInfo resultInfo,
                                                          int requestCode) {
                                        List<HandPickContentProductInfo.DataEntity.ItemsEntity>
                                                itemsEntityList =
                                                resultInfo.getData().getItems();
                                        pageEntityList.add(resultInfo.getData().getPaging());
                                        // 此步骤不省
                                        groupTitleList.clear();
                                        itemsMap.clear();
                                        updateExpandListViewData(itemsEntityList);
                                    }
                                }, 4);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                OkHttpUtils.newInstance().doGet(pageEntityList.get(pageEntityList.size() - 1)
                        .getNext_url(), HandPickContentProductInfo.class, new
                        IOkCallBack<HandPickContentProductInfo>() {

                            @Override
                            public void onSuccess(HandPickContentProductInfo resultInfo, int
                                    requestCode) {
                                List<HandPickContentProductInfo.DataEntity.ItemsEntity>
                                        itemsEntityList =
                                        resultInfo.getData().getItems();
                                pageEntityList.add(resultInfo.getData().getPaging());
                                updateExpandListViewData(itemsEntityList);
                                mRefreshExpandableListView.onRefreshComplete();
                            }
                        }, 5);
            }
        });
    }

    /**
     * 关于ExpandableListView的操作
     */
    private void aboutExpandableListView() {
        itemsMap = new HashMap<>();
        groupTitleList = new ArrayList<>();
        pageEntityList = new ArrayList<>();
        expandAdapter = new ExpandAdapter(getActivity(), itemsMap, groupTitleList);
        mExpandableListView.setAdapter(expandAdapter);
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition,
                                        long id) {
                return true;
            }
        });
        // 从网络加载数据
        OkHttpUtils.newInstance().doGet(UrlConfig.HAND_PICK_LIST_VIEW, HandPickContentProductInfo
                .class, new IOkCallBack<HandPickContentProductInfo>() {

            @Override
            public void onSuccess(HandPickContentProductInfo resultInfo, int requestCode) {
                List<HandPickContentProductInfo.DataEntity.ItemsEntity> itemsEntityList = resultInfo
                        .getData().getItems();
                // 为下拉刷新准备数据
                pageEntityList.add(resultInfo.getData().getPaging());
                // 此步骤不省
                groupTitleList.clear();
                itemsMap.clear();
                updateExpandListViewData(itemsEntityList);//更新ExpandListView数据源
            }
        }, 3);

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int
                    childPosition, long id) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                HandPickContentProductInfo.DataEntity.ItemsEntity itemsEntity = itemsMap.get
                        (groupTitleList.get(groupPosition)).get(childPosition);
                intent.putExtra("url", itemsEntity.getContent_url());
                intent.putExtra("image_url", itemsEntity.getCover_webp_url());
                intent.putExtra("title", itemsEntity.getTitle());
                startActivity(intent);
                return true;
            }
        });
    }

    /**
     * 更新ExpandListView数据源
     *
     * @param itemsEntityList
     */
    private void updateExpandListViewData(List<HandPickContentProductInfo.DataEntity.ItemsEntity>
                                                  itemsEntityList) {

        for (int i = 0; i < itemsEntityList.size(); i++) {
            HandPickContentProductInfo.DataEntity.ItemsEntity itemsEntity = itemsEntityList
                    .get(i);
            String key = DateFormatUtils.formatDate(itemsEntity.getPublished_at() * 1000L);
            List<HandPickContentProductInfo.DataEntity.ItemsEntity> itemsEntities =
                    itemsMap.get(key);
            if (itemsEntities != null) {
                itemsEntities.add(itemsEntity);
            } else {
                groupTitleList.add(key);
                itemsEntities = new ArrayList<>();
                itemsEntities.add(itemsEntity);
                itemsMap.put(key, itemsEntities);
            }
        }
        //将ExpandableListView展开，使child显示
        for (int i = 0; i < groupTitleList.size(); i++) {
            mExpandableListView.expandGroup(i);
        }
        expandAdapter.notifyDataSetChanged();
    }

    /**
     * 为mExpandableListView添加头部
     */
    private void addHeaderView() {
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout
                .expand_listview_header, null);
        hearderViewHolder = new HearderViewHolder(headerView);

        recyclerViewList = new ArrayList<>();
        mViewPagerUrlList = new ArrayList<>();
        //制定一个布局管理器
        hearderViewHolder.mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        //创建适配器并绑定适配器
        handPickRecyclerViewAdapter = new HandPickRecyclerViewAdapter(recyclerViewList,
                getActivity());
        hearderViewHolder.mRecycleView.setAdapter(handPickRecyclerViewAdapter);
        //请求数据
        OkHttpUtils.newInstance().doGet(UrlConfig.HAND_PICK_HORIZONTAL_LIST_VIEW_URL,
                HandPickRecyclerViewInfo.class, new IOkCallBack<HandPickRecyclerViewInfo>() {
                    @Override
                    public void onSuccess(HandPickRecyclerViewInfo resultInfo, int requestCode) {
                        List<HandPickRecyclerViewInfo.DataEntity.SecondaryBannersEntity>
                                secondary_banners = resultInfo.getData().getSecondary_banners();
                        recyclerViewList.clear();
                        recyclerViewList.addAll(secondary_banners);
                        handPickRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }, 1);

        OkHttpUtils.newInstance().doGet(UrlConfig.HAND_PICK_VIEW_PAGER_URL,
                HandPickViewPagerProductInfo.class, new IOkCallBack<HandPickViewPagerProductInfo>
                        () {
                    @Override
                    public void onSuccess(HandPickViewPagerProductInfo resultInfo, int
                            requestCode) {
                        List<HandPickViewPagerProductInfo.DataEntity.BannersEntity> banners =
                                resultInfo
                                        .getData().getBanners();
                        if (mViewPagerUrlList.isEmpty()) {
                            for (int i = 0; i < banners.size(); i++) {
                                mViewPagerUrlList.add(banners.get(i).getImage_url());
                            }
                        }
                        //参数一：CBViewHolderCreator创建者对象
                        //参数二：数据源
                        hearderViewHolder.mConvenientBanner.setPages(new CBViewHolderCreator() {
                            @Override
                            public Object createHolder() {
                                return new BannerViewHolder();
                            }
                        }, mViewPagerUrlList).setPageIndicator(new int[]{R.drawable
                                .ic_page_indicator, R
                                .drawable.ic_page_indicator_focused}).setPageIndicatorAlign
                                (ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
                    }
                }, 2);
        mExpandableListView.addHeaderView(headerView);
    }

    // Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement " +
                    "OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * 获得焦点时使ConvenientBanner滚动
     */
    @Override
    public void onResume() {
        super.onResume();
        if (hearderViewHolder != null) {
            hearderViewHolder.mConvenientBanner.startTurning(3000);
        }
    }

    /**
     * 停止焦点时停止ConvenientBanner滚动
     */
    @Override
    public void onStop() {
        super.onStop();
        if (hearderViewHolder != null) {
            hearderViewHolder.mConvenientBanner.stopTurning();
        }
    }

    /**
     * ConvenientBanner的ViewHolder
     */
    class BannerViewHolder implements Holder<String> {
        private ImageView mBannerImageView;

        @Override
        public View createView(Context context) {
            mBannerImageView = new ImageView(getActivity());
            mBannerImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return mBannerImageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            Picasso.with(context).load(data).into(mBannerImageView);
        }
    }

    /**
     * mExpandableListView头部的ViewHolder
     */
    static class HearderViewHolder {
        @BindView(R.id.cb_elv_header)
        ConvenientBanner mConvenientBanner;
        @BindView(R.id.rv_elv_header)
        RecyclerView mRecycleView;

        HearderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

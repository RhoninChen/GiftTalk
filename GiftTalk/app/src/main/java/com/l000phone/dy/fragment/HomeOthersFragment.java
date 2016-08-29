package com.l000phone.dy.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.l000phone.dy.adapter.HomeOthersListViewAdapter;
import com.l000phone.dy.bean.HomeOthersListViewInfo;
import com.l000phone.dy.common.UrlConfig;
import com.l000phone.dy.dygifttalk.R;
import com.l000phone.dy.dygifttalk.WebActivity;
import com.l000phone.dy.interf.IOkCallBack;
import com.l000phone.dy.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeOthersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeOthersFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CHANNEL_ID = "channelID";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView mListView;

    @BindView(R.id.lv_fg_home_others)
    PullToRefreshListView mRefreshListView;
    private List<HomeOthersListViewInfo.DataEntity.ItemsEntity> itemsEntityList;
    private List<HomeOthersListViewInfo.DataEntity.PagingEntity> pageEntityList;

    private HomeOthersListViewAdapter mLvAdpter;
    private String url;//每个标签对应的网址


    public HomeOthersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param channelID 频道的ID.
     * @param param2    Parameter 2.
     * @return A new instance of fragment HomeOthersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeOthersFragment newInstance(String channelID, String param2) {
        HomeOthersFragment fragment = new HomeOthersFragment();
        Bundle args = new Bundle();
        args.putString(CHANNEL_ID, channelID);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(CHANNEL_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_others, container, false);
        ButterKnife.bind(this, view);

        aboutListView();
        aboutRefreshListView();
        return view;
    }

    /**
     * mRefreshListView的相关操作
     */
    private void aboutRefreshListView() {
        // 设置只有下拉刷新模式
        mRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                LoadDataFromServer();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshListView.onRefreshComplete();
                    }
                },2000);
            }
        });
    }

    /**
     * ListView相关操作
     */
    private void aboutListView() {
        itemsEntityList = new ArrayList<>();
        pageEntityList = new ArrayList<>();
        url = UrlConfig.HOME_OTHERS_URL_1 + mParam1 + UrlConfig
                .HOME_OTHERS_URL_2;
        Log.d("Kevin",url);
        mListView = mRefreshListView.getRefreshableView();
        mLvAdpter = new HomeOthersListViewAdapter(getActivity(), itemsEntityList);
        mListView.setAdapter(mLvAdpter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                HomeOthersListViewInfo.DataEntity.ItemsEntity itemsEntity = itemsEntityList.get
                        (position);
                intent.putExtra("url", itemsEntity.getContent_url());
                intent.putExtra("title", itemsEntity.getTitle());
                intent.putExtra("image_url", itemsEntity.getCover_webp_url());
                startActivity(intent);
            }
        });
        //分页加载
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean isReachBottom;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isReachBottom && scrollState == SCROLL_STATE_IDLE) {
                    LoadNextPageDataFromServer();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                isReachBottom = (totalItemCount == firstVisibleItem + visibleItemCount);
            }
        });
        LoadDataFromServer();
    }

    /**
     * 加载下一页数据
     */
    private void LoadNextPageDataFromServer() {
        OkHttpUtils.newInstance().doGet(pageEntityList.get(pageEntityList.size() - 1).getNext_url
                (), HomeOthersListViewInfo.class, new IOkCallBack<HomeOthersListViewInfo>() {
            @Override
            public void onSuccess(HomeOthersListViewInfo resultInfo, int requestCode) {
                List<HomeOthersListViewInfo.DataEntity.ItemsEntity> items = resultInfo.getData()
                        .getItems();
                itemsEntityList.addAll(items);
                mLvAdpter.notifyDataSetChanged();

            }
        }, 1);
    }

    /**
     * 更新数据
     */
    private void LoadDataFromServer() {
        OkHttpUtils.newInstance().doGet(url, HomeOthersListViewInfo.class, new
                IOkCallBack<HomeOthersListViewInfo>() {

                    @Override
                    public void onSuccess(HomeOthersListViewInfo resultInfo, int requestCode) {
                        List<HomeOthersListViewInfo.DataEntity.ItemsEntity> itemsEntities =
                                resultInfo.getData().getItems();
                        pageEntityList.add(resultInfo.getData().getPaging());
                        itemsEntityList.clear();
                        if (itemsEntities != null) {
                            itemsEntityList.addAll(itemsEntities);
                        }
                        mLvAdpter.notifyDataSetChanged();
                    }
                }, 1);
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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

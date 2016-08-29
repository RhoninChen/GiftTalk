package com.l000phone.dy.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.l000phone.dy.adapter.HomeFragmentViewPagerAdapter;
import com.l000phone.dy.adapter.PopupWindowGridViewAdapter;
import com.l000phone.dy.bean.HomeTabProductInfo;
import com.l000phone.dy.common.UrlConfig;
import com.l000phone.dy.customview.MeasureGridView;
import com.l000phone.dy.dygifttalk.R;
import com.l000phone.dy.interf.IOkCallBack;
import com.l000phone.dy.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 指南页
 */
public class HomeFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.tl_fg_home)
    TabLayout tabLayout;
    @BindView(R.id.ib_fg_home)
    ImageButton ibDown;
    @BindView(R.id.ib_popup_window_home)
    ImageButton ibUp;
    @BindView(R.id.vp_fg_home)
    ViewPager viewPager;
    @BindView(R.id.fl_popup_window_home)
    FrameLayout frameLayout;

    private OnFragmentInteractionListener mListener;
    private PopupWindow popupWindow;
    private MeasureGridView mGridView;
    private List<String> titleList;
    private List<Fragment> fragmentList;
    private PopupWindowGridViewAdapter popupWindowGridViewAdapter;
    private HomeFragmentViewPagerAdapter homeFragmentViewPagerAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Log.d("Kevin", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();

        // aboutViewPager();
        aboutTabLayout();
        aboutIbDown();
        aboutIbUp();

        OkHttpUtils.newInstance().doGet(UrlConfig.TAB_URL, HomeTabProductInfo.class, new IOkCallBack<HomeTabProductInfo>() {
            @Override
            public void onSuccess(HomeTabProductInfo resultInfo, int requestCode) {
                List<HomeTabProductInfo.DataEntity.ChannelsEntity> channels = resultInfo.getData().getChannels();
                for (int i = 0; i < channels.size(); i++) {
                    titleList.add(channels.get(i).getName());
                    if (i == 0) {
                        fragmentList.add(HandPickFragment.newInstance(null, null));
                    } else {
                        Log.d("Kevin",channels.get(i).getId()+"");
                        fragmentList.add(HomeOthersFragment.newInstance(channels.get(i).getId()+"", null));
                    }
                }
                aboutViewPager();
                // 通知ViewPager刷新数据源
                // homeFragmentViewPagerAdapter.notifyDataSetChanged();
            }
        }, 1);
        return view;
    }

    /**
     * 关于TabLayout的操作
     */
    private void aboutTabLayout() {
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //将TabLayout和ViewPager关联起来
        // tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 关于ViewPager的操作
     */
    private void aboutViewPager() {
        homeFragmentViewPagerAdapter = new HomeFragmentViewPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(homeFragmentViewPagerAdapter);
        //将TabLayout和ViewPager关联起来
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 关于popupWindow向下收起的操作
     */
    private void aboutIbUp() {
        ibUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.INVISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                ibDown.setVisibility(View.VISIBLE);
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 关于popupWindow向下展开的操作
     */
    private void aboutIbDown() {
        ibDown.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                // int height = viewPager.getHeight();
                View popView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window_home, null);
                popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mGridView = (MeasureGridView) popView.findViewById(R.id.mgv_popup_window_home);
                popupWindowGridViewAdapter = new PopupWindowGridViewAdapter(getActivity(), titleList);
                mGridView.setAdapter(popupWindowGridViewAdapter);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        frameLayout.setVisibility(View.INVISIBLE);
                        tabLayout.setVisibility(View.VISIBLE);
                        ibDown.setVisibility(View.VISIBLE);
                        popupWindow.dismiss();
                        viewPager.setCurrentItem(position);
                    }
                });

                popupWindow.setContentView(popView);
                popupWindow.setOutsideTouchable(true);

                popupWindow.showAsDropDown(tabLayout, 0, 0, Gravity.CENTER);
                frameLayout.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.INVISIBLE);
                ibDown.setVisibility(View.INVISIBLE);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.setFocusable(true);
                        return false;
                    }
                });
                popupWindow.setFocusable(false);
            }
        });
    }

    // Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 是去焦点时收起弹窗
        frameLayout.setVisibility(View.INVISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        ibDown.setVisibility(View.VISIBLE);
        if (popupWindow != null) {
            popupWindow.dismiss();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        frameLayout.setVisibility(View.INVISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        ibDown.setVisibility(View.VISIBLE);
        if (popupWindow != null) {
            popupWindow.dismiss();
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

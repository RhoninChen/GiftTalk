package com.l000phone.dy.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.l000phone.dy.adapter.CategoryGiftDetailListViewAdapter;
import com.l000phone.dy.adapter.CategoryGiftTitleListViewAdapter;
import com.l000phone.dy.bean.CategoryGiftInfo;
import com.l000phone.dy.common.UrlConfig;
import com.l000phone.dy.dygifttalk.R;
import com.l000phone.dy.dygifttalk.SelectGiftGodActivity;
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
 * Use the {@link CategoryGiftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryGiftFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.btn_select_gift_god)
    Button btnSelectGiftGod;
    @BindView(R.id.lv_category_gift_title)
    ListView lvTitle;
    @BindView(R.id.lv_category_gift_detail)
    ListView lvDetail;

    private CategoryGiftTitleListViewAdapter titleListViewAdapter;//左侧ListView数据源
    private List<CategoryGiftInfo.DataEntity.CategoriesEntity> categoriesEntityList;//左侧适配器

    private CategoryGiftDetailListViewAdapter detailListViewAdapter;//右侧适配器

    public CategoryGiftFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryGiftFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryGiftFragment newInstance(String param1, String param2) {
        CategoryGiftFragment fragment = new CategoryGiftFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_gift, container, false);
        ButterKnife.bind(this, view);

        aboutLvTitle();
        aboutLvDetail();

        btnSelectGiftGod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , SelectGiftGodActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    /**
     * 关于右侧ListView的操作
     */
    private void aboutLvDetail() {
        detailListViewAdapter = new CategoryGiftDetailListViewAdapter(getActivity() , categoriesEntityList);
        lvDetail.setAdapter(detailListViewAdapter);
        lvDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int firstVisiblePosition = lvDetail.getFirstVisiblePosition();

                titleListViewAdapter.setSelectPosition(firstVisiblePosition);
                lvTitle.smoothScrollToPositionFromTop(firstVisiblePosition,150);
                titleListViewAdapter.notifyDataSetChanged();

                if(firstVisiblePosition >= 1){
                    lvTitle.setSelection(firstVisiblePosition - 1);
                }else {
                    lvTitle.setSelection(firstVisiblePosition);
                }
                return false;
            }
        });
    }

    /**
     * 关于左侧ListView的操作
     */
    private void aboutLvTitle() {
        categoriesEntityList = new ArrayList<>();
        titleListViewAdapter = new CategoryGiftTitleListViewAdapter(categoriesEntityList,getActivity());
        lvTitle.setAdapter(titleListViewAdapter);
        OkHttpUtils.newInstance().doGet(UrlConfig.CATEGORY_GIFT_URL, CategoryGiftInfo.class, new IOkCallBack<CategoryGiftInfo>() {

            @Override
            public void onSuccess(CategoryGiftInfo resultInfo, int requestCode) {
                categoriesEntityList.addAll(resultInfo.getData().getCategories());
                titleListViewAdapter.notifyDataSetChanged();

                detailListViewAdapter.notifyDataSetChanged();
            }
        },5);
        lvTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 改变左侧背景色，并滚动
                titleListViewAdapter.setSelectPosition(position);
                lvTitle.smoothScrollToPositionFromTop(position,150);
                titleListViewAdapter.notifyDataSetChanged();

                lvDetail.setSelection(position);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
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

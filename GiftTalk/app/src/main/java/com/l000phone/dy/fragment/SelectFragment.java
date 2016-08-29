package com.l000phone.dy.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.l000phone.dy.adapter.SelectGridViewAdapter;
import com.l000phone.dy.bean.SelectGridViewInfo;
import com.l000phone.dy.common.UrlConfig;
import com.l000phone.dy.dygifttalk.R;
import com.l000phone.dy.dygifttalk.SelectDetailActivity;
import com.l000phone.dy.interf.IOkCallBack;
import com.l000phone.dy.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this com.pluto.fragment must implement the
 * {@link SelectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectFragment#newInstance} factory method to
 * create an instance of this com.pluto.fragment.
 */
public class SelectFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the com.pluto.fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SelectFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.gv_fg_select)
    GridView mGridView;
    private List<SelectGridViewInfo.DataEntity.ItemsEntity> dataEntity1List =
            new ArrayList<>();
    private SelectGridViewAdapter gridViewAdapter;


    /**
     * Use this factory method to create a new instance of
     * this com.pluto.fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of com.pluto.fragment SelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectFragment newInstance(String param1, String param2) {
        SelectFragment fragment = new SelectFragment();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.pluto.fragment
        View view = inflater.inflate(R.layout.fragment_select, container, false);
        ButterKnife.bind(this , view);

        gridViewAdapter = new SelectGridViewAdapter(getActivity() , dataEntity1List);
        mGridView.setAdapter(gridViewAdapter);

        OkHttpUtils.newInstance().doGet(UrlConfig.SELECT_GRID_VIEW_URL, SelectGridViewInfo.class
                , new IOkCallBack<SelectGridViewInfo>() {
                    @Override
                    public void onSuccess(SelectGridViewInfo resultInfo, int requestCode) {
                        List<SelectGridViewInfo.DataEntity.ItemsEntity> items = resultInfo.getData().getItems();
                        dataEntity1List.addAll(items);
                        gridViewAdapter.notifyDataSetChanged();
                    }
                }, 1);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity() , SelectDetailActivity.class);
                intent.putExtra("id" , dataEntity1List.get(position).getData1().getId() + "");
                startActivity(intent);
            }
        });
        return view;
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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

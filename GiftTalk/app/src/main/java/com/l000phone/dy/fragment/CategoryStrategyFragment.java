package com.l000phone.dy.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.l000phone.dy.adapter.CategoryGridAdapter;
import com.l000phone.dy.bean.ChannelGroupsData;
import com.l000phone.dy.bean.ColumnsDate;
import com.l000phone.dy.common.UrlConfig;
import com.l000phone.dy.customview.MeasureGridView;
import com.l000phone.dy.dygifttalk.ItemDetailActivity;
import com.l000phone.dy.dygifttalk.R;
import com.l000phone.dy.dygifttalk.StrategyDetail;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryStrategyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryStrategyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryStrategyFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView strategyList;
    private List<ChannelGroupsData.DataBean.ChannelGroupsBean> channel_groups;
    private LinearLayout id_gallery;

    private List<ColumnsDate.DataBean.ColumnsBean> columns;

    public CategoryStrategyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryStrategyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryStrategyFragment newInstance(String param1, String param2) {
        CategoryStrategyFragment fragment = new CategoryStrategyFragment();
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
        View view = inflater.inflate(R.layout.fragment_category_strategy, container, false);
        strategyList = (ListView) view.findViewById(R.id.lv_strategy);

        //给ListView填充栏目布局
        View SubjectView = View.inflate(getActivity(), R.layout.subject_layout, null);

        id_gallery = (LinearLayout) SubjectView.findViewById(R.id.id_gallery);

        //给栏目加载数据
        String subjectUrl = UrlConfig.SUBJECT_URL;
        //SUBJECT_URL = "http://api.liwushuo.com/v2/columns"
        //404报错NotFound,将Json数据放置本地服务器
        initSubjectData(subjectUrl);
        //给品类,风格,对象的GridView添加数据
        String cateUrl = UrlConfig.CATEGORY_URL;
        //CATEGORY_URL = "http://api.liwushuo.com/v2/channel_groups/all"
        initCateData(cateUrl);

        //设置栏目布局为ListView的头布局
        strategyList.addHeaderView(SubjectView);

        return view;
    }

    //给品类,风格,对象的GridView添加数据
    private void initCateData(String cateUrl) {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, cateUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Log.i("TAG", "----->种类数据加载成功");
                ParseJsonToBean(result, false);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("TAG", "----->种类数据加载失败");
            }
        });
    }
    //给栏目加载数据
    private void initSubjectData(String subjectUrl) {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, subjectUrl, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Log.i("TAG", "----->栏目数据加载成功\n------->数据是" + result);
                ParseJsonToBean(result, true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(), "栏目数据下载失败", Toast.LENGTH_SHORT).show();
                Log.i("TAG", "----->栏目数据加载失败");
            }
        });
    }

    private void ParseJsonToBean(String result, boolean isHeaderView) {
        Gson gson = new Gson();
        if (isHeaderView) {
            //解析栏目数据
            ColumnsDate columnsDate = gson.fromJson(result, ColumnsDate.class);

            columns = columnsDate.getData().getColumns();
            Log.i("TAG", "----->准备给栏目项部署数据");
            if (columnsDate != null) {
                for (int i = 0; i < columns.size(); i++) {
                    LinearLayout columnView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.recycler_view_item, id_gallery, false);
                    TextView tv_columns_type = (TextView) columnView.findViewById(R.id.tv_columns_type);
                    TextView tv_columns_phase = (TextView) columnView.findViewById(R.id.tv_columns_phase);
                    TextView tv_columns_author = (TextView) columnView.findViewById(R.id.tv_columns_author);
                    final ImageView iv_sub_img = (ImageView) columnView.findViewById(R.id.iv_sub_img);
                    String imageUrl = columns.get(i).getCover_image_url();

                    //栏目item项部署数据
                    tv_columns_type.setText(columns.get(i).getTitle());
                    tv_columns_phase.setText(columns.get(i).getSubtitle());
                    tv_columns_author.setText(columns.get(i).getAuthor());

                    //下载图片
                    BitmapUtils utils = new BitmapUtils(getActivity());
                    utils.display(iv_sub_img, imageUrl, new BitmapLoadCallBack<ImageView>() {
                        @Override
                        public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                            //设置圆角图片
                            Log.i("TAG", "加载图片");
                            //Bitmap roundCornerImage = BitmapHelper.getRoundCornerImage(bitmap, 10);
                            iv_sub_img.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {

                        }
                    });
                    id_gallery.addView(columnView);
                    //攻略->栏目->每日值得Buy条目: http://api.liwushuo.com/v2/columns/46?limit=20&offset=0
                    //CATEGORY_ITEM_URL = "http://api.liwushuo.com/v2/columns/%d?limit=20&offset=0";
                    //给栏目项添加监听器跳转页面(WebView)
                    int columnItemID = columns.get(i).getId();
                    String CATEGORY_ITEM_URL_after = String.format(UrlConfig.CATEGORY_ITEM_URL, columnItemID);
                    Intent intent = new Intent(columnView.getContext(), StrategyDetail.class);
                    intent.putExtra("url", CATEGORY_ITEM_URL_after);
                }
                //给LinearLayout添加监听器,跳转至WebView界面
                //columnView.getContext().startActivity(intent);

            }
        } else {
            //解析种类数据
            ChannelGroupsData channelGroupsData = gson.fromJson(result, ChannelGroupsData.class);
            //品类,风格,对象
            channel_groups = channelGroupsData.getData().getChannel_groups();
            Log.i("TAG", "----->给种类项GridView设置数据");
            strategyList.setAdapter(new CategoryListAdapter());
        }
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

    private class CategoryListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return channel_groups != null ? channel_groups.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return channel_groups.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup viewGroup) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = View.inflate(viewGroup.getContext(), R.layout.list_item, null);
                vh.name = (TextView) convertView.findViewById(R.id.tv_name);
                vh.gridView = (MeasureGridView) convertView.findViewById(R.id.gv_grid);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            //获取 品类,风格,对象 中的一项
            final String name = channel_groups.get(position).getName();
            //设置标题
            vh.name.setText(name);
            //GridView数据
            final List<ChannelGroupsData.DataBean.ChannelGroupsBean.ChannelsBean> channels = channel_groups.get(position).getChannels();
            //类别项(品类,风格,对象)item部署数据
            CategoryGridAdapter mGridAdapter = new CategoryGridAdapter(CategoryStrategyFragment.this.getActivity());
            mGridAdapter.setData(channels);
            //绑定适配器
            vh.gridView.setAdapter(mGridAdapter);
            //绑定监听器
            vh.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = channels.get(position).getName();//获取品类项的名字(品类的礼物)
                    int channelsId = channels.get(position).getId();//对应项的ID(品类的礼物ID)

                    //拼接Url(攻略->品类->礼物):
                    // http://api.liwushuo.com/v2/channels/111/items_v2?gender=1&generation=2&order_by=now&limit=20&offset=0
                    String CATEGORY_URL_ITEM_after = String.format(UrlConfig.CATEGORY_URL_ITEM, channelsId);
                    Intent intent = new Intent(parent.getContext(), ItemDetailActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("imgUrl", CATEGORY_URL_ITEM_after);

                    Log.i("TAG", "拼接网址:" + CATEGORY_URL_ITEM_after);

                    viewGroup.getContext().startActivity(intent);

                }
            });
            return convertView;
        }

        private class ViewHolder {
            private TextView name;
            private MeasureGridView gridView;
        }

    }
}

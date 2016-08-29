package com.l000phone.dy.utils;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.reflect.TypeToken;
import com.l000phone.dy.interf.IOkCallBack;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HP on 2016/7/11.
 */
public class OkHttpUtils {
    private static OkHttpUtils okHttpUtils;
    private OkHttpClient client;
    private Handler mHandler;

    private OkHttpUtils() {
        client = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpUtils newInstance() {
        if (okHttpUtils == null) {
            okHttpUtils = new OkHttpUtils();
        }
        return okHttpUtils;
    }

    public <T> void doGet(String url, final Class<T> clazz, final IOkCallBack okCallBack, final int requestCode) {
        //创建一个请求
        Request request = new Request.Builder().url(url).build();
        // 执行请求
        client.newCall(request).enqueue(new Callback() {
            //请求失败
            @Override
            public void onFailure(Call call, IOException e) {
                //工作线程，不能更新UI
            }

            //请求成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //工作线程，不能更新UI
                String result = response.body().string();
                final T resultInfo = GsonUtils.parseJson2Object(result, clazz);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        okCallBack.onSuccess(resultInfo, requestCode);
                    }
                });
            }
        });
    }

    /**
     * okHttp的get请求,json类型为Array
     * @param url
     * @param typeToken
     * @param okCallBack
     * @param requestCode
     * @param <T>
     */
    public <T> void okGet(String url , final TypeToken<List<T>> typeToken, final IOkCallBack okCallBack , final int requestCode){
        //创建一个请求
        Request request = new Request.Builder().url(url).build();
        //执行请求
        client.newCall(request).enqueue(new Callback() {
            //请求失败
            @Override
            public void onFailure(Call call, IOException e) {
                //工作线程，不能更新UI
            }

            //请求成功
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //工作线程，不能更新UI
                String result = response.body().string();
                final List<T> resultListInfo = GsonUtils.parseJason2Array(result, typeToken);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        okCallBack.onSuccess(resultListInfo, requestCode);
                    }
                });
            }
        });
    }
}

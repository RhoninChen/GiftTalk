package com.l000phone.dy.dygifttalk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CrowdActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TITLE_GENDER = "请选择您的性别";
    private final String TITLE_IDENTTITY = "请选择您的身份";


    @BindView(R.id.tv_slect_title)
    TextView tvSlectTitle;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.ll_select_boy)
    LinearLayout llSelectBoy;
    @BindView(R.id.ll_select_girl)
    LinearLayout llSelectGirl;
    @BindView(R.id.ll_select_gender)
    LinearLayout llSelectGender;
    @BindView(R.id.chuzhong)
    Button chuzhong;
    @BindView(R.id.gaozhong)
    Button gaozhong;
    @BindView(R.id.daxue)
    Button daxue;
    @BindView(R.id.fresh)
    Button fresh;
    @BindView(R.id.old_work)
    Button oldWork;
    @BindView(R.id.rl_select_identity)
    RelativeLayout rlSelectIdentity;

    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowd);
        ButterKnife.bind(this);

        llSelectBoy.setOnClickListener(this);
        llSelectGirl.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        chuzhong.setOnClickListener(this);
        gaozhong.setOnClickListener(this);
        daxue.setOnClickListener(this);
        fresh.setOnClickListener(this);
        oldWork.setOnClickListener(this);

        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        edit = sp.edit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_select_boy:
                btnBack.setVisibility(View.VISIBLE);
                rlSelectIdentity.setVisibility(View.VISIBLE);
                llSelectGender.setVisibility(View.INVISIBLE);
                tvSlectTitle.setText(TITLE_IDENTTITY);
                edit.putInt("gender", 1);
                break;
            case R.id.ll_select_girl:
                btnBack.setVisibility(View.VISIBLE);
                rlSelectIdentity.setVisibility(View.VISIBLE);
                llSelectGender.setVisibility(View.INVISIBLE);
                tvSlectTitle.setText(TITLE_IDENTTITY);
                edit.putInt("gender", 2);
                break;
            case R.id.btn_back:
                btnBack.setVisibility(View.INVISIBLE);
                rlSelectIdentity.setVisibility(View.INVISIBLE);
                llSelectGender.setVisibility(View.VISIBLE);
                tvSlectTitle.setText(TITLE_GENDER);
                break;
            case R.id.chuzhong:
                skipActivity(MainActivity.class);
                edit.putInt("chuzhong", 1);
                break;
            case R.id.gaozhong:
                skipActivity(MainActivity.class);
                edit.putInt("chuzhong", 2);
                break;
            case R.id.daxue:
                skipActivity(MainActivity.class);
                edit.putInt("chuzhong", 3);
                break;
            case R.id.fresh:
                skipActivity(MainActivity.class);
                edit.putInt("chuzhong", 4);
                break;
            case R.id.old_work:
                skipActivity(MainActivity.class);
                edit.putInt("chuzhong", 5);
                break;
        }
    }

    private void skipActivity(Class clazz) {
        Intent intent = new Intent(CrowdActivity.this, clazz);
        startActivity(intent);
        finish();
    }

}

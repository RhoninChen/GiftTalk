package com.l000phone.dy.dygifttalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.l000phone.dy.fragment.CategoryFragment;
import com.l000phone.dy.fragment.HomeFragment;
import com.l000phone.dy.fragment.ProfileFragment;
import com.l000phone.dy.fragment.SelectFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity{

    @BindView(R.id.ib_main_sign_in)
    ImageButton ibMainSignIn;
    @BindView(R.id.iv_main_logo_title)
    ImageView ivMainLogoTitle;
    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;
    @BindView(R.id.ib_main_search)
    ImageButton ibMainSearch;
    @BindView(R.id.toolbar_main)
    Toolbar toolbarMain;
    @BindView(R.id.rg_main_bottom)
    RadioGroup rgMainBottom;

    private FragmentManager supportFragmentManager;
    private HomeFragment homeFragment;
    private SelectFragment selectFragment;
    private CategoryFragment categoryFragment;
    private ProfileFragment profileFragment;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        //默认显示主页碎片
        //HomeFragment homeFragment = HomeFragment.newInstance(null, null);
        homeFragment = HomeFragment.newInstance(null, null);
        supportFragmentManager = getSupportFragmentManager();
        fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_main_fragment, homeFragment);
        //这一步别忘了
        fragmentTransaction.commit();

        aboutRadioGroup();

        ibMainSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 关于RadioGroup的操作
     */
    private void aboutRadioGroup() {
        //默认选中主页RadioButton
        // rgMainBottom.check(R.id.rb_main_home);
        ((RadioButton) rgMainBottom.getChildAt(0)).setChecked(true);
        rgMainBottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction fragmentTransaction1 = supportFragmentManager.beginTransaction();
                hideAllFragment(fragmentTransaction1);
                switch (checkedId) {
                    case R.id.rb_main_home:
                        if (homeFragment == null) {
                            //Log.d("Kevin","HomeFragment == 1");
                            homeFragment = HomeFragment.newInstance(null, null);
                            fragmentTransaction1.add(R.id.fl_main_fragment, homeFragment);
                        }else{
                            //Log.d("Kevin","HomeFragment == ");
                            fragmentTransaction1.show(homeFragment);
                        }
                        //这些必须写，否则在点击了第三、四个标签之后，切回第一二个后，toolbar将不显示
                        toolbarMain.setVisibility(View.VISIBLE);
                        ibMainSignIn.setVisibility(View.VISIBLE);
                        ibMainSearch.setVisibility(View.VISIBLE);
                        ivMainLogoTitle.setVisibility(View.VISIBLE);
                        tvMainTitle.setVisibility(View.GONE);
                        break;
                    case R.id.rb_main_select:
                        if(selectFragment == null ){
                            selectFragment = SelectFragment.newInstance(null, null);
                            fragmentTransaction1.add(R.id.fl_main_fragment, selectFragment);
                        }else {
                            fragmentTransaction1.show(selectFragment);
                        }
                        toolbarMain.setVisibility(View.VISIBLE);
                        ibMainSignIn.setVisibility(View.GONE);
                        ibMainSearch.setVisibility(View.VISIBLE);
                        tvMainTitle.setVisibility(View.VISIBLE);
                        ivMainLogoTitle.setVisibility(View.GONE);
                        break;
                    case R.id.rb_main_category:
                        if(categoryFragment == null ){
                            categoryFragment = CategoryFragment.newInstance(null, null);
                            fragmentTransaction1.add(R.id.fl_main_fragment, categoryFragment);
                        }else {
                            fragmentTransaction1.show(categoryFragment);
                        }
                        toolbarMain.setVisibility(View.GONE);
                        break;
                    case R.id.rb_main_profile:
                        if(profileFragment == null ){
                            profileFragment = ProfileFragment.newInstance(null, null);
                            fragmentTransaction1.add(R.id.fl_main_fragment, profileFragment);
                        }else {
                            fragmentTransaction1.show(profileFragment);
                        }
                        toolbarMain.setVisibility(View.GONE);
                        break;
                }
                fragmentTransaction1.commit();
            }
        });
    }

    /**
     * 隐藏所有fragment
     */
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        //Log.d("Kevin","hideAllFragment");
        if (homeFragment != null)
            fragmentTransaction.hide(homeFragment);
        if (selectFragment != null)
            fragmentTransaction.hide(selectFragment);
        if (categoryFragment != null)
            fragmentTransaction.hide(categoryFragment);
        if (profileFragment != null)
            fragmentTransaction.hide(profileFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 添加逻辑使用户需要点击两次后退键退出
        return super.onKeyDown(keyCode, event);
    }
}

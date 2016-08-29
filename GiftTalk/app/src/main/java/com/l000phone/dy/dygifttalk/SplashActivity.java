package com.l000phone.dy.dygifttalk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE); 直接更改application主题为NoActionBar
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        Boolean isFirstIn = sharedPreferences.getBoolean("isFirstIn", true);
        if (isFirstIn) {
            // 记得提交
            sharedPreferences.edit().putBoolean("isFirstIn", false).commit();
            skipActivity(CrowdActivity.class);
        } else {
            skipActivity(MainActivity.class);
        }
    }

    private void skipActivity(final Class clazz) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, clazz);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        },1500);
    }
}

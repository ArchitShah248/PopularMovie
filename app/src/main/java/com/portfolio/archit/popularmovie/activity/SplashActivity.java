package com.portfolio.archit.popularmovie.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.portfolio.archit.popularmovie.R;

/**
 *  Splash screen
 */
public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent movieIntent = new Intent(mContext,MainActivity.class);
                startActivity(movieIntent);
                finish();
            }
        },1500);

    }

    @Override
    public void setListeners() {

    }


}

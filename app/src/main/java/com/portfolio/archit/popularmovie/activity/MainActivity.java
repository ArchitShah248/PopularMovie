package com.portfolio.archit.popularmovie.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.portfolio.archit.popularmovie.R;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}

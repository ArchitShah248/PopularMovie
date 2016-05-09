package com.portfolio.archit.popularmovie.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.portfolio.archit.popularmovie.R;
import com.portfolio.archit.popularmovie.fragments.MovieDetailFragment;

public class DetailActivity extends AppCompatActivity{

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView(savedInstanceState);

    }

    public void initView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = getIntent().getExtras();

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.detail_container, fragment).commit();
        }
    }
}

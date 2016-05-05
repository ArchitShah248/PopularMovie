package com.portfolio.archit.popularmovie.activity;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.portfolio.archit.popularmovie.R;
import com.portfolio.archit.popularmovie.data.AppConstants;
import com.portfolio.archit.popularmovie.data.AppURLs;
import com.portfolio.archit.popularmovie.fragments.MovieDetailFragment;
import com.portfolio.archit.popularmovie.httphelper.VolleyHelper;
import com.portfolio.archit.popularmovie.model.Movie;
import com.portfolio.archit.popularmovie.utilities.Utils;

public class DetailActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_detail);
        super.onCreate(savedInstanceState);


        initView(savedInstanceState);
        setListeners();
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


    public void setListeners() {

    }

}

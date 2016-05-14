package com.portfolio.archit.popularmovie.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.portfolio.archit.popularmovie.R;
import com.portfolio.archit.popularmovie.data.AppConstants;
import com.portfolio.archit.popularmovie.fragments.MovieDetailFragment;
import com.portfolio.archit.popularmovie.fragments.MovieListFragment;
import com.portfolio.archit.popularmovie.model.Movie;

public class MainActivity extends AppCompatActivity implements MovieListFragment.MovieListCallback {

    private Context mContext;
    private RelativeLayout detailContainer;
    private MovieListFragment movieListFragment;
    private boolean isMultiPane = false;
    private static final String SAVED_INSTANCE_IS_MULTIPANE = "saved_instance_is_multipane";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initViews(savedInstanceState);

    }

    private void initViews(Bundle savedInstanceState) {

        movieListFragment = (MovieListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movie_list);

        detailContainer = (RelativeLayout) findViewById(R.id.detail_container);

        if (detailContainer != null) {
            isMultiPane = true;
        }

        if (movieListFragment != null) {
            movieListFragment.setMultiPane(isMultiPane);
        }

    }


    @Override
    public void onItemClickListener( int position, Movie movie) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstants.INTENT_KEY_MOVIE_DETAIL, movie);
        bundle.putBoolean(AppConstants.INTENT_KEY_IS_MULTIPANE, isMultiPane);

        if (isMultiPane) {

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setMovieDetailCallbacks(new MovieDetailFragment.MovieDetailCallbacks() {
                @Override
                public void onFavouriteStateChanged() {
                    if(movieListFragment != null){
                        movieListFragment.onFavoriteDataChanged();
                    }
                }
            });
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, fragment).commit();

        } else {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }


    }
}

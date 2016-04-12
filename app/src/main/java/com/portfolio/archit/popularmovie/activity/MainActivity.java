package com.portfolio.archit.popularmovie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.portfolio.archit.popularmovie.R;
import com.portfolio.archit.popularmovie.adapter.MovieGridAdapter;
import com.portfolio.archit.popularmovie.data.AppConstants;
import com.portfolio.archit.popularmovie.data.AppURLs;
import com.portfolio.archit.popularmovie.httphelper.VolleyHelper;
import com.portfolio.archit.popularmovie.model.Movie;
import com.portfolio.archit.popularmovie.model.MovieList;
import com.portfolio.archit.popularmovie.utilities.Utils;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GridView gridView;
    private Spinner spinnerSort;
    private MovieGridAdapter movieGridAdapter;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private int currentPage = 1, retreivingPage = 1;

    private String selectedSortProperty = "";

    private String[] sortingProperties;

    private SimpleArcDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = this;
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {

        sortingProperties = getResources().getStringArray(R.array.sort_properties_values);

        gridView = (GridView) findViewById(R.id.gridMoviePoster);
        movieGridAdapter = new MovieGridAdapter(mContext);
        gridView.setAdapter(movieGridAdapter);

        spinnerSort = (Spinner) findViewById(R.id.spinnerSort);

        movieGridAdapter.setDataList(currentPage, movieArrayList);

        mDialog = new SimpleArcDialog(this);
        mDialog.setConfiguration(new ArcConfiguration(this));

    }

    @Override
    public void setListeners() {

        movieGridAdapter.setOnGridItemClickedListener(new MovieGridAdapter.OnGridItemClickedListener() {
            @Override
            public int getCurrentPage() {
                return currentPage;
            }

            @Override
            public void onGridItemSelected(int position, Movie movie) {

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(AppConstants.INTENT_KEY_MOVIE_DETAIL, movie);
                startActivity(intent);

            }

            @Override
            public void onScrolledToLast(int position, int nextPageIndex) {
                if (retreivingPage != nextPageIndex) {
                    fetchData(nextPageIndex, selectedSortProperty);
                }
            }
        });

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String property = sortingProperties[position];

                fetchData(1, property);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void fetchData(final int page, String sortProp) {
        if (Utils.isInternetAvailable(mContext)) {
            if (!sortProp.equalsIgnoreCase(selectedSortProperty)) {
                retreivingPage = page;
                selectedSortProperty = sortProp;
            }

            String url = AppURLs.MOVIE_DB_BASE_URL + AppURLs.MOVIE_ACTION_DISCOVER + AppURLs.MOVIE_PARA_API_KEY + AppURLs.PARA_SORT_BY + sortProp + AppURLs.PARA_PAGE + page;

            mDialog.show();
            Log.i(TAG, "Movie URL: " + url);
            VolleyHelper volleyHelper = VolleyHelper.getInstance(mContext);
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Gson gson = new Gson();
                    MovieList movieList = gson.fromJson(response, MovieList.class);

                    if (movieList != null && movieList.getMovieArrayList().size() > 0) {
                        currentPage = page;
                        if (currentPage != 1) {
                            movieArrayList.addAll(movieList.getMovieArrayList());
                        } else {
                            movieArrayList = movieList.getMovieArrayList();
                        }

                        movieGridAdapter.setDataList(currentPage, movieArrayList);
                    }
                    mDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error: " + error.getLocalizedMessage());
                    Toast.makeText(mContext, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }
            });
            volleyHelper.addToRequestQueue(request);
        } else {
            Toast.makeText(mContext, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

}

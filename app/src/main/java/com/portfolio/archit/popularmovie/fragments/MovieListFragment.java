package com.portfolio.archit.popularmovie.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.portfolio.archit.popularmovie.R;
import com.portfolio.archit.popularmovie.activity.DetailActivity;
import com.portfolio.archit.popularmovie.adapter.EqualSpaceItemDecoration;
import com.portfolio.archit.popularmovie.adapter.MovieRecyclerAdapter;
import com.portfolio.archit.popularmovie.data.AppConstants;
import com.portfolio.archit.popularmovie.data.AppURLs;
import com.portfolio.archit.popularmovie.httphelper.GsonRequest;
import com.portfolio.archit.popularmovie.httphelper.VolleyHelper;
import com.portfolio.archit.popularmovie.model.Movie;
import com.portfolio.archit.popularmovie.model.MovieList;
import com.portfolio.archit.popularmovie.utilities.Utils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MovieListFragment extends BaseFragment {

    private static final String TAG = MovieListFragment.class.getSimpleName();

    private RecyclerView gridView;
    private Spinner spinnerSort;
    //    private MovieGridAdapter movieGridAdapter;
    private MovieRecyclerAdapter movieRecyclerAdapter;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private int currentPage = 1, retreivingPage = 1;

    private String selectedSortProperty = "";

    private String[] sortingProperties;

    private SimpleArcDialog mDialog;


    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initView(Bundle savedInstanceState) {

        sortingProperties = getResources().getStringArray(R.array.sort_properties_values);

        gridView = (RecyclerView) mView.findViewById(R.id.gridMoviePoster);
//        movieGridAdapter = new MovieGridAdapter(mContext);
//        gridView.setAdapter(movieGridAdapter);
        movieRecyclerAdapter = new MovieRecyclerAdapter(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridView.setLayoutManager(gridLayoutManager);
        gridView.addItemDecoration(new EqualSpaceItemDecoration(16));
        gridView.setAdapter(movieRecyclerAdapter);


        spinnerSort = (Spinner) mView.findViewById(R.id.spinnerSort);


        movieRecyclerAdapter.setDataList(currentPage, movieArrayList);

        mDialog = new SimpleArcDialog(mContext);
        mDialog.setConfiguration(new ArcConfiguration(mContext));

    }

    @Override
    protected void setListeners() {
        movieRecyclerAdapter.setOnGridItemClickedListener(new MovieRecyclerAdapter.OnGridItemClickedListener() {
            @Override
            public int getCurrentPage() {
                return currentPage;
            }

            @Override
            public void onGridItemSelected(int position, Movie movie) {

                Intent intent = new Intent(mContext, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppConstants.INTENT_KEY_MOVIE_DETAIL, movie);
                intent.putExtras(bundle);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getContext();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void fetchData(final int page, String sortProp) {
        if (Utils.isInternetAvailable(mContext)) {
            if (!sortProp.equalsIgnoreCase(selectedSortProperty)) {
                retreivingPage = page;
                selectedSortProperty = sortProp;
            }

            String url = AppURLs.MOVIE_DB_BASE_URL + sortProp + AppURLs.MOVIE_PARA_API_KEY + AppURLs.PARA_PAGE + page;

            mDialog.show();
            Log.i(TAG, "Movie URL: " + url);
            VolleyHelper volleyHelper = VolleyHelper.getInstance(mContext);
            GsonRequest<MovieList> request = new GsonRequest<>(url, MovieList.class, null, new Response.Listener<MovieList>() {
                @Override
                public void onResponse(MovieList response) {
                    MovieList movieList = response;
                    if (movieList != null && movieList.getMovieArrayList().size() > 0) {
                        currentPage = page;
                        if (currentPage != 1) {
                            movieArrayList.addAll(movieList.getMovieArrayList());
                        } else {
                            movieArrayList = movieList.getMovieArrayList();
                        }

                        movieRecyclerAdapter.setDataList(currentPage, movieArrayList);
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
package com.portfolio.archit.popularmovie.fragments;

import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.portfolio.archit.popularmovie.R;
import com.portfolio.archit.popularmovie.adapter.EqualSpaceItemDecoration;
import com.portfolio.archit.popularmovie.adapter.MovieRecyclerAdapter;
import com.portfolio.archit.popularmovie.adapter.RecyclerItemListener;
import com.portfolio.archit.popularmovie.data.AppURLs;
import com.portfolio.archit.popularmovie.data.MovieFavoritesColumns;
import com.portfolio.archit.popularmovie.data.MovieProvider;
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
    private static final String SAVED_INSTANCE_SELECTED_ITEM = "selected_item";
    private static final String SAVED_INSTANCE_MOVIE_LIST = "movie_list";
    private static final String SAVED_INSTANCE_MOVIE_SORT_INDEX = "movie_sort_index";
    private static final String SAVED_INSTANCE_CURRENT_PAGE = "current_page";
    private static final String SAVED_INSTANCE_SHOULD_LOAD_NEXT_PAGE = "should_load_next_page";


    private TextView tvNoMovieFound;
    private RecyclerView gridView;
    private Spinner spinnerSort;
    private MovieRecyclerAdapter movieRecyclerAdapter;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private int currentPage = 1, retreivingPage = 1;

    private int selectedSortPropIndex = 0;
    private String selectedSortProperty = "";

    private String[] sortingProperties;

    private SimpleArcDialog mDialog;
    private boolean mIsMultiPane = false, isListLoadedOnCreate = false, shouldLoadNextPage = false;
    private int selectedListItem = 0;


    private static final String[] MOVIE_COLUMNS = {
            MovieFavoritesColumns._ID,
            MovieFavoritesColumns.MOVIE_ID,
            MovieFavoritesColumns.BACKDROP_PATH,
            MovieFavoritesColumns.ORIGINAL_TITLE,
            MovieFavoritesColumns.OVERVIEW,
            MovieFavoritesColumns.POPULARITY,
            MovieFavoritesColumns.POSTER_PATH,
            MovieFavoritesColumns.RELEASE_DATE,
            MovieFavoritesColumns.TITLE,
            MovieFavoritesColumns.VOTE_AVERAGE,
            MovieFavoritesColumns.VOTE_COUNT,
    };

    public final static int COL_ID = 0;
    public final static int COL_MOVIE_ID = 1;
    public final static int COL_BACK_DROP = 2;
    public final static int COL_ORIGINAL_TITLE = 3;
    public final static int COL_OVERVIEW = 4;
    public final static int COL_POPULARITY = 5;
    public final static int COL_POSTER_PATH = 6;
    public final static int COL_RELEASE_DATE = 7;
    public final static int COL_TITLE = 8;
    public final static int COL_VOTE_AVAERAGE = 9;
    public final static int COL_VOTE_COUNT = 10;


    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these must change.
    public static final int COL_WEATHER_ID = 0;
    public static final int COL_WEATHER_DATE = 1;
    public static final int COL_WEATHER_DESC = 2;
    public static final int COL_WEATHER_MAX_TEMP = 3;
    public static final int COL_WEATHER_MIN_TEMP = 4;
    public static final int COL_LOCATION_SETTING = 5;
    public static final int COL_WEATHER_CONDITION_ID = 6;
    public static final int COL_COORD_LAT = 7;
    public static final int COL_COORD_LONG = 8;

    public interface MovieListCallback {
        void onItemClickListener(int position, Movie movie);
    }

    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initView(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            selectedListItem = savedInstanceState.getInt(SAVED_INSTANCE_SELECTED_ITEM);
            movieArrayList = savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_MOVIE_LIST);
            selectedSortPropIndex = savedInstanceState.getInt(SAVED_INSTANCE_MOVIE_SORT_INDEX);
            currentPage = savedInstanceState.getInt(SAVED_INSTANCE_CURRENT_PAGE);
            shouldLoadNextPage = savedInstanceState.getBoolean(SAVED_INSTANCE_SHOULD_LOAD_NEXT_PAGE);
            isListLoadedOnCreate = true;
        }

        sortingProperties = getResources().getStringArray(R.array.sort_properties_values);

        gridView = (RecyclerView) mView.findViewById(R.id.gridMoviePoster);
        tvNoMovieFound = (TextView) mView.findViewById(R.id.tvNoMovieFound);


        movieRecyclerAdapter = new MovieRecyclerAdapter(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridView.setLayoutManager(gridLayoutManager);
        gridView.addItemDecoration(new EqualSpaceItemDecoration(16));
        gridView.setAdapter(movieRecyclerAdapter);


        spinnerSort = (Spinner) mView.findViewById(R.id.spinnerSort);
        spinnerSort.setSelection(selectedSortPropIndex);
        movieRecyclerAdapter.setDataList(currentPage, movieArrayList);

        mDialog = new SimpleArcDialog(mContext);
        mDialog.setConfiguration(new ArcConfiguration(mContext));

    }

    @Override
    protected void setListeners() {

        movieRecyclerAdapter.setMovieRecyclerItemListener(new RecyclerItemListener<Movie>() {
            @Override
            public int getCurrentPage() {
                return currentPage;
            }

            @Override
            public void onRecyclerItemSelected(int position, Movie movie) {
                if (Utils.isInternetAvailable(mContext)) {
                    selectedListItem = position;
                    ((MovieListCallback) getActivity()).onItemClickListener(position, movie);
                } else {
                    Toast.makeText(mContext, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScrolledToLast(int position, int nextPageIndex) {
                if (retreivingPage != nextPageIndex && shouldLoadNextPage) {
                    fetchData(nextPageIndex, selectedSortProperty);
                }
            }
        });

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String property = sortingProperties[position];

                if (!property.equalsIgnoreCase(selectedSortProperty)) {
                    selectedSortPropIndex = position;
                    selectedSortProperty = property;
                    currentPage = 1;
                    selectedListItem = 0;

                    if (!isListLoadedOnCreate) {
                        fetchData(1, property);
                    } else {
                        isListLoadedOnCreate = false;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onResume() {
        if (selectedSortProperty.equalsIgnoreCase(sortingProperties[2])) {
            fetchData(1, selectedSortProperty);
        }
        super.onResume();
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

    private void fetchData(final int page, String sortProp) {

        if (!sortProp.equalsIgnoreCase(sortingProperties[2])) {
            if (Utils.isInternetAvailable(mContext)) {
                retreivingPage = page;
                shouldLoadNextPage = true;

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
                                if (mIsMultiPane) {
                                    Movie movie = movieArrayList.get(0);
                                    ((MovieListCallback) getActivity()).onItemClickListener(0, movie);
                                }
                            }
                            tvNoMovieFound.setVisibility(View.GONE);
                            movieRecyclerAdapter.setDataList(currentPage, movieArrayList);
                        } else {
                            //No movie found
                            if (movieArrayList.size() == 0) {
                                tvNoMovieFound.setVisibility(View.VISIBLE);
                            }
                        }
                        mDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getLocalizedMessage());
                        Toast.makeText(mContext, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                        if (movieArrayList != null && movieArrayList.size() == 0) {
                            tvNoMovieFound.setVisibility(View.VISIBLE);
                        }
                    }
                });
                volleyHelper.addToRequestQueue(request);
            } else {
                Toast.makeText(mContext, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        } else {

            shouldLoadNextPage = false;
            currentPage = page;

            onFavoriteDataChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_SELECTED_ITEM, selectedListItem);
        outState.putParcelableArrayList(SAVED_INSTANCE_MOVIE_LIST, movieArrayList);
        outState.putInt(SAVED_INSTANCE_MOVIE_SORT_INDEX, selectedSortPropIndex);
        outState.putInt(SAVED_INSTANCE_CURRENT_PAGE, currentPage);
        outState.putBoolean(SAVED_INSTANCE_SHOULD_LOAD_NEXT_PAGE, shouldLoadNextPage);
    }

    public void onFavoriteDataChanged() {

        if (movieRecyclerAdapter != null && selectedSortProperty.equalsIgnoreCase(sortingProperties[2])) {
            ArrayList<Movie> favMovieArrayList = new ArrayList<>();
            Cursor cursor = getActivity().getContentResolver().query(MovieProvider.Favorites.CONTENT_URI, MOVIE_COLUMNS, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    favMovieArrayList.add(Movie.fromCursor(cursor));
                }
                cursor.close();

            }
            movieArrayList = favMovieArrayList;
            movieRecyclerAdapter.setDataList(1, favMovieArrayList);

            if (selectedListItem != 0 && selectedListItem >= favMovieArrayList.size()) {
                selectedListItem = 0;
            }

            if (favMovieArrayList.size() != 0) {
                if (mIsMultiPane) {
                    Movie movie = favMovieArrayList.get(selectedListItem);
                    ((MovieListCallback) getActivity()).onItemClickListener(selectedListItem, movie);
                }
                tvNoMovieFound.setVisibility(View.GONE);
            } else {
                //No favorites found
                tvNoMovieFound.setVisibility(View.VISIBLE);
                if (mIsMultiPane) {
                    ((MovieListCallback) getActivity()).onItemClickListener(0, null);
                }
            }
        }
    }

    public void setMultiPane(boolean isMultiPane) {
        this.mIsMultiPane = isMultiPane;
    }
}
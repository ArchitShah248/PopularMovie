package com.portfolio.archit.popularmovie.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.leo.simplearcloader.SimpleArcDialog;
import com.portfolio.archit.popularmovie.R;
import com.portfolio.archit.popularmovie.adapter.EqualSpaceItemDecoration;
import com.portfolio.archit.popularmovie.adapter.TrailersRecyclerAdapter;
import com.portfolio.archit.popularmovie.customviews.ReviewDialog;
import com.portfolio.archit.popularmovie.data.AppConstants;
import com.portfolio.archit.popularmovie.data.AppURLs;
import com.portfolio.archit.popularmovie.data.MovieFavoritesColumns;
import com.portfolio.archit.popularmovie.data.MovieProvider;
import com.portfolio.archit.popularmovie.httphelper.GsonRequest;
import com.portfolio.archit.popularmovie.httphelper.VolleyHelper;
import com.portfolio.archit.popularmovie.model.Movie;
import com.portfolio.archit.popularmovie.model.Trailer;
import com.portfolio.archit.popularmovie.model.TrailerList;
import com.portfolio.archit.popularmovie.utilities.Utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends BaseFragment {

    private static String TAG = MovieDetailFragment.class.getSimpleName();

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageLoader imageLoader;
    private AppBarLayout app_bar_layout;
    private Toolbar appBarMovieDetail;
    private Movie selectedMovie;
    private NetworkImageView imgVBackdrop;
    private ToggleButton tbFavourite;
    private TextView tvMovieTitle, tvMovieOverView, tvRatingTitle, tvReleaseDate, tvMovieReviews, tvNoMovieSelected, tvNoTrailerFound;
    private RecyclerView recycleviewMovieTrailers;
    private ScrollView scrollViewMovieDetail;
    private LinearLayout llDetailContainer;
    private TrailersRecyclerAdapter trailersRecyclerAdapter;
    private ArrayList<Trailer> trailerArrayList = new ArrayList<>();
    private SimpleArcDialog mDialog;
    private boolean mIsMultiPane = false;

    public interface MovieDetailCallbacks {
        void onFavouriteStateChanged();
    }

    public void setMovieDetailCallbacks(MovieDetailCallbacks movieDetailCallbacks) {
        this.movieDetailCallbacks = movieDetailCallbacks;
    }

    private MovieDetailCallbacks movieDetailCallbacks;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getContext();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mIsMultiPane = bundle.getBoolean(AppConstants.INTENT_KEY_IS_MULTIPANE);
        }
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_detail, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return mView;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {


        mDialog = new SimpleArcDialog(mContext);
        selectedMovie = getArguments().getParcelable(AppConstants.INTENT_KEY_MOVIE_DETAIL);

        imageLoader = VolleyHelper.getInstance(mContext).getImageLoader();

        app_bar_layout = (AppBarLayout) mView.findViewById(R.id.app_bar_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) mView.findViewById(R.id.collapsing_toolbar_layout);

        appBarMovieDetail = (Toolbar) mView.findViewById(R.id.appBarMovieDetail);
        tvMovieTitle = (TextView) mView.findViewById(R.id.tvMovieTitle);
        if (mIsMultiPane) {
            appBarMovieDetail.setVisibility(View.GONE);
            tvMovieTitle.setVisibility(View.VISIBLE);
        } else {
            tvMovieTitle.setVisibility(View.GONE);
        }

        llDetailContainer = (LinearLayout) mView.findViewById(R.id.llDetailContainer);
        scrollViewMovieDetail = (ScrollView) mView.findViewById(R.id.scrollViewMovieDetail);
        imgVBackdrop = (NetworkImageView) mView.findViewById(R.id.imgVBackdrop);
        tbFavourite = (ToggleButton) mView.findViewById(R.id.tbFavourite);
        tvMovieOverView = (TextView) mView.findViewById(R.id.tvMovieOverView);
        tvRatingTitle = (TextView) mView.findViewById(R.id.tvRatingTitle);
        tvReleaseDate = (TextView) mView.findViewById(R.id.tvReleaseDate);
        tvMovieReviews = (TextView) mView.findViewById(R.id.tvMovieReviews);
        tvNoMovieSelected = (TextView) mView.findViewById(R.id.tvNoMovieSelected);
        tvNoTrailerFound = (TextView) mView.findViewById(R.id.tvNoTrailerFound);

        imgVBackdrop.setDefaultImageResId(R.drawable.no_preview_available);

        recycleviewMovieTrailers = (RecyclerView) mView.findViewById(R.id.recycleviewMovieTrailers);
        recycleviewMovieTrailers.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        trailersRecyclerAdapter = new TrailersRecyclerAdapter(mContext);
        trailersRecyclerAdapter.setDataList(0, trailerArrayList);


        if (selectedMovie != null) {
            tvNoMovieSelected.setVisibility(View.GONE);
            llDetailContainer.setVisibility(View.VISIBLE);
            tbFavourite.setVisibility(View.VISIBLE);

            collapsingToolbarLayout.setTitle(selectedMovie.getTitle());
            tvMovieTitle.setText(selectedMovie.getTitle());

            if (selectedMovie != null && !Utils.isStringEmpty(selectedMovie.getBackdropPath())) {
                String url = AppURLs.MOVIE_DB_POSTER_BASE_URL + selectedMovie.getBackdropPath();
                imgVBackdrop.setImageUrl(url, imageLoader);
            }

            if (!Utils.isStringEmpty(selectedMovie.getOverview())) {
                tvMovieOverView.setText(selectedMovie.getOverview());
            } else {
                tvMovieOverView.setText(getString(R.string.no_overview));
            }

            String ratings = mContext.getString(R.string.rating_title) + " " + selectedMovie.getVoteAverage() + "/10";
            tvRatingTitle.setText(ratings);


            String date = getString(R.string.release_date_title) + " ";

            if (!Utils.isStringEmpty(selectedMovie.getReleaseDate())) {
                date = date + selectedMovie.getReleaseDate();
            } else {
                date = date + getString(R.string.not_available);
            }

            tvReleaseDate.setText(date);

            recycleviewMovieTrailers.setAdapter(trailersRecyclerAdapter);
            recycleviewMovieTrailers.addItemDecoration(new EqualSpaceItemDecoration(8));

            String where = MovieFavoritesColumns.MOVIE_ID + " = ? ";
            String[] args = {String.valueOf(selectedMovie.getId())};

            Cursor cursor = getActivity().getContentResolver().query(MovieProvider.Favorites.CONTENT_URI,
                    null, where, args, null);

            if (cursor != null && cursor.getCount() > 0) {
                tbFavourite.setChecked(true);
            } else {
                tbFavourite.setChecked(false);
            }

            fetchTrailers();
        } else {
            //No movie selected
            tvNoMovieSelected.setVisibility(View.VISIBLE);
            llDetailContainer.setVisibility(View.GONE);
            tbFavourite.setVisibility(View.GONE);

        }
    }

    @Override
    protected void setListeners() {

        tbFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    ContentValues contentValues = new ContentValues();

                    contentValues.put(MovieFavoritesColumns.MOVIE_ID, selectedMovie.getId());
                    contentValues.put(MovieFavoritesColumns.ADULT, selectedMovie.isAdult());
                    contentValues.put(MovieFavoritesColumns.BACKDROP_PATH, selectedMovie.getBackdropPath());
                    contentValues.put(MovieFavoritesColumns.GENRE_IDS, Arrays.toString(selectedMovie.getGenreIds()));
                    contentValues.put(MovieFavoritesColumns.ORIGINAL_TITLE, selectedMovie.getOriginalTitle());
                    contentValues.put(MovieFavoritesColumns.OVERVIEW, selectedMovie.getOverview());
                    contentValues.put(MovieFavoritesColumns.POPULARITY, selectedMovie.getPopularity());
                    contentValues.put(MovieFavoritesColumns.POSTER_PATH, selectedMovie.getPosterPath());
                    contentValues.put(MovieFavoritesColumns.RELEASE_DATE, selectedMovie.getReleaseDate());
                    contentValues.put(MovieFavoritesColumns.TITLE, selectedMovie.getTitle());
                    contentValues.put(MovieFavoritesColumns.VOTE_AVERAGE, selectedMovie.getVoteAverage());
                    contentValues.put(MovieFavoritesColumns.VOTE_COUNT, selectedMovie.getVoteCount());
                    contentValues.put(MovieFavoritesColumns.VIDEO, selectedMovie.isVideo());

                    Uri uri = getActivity().getContentResolver().insert(MovieProvider.Favorites.withId(selectedMovie.getId()), contentValues);
                    Log.d(TAG, "Movie inserted: " + uri);

                } else {

                    int id = getActivity().getContentResolver().delete(MovieProvider.Favorites.withId(selectedMovie.getId()), null, null);
                    Log.d(TAG, "Movie deleted: " + id);

                }

                if (movieDetailCallbacks != null) {
                    movieDetailCallbacks.onFavouriteStateChanged();
                }

            }
        });

        appBarMovieDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        trailersRecyclerAdapter.setOnItemClickedListener(new TrailersRecyclerAdapter.OnItemClickedListener() {
            @Override
            public void onGridItemSelected(int position, Trailer trailer) {
                if (Utils.isInternetAvailable(mContext)) {
                    playVideo(trailer.getKey());
                } else {
                    Toast.makeText(mContext, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvMovieReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isInternetAvailable(mContext)) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(AppConstants.INTENT_KEY_MOVIE_DETAIL, selectedMovie);
                    ReviewsFragment reviewsFragment = new ReviewsFragment();
                    reviewsFragment.setArguments(bundle);

                    if (mIsMultiPane) {
//                    getFragmentManager().beginTransaction().replace(R.id.detail_container, reviewsFragment).commit();
                        ReviewDialog reviewDialog = ReviewDialog.getInstance(bundle);
                        reviewDialog.show(getFragmentManager(), "ReviewDialog");

                    } else {
                        getFragmentManager().beginTransaction().replace(R.id.detail_container, reviewsFragment).addToBackStack("ReviewFragment").commit();
                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    private void fetchTrailers() {

        String url = AppURLs.MOVIE_DB_BASE_URL + String.format(AppURLs.MOVIE_DB_TRAILER_BASE_URL, selectedMovie.getId()) + AppURLs.MOVIE_PARA_API_KEY;
        if (Utils.isInternetAvailable(mContext)) {
            mDialog.show();
            Log.i(TAG, "Movie URL: " + url);
            VolleyHelper volleyHelper = VolleyHelper.getInstance(mContext);

            GsonRequest gsonRequest = new GsonRequest(url, TrailerList.class, null, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    TrailerList trailerList = (TrailerList) response;

                    if (trailerList != null && trailerList.getTrailerList().size() > 0) {
                        tvNoTrailerFound.setVisibility(View.GONE);
                        trailerArrayList = trailerList.getTrailerList();
                        trailersRecyclerAdapter.setDataList(1, trailerArrayList);
                    } else {
                        //No trailer found
                        tvNoTrailerFound.setVisibility(View.VISIBLE);
                    }
                    mDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    tvNoTrailerFound.setVisibility(View.VISIBLE);
                    Log.e(TAG, "Error: " + error.getLocalizedMessage());
                    Toast.makeText(mContext, getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }
            });
            volleyHelper.addToRequestQueue(gsonRequest);

        } else {
            Toast.makeText(mContext, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

    }

    private void playVideo(String youTubeKey) {
        String url = String.format(AppURLs.YOUTUBE_VIDEO_BASE_URL, youTubeKey);
        Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(youTubeIntent);
    }
}
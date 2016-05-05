package com.portfolio.archit.popularmovie.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.leo.simplearcloader.SimpleArcDialog;
import com.portfolio.archit.popularmovie.R;
import com.portfolio.archit.popularmovie.adapter.EqualSpaceItemDecoration;
import com.portfolio.archit.popularmovie.adapter.TrailersRecyclerAdapter;
import com.portfolio.archit.popularmovie.data.AppConstants;
import com.portfolio.archit.popularmovie.data.AppURLs;
import com.portfolio.archit.popularmovie.httphelper.GsonRequest;
import com.portfolio.archit.popularmovie.httphelper.VolleyHelper;
import com.portfolio.archit.popularmovie.model.Movie;
import com.portfolio.archit.popularmovie.model.Trailer;
import com.portfolio.archit.popularmovie.model.TrailerList;
import com.portfolio.archit.popularmovie.utilities.Utils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends BaseFragment {

    private static String TAG = MovieDetailFragment.class.getSimpleName();

    private ImageLoader imageLoader;
    private Movie selectedMovie;
    private NetworkImageView imgVBackdrop;
    private TextView tvMovieOverView, tvMovieTitle, tvRatingTitle, tvReleaseDate, tvMovieReviews;
    private RecyclerView recycleviewMovieTrailers;
    private TrailersRecyclerAdapter trailersRecyclerAdapter;
    private ArrayList<Trailer> trailerArrayList = new ArrayList<>();
    private SimpleArcDialog mDialog;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getContext();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_detail, container, false);
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

    @Override
    protected void initView(Bundle savedInstanceState) {

        mDialog = new SimpleArcDialog(mContext);
        selectedMovie = getArguments().getParcelable(AppConstants.INTENT_KEY_MOVIE_DETAIL);

        imageLoader = VolleyHelper.getInstance(mContext).getImageLoader();

        imgVBackdrop = (NetworkImageView) mView.findViewById(R.id.imgVBackdrop);
        tvMovieOverView = (TextView) mView.findViewById(R.id.tvMovieOverView);
        tvMovieTitle = (TextView) mView.findViewById(R.id.tvMovieTitle);
        tvRatingTitle = (TextView) mView.findViewById(R.id.tvRatingTitle);
        tvReleaseDate = (TextView) mView.findViewById(R.id.tvReleaseDate);
        tvMovieReviews = (TextView) mView.findViewById(R.id.tvMovieReviews);

        imgVBackdrop.setDefaultImageResId(R.drawable.no_preview_available);
        if (selectedMovie != null && !Utils.isStringEmpty(selectedMovie.getBackdropPath())) {
            String url = AppURLs.MOVIE_DB_POSTER_BASE_URL + selectedMovie.getBackdropPath();
            imgVBackdrop.setImageUrl(url, imageLoader);
        }

        tvMovieTitle.setText(selectedMovie.getOriginalTitle());

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


        recycleviewMovieTrailers = (RecyclerView) mView.findViewById(R.id.recycleviewMovieTrailers);
        recycleviewMovieTrailers.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        trailersRecyclerAdapter = new TrailersRecyclerAdapter(mContext);
        trailersRecyclerAdapter.setDataList(0, trailerArrayList);

        recycleviewMovieTrailers.setAdapter(trailersRecyclerAdapter);
        recycleviewMovieTrailers.addItemDecoration(new EqualSpaceItemDecoration(8));
        fetchTrailers();

    }

    @Override
    protected void setListeners() {
        trailersRecyclerAdapter.setOnItemClickedListener(new TrailersRecyclerAdapter.OnItemClickedListener() {
            @Override
            public void onGridItemSelected(int position, Trailer trailer) {
                playVideo(trailer.getKey());
            }
        });

        tvMovieReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putParcelable(AppConstants.INTENT_KEY_MOVIE_DETAIL, selectedMovie);
                ReviewsFragment reviewsFragment = new ReviewsFragment();
                reviewsFragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.detail_container, reviewsFragment).addToBackStack("ReviewFragment").commit();

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

                        trailerArrayList = trailerList.getTrailerList();
                        trailersRecyclerAdapter.setDataList(0, trailerArrayList);
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
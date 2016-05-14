package com.portfolio.archit.popularmovie.customviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.portfolio.archit.popularmovie.R;
import com.portfolio.archit.popularmovie.adapter.EqualSpaceItemDecoration;
import com.portfolio.archit.popularmovie.adapter.RecyclerItemListener;
import com.portfolio.archit.popularmovie.adapter.ReviewsRecyclerAdapter;
import com.portfolio.archit.popularmovie.data.AppConstants;
import com.portfolio.archit.popularmovie.data.AppURLs;
import com.portfolio.archit.popularmovie.httphelper.GsonRequest;
import com.portfolio.archit.popularmovie.httphelper.VolleyHelper;
import com.portfolio.archit.popularmovie.model.Movie;
import com.portfolio.archit.popularmovie.model.Review;
import com.portfolio.archit.popularmovie.model.ReviewList;
import com.portfolio.archit.popularmovie.utilities.Utils;

import java.util.ArrayList;

/**
 * Created by Archit Shah on 5/7/2016.
 */
public class ReviewDialog extends DialogFragment {

    private static final String TAG = ReviewDialog.class.getSimpleName();

    private Context mContext;
    private View mView;
    private Toolbar appBarMovieReview;

    private RecyclerView recyclerView;
    private TextView tvNoReviewsAvailable;
    private Movie selectedMovie;
    private ReviewsRecyclerAdapter reviewsRecyclerAdapter;
    private ArrayList<Review> reviewArrayList = new ArrayList<>();
    private int currentPage = 1, retreivingPage = 1;
    private boolean isLastPage = false;

    private SimpleArcDialog mDialog;

    public static ReviewDialog getInstance(Bundle args) {
        ReviewDialog reviewDialog = new ReviewDialog();
        reviewDialog.setArguments(args);
        return reviewDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContext = getContext();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_review_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        initView(savedInstanceState);
        setListeners();
        return mView;
    }

    protected void initView(Bundle savedInstanceState) {

        selectedMovie = getArguments().getParcelable(AppConstants.INTENT_KEY_MOVIE_DETAIL);

        appBarMovieReview = (Toolbar) mView.findViewById(R.id.appBarMovieReview);
        appBarMovieReview.setVisibility(View.GONE);

        tvNoReviewsAvailable = (TextView) mView.findViewById(R.id.tvNoReviewsAvailable);
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycleviewMovieReviews);

        reviewsRecyclerAdapter = new ReviewsRecyclerAdapter(mContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new EqualSpaceItemDecoration(8));
        recyclerView.setAdapter(reviewsRecyclerAdapter);


        reviewsRecyclerAdapter.setDataList(currentPage, false, reviewArrayList);

        mDialog = new SimpleArcDialog(mContext);
        mDialog.setConfiguration(new ArcConfiguration(mContext));

        fetchData(currentPage);
    }

    protected void setListeners() {

        reviewsRecyclerAdapter.setOnItemClickedListener(new RecyclerItemListener<Review>() {
            @Override
            public int getCurrentPage() {
                return currentPage;
            }

            @Override
            public void onRecyclerItemSelected(int position, Review model) {

            }

            @Override
            public void onScrolledToLast(int position, int nextPageIndex) {
                if (retreivingPage != nextPageIndex) {
                    fetchData(nextPageIndex);
                }
            }
        });

    }


    private void fetchData(final int page) {
        if (Utils.isInternetAvailable(mContext)) {

            String url = AppURLs.MOVIE_DB_BASE_URL + String.format(AppURLs.MOVIE_DB_REVIEWS_BASE_URL, selectedMovie.getId()) + AppURLs.MOVIE_PARA_API_KEY + AppURLs.PARA_PAGE + page;

            mDialog.show();
            Log.i(TAG, "Movie review URL: " + url);
            VolleyHelper volleyHelper = VolleyHelper.getInstance(mContext);

            GsonRequest<ReviewList> request = new GsonRequest<>(url, ReviewList.class, null, new Response.Listener<ReviewList>() {
                @Override
                public void onResponse(ReviewList response) {

                    if (response != null && response.getReviewArrayList().size() > 0) {

                        isLastPage = page == response.getTotalPages();

                        currentPage = page;

                        Log.i(TAG, "Review current page: " + response.getPage() + " Total page: " + response.getTotalPages());

                        if (currentPage != 1) {
                            reviewArrayList.addAll(response.getReviewArrayList());
                        } else {
                            reviewArrayList = response.getReviewArrayList();
                        }

                        reviewsRecyclerAdapter.setDataList(currentPage, isLastPage, reviewArrayList);
                    }

                    if (reviewArrayList.size() == 0) {
                        tvNoReviewsAvailable.setVisibility(View.VISIBLE);
                    } else {
                        tvNoReviewsAvailable.setVisibility(View.GONE);
                    }

                    mDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (reviewArrayList.size() == 0) {
                        tvNoReviewsAvailable.setVisibility(View.VISIBLE);
                    }
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

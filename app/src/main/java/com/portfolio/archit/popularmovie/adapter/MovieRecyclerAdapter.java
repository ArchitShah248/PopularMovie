package com.portfolio.archit.popularmovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.portfolio.archit.popularmovie.R;
import com.portfolio.archit.popularmovie.data.AppConstants;
import com.portfolio.archit.popularmovie.data.AppURLs;
import com.portfolio.archit.popularmovie.httphelper.VolleyHelper;
import com.portfolio.archit.popularmovie.model.Movie;
import com.portfolio.archit.popularmovie.utilities.Utils;

import java.util.ArrayList;

/**
 * Created by Archit Shah on 5/1/2016.
 */
public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    private static final String TAG = MovieRecyclerAdapter.class.getSimpleName();
    private Context mContext;
    private ImageLoader imageLoader;
    private LayoutInflater layoutInflater;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private int currentPageIndex = 1;


    private RecyclerItemListener<Movie> movieRecyclerItemListener;

    public void setMovieRecyclerItemListener(RecyclerItemListener<Movie> movieRecyclerItemListener) {
        this.movieRecyclerItemListener = movieRecyclerItemListener;
    }

    public MovieRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = VolleyHelper.getInstance(mContext).getImageLoader();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = layoutInflater.inflate(R.layout.grid_cell_poster_image, null);

        MovieViewHolder viewHolder = new MovieViewHolder(convertView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder viewHolder, final int position) {

        final Movie movie = movieArrayList.get(position);
        viewHolder.tvMovieName.setText(movie.getTitle());

        if (!Utils.isStringEmpty(movie.getPosterPath())) {
            String url = AppURLs.MOVIE_DB_IMAGE_BASE_URL + movie.getPosterPath();
            viewHolder.imgVMoviePoster.setImageUrl(url, imageLoader);
        } else {
            viewHolder.imgVMoviePoster.setImageResource(R.drawable.no_preview_available);
        }

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieRecyclerItemListener != null) {
                    movieRecyclerItemListener.onRecyclerItemSelected(position, movie);
                }
            }
        });
        if (movieRecyclerItemListener != null) {
            int nextPageIndex = movieRecyclerItemListener.getCurrentPage() + 1;
            Log.v(TAG, "Page: " + nextPageIndex + " Current page: " + currentPageIndex + " Item Posi: " + position + " Size: " + movieArrayList.size());
            if (currentPageIndex < AppConstants.ALLOWED_MAX_PAGES && currentPageIndex != nextPageIndex
                    && movieArrayList.size() > 0 && movieArrayList.size() == (position + 1)) {
                Log.v(TAG, "Page scrolled to end. Posi: " + position);
                movieRecyclerItemListener.onScrolledToLast(position, nextPageIndex);
            }
        }
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }


    public void setDataList(int pageIndex, ArrayList<Movie> list) {
        movieArrayList = list;
        currentPageIndex = pageIndex;
        notifyDataSetChanged();
    }
}

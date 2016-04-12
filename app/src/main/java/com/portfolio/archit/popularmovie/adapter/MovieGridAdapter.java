package com.portfolio.archit.popularmovie.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
 * Created by Archit Shah on 4/9/2016.
 */
public class MovieGridAdapter extends BaseAdapter {

    private static final String TAG = MovieGridAdapter.class.getSimpleName();
    private Context mContext;
    private ImageLoader imageLoader;
    private LayoutInflater layoutInflater;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private int currentPageIndex = 1;

    public interface OnGridItemClickedListener {
        int getCurrentPage();

        void onGridItemSelected(int position, Movie movie);

        void onScrolledToLast(int position, int nextPageIndex);
    }

    private OnGridItemClickedListener onGridItemClickedListener;

    public void setOnGridItemClickedListener(OnGridItemClickedListener onGridItemClickedListener) {
        this.onGridItemClickedListener = onGridItemClickedListener;
    }

    public MovieGridAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = VolleyHelper.getInstance(mContext).getImageLoader();
    }

    @Override
    public int getCount() {
        return movieArrayList.size();
    }

    @Override
    public Object getItem(int position) {


        return movieArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        public NetworkImageView imgVMoviePoster;
        public TextView tvMovieName;

        public ViewHolder(View parent) {
            imgVMoviePoster = (NetworkImageView) parent.findViewById(R.id.imgVMoviePoster);
            tvMovieName = (TextView) parent.findViewById(R.id.tvMovieTitle);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.grid_cell_poster_image, null);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Movie movie = movieArrayList.get(position);
        viewHolder.tvMovieName.setText(movie.getTitle());

        if (!Utils.isStringEmpty(movie.getPosterPath())) {
            String url = AppURLs.MOVIE_DB_IMAGE_BASE_URL + movie.getPosterPath();
            viewHolder.imgVMoviePoster.setImageUrl(url, imageLoader);
        } else {
            viewHolder.imgVMoviePoster.setImageResource(R.drawable.no_preview_available);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onGridItemClickedListener != null) {
                    onGridItemClickedListener.onGridItemSelected(position, movie);
                }
            }
        });
        if (onGridItemClickedListener != null) {
            int nextPageIndex = onGridItemClickedListener.getCurrentPage() + 1;
            Log.v(TAG, "Page: " + nextPageIndex + " Current page: " + currentPageIndex + " Item Posi: "
                    + position + " Size: " + movieArrayList.size());
            if (currentPageIndex < AppConstants.ALLOWED_MAX_PAGES && currentPageIndex != nextPageIndex
                    && movieArrayList.size() > 0 && movieArrayList.size() == (position + 1)) {
                Log.v(TAG, "Page scrolled to end. Posi: " + position);
                onGridItemClickedListener.onScrolledToLast(position, nextPageIndex);
            }
        }
        return convertView;
    }

    public void setDataList(int pageIndex, ArrayList<Movie> list) {
        movieArrayList = list;
        currentPageIndex = pageIndex;
        notifyDataSetChanged();
    }
}

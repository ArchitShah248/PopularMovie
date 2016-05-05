package com.portfolio.archit.popularmovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.portfolio.archit.popularmovie.R;
import com.portfolio.archit.popularmovie.data.AppURLs;
import com.portfolio.archit.popularmovie.httphelper.VolleyHelper;
import com.portfolio.archit.popularmovie.model.Trailer;
import com.portfolio.archit.popularmovie.utilities.Utils;

import java.util.ArrayList;

/**
 * Created by Archit Shah on 5/1/2016.
 */
public class TrailersRecyclerAdapter extends RecyclerView.Adapter<TrailersRecyclerAdapter.ViewHolder> {

    private static final String TAG = TrailersRecyclerAdapter.class.getSimpleName();
    private Context mContext;
    private ImageLoader imageLoader;
    private LayoutInflater layoutInflater;
    private ArrayList<Trailer> trailerArrayList = new ArrayList<>();
    private int currentPageIndex = 1;


    public interface OnItemClickedListener {
        void onGridItemSelected(int position, Trailer trailer);
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public TrailersRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = VolleyHelper.getInstance(mContext).getImageLoader();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = layoutInflater.inflate(R.layout.list_item_trailer, null);

        ViewHolder viewHolder = new ViewHolder(convertView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final Trailer trailer = trailerArrayList.get(position);
        viewHolder.tvTrailerTitle.setText(trailer.getName());

        if (!Utils.isStringEmpty(trailer.getKey())) {
            String url = String.format(AppURLs.YOUTUBE_MOVIE_THUMBNAIL_BASE_URL, trailer.getKey());
            viewHolder.imgVMovieTrailer.setImageUrl(url, imageLoader);
        } else {
            viewHolder.imgVMovieTrailer.setImageResource(R.drawable.no_preview_available);
        }

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedListener != null) {
                    onItemClickedListener.onGridItemSelected(position, trailer);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return trailerArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View root;
        public NetworkImageView imgVMovieTrailer;
        public TextView tvTrailerTitle;

        public ViewHolder(View parent) {
            super(parent);
            root = parent;
            imgVMovieTrailer = (NetworkImageView) parent.findViewById(R.id.imgVMovieTrailer);
            tvTrailerTitle = (TextView) parent.findViewById(R.id.tvTrailerTitle);
        }
    }


    public void setDataList(int pageIndex, ArrayList<Trailer> list) {
        trailerArrayList = list;
        currentPageIndex = pageIndex;
        notifyDataSetChanged();
    }
}

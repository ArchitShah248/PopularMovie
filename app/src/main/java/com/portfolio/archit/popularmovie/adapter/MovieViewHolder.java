package com.portfolio.archit.popularmovie.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.portfolio.archit.popularmovie.R;

/**
 * Created by Archit Shah on 5/11/2016.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {

    public View root;
    public NetworkImageView imgVMoviePoster;
    public TextView tvMovieName;

    public MovieViewHolder(View parent) {
        super(parent);
        root = parent;
        imgVMoviePoster = (NetworkImageView) parent.findViewById(R.id.imgVMoviePoster);
        tvMovieName = (TextView) parent.findViewById(R.id.tvMovieTitle);
    }

}

package com.portfolio.archit.popularmovie.adapter;

import com.portfolio.archit.popularmovie.model.Movie;

/**
 * Created by Archit Shah on 5/4/2016.
 */
public interface RecyclerItemListener<T> {
    int getCurrentPage();
    void onRecyclerItemSelected(int position, T model);
    void onScrolledToLast(int position, int nextPageIndex);
}

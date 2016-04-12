package com.portfolio.archit.popularmovie.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Archit Shah on 4/10/2016.
 */
public class MovieList {

    @SerializedName("results")
    private ArrayList<Movie> movieArrayList = new ArrayList<>();

    @SerializedName("page")
    private int currentPage;

    @SerializedName("total_pages")
    private int totalPages;

    public ArrayList<Movie> getMovieArrayList() {
        return movieArrayList;
    }

    public void setMovieArrayList(ArrayList<Movie> movieArrayList) {
        this.movieArrayList = movieArrayList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}

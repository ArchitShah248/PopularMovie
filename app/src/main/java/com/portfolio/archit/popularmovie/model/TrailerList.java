package com.portfolio.archit.popularmovie.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Archit Shah on 5/3/2016.
 */
public class TrailerList {

    private String id;

    @SerializedName("results")
    private ArrayList<Trailer> trailerList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Trailer> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(ArrayList<Trailer> trailerList) {
        this.trailerList = trailerList;
    }
}

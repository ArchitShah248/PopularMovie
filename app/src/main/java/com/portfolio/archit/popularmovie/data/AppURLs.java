package com.portfolio.archit.popularmovie.data;

/**
 * Created by Archit Shah on 4/9/2016.
 */
public class AppURLs {

    public static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3";
    public static final String MOVIE_ACTION_DISCOVER = "/discover/movie";
    public static final String MOVIE_PARA_API_KEY = "?api_key=" + AppConstants.MOVIE_DB_API_KEY;
    public static final String PARA_SORT_BY = "&sort_by=";
    public static final String PARA_PAGE = "&page=";

//    public static final String SORT_BY_POPULARITY_DESC = "popularity.desc";
//    public static final String SORT_BY_POPULARITY_ASC = "popularity.asc";



    public static final String MOVIE_DB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String MOVIE_DB_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";
}

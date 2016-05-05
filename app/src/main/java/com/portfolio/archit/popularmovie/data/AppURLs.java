package com.portfolio.archit.popularmovie.data;

/**
 * Created by Archit Shah on 4/9/2016.
 */
public class AppURLs {

    public static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3";
    public static final String MOVIE_ACTION_POPULAR = "/movie/popular";
    public static final String MOVIE_ACTION_TOP_RATED = "/movie/top_rated";

    public static final String MOVIE_PARA_API_KEY = "?api_key=" + AppConstants.MOVIE_DB_API_KEY;
    public static final String PARA_PAGE = "&page=";

    public static final String MOVIE_DB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String MOVIE_DB_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";

    public static final String MOVIE_DB_TRAILER_BASE_URL = "/movie/%s/videos";
    public static final String MOVIE_DB_REVIEWS_BASE_URL = "/movie/%s/reviews";

    public static final String YOUTUBE_VIDEO_BASE_URL = "https://www.youtube.com/watch?v=%s";
    public static final String YOUTUBE_MOVIE_THUMBNAIL_BASE_URL = "http://img.youtube.com/vi/%s/sddefault.jpg";


}

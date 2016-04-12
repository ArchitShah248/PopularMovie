package com.portfolio.archit.popularmovie;

import android.app.Application;

import com.portfolio.archit.popularmovie.data.AppConstants;
import com.portfolio.archit.popularmovie.utilities.ValidAPIKeyException;

/**
 * Created by Archit Shah on 4/9/2016.
 */
public class PopularMovieApp extends Application{

    @Override
    public void onCreate() {

        if(AppConstants.MOVIE_DB_API_KEY.equalsIgnoreCase(AppConstants.DUMMY_API_MSG)){
            throw new ValidAPIKeyException("Please enter your 'The Movie DB' API key in " + AppConstants.class.getSimpleName() + ".java file.");
        }

        super.onCreate();
    }
}

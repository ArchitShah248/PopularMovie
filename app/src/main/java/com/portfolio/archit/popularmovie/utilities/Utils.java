package com.portfolio.archit.popularmovie.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Archit Shah on 4/10/2016.
 */
public class Utils {

    public static boolean isStringEmpty(String str) {
        if (str != null && str.length() > 0) {
            return false;
        }
        return true;
    }


    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}

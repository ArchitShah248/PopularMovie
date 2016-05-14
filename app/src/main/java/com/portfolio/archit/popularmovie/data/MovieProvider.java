package com.portfolio.archit.popularmovie.data;

import android.content.ContentResolver;
import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Archit Shah on 5/6/2016.
 */
@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public final class MovieProvider {

    public static final String AUTHORITY = "com.portfolio.archit.popularmovie.data.MovieProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String FAVORITES = "favourites";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.FAVORITES)
    public static class Favorites {

        @ContentUri(
                path = Path.FAVORITES,
                type = ContentResolver.CURSOR_DIR_BASE_TYPE + "/favorite")
        public static final Uri CONTENT_URI = buildUri(Path.FAVORITES);

        @InexactContentUri(
                name = "MOVIE_ID",
                path = Path.FAVORITES + "/#",
                type = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/favorite",
                whereColumn = MovieFavoritesColumns.MOVIE_ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.FAVORITES, String.valueOf(id));
        }
    }

}

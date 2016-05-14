package com.portfolio.archit.popularmovie.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Archit Shah on 5/6/2016.
 */
@Database(version = MovieDatabase.VERSION)
public class MovieDatabase {

    public static final int VERSION = 1;

    @Table(MovieFavoritesColumns.class)
    public static final String FAVORITES = "favorites";

}

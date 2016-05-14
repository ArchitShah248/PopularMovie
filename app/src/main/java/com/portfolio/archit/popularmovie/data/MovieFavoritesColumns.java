package com.portfolio.archit.popularmovie.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

/**
 * Created by Archit Shah on 5/6/2016.
 */
public interface MovieFavoritesColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement public static final String _ID = "_id";
    @DataType(DataType.Type.INTEGER) @Unique @NotNull public static final String MOVIE_ID = "movie_id";
    @DataType(DataType.Type.TEXT)  public static final String TITLE = "title";
    @DataType(DataType.Type.TEXT)  public static final String ORIGINAL_TITLE = "original_title";
    @DataType(DataType.Type.TEXT)  public static final String BACKDROP_PATH = "backdrop_path";
    @DataType(DataType.Type.REAL)  public static final String POPULARITY = "popularity";
    @DataType(DataType.Type.INTEGER)  public static final String VOTE_COUNT = "vote_count";
    @DataType(DataType.Type.INTEGER)  public static final String VIDEO = "video";
    @DataType(DataType.Type.REAL)  public static final String VOTE_AVERAGE = "vote_average";
    @DataType(DataType.Type.TEXT)  public static final String POSTER_PATH = "poster_path";
    @DataType(DataType.Type.INTEGER)  public static final String ADULT = "adult";
    @DataType(DataType.Type.TEXT)  public static final String OVERVIEW = "overview";
    @DataType(DataType.Type.TEXT)  public static final String RELEASE_DATE = "release_date";
    @DataType(DataType.Type.TEXT)  public static final String GENRE_IDS = "genre_ids";

}

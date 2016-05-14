package com.portfolio.archit.popularmovie.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.portfolio.archit.popularmovie.fragments.MovieListFragment;

/**
 * Created by Archit Shah on 4/9/2016.
 */
public class Movie implements Parcelable {

    private long id;
    private String title;

    @SerializedName("original_title")
    private String originalTitle;
    private String overview;
    private double popularity;

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("vote_average")
    private String voteAverage;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("release_date")
    private String releaseDate;
    private boolean adult;

    @SerializedName("genre_ids")
    private int[] genreIds;

    @SerializedName("original_language")
    private String originalLanguage;

    private boolean video;


    public Movie() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public static Movie fromCursor(Cursor cursor) {
        Movie movie = new Movie();

        movie.setId(cursor.getLong(MovieListFragment.COL_MOVIE_ID));
        movie.setBackdropPath(cursor.getString(MovieListFragment.COL_BACK_DROP));
        movie.setOriginalTitle(cursor.getString(MovieListFragment.COL_ORIGINAL_TITLE));
        movie.setOverview(cursor.getString(MovieListFragment.COL_OVERVIEW));
        movie.setPopularity(cursor.getDouble(MovieListFragment.COL_POPULARITY));
        movie.setPosterPath(cursor.getString(MovieListFragment.COL_POSTER_PATH));
        movie.setReleaseDate(cursor.getString(MovieListFragment.COL_RELEASE_DATE));
        movie.setTitle(cursor.getString(MovieListFragment.COL_TITLE));
        movie.setVoteAverage(cursor.getString(MovieListFragment.COL_VOTE_AVAERAGE));
        movie.setVoteCount(cursor.getInt(MovieListFragment.COL_VOTE_COUNT));


        return movie;
    }

    protected Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        popularity = in.readDouble();
        voteCount = in.readInt();
        voteAverage = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        releaseDate = in.readString();
        adult = in.readByte() != 0x00;
        originalLanguage = in.readString();
        video = in.readByte() != 0x00;
        genreIds = in.createIntArray();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeDouble(popularity);
        dest.writeInt(voteCount);
        dest.writeString(voteAverage);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(releaseDate);
        dest.writeByte((byte) (adult ? 0x01 : 0x00));
        dest.writeString(originalLanguage);
        dest.writeByte((byte) (video ? 0x01 : 0x00));
        dest.writeIntArray(genreIds);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

package com.example.moviedicoding.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private int id;
    private String title;
    private double popularity;
    private String overview;
    private String releaseDate;
    private double voteAverage;
    private String posterPath;

    public Movie(int id, String title, double popularity, String overview, String releaseDate, double voteAverage, String posterPath) {
        this.id = id;
        this.title = title;
        this.popularity = popularity;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        popularity = in.readDouble();
        overview = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        posterPath = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeDouble(popularity);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeString(posterPath);
    }
}

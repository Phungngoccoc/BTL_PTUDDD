package com.example.necomovie.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Movie implements Parcelable {
    public String id;
    public String original_language;
    public String title;
    public String original_title;
    public String overview;
    public double popularity;
    public String poster_path;
    public String release_date;

    protected Movie(Parcel in) {
        id = in.readString();
        original_language = in.readString();
        title = in.readString();
        original_title = in.readString();
        overview = in.readString();
        popularity = in.readDouble();
        poster_path = in.readString();
        release_date = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(original_language);
        parcel.writeString(title);
        parcel.writeString(original_title);
        parcel.writeString(overview);
        parcel.writeDouble(popularity);
        parcel.writeString(poster_path);
        parcel.writeString(release_date);
    }
}


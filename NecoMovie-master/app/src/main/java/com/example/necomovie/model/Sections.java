package com.example.necomovie.model;

import java.util.List;

public enum Sections {
    TRENDING("Trending", "trending", "movie", "day", ""),
    POPULAR("Popular", "movie", "popular", "", ""),
    UPCOMING("Upcoming", "movie", "upcoming", "", ""),
    TOP_RATED("Top Rated", "movie", "top_rated", "", ""),
    ACTION("Action", "discover", "movie", "", "28"),
    ADVENTURE("Adventure", "discover", "movie", "", "12"),
    ANIMATION("Animation", "discover", "movie", "", "16"),
    COMEDY("Comedy", "discover", "movie", "", "35"),
    CRIME("Crime", "discover", "movie", "", "80"),
    DRAMA("Drama", "discover", "movie", "", "18"),
    FAMILY("Family", "discover", "movie", "", "10751"),
    FANTASY("Fantasy", "discover", "movie", "", "14"),
    HISTORY("History", "discover", "movie", "", "36"),
    HORROR("Horror", "discover", "movie", "", "27"),
    SCIENCE_FICTION("Science Fiction", "discover", "movie", "", "878");


    private final String title;
    private final String type;
    private final String status;
    private final String time;
    private final String genreId;

    Sections(String title, String type, String status, String time, String genreId) {
        this.title = title;
        this.type = type;
        this.status = status;
        this.time = time;
        this.genreId = genreId;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public String getGenreId() {
        return genreId;
    }
}


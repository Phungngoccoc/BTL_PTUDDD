package com.example.necomovie.model;

import java.util.List;

public class SectionMovies implements Comparable<SectionMovies> {
    Sections section;
    List<Movie> movies;
    public SectionMovies(Sections section, List<Movie> movies) {
        this.section = section;
        this.movies = movies;
    }

    public Sections getSection() {
        return section;
    }

    public void setSection(Sections section) {
        this.section = section;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public int compareTo(SectionMovies sectionMovies) {
        return this.section.compareTo(sectionMovies.section);
    }
}

package com.example.necomovie.managers;

import com.example.necomovie.model.Movie;
import com.example.necomovie.model.MoviesResponse;
import com.example.necomovie.model.TrailersResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface MovieService {
    @GET
    Call<MoviesResponse> getMovies(@Url String url);
    @GET
    Call<Movie> getMovieById(@Url String url);
    @GET
    Call<TrailersResponse> getTrailers(@Url String url);
    @GET
    Call<MoviesResponse> search(@Url String url);
}

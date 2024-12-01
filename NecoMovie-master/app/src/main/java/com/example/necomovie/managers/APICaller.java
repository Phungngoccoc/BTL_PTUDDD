package com.example.necomovie.managers;

import android.util.Log;

import com.example.necomovie.model.Movie;
import com.example.necomovie.model.MoviesResponse;
import com.example.necomovie.model.Sections;
import com.example.necomovie.model.TrailersResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APICaller {
    static String API_KEY = "f14dabb7ec91ad6abc33ee277d4c4d07";
    static String BASE_URL = "https://api.themoviedb.org/";
    public static APICaller INSTANCE;
    MovieService service;
    private APICaller() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MovieService.class);
    }

    public static APICaller getINSTANCE() {
        if (null == INSTANCE){
            INSTANCE = new APICaller();
        }
        return INSTANCE;
    }
    public Call<MoviesResponse> getMovies(Sections section){

        return getMovies(section, 1);
    }
    public Call<MoviesResponse> getMovies(Sections section, int page){

        String time = "";
        if (section.getTime() != ""){
            time = "/" + section.getTime();
        }
//        let url = "\(Constants.BASE_URL)/3/\(type)/\(status)\(tempTime!)?api_key=\(Constants.API_KEY)\("&query=" + query)&page=\(page)&with_genres=\(genre)"
        String url = "3/" + section.getType() +"/"+ section.getStatus() + time + "?api_key=" + API_KEY +"&page=" + page + "&with_genres=" + section.getGenreId();
        return service.getMovies(url);
    }
    public Call<MoviesResponse> getSimilarsById(String id){
        String url = "3/movie/" + id + "/similar?api_key=" + API_KEY;
        return service.getMovies(url);
    }

    public Call<Movie> getMovieById(String id) {
        String url = "3/movie/"+ id + "?api_key=" + API_KEY;
        return  service.getMovieById(url);
    }

    public Call<TrailersResponse> getTrailers(String id){
        String url = "3/movie/"+ id + "/videos?api_key=" + API_KEY;
        return  service.getTrailers(url);
    }
    public Call<MoviesResponse> search(String query) {
        String url = "3/search/movie?api_key=" + API_KEY + "&query=" + query;
        return service.search(url);
    }
}

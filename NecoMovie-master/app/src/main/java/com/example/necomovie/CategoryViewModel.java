package com.example.necomovie;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.necomovie.managers.APICaller;
import com.example.necomovie.model.Movie;
import com.example.necomovie.model.MoviesResponse;
import com.example.necomovie.model.SectionMovies;
import com.example.necomovie.model.Sections;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends ViewModel {
    MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    Sections section;
    Boolean isLoading = false;
    int page = 0;
    void fetchData() {
        if(!isLoading) {
            page += 1;
            isLoading = true;
            APICaller.getINSTANCE().getMovies(section, page).enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    if (page == 1) {
                        movies.setValue(response.body().results);
                    } else  {
                        movies.getValue().addAll(response.body().results);
                        movies.setValue(movies.getValue());
                    }
                    isLoading = false;
                }
                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {

                }
            });
        }
    }
}

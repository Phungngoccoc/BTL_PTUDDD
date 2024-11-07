package com.example.necomovie.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.necomovie.managers.APICaller;
import com.example.necomovie.model.MoviesResponse;
import com.example.necomovie.model.SectionMovies;
import com.example.necomovie.model.Sections;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    MutableLiveData<List<SectionMovies>> sectionMovies = new MutableLiveData<>();
    MutableLiveData<Boolean> isFetchedSuccessful = new MutableLiveData<>();

    void fetchData() {
            for (Sections section : Sections.values()) {
                APICaller.getINSTANCE().getMovies(section).enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        List<SectionMovies> sectionMoviesList = sectionMovies.getValue();
                        if (sectionMoviesList == null) {
                            sectionMoviesList = new ArrayList<>();
                            sectionMovies.setValue(sectionMoviesList);
                        }
                        sectionMoviesList.add(new SectionMovies(section, response.body().results));
                        if (sectionMoviesList.size() == Sections.values().length){
                            isFetchedSuccessful.setValue(true);
                        }
                    }
                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {

                    }
                });
            }
        }
    }

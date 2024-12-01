package com.example.necomovie;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.necomovie.managers.APICaller;
import com.example.necomovie.model.Movie;
import com.example.necomovie.model.MoviesResponse;
import com.example.necomovie.model.SectionMovies;
import com.example.necomovie.model.Sections;
import com.example.necomovie.model.Trailer;
import com.example.necomovie.model.TrailersResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailViewModel extends ViewModel {
    Movie movie;
    MutableLiveData<List<Trailer>> trailers = new MutableLiveData<>();
    MutableLiveData<List<Movie>> similarMovies = new MutableLiveData<>();
    MutableLiveData<Boolean> isFavourite = new MutableLiveData<>(false);


    void fetchData() {
        APICaller.getINSTANCE().getTrailers(movie.id).enqueue(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                trailers.setValue(response.body().results);
            }

            @Override
            public void onFailure(Call<TrailersResponse> call, Throwable t) {

            }
        });
        APICaller.getINSTANCE().getSimilarsById(movie.id).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                similarMovies.setValue(response.body().results);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
            }
        });
        FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).whereEqualTo("id", movie.id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()){
                        isFavourite.setValue(true);
                    }
                }
            }
        });
    }
    void setFavourite() {
        CollectionReference ref = FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if(!isFavourite.getValue()){
            Map<String, Object> movieData = new HashMap<>();
            movieData.put("id", movie.id);
            ref.add(movieData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    isFavourite.setValue(true);
                }
            });
        }
        else {
            ref.whereEqualTo("id", movie.id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            ref.document(document.getId()).delete();
                            isFavourite.setValue(false);
                        }
                    }
                }
            });
        }
    }
}

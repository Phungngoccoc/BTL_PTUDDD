package com.example.necomovie.ui.favourite;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.necomovie.managers.APICaller;
import com.example.necomovie.model.Movie;
import com.example.necomovie.model.MoviesResponse;
import com.example.necomovie.model.Sections;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteViewModel extends ViewModel {
    public MutableLiveData<List<Movie>> movies = new MutableLiveData<>(new ArrayList<>());
    ListenerRegistration registration;
    CollectionReference ref = FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid());

    void fetchData() {
        registration = ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange document : value.getDocumentChanges()) {
                    if (document.getType() == DocumentChange.Type.ADDED) {
                        APICaller.getINSTANCE().getMovieById(document.getDocument().get("id").toString()).enqueue(new Callback<Movie>() {
                            @Override
                            public void onResponse(Call<Movie> call, Response<Movie> response) {
                                List<Movie> currentList = movies.getValue();
                                currentList.add(response.body());
                                movies.setValue(currentList);
                            }
                            @Override
                            public void onFailure(Call<Movie> call, Throwable t) {
                            }
                        });
                    } else if (document.getType() == DocumentChange.Type.REMOVED) {
                        List<Movie> tempList = movies.getValue();
                        for (int i = 0; i < tempList.size(); i++) {
                            if (tempList.get(i).id.equals(document.getDocument().get("id").toString())) {
                                tempList.remove(i);
                                break;
                            }
                        }
                        movies.setValue(tempList);
                    }
                }
            }
        });
    }
}
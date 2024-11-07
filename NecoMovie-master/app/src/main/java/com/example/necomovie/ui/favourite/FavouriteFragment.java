package com.example.necomovie.ui.favourite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.necomovie.DetailActivity;
import com.example.necomovie.R;
import com.example.necomovie.common.MovieRecycleViewAdapter;
import com.example.necomovie.common.SpacingItemDecorator;
import com.example.necomovie.databinding.FragmentFavouriteBinding;
import com.example.necomovie.model.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FavouriteFragment extends Fragment {

    private FragmentFavouriteBinding binding;
    RecyclerView favouriteRecycleView;
    FavouriteViewModel favouriteViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favouriteRecycleView = view.findViewById(R.id.favouriteRecycleView);
        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);
        List<Movie> movies = favouriteViewModel.movies.getValue();
        MovieRecycleViewAdapter adapter = new MovieRecycleViewAdapter(getContext(), movies);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        favouriteRecycleView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(20, 0);
        favouriteRecycleView.addItemDecoration(itemDecorator);
        favouriteRecycleView.setAdapter(adapter);
        favouriteViewModel.fetchData();
        CollectionReference ref = FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid());


        favouriteViewModel.movies.observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                adapter.list = movies;
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        favouriteViewModel.registration.remove();
    }

}
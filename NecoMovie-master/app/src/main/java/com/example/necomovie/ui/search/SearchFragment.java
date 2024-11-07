package com.example.necomovie.ui.search;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.necomovie.R;
import com.example.necomovie.common.Debounce;
import com.example.necomovie.common.GridSpacingItemDecoration;
import com.example.necomovie.common.MovieRecycleViewAdapter;
import com.example.necomovie.common.SectionRecycleViewAdapter;
import com.example.necomovie.common.SpacingItemDecorator;
import com.example.necomovie.databinding.FragmentSearchBinding;
import com.example.necomovie.model.Movie;

import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    RecyclerView recyclerView;
    SearchViewModel searchViewModel;
    SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);
        searchView = (SearchView) view.findViewById(R.id.searchView);
        EditText editText = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(Color.WHITE);
        searchView.setQueryHint("Search for a movie");
        editText.setHintTextColor(Color.parseColor("#C6C6CE"));
        ImageView iconClose = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        iconClose.setColorFilter(Color.parseColor("#C6C6CE"));
        ImageView iconSearch = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        iconSearch.setColorFilter(Color.parseColor("#C6C6CE"));
        recyclerView = (RecyclerView) view.findViewById(R.id.searchRecycleView);
        List<Movie> movies = searchViewModel.movies.getValue();
        MovieRecycleViewAdapter adapter = new MovieRecycleViewAdapter(getContext(), movies);
        SectionRecycleViewAdapter searchAdapter = new SectionRecycleViewAdapter(getContext(), movies);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(20,0);
        GridSpacingItemDecoration searchDecorator = new GridSpacingItemDecoration(3, 20);
        recyclerView.addItemDecoration(itemDecorator);
        recyclerView.setAdapter(adapter);

        searchViewModel.fetchData();

        searchViewModel.movies.observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> list) {
                adapter.list = list;
                adapter.notifyDataSetChanged();
            }
        });
        searchViewModel.searchResults.observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> list) {
                searchAdapter.list = list;
                searchAdapter.notifyDataSetChanged();
            }
        });
        searchViewModel.isSearching.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(searchAdapter);
                    recyclerView.removeItemDecoration(itemDecorator);
                    recyclerView.addItemDecoration(searchDecorator);
                }
                else  {
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    recyclerView.removeItemDecoration(searchDecorator);
                    recyclerView.addItemDecoration(itemDecorator);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()) {
                    if(searchViewModel.isSearching.getValue()){
                        searchViewModel.isSearching.setValue(false);
                    }
                }
                else {
                    if(!searchViewModel.isSearching.getValue()){
                        searchViewModel.isSearching.setValue(true);
                    }

                    searchViewModel.search(newText);
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
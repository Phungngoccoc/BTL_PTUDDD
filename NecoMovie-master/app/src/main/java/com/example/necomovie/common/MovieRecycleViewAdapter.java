package com.example.necomovie.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.necomovie.R;
import com.example.necomovie.model.Movie;
import com.example.necomovie.model.SectionMovies;

import java.util.List;

public class MovieRecycleViewAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    Context context;
    public List<Movie> list;
    public MovieRecycleViewAdapter(Context context, List<Movie> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.movie_recycleview_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.setMovie(list.get(position));
    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();
        return 0;
    }
}

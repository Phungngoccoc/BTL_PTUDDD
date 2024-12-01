package com.example.necomovie.common;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.necomovie.DetailActivity;
import com.example.necomovie.R;
import com.example.necomovie.model.Movie;

public class SectionViewHolder extends RecyclerView.ViewHolder {
    Context context;
    ImageView image;
    Movie movie;
    public SectionViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.imageView);
        context = itemView.getContext();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("movie", movie);
                context.startActivity(intent);
            }
        });
    }
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

}

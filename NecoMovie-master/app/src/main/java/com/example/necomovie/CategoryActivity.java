package com.example.necomovie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.necomovie.common.GridSpacingItemDecoration;
import com.example.necomovie.common.SectionRecycleViewAdapter;
import com.example.necomovie.model.Movie;
import com.example.necomovie.model.Sections;
import com.example.necomovie.ui.home.HomeViewModel;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    ImageButton backButton;
    TextView categoryTextView;
    RecyclerView recyclerView;
    CategoryViewModel categoryViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        categoryViewModel =
                new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.section = (Sections) getIntent().getSerializableExtra("section");
        toolbar = (MaterialToolbar) findViewById(R.id.categoryToolbar);
        backButton = (ImageButton) findViewById(R.id.backButton);
        categoryTextView = (TextView) findViewById(R.id.activityName);
        recyclerView = (RecyclerView) findViewById(R.id.categoryRecycleView);
        categoryTextView.setText(categoryViewModel.section.getTitle());
        List<Movie> movies = categoryViewModel.movies.getValue();
        SectionRecycleViewAdapter adapter = new SectionRecycleViewAdapter(this, movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 20));
        recyclerView.setAdapter(adapter);
        categoryViewModel.fetchData();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition >= categoryViewModel.movies.getValue().size() - 6) {
                    categoryViewModel.fetchData();
                }
            }
        });
        categoryViewModel.movies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> list) {
                adapter.list = list;
                adapter.notifyDataSetChanged();
            }
        });

    }
}
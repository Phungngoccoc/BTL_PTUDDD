package com.example.necomovie;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.necomovie.common.GridSpacingItemDecoration;
import com.example.necomovie.common.HomeRecycleViewAdapter;
import com.example.necomovie.common.SectionRecycleViewAdapter;
import com.example.necomovie.common.SpacingItemDecorator;
import com.example.necomovie.model.Movie;
import com.example.necomovie.model.SectionMovies;
import com.example.necomovie.model.Trailer;
import com.example.necomovie.ui.favourite.FavouriteFragment;
import com.example.necomovie.ui.favourite.FavouriteViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;


public class DetailActivity extends AppCompatActivity {
    private Boolean isFullscreen = false;
    YouTubePlayer youTubePlayer;
    YouTubePlayerView youTubePlayerView;
    FrameLayout fullscreenViewContainer;
    TextView titleTextView;
    TextView yearTextView;
    TextView descriptionTextView;
    RecyclerView recyclerView;
    DetailViewModel detailViewModel;
    MaterialToolbar toolbar;
    ImageButton backButton;
    Button favouriteBtn;
    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (isFullscreen) {
                youTubePlayer.toggleFullscreen();
            } else {
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getOnBackPressedDispatcher().addCallback(onBackPressedCallback);
        setContentView(R.layout.activity_detail);
        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        detailViewModel.movie = getIntent().getParcelableExtra("movie");
        fullscreenViewContainer = findViewById(R.id.full_screen_view_container);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        toolbar = (MaterialToolbar) findViewById(R.id.detailToolbar);
        backButton = (ImageButton) toolbar.findViewById(R.id.backButton);
        favouriteBtn = findViewById(R.id.favouriteBtn);
        titleTextView = (TextView) findViewById(R.id.movieTitleTextView);
        yearTextView = (TextView) findViewById(R.id.yearTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        recyclerView = (RecyclerView) findViewById(R.id.detailRecycleView);
        titleTextView.setText((detailViewModel.movie.title != null) ? detailViewModel.movie.title : detailViewModel.movie.original_title);
        yearTextView.setText(detailViewModel.movie.release_date);
        descriptionTextView.setText(detailViewModel.movie.overview);
        List<Movie> movies = detailViewModel.similarMovies.getValue();
        SectionRecycleViewAdapter adapter = new SectionRecycleViewAdapter(this, movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 20));
        recyclerView.setAdapter(adapter);
        detailViewModel.fetchData();
        View decorView = getWindow().getDecorView();
        IFramePlayerOptions iFramePlayerOptions = new IFramePlayerOptions.Builder()
                .controls(1)
                .fullscreen(1)
                .build();
        youTubePlayerView.addFullscreenListener(new FullscreenListener() {

            @Override
            public void onEnterFullscreen(@NonNull View view, @NonNull Function0<Unit> function0) {
                isFullscreen = true;
                youTubePlayerView.setVisibility(View.GONE);
                fullscreenViewContainer.setVisibility(View.VISIBLE);
                fullscreenViewContainer.addView(view);
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            @Override
            public void onExitFullscreen() {
                isFullscreen = false;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                youTubePlayerView.setVisibility(View.VISIBLE);
                fullscreenViewContainer.setVisibility(View.GONE);
                fullscreenViewContainer.removeAllViews();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        });
        youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                DetailActivity.this.youTubePlayer = youTubePlayer;
            }

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                if(state == PlayerConstants.PlayerState.PLAYING && isFullscreen){
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                }
            }
        }, iFramePlayerOptions);
        favouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailViewModel.setFavourite();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        detailViewModel.isFavourite.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                favouriteBtn.setText(aBoolean ? "Remove from favourite" : "Add to favourite");
            }
        });
        detailViewModel.trailers.observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(List<Trailer> trailers) {
                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {

                        for (Trailer trailer : detailViewModel.trailers.getValue()) {
                            if ("Trailer".equals(trailer.type)) {
                                youTubePlayer.cueVideo(trailer.key, 0);
                                break;
                            }
                        }
                    }
                });
            }
        });
        detailViewModel.similarMovies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> list) {
                adapter.list = list;
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && isFullscreen) {

        }
    }
}

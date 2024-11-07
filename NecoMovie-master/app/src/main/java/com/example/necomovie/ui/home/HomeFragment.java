package com.example.necomovie.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.necomovie.R;
import com.example.necomovie.common.CarouselRecycleViewAdapter;
import com.example.necomovie.common.Debounce;
import com.example.necomovie.common.HomeRecycleViewAdapter;
import com.example.necomovie.common.SpacingItemDecorator;
import com.example.necomovie.databinding.FragmentHomeBinding;
import com.example.necomovie.model.Movie;
import com.example.necomovie.model.SectionMovies;
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager;
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private HomeViewModel homeViewModel;
    CarouselRecyclerview carouselRecyclerview;
    private LinearLayout indicatorLayout;
    private final int INDICATOR_NUMBER_ITEMS = 6;
    private int indicatorCurrentPosition = 0;
    private Debounce debounce;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        recyclerView = view.findViewById(R.id.homeRecycleView);
        List<SectionMovies> s = homeViewModel.sectionMovies.getValue();
        HomeRecycleViewAdapter adapter = new HomeRecycleViewAdapter(getContext(), s);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        carouselRecyclerview = view.findViewById(R.id.carousel_recycler);
        indicatorLayout = view.findViewById(R.id.carousel_indicator);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        SpacingItemDecorator itemDecorator = new  SpacingItemDecorator(50,0);
        recyclerView.addItemDecoration(itemDecorator);
        recyclerView.setAdapter(adapter);
        CarouselRecycleViewAdapter carouselAdapter = new CarouselRecycleViewAdapter(getContext(), new ArrayList<Movie>());
        carouselRecyclerview.setAdapter(carouselAdapter);
        //carouselRecyclerview.setInfinite(true);
        carouselRecyclerview.setIntervalRatio(0.63F);
        homeViewModel.fetchData();
        setupIndicator();
        debounce = new Debounce(4000, new Runnable() {
            @Override
            public void run() {
                setIndicatorCurrentPosition((indicatorCurrentPosition + 1) % INDICATOR_NUMBER_ITEMS);
                carouselRecyclerview.getCarouselLayoutManager().smoothScrollToPosition(carouselRecyclerview, new RecyclerView.State(),indicatorCurrentPosition);
                debounce.execute();
            }
        });
        homeViewModel.isFetchedSuccessful.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Collections.sort(homeViewModel.sectionMovies.getValue());
                adapter.setSections(homeViewModel.sectionMovies.getValue());
                carouselAdapter.list = homeViewModel.sectionMovies.getValue().get(0).getMovies().subList(0, INDICATOR_NUMBER_ITEMS);
                carouselAdapter.notifyDataSetChanged();
                debounce.execute();
            }
        });
        carouselRecyclerview.setItemSelectListener(new CarouselLayoutManager.OnSelected() {
            @Override
            public void onItemSelected(int i) {
                carouselDidScroll(i);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private ImageView createCircleImageView(Context context) {
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                40,40
        );
        layoutParams.setMargins(10, 0, 10, 0);
        imageView.setImageResource(R.drawable.circle_indicator);
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }
    public void setIndicatorCurrentPosition(int destinyPosition) {
        ImageView i = (ImageView) indicatorLayout.getChildAt(indicatorCurrentPosition);
        i.setImageResource(R.drawable.circle_indicator);
        ImageView i1 = (ImageView) indicatorLayout.getChildAt(destinyPosition);
        i1.setImageResource(R.drawable.circle_selected_indicator);
        this.indicatorCurrentPosition = destinyPosition;
    }
    private void setupIndicator(){
        for (int i = 0; i < INDICATOR_NUMBER_ITEMS; i++) {
            ImageView circle = createCircleImageView(getContext());
            indicatorLayout.addView(circle);
        }
        ImageView i = (ImageView) indicatorLayout.getChildAt(indicatorCurrentPosition);
        i.setImageResource(R.drawable.circle_selected_indicator);
    }
    private void carouselDidScroll(int destinyPosition){
        if(destinyPosition == indicatorCurrentPosition)
            return;
        setIndicatorCurrentPosition(destinyPosition);
    }

}
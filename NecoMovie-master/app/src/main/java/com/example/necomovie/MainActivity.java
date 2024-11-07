package com.example.necomovie;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.example.necomovie.ui.favourite.FavouriteFragment;
import com.example.necomovie.ui.home.HomeFragment;
import com.example.necomovie.ui.search.SearchFragment;
import com.google.android.material.appbar.MaterialToolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.necomovie.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    final Fragment homeFragment = new HomeFragment();
    final Fragment dashboardFragment = new FavouriteFragment();
    final Fragment notificationsFragment = new SearchFragment();
    private Fragment active = homeFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    ImageButton profileBtn;
    MaterialToolbar toolbar;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        profileBtn = findViewById(R.id.profileBtn);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setUpFragments();
        replaceFragment(homeFragment);

        if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null) {
            Glide.with(this)
                    .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                    .circleCrop()
                    .placeholder(R.drawable.person_circle_fill)
                    .into(profileBtn);
        }
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
            }
        });
        binding.navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    replaceFragment(homeFragment);
                } else if (itemId == R.id.navigation_favourite) {
                    replaceFragment(dashboardFragment);
                } else if (itemId == R.id.navigation_search) {
                   replaceFragment(notificationsFragment);
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(active).show(fragment).commit();
        active = fragment;
    }

    private void setUpFragments() {
        fragmentManager.beginTransaction().add(R.id.frame_layout, notificationsFragment).hide(notificationsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frame_layout, dashboardFragment).hide(dashboardFragment).commit();
        fragmentManager.beginTransaction().add(R.id.frame_layout, homeFragment).commit();
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
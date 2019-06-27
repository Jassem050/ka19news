package com.amitzinfy.ka19news.views;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.amitzinfy.ka19news.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        HeadLineFragment.OnFragmentInteractionListener, FavouriteFragment.OnFragmentInteractionListener,
        DynamicTabFragment.OnFragmentInteractionListener{

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setUpBottomNavView();
        loadFragment(HomeFragment.newInstance("home","home"));
    }

    private void init(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setUpBottomNavView(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        fragment = HomeFragment.newInstance("home", "home");
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_headlines:
                        fragment = HeadLineFragment.newInstance("headlines", "headlines");
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_favourites:
                        fragment = FavouriteFragment.newInstance("favourites", "favourites");
                        loadFragment(fragment);
                        return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_main_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

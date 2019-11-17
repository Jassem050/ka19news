package com.amitzinfy.ka19news.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.utils.PreferenceManager;
import com.amitzinfy.ka19news.viewmodels.MyFeedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        HeadLineFragment.OnFragmentInteractionListener, FavouriteFragment.OnFragmentInteractionListener,
        DynamicTabFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener,
        AccountFragment.OnFragmentInteractionListener {
    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;
    public DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private PreferenceManager preferenceManager;
    private MyFeedViewModel myFeedViewModel;
    private Fragment.SavedState savedFragmentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setUpBottomNavView(savedInstanceState);
        setUpNavigationView();
        if (preferenceManager. getAppStatus() != null && (preferenceManager.getAppStatus().equals(getString(R.string.reader_status)) ||
                preferenceManager.getAppStatus().equals(getString(R.string.reader_writer_status)))) {
            loadFragment(HomeFragment.newInstance("home", "home"));
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        } else if (preferenceManager. getAppStatus() != null &&
                preferenceManager.getAppStatus().equals(getString(R.string.writer_status))) {
            loadFragment(AccountFragment.newInstance("account", "account"));
            bottomNavigationView.setSelectedItemId(R.id.navigation_account);
        }
    }

    private void init() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation);
        preferenceManager = PreferenceManager.getInstance(this);
        myFeedViewModel = ViewModelProviders.of(this).get(MyFeedViewModel.class);
        navigationView.setItemIconTintList(null);
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawers();
//                item.setChecked(true);
            switch (item.getItemId()) {
                case R.id.english:
                    Toast.makeText(MainActivity.this, "english", Toast.LENGTH_SHORT).show();
                    preferenceManager.setLanguageName("English");
                    Log.d(TAG, "onNavigationItemSelected: language_id: " + preferenceManager.getLanguageName());
                    myFeedViewModel.setLanguageId(preferenceManager.getLanguageName());
                    bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                    break;
                case R.id.kannada:
                    Toast.makeText(MainActivity.this, "Kannada", Toast.LENGTH_SHORT).show();
                    preferenceManager.setLanguageName("Kannada");
                    Log.d(TAG, "onNavigationItemSelected: language_id: " + preferenceManager.getLanguageName());
                    myFeedViewModel.setLanguageId(preferenceManager.getLanguageName());
                    bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                    break;
                case R.id.myfeed:
                    startActivity(new Intent(this, MyFeedPersonalizationActivity.class));
                    Log.d(TAG, "setUpNavigationView: manufctrr: " + Build.MANUFACTURER + Build.MODEL);
                    break;
                case R.id.feedback:
                    String deviceInfo = "\n\n\n\n" + "Device: " + Build.DEVICE + "\n" + "Brand: " + Build.BRAND + "\n"
                            + "Model: " + Build.MODEL + "\n" + "Manufacturer: " + Build.MANUFACTURER +
                            "\n" + "Version: " + Build.VERSION.SDK_INT;
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setType("text/plain");
                    intent.setData(Uri.parse("mailto:ka19news@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                    intent.putExtra(Intent.EXTRA_TEXT, deviceInfo);
                    startActivity(Intent.createChooser(intent, "Send Feedback"));
                    break;
            }
            return false;
        });
    }

    private void setUpBottomNavView(Bundle savedState) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            FragmentManager fragmentManager = getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (!(fragmentManager.findFragmentById(R.id.content_main_frame) instanceof HomeFragment)) {
                        fragment = HomeFragment.newInstance("home", "home");
                        loadFragment(fragment);
                    }
                    return true;
                case R.id.navigation_headlines:
                    if (!(fragmentManager.findFragmentById(R.id.content_main_frame) instanceof HeadLineFragment)) {
                        fragment = HeadLineFragment.newInstance("headlines", "headlines");
                        loadFragment(fragment);
                    }
                    return true;
                case R.id.navigation_favourites:
                    if (!(fragmentManager.findFragmentById(R.id.content_main_frame) instanceof FavouriteFragment)) {
                        fragment = FavouriteFragment.newInstance("favourites", "favourites");
                        loadFragment(fragment);
                    }
                    return true;
                case R.id.navigation_account:
                    if (preferenceManager.getUserStatus().equals("logged_in")) {
                        if (!(fragmentManager.findFragmentById(R.id.content_main_frame) instanceof AccountFragment)) {
                            fragment = AccountFragment.newInstance("account", "account");
                            loadFragment(fragment);
                        }
                    } else if (preferenceManager.getUserStatus().equals("logged_out")) {
                        if (!(fragmentManager.findFragmentById(R.id.content_main_frame) instanceof LoginFragment)) {
                            fragment = LoginFragment.newInstance("login", "login");
                            loadFragment(fragment);
                        }
                    }
                    return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_main_frame, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDrawerButtonClicked() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

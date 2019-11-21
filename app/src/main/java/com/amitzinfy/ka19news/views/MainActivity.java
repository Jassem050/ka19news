package com.amitzinfy.ka19news.views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setUpBottomNavView();
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
                    myFeedViewModel.setLanguageId(preferenceManager.getLanguageName());
                    bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                    if ((getSupportFragmentManager().findFragmentById(R.id.content_main_frame) instanceof HomeFragment)) {
                        loadFragment(HomeFragment.newInstance("home", "home"));
                    }
                    break;
                case R.id.kannada:
                    Toast.makeText(MainActivity.this, "Kannada", Toast.LENGTH_SHORT).show();
                    preferenceManager.setLanguageName("Kannada");
                    myFeedViewModel.setLanguageId(preferenceManager.getLanguageName());
                    bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                    if ((getSupportFragmentManager().findFragmentById(R.id.content_main_frame) instanceof HomeFragment)) {
                        loadFragment(HomeFragment.newInstance("home", "home"));
                    }
                    break;
                case R.id.myfeed:
                    startActivity(new Intent(this, MyFeedPersonalizationActivity.class));
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
                case R.id.instagram:
                    Uri uri = Uri.parse("https://www.instagram.com/ka19_news/");
                    Intent insta = new Intent(Intent.ACTION_VIEW, uri);
                    insta.setPackage("com.instagram.android");

                    if (isIntentAvailable(this, insta)){
                        startActivity(insta);
                    } else{
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ka19_news/")));
                    }
                    break;
                case R.id.facebook:
                    PackageManager packageManager = this.getPackageManager();

                        if (isAppInstalled()) {
                            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                            String facebookUrl = getFacebookPageURL(this);
                            facebookIntent.setData(Uri.parse(facebookUrl));
                            startActivity(facebookIntent);
                        } else {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ka19news")));
                        }

                    break;
                case R.id.twitter:
                    startTwitter(this);
                    break;

            }
            return false;
        });
    }

    public void startTwitter(Context context) {

        Intent intent;
        try {
            // get the Twitter app if possible
            context.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=ka19_news"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            return intent;
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ka19_news"));
        }
        startActivity(intent);
    }

    public static String FACEBOOK_URL = "https://www.facebook.com/ka19news";
    public static String FACEBOOK_PAGE_ID = "2087654634803168";

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    public boolean isAppInstalled() {
        try {
            getApplicationContext().getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            boolean activated = getPackageManager().getApplicationInfo("com.facebook.katana", 0).enabled;
            return activated;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
    private void setUpBottomNavView() {
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

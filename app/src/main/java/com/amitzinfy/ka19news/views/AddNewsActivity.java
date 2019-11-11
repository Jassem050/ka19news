package com.amitzinfy.ka19news.views;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.amitzinfy.ka19news.R;

public class AddNewsActivity extends AppCompatActivity implements AddNewsDetailsFragment.OnFragmentInteractionListener,
        AddNewsContentFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        loadFragment(AddNewsDetailsFragment.newInstance("news_details", "news_details"));
    }

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

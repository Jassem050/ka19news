package com.amitzinfy.ka19news.views;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.utils.PreferenceManager;
import com.amitzinfy.ka19news.viewmodels.UserViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    private static final String TAG = "AccountFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private MaterialToolbar materialToolbar;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private PreferenceManager preferenceManager;
    private UserViewModel userViewModel;
    private MaterialButton uploadImageBtn;
    private MaterialTextView profileName, profileEmail, profileMobileNo;
    private MaterialTextView newsAddCount, newsAcceptCount;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        setToolbar(rootView);
        bindView(rootView);
        if (preferenceManager.getUserStatus().equals(getString(R.string.logged_in_status))){
            preferenceManager.setAppStatus(getString(R.string.reader_writer_status));
        }

        // for drawer hamburger animation
        if (getActivity() != null)
            drawerLayout = ((MainActivity) getActivity()).drawerLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                materialToolbar, R.string.drawer_open, R.string.drawer_close);

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(getActivity(), R.color.white));
        actionBarDrawerToggle.setDrawerSlideAnimationEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        getUserDetails(preferenceManager.getAccessToken());
        getAddedNewsCount(preferenceManager.getAccessToken());
        return rootView;
    }

    private void setToolbar(View view){
        materialToolbar = view.findViewById(R.id.account_toolbar);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(materialToolbar);
            actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Account");
                //            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

        }
    }

    private void bindView(View view){
        uploadImageBtn = view.findViewById(R.id.upload_image_btn);
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        profileMobileNo = view.findViewById(R.id.profile_phone);
        newsAddCount = view.findViewById(R.id.news_add_count);
        newsAcceptCount = view.findViewById(R.id.news_accept_count);

        preferenceManager = PreferenceManager.getInstance(getActivity());
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: access_token: " + preferenceManager.getAccessToken());
                Toast.makeText(getActivity(), preferenceManager.getAccessToken(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserDetails(String access_token){
        Log.d(TAG, "getUserDetails: ");
        userViewModel.getUserDetails(access_token).observe(getViewLifecycleOwner(), userResponse -> {
            Log.d(TAG, "getUserDetails: user: " + userResponse.getUser().toString());
            profileName.setText(userResponse.getUser().getName());
            profileEmail.setText(userResponse.getUser().getEmail());
            profileMobileNo.setText(userResponse.getUser().getMobileNumber());
        });
    }

    private void getAddedNewsCount(String access_token){
        userViewModel.getAddedNewsCount(access_token).observe(getViewLifecycleOwner(), newsAdded -> {
            newsAddCount.setText(String.valueOf(newsAdded.getNewsCount()));
            newsAcceptCount.setText(String.valueOf(newsAdded.getNewsAcceptedCount()));
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
                mListener.onDrawerButtonClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        void onDrawerButtonClicked();
    }
}

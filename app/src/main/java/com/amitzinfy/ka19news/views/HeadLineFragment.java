package com.amitzinfy.ka19news.views;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.adapters.CategoryAdapter;
import com.amitzinfy.ka19news.utils.PreferenceManager;
import com.amitzinfy.ka19news.viewmodels.HeadLinesViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HeadLineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HeadLineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeadLineFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "HeadLineFragment";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CategoryAdapter categoryAdapter;
    private HeadLinesViewModel headLinesViewModel;
    private MaterialToolbar materialToolbar;
    private ActionBar actionBar;
    private PreferenceManager preferenceManager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public HeadLineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HeadLineFragment.
     */
    public static HeadLineFragment newInstance(String param1, String param2) {
        HeadLineFragment fragment = new HeadLineFragment();
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
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_head_line, container, false);

        setToolbar(rootView);
        init(rootView);
        viewPager.setAdapter(categoryAdapter);
        tabLayout.setupWithViewPager(viewPager, true);

        headLinesViewModel.getNewsCategories(preferenceManager.getLanguageId()).observe(this, newsCategories -> {
            categoryAdapter.setCategoryList(newsCategories);
            categoryAdapter.notifyDataSetChanged();
        });

        return rootView;
    }

    // initializing or binding views
    private void init(View view){
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        if (categoryAdapter == null){
            categoryAdapter = new CategoryAdapter(getChildFragmentManager(),
                    FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        headLinesViewModel = ViewModelProviders.of(this).get(HeadLinesViewModel.class);
        preferenceManager = PreferenceManager.getInstance(getActivity());
        // for drawer hamburger animation
        if (getActivity() != null)
            drawerLayout = ((MainActivity) getActivity()).drawerLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                materialToolbar, R.string.drawer_open, R.string.drawer_close);

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(getActivity(), R.color.white));
        actionBarDrawerToggle.setDrawerSlideAnimationEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

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

    private void setToolbar(View view){
        materialToolbar = (MaterialToolbar) view.findViewById(R.id.headline_toolbar);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(materialToolbar);
            actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.setTitle(getResources().getString(R.string.app_name));
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
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

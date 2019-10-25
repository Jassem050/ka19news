package com.amitzinfy.ka19news.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.adapters.MyFeedNewsListAdapter;
import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.utils.PreferenceManager;
import com.amitzinfy.ka19news.viewmodels.MyFeedViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements MyFeedNewsListAdapter.NewsItemClickListener {

    private static final String TAG = "HomeFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private MyFeedNewsListAdapter myFeedNewsListAdapter;
    private RecyclerView recyclerView;
    private MyFeedViewModel myFeedViewModel;
    private MaterialToolbar materialToolbar;
    private ActionBar actionBar;
    private ShimmerFrameLayout shimmerFrameLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Observer<List<News>> newsObserver;
    private List<News> newsList;
    private ToggleButton toggleButton;
    private PreferenceManager preferenceManager;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private Handler handler;
    private Runnable runnable;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        setToolbar(rootView);
        init(rootView);

        if (preferenceManager.getLanguageName().equals("English")) {
            subscribe();
        } else {
            getLanguageNews();
        }


        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (preferenceManager.getLanguageName().equals("English")) {
                subscribe();
            } else {
                getLanguageNews();
            }
        });

        return rootView;
    }


    private void subscribe(){
        newsObserver = news -> {
            if (news != null && news.size() > 0) {
                String errorMessage = news.get(0).getMessageType();
                if (errorMessage != null && errorMessage.equals("error")){
                    Toast.makeText(getActivity(), news.get(0).getErrorMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    newsList = news;
                    myFeedNewsListAdapter.setNewsList(newsList);
                    myFeedNewsListAdapter.notifyDataSetChanged();
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    handler.postDelayed(runnable = () -> swipeRefreshLayout.setRefreshing(false), 1000);
                }
            }
        };
        myFeedViewModel.setLanguageId("English");
        myFeedViewModel.getNewsList(preferenceManager.getCategory()).observe(getViewLifecycleOwner(), newsObserver);
    }

    private void getLanguageNews(){
        newsObserver = news -> {
            if (news != null && news.size() > 0) {
                String errorMessage = news.get(0).getMessageType();
                if (errorMessage != null && errorMessage.equals("error")){
                    Toast.makeText(getActivity(), news.get(0).getErrorMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    newsList = news;
                    myFeedNewsListAdapter.setNewsList(newsList);
                    myFeedNewsListAdapter.notifyDataSetChanged();
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    handler.postDelayed(runnable = () -> swipeRefreshLayout.setRefreshing(false), 1000);
                }
            }
        };
        myFeedViewModel.getLanguageNews(preferenceManager.getLanguageName()).observe(getViewLifecycleOwner(), newsObserver);
    }

    private void init(View view){
        recyclerView = view.findViewById(R.id.mynews_recyclerview);
        myFeedNewsListAdapter = new MyFeedNewsListAdapter(getActivity(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myFeedNewsListAdapter);
        myFeedViewModel = ViewModelProviders.of(this).get(MyFeedViewModel.class);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        shimmerFrameLayout = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_layout);
        shimmerFrameLayout.startShimmer();
        preferenceManager = PreferenceManager.getInstance(getActivity());
        // for drawer hamburger animation
        if (getActivity() != null)
        drawerLayout = ((MainActivity) getActivity()).drawerLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                materialToolbar, R.string.drawer_open, R.string.drawer_close);

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(getActivity(), R.color.white));
        actionBarDrawerToggle.setDrawerSlideAnimationEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        handler = new Handler();
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
        materialToolbar = (MaterialToolbar) view.findViewById(R.id.myfeed_toolbar);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(materialToolbar);
            actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(getResources().getString(R.string.app_name));
                //            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

        }
    }




    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search){
            startActivity(new Intent(getActivity(), SearchResultsActivity.class));
            return true;
        }
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

    @Override
    public void onItemToggleButtonChecked(int position) {
//        News news = newsList.get(position);
//        Log.d(TAG, "onItemToggleButtonChecked: id: " + news.getId());
//        myFeedViewModel.insertFavNews(new FavouriteNews(news.getId(),news.getTitle(), news.getDescription(),
//                news.getImage(), news.getCategoryName()));
    }

    @Override
    public void onItemToggleButtonUnChecked(int position) {
//        News news = newsList.get(position);
//        Log.d(TAG, "onItemToggleButtonUnChecked: id: " + news.getId());
//        myFeedViewModel.deleteFavNews(new FavouriteNews(news.getId(),news.getTitle(), news.getDescription(),
//                news.getImage(), news.getCategoryName()));
    }

    @Override
    public void setItemToggleButton(ToggleButton toggleButton, int position) {
//        News news = newsList.get(position);
//        myFeedViewModel.getFavouriteNews(news.getId()).observe(getViewLifecycleOwner(), favouriteNews -> {
//            if (favouriteNews.length > 0){
//                if (!toggleButton.isChecked()) {
//                    toggleButton.setChecked(true);
//                }
//            }
//        });
    }

    @Override
    public void onItemClicked(int position) {
        News news = newsList.get(position);
        Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
        intent.putExtra("news_id", news.getId());
        intent.putExtra("news_title", news.getTitle());
        intent.putExtra("news_description", news.getDescription());
        intent.putExtra("news_image", news.getImage());
        intent.putExtra("news_image_caption", news.getImageCaption());
        intent.putExtra("news_category", news.getCategoryName());
        intent.putExtra("news_time", news.getTime());
        startActivity(intent);
    }

    @Override
    public void onShareButtonClicked(int position) {
        News news = newsList.get(position);
        if (getActivity() != null)
        ShareCompat.IntentBuilder
                .from(getActivity())
                .setType("text/plain")
                .setChooserTitle("Share News with: ")
                .setText("https://sports.ndtv.com/west-indies-vs-india-2019/ms-dhoni-doubtful-for-west-indies-tour-will-participate-in-transitioning-phase-for-team-india-report-2070897?News_Trending")
                .startChooser();
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}

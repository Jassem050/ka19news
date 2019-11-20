package com.amitzinfy.ka19news.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.adapters.FavouriteNewsAdapter;
import com.amitzinfy.ka19news.models.room.FavouriteNews;
import com.amitzinfy.ka19news.viewmodels.FavouritesViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavouriteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment implements FavouriteNewsAdapter.FavNewsItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "FavouriteFragment";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private MaterialToolbar materialToolbar;
    private ActionBar actionBar;
    private FavouritesViewModel favouritesViewModel;
    private RecyclerView recyclerView;
    private FavouriteNewsAdapter favouriteNewsAdapter;
    private ShimmerFrameLayout shimmerFrameLayout;
    private List<FavouriteNews> favouriteNewsList;
    private LottieAnimationView lottieAnimationView;
    private AppCompatTextView favEmtyText;
    private List<FavouriteNews> newsList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteFragment.
     */
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);

        setToolbar(rootView);
        init(rootView);
        subscribe();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                FavouriteNews favouriteNews = favouriteNewsAdapter.getFavNewsAtPosition(position);
                Log.d(TAG, "onSwiped: favNews at position: " + position);
                favouritesViewModel.deleteFavNews(favouriteNews);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));

        return rootView;
    }

    private void setToolbar(View view){
        materialToolbar = view.findViewById(R.id.favourite_toolbar);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(materialToolbar);
            actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(getResources().getString(R.string.favourites));
                actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void init(View view){
        favouritesViewModel = ViewModelProviders.of(this).get(FavouritesViewModel.class);
        favouriteNewsAdapter = new FavouriteNewsAdapter(getActivity(), this);
        recyclerView = view.findViewById(R.id.favnews_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(favouriteNewsAdapter);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout);
        lottieAnimationView = view.findViewById(R.id.fav_empty_anim);
        favEmtyText = view.findViewById(R.id.fav_empty_text);
        // for drawer hamburger animation
        if (getActivity() != null)
            drawerLayout = ((MainActivity) getActivity()).drawerLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                materialToolbar, R.string.drawer_open, R.string.drawer_close);

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(getActivity(), R.color.white));
        actionBarDrawerToggle.setDrawerSlideAnimationEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

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

    // checking fav news LiveData
    private void subscribe(){
        favouritesViewModel.getAllFavNews().observe(getViewLifecycleOwner(), favouriteNews -> {
            if (favouriteNews != null && favouriteNews.size() > 0){
                newsList = favouriteNews;
                lottieAnimationView.setVisibility(View.GONE);
                favEmtyText.setVisibility(View.GONE);
                favouriteNewsList = favouriteNews;
                shimmerFrameLayout.setVisibility(View.GONE);
                favouriteNewsAdapter.setFavouriteNewsList(favouriteNews);
                favouriteNewsAdapter.notifyDataSetChanged();
            } else {
                recyclerView.setVisibility(View.GONE);
                shimmerFrameLayout.setVisibility(View.GONE);
                lottieAnimationView.setVisibility(View.VISIBLE);
                favEmtyText.setVisibility(View.VISIBLE);
            }
        });
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

    @Override
    public void onItemToggleButtonUnChecked(int position) {
        FavouriteNews favouriteNews = favouriteNewsList.get(position);
        favouritesViewModel.deleteFavNews(favouriteNews);
    }

    @Override
    public void setItemToggleButton(ToggleButton toggleButton, int position) {
        FavouriteNews favouriteNews = favouriteNewsList.get(position);
        favouritesViewModel.getFavouriteNews(favouriteNews.getId()).observe(this, favouriteNews1 -> {
            if (favouriteNews1.length > 0){
                if (!toggleButton.isChecked()){
                    toggleButton.setChecked(true);
                }
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        FavouriteNews news = newsList.get(position);
        Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
        intent.putExtra("news_id", news.getId());
        intent.putExtra("news_title", news.getTitle());
        intent.putExtra("news_description", news.getDescription());
        intent.putExtra("news_image", news.getImage());
        intent.putExtra("news_image_caption", news.getImageCaption());
        intent.putExtra("news_category", news.getCategory());
//        intent.putExtra("news_time", news.());
        intent.putExtra("writer_name", news.getWriterName());
//        intent.putExtra("admin_id", news.getAdmin_id());
        startActivity(intent);
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

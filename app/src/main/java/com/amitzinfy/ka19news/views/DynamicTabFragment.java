package com.amitzinfy.ka19news.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.adapters.CategoryNewsAdapter;
import com.amitzinfy.ka19news.models.retrofit.News;
import com.amitzinfy.ka19news.models.room.FavouriteNews;
import com.amitzinfy.ka19news.utils.PreferenceManager;
import com.amitzinfy.ka19news.viewmodels.HeadLinesViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DynamicTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DynamicTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DynamicTabFragment extends Fragment implements CategoryNewsAdapter.NewsItemClickListener {
    private static final String TAG = "DynamicTabFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAB_POSITION = "param1";
    private static final String CATEGORY_ID = "param2";

    private int mTabPosition;
    private int mCategoryId;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private CategoryNewsAdapter categoryNewsAdapter;
    private ShimmerFrameLayout shimmerFrameLayout;
    private HeadLinesViewModel headLinesViewModel;
    private Observer<List<News>> newsObserver;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<News> newsList;
    private PreferenceManager preferenceManager;

    public DynamicTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DynamicTabFragment.
     */
    public static DynamicTabFragment newInstance(int param1, int param2) {
        DynamicTabFragment fragment = new DynamicTabFragment();
        Bundle args = new Bundle();
        args.putInt(TAB_POSITION, param1);
        args.putInt(CATEGORY_ID, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTabPosition = getArguments().getInt(TAB_POSITION);
            mCategoryId = getArguments().getInt(CATEGORY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dynamic_tab, container, false);

        init(rootView);
        subscribe();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            subscribe();
            swipeRefreshLayout.setRefreshing(false);
        });

        return rootView;
    }

    // initializing or binding views
    private void init(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.headline_recycler_view);
        categoryNewsAdapter = new CategoryNewsAdapter(getActivity(), this);
        recyclerView.setAdapter(categoryNewsAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.headline_swiperefresh);
        shimmerFrameLayout = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_layout);
        shimmerFrameLayout.startShimmer();
        headLinesViewModel = ViewModelProviders.of(this).get(HeadLinesViewModel.class);
        preferenceManager = PreferenceManager.getInstance(getActivity());
    }

    private void subscribe(){
        newsObserver = news -> {
            newsList = news;
            categoryNewsAdapter.setNewsList(news);
            categoryNewsAdapter.notifyDataSetChanged();
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        };
        headLinesViewModel.getNewsList(preferenceManager.getLanguageName(), mCategoryId).observe(getViewLifecycleOwner(), newsObserver);
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
        News news = newsList.get(position);
        Log.d(TAG, "onItemToggleButtonChecked: id: " + news.getId());
        headLinesViewModel.insertFavNews(new FavouriteNews(news.getId(),news.getTitle(), news.getDescription(),
                news.getImage(), news.getCategoryName()));
    }

    @Override
    public void onItemToggleButtonUnChecked(int position) {
        News news = newsList.get(position);
        Log.d(TAG, "onItemToggleButtonUnChecked: id: " + news.getId());
        headLinesViewModel.deleteFavNews(new FavouriteNews(news.getId(),news.getTitle(), news.getDescription(),
                news.getImage(), news.getCategoryName()));
    }

    @Override
    public void setItemToggleButton(ToggleButton toggleButton, int position) {
        News news = newsList.get(position);
        headLinesViewModel.getFavouriteNews(news.getId()).observe(getViewLifecycleOwner(), favouriteNews -> {
            if (favouriteNews.length > 0){
                if (!toggleButton.isChecked()) {
                    toggleButton.setChecked(true);
                }
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        News news = newsList.get(position);
        Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
        intent.putExtra("news_id", news.getId());
        intent.putExtra("news_title", news.getTitle());
        intent.putExtra("news_description", news.getDescription());
        intent.putExtra("news_image", news.getImage());
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
    }
}

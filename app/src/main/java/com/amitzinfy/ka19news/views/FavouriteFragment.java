package com.amitzinfy.ka19news.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private MaterialToolbar materialToolbar;
    private ActionBar actionBar;
    private FavouritesViewModel favouritesViewModel;
    private RecyclerView recyclerView;
    private FavouriteNewsAdapter favouriteNewsAdapter;
    private ShimmerFrameLayout shimmerFrameLayout;

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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);

        setToolbar(rootView);
        init(rootView);
        subscribe();

        return rootView;
    }

    private void setToolbar(View view){
        materialToolbar = (MaterialToolbar) view.findViewById(R.id.favourite_toolbar);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(materialToolbar);
            actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.setTitle(getResources().getString(R.string.app_name));
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
    }

    // checking fav news LiveData
    private void subscribe(){
        favouritesViewModel.getAllFavNews().observe(getViewLifecycleOwner(), new Observer<List<FavouriteNews>>() {
            @Override
            public void onChanged(List<FavouriteNews> favouriteNews) {
                if (favouriteNews != null){
                    shimmerFrameLayout.setVisibility(View.GONE);
                    favouriteNewsAdapter.setFavouriteNewsList(favouriteNews);
                    favouriteNewsAdapter.notifyDataSetChanged();
                }
            }
        });
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
//        favouritesViewModel.deleteFavNews();
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

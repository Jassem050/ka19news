package com.amitzinfy.ka19news.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.adapters.CategoryNewsAdapter;
import com.amitzinfy.ka19news.models.retrofit.News;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DynamicTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DynamicTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DynamicTabFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAB_POSITION = "param1";
    private static final String CATEGORY_ID = "param2";

    private int mTabPosition;
    private int mCategoryId;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private CategoryNewsAdapter categoryNewsAdapter;

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
        return rootView;
    }

    // initializing or binding views
    private void init(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.headline_recycler_view);
        categoryNewsAdapter = new CategoryNewsAdapter(getActivity());
        recyclerView.setAdapter(categoryNewsAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<News> newsList = new ArrayList<>();
        for (int i = 0; i< 10; i++){
            newsList.add(new News("news of category: " + mCategoryId));
        }
        categoryNewsAdapter.setNewsList(newsList);
        categoryNewsAdapter.notifyDataSetChanged();
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
    }
}

package com.amitzinfy.ka19news.views;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.utils.PreferenceManager;
import com.amitzinfy.ka19news.viewmodels.AddNewsViewModel;
import com.chinalwb.are.glidesupport.GlideApp;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PreviewNewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PreviewNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviewNewsFragment extends Fragment {
    private static final String TAG = "PreviewNewsFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private AppCompatTextView newsTitle, newsImageCaption;
    private AppCompatImageView newsImage;
    private WebView newsDescription;
    private PreferenceManager preferenceManager;
    private AddNewsViewModel addNewsViewModel;
    private RelativeLayout newsDetailLayout, bgSuccessLayout, progressBarLayout;

    public PreviewNewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreviewNewsFragment.
     */
    public static PreviewNewsFragment newInstance(String param1, String param2) {
        PreviewNewsFragment fragment = new PreviewNewsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_preview_news, container, false);
        bindViews(rootView);
        setNewsInfo();
        return rootView;
    }

    private void bindViews(View view){
        newsTitle = view.findViewById(R.id.news_title);
        newsImageCaption = view.findViewById(R.id.news_image_caption);
        newsImage = view.findViewById(R.id.news_image);
        newsDescription = view.findViewById(R.id.news_description);
        preferenceManager = PreferenceManager.getInstance(getActivity());
        addNewsViewModel = ViewModelProviders.of(getActivity()).get(AddNewsViewModel.class);
        newsDetailLayout = view.findViewById(R.id.news_detail_layout);
        bgSuccessLayout = view.findViewById(R.id.bg_layout_success);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
    }

    private void setNewsInfo(){
        newsTitle.setText(preferenceManager.getNewsTitle());
        newsDescription.loadData(preferenceManager.getNewsContent(), "text/html", "utf-8");
        if (!preferenceManager.getNewsImageUrl().equals("")) {
            Uri path = Uri.parse(preferenceManager.getNewsImageUrl());
            File file = getImageFile(path);
            GlideApp.with(getActivity()).load(file).placeholder(R.drawable.placeholder_image).into(newsImage);
        } else {
            newsImage.setVisibility(View.GONE);
            newsImageCaption.setVisibility(View.GONE);
        }
    }

    /**
     * actual image file
     * @param path image Uri
     * @return file
     */
    private File getImageFile(Uri path){
        String imgChooser = preferenceManager.getImgChooser();
        File file = null;
        if (imgChooser.equals("gallery")) {
            file = new File(getImageFilePath(path));
        } else if (imgChooser.equals("camera")){
            file = new File(preferenceManager.getNewsImageUrl());
        }
        return file;
    }

    /**
     * get path of image picked from gallery
     * @param uri image content uri
     * @return absolute path
     */
    private String getImageFilePath(Uri uri) {
        String path = null, image_id = null;
        Cursor cursor = null;
        Cursor cursor1 = null;
        if (getActivity() != null)
            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            image_id = cursor.getString(0);
            image_id = image_id.substring(image_id.lastIndexOf(":") + 1);
            cursor.close();
        }
        if (getActivity() != null)
            cursor1 = getActivity().getContentResolver()
                    .query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                            MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor1 != null) {
            cursor1.moveToFirst();
            path = cursor1.getString(cursor1.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor1.close();
        }
        return path;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.preview_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_send){
            progressBarLayout.setVisibility(View.VISIBLE);
            postNews();

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * hide action bar and
     * success alert dialog
     */
    private void showSuccessDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.success_layout, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        dialog.show();
        bgSuccessLayout.setVisibility(View.VISIBLE);
        MaterialButton okBtn = view.findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(view1 -> {
            dialog.dismiss();
            getActivity().finish();
        });
    }

    /**
     * upload news to server
     */
    private void postNews(){
        String languageId = preferenceManager.getLanguageId();
        String languageName = preferenceManager.getLanguageNameNews();
        String categoryId = preferenceManager.getCategoryIdNews();
        String newsTitle = preferenceManager.getNewsTitle();
        String newsContent = preferenceManager.getNewsContent();
        Uri path = Uri.parse(preferenceManager.getNewsImageUrl());

        File imageFile = getImageFile(path);
        File compressedFile = getCompressedFile(imageFile);
        addNewsViewModel.postNews(preferenceManager.getAccessToken(), compressedFile,
                languageId, languageName, categoryId, newsTitle, newsContent).observe(getViewLifecycleOwner(),
                addNewsResponse -> {
                    progressBarLayout.setVisibility(View.GONE);
//                    Toast.makeText(PreviewNewsFragment.this.getActivity(), "News Added" + addNewsResponse.getSuccess(), Toast.LENGTH_SHORT).show();
                    PreviewNewsFragment.this.clearAllDataFromPref();
                    if (preferenceManager.getImgChooser().equals("camera")) {
                        if (imageFile != null) {
                            if (imageFile.delete()){
                                showSuccessDialog();
                            }
                        }
                    } else {
                        showSuccessDialog();
                    }

                });

    }

    /**
     * compress the image using Compressor
     * @param file image file
     * @return compressed image file
     */
    private File getCompressedFile(File file){
        try {
            String imageName = null;
            if (file != null) {
                imageName = file.getName().replaceAll(".[jpg][png][jpeg]", "");
                imageName = imageName.replaceAll("[-]", "");
            }
            if (getActivity() != null) {
                File compressedImage = null;
                if (file != null) {
                    compressedImage = new Compressor(getActivity())
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/ka19news")
                            .compressToFile(file, imageName + ".webp");
                }

                return compressedImage;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void clearAllDataFromPref() {
        preferenceManager.setLanguageId("");
        preferenceManager.setLanguageNameNews("");
        preferenceManager.setCategoryIdNews("");
        preferenceManager.setNewsTitle("");
        preferenceManager.setNewsContent("");
        preferenceManager.setNewsImageUrl("");
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

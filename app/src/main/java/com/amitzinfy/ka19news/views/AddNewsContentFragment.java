package com.amitzinfy.ka19news.views;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.utils.PreferenceManager;
import com.amitzinfy.ka19news.viewmodels.AddNewsViewModel;
import com.chinalwb.are.AREditText;
import com.chinalwb.are.styles.toolbar.ARE_ToolbarDefault;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentCenter;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentLeft;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_AlignmentRight;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_At;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_FontColor;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_FontSize;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Hr;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Italic;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Link;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListBullet;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_ListNumber;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Quote;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Strikethrough;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Subscript;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Superscript;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Underline;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddNewsContentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNewsContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewsContentFragment extends Fragment {
    private static final String TAG = "AddNewsContentFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private IARE_Toolbar mToolbar;
    private AREditText mEditText;
    private boolean scrollerAtEnd;

    private PreferenceManager preferenceManager;
    private ActionBar actionBar;
    private AddNewsViewModel addNewsViewModel;
    private RelativeLayout bgSuccessLayout, progressBarLayout;


    public AddNewsContentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewsContentFragment.
     */
    public static AddNewsContentFragment newInstance(String param1, String param2) {
        AddNewsContentFragment fragment = new AddNewsContentFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_add_news_content, container, false);

        initToolbar(rootView);
        bindViews(rootView);
        if (getActivity() != null)
        addNewsViewModel = ViewModelProviders.of(getActivity()).get(AddNewsViewModel.class);

        if (preferenceManager.getNewsContent() != null && !preferenceManager.getNewsContent().equals("")) {
            mEditText.fromHtml(preferenceManager.getNewsContent());
        }
        return rootView;
    }

    private void bindViews(View view){
        bgSuccessLayout = view.findViewById(R.id.bg_layout_success);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        preferenceManager = PreferenceManager.getInstance(getActivity());
    }

    private void setActionBar(View view) {
        if (getActivity() != null) {
            actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        }
    }

    /**
     * editor tools toolbar
     * @param view rootView
     */
    private void initToolbar(View view) {
        mToolbar = view.findViewById(R.id.areToolbar);
        IARE_ToolItem bold = new ARE_ToolItem_Bold();
        IARE_ToolItem italic = new ARE_ToolItem_Italic();
        IARE_ToolItem underline = new ARE_ToolItem_Underline();
        IARE_ToolItem strikethrough = new ARE_ToolItem_Strikethrough();
        IARE_ToolItem quote = new ARE_ToolItem_Quote();
        IARE_ToolItem listNumber = new ARE_ToolItem_ListNumber();
        IARE_ToolItem listBullet = new ARE_ToolItem_ListBullet();
        IARE_ToolItem hr = new ARE_ToolItem_Hr();
        IARE_ToolItem link = new ARE_ToolItem_Link();
        IARE_ToolItem subscript = new ARE_ToolItem_Subscript();
        IARE_ToolItem superscript = new ARE_ToolItem_Superscript();
        IARE_ToolItem left = new ARE_ToolItem_AlignmentLeft();
        IARE_ToolItem center = new ARE_ToolItem_AlignmentCenter();
        IARE_ToolItem right = new ARE_ToolItem_AlignmentRight();
//        IARE_ToolItem image = new ARE_ToolItem_Image();
//        IARE_ToolItem video = new ARE_ToolItem_Video();
        IARE_ToolItem at = new ARE_ToolItem_At();
        IARE_ToolItem font = new ARE_ToolItem_FontSize();
        IARE_ToolItem color = new ARE_ToolItem_FontColor();
        mToolbar.addToolbarItem(bold);
        mToolbar.addToolbarItem(italic);
        mToolbar.addToolbarItem(underline);
        mToolbar.addToolbarItem(font);
        mToolbar.addToolbarItem(color);
        mToolbar.addToolbarItem(strikethrough);
        mToolbar.addToolbarItem(quote);
        mToolbar.addToolbarItem(listNumber);
        mToolbar.addToolbarItem(listBullet);
        mToolbar.addToolbarItem(hr);
        mToolbar.addToolbarItem(link);
        mToolbar.addToolbarItem(subscript);
        mToolbar.addToolbarItem(superscript);
        mToolbar.addToolbarItem(left);
        mToolbar.addToolbarItem(center);
        mToolbar.addToolbarItem(right);
//        mToolbar.addToolbarItem(image);
//        mToolbar.addToolbarItem(video);
        mToolbar.addToolbarItem(at);

        mEditText = view.findViewById(R.id.arEditText);
        mEditText.setToolbar(mToolbar);
        initToolbarArrow(view);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_news_menu, menu);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String newsContent = mEditText.getText().toString();
        if (item.getItemId() == R.id.action_send) {
            if (newsContent.equals("")) {
                mEditText.setError("Enter the content");
            } else {
                progressBarLayout.setVisibility(View.VISIBLE);
                postNews();
            }
        } else if (item.getItemId() == R.id.action_preview){
            if (newsContent.equals("")){
                mEditText.setError("Enter the content");
            } else {
                preferenceManager.setNewsContent(mEditText.getHtml());
                loadPreviewFragment();
            }
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

    private void loadPreviewFragment(){
        Fragment fragment = PreviewNewsFragment.newInstance("preview","preview");
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.frame_container, fragment).commit();
    }

    /**
     * upload news to server
     */
    private void postNews(){
        preferenceManager.setNewsContent(mEditText.getHtml());
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
//                    Toast.makeText(AddNewsContentFragment.this.getActivity(), "News Added" + addNewsResponse.getSuccess(), Toast.LENGTH_SHORT).show();
                    AddNewsContentFragment.this.clearAllDataFromPref();
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
     * compress the image using Compressor
     * @param file image file
     * @return compressed image file
     */
    private File getCompressedFile(File file){
        try {
            String imageName = null;
            if (file != null) {
                imageName = file.getName().replaceAll(".[jpg][png][jpeg]", "");
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

    /**
     * editor tools toolbar arrow
     * @param view imageView
     */
    private void initToolbarArrow(View view) {
        final AppCompatImageView imageView = view.findViewById(R.id.arrow);
        if (this.mToolbar instanceof ARE_ToolbarDefault) {
            ((ARE_ToolbarDefault) mToolbar).getViewTreeObserver().addOnScrollChangedListener(() -> {
                int scrollX = ((ARE_ToolbarDefault) mToolbar).getScrollX();
                int scrollWidth = ((ARE_ToolbarDefault) mToolbar).getWidth();
                int fullWidth = ((ARE_ToolbarDefault) mToolbar).getChildAt(0).getWidth();

                if (scrollX + scrollWidth < fullWidth) {
                    imageView.setImageResource(R.drawable.ic_arrow_forward_black_24dp);
                    scrollerAtEnd = false;
                } else {
                    imageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
                    scrollerAtEnd = true;
                }
            });
        }

        imageView.setOnClickListener(view1 -> {
            if (scrollerAtEnd) {
                ((ARE_ToolbarDefault) mToolbar).smoothScrollBy(-Integer.MAX_VALUE, 0);
                scrollerAtEnd = false;
            } else {
                int hsWidth = ((ARE_ToolbarDefault) mToolbar).getChildAt(0).getWidth();
                ((ARE_ToolbarDefault) mToolbar).smoothScrollBy(hsWidth, 0);
                scrollerAtEnd = true;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        preferenceManager.setNewsContent(mEditText.getHtml());
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

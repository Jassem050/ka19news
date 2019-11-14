package com.amitzinfy.ka19news.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.utils.NetworkUtils;
import com.amitzinfy.ka19news.utils.PreferenceManager;
import com.amitzinfy.ka19news.viewmodels.UserViewModel;
import com.chinalwb.are.glidesupport.GlideApp;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

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
    private static final int IMG_REQUEST_CODE = 123;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

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
    private Bitmap bitmap;
    private String encodedImage;
    private AppCompatImageView profileImage;
    private MaterialCardView addNewsBtn, viewNewsBtn;

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
        profileImage = view.findViewById(R.id.profile_image);
        addNewsBtn = view.findViewById(R.id.add_news);
        viewNewsBtn = view.findViewById(R.id.view_btn_layout);

        preferenceManager = PreferenceManager.getInstance(getActivity());
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        uploadImageBtn.setOnClickListener(view1 -> {
            Log.d(TAG, "onClick: access_token: " + preferenceManager.getAccessToken());
            if (getActivity() != null) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    selectImage();
                }
            }
        });

        addNewsBtn.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), AddNewsActivity.class)));
    }

    private void getUserDetails(String access_token){
        Log.d(TAG, "getUserDetails: ");
        userViewModel.getUserDetails(access_token).observe(getViewLifecycleOwner(), userResponse -> {
            Log.d(TAG, "getUserDetails: user: " + userResponse.getUser().toString());
            profileName.setText(userResponse.getUser().getName());
            profileEmail.setText(userResponse.getUser().getEmail());
            profileMobileNo.setText(userResponse.getUser().getMobileNumber());
            if (getActivity() != null)
            GlideApp.with(getActivity()).load(NetworkUtils.PROFILE_IMG_URL + userResponse.getUser().getImage()).into(profileImage);
        });
    }

    private void getAddedNewsCount(String access_token){
        userViewModel.getAddedNewsCount(access_token).observe(getViewLifecycleOwner(), newsAdded -> {
            newsAddCount.setText(String.valueOf(newsAdded.getNewsCount()));
            newsAcceptCount.setText(String.valueOf(newsAdded.getNewsAcceptedCount()));
        });
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST_CODE);
    }


    private void uploadImage(String access_token, File file){
        userViewModel.updateProfImage(access_token, file).observe(getViewLifecycleOwner(), user -> {
            if (getActivity() != null)
                GlideApp.with(getActivity()).load(NetworkUtils.PROFILE_IMG_URL + user.getImage()).into(profileImage);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: granted");
                selectImage();
            } else {
                Toast.makeText(getActivity(), "Permission is needed to upload", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getActivity() != null) {
            if (requestCode == IMG_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
                Uri path = data.getData();

                File file = new File(getImageFilePath(path));
                File compressImage;
                try {
                     String imageName = file.getName().replaceAll(".[jpg][png][jpeg]", "");
                     compressImage = new Compressor(getActivity())
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(file, imageName + ".webp");
                    Log.d(TAG, "onActivityResult: compressImage: " + compressImage.getPath());
                    uploadImage(preferenceManager.getAccessToken(), compressImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


//    public String getRealPathFromURI(Context context, Uri contentUri) {
//        Cursor cursor = null;
//        try {
//            String[] proj = { MediaStore.Images.Media.DATA };
//            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            Log.d(TAG, "getRealPathFromURI: index: " + column_index);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } catch (Exception e) {
//            Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
//            return "";
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }

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
        cursor1 = getActivity().getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor1!=null) {
            cursor1.moveToFirst();
            path = cursor1.getString(cursor1.getColumnIndex(MediaStore.Images.Media.DATA));
            Log.d(TAG, "getImageFilePath: path: " + path);
            cursor1.close();
        }
        return path;
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
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

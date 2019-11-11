package com.amitzinfy.ka19news.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.amitzinfy.ka19news.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddNewsDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNewsDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewsDetailsFragment extends Fragment {
    private static final String TAG = "AddNewsDetailsFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int IMG_REQUEST_CODE = 321;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 456;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextInputLayout languageInputLayout, categoryInputLayout;
    private TextInputEditText newsTitleEditText;
    private AutoCompleteTextView languageDropDown, categoryDropDown;
    private RelativeLayout selectImageBtn;
    private AppCompatImageView uploadedImageView;
    private Bitmap bitmap;

    public AddNewsDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewsDetailsFragment.
     */
    public static AddNewsDetailsFragment newInstance(String param1, String param2) {
        AddNewsDetailsFragment fragment = new AddNewsDetailsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_add_news_details, container, false);
        bindViews(rootView);

        String[] COUNTRIES = new String[] {"Item 1", "Item 2", "Item 3", "Item 4"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,
                        COUNTRIES);


        languageDropDown.setAdapter(adapter);


        languageDropDown.setOnItemClickListener((adapterView, view, position, l) -> {
            Log.d(TAG, "onItemSelected: id: " + position);
        });
        rootView.findViewById(R.id.click_btn).setOnClickListener(view -> {
            Log.d(TAG, "onCreateView: " + languageDropDown.getText().toString());

            if (languageDropDown.getText().toString().equals("")) {
                languageInputLayout.setErrorEnabled(true);
                languageInputLayout.setError("not selected");
            }
        });

        selectImageBtn.setOnClickListener(view-> {
            if (getActivity() != null) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    selectImage();
                }
            }
        });

        return rootView;
    }

    private void bindViews(View view){
        languageInputLayout = view.findViewById(R.id.languages_input_layout);
        categoryInputLayout = view.findViewById(R.id.categories_input_layout);
        languageDropDown = view.findViewById(R.id.languages_dropdown);
        categoryDropDown = view.findViewById(R.id.categories_dropdown);
        selectImageBtn = view.findViewById(R.id.upload_img_layout);
        uploadedImageView = view.findViewById(R.id.uploaded_img);
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST_CODE);
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
        if (requestCode == IMG_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
            Uri path = data.getData();
            // store uri in sharedpreferences with other data
//            Log.d(TAG, "onActivityResult: path: " + path.toString());
//            Uri uri = Uri.parse(path.toString());
//            Log.d(TAG, "onActivityResult: uri: " + uri.toString());


            try {
                if (getActivity() != null) {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                        bitmap = getResizedBitmap(bitmap, 1024);
                    } else {
                        ImageDecoder.Source source;
                        if (path != null) {
                            source = ImageDecoder.createSource(getActivity().getContentResolver(), path);
                            bitmap = ImageDecoder.decodeBitmap(source);
                        }

                    }
                    selectImageBtn.setVisibility(View.GONE);
                    uploadedImageView.setVisibility(View.VISIBLE);
                    uploadedImageView.setImageBitmap(bitmap);
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

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

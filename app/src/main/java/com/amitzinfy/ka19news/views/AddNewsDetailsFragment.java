package com.amitzinfy.ka19news.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.amitzinfy.ka19news.R;
import com.amitzinfy.ka19news.models.retrofit.Category;
import com.amitzinfy.ka19news.models.retrofit.Language;
import com.amitzinfy.ka19news.utils.PreferenceManager;
import com.amitzinfy.ka19news.viewmodels.AddNewsViewModel;
import com.chinalwb.are.glidesupport.GlideApp;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddNewsDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNewsDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewsDetailsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "AddNewsDetailsFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int IMG_REQUEST_CODE = 321;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 456;
    private static final int CAMERA_IMG_REQUEST_CODE = 789;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextInputLayout languageInputLayout, categoryInputLayout, newsTitleLayout;
    private TextInputEditText newsTitleEditText;
    private AutoCompleteTextView languageDropDown, categoryDropDown;
    private RelativeLayout selectImageBtn;
    private LinearLayout btnLayout;
    private AppCompatImageView uploadedImageView;
    private Bitmap bitmap;
    private MaterialButton nextBtn, changeImgBtn, removeImgBtn;

    private AddNewsViewModel addNewsViewModel;
    private PreferenceManager preferenceManager;
    private List<Language> languageListForID;
    private List<Category> categoryListForID;
    private File mPhotoFile = null;
    private Uri mImgUri;

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

        getLanguages(preferenceManager.getAccessToken());


        languageDropDown.setOnItemClickListener((adapterView, view, position, l) -> {
            if (languageListForID != null && languageListForID.size() > 0){
                Language language = languageListForID.get(position);
                categoryDropDown.setText("");
                preferenceManager.setLanguageId(language.getLanguageId());
                preferenceManager.setLanguageNameNews(language.getLanguageName());
                getCategories(preferenceManager.getAccessToken(), language.getLanguageId());
            }
        });

        categoryDropDown.setOnItemClickListener((adapterView, view, position, l) -> {
            Category category = categoryListForID.get(position);
            preferenceManager.setCategoryIdNews(String.valueOf(category.getId()));
            newsTitleLayout.setVisibility(View.VISIBLE);
            selectImageBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
        });

        newsTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (newsTitleEditText.getText().toString().length() > 0){
                    newsTitleLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return rootView;
    }

    private void bindViews(View view){
        preferenceManager = PreferenceManager.getInstance(getActivity());
        if (getActivity() != null)
        addNewsViewModel = ViewModelProviders.of(getActivity()).get(AddNewsViewModel.class);
        // input layouts
        languageInputLayout = view.findViewById(R.id.languages_input_layout);
        categoryInputLayout = view.findViewById(R.id.categories_input_layout);
        newsTitleLayout = view.findViewById(R.id.news_title_layout);
        // editext
        newsTitleEditText = view.findViewById(R.id.news_title_edittext);
        // Dropdowns
        languageDropDown = view.findViewById(R.id.languages_dropdown);
        categoryDropDown = view.findViewById(R.id.categories_dropdown);
        uploadedImageView = view.findViewById(R.id.uploaded_img);
        // buttons
        selectImageBtn = view.findViewById(R.id.upload_img_layout);
        nextBtn = view.findViewById(R.id.next_btn);
        changeImgBtn = view.findViewById(R.id.change_image_btn);
        btnLayout = view.findViewById(R.id.btn_layout);
        removeImgBtn = view.findViewById(R.id.remove_image_btn);
        // setOnclickListeners
        selectImageBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        changeImgBtn.setOnClickListener(this);
        removeImgBtn.setOnClickListener(this);
    }

    /**
     * load languages from server to the dropdown menu
     * livedata observer
     * @param accessToken user access token
     */
    private void getLanguages(String accessToken){
        addNewsViewModel.getLanguages(accessToken).observe(getViewLifecycleOwner(), languages -> {
            languageListForID = languages;
            List<String> languageList = new ArrayList<>();
            for (Language language : languages) {
                 languageList.add(language.getLanguage());
            }

            if (getContext() != null) {
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(
                                getContext(),
                                R.layout.dropdown_menu_popup_item,
                                languageList);


                languageDropDown.setAdapter(adapter);
            }
        });
    }

    /**
     * load categories of particular language from to category dropdown menu
     * livedata observer
     * @param accessToken user access token
     * @param languageId language id of selected language
     */
    private void getCategories(String accessToken, String languageId){
        addNewsViewModel.getCategories(accessToken, languageId).observe(getViewLifecycleOwner(), categories -> {
            categoryListForID = categories;
            List<String> categoryList = new ArrayList<>();
            for (Category category : categories){
                categoryList.add(category.getName());
            }
            if (getContext() != null) {
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(
                                getContext(),
                                R.layout.dropdown_menu_popup_item,
                                categoryList);
                categoryDropDown.setAdapter(adapter);
                categoryInputLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * image picker dialog to upload picture from
     * camera or gallery
     */
    private void showImagePickerDialog(){
        CharSequence[] items = {"Camera", "Gallery"};
        if (getActivity() != null) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
            builder.setItems(items, (dialogInterface, i) -> {
                switch (i) {
                    case 0:
                        captureImageFromCamera();
                        break;
                    case 1:
                        selectImageFromGallery();
                        break;
                }
            });
            builder.create().show();
        }
    }

    /**
     * camera image picker
     * create image file in application package in file/Pictures directory
     * then start camera intent.
     */
    private void captureImageFromCamera(){
        if (mPhotoFile != null && mPhotoFile.length() == 0){
            mPhotoFile.delete();
            mPhotoFile = null;
        }
        preferenceManager.setImgChooser("camera");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (getActivity() != null)
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(TAG, "captureImageFromCamera: fileError: ", ex);
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),"com.amitzinfy.ka19news.provider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                mPhotoFile = photoFile;
                mImgUri = photoURI;
                Log.d(TAG, "captureImageFromCamera: imgName: " + mPhotoFile.getName());
                startActivityForResult(cameraIntent, CAMERA_IMG_REQUEST_CODE);
            }

        }
    }

    /**
     * creating image file before capturing from camera and
     * store it in the Pictures directory in the application package.
     */
    private String imageFilePath;
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    /**
     * image selection from gallery
     */
    private void selectImageFromGallery(){
        preferenceManager.setImgChooser("gallery");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST_CODE);
    }

    /**
     * resizing the image picked from gallery
     * @param image bitmap image
     * @param maxSize size
     * @return scaled bitmap
     */
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
                showImagePickerDialog();
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
            imageUploadFromGalley(data);
        } else if (requestCode == CAMERA_IMG_REQUEST_CODE && resultCode == Activity.RESULT_OK ){

            preferenceManager.setNewsImageUrl(mPhotoFile.getAbsolutePath());
            Log.d(TAG, "onActivityResult: imgNameAbsoloute: " + mPhotoFile.getAbsolutePath());
            Log.d(TAG, "onActivityResult: imgNameUri: " +mImgUri.toString());
            Log.d(TAG, "onActivityResult: imgNamePath: " + mImgUri.getPath());

            try {
                if (getActivity() != null) {
                    File photoFile = new Compressor(getActivity())
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToFile(mPhotoFile);
                    GlideApp.with(getActivity()).load(photoFile).into(uploadedImageView);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     *  get intent data from onActivityResult and
     *  store the uri of image in sharedPreference
     *  and set image bitmap to uploadImageView
     *
     */
    private void imageUploadFromGalley(Intent data){
        Uri path = data.getData();

        if (path != null) {
            preferenceManager.setNewsImageUrl(path.toString());
        }

        bitmap = null;
        try {
            if (getActivity() != null) {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                } else {
                    ImageDecoder.Source source;
                    if (path != null) {
                        source = ImageDecoder.createSource(getActivity().getContentResolver(), path);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    }
                }
                bitmap = getResizedBitmap(bitmap, 1024);
                btnLayout.setVisibility(View.VISIBLE);
                uploadedImageView.setVisibility(View.VISIBLE);
                uploadedImageView.setImageBitmap(bitmap);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * check for read, write external storage
     * and camera runtime permissions
     * @return boolean
     */
    private boolean isPermissionGranted(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    }, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return false;
        } else
            return true;
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.upload_img_layout:
                if (getActivity() != null) {
                    if (isPermissionGranted()){
                        showImagePickerDialog();
                    }
                }
                break;

            case R.id.next_btn:
                String title = newsTitleEditText.getText().toString();
                if (languageDropDown.getText().toString().equals("")) {
                    languageInputLayout.setErrorEnabled(true);
                    languageInputLayout.setError("not selected");
                }
                else if (languageDropDown.getText().toString().equals("")){
                    categoryInputLayout.setErrorEnabled(true);
                    categoryInputLayout.setError("not selected");
                }
                else if (title.equals("")){
                    newsTitleLayout.setErrorEnabled(true);
                    newsTitleLayout.setError("Enter the news title");
                } else {
                    preferenceManager.setNewsTitle(title);
                    if (bitmap == null && mPhotoFile == null) {
                        showConfirmDialog();
                    } else {
                        loadAddNewsContentFragment();
                    }
                }
                break;

            case R.id.change_image_btn:
                showImagePickerDialog();
                break;
            case R.id.remove_image_btn:
                removeImageFromImageView();
                break;
        }
    }



    private void loadAddNewsContentFragment(){
        Fragment fragment = AddNewsContentFragment.newInstance("news_content", "news_content");
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, fragment).addToBackStack(null).commit();
        }
    }

    /**
     * dialog shown if image is not picked
     */
    private void showConfirmDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle("Upload an image");
        builder.setCancelable(true);
        builder.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
//        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    /**
     * make the uploadImageView invisible if image is not picked
     */
    private void removeImageFromImageView(){
        bitmap = null;
        uploadedImageView.setImageBitmap(bitmap);
        uploadedImageView.setVisibility(View.GONE);
        btnLayout.setVisibility(View.GONE);
        selectImageBtn.setVisibility(View.VISIBLE);
        preferenceManager.setNewsImageUrl("");
        if (mPhotoFile != null) mPhotoFile.delete();
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void displayAllViews(){
        categoryInputLayout.setVisibility(View.VISIBLE);
        newsTitleLayout.setVisibility(View.VISIBLE);
        selectImageBtn.setVisibility(View.VISIBLE);
        nextBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (categoryListForID != null && categoryListForID.size() > 0){
            displayAllViews();
        }
        // display the image picked from gallery
        if (bitmap != null){
            selectImageBtn.setVisibility(View.GONE);
            btnLayout.setVisibility(View.VISIBLE);
            uploadedImageView.setVisibility(View.VISIBLE);
            uploadedImageView.setImageBitmap(bitmap);
        }
        // load the captured image file from camera to uploadImageView
        if (mPhotoFile != null){
            selectImageBtn.setVisibility(View.GONE);
            btnLayout.setVisibility(View.VISIBLE);
            uploadedImageView.setVisibility(View.VISIBLE);
            if (getActivity() != null)
            GlideApp.with(getActivity()).load(mPhotoFile).into(uploadedImageView);
        }
        // if image not captured from camera or camera closed before capturing
        // delete the created image file from Pictures directory in application package
        if (mPhotoFile != null && mPhotoFile.length() == 0){
            mPhotoFile.delete();
            mPhotoFile = null;
            removeImageFromImageView();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPhotoFile != null)
        mPhotoFile.delete();
    }
}

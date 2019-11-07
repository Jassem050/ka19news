package com.amitzinfy.ka19news.models.retrofit;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("gender")
    private String gender;
    @Expose
    @SerializedName("dob")
    private String dateOfBirth;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("location")
    private String address;
    @Expose
    @SerializedName("mobile")
    private String mobileNumber;
    @Expose
    @SerializedName("image")
    private String image;
    @Expose
    @SerializedName("message")
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @NonNull
    @Override
    public String toString() {
        return name + "\n" + address + "\n" + mobileNumber;
    }
}

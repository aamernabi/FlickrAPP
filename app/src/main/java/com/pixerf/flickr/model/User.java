package com.pixerf.flickr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * created by Aamer on 12/17/2016.
 */

public class User implements Parcelable {

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private String username;
    private String fullName;

    // TODO: Add fields for uploading photos and getting user's photos
    private String userId;

    public User() {

    }

    //constructor
    public User(String username, String fullName, String userId) {
        setUsername(username);
        setFullName(fullName);
        setUserId(userId);
    }

    // parcel constructor
    protected User(Parcel in) {
        username = in.readString();
        fullName = in.readString();
        userId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(fullName);
        parcel.writeString(userId);
    }

    //getters abd setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

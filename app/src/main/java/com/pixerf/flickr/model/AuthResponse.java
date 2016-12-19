package com.pixerf.flickr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * created by Aamer on 12/17/2016.
 */

public class AuthResponse implements Parcelable {

    public static final Creator<AuthResponse> CREATOR = new Creator<AuthResponse>() {
        @Override
        public AuthResponse createFromParcel(Parcel in) {
            return new AuthResponse(in);
        }

        @Override
        public AuthResponse[] newArray(int size) {
            return new AuthResponse[size];
        }
    };
    private String token;
    private User user;
    private String permission;
    private String stat;
    private Error error;

    public AuthResponse() {

    }

    public AuthResponse(String stat, String token, User user, String permission) {
        setStat(stat);
        setToken(token);
        setUser(user);
        setPermission(permission);
    }

    public AuthResponse(String stat, Error error) {
        setStat(stat);
        setError(error);
    }

    // parcel
    protected AuthResponse(Parcel in) {
        stat = in.readString();
        token = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        permission = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(stat);
        parcel.writeString(token);
        parcel.writeParcelable(user, i);
        parcel.writeString(permission);
    }

    //getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}

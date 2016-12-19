package com.pixerf.flickr.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * created by Aamer on 12/18/2016.
 */

public class Photos implements Parcelable {

    public static final Creator<Photos> CREATOR = new Creator<Photos>() {
        @Override
        public Photos createFromParcel(Parcel in) {
            return new Photos(in);
        }

        @Override
        public Photos[] newArray(int size) {
            return new Photos[size];
        }
    };
    private String page;
    private String pages;
    private String perpage;
    private String total;
    private List<Photo> photoList;
    private String stat;
    private Error error;

    public Photos() {
    }

    public Photos(String stat, String page, String pages, String perpage, String total, List<Photo> photoList) {
        this.stat = stat;
        this.page = page;
        this.pages = pages;
        this.perpage = perpage;
        this.total = total;
        this.photoList = photoList;
    }

    public Photos(String stat, Error error) {
        setStat(stat);
        setError(error);
    }

    //parcel methods and fields
    protected Photos(Parcel in) {
        page = in.readString();
        pages = in.readString();
        perpage = in.readString();
        total = in.readString();
        photoList = in.createTypedArrayList(Photo.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(page);
        parcel.writeString(pages);
        parcel.writeString(perpage);
        parcel.writeString(total);
        parcel.writeTypedList(photoList);
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPerpage() {
        return perpage;
    }

    public void setPerpage(String perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
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

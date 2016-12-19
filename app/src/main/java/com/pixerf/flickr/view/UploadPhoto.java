package com.pixerf.flickr.view;

import android.content.Context;
import android.os.AsyncTask;

import com.pixerf.flickr.R;
import com.pixerf.flickr.utils.UrlUtils;

import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * created by Aamer on 12/19/2016.
 */

public class UploadPhoto {

    LinkedHashMap<String, String> params;

    public UploadPhoto(Context context, byte[] data, String title) {
        params = new LinkedHashMap<>();
        params.put(context.getString(R.string.api_key), UrlUtils.API_KEY);
        params.put("title", title);
        params.put("photo", Arrays.toString(data));
        // TODO: 12/18/2016 Rename user_idd with user id
    }

    public void execute() {
        new UploadPhotoTask().execute();
    }

    private class UploadPhotoTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            //new FlickrPhotos().uploadPhoto(params);
            return null;
        }
    }
}

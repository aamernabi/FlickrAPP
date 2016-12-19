package com.pixerf.flickr.utils;

import android.net.Uri;
import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * created by Aamer on 12/17/2016.
 */

public class UrlBuilder {

    private static final String TAG = UrlBuilder.class.getSimpleName();

    public static String build(String endPoint, LinkedHashMap<String, String> params) {
        String url = null;

        Uri.Builder builder = Uri.parse(endPoint).buildUpon();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }

        url = builder.build().toString();

        Log.e(TAG, "URL = " + url);

        return url;
    }

    public static String build(String searchQuery, int page) {
        String url = null;
        if (searchQuery == null || searchQuery.trim().equalsIgnoreCase("null")) {
            url = Uri.parse(UrlUtils.END_POINT).buildUpon()
                    .appendQueryParameter("method", UrlUtils.METHOD_INTERESTINGNESS)
                    .appendQueryParameter("api_key", UrlUtils.API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("page", String.valueOf(page))
                    .build().toString();
        } else if (searchQuery.trim().equalsIgnoreCase("recent")) {
            url = Uri.parse(UrlUtils.END_POINT).buildUpon()
                    .appendQueryParameter("method", UrlUtils.METHOD_GET_RECENT)
                    .appendQueryParameter("api_key", UrlUtils.API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("page", String.valueOf(page))
                    .build().toString();
        } else {
            url = Uri.parse(UrlUtils.END_POINT).buildUpon()
                    .appendQueryParameter("method", UrlUtils.METHOD_SEARCH)
                    .appendQueryParameter("api_key", UrlUtils.API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("text", searchQuery)
                    .appendQueryParameter("page", String.valueOf(page))
                    .build().toString();
        }

        Log.e(TAG, "URL = " + url);

        return url;
    }
}

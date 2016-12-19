package com.pixerf.flickr.auth;

import android.util.Log;

import com.pixerf.flickr.model.AuthResponse;
import com.pixerf.flickr.network.NetConnection;
import com.pixerf.flickr.parser.XMLHandler;
import com.pixerf.flickr.utils.SignatureGenerator;
import com.pixerf.flickr.utils.UrlBuilder;
import com.pixerf.flickr.utils.UrlUtils;

import java.util.LinkedHashMap;
import java.util.TreeMap;

/**
 * created by Aamer on 12/17/2016.
 */

public class FlickrAuth {

    private final static String TAG = FlickrAuth.class.getSimpleName();

    public AuthResponse getFullToken(LinkedHashMap<String, String> params) {

        TreeMap<String, String> map = new TreeMap<>();
        map.putAll(params);
        params.put("api_sig", SignatureGenerator.getMD5Signature(UrlUtils.SECRET, map));

        Log.e(TAG, "params = " + params);

        String str = NetConnection.makeGETRequest(UrlBuilder.build(UrlUtils.END_POINT, params));
        if (str != null)
            return new XMLHandler().parse(str);
        else
            return null;
    }
}

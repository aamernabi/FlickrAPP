package com.pixerf.flickr.photos;

import android.util.Log;

import com.pixerf.flickr.model.Photos;
import com.pixerf.flickr.network.NetConnection;
import com.pixerf.flickr.parser.JSONParser;
import com.pixerf.flickr.utils.SignatureGenerator;
import com.pixerf.flickr.utils.UrlBuilder;
import com.pixerf.flickr.utils.UrlUtils;

import java.util.LinkedHashMap;
import java.util.TreeMap;

/**
 * created by Aamer on 12/18/2016.
 */

public class FlickrPhotos {

    private static String TAG = FlickrPhotos.class.getSimpleName();

    public Photos getUserPhotos(LinkedHashMap<String, String> params) {

        TreeMap<String, String> map = new TreeMap<>();
        map.putAll(params);
        params.put("api_sig", SignatureGenerator.getMD5Signature(UrlUtils.SECRET, map));

        Log.e(TAG, "params = " + params);

        String str = NetConnection.makeGETRequest(UrlBuilder.build(UrlUtils.END_POINT, params));
        if (str != null)
            return JSONParser.parse(str);
        else
            return null;
    }

    public Photos searchPhotos(String searchQuery, int page) {

        String str = NetConnection.makeGETRequest(UrlBuilder.build(searchQuery, page));
        if (str != null)
            return JSONParser.parse(str);
        else
            return null;
    }

    /*public void uploadPhoto(LinkedHashMap<String, String> params){
        TreeMap<String, String> map = new TreeMap<>();
        map.putAll(params);
        params.put("api_sig", SignatureGenerator.getMD5Signature(UrlUtils.SECRET, map));

        Log.e(TAG, "params = "+params);
        String str = NetConnection.makePOSTRequest(UrlBuilder.build(UrlUtils.END_POINT_UPLOAD, params), params);
        Log.e(TAG, str);
    }*/
}

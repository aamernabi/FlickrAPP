package com.pixerf.flickr.parser;

import android.util.Log;

import com.pixerf.flickr.model.Error;
import com.pixerf.flickr.model.Photo;
import com.pixerf.flickr.model.Photos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * created by Aamer on 12/18/2016.
 */

public class JSONParser {
    private static final String TAG = JSONParser.class.getSimpleName();

    public static Photos parse(String response) {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            if (jsonObject.getString("stat").equalsIgnoreCase("ok")) {
                JSONObject jsonObjectPhotos = jsonObject.getJSONObject("photos");
                List<Photo> photoList = new ArrayList<>();
                JSONArray jsonArray = jsonObjectPhotos.getJSONArray("photo");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectPhoto = jsonArray.getJSONObject(i);
                    Photo photo = new Photo(
                            jsonObjectPhoto.getString("id"),
                            jsonObjectPhoto.getString("owner"),
                            jsonObjectPhoto.getString("secret"),
                            jsonObjectPhoto.getString("server"),
                            jsonObjectPhoto.getString("farm"),
                            jsonObjectPhoto.getString("title")
                    );
                    photoList.add(photo);
                }
                return new Photos(
                        jsonObject.getString("stat"),
                        jsonObjectPhotos.getString("page"),
                        jsonObjectPhotos.getString("pages"),
                        jsonObjectPhotos.getString("perpage"),
                        jsonObjectPhotos.getString("total"),
                        photoList
                );
            } else if (jsonObject.getString("stat").equalsIgnoreCase("fail")) {
                Error error = new Error();
                error.setCode(jsonObject.getInt("code"));
                error.setMessage(jsonObject.getString("message"));
                return new Photos(jsonObject.getString("stat"), error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage() + ", " + Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
}

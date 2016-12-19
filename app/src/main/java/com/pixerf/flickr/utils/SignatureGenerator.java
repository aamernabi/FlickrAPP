package com.pixerf.flickr.utils;


import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * created by Aamer on 12/17/2016.
 */

public class SignatureGenerator {

    private static final String TAG = SignatureGenerator.class.getSimpleName();

    public static String getMD5Signature(String secret, TreeMap<String, String> params) {

        StringBuffer buffer = new StringBuffer();
        buffer.append(secret);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            buffer.append(entry.getKey());
            buffer.append(entry.getValue());
        }

        Log.e(TAG, buffer.toString());

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            return ByteUtil.toHexString(md5.digest(buffer.toString().getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage() + ", " + Arrays.toString(e.getStackTrace()));
            return null;
        }
    }


}

package com.pixerf.flickr.utils.imageutils;

import android.content.Context;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * created by Aamer on 12/16/2016.
 */

class FileCache {

    private final static String TAG = FileCache.class.getSimpleName();

    private File cacheDir;

    FileCache(Context context) {
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "flickr_images_cache");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    File getFile(String url) throws UnsupportedEncodingException {
        String filename = URLEncoder.encode(url, "UTF-8");
        return new File(cacheDir, filename);
    }

    void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files) {
            f.delete();
        }
    }
}

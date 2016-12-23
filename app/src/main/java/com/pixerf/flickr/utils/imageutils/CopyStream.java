package com.pixerf.flickr.utils.imageutils;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * created by Aamer on 12/16/2016.
 */

class CopyStream {

    private static final String TAG = CopyStream.class.getSimpleName();

    public static void copy(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] buffer = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(buffer, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(buffer, 0, count);
            }
        } catch (Exception ex) {
            ex.getStackTrace();
            //Log.e(TAG, ex.getMessage());
        }
    }
}

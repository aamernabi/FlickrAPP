package com.pixerf.flickr.utils;

/**
 * created by Aamer on 12/17/2016.
 */

public class ByteUtil {

    static char[] hexChar = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'};

    /**
     * Convert a byte array to a hex string.
     *
     * @param b The byte array
     * @return The hex String
     */
    public static String toHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            // look up high nibble char
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);

            // look up low nibble char
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

}

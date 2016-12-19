package com.pixerf.flickr.utils;

/**
 * created by Aamer on 12/17/2016.
 */

public class UrlUtils {

    // urls
    public static final String LOGIN_URL = "https://www.flickr.com/auth-72157676032989762";
    public static final String END_POINT = "https://api.flickr.com/services/rest/";
    public static final String END_POINT_UPLOAD = "https://up.flickr.com/services/upload/";
    // api key and shared secret
    public static final String API_KEY = "d5d14485477f2d95048c5f57e9a77581";
    public static final String SECRET = "7e9eb8a356bdc19c";
    // methods
    public static final String METHOD_CHECK_TOKEN = "flickr.auth.checkToken";
    public static final String METHOD_GET_FROB = "flickr.auth.getFrob";
    public static final String METHOD_GET_TOKEN = "flickr.auth.getToken";
    public static final String METHOD_GET_FULL_TOKEN = "flickr.auth.getFullToken";
    public static final String METHOD_SEARCH = "flickr.photos.search";
    public static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    public static final String METHOD_INTERESTINGNESS = "flickr.interestingness.getList";
    public static final String LOGOUT_URL = "https://www.flickr.com/logout.gne?magic_cookie=21985278499a39223415fd413cd46c1ca0521ff57e4538f96c362fdce55ee069";
    private static final String TAG = UrlUtils.class.getSimpleName();

}

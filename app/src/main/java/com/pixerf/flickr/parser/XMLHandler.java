package com.pixerf.flickr.parser;

import android.util.Log;

import com.pixerf.flickr.model.AuthResponse;
import com.pixerf.flickr.model.Error;
import com.pixerf.flickr.model.User;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * created by Aamer on 12/17/2016.
 */

public class XMLHandler {

    private static final String TAG = XMLHandler.class.getSimpleName();
    private String text;

    public AuthResponse parse(String response) {

        AuthResponse authResponse = null;
        User user;
        Error error;

        try {

            InputStream inputStream = new ByteArrayInputStream(response.getBytes("UTF-8"));

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                String tagName = parser.getName();

                switch (eventType) {

                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("rsp")) {

                            authResponse = new AuthResponse();
                            authResponse.setStat(parser.getAttributeValue(null, "stat"));

                        } else if (tagName.equalsIgnoreCase("user")) {

                            user = new User();
                            user.setUserId(parser.getAttributeValue(null, "nsid"));
                            user.setUsername(parser.getAttributeValue(null, "username"));
                            user.setFullName(parser.getAttributeValue(null, "fullname"));

                            if (authResponse != null)
                                authResponse.setUser(user);
                        } else if (tagName.equalsIgnoreCase("err")) {
                            error = new Error();
                            error.setCode(Integer.parseInt(parser.getAttributeValue(null, "code")));
                            error.setMessage(parser.getAttributeValue(null, "msg"));

                            if (authResponse != null)
                                authResponse.setError(error);
                        }

                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase("token")) {
                            if (authResponse != null)
                                authResponse.setToken(text.trim());
                        } else if (tagName.equalsIgnoreCase("perms")) {
                            if (authResponse != null)
                                authResponse.setPermission(text.trim());
                        }
                        break;

                    default:
                        break;
                }

                eventType = parser.next();
            }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        return authResponse;
    }
}

package com.pixerf.flickr.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.pixerf.flickr.R;
import com.pixerf.flickr.view.fragment.FragmentPhoto;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = new FragmentPhoto();
            fragmentManager.beginTransaction().add(R.id.container, fragment).commit();
        }
    }
}

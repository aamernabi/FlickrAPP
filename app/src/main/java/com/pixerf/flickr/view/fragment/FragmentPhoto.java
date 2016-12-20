package com.pixerf.flickr.view.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pixerf.flickr.R;
import com.pixerf.flickr.model.Photo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;


public class FragmentPhoto extends Fragment {

    public static final String TAG = FragmentPhoto.class.getSimpleName();
    private ImageView imageViewPhoto;
    private ProgressBar progressBar;
    private LruCache<String, Bitmap> imageCache;
    private volatile boolean running = true;

    public FragmentPhoto() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        imageViewPhoto = (ImageView) view.findViewById(R.id.imageViewPhoto);
        TextView textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        setUpImageCache();
        Photo photo = getActivity().getIntent().getParcelableExtra("photo");
        load(photo);
        textViewTitle.setText(photo.getTitle());
        return view;
    }

    private void setUpImageCache() {
        // calculating cache size..
        final int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);
    }

    // Lazy-Loading image
    private void load(Photo photo) {
        Bitmap bitmap = imageCache.get(photo.getUrl());
        if (bitmap != null) {
            imageViewPhoto.setImageBitmap(bitmap);
        } else {
            new ImageLoader(photo.getUrl()).execute();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        running = false;
    }

    class ImageLoader extends AsyncTask<String, Void, Bitmap> {

        private final String url;

        ImageLoader(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            if (running) {
                try {
                    InputStream is = (InputStream) new URL(this.url).getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    return bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                } catch (OutOfMemoryError e) {
                    imageCache.evictAll();
                    Log.e(TAG, e.getMessage() + ", " + Arrays.toString(e.getStackTrace()));
                }
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            running = false;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressBar.setVisibility(View.GONE);
            if (bitmap != null) {
                imageViewPhoto.setImageBitmap(bitmap);
                imageCache.put(url, bitmap);
            }
        }
    }
}

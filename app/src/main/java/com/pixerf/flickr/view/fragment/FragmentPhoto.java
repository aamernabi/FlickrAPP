package com.pixerf.flickr.view.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pixerf.flickr.R;
import com.pixerf.flickr.model.Photo;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class FragmentPhoto extends Fragment {

    public static final String TAG = FragmentPhoto.class.getSimpleName();
    private ImageView imageViewPhoto;
    private TextView textViewTitle;
    private ProgressBar progressBar;

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
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        Photo photo = getActivity().getIntent().getParcelableExtra("photo");
        Picasso.with(getActivity()).load(photo.getUrl()).into(imageViewPhoto);
        textViewTitle.setText(photo.getTitle());
        return view;
    }

    class ImageLoader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            try {
                InputStream is = (InputStream) new URL(url[0]).getContent();
                // TODO: 12/18/2016 Out of memory exception.. may occur
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressBar.setVisibility(View.GONE);
            if (bitmap != null) {
                imageViewPhoto.setImageBitmap(bitmap);
            }
        }
    }
}

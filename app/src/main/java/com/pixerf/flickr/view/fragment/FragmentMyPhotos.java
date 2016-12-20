package com.pixerf.flickr.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pixerf.flickr.R;
import com.pixerf.flickr.model.Photo;
import com.pixerf.flickr.model.Photos;
import com.pixerf.flickr.photos.FlickrPhotos;
import com.pixerf.flickr.utils.UrlUtils;
import com.pixerf.flickr.view.adapter.PhotoAdapter;

import java.util.LinkedHashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentMyPhotos extends Fragment {

    private static final String TAG = FragmentMyPhotos.class.getSimpleName();
    private static final int NO_OF_COLS = 2;
    private static final int ITEM_PER_PAGE = 100;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private OnFragmentInteractionListener mListener;

    public FragmentMyPhotos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_photos, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(NO_OF_COLS, StaggeredGridLayoutManager.VERTICAL));

        getUserPhotos();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void getUserPhotos() {
        //int totalItems = recyclerView.getLayoutManager().getItemCount();
        //int page = totalItems/ITEM_PER_PAGE + 1;

        SharedPreferences preferences = getActivity().getApplicationContext()
                .getSharedPreferences(getResources().getString(R.string.login_info_preferences), MODE_PRIVATE);

        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put(getResources().getString(R.string.method), UrlUtils.METHOD_SEARCH);
        params.put(getResources().getString(R.string.api_key), UrlUtils.API_KEY);
        // TODO: 12/18/2016 Rename user_idd with user id
        params.put(getResources().getString(R.string.user_idd),
                preferences.getString(getResources().getString(R.string.user_id), null));
        params.put("format", "json");
        params.put("nojsoncallback", "1");
        params.put(getResources().getString(R.string.auth_token),
                preferences.getString(getResources().getString(R.string.full_token), null));

        new PhotosTask(params).execute();
    }

    private void setRecyclerView(List<Photo> photoList) {
        if (photoList != null) {
            for (Photo photo : photoList) {
                Log.e(TAG, "id :" + photo.getId());
                Log.e(TAG, "owner :" + photo.getOwner());
                Log.e(TAG, "secret :" + photo.getSecret());
                Log.e(TAG, "server :" + photo.getServer());
                Log.e(TAG, "title :" + photo.getTitle());
                Log.e(TAG, "--------------------------");
            }

            PhotoAdapter adapter = new PhotoAdapter(getActivity(), photoList);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(),
                    "Something went wrong. Make sure you have an active internet connection",
                    Toast.LENGTH_LONG).show();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class PhotosTask extends AsyncTask<Void, Void, Photos> {
        private LinkedHashMap<String, String> params;

        PhotosTask(LinkedHashMap<String, String> params) {
            this.params = params;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Photos doInBackground(Void... voids) {
            return new FlickrPhotos().getUserPhotos(params);
        }

        @Override
        protected void onPostExecute(Photos photos) {
            progressBar.setVisibility(View.GONE);
            if (photos != null) {
                if (photos.getStat().equalsIgnoreCase("ok")) {
                    setRecyclerView(photos.getPhotoList());
                } else if (photos.getStat().equalsIgnoreCase("fail")) {
                    Toast.makeText(getActivity(),
                            "Code: " + photos.getError().getCode()
                                    + "nMesssage:" + photos.getError().getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(),
                        "Network connection error. Make sure you have an active internet connection",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}

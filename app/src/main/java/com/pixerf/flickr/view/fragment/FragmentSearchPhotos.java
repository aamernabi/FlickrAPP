package com.pixerf.flickr.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pixerf.flickr.R;
import com.pixerf.flickr.model.Photo;
import com.pixerf.flickr.model.Photos;
import com.pixerf.flickr.photos.FlickrPhotos;
import com.pixerf.flickr.view.adapter.PhotoAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSearchPhotos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSearchPhotos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSearchPhotos extends Fragment {

    private static final String TAG = FragmentMyPhotos.class.getSimpleName();
    private static final int NO_OF_COLS = 2;
    private static final int ITEM_PER_PAGE = 100;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private PhotoAdapter adapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentSearchPhotos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSearchPhotos.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSearchPhotos newInstance(String param1, String param2) {
        FragmentSearchPhotos fragment = new FragmentSearchPhotos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_photos, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(NO_OF_COLS, StaggeredGridLayoutManager.VERTICAL));

        String searchQuery = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(getString(R.string.search_query_preferences), null);

        searchPhotos(searchQuery);
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

    private void searchPhotos(String searchQuery) {
        int totalItems = recyclerView.getLayoutManager().getItemCount();
        int page = totalItems / ITEM_PER_PAGE + 1;


        new PhotosTask(searchQuery, page).execute();
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

            adapter = new PhotoAdapter(getActivity(), photoList);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(),
                    "Something went wrong. Make sure you have an active internet connection",
                    Toast.LENGTH_LONG).show();
        }
    }

    // menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard, menu);

        MenuItem itemSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) itemSearch.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchQuery) {
                if (!(searchQuery.trim().isEmpty()))
                    refresh(searchQuery);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean isItemSelected;

        switch (item.getItemId()) {
            case R.id.action_search:
                getActivity().onSearchRequested();
                isItemSelected = true;
                break;
            default:
                isItemSelected = super.onOptionsItemSelected(item);
                break;
        }

        return isItemSelected;
    }

    public void refresh(String searchQuery) {
        if (adapter != null)
            adapter.clear();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                .putString(getResources().getString(R.string.search_query_preferences), searchQuery)
                .apply();
        searchPhotos(searchQuery);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class PhotosTask extends AsyncTask<Void, Void, Photos> {
        private ProgressDialog dialog;
        private String searchQuery;
        private int page;

        PhotosTask(String searchQuery, int page) {
            this.searchQuery = searchQuery;
            this.page = page;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Fetching your photos");
            dialog.show();
        }

        @Override
        protected Photos doInBackground(Void... voids) {
            return new FlickrPhotos().searchPhotos(searchQuery, page);
        }

        @Override
        protected void onPostExecute(Photos photos) {
            dialog.dismiss();
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

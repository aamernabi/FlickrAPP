package com.pixerf.flickr.view.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pixerf.flickr.R;
import com.pixerf.flickr.model.AuthResponse;
import com.pixerf.flickr.utils.LoggingUtils;
import com.pixerf.flickr.view.fragment.FragmentMyPhotos;
import com.pixerf.flickr.view.fragment.FragmentSearchPhotos;

import java.util.Arrays;

public class Dashboard extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        FragmentMyPhotos.OnFragmentInteractionListener, FragmentSearchPhotos.OnFragmentInteractionListener {

    private static final String TAG = Dashboard.class.getSimpleName();
    private static final String TAG_HOME = "home";
    private static final String TAG_MY_PHOTOS = "myPhotos";
    private static final int IMG_RESULT = 1;
    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_HOME;
    TextView textViewUserName, textViewFullName;
    FloatingActionButton fab;
    private Toolbar toolbar;
    private String[] toolbarTitles;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        saveUserInfo();
        toolbarTitles = getResources().getStringArray(R.array.nav_item_toolbar_titles);
        setToolbarTitle();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoto();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (new LoggingUtils(getApplicationContext()).isLoggedIn()) {
            MenuItem item = navigationView.getMenu().findItem(R.id.nav_sign_out);
            item.setTitle(getString(R.string.sign_out));
        }

        mHandler = new Handler();
        View navHeader = navigationView.getHeaderView(0);
        textViewUserName = (TextView) navHeader.findViewById(R.id.textViewUserName);
        textViewFullName = (TextView) navHeader.findViewById(R.id.textViewFullName);

        loadNavHeader();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadCurrentFragment();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_public_photos) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadCurrentFragment();
        } else if (id == R.id.nav_my_photos) {
            navItemIndex = 1;
            CURRENT_TAG = TAG_MY_PHOTOS;
            loadCurrentFragment();
        } else if (id == R.id.nav_sign_out) {
            logout();
        } else {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadCurrentFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadNavHeader() {
        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences(getString(R.string.login_info_preferences), MODE_PRIVATE);
        if (preferences.getBoolean(getString(R.string.logged_in), false)) {
            textViewUserName.setText(preferences.getString(getString(R.string.user_name), ""));
            textViewFullName.setText(preferences.getString(getString(R.string.full_name), ""));
        } else {
            textViewUserName.setText("");
            textViewFullName.setText("");
        }
    }

    private void loadCurrentFragment() {
        setToolbarTitle();

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in Gmail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getCurrentFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frameLayout, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        mHandler.post(mPendingRunnable);
    }

    private Fragment getCurrentFragment() {
        switch (navItemIndex) {
            case 0:
                fab.setVisibility(View.GONE);
                return new FragmentSearchPhotos();
            case 1:
                fab.setVisibility(View.VISIBLE);
                return new FragmentMyPhotos();
            default:
                return new FragmentMyPhotos();
        }
    }

    private void setToolbarTitle() {
        toolbar.setTitle(toolbarTitles[navItemIndex]);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // TODO: 12/18/2016
    }

    private void saveUserInfo() {
        AuthResponse response = getIntent().getParcelableExtra("response");
        if (response != null)
            new LoggingUtils(getApplicationContext()).saveResponseToPreferences(response);

        new LoggingUtils(getApplicationContext()).displayPreferencesInLog();
    }

    private void logout() {
        new LoggingUtils(getApplicationContext()).clearPreferences();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMG_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
                showDialog(data);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + ", " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void showDialog(final Intent data) {
        final Dialog dialog = new Dialog(Dashboard.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_upload_photo);
        final ImageView imageView = (ImageView) dialog.findViewById(R.id.imageView);
        Button buttonUpload = (Button) dialog.findViewById(R.id.buttonUpload);
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);

        dialog.show();

        //load image from uri
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... strings) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    return picturePath;
                }

                return null;
            }

            @Override
            protected void onPostExecute(String path) {
                if (path != null)
                    imageView.setImageBitmap(BitmapFactory.decodeFile(path));
            }
        }.execute();

        buttonUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
//                final RadioButton radioButton;
//                int selectedId = radioGroup.getCheckedRadioButtonId();
//                radioButton = (RadioButton) findViewById(selectedId);
                // TODO: 12/19/2016 error
//                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
//                ByteArrayOutputStream stream=new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
//                byte[] data = stream.toByteArray();
//                new UploadPhoto(getApplicationContext(), data,"image.jpg").execute();
            }
        });
    }
}

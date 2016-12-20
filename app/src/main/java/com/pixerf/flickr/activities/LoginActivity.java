package com.pixerf.flickr.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pixerf.flickr.R;
import com.pixerf.flickr.auth.FlickrAuth;
import com.pixerf.flickr.model.AuthResponse;
import com.pixerf.flickr.utils.UrlUtils;

import java.util.LinkedHashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    LinearLayout linearLayout;
    EditText editTextPin;
    Button buttonLogin;
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Make intro Activity
        SharedPreferences preferences = getSharedPreferences(getString(R.string.login_info_preferences), MODE_PRIVATE);
        if (preferences.getBoolean(getResources().getString(R.string.logged_in), false)) {
            Intent intent = new Intent(LoginActivity.this, Dashboard.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_login);

            linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
            editTextPin = (EditText) findViewById(R.id.editTextPin);
            webView = (WebView) findViewById(R.id.webView);
            linearLayout.setVisibility(View.GONE);

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    if (url.trim().startsWith("https://m.flickr.com/#/#")) {
                        linearLayout.setVisibility(View.VISIBLE);
                    }

                    Log.e(TAG, url);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.clearCache(true);
            webView.clearSslPreferences();
            webView.loadUrl(UrlUtils.LOGIN_URL);

            buttonLogin = (Button) findViewById(R.id.buttonLogin);
            buttonLogin.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogin) {

            if (editTextPin.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "You need to enter PIN", Toast.LENGTH_LONG).show();
            } else {
                LinkedHashMap<String, String> params = new LinkedHashMap<>();
                params.put(getResources().getString(R.string.method), UrlUtils.METHOD_GET_FULL_TOKEN);
                params.put(getResources().getString(R.string.api_key), UrlUtils.API_KEY);
                params.put(getResources().getString(R.string.mini_token), editTextPin.getText().toString());

                new LoginTask(params).execute();
            }
        }
    }

    private class LoginTask extends AsyncTask<String, Void, AuthResponse> {

        private LinkedHashMap<String, String> params;
        private ProgressDialog dialog;

        LoginTask(LinkedHashMap<String, String> params) {
            this.params = params;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Authenticating..");
            dialog.show();
        }

        @Override
        protected AuthResponse doInBackground(String... strings) {
            FlickrAuth auth = new FlickrAuth();
            return auth.getFullToken(params);
        }

        @Override
        protected void onPostExecute(AuthResponse authResponse) {
            dialog.dismiss();
            if (authResponse != null) {
                if (authResponse.getStat().equalsIgnoreCase("ok")) {
                    Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                    intent.putExtra("response", authResponse);
                    startActivity(intent);
                    finish();
                } else if (authResponse.getStat().equalsIgnoreCase("fail")) {
                    // TODO: make proper error displaying
                    Toast.makeText(getApplicationContext(),
                            "Code: " + authResponse.getError().getCode() + "\nMessage: " + authResponse.getError().getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Network connection error.\nMake sure you have an active internet connection",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}

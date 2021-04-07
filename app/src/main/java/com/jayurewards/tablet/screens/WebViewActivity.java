package com.jayurewards.tablet.screens;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jayurewards.tablet.BuildConfig;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.GlobalConstants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";

    private ConstraintLayout loadingSpinner;
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        String endingUrl = intent.getStringExtra(GlobalConstants.INTENT_ENDING_URL);

        Toolbar toolbar = findViewById(R.id.toolbarWebView);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Jayu");
        }

        if (endingUrl != null && !endingUrl.isEmpty()) {
            loadingSpinner = findViewById(R.id.loadingWebView);
            loadingSpinner.setVisibility(View.VISIBLE);

//            if (!endingUrl.equals(GlobalConstantsMerchant.PORTAL_URL_TEAM_HELP) && !endingUrl.equals(GlobalConstantsMerchant.PORTAL_URL_REWARDS_HELP)) {
//                toolbar.setVisibility(View.GONE);
//            }

            String url = BuildConfig.merchantPortalurl + endingUrl;

            webView = findViewById(R.id.webView);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    loadingSpinner.setVisibility(View.GONE);
                }
            });

            WebSettings webSettings = webView.getSettings();
            webView.getSettings().setSupportZoom(true);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAllowFileAccess(true);


            // Enable HTML5 Features
            try {
                Method m1 = WebSettings.class.getMethod("setDomStorageEnabled", Boolean.TYPE);
                m1.invoke(webSettings, Boolean.TRUE);

                Method m2 = WebSettings.class.getMethod("setDatabaseEnabled", Boolean.TYPE);
                m2.invoke(webSettings, Boolean.TRUE);

                Method m3 = WebSettings.class.getMethod("setDatabasePath", String.class);
                m3.invoke(webSettings, getFilesDir().getPath() + getPackageName() + "/databases/");

                Method m4 = WebSettings.class.getMethod("setAppCacheMaxSize", Long.TYPE);
                m4.invoke(webSettings, 1024*1024*8);

                Method m5 = WebSettings.class.getMethod("setAppCachePath", String.class);
                m5.invoke(webSettings, getFilesDir().getPath() + getPackageName() + "/cache/");

                Method m6 = WebSettings.class.getMethod("setAppCacheEnabled", Boolean.TYPE);
                m6.invoke(webSettings, Boolean.TRUE);

            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                Log.e(TAG, "Reflection fail", e);
            }

            webView.loadUrl(url);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
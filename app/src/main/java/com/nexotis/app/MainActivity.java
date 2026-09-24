package com.nexotis.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private String targetUrl = "https://google.com"; // fallback default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setDatabaseEnabled(true);
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        ws.setAllowFileAccess(true);
        ws.setAllowContentAccess(true);
        ws.setMediaPlaybackRequiresUserGesture(false);
        ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Buka aplikasi eksternal
                if (isExternalApp(url)) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        return true;
                    } catch (Exception e) {
                        view.loadUrl(url);
                        return true;
                    }
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    loadOfflinePage();
                }
            }
        });

        // Baca URL dari file INDAH
        targetUrl = readUrlFromAssets();

        // Cek koneksi
        if (isOnline()) {
            webView.loadUrl(targetUrl);
        } else {
            loadOfflinePage();
        }

        setContentView(webView);
    }

    // ================== BACA FILE INDAH ==================
    private String readUrlFromAssets() {
        try {
            InputStream is = getAssets().open("INDAH");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line.trim());
            }
            reader.close();

            // Decode Base64
            byte[] decoded = Base64.decode(sb.toString(), Base64.DEFAULT);
            return new String(decoded, "UTF-8").trim();
        } catch (Exception e) {
            return "https://google.com"; // fallback
        }
    }

    // ================== CEK ONLINE ==================
    private boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo net = cm.getActiveNetworkInfo();
            return net != null && net.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    // ================== HALAMAN OFFLINE ==================
    private void loadOfflinePage() {
        webView.loadUrl("file:///android_asset/offline.html");
    }

    // ================== DETEKSI LINK APP ==================
    private boolean isExternalApp(String url) {
        return url.startsWith("whatsapp://") ||
               url.startsWith("tg://") ||
               url.startsWith("telegram://") ||
               url.startsWith("instagram://") ||
               url.startsWith("tiktok://") ||
               url.startsWith("youtube://") ||
               url.startsWith("fb://") ||
               url.startsWith("twitter://") ||
               url.startsWith("intent://") ||
               url.contains("wa.me") ||
               url.contains("t.me") ||
               url.contains("instagram.com") ||
               url.contains("tiktok.com") ||
               url.contains("youtu.be") ||
               url.contains("youtube.com") ||
               url.contains("facebook.com") ||
               url.contains("twitter.com") ||
               url.contains("x.com");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    }
    

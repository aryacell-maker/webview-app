package com.example.webviewapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        webView = new WebView(this);
        
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Buka di aplikasi eksternal
                if (url.startsWith("whatsapp://") || 
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
                    url.contains("x.com")) {
                    
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        // Kalau aplikasi tidak terinstall, buka di browser
                        view.loadUrl(url);
                        return true;
                    }
                }
                
                // Link biasa tetap di WebView
                view.loadUrl(url);
                return true;
            }
        });
        
        webView.loadUrl("file:///android_asset/html/index.html");
        setContentView(webView);
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

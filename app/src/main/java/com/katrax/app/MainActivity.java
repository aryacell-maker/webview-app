package com.katrax.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    // File yang berisi URL target
    private static final String CONFIG_URL =
            "https://aryacell-maker.github.io/web-config/url.txt";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean mainFrameError = false;

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
        ws.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return handleUrl(request.getUrl().toString());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return handleUrl(url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mainFrameError = false;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    mainFrameError = true;
                    loadOfflinePage();
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
        setContentView(webView);

        // Ambil URL dari url.txt lalu buka
        fetchTargetAndLoad();
    }

    // ================== BACA url.txt ==================
    private void fetchTargetAndLoad() {
        executor.execute(() -> {
            String target = null;

            try {
                URL url = new URL(CONFIG_URL + "?t=" + System.currentTimeMillis());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(12000);
                conn.setReadTimeout(12000);
                conn.setRequestMethod("GET");
                conn.setInstanceFollowRedirects(true);

                int code = conn.getResponseCode();
                if (code >= 200 && code < 300) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line.trim());
                    }
                    reader.close();
                    target = sb.toString().trim();
                }
                conn.disconnect();
            } catch (Exception e) {
                target = null;
            }

            final String finalTarget = target;

            handler.post(() -> {
                if (finalTarget != null &&
                        (finalTarget.startsWith("http://") || finalTarget.startsWith("https://"))) {
                    webView.loadUrl(finalTarget);
                } else {
                    loadOfflinePage();
                }
            });
        });
    }

    // ================== OFFLINE ==================
    private void loadOfflinePage() {
        webView.loadUrl("file:///android_asset/offline.html");
    }

    // ================== LINK APP LUAR ==================
    private boolean handleUrl(String url) {
        if (url == null) return false;

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
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
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

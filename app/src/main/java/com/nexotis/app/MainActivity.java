package com.nexotis.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    // Key & IV harus sama dengan yang dipakai saat encrypt (16 karakter)
    private static final String SECRET_KEY = "NexotisKey123456";
    private static final String IV = "NexotisIV1234567";

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

        // Bridge agar HTML bisa panggil Java (untuk pindah halaman)
        webView.addJavascriptInterface(new WebAppInterface(), "Android");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("whatsapp://") || url.startsWith("tg://") ||
                    url.startsWith("telegram://") || url.startsWith("instagram://") ||
                    url.startsWith("tiktok://") || url.startsWith("youtube://") ||
                    url.startsWith("fb://") || url.startsWith("twitter://") ||
                    url.startsWith("intent://") ||
                    url.contains("wa.me") || url.contains("t.me") ||
                    url.contains("instagram.com") || url.contains("tiktok.com") ||
                    url.contains("youtu.be") || url.contains("youtube.com") ||
                    url.contains("facebook.com") || url.contains("twitter.com") ||
                    url.contains("x.com")) {
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
        });

        // Load halaman utama (index.ox)
        loadEncryptedPage("index");

        setContentView(webView);
    }

    // ================== DECRYPT & LOAD ==================
    // ================== DECRYPT & LOAD ==================
private void loadEncryptedPage(String pageName) {
    try {
        String fileName = "html/" + pageName + ".ox";
        InputStream is = getAssets().open(fileName);

        // Baca sebagai teks (Base64)
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int n;
        while ((n = is.read(data)) != -1) {
            buffer.write(data, 0, n);
        }
        is.close();

        String b64 = new String(buffer.toByteArray(), "UTF-8").trim();
        byte[] encrypted = android.util.Base64.decode(b64, android.util.Base64.DEFAULT);

        String html = decrypt(encrypted);

        webView.loadDataWithBaseURL(
            "file:///android_asset/html/",
            html,
            "text/html",
            "UTF-8",
            null
        );
    } catch (Exception e) {
        webView.loadData(
            "<h2 style='color:red;padding:20px'>Error load page: " + pageName + "</h2>" +
            "<p>" + e.getMessage() + "</p>",
            "text/html", "UTF-8"
        );
    }
}

private String decrypt(byte[] encrypted) throws Exception {
    SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8"));

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

    byte[] decrypted = cipher.doFinal(encrypted);
    return new String(decrypted, "UTF-8");
}

    // ================== BRIDGE DARI HTML ==================
    public class WebAppInterface {
        @JavascriptInterface
        public void openPage(String pageName) {
            // Dipanggil dari HTML: Android.openPage('login')
            runOnUiThread(() -> loadEncryptedPage(pageName));
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
}

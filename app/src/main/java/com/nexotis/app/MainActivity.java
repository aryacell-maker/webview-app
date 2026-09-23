package com.nexotis.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    // ================== PENGATURAN ENKRIPSI ==================
    // Ganti key ini (harus 16 karakter)
    private static final String SECRET_KEY = "NexotisKey123456"; // 16 karakter
    private static final String IV = "NexotisIV1234567";         // 16 karakter

    // Hasil enkripsi HTML nanti ditaruh di sini
    private static final String ENCRYPTED_HTML = "HASIL_ENKRIPSI_NANTI_DISINI";

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
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

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
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
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

        // Decrypt HTML lalu tampilkan
        try {
            String html = decrypt(ENCRYPTED_HTML);
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        } catch (Exception e) {
            webView.loadData("<h1>Error decrypt</h1><p>" + e.getMessage() + "</p>", "text/html", "UTF-8");
        }

        setContentView(webView);
    }

    // ================== FUNGSI DECRYPT ==================
    private String decrypt(String encrypted) throws Exception {
        byte[] keyBytes = SECRET_KEY.getBytes("UTF-8");
        byte[] ivBytes = IV.getBytes("UTF-8");

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decoded = Base64.decode(encrypted, Base64.DEFAULT);
        byte[] decrypted = cipher.doFinal(decoded);

        return new String(decrypted, "UTF-8");
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

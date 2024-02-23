package com.example.todoapp;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        // Récupérer le lien en tant qu'extra de l'intent
        String link = getIntent().getStringExtra("lien");

        webView = findViewById(R.id.webview);

        // Activer JavaScript (si nécessaire)
        webView.getSettings().setJavaScriptEnabled(true);

        // Charger le lien dans la WebView
        webView.loadUrl(link);
    }
}

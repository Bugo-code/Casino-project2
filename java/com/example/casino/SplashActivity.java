package com.example.casino;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {
    private FullScreenVideoView splashVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        splashVideo = findViewById(R.id.splashVideo);

        Uri videoUri = Uri.parse(
                "android.resource://" + getPackageName()
                        + "/" + R.raw.splashscreen
        );

        splashVideo.setVideoURI(videoUri);

        splashVideo.setOnPreparedListener(mp ->
                splashVideo.setVideoSize(
                        mp.getVideoWidth(),
                        mp.getVideoHeight()
                )
        );

        splashVideo.setOnCompletionListener(mp ->
                new Handler(android.os.Looper.getMainLooper()).postDelayed(
                        this::openMainActivity,
                        1000
                )
        );

        splashVideo.setOnErrorListener((mp, what, extra) -> {
            openMainActivity();
            return true;
        });

        splashVideo.start();

    }
    private void openMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
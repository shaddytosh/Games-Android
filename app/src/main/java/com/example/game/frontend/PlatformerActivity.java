package com.example.game.frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.game.backend.Platformer.PlatformerManager;
import com.example.game.backend.Platformer.PlatformerView;

import java.io.Serializable;

public class PlatformerActivity extends Activity implements Serializable {

    private PlatformerView platformerView;
    private PlatformerManager platformerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        platformerView = new PlatformerView(this);
        platformerView.setContext(this);
        setContentView(platformerView);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        platformerView.setResuming(true);
        // when quitting the game store the platformerManager in this activity
        platformerView.setPausing(true);
        platformerManager = platformerView.getPlatformerManager();
        System.out.println("Pausing");
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean gameInProgress = getIntent().getBooleanExtra("GAME_IN_PROGRESS_LAUNCH", false);
        if (gameInProgress) {
            platformerView.setResuming(true);
            platformerManager = (PlatformerManager) getIntent().getSerializableExtra("GAME_MANAGER_LAUNCH");
            platformerView.setPreviousGameTime((long) getIntent().getSerializableExtra("IN_GAME_TIME_LAUNCH"));
            platformerManager.context = this;
            platformerManager.generate();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public PlatformerManager getPlatformerManager() {
        return platformerManager;
    }
}


package com.example.game.backend.Platformer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

import com.example.game.frontend.ChooseLevelActivity;
import com.example.game.frontend.PlatformerActivity;
import com.example.game.R;

import com.example.game.backend.PermanentStatistics;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * the main view class of Platformer
 */
public class PlatformerView extends SurfaceView implements Serializable, SurfaceHolder.Callback {
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private PlatformerManager platformerManager;
    private PlatformerThread thread;
    private transient ArrayList<Background> backgrounds;
    private transient Bitmap jumpButton;
    private transient Bitmap pauseButton;
    private transient Bitmap homeButton;
    private transient Bitmap continueButton;
    private transient Paint scorePaint;
    private transient Paint healthPaint;
    private transient Paint timerPaint;
    private boolean isGameOver = false;
    private boolean Resuming = false;
    private boolean Pausing = false;
    private long startGameTime;
    private boolean previousPausingState = false;
    private long totalPausedTime;
    private long pauseTimeStamp;
    private long inGameTime;
    private long previousGameTime;
    private PlatformerActivity platformerActivity;

    private DebouncedOnTouchListener handleTouch = new DebouncedOnTouchListener(200) {
        @Override
        public void onATouch(View v, MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i("TAG", "touched down" + " (" + x + ", " + y + ")");
                    if (detectJump(x, y) && !Pausing && !isGameOver) {
                        for (PlatformerGameObject i : platformerManager.platformerGameObjects) {
                            i.jump();
                        }
                    }
                    if (detectPause(x, y) && !isGameOver) {
                        Pausing = !Pausing;
                    }
                    if (detectExit(x, y)) {
                        Intent intent = new Intent(platformerActivity, ChooseLevelActivity.class);
                        //intent
                        //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK||Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        if (!isGameOver) {
                            intent.putExtra("GAME_MANAGER_SAVE", platformerManager);
                            intent.putExtra("GAME_IN_PROGRESS_SAVE", true);
                            intent.putExtra("IN_GAME_TIME_SAVE", inGameTime);
                            intent.putExtra("USER_NAME", platformerActivity.getIntent().getStringExtra("USER_NAME"));
                        }
                        platformerActivity.startActivity(intent);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i("TAG", "moving: (" + x + ", " + y + ")");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("TAG", "touched up");
                    break;
            }
            if (isGameOver) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Intent intent = new Intent(getContext(), ChooseLevelActivity.class);
                    intent.putExtra("PLATFORMER_IN_GAME_TIME", (int)((previousGameTime + inGameTime) / 1000));
                    intent.putExtra("PLATFORMER_SCORE", platformerManager.getScore());
                    intent.putExtra("PLATFORMER_TOTAL_TIME", (previousGameTime + inGameTime));
                    intent.putExtra("SWITCH_GAME", true);
                    intent.putExtra("USER_NAME", platformerActivity.getIntent().getStringExtra("USER_NAME"));
                    getContext().startActivity(intent);
                }
            }
        }
    };

    public PlatformerView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new PlatformerThread(getHolder(), this);
        backgrounds = new ArrayList<>();
        setFocusable(true);
        setElements();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("Changed");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (Resuming) {
            platformerManager = platformerActivity.getPlatformerManager();
            Pausing = true;
        } else {
            platformerManager = new PlatformerManager(screenHeight, screenWidth, getContext());
        }
        this.setOnTouchListener(handleTouch);

        if (thread.getState() == Thread.State.TERMINATED) {
            thread = new PlatformerThread(getHolder(), this);
        }
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("Destroyed");
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    /*update the platform manager*/
    public void update() {
        if (!Pausing && !isGameOver) {
            platformerManager.update();
            updateBackground();
        }
        if (!previousPausingState && Pausing) {
            pauseTimeStamp = SystemClock.elapsedRealtime() - startGameTime;
            inGameTime = pauseTimeStamp - totalPausedTime;
            previousPausingState = true;
        } else if (previousPausingState && Pausing) {
            inGameTime = pauseTimeStamp - totalPausedTime;
        } else if (previousPausingState) {
            inGameTime = pauseTimeStamp - totalPausedTime;
            previousPausingState = false;
            totalPausedTime += SystemClock.elapsedRealtime() - startGameTime - pauseTimeStamp;
        } else {
            inGameTime = SystemClock.elapsedRealtime() - startGameTime - totalPausedTime;
        }
    }

    /*Draw everything on canvas*/
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            drawBackground(canvas);
            drawElements(canvas);
        }
    }

    /*draw game elements on canvas*/
    private void drawElements(Canvas canvas) {
        canvas.drawBitmap(jumpButton, screenWidth - 80, screenHeight - 200, null);
        if (!Pausing) {
            canvas.drawBitmap(pauseButton, screenWidth - 80, 30, null);
        } else {
            canvas.drawBitmap(continueButton, screenWidth - 80, 30, null);
        }
        canvas.drawBitmap(homeButton, screenWidth - 80, 180, null);
        drawTiles(canvas);
        canvas.drawText("Score: " + platformerManager.getScore(), screenWidth - 100, 100, this.scorePaint);
        canvas.drawText("Health: " + platformerManager.getHealth(), 450, 100, this.healthPaint);
        canvas.drawText("Timer: " + (int)((previousGameTime + inGameTime) / 1000), screenWidth / 2 + 100, 100, this.timerPaint);
        platformerManager.pausing = Pausing;
        platformerManager.draw(canvas);
        if (platformerManager.getHealth() == 0) {
            drawGameOver(canvas);
        }
    }

    /*Draw game over screen on canvas*/
    private void drawGameOver(Canvas canvas) {
        Paint paint1 = new Paint();
        paint1.setColor(Color.rgb(255, 0, 0));
        paint1.setTextSize(100);
        paint1.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Game Over", screenWidth / 2, screenHeight / 2, paint1);
        canvas.drawText("Tap to return to level chooser", screenWidth / 2, screenHeight / 2 + 100, paint1);
        isGameOver = true;
        Pausing = true;
    }

    /*Setup background*/
    private void setBackground() {
        Bitmap background1 = BitmapFactory.decodeResource(getResources(), R.drawable.mountainbg);
        Bitmap background2 = BitmapFactory.decodeResource(getResources(), R.drawable.mountainfar);
        Bitmap background3 = BitmapFactory.decodeResource(getResources(), R.drawable.mountains);
        Bitmap background4 = BitmapFactory.decodeResource(getResources(), R.drawable.mountaintrees);
        Bitmap background5 = BitmapFactory.decodeResource(getResources(), R.drawable.mountainforegroundtrees);
        background1 = Bitmap.createScaledBitmap(background1, screenWidth, screenHeight, true);
        background2 = Bitmap.createScaledBitmap(background2, screenWidth, screenHeight, true);
        background3 = Bitmap.createScaledBitmap(background3, screenWidth, screenHeight, true);
        background4 = Bitmap.createScaledBitmap(background4, screenWidth, screenHeight, true);
        background5 = Bitmap.createScaledBitmap(background5, screenWidth, screenHeight, true);
        backgrounds.add(new Background(background1, 0));
        backgrounds.add(new Background(background2, 100));
        backgrounds.add(new Background(background3, 140));
        backgrounds.add(new Background(background4, 200));
        backgrounds.add(new Background(background5, 300));
    }

    /*Move all parallax backgrounds*/
    private void updateBackground() {
        // Update all the background positions except the moon
        int i = 1;
        for (; i < backgrounds.size(); i++) {
            backgrounds.get(i).update(thread.getFps());
        }
    }

    /*Draw all backgrounds onto canvas*/
    private void drawBackground(Canvas canvas) {
        for (Background bg : backgrounds) {
            bg.draw(canvas);
        }
    }

    private boolean detectJump(int x, int y) {
        return screenWidth - 80 <= x && x <= screenWidth - 80 + jumpButton.getWidth()
                && screenHeight - 200 <= y && y <= screenHeight - 200 + jumpButton.getHeight();
    }

    private boolean detectPause(int x, int y) {
        return screenWidth - 80 <= x && x <= screenWidth - 80 + pauseButton.getWidth()
                && 30 <= y && y <= 30 + pauseButton.getHeight();
    }

    private boolean detectExit(int x, int y) {
        return screenWidth - 80 <= x && x <= screenWidth - 80 + homeButton.getWidth()
                && 180 <= y && y <= 180 + homeButton.getHeight();
    }

    private void drawTiles(Canvas canvas){
        int n = 0;
        while (n<80){
            canvas.drawBitmap(PlatformerManager.tileBitmap,n*32, screenHeight - 50, null );
            n++;}

    }

    /*Setup all elements*/
    private void setElements() {
        startGameTime = SystemClock.elapsedRealtime();
        setButtons();
        setBackground();
        setHealthPaint();
        setScorePaint();
        setTimerPaint();
    }

    /*Setup buttons*/
    private void setButtons() {
        this.pauseButton = BitmapFactory.decodeResource(getResources(), R.drawable.white_pause);
        this.jumpButton = BitmapFactory.decodeResource(getResources(), R.drawable.jump);
        this.homeButton = BitmapFactory.decodeResource(getResources(), R.drawable.white_home);
        this.continueButton = BitmapFactory.decodeResource(getResources(), R.drawable.white_play);
    }

    /*Setup score paint*/
    private void setScorePaint() {
        this.scorePaint = new Paint();
        this.scorePaint.setColor(Color.rgb(100, 255, 200));
        this.scorePaint.setTextSize(100);
        this.scorePaint.setTextAlign(Paint.Align.RIGHT);
    }

    /*Setup health point paint*/
    private void setHealthPaint() {
        this.healthPaint = new Paint();
        this.healthPaint.setColor(Color.rgb(255, 0, 0));
        this.healthPaint.setTextSize(100);
        this.healthPaint.setTextAlign(Paint.Align.RIGHT);
    }

    /*Setup timer paint*/
    private void setTimerPaint() {
        this.timerPaint = new Paint();
        this.timerPaint.setColor(Color.rgb(0, 0, 0));
        this.timerPaint.setTextSize(100);
        this.timerPaint.setTextAlign(Paint.Align.RIGHT);
    }

    public void setContext(PlatformerActivity platformerActivity) {
        this.platformerActivity = platformerActivity;
    }

    public PlatformerManager getPlatformerManager() {
        return platformerManager;
    }

    public void setPreviousGameTime(long previousGameTime) {
        this.previousGameTime = previousGameTime;
    }

    public void setResuming(boolean resuming) {
        Resuming = resuming;
    }

    public void setPausing(boolean pausing) {
        Pausing = pausing;
    }
}
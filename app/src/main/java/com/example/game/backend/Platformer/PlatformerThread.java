package com.example.game.backend.Platformer;

import android.view.SurfaceHolder;
import android.graphics.Canvas;

public class PlatformerThread extends Thread {
    private final SurfaceHolder surfaceHolder;
    private com.example.game.backend.Platformer.PlatformerView platformerView;
    private boolean running;
    private static Canvas canvas;
    private long fps = 60;

    //https://stackoverflow.com/questions/8586888/how-to-pause-and-resume-a-thread-in-my-app
    //https://stackoverflow.com/questions/6776327/how-to-pause-resume-thread-in-android
    PlatformerThread(SurfaceHolder surfaceHolder,
                     com.example.game.backend.Platformer.PlatformerView platformerView) {
        this.surfaceHolder = surfaceHolder;
        this.platformerView = platformerView;
    }

    @Override
    public void run() {
        while (running) {
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    long startFrameTime = System.currentTimeMillis();
                    this.platformerView.update();
                    this.platformerView.draw(canvas);
                    long timeThisFrame = System.currentTimeMillis() - startFrameTime;
                    if (timeThisFrame >= 1) {
                        fps = 1000 / timeThisFrame;//get current fps
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void setRunning(boolean isRunning) {
        this.running = isRunning;
    }

    long getFps() {
        return fps;
    }
}

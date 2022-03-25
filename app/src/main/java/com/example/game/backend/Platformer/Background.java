package com.example.game.backend.Platformer;

//http://gamecodeschool.com/android/coding-a-parallax-scrolling-background-for-android/

import android.graphics.Bitmap;
import android.graphics.Canvas;


class Background {
    private int speed;
    private int cut = 0;
    private Bitmap background;
    private int width;

    Background(Bitmap bitmap, int speed) {
        this.background = bitmap;
        this.speed = speed;
        width = background.getWidth();
    }

    /*Update the background's current position */
    void update(long fps) {
        cut -= speed / fps;
        if (cut <= -width) {
            cut = 0;
        }
    }


    void draw(Canvas canvas) {
        //Draw two background images since we want to make them connected to cover the whole screen
        canvas.drawBitmap(this.background, cut, 0, null);
        canvas.drawBitmap(this.background, width + cut, 0, null);
    }
}

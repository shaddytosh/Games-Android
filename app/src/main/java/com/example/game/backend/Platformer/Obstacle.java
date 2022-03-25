package com.example.game.backend.Platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ListIterator;

class Obstacle extends PlatformerGameObject implements Serializable {
    private transient ArrayList<Bitmap> obstacle;
    private int xVelocity;

    Obstacle(int l, int t, PlatformerManager platformerManager, Context context,int speedCoefficient) {
        super(platformerManager);
        generate(context);
        setHitBox(new HitBox(l, t,l+obstacle.get(0).getWidth(), t+obstacle.get(0).getHeight()));
        setVelocity(speedCoefficient);
    }

    /*Create bitmap*/
    @Override
    public void generate(Context context) {
        obstacle = new ArrayList<>();
        obstacle.add(PlatformerManager.obstacleBitmap);
    }

    /*Set a random velocity for obstacles*/
    private void setVelocity(int speedCoefficient){
        if (speedCoefficient == 0) {
            xVelocity = 20;
        } else {
            xVelocity = 30;
        }
    }

    @Override
    void draw(Canvas canvas) {
        canvas.drawBitmap(obstacle.get(0), getHitBox().getLeft(), getHitBox().getTop(), null);
    }

    /*Move the obstacle to the left*/
    @Override
    void move(ListIterator<PlatformerGameObject> iterator) {
        if (getHitBox().getRight() <= 0) {//if moved outside the left boundary
            iterator.remove();
        }
        updateHitBox("Left", getHitBox().getLeft() - xVelocity);
        updateHitBox("Right", getHitBox().getRight() - xVelocity);
    }


}

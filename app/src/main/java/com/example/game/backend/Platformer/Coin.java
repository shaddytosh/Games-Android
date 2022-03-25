package com.example.game.backend.Platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ListIterator;

class Coin extends PlatformerGameObject implements Serializable {
    private transient ArrayList<Bitmap> coin;
    private int xVelocity;

    Coin(int l, int t, PlatformerManager platformerManager, Context context) {
        super(platformerManager);
        generate(context);
        setHitBox(new HitBox(l, t, l + coin.get(0).getWidth(), t + coin.get(0).getHeight()));
        xVelocity = 20;
    }

    /*Create bitmap*/
    @Override
    public void generate(Context context) {
        coin = new ArrayList<>();
        coin.add(PlatformerManager.coinBitmap);
    }

    @Override
    void draw(Canvas canvas) {
        canvas.drawBitmap(coin.get(0), getHitBox().getLeft(), getHitBox().getTop(), null);
    }

    /*Move the obstacle to the left*/
    @Override
    void move(ListIterator<PlatformerGameObject> iterator) {
        //if moved outside the left boundary clean garbage
        if (getHitBox().getLeft() + coin.get(0).getWidth() <= 0) {
            iterator.remove();
        }
        updateHitBox("Left", getHitBox().getLeft() - xVelocity);
        updateHitBox("Right", getHitBox().getRight() - xVelocity);
    }
}

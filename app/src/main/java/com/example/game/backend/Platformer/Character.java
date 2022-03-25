package com.example.game.backend.Platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import com.example.game.R;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ListIterator;


class Character extends PlatformerGameObject implements Serializable {
    private transient ArrayList<Bitmap> character;
    private int jumpHeight;
    private boolean jumped;
    private int timer = 0;
    private int imageIndex = 20;
    private int frame = 0;

    Character(PlatformerManager gm, Context context) {
        super(gm);
        character = new ArrayList<>();
        generate(context);
        jumpHeight = 20;
        jumped = false;
        gm.setHealth(3);
        setHitBox(new HitBox(20, gm.getHeight() - character.get(1).getHeight() - 50,
                20 + character.get(1).getWidth(), gm.getHeight() - 50));
    }

    /*Create bitmap*/
    @Override
    public void generate(Context context) {
        character = new ArrayList<>();
        character.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.k1));
        character.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.k2));
        character.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.k3));
        character.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.k4));
        character.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.k5));
        character.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.k6));
        character.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.k7));
        character.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.k8));
    }

    /*Draw character's bitmap*/
    @Override
    void draw(Canvas canvas) {
        if (!getPauseMovement()) {
            if (imageIndex < character.size() - 1) {
                if (frame < 3) {//draw current bitmap for 3 frames
                    canvas.drawBitmap(character.get(imageIndex),
                            getHitBox().getLeft(), getHitBox().getTop(), null);
                    frame++;
                } else {//change to next bitmap at 4th frame
                    imageIndex++;
                    canvas.drawBitmap(character.get(imageIndex),
                            getHitBox().getLeft(), getHitBox().getTop(), null);
                    frame = 0;
                }
            } else {//reset to first bitmap if a loop of bitmaps is finished
                frame = 1;
                canvas.drawBitmap(character.get(0),
                        getHitBox().getLeft(), getHitBox().getTop(), null);
                imageIndex = 0;
            }
        } else {
            canvas.drawBitmap(character.get(imageIndex),
                    getHitBox().getLeft(), getHitBox().getTop(), null);
        }
    }

    /*Make character move upward*/
    @Override
    void jump() {
        if (onPlatform()) {
            jumped = true;
            jumpHeight = -20;
        }
    }

    /*Make character move based on current state*/
    @Override
    void move(ListIterator<PlatformerGameObject> iterator) {
        if (!onPlatform() && !jumped) {
            updateHitBox("Top", getHitBox().getTop() + jumpHeight);
            updateHitBox("Bottom", getHitBox().getBottom() + jumpHeight);
        } else if (this.jumped) {
            updateHitBox("Top", getHitBox().getTop() + jumpHeight);
            updateHitBox("Bottom", getHitBox().getBottom() + jumpHeight);
            timer++;
        }
        if (timer == 15) {
            jumpHeight = 20;
            timer = 0;
            jumped = false;
        }
    }

    /*Detect collision between character and obstacles*/
    @Override
    PlatformerGameObject detectObstacle() {
        for (PlatformerGameObject item : getGm().platformerGameObjects) {
            if (item instanceof Obstacle && item.getHitBox().intersect(getHitBox())) {
                if (getGm().getHealth() > 0) {
                    getGm().decreaseHealth();
                }
                return item;
            }
        }
        return null;
    }

    /*Detect collision between character and coins*/
    @Override
    PlatformerGameObject detectCoin() {
        for (PlatformerGameObject item : getGm().platformerGameObjects) {
            if (item instanceof Coin && item.getHitBox().intersect(getHitBox())) {
                getGm().increaseScore();
                return item;
            }
        }
        return null;
    }

    /*Check if character is on platform*/
    private boolean onPlatform() {
        return getHitBox().getBottom() <= getGm().getHeight() - 50 && getHitBox().getBottom()
                >= getGm().getHeight() - 55;
    }
}

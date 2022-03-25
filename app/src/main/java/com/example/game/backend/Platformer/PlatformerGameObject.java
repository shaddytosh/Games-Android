package com.example.game.backend.Platformer;

import android.content.Context;
import android.graphics.Canvas;

import java.io.Serializable;
import java.util.ListIterator;

class PlatformerGameObject implements Serializable {
    private PlatformerManager gm;
    private HitBox hitBox;
    private boolean pauseMovement;
    
    PlatformerGameObject(PlatformerManager platformerManager) {
        gm = platformerManager;
    }

    void draw(Canvas canvas) {
    }

    void move(ListIterator<PlatformerGameObject> iterator) {
    }

    void jump() {
    }

    HitBox getHitBox() {
        return hitBox;
    }

    PlatformerManager getGm() {
        return gm;
    }
    boolean getPauseMovement() {
        return pauseMovement;
    }

    PlatformerGameObject detectObstacle() {
        return null;
    }

    PlatformerGameObject detectCoin() {
        return null;
    }

    public void generate(Context context) {}

    void setHitBox(HitBox hitBox) {
        this.hitBox = hitBox;
    }

    void setPauseMovement(boolean pauseMovement) {
        this.pauseMovement = pauseMovement;
    }

    void updateHitBox(String which, int value) {
        if (which.equals("Left")) {
            hitBox.setLeft(value);
        }
        if (which.equals("Top")) {
            hitBox.setTop(value);
        }
        if (which.equals("Right")) {
            hitBox.setRight(value);
        }
        if (which.equals("Bottom")) {
            hitBox.setBottom(value);
        }
    }
}

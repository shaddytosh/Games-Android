package com.example.game.backend.Platformer;

import java.io.Serializable;

class HitBox implements Serializable {
    private int left;
    private int top;
    private int right;
    private int bottom;

    HitBox(int l, int t, int r, int b) {
        left = l;
        top = t;
        right = r;
        bottom = b;
    }

    /*Detect if two HitBox of objects are intersected*/
    boolean intersect(HitBox hitbox) {
        return !(top > hitbox.bottom || bottom < hitbox.top ||
                right < hitbox.left || left > hitbox.right);
    }

    int getLeft() {
        return left;
    }

    int getTop() {
        return top;
    }

    int getRight() {
        return right;
    }

    int getBottom() {
        return bottom;
    }

    void setLeft(int left) {
        this.left = left;
    }

    void setRight(int right) {
        this.right = right;
    }

    void setTop(int top) {
        this.top = top;
    }

    void setBottom(int bottom) {
        this.bottom = bottom;
    }
}

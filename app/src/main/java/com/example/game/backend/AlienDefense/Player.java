package com.example.game.backend.AlienDefense;

import android.graphics.Bitmap;

/**
 * The player class!
 */
public class Player extends DynamicGameObject {
    /**
     * The basic constructor, with just the bitmap for the plane.
     *
     * @param image - the bitmap to use for the player.
     */
    Player(Bitmap image) {
        super(image);
    }

    /**
     * The full constructor, with the coordinates and the bitmap to use for the player.
     *
     * @param x     - the x-coordinate
     * @param y     - the y-coordinate
     * @param image - the image to use with the plane.
     */
    Player(int x, int y, Bitmap image) {
        super(x, y, image);
    }

    /**
     * The update method.
     * Despite being a DynamicGameObject, it doesn't have anything to update programmatically
     * (e.g., it doesn't move automatically up or down or anything), so the update method
     * does nothing.
     *
     * @param variables - the game variables, which contain information about how the objects should move.
     */
    @Override
    public void update(GameVariables variables) {

    }

    /**
     * Sets the x-coordinate of the plane. Useful for moving the plane left/right.
     *
     * @param x - the x-coordinate to set the plane's x-coordinate to.
     */
    void setX(int x) {
        super.setCoordinates(x, this.y());
    }
}

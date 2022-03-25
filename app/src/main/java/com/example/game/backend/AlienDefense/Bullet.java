package com.example.game.backend.AlienDefense;

import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;

/**
 * The Bullet class, that is the bullets that are shot by the player.
 */
public class Bullet extends DynamicGameObject {
    /**
     * Constructor with just the image.
     *
     * @param image - the bitmap to use for the bullet.
     */
    Bullet(Bitmap image) {
        super(image);
    }

    /**
     * Complete Bullet constructor with the x- and y-coordinates, and the bitmap.
     *
     * @param x     - the x-coordinate of the bullet
     * @param y     - the y-coordinate of the bullet
     * @param image - the image which we draw for the bullet.
     */
    Bullet(int x, int y, Bitmap image) {
        super(x, y, image);
    }

    /**
     * The update method for the bullet.
     * Moves the bullet upward by its speed, and marks it for removal if it is outside the screen.
     *
     * @param variables - the game variables.
     */
    @Override
    public void update(@NotNull GameVariables variables) {
        if (variables.getStep() % variables.getBulletMovementDelay() == 0) {
            super.setCoordinates(x(), y() - variables.getBulletSpeed());
        }

        // If the bullet is outside the screen, we can remove it to save memory.
        if (y() < -10) {
            this.markForRemoval();
        }
    }
}

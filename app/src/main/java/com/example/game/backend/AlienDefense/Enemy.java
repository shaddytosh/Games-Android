package com.example.game.backend.AlienDefense;

import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The enemies in their flying saucers!
 */
public class Enemy extends DynamicGameObject {
    /**
     * Constructor with just the image.
     *
     * @param image - the bitmap to use for the enemy.
     */
    Enemy(Bitmap image) {
        super(image);
    }

    /**
     * Complete Enemy constructor with the x- and y-coordinates, and the bitmap.
     *
     * @param x     - the x-coordinate of the enemy
     * @param y     - the y-coordinate of the enemy
     * @param image - the image which we draw for the enemy.
     */
    Enemy(int x, int y, Bitmap image) {
        super(x, y, image);
    }

    /**
     * The update method for the enemy.
     * Moves the enemy downward by its speed.
     *
     * @param variables - the game variables.
     */
    @Override
    public void update(@NotNull GameVariables variables) {
        if (variables.getStep() % variables.getEnemyMovementDelay() == 0) {
            super.setCoordinates(x(), y() + variables.getEnemySpeed());
        }
    }

    /**
     * Checks if an enemy has hit the ground.
     * This is a "loss" for the player.
     *
     * @param variables - the game variables.
     * @return - whether the enemy has reached the ground.
     */
    boolean checkLoss(@NotNull GameVariables variables) {
        if (this.y() > variables.getHeight()) {
            this.markForRemoval();

            return true;
        }

        return false;
    }

    /**
     * Checks whether the enemy has been hit.
     * If so, marks the enemy and the bullet for removal, and returns a HitEnemey object in its place.
     *
     * @param bullets       - the bullets in play
     * @param hitEnemyImage - what image to use for the generated HitEnemy.
     * @return - the HitEnemy, if the enemy has been hit.
     */
    HitEnemy checkHits(@NotNull List<Bullet> bullets, Bitmap hitEnemyImage) {
        for (Bullet b : bullets) {
            int enemyXCoordinate = x();
            int enemyYCoordinate = y();
            int enemyWidth = this.getBitmap().getWidth();
            int enemyHeight = this.getBitmap().getHeight();

            int bulletXCoordinate = b.x();
            int bulletYCoordinate = b.y();
            int bulletWidth = b.getBitmap().getWidth();
            int bulletHeight = b.getBitmap().getHeight();

            // Standard rectangular bitmap intersection formula
            // (Good enough up to +/- the corner pixels, which are not
            //  noticeable in the game)
            if (enemyXCoordinate + enemyWidth >= bulletXCoordinate
                    && bulletXCoordinate + bulletWidth >= enemyXCoordinate
                    && enemyYCoordinate + enemyHeight >= bulletYCoordinate
                    && bulletYCoordinate + bulletHeight >= enemyYCoordinate) {
                b.markForRemoval();
                this.markForRemoval();

                return new HitEnemy(this.x(), this.y() - 34, hitEnemyImage);
            }
        }

        return null;
    }
}

package com.example.game.backend.AlienDefense;

import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The hit enemies in their burning flying saucers!
 */
public class HitEnemy extends DynamicGameObject {
    private int xFallSpeed;

    /**
     * Constructor with just the image.
     *
     * @param image - the bitmap to use for the enemy.
     */
    HitEnemy(Bitmap image) {
        super(image);

        this.xFallSpeed = ThreadLocalRandom.current().nextInt(-3, 3 + 1);
    }

    /**
     * Complete HitEnemy constructor with the x- and y-coordinates, and the bitmap.
     * Randomly generates which direction the hit enemies fall in, from -3 to 3 pixels/step.
     *
     * @param x     - the x-coordinate of the enemy
     * @param y     - the y-coordinate of the enemy
     * @param image - the image which we draw for the enemy.
     */
    HitEnemy(int x, int y, Bitmap image) {
        super(x, y, image);

        this.xFallSpeed = ThreadLocalRandom.current().nextInt(-3, 3 + 1);
    }

    /**
     * The update method for the hit enemy.
     * Moves the enemy downward by its speed, and left/right by its unbalanced x-speed.
     * <p>
     * Also marks the hit enemy for removal if it is outside of the view area.
     *
     * @param variables - the game variables.
     */
    @Override
    public void update(@NotNull GameVariables variables) {
        if (variables.getStep() % variables.getEnemyMovementDelay() == 0) {
            this.setCoordinates(x() + this.xFallSpeed, y() + 3);

            if (this.rotation() < 30) {
                this.setRotation(this.rotation() + 1);
            }
        }

        if (this.y() > variables.getHeight()) {
            this.markForRemoval();
        }

        if (this.x() < 0 || this.x() > variables.getWidth()) {
            this.markForRemoval();
        }
    }

    /**
     * Checks whether the hit enemy has been hit again.
     * If it has, marks itself and the hitting bullet for removal.
     *
     * @param bullets - the list of bullets to check
     * @return - whether the hit enemy has been hit.
     */
    boolean checkHits(@NotNull List<Bullet> bullets) {
        for (Bullet b : bullets) {
            int X1 = x();
            int Y1 = y();
            int W1 = this.getBitmap().getWidth();
            int H1 = this.getBitmap().getHeight();

            int X2 = b.x();
            int Y2 = b.y();
            int W2 = b.getBitmap().getWidth();
            int H2 = b.getBitmap().getHeight();

            if (X1 + W1 >= X2 && X2 + W2 >= X1 && Y1 + H1 >= Y2 && Y2 + H2 >= Y1) {
                b.markForRemoval();
                this.markForRemoval();
                return true;
            }
        }

        return false;
    }
}

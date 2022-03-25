package com.example.game.backend.AlienDefense;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.game.R;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A factory class that generates all the bitmaps in the game, with caching.
 */
public class BitmapFactory {
    private Context ctx;

    // Stored bitmaps for caching.
    private static Bitmap bulletBitmap;
    private static Bitmap enemyBitmap;
    private static Bitmap hitEnemyBitmap;

    /**
     * The class constructor.
     *
     * @param ctx - the application context.
     */
    BitmapFactory(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Creates and returns the background bitmap.
     *
     * @return - a bitmap containing the background image.
     */
    Bitmap getBackgroundBitmap() {
        return android.graphics.BitmapFactory.decodeResource(ctx.getResources(), R.drawable.sky);
    }

    /**
     * Creates and returns the plane bitmap.
     *
     * @return - a bitmap containing the plane image.
     */
    Bitmap getPlaneBitmap() {
        return android.graphics.BitmapFactory.decodeResource(ctx.getResources(), R.drawable.plane_style2);
    }

    /**
     * Returns the enemy bitmap.
     * If we already generated the enemy bitmap, return the cached one.
     * Otherwise, create a new one and cache it.
     *
     * @return - the enemy bitmap.
     */
    Bitmap getEnemyBitmap() {
        if (BitmapFactory.enemyBitmap != null) {
            return BitmapFactory.enemyBitmap;
        }

        AtomicReference<Bitmap> decodedEnemyBitmap = new AtomicReference<>(android.graphics.BitmapFactory.decodeResource(ctx.getResources(), R.drawable.enemy_style2));
        BitmapFactory.enemyBitmap = decodedEnemyBitmap.get();
        return BitmapFactory.enemyBitmap;
    }

    /**
     * Returns the hit enemy bitmap.
     * If we already generated the hit enemy bitmap, return the cached one.
     * Otherwise, create a new one and cache it.
     *
     * @return - the hit enemy bitmap.
     */
    Bitmap getHitEnemyBitmap() {
        if (BitmapFactory.hitEnemyBitmap != null) {
            return BitmapFactory.hitEnemyBitmap;
        }

        AtomicReference<Bitmap> decodedHitEnemyBitmap = new AtomicReference<>(android.graphics.BitmapFactory.decodeResource(ctx.getResources(), R.drawable.enemy_hit_style2));
        BitmapFactory.hitEnemyBitmap = decodedHitEnemyBitmap.get();
        return BitmapFactory.hitEnemyBitmap;
    }

    /**
     * Returns the bullet bitmap.
     * If we already generated the bullet bitmap, return the cached one.
     * Otherwise, create a new one and cache it.
     *
     * @return - the bullet bitmap.
     */
    Bitmap getBulletBitmap() {
        if (BitmapFactory.bulletBitmap != null) {
            return BitmapFactory.bulletBitmap;
        }

        AtomicReference<Bitmap> decodedBulletBitmap = new AtomicReference<>(android.graphics.BitmapFactory.decodeResource(ctx.getResources(), R.drawable.bullet_style2));
        BitmapFactory.bulletBitmap = decodedBulletBitmap.get();
        return BitmapFactory.bulletBitmap;
    }

    /**
     * Private helper method to update the enemy bitmap to a new enemy.
     *
     * @param newEnemyBitmap - the new bitmap to which to set the enemies.
     */
    private static void setNewEnemyBitmap(Bitmap newEnemyBitmap) {
        BitmapFactory.enemyBitmap = newEnemyBitmap;
    }

    /**
     * A public-facing method that updates the enemy bitmap.
     * This method unpacks the new bitmap in a background thread as to not clog the main thread
     * with possibly heavy computation; once that is done, it updates the enemy bitmap in
     * BitmapFactory by using the private setNewEnemyBitmap method.
     *
     * @param resourceID - the resource ID of the new bitmap (e.g., R.drawable.newEnemy)
     */
    public void updateEnemyBitmap(final int resourceID) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap newEnemyBitmap = android.graphics.BitmapFactory.decodeResource(ctx.getResources(), resourceID);

                BitmapFactory.setNewEnemyBitmap(newEnemyBitmap);
            }
        });
    }
}

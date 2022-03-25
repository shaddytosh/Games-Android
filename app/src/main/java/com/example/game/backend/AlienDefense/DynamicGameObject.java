package com.example.game.backend.AlienDefense;

import android.graphics.Bitmap;

/**
 * An abstract class encompassing all dynamic game objects--i.e., all objects that are drawn
 * in the game and need to move around.
 */
public abstract class DynamicGameObject {
    private int x;
    private int y;
    private int rotation;
    private Bitmap displayImage;

    private boolean remove;

    /**
     * The simple constructor with just the bitmap.
     * Sets the position to (0, 0) and the rotation to 0.
     *
     * @param image - the bitmap to use for the object.
     */
    DynamicGameObject(Bitmap image) {
        this.x = 0;
        this.y = 0;
        this.rotation = 0;
        this.displayImage = image;
        this.remove = false;
    }

    /**
     * The constructor with the position and the image.
     * Sets the rotation to 0.
     *
     * @param x     - the x-coordinate of the game object
     * @param y     - the y-coordinate of the game object
     * @param image - the bitmap to use for the object.
     */
    DynamicGameObject(int x, int y, Bitmap image) {
        this.x = x;
        this.y = y;
        this.rotation = 0;
        this.displayImage = image;
    }

    /**
     * Sets the coordinates of the object.
     *
     * @param x - new x-coordinate
     * @param y - new y-coordinate
     */
    void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get an array of the coordinates of the object.
     *
     * @return - an int array, where array[0] is the x-coordinate
     * and array[1] is the y-coordinate.
     */
    int[] getCoordinates() {
        return new int[]{this.x, this.y};
    }

    /**
     * Gets the x-coordinate of the object.
     * Much more readable than getCoordinates()[0].
     *
     * @return - the x-coordinate of the object.
     */
    public int x() {
        return x;
    }

    /**
     * Gets the y-coordinate of the object.
     * Much more readable than getCoordinates()[1].
     *
     * @return - the y-coordinate of the object.
     */
    public int y() {
        return y;
    }

    /**
     * Gets the rotation of the object.
     *
     * @return - the rotation, in degrees.
     */
    int rotation() {
        return rotation;
    }

    /**
     * Sets the rotation of the object.
     *
     * @param rotation - the rotation which we set to the object, in degrees.
     */
    void setRotation(int rotation) {
        this.rotation = rotation;
    }

    /**
     * Gets the bitmap used to display the object.
     *
     * @return - the bitmap of the object.
     */
    public Bitmap getBitmap() {
        return this.displayImage;
    }

    /**
     * Marks the object for removal.
     */
    void markForRemoval() {
        this.remove = true;
    }

    /**
     * Gets whether the object is marked for removal.
     *
     * @return - a boolean which indicates whether the object is marked for removal or not.
     */
    boolean isMarkedForRemoval() {
        return this.remove;
    }

    /**
     * An abstract method which inherited classes should implement.
     * The "update" method gets called once per game step, and should contain
     * the moving around of the DynamicGameObject (e.g., where enemies move towards the ground).
     *
     * @param variables - the game variables, which contain information about how the objects should move.
     */
    public abstract void update(GameVariables variables);
}

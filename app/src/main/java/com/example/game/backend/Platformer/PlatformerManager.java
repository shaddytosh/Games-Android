package com.example.game.backend.Platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;



import com.example.game.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class PlatformerManager implements Serializable {

    transient static Bitmap coinBitmap;
    transient static Bitmap obstacleBitmap;
    transient static Bitmap tileBitmap;
    public transient Context context;
    List<PlatformerGameObject> platformerGameObjects;
    boolean pausing = false;
    private int width;
    private int height;
    private int score;
    private int health;
    private int timer = 0;

    PlatformerManager(int height, int width, Context context) {
        this.height = height;
        this.width = width;
        platformerGameObjects = new ArrayList<>();
        this.context = context;
        score = 0;
        health = 3;
        platformerGameObjects.add(new Character(this, context));
        createBitmap();
    }

    /*Create bitmap*/
    public void generate() {
        for (PlatformerGameObject item : platformerGameObjects) {
            item.generate(context);
        }
    }

    /*Draw all game objects on canvas*/
    void draw(Canvas canvas) {
        for (PlatformerGameObject item : platformerGameObjects) {
            item.setPauseMovement(pausing);
            item.draw(canvas);
        }
    }

    /*Update all game objects' positions*/
    void update() {
        PlatformerGameObject collidedObstacle = null;
        PlatformerGameObject collidedCoin = null;
        ListIterator<PlatformerGameObject> iterator = platformerGameObjects.listIterator();
        while (iterator.hasNext()) {
            PlatformerGameObject item = iterator.next();
            if (item instanceof Character) {
                item.move(iterator);
                collidedObstacle = item.detectObstacle();
                collidedCoin = item.detectCoin();
            } else {
                item.move(iterator);
            }
        }
        if (collidedObstacle != null) {//if collision happens between character and obstacle
            platformerGameObjects.remove(collidedObstacle);
        }
        if (collidedCoin != null) {//if collision happens between character and coin
            platformerGameObjects.remove(collidedCoin);
        }
        timer++;
        if (timer == 70) {//create obstacle every 70 frames
            createObstacle();
            timer = 0;//reset timer
        } else if (timer == 60) {//create coin every 60 frames
            createCoin();
        }
    }

    /*create random number of obstacles*/
    private void createObstacle() {
        int speedCoefficient = new Random().nextInt(2);
        platformerGameObjects.add(new Obstacle(2050, height - 120,
                this, context, speedCoefficient));
        int x = new Random().nextInt(2);//decide how many obstacles are created
        int n = 0;
        while (x < 2){
            n++;
            x++;
            platformerGameObjects.add(new Obstacle(2050 + 50 * n, height - 120,
                    this, context, speedCoefficient));
        }

    }

    /*create random number of coins*/
    private void createCoin() {
        platformerGameObjects.add(new Coin(2050, height - 350,
                this, context));
        Random randN = new Random();
        int x = randN.nextInt(4);
        int n = 0;
        while (x < 4) {//decide how many coins are created
                n++;
                x++;
            platformerGameObjects.add(new Coin(2050 + 96 * n, height - 350 - 50 * n,
                    this, context));
            }
    }

    /*create bitmaps for coin, obstacle and tile*/
    private void createBitmap(){
        coinBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.coin_gold);
        obstacleBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.spikes);
        tileBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.platformertiles);
    }

    int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    int getHealth() {
        return health;
    }

    void setHealth(int health) {
        this.health = health;
    }

    void increaseScore() {
        this.score++;
    }

    void decreaseHealth() {
        this.health--;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
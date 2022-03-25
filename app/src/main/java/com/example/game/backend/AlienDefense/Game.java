package com.example.game.backend.AlienDefense;

import com.example.game.backend.PermanentStatistics;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The main Game class, containing all the game logic.
 */
class Game {
    private SpaceInvadersView ui;

    private Player plane;

    // Stylistic choice: why have three lists of objects instead of a list of DynamicGameObjects?
    // The answer is simple: this improves readability by a *lot*,
    // which seems like a very important design consideration.
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private List<HitEnemy> hitEnemies;

    private GameVariables vars;

    private SoundEffectsController sfx;

    private long start;

    /**
     * The constructor. Needs a UI and a main player.
     *
     * @param spaceInvadersView - the UI.
     * @param plane             - the player's plane.
     */
    Game(@NotNull SpaceInvadersView spaceInvadersView, Player plane) {
        this.ui = spaceInvadersView;

        this.plane = plane;
        this.bullets = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.hitEnemies = new ArrayList<>();

        // Load upgrades
        UpgradesManager.load(spaceInvadersView.getContext());

        // Create the game variables
        this.vars = new GameVariables(UpgradesManager.getMoney(spaceInvadersView.getContext()),
                3,
                300,
                500,
                1,
                1,
                10,
                1);

        // Create the sound effects controller
        this.sfx = new SoundEffectsController(spaceInvadersView.getContext());
        this.sfx.playBackgroundMusic();

        // Track game play time
        this.start = System.currentTimeMillis();
    }

    /**
     * Gets the plane.
     *
     * @return - the plane contolled by the player.
     */
    Player getPlane() {
        return plane;
    }

    /**
     * Fires one shot.
     * <p>
     * This method is slightly complex, since it needs to look through all the bonuses
     * and figure out how many bullets get shot each time (for instance, if we've unlocked
     * the right-side gun, then a single shot should fire *two* bullets).
     * <p>
     * It then creates these bullets and adds them to the list of bullets.
     */
    private void shoot() {
        int planeX = this.getPlane().x();
        int planeY = this.getPlane().y();
        int bulletX = planeX - 52;
        int bulletY = planeY - this.getPlane().getBitmap().getHeight();

        // Create left and right bullets
        Bullet leftBullet = new Bullet(bulletX, bulletY, ui.getBulletBitmap());
        Bullet rightBullet = new Bullet(bulletX + 83, bulletY, ui.getBulletBitmap());

        // Always fire the left bullet
        bullets.add(leftBullet);

        // Fire the right bullet if we've unlocked it
        if (this.vars.checkBonusActive(Bonuses.RIGHT_SIDE_GUN)) {
            bullets.add(rightBullet);
        }

        // Add a bunch more bullets if we're unlocked the spray bonus
        if (this.vars.checkBonusActive(Bonuses.SPRAY_BULLETS)) {
            bullets.add(new Bullet(bulletX - 40, bulletY, ui.getBulletBitmap()));
            bullets.add(new Bullet(bulletX + 40, bulletY, ui.getBulletBitmap()));
            bullets.add(new Bullet(bulletX + 43, bulletY, ui.getBulletBitmap()));
            bullets.add(new Bullet(bulletX + 126, bulletY, ui.getBulletBitmap()));
        }

        // Create a literal wall of bullets if we've unlocked that bonus
        if (this.vars.checkBonusActive(Bonuses.WALL_OF_BULLETS)) {
            for (int i = 0; i < ui.getWidth(); i += 40) {
                bullets.add(new Bullet(i, bulletY, ui.getBulletBitmap()));
            }
        }
    }

    /**
     * Creates a new enemy at a random position, and adds it to the enemies list.
     */
    private void createEnemy() {
        int enemyX = (int) (Math.random() * this.vars.getWidth() * 0.9);
        int enemyY = -100;

        Enemy enemy = new Enemy(enemyX, enemyY, ui.getEnemyBitmap());

        enemies.add(enemy);
    }

    /**
     * This method gets all DynamicGameObjects in the game.
     * This includes bullets and enemies, and should be modified if more objects are added.
     * Because this method exists, our UI simply loops through this list, and draws each
     * object with its corresponding bitmap at its required coördinates.
     *
     * @return - the list of dynamic game objects.
     */
    List<DynamicGameObject> getAllDynamicGameObjects() {
        List<DynamicGameObject> objects = new ArrayList<>();

        objects.addAll(bullets);
        objects.addAll(enemies);
        objects.addAll(hitEnemies);

        return objects;
    }

    /**
     * The main logic of the game.
     * This method gets called every 10ms, and recalculates the positions of bullets, enemies, &c.
     */
    void step() {
        // Step
        this.vars.step();

        // Check for loss (if so, we don't need to update the game anymore.)
        if (this.vars.lost()) {
            return;
        }

        // If it's time to shoot per the game variables, we should shoot.
        // Note how this method doesn't need to be changed even if we want to make
        // the game work faster/slower!
        if (this.vars.timeToShoot()) {
            shoot();

            // Unless we have the free bullets bonus, shots are $200.
            if (!this.vars.checkBonusActive(Bonuses.FREE_BULLETS)) {
                this.vars.incrementScore(-200);
            }

            // Play the sound.
            // If we've unlocked the "wall of bullets" bonus, then there's simply too many bullets
            // for the sounds to make any sense. Thus, we play dramatic music instead.
            if (!this.vars.checkBonusActive(Bonuses.WALL_OF_BULLETS)) {
                System.out.println("PEW");
                sfx.playPewSound();
            } else {
                sfx.playBossMusic();
            }
        }

        // Spawns enemy if necessary.
        if (this.vars.timeToSpawnEnemy()) {
            createEnemy();
        }


        // Update all the game objects.

        for (Bullet bullet : bullets) {
            bullet.update(this.vars);
        }

        for (HitEnemy hitEnemy : hitEnemies) {
            hitEnemy.update(this.vars);

            boolean hit = hitEnemy.checkHits(bullets);

            if (hit) {
                sfx.playExplosionSound();

                // Hitting a hit enemy is 3000 points
                vars.incrementScore((int) (3000 + Math.random() * -200));
            }
        }

        for (Enemy enemy : enemies) {
            enemy.update(this.vars);

            // Check for a hit
            HitEnemy hitEnemy = enemy.checkHits(bullets, ui.getHitEnemyBitmap());

            if (hitEnemy != null) {
                sfx.playExplosionSound();
                vars.hitEnemy();

                hitEnemies.add(hitEnemy);


                // Added number of points is exponential
                vars.incrementScore((int) ((int) Math.pow(1.5849, vars.getEnemiesHit()) + Math.random() * 30));
            }


            // Check for ground hit (lose a life)
            boolean hitGround = enemy.checkLoss(vars);

            if (hitGround) {
                this.vars.badEconomy();

                sfx.playInvasionSound();

                loseLife();
            }
        }


        // Remove objects that are marked for removal!
        // Absolutely crucial later in the game, where we might have millions (!) of bullets
        // shot per minute.
        removeDeadObjects();


        // Check for game loss
        if (vars.lost()) {
            sfx.playGameLostSound();

            // Save amount of money
            UpgradesManager.setMoneyAmount(ui.getContext(), this.vars.score());
        }


        // Tell the UI to update the canvas
        ui.update();
    }

    long getTimePlayed() {
        return System.currentTimeMillis() - start;
    }

    /**
     * Lose a life. Updates game variables accordingly.
     */
    private void loseLife() {
        this.vars.loseLife();
    }

    /**
     * Gets rid of all objects marked for removal.
     */
    private void removeDeadObjects() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        List<Enemy> enemiesToRemove = new ArrayList<>();
        List<HitEnemy> hitEnemiesToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            if (bullet.isMarkedForRemoval()) {
                bulletsToRemove.add(bullet);
            }
        }

        for (Enemy enemy : enemies) {
            if (enemy.isMarkedForRemoval()) {
                enemiesToRemove.add(enemy);
            }
        }

        for (HitEnemy hitEnemy : hitEnemies) {
            if (hitEnemy.isMarkedForRemoval()) {
                hitEnemiesToRemove.add(hitEnemy);
            }
        }

        bullets.removeAll(bulletsToRemove);
        enemies.removeAll(enemiesToRemove);
        hitEnemies.removeAll(hitEnemiesToRemove);
    }

    /**
     * Sets the width and the height in the game variables.
     *
     * @param width  - width to set
     * @param height - height to set
     */
    void setWidthAndHeight(int width, int height) {
        this.vars.setWidth(width);
        this.vars.setHeight(height);
    }

    /**
     * Returns whether we have lost the game.
     *
     * @return - whether we have lost the game.
     */
    boolean lost() {
        return vars.lost();
    }

    /**
     * Gets the score, formatted like a string with a dollar sign.
     *
     * @return - the score, properly formatted.
     */
    String getFormattedScore() {
        return "Money: $" + vars.score();
    }

    /**
     * Gets lives left, formatted properly.
     *
     * @return - the number of remaining lives, properly formatted.
     */
    String getFormattedLives() {
        return "Lives: " + vars.getLivesRemaining();
    }

    public GameVariables getVars() {
        return vars;
    }
}
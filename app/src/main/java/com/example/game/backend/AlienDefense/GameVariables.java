package com.example.game.backend.AlienDefense;

/**
 * A class for storing and changing all the game variables.
 */
public class GameVariables {
    // The "tick" of the game.
    private int step;

    // Main game statistics
    private int lives;
    private long score;

    // Keeping track of enemies hit
    private int enemiesHit;

    // Rate a which bullets are fired and enemies are spawned
    private int bulletFiringRate;
    private int enemySpawnRate;

    // We update bullet/enemy movement every <x> frames (usually 1)
    private int bulletMovementDelay;
    private int enemyMovementDelay;

    // Speeds of bullets and enemies
    private int bulletSpeed;
    private int enemySpeed;

    // Width and height of play area (usually equals w/h of view)
    private int width;
    private int height;

    /**
     * The constructor method for the game variables. All variables are initialized here.
     *
     * @param startingMoney       - how much money the player starts with.
     * @param startingLives       - how many lives the player starts with.
     * @param bulletFiringRate    - the number of steps between bullet shots.
     * @param enemySpawnRate      - the number of steps between enemy spawns.
     * @param bulletMovementDelay - the number of steps we wait before updating bullet movement. (for smooth gameplay, should be 1).
     * @param enemyMovementDelay  - the number of steps we wait before updating enemy movement. (for smooth gameplay, should be 1).
     * @param bulletSpeed         - the speed of the bullets, in pixels per step.
     * @param enemySpeed          - the speed of the enemies, in pixels per step.
     */
    GameVariables(long startingMoney, int startingLives, int bulletFiringRate, int enemySpawnRate, int bulletMovementDelay, int enemyMovementDelay, int bulletSpeed, int enemySpeed) {
        this.step = 0;

        this.lives = startingLives;
        this.score = startingMoney;

        this.enemiesHit = 0;

        this.bulletFiringRate = bulletFiringRate;
        this.enemySpawnRate = enemySpawnRate;

        this.bulletMovementDelay = bulletMovementDelay;
        this.enemyMovementDelay = enemyMovementDelay;

        this.bulletSpeed = bulletSpeed;
        this.enemySpeed = enemySpeed;

        // Enable bonuses, if necessary
        if (checkBonusActive(Bonuses.MODERN_BULLETS)) {
            this.bulletFiringRate = bulletFiringRate / 2;
        }

        if (checkBonusActive(Bonuses.MACHINE_GUNS)) {
            this.bulletFiringRate = bulletFiringRate / 5;
        }

        if (checkBonusActive(Bonuses.WALL_OF_BULLETS)) {
            this.bulletFiringRate = 3;
        }
    }

    /**
     * Checks if a certain bonus is active.
     *
     * @param bonus - the bonus to check (e.g., Bonuses.MACHINE_GUNS).
     * @return - whether the player has this bonus.
     */
    boolean checkBonusActive(int bonus) {
        switch (bonus) {
            case Bonuses.FREE_BULLETS:
                return UpgradesManager.isFreeBullets();
            case Bonuses.MODERN_BULLETS:
                return UpgradesManager.isModernBullets();
            case Bonuses.RIGHT_SIDE_GUN:
                return UpgradesManager.isRightSideGun();
            case Bonuses.MACHINE_GUNS:
                return UpgradesManager.isMachineGuns();
            case Bonuses.SPRAY_BULLETS:
                return UpgradesManager.isSprayBullets();
            case Bonuses.WALL_OF_BULLETS:
                return UpgradesManager.isWallOfBullets();
            case Bonuses.MONEY_MULTIPLIER:
                return UpgradesManager.isMoneyMultiplier();
            default:
                return false;
        }
    }

    /**
     * Runs a step in game variables.
     * <p>
     * Essentially, this method is responsible for making the game incrementally more difficult,
     * by increasing the spawn rate and enemy speed every 1000 and 3000 steps, respectively.
     */
    void step() {
        this.step++;

        if (step % 200 == 0) {
            System.out.println("STEP: " + step);
        }

        if (step % 1000 == 0) {
            System.out.println("INCREASING SPAWN RATE");
            if (this.enemySpawnRate > 10) {
                this.enemySpawnRate *= 0.8;
            }
        }

        if (step % 3000 == 0) {
            System.out.println("INCREASING ENEMY SPEED");
            this.enemySpeed++;
        }
    }

    /**
     * Increments the user's score.
     * Checks for the money multiplier bonuses, if they're active.
     *
     * @param amountToIncrementScoreBy - amount to increment the score by.
     */
    void incrementScore(long amountToIncrementScoreBy) {
        if (checkBonusActive(Bonuses.MONEY_MULTIPLIER)) {
            amountToIncrementScoreBy *= 1.1;
        }

        try {
            this.score = Math.addExact(this.score, amountToIncrementScoreBy);
        } catch (ArithmeticException ignored) {
            // If the updated score will overflow a _long_, then don't update the score.
            // Seriously though, if the player gets here, the player won't care about not
            // getting even more money :P
        }
    }

    /**
     * Gets the player's score.
     *
     * @return - the player's score.
     */
    long score() {
        return this.score;
    }

    /**
     * Gets the current game step.
     *
     * @return - the current game step.
     */
    int getStep() {
        return this.step;
    }

    /**
     * Gets the bullet movement delay.
     *
     * @return - the bullet movement delay, in steps.
     */
    int getBulletMovementDelay() {
        return this.bulletMovementDelay;
    }

    /**
     * Gets the enemy movement delay.
     *
     * @return - the enemy movement delay, in steps.
     */
    int getEnemyMovementDelay() {
        return this.enemyMovementDelay;
    }

    /**
     * Gets the bullet firing rate.
     *
     * @return - returns the bullet firing rate, in steps.
     */
    public int getBulletFiringRate() {
        return bulletFiringRate;
    }

    /**
     * Gets the enemy firing rate.
     *
     * @return - returns the enemy firing rate, in steps.
     */
    public int getEnemySpawnRate() {
        return enemySpawnRate;
    }

    /**
     * Gets the bullet speed, in pixels/step.
     *
     * @return - returns the bullet speed, in pixels/step.
     */
    int getBulletSpeed() {
        return bulletSpeed;
    }

    /**
     * Gets the number of enemies hit.
     *
     * @return - returns the number of enemies hit.
     */
    int getEnemiesHit() {
        return this.enemiesHit;
    }

    /**
     * Increases the number of enemies hit by 1.
     */
    void hitEnemy() {
        this.enemiesHit++;
    }

    /**
     * Gets the enemy speed, in pixels/step.
     *
     * @return - returns the enemy speed, in pixels/step.
     */
    int getEnemySpeed() {
        return enemySpeed;
    }

    /**
     * Gets the width of the play area.
     *
     * @return - the width of the play area, in pixels.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the play area.
     *
     * @param width - the width of the play area, as measured.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the height of the play area.
     *
     * @return - the height of the play area, in pixels.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the play area.
     *
     * @param height - the height of the play area, as measured.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Says whether it is time to shoot, based on the game variables.
     *
     * @return - a boolean saying whether it is time to shoot.
     */
    boolean timeToShoot() {
        return this.step % this.bulletFiringRate == 0;
    }

    /**
     * Says whether it is time to spawn an enemy, based on the game variables.
     *
     * @return - a boolean saying whether it is time to spawn an enemy.
     */
    boolean timeToSpawnEnemy() {
        return this.step % this.enemySpawnRate == 0;
    }

    /**
     * Loses a life.
     */
    void loseLife() {
        this.lives--;
    }

    /**
     * Gets the number of lives remaining.
     *
     * @return - the number of remaining lives.
     */
    int getLivesRemaining() {
        return this.lives;
    }

    /**
     * Says whether the player has lost
     *
     * @return - whether the player has lost (i.e., number of lives is zero or less).
     */
    boolean lost() {
        return this.lives <= 0;
    }

    /**
     * Decreases the player's money by 2/3.
     * Called when aliens are let through and attack civilization, thereby destroying
     * two-thirds of the banks.
     */
    void badEconomy() {
        this.score = this.score / 3;
    }
}

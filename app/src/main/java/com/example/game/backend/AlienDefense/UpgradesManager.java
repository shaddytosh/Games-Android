package com.example.game.backend.AlienDefense;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The class that managers all the user's upgrades and upgrade purchases.
 */
public class UpgradesManager {
    private static boolean freeBullets;
    private static boolean modernBullets;
    private static boolean rightSideGun;
    private static boolean machineGuns;
    private static boolean sprayBullets;
    private static boolean wallOfBullets;
    private static boolean moneyMultiplier;

    /**
     * Loads what bonuses the player currently owns into the static variables.
     *
     * @param context - the application context via which to access the shared preferences.
     */
    static void load(@NotNull Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                "upgrades", Context.MODE_PRIVATE);

        boolean defaultValue = false;
        freeBullets = sharedPref.getBoolean("freeBullets", defaultValue);
        modernBullets = sharedPref.getBoolean("modernBullets", defaultValue);
        rightSideGun = sharedPref.getBoolean("rightSideGun", defaultValue);
        machineGuns = sharedPref.getBoolean("machineGuns", defaultValue);
        sprayBullets = sharedPref.getBoolean("sprayBullets", defaultValue);
        wallOfBullets = sharedPref.getBoolean("wallOfBullets", defaultValue);
        moneyMultiplier = sharedPref.getBoolean("moneyMultiplier", defaultValue);
    }

    /**
     * Gets the money the user has.
     *
     * @param context - the application context.
     * @return - the money.
     */
    public static long getMoney(@NotNull Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                "upgrades", Context.MODE_PRIVATE);
        int defaultValue = 0;
        return sharedPref.getLong("money", defaultValue);
    }

    /**
     * Withdraws some amount of money.
     *
     * @param context         - the application context.
     * @param moneyToWithdraw - how much money to withdraw.
     */
    private static void withdraw(@NotNull Context context, long moneyToWithdraw) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                "upgrades", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("money", getMoney(context) - moneyToWithdraw);
        editor.apply();
    }

    /**
     * Deposits some amount of money.
     *
     * @param context        - the application context.
     * @param moneyToDeposit - how much money to deposit.
     */
    public static void deposit(@NotNull Context context, long moneyToDeposit) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                "upgrades", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("money", getMoney(context) + moneyToDeposit);
        editor.apply();
    }

    /**
     * Sets the amount of money the user has.
     *
     * @param context - the application context.
     * @param money   - the money to set to the user.
     */
    static void setMoneyAmount(@NotNull Context context, long money) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                "upgrades", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("money", money);
        editor.apply();
    }

    /**
     * Whether the user has the free bullets upgrade.
     *
     * @return - boolean with the corresponding value.
     */
    @Contract(pure = true)
    static boolean isFreeBullets() {
        return freeBullets;
    }

    /**
     * Whether the user has the modern bullets upgrade.
     *
     * @return - boolean with the corresponding value.
     */
    @Contract(pure = true)
    static boolean isModernBullets() {
        return modernBullets;
    }

    /**
     * Whether the user has the right-side gun upgrade.
     *
     * @return - boolean with the corresponding value.
     */
    @Contract(pure = true)
    static boolean isRightSideGun() {
        return rightSideGun;
    }

    /**
     * Whether the user has the machine guns upgrade.
     *
     * @return - boolean with the corresponding value.
     */
    @Contract(pure = true)
    static boolean isMachineGuns() {
        return machineGuns;
    }

    /**
     * Whether the user has the spray bullets upgrade.
     *
     * @return - boolean with the corresponding value.
     */
    @Contract(pure = true)
    static boolean isSprayBullets() {
        return sprayBullets;
    }

    /**
     * Whether the user has the "wall of bullets" upgrade.
     *
     * @return - boolean with the corresponding value.
     */
    @Contract(pure = true)
    static boolean isWallOfBullets() {
        return wallOfBullets;
    }

    /**
     * Whether the user has the money multiplier upgrade.
     *
     * @return - boolean with the corresponding value.
     */
    @Contract(pure = true)
    static boolean isMoneyMultiplier() {
        return moneyMultiplier;
    }

    /**
     * Enables a bonus, based on the passed id.
     *
     * @param context   - the application context
     * @param bonusType - the ID of the bonus to enable (e.g., Bonuses.MACHINE_GUNS)
     * @return - whether the bonus was enabled. Returns `false` if the user did not have enough money.
     */
    public static boolean enableBonus(Context context, int bonusType) {
        int price = 0;
        String upgradeName = "";

        switch (bonusType) {
            case Bonuses.FREE_BULLETS:
                price = 1000;
                upgradeName = "freeBullets";
                break;
            case Bonuses.MODERN_BULLETS:
                price = 1500;
                upgradeName = "modernBullets";
                break;
            case Bonuses.RIGHT_SIDE_GUN:
                price = 50000;
                upgradeName = "rightSideGun";
                break;
            case Bonuses.MACHINE_GUNS:
                price = 250000;
                upgradeName = "machineGuns";
                break;
            case Bonuses.SPRAY_BULLETS:
                price = 1000000;
                upgradeName = "sprayBullets";
                break;
            case Bonuses.WALL_OF_BULLETS:
                price = 100000000;
                upgradeName = "wallOfBullets";
                break;
            case Bonuses.MONEY_MULTIPLIER:
                price = 100;
                upgradeName = "moneyMultiplier";
                break;
            default:

                break;
        }

        long currentMoney = getMoney(context);

        if (currentMoney < price) {
            return false;
        }

        withdraw(context, price);

        SharedPreferences sharedPref = context.getSharedPreferences(
                "upgrades", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(upgradeName, true);
        editor.apply();

        return true;
    }
}

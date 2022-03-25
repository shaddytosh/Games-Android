package com.example.game.backend;

import java.io.Serializable;

public class PermanentStatistics implements Serializable {
    public int alienDefenseTotalEnemiesKilled = 0;
    public int platformerLongestTime = 0;
    public int platformerTopScore = 0;
    public int zvpTotalPlantsKilled = 0;
    public long totalCoins = 0;
    public long totalPlayingTime = 0;

    public void updateSpace(int score, long enemiesHit, long totalTime) {
        alienDefenseTotalEnemiesKilled += enemiesHit;
        totalCoins += score;
        totalPlayingTime += totalTime;
    }
    public void updatePlatformer(int inGameTime, int score, long totalTime) {
        if (inGameTime > platformerLongestTime) {
            platformerLongestTime = inGameTime;
        }
        if (score > platformerTopScore) {
            platformerTopScore = score;
        }
        totalCoins += score;
        totalPlayingTime += totalTime;
    }
    public void updateZVP(int plantsKilled, int coins, long totalTime) {
        zvpTotalPlantsKilled += plantsKilled;
        totalCoins += coins;
        totalPlayingTime += totalTime;
    }
}

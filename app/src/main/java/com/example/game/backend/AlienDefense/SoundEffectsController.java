package com.example.game.backend.AlienDefense;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.game.R;

/**
 * A controller class in charge of all the sound effects of the game.
 */
class SoundEffectsController {
    // All the different sound effects.
    // We use multiple MediaPlayers instead of using one and changing the tracks
    // so that we can play multiple sounds at once (see below).
    private MediaPlayer mediaPlayerExplosion;
    private MediaPlayer mediaPlayerPew;
    private MediaPlayer mediaPlayerLoss;
    private MediaPlayer mediaPlayerScreams;
    private MediaPlayer mediaPlayerInvasion;
    private MediaPlayer mediaPlayerBossMusic;
    private MediaPlayer mediaPlayerBackgroundMusic;

    /**
     * Create the SoundEffectsController.
     * <p>
     * The constructor initializes several MediaPlayers, one for each sound.
     * (Multiple players are necessary in case we want to play two different
     * sounds at the same time).
     *
     * @param context - the application context
     */
    SoundEffectsController(Context context) {
        mediaPlayerExplosion = MediaPlayer.create(context, R.raw.boom);
        mediaPlayerPew = MediaPlayer.create(context, R.raw.pew);
        mediaPlayerLoss = MediaPlayer.create(context, R.raw.lost);
        mediaPlayerScreams = MediaPlayer.create(context, R.raw.screaming);
        mediaPlayerInvasion = MediaPlayer.create(context, R.raw.warning);
        mediaPlayerBossMusic = MediaPlayer.create(context, R.raw.boss_music);
        mediaPlayerBackgroundMusic = MediaPlayer.create(context, R.raw.dramatic);
    }

    /**
     * Plays a "playExplosionSound" sound.
     * <p>
     * Restarts the explosion audio file if it's currently being played (killing enemies too fast!)
     * and then plays it.
     */
    void playExplosionSound() {
        mediaPlayerExplosion.seekTo(0);
        mediaPlayerExplosion.start();
    }

    /**
     * Plays a "pew" sound.
     */
    void playPewSound() {
        mediaPlayerPew.seekTo(0);
        mediaPlayerPew.start();
    }

    /**
     * Play boss music.
     * <p>
     * (That is, start it if we're not already playing it.)
     */
    void playBossMusic() {
        if (mediaPlayerBossMusic.isPlaying()) {
            return;
        }

        mediaPlayerBossMusic.seekTo(0);
        mediaPlayerBossMusic.setLooping(true);
        mediaPlayerBossMusic.start();
    }

    /**
     * Play background music.
     * <p>
     * (That is, start it if we're not already playing it.)
     * Attribution: joshuaempyre at freesound.org.
     */
    void playBackgroundMusic() {
        if (mediaPlayerBackgroundMusic.isPlaying()) {
            return;
        }

        mediaPlayerBackgroundMusic.seekTo(0);
        mediaPlayerBackgroundMusic.setLooping(true);
        mediaPlayerBackgroundMusic.start();
    }

    /**
     * Plays an alien invasion sound.
     */
    void playInvasionSound() {
        mediaPlayerScreams.seekTo(0);
        mediaPlayerScreams.start();

        mediaPlayerInvasion.seekTo(0);
        mediaPlayerInvasion.start();
    }

    /**
     * Plays the sound when the game is lost.
     */
    void playGameLostSound() {
        mediaPlayerLoss.start();
    }
}

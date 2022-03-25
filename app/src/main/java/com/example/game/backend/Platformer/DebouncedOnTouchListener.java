package com.example.game.backend.Platformer;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A Debounced OnTouchListener
 * Rejects touch that are too close together in time.
 * This class is safe to use as an OnTouchListener for multiple views, and will debounce each one separately.
 * reference:
 * https://stackoverflow.com/questions/16534369/avoid-button-multiple-rapid-touchs?noredirect=1&lq=1
 */
public abstract class DebouncedOnTouchListener implements View.OnTouchListener {

    private final long minimumIntervalMillis;
    private Map<View, Long> lastTouchMap;

    /**
     * Implement this in your subclass instead of onTouch
     *
     * @param v The view that was touched
     */
    public abstract void onATouch(View v, MotionEvent event);

    /**
     * The one and only constructor
     *
     * @param minimumIntervalMillis The minimum allowed time between touchs - any touch sooner than this after a previous touch will be rejected
     */
    DebouncedOnTouchListener(long minimumIntervalMillis) {
        this.minimumIntervalMillis = minimumIntervalMillis;
        this.lastTouchMap = new WeakHashMap<>();
    }

    @Override
    public boolean onTouch(View touchedView, MotionEvent event) {
        Long previousTouchTimestamp = lastTouchMap.get(touchedView);
        long currentTimestamp = SystemClock.uptimeMillis();

        if (previousTouchTimestamp == null || Math.abs(currentTimestamp - previousTouchTimestamp) > minimumIntervalMillis) {
            lastTouchMap.put(touchedView, currentTimestamp);
            onATouch(touchedView, event);
            touchedView.performClick();
        }
        return true;
    }
}
package com.example.game.backend.AlienDefense;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.example.game.R;
import com.example.game.frontend.UpgradesActivity;

/**
 * The main view class for Space Invaders (Alien Defense).
 * Handles all of the UI.
 */
public class SpaceInvadersView extends View {
    // Application context
    private Context ctx;

    // Bitmaps and bitmap factories
    private com.example.game.backend.AlienDefense.BitmapFactory bitmapFactory;
    private Bitmap backgroundBitmap;
    private Bitmap bulletBitmap;
    private Bitmap enemyBitmap;
    private Bitmap hitEnemyBitmap;

    // Game variable
    // Needed in order to avoid drawing graphics
    // before the game starts (leads to potentially
    // undefined behaviour)
    static boolean gameStarted;

    // The main game code
    private Game game;

    // Screen dimensions to set the canvas
    private int w;
    private int h;

    // Helper fields to set width/height onMeasure
    private Rect mMeasuredRect;

    // Painters and text drawers
    private Paint textPaint;
    Typeface font;
    Matrix planeDrawMatrix;
    Matrix dynamicObjectMatrix;

    // Thread handler to load game
    Handler gameThreadHandler = new Handler();

    // Game thread which launches the game
    final Runnable gameThread = new Runnable() {
        public void run() {
            game.step();

            gameThreadHandler.postDelayed(this, 10);
        }
    };

    /**
     * Default constructor.
     *
     * @param ct - the application context.
     */
    public SpaceInvadersView(final Context ct) {
        super(ct);

        init(ct);
    }

    /**
     * Constructor for the custom view.
     *
     * @param ct    - the application context
     * @param attrs - custom attributes to pass to the view.
     */
    public SpaceInvadersView(final Context ct, final AttributeSet attrs) {
        super(ct, attrs);

        init(ct);
    }

    /**
     * Constructor for the custom view.
     *
     * @param ct       - the application context
     * @param attrs    - custom attributes to pass to the view
     * @param defStyle - current theme attribute that contains a reference to a style resource.
     */
    public SpaceInvadersView(final Context ct, final AttributeSet attrs, final int defStyle) {
        super(ct, attrs, defStyle);

        init(ct);
    }

    /**
     * Initializes the text drawing for the game.
     * Needed to display money, lives, &c.
     */
    private void initText() {
        this.textPaint = new Paint();

        font = ResourcesCompat.getFont(getContext(), R.font.gamefont);
        textPaint.setTypeface(font);
    }

    /**
     * Initializes the sprites through the BitmapFactory.
     */
    private void initSprites() {
        bitmapFactory = new com.example.game.backend.AlienDefense.BitmapFactory(ctx);

        // Generate bitmaps
        backgroundBitmap = bitmapFactory.getBackgroundBitmap();
        bulletBitmap = bitmapFactory.getBulletBitmap();
        enemyBitmap = bitmapFactory.getEnemyBitmap();
        hitEnemyBitmap = bitmapFactory.getHitEnemyBitmap();
    }

    /**
     * Initializes all the graphics of the game.
     * Automatically takes care of text and sprite initialization;
     * also initializes graphics matrices.
     */
    private void initGraphics() {
        initText();
        initSprites();

        // Allocate drawing matrices here,
        // so we don't need to do so in onPaint.
        planeDrawMatrix = new Matrix();
        dynamicObjectMatrix = new Matrix();
    }

    /**
     * Initializes the game logic.
     * <p>
     * That is, it creates the game in a background thread and launches it on the main UI thread
     * once it's ready.
     */
    private void initAndLaunchGame() {
        gameStarted = false;


        // Get plane bitmap
        final Bitmap b = bitmapFactory.getPlaneBitmap();


        this.post(new Runnable() {
            @Override
            public void run() {
                Player player = new Player(0, h, b);
                game = new Game(SpaceInvadersView.this, player);
                game.setWidthAndHeight(w, h);

                // Start the game
                startGame(10);

                SpaceInvadersView.gameStarted = true;
            }
        });
    }

    /**
     * The initialization function for our custom view.
     * We do this here, and not in the constructor, since we have many different constructors.
     * This function takes care of the base object allocations--that is, it generates the bitmaps
     * for the background, the plane, the enemies, etc. so they can be used later.
     *
     * @param ct - the application context.
     */
    private void init(final Context ct) {
        this.ctx = ct;

        initGraphics();
        initAndLaunchGame();
    }

    /**
     * Asks the UI to update.
     * <p>
     * Normally, the UI updates when it sees fit (i.e., when it detects something changes).
     * However, this is not always what we want, and so we call update every time we do
     * something programmatically and we are sure that something onscreen will change.
     */
    void update() {
        invalidate();
    }

    /**
     * Starts the game.
     *
     * @param delayMillis - the delay before the game starts, in milliseconds. Should be small.
     */
    private void startGame(int delayMillis) {
        gameThreadHandler.postDelayed(gameThread, delayMillis);
    }


    /**
     * Adapts to changing size, if necessary.
     *
     * @param w    - current view width
     * @param h    - current view height
     * @param oldw - old view width
     * @param oldh - old view height
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * The main draw function. Takes care of actually drawing everything on-screen where it needs
     * to be. Note that this function gets called a *lot*, so computation-intensive methods, such
     * as allocation and image reading, should not be called here but pre-computed and reused.
     *
     * @param canvas - the canvas on which to draw
     */
    @Override
    public void onDraw(final Canvas canvas) {
        // Background
        canvas.drawBitmap(backgroundBitmap, null, mMeasuredRect, null);

        if (!gameStarted) {
            return;
        }

        // Draw plane
        planeDrawMatrix.reset();
        planeDrawMatrix.postRotate(0);
        planeDrawMatrix.postTranslate(
                game.getPlane().x() - game.getPlane().getBitmap().getWidth() / 2,
                game.getPlane().y() - game.getPlane().getBitmap().getHeight());
        canvas.drawBitmap(game.getPlane().getBitmap(), planeDrawMatrix, null);

        // Draw bullets and enemies (and anything else we add)
        for (DynamicGameObject object : game.getAllDynamicGameObjects()) {
            dynamicObjectMatrix.reset();
            dynamicObjectMatrix.postRotate(object.rotation());
            dynamicObjectMatrix.postTranslate(object.x(), object.y());
            canvas.drawBitmap(object.getBitmap(), dynamicObjectMatrix, null);
        }

        // Draw all the text things--the score, etc.
        drawText(canvas);
    }

    /**
     * Draws all the text things--the score, lives, and information about losing.
     *
     * @param canvas - the canvas to draw on.
     */
    private void drawText(Canvas canvas) {
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(60);
        canvas.drawText(game.getFormattedScore(), 100, 100, textPaint);
        textPaint.setTextSize(40);
        canvas.drawText(game.getFormattedLives(), 100, 160, textPaint);

        if (game.lost()) {
            textPaint.setColor(Color.RED);
            textPaint.setTextSize(200);
            canvas.drawText("YOU LOST!", 10, 400, textPaint);

            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(50);
            canvas.drawText("Tap to continue", 10, 600, textPaint);
        }
    }

    /**
     * The onTouch listener that handles the logic for what happens when the screen is touched.
     * Currently, it moves the position of the plane, and nothing else.
     * TODO: Improve accessibility by implementing performClick (used by screen readers).
     *
     * @param event - the motion event that happens to the screen.
     * @return - always returns true to keep following the action after a click (we need drags)
     */
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        int xTouch = (int) event.getX();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (game.lost()) {
                    Intent upgradesIntent = new Intent(getContext(), UpgradesActivity.class);
                    upgradesIntent.putExtra("ENEMIES_HIT", game.getVars().getEnemiesHit());//long
                    upgradesIntent.putExtra("SCORE", game.getVars().getEnemiesHit());//int
                    upgradesIntent.putExtra("TIME_PLAYED", game.getTimePlayed());//long
                    upgradesIntent.putExtra("USER_NAME", getActivity().getIntent().getStringExtra("USER_NAME"));
                    getContext().startActivity(upgradesIntent);
                }

            case MotionEvent.ACTION_MOVE:
                // Update the plane's x-coordinate
                game.getPlane().setX(xTouch);
                invalidate();

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_DOWN:

            default:
                break;
        }

        return true;
    }

    /**
     * Helper method that allows us to measure the screen size.
     * This is useful for drawing the background of a proper size in the game.
     *
     * @param widthMeasureSpec  - horizontal space requirements
     * @param heightMeasureSpec - vertical space requirements.
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMeasuredRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    /**
     * Utility method to get the bullet bitmap.
     *
     * @return - the bullet bitmap.
     */
    public android.graphics.Bitmap getBulletBitmap() {
        return this.bulletBitmap;
    }

    /**
     * Utility method to get the enemy bitmap.
     *
     * @return - the enemy bitmap.
     */
    public android.graphics.Bitmap getEnemyBitmap() {
        return this.enemyBitmap;
    }

    /**
     * Utility method to get the "hit enemy" bitmap.
     *
     * @return - the "hit enemy" bitmap.
     */
    public android.graphics.Bitmap getHitEnemyBitmap() {
        return this.hitEnemyBitmap;
    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}
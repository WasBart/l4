package at.ac.tuwien.policenauts.l4.android;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import at.ac.tuwien.policenauts.l4.game.Game;
import at.ac.tuwien.policenauts.l4.game.GameLoop;
import at.ac.tuwien.policenauts.l4.game.ResolutionConverter;

/**
 * The surface view located on the game activity,
 * start and hold the game loop.
 *
 * @author Wassily Bartuska
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private Game game;
    private GameLoop gameLoop;
    private Thread gameThread;

    /**
     * Initialize gameSurfaceView object with context and attributeSet.
     *
     * @param context Application context
     * @param attributeSet set of attributes
     */
    public GameSurfaceView(Context context, AttributeSet attributeSet) {
        // Initialize surface view component (code from game programming lecture)
        super(context, attributeSet);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    /**
     * Invoked when an instance of this class is created.
     *
     * @param surfaceHolder holder of the surface object
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // Get global game state from application context
        GameApplication applicationContext = (GameApplication) getContext().getApplicationContext();
        game = applicationContext.getGame();

        // Initialize game and start the gameloop in a separate thread
        gameLoop = new GameLoop(surfaceHolder, applicationContext.getGame());
        if (!game.isPaused())
            resume();
    }

    /**
     * Update the size of the surface view in the resolution manager of Game
     *
     * @param w Width
     * @param h Height
     * @param oldw Old width
     * @param oldh Old height
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        GameApplication applicationContext = (GameApplication) getContext().getApplicationContext();
        ResolutionConverter resolution = applicationContext.getGame().resolution;
        resolution.setDimensions(w, h);
        resolution.setDensity(applicationContext.getResources().getDisplayMetrics().density);
    }


    /**
     * Invoked when an instance of this class is changed.
     *
     * @param surfaceHolder holder of the surface object
     * @param i new pixel format of the surface
     * @param i1 new width of the surface
     * @param i2 new height of the surface
     */
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    /**
     * Invoked when an instance of this class is destroyed.
     *
     * @param surfaceHolder holder of the surface object
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        pause();
    }

    /**
     * Invoked when an instance of this class is drawn.
     *
     * @param canvas medium used for drawing
     */
    @Override
    public void onDraw(Canvas canvas) {
        game.render(canvas);
    }

    /**
     * Handle an incoming motion event.
     *
     * @param e The occurred event.
     * @return True, if event has been handled
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return game.handleTouch(e);
    }

    /**
     * Stop the gameloop.
     */
    public void pause() {
        gameLoop.setRunning(false);
        try {
            gameThread.join();
        } catch (InterruptedException ex) {
            Log.e("Error", ex.getMessage());
        }
    }

    /**
     * Start the gameloop.
     */
    public void resume() {
        if (gameThread != null && gameThread.isAlive())
            return;
        gameThread = new Thread(gameLoop);
        gameThread.start();
    }
}

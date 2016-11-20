package at.ac.tuwien.policenauts.l4.android;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import at.ac.tuwien.policenauts.l4.game.Game;
import at.ac.tuwien.policenauts.l4.game.GameLoop;

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

    public GameSurfaceView(Context context, AttributeSet attributeSet) {
        // Initialize surface view component (code from game programming lecture)
        super(context, attributeSet);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // Get global game state from application context
        GameApplication applicationContext = (GameApplication) getContext().getApplicationContext();
        game = applicationContext.getGame();

        // Initialize game and start the gameloop in a separate thread
        gameLoop = new GameLoop(surfaceHolder, this, applicationContext.getGame());
        gameThread = new Thread(gameLoop);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // TODO: What is this actually good for?
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        gameLoop.setRunning(false);
        try {
            gameThread.join();
        } catch (InterruptedException ex) {
            Log.e("Error", ex.getMessage());
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        game.render(canvas);
    }
}

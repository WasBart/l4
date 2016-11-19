package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import at.ac.tuwien.policenauts.l4.android.GameSurfaceView;

/**
 * Run the gameloop and make sure to update/render the game logic
 * in the process. Seperate class from Game, allows keeping the
 * view and holder variables final.
 */
public class GameLoop implements Runnable {
    private final String TAG = "GameLoop";
    private final SurfaceHolder holder;
    private final GameSurfaceView view;
    private final Game game;
    private boolean running = false;

    /**
     * Initialize the gameloop object.
     *
     * @param holder Owner of the game surface
     * @param view The actual surface view
     * @param game Reference to global game object
     */
    public GameLoop(SurfaceHolder holder, GameSurfaceView view, Game game) {
        this.holder = holder;
        this.view = view;
        this.game = game;
    }

    /**
     * The actual game loop, running as separate thread.
     */
    @Override
    public void run() {
        Canvas canvas = null;
        running = true;

        while (running) {
            // Lock canvas, since we're in a separate thread, and render
            canvas = null;
            try {
                canvas = holder.lockCanvas();
                synchronized (holder) {
                    if (canvas != null)
                        view.draw(canvas);
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    /**
     * Enable/disable running flag. Effectively stops the game loop.
     *
     * @param running Mark the game loop as running or not
     */
    public void setRunning(boolean running) {
        this.running = running;
    }
}

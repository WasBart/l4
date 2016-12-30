package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import at.ac.tuwien.policenauts.l4.android.GameSurfaceView;

/**
 * Run the gameloop and make sure to update/render the game logic
 * in the process. Seperate class from Game, allows keeping the
 * view and holder variables final.
 *
 * @author Wassily Bartuska
 */
public class GameLoop implements Runnable {
    private final String TAG = "GameLoop";
    private final SurfaceHolder holder;
    private final Game game;
    private boolean running = false;

    /**
     * Initialize the gameloop object.
     *
     * @param holder Owner of the game surface
     * @param game Reference to global game object
     */
    public GameLoop(SurfaceHolder holder, Game game) {
        this.holder = holder;
        this.game = game;
    }

    /**
     * The actual game loop, running as separate thread.
     */
    @Override
    public void run() {
        Canvas canvas = null;
        running = true;
        float old_time = (float) System.nanoTime() / 1000000;
        float cur_time;
        float delta_time;

        game.playBgm();

        while (running) {

            //Calculate delta_time
            cur_time =  (float) System.nanoTime() / 1000000;
            delta_time = cur_time - old_time;
            old_time = cur_time;

            game.updateLogic(delta_time);

            // Lock canvas, since we're in a separate thread, and render
            canvas = null;
            try {
                canvas = holder.lockCanvas();
                synchronized (holder) {
                    if (canvas != null)
                        game.render(canvas);
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
        }
        game.pauseBgm();
       // game.destroyBgm();
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

package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import at.ac.tuwien.policenauts.l4.android.GameSurfaceView;

/**
 * Run the gameloop and make sure to update/render the game logic
 * in the process. Seperate class from Game, allows keeping the
 * view and holder variables final.
 */
public class GameLoop implements Runnable {
    private final SurfaceHolder holder;
    private final GameSurfaceView view;
    private final Game game;
    private boolean running = false;

    public GameLoop(SurfaceHolder holder, GameSurfaceView view, Game game) {
        this.holder = holder;
        this.view = view;
        this.game = game;
    }

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

    public void setRunning(boolean running) {
        this.running = running;
    }
}

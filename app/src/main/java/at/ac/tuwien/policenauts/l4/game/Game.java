package at.ac.tuwien.policenauts.l4.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

import at.ac.tuwien.policenauts.l4.android.GameSurfaceView;

/**
 * The main game object, used for loading resources and rendering.
 * The GameLoop calls and updates this class.
 */
public class Game {
    private final Context context;
    private TextureManager textureManager = null;

    /**
     * Initialize game object with application context.
     *
     * @param context Application context
     */
    public Game(Context context) {
        this.context = context;
    }

    /**
     * Initialize all important resource managers.
     */
    public void initialize() {
        textureManager = new TextureManager(context);
    }

    /**
     * Rendering all objects to the canvas here.
     *
     * @param canvas Drawing canvas
     */
    public void render(Canvas canvas) {
        // TODO: Draw stuff here
        canvas.drawColor(Color.BLUE);
    }
}

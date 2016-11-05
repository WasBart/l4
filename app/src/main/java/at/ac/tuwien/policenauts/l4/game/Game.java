package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

import at.ac.tuwien.policenauts.l4.android.GameSurfaceView;

/**
 * The main game object, used for loading resources and rendering.
 * The GameLoop calls and updates this class.
 */
public class Game {
    public void render(Canvas canvas) {
        // TODO: Draw stuff here
        canvas.drawColor(Color.BLUE);
    }
}

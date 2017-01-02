package at.ac.tuwien.policenauts.l4.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

/**
 * The main game object, used for loading resources and rendering.
 * The GameLoop calls and updates this class.
 *
 * @author Wassily Bartuska
 */
public class Game {
    public final ResolutionConverter resolution = new ResolutionConverter();

    // Resource management
    private final Context context;
    private TextureManager textureManager = null;
    private SoundManager soundManager = null;
    private LevelLoader levelLoader = null;
    private boolean resourcesLoaded = false;

    // Game state
    private float fps = 60.0f;
    private int bgmPos = 0;
    private int reachedLevel = 0;
    private int currentlyActiveLevel = -1;

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
        // Initialize resource managers
        if (!resourcesLoaded) {
            textureManager = new TextureManager(context);
            soundManager = new SoundManager(context);
            levelLoader = new LevelLoader(context, textureManager);
            soundManager.setBgm();
            resourcesLoaded = true;
        }
        resume();
    }

    /**
     * Remove all loaded resources from memory, but keep game logic state.
     */
    public void freeResources() {
        textureManager.unloadTextures();
        soundManager.releaseBgm();

        // Reset managers
        textureManager = null;
        levelLoader = null;
        soundManager = null;
        resourcesLoaded = false;
    }

    /**
     * Start the next level, based on the reached level.
     */
    private void startGame() {

    }

    /**
     * Update the game logic.
     *
     * @param tpf Time per frame in milliseconds
     */
    void updateLogic(float tpf) {
        if (currentlyActiveLevel != reachedLevel)
            return;

        fps = 1 / tpf * 1000;
    }

    /**
     * Rendering all objects to the canvas here.
     *
     * @param canvas Drawing canvas
     */
    public void render(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        // Draw fps counter
        Paint textP = new Paint();
        textP.setColor(Color.GREEN);
        textP.setTextAlign(Paint.Align.RIGHT);
        String fpsText = "frames per second: " + fps;
        canvas.drawText(fpsText, 0, fpsText.length()-1, (canvas.getWidth()/10) * 9,
                canvas.getHeight()/10, textP);

        // Quit here if level hasn't started yet
        if (currentlyActiveLevel != reachedLevel)
            return;

        // Pass canvas to texture manager
        textureManager.setCanvas(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawRect(canvas.getWidth()/2-40, canvas.getHeight()/2-40,
                canvas.getWidth()/2+40, canvas.getHeight()/2+40, paint);
    }

    /**
     * Pause the game, save the state and release unnecessary resources.
     */
    public void pause() {
        soundManager.pauseBgm();
        bgmPos = soundManager.getBgmPos();
    }

    /**
     * Resume a previously paused game.
     */
    public void resume() {
        soundManager.forwardBgm(bgmPos);
        soundManager.startBgm();
    }

}

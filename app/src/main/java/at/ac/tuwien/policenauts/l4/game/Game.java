package at.ac.tuwien.policenauts.l4.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
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

    Sprite testSprite = new Sprite(0, 5, 150);

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
            textureManager = new TextureManager(context, resolution);
            soundManager = new SoundManager(context);
            levelLoader = new LevelLoader(context, textureManager);
            soundManager.setBgm();
            resourcesLoaded = true;

            List<String> sprites = new ArrayList<>();
            sprites.add("o2_5");
            List<Integer> frameCounts = new ArrayList<>();
            frameCounts.add(5);
            textureManager.loadTextures(sprites, frameCounts, null);
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
        //if (currentlyActiveLevel != reachedLevel)
        //    return;

        fps = 1 / tpf * 1000;
        testSprite.update(tpf);
    }

    /**
     * Rendering all objects to the canvas here.
     *
     * @param canvas Drawing canvas
     */
    public void render(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        // Density independent pixels, move to resolution
        final float GESTURE_THRESHOLD_DIP = 16.0f;
        final float scale = context.getResources().getDisplayMetrics().density;
        final int gestureThreshold = (int) (GESTURE_THRESHOLD_DIP * scale + 0.5f);

        // Draw fps counter
        Paint textP = new Paint();
        Rect src = new Rect(1600, 50, 1600, 50);
        resolution.toScreenRect(src, src);
        textP.setColor(Color.GREEN);
        textP.setTextAlign(Paint.Align.RIGHT);
        textP.setTextSize(gestureThreshold / 1.5f);
        String fpsText = fps + " FPS";
        canvas.drawText(fpsText, src.left, src.top, textP);

        // Quit here if level hasn't started yet
        //if (currentlyActiveLevel != reachedLevel)
        //    return;

        // Pass canvas to texture manager
        textureManager.setCanvas(canvas);
        textureManager.drawSprite(testSprite, new Rect(750, 450, 850, 550));
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

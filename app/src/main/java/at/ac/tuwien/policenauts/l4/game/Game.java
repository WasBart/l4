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
<<<<<<< HEAD
    private int reachedLevel = 0;
    private int currentlyActiveLevel = -1;
=======
    private float timer = 0.0f;

    // Sound handles
    int worldID;
>>>>>>> sound

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
            soundManager.initSp(5);
            worldID = soundManager.loadSound(context, "world");

            resourcesLoaded = true;

            startGame();
        }
        resume();
    }

    /**
     * Remove all loaded resources from memory, but keep game logic state.
     */
    public void freeResources() {
        textureManager.unloadTextures();
        soundManager.releaseBgm();
        soundManager.releaseSounds();

        // Reset managers
        textureManager = null;
        levelLoader = null;
        soundManager = null;
        resourcesLoaded = false;
    }

    /**
     * Start the next level, based on the reached level.
     */
    void startGame() {
        currentlyActiveLevel = reachedLevel;
        levelLoader.getLevel(0).startLevel();
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

        // Update all objects in the level
        levelLoader.getLevel(0).updateLevel(tpf);

        // Play sound
        timer += tpf;
        if (timer > 5000.0f)
            if(worldID != 0)
            soundManager.playSound(worldID,1,1,1,0,1.0f);
        timer %= 5000.0f;
    }

    /**
     * Rendering all objects to the canvas here.
     *
     * @param canvas Drawing canvas
     */
    public void render(Canvas canvas) {
        // Quit here if level hasn't started yet
        if (currentlyActiveLevel != reachedLevel)
            return;
        
        // Pass canvas to texture manager
        textureManager.setCanvas(canvas);
        levelLoader.getLevel(currentlyActiveLevel).renderLevel(textureManager);

        // Draw fps counter
        Paint textP = new Paint();
        Rect src = new Rect(1600, 50, 1600, 50);
        resolution.toScreenRect(src, src);
        textP.setColor(Color.GREEN);
        textP.setTextAlign(Paint.Align.RIGHT);
        textP.setTextSize(30 * resolution.density());
        String fpsText = fps + " FPS";
        canvas.drawText(fpsText, src.left, src.top, textP);
    }

    /**
     * Pause the game, save the state and release unnecessary resources.
     */
    public void pause() {
        soundManager.pauseBgm();
        bgmPos = soundManager.getBgmPos();

        soundManager.pauseSounds();
    }

    /**
     * Resume a previously paused game.
     */
    public void resume() {
        soundManager.forwardBgm(bgmPos);
        soundManager.startBgm();

        soundManager.resumeSounds();
    }

}

package at.ac.tuwien.policenauts.l4.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.policenauts.l4.android.PauseMenuActivity;

/**
 * The main game object, used for loading resources and rendering.
 * The GameLoop calls and updates this class.
 *
 * @author Wassily Bartuska
 */
public class Game {
    public final ResolutionConverter resolution = new ResolutionConverter();
    private final Rect audioIconPosition = new Rect(1300, 0, 1450, 150);
    private final Rect pauseIconPosition = new Rect(1450, 0, 1600, 150);
    private final Rect positionCalc = new Rect(0,0,0,0);

    // Resource management
    private final Context context;
    private TextureManager textureManager = null;
    private SoundManager soundManager = null;
    private LevelLoader levelLoader = null;
    private boolean resourcesLoaded = false;

    // Intents and transition handling
    private Context activityContext;
    private Intent pauseIntent;
    private boolean paused = false;

    // Enable/disable audio
    private int audioIcon = 1;

    // Game state
    private float fps = 60.0f;
    private int bgmPos = 0;
    private int reachedLevel = 0;
    private int currentlyActiveLevel = -1;
    private float timer = 0.0f;
    private Player player;

    // Sound handles
    private int worldID;

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
     *
     * @param activityContext Activity context of the GameActivity
     */
    public void initialize(Context activityContext) {
        // Initialize the pause intent
        this.activityContext = activityContext;
        pauseIntent = new Intent(context, PauseMenuActivity.class);

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
        if (paused || currentlyActiveLevel != reachedLevel)
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
        renderUI(canvas);
    }

    /**
     * Render the ingame user interface.
     *
     * @param canvas Canvas to draw on
     */
    private void renderUI(Canvas canvas) {
        // Render icons
        textureManager.drawTexture(audioIcon, audioIconPosition);
        textureManager.drawTexture(2, pauseIconPosition);

        // Draw fps counter
        Paint textP = new Paint();
        Rect src = new Rect(1600, 1000, 1600, 1000);
        resolution.toScreenRect(src, src);
        textP.setColor(Color.WHITE);
        textP.setTextAlign(Paint.Align.RIGHT);
        textP.setTextSize(30 * resolution.density());
        String fpsText = fps + " FPS";
        canvas.drawText(fpsText, src.left, src.top, textP);
    }

    /**
     * Pause the game, save the state and release unnecessary resources.
     */
    public void pause() {
        paused = true;

        soundManager.pauseBgm();
        bgmPos = soundManager.getBgmPos();
        soundManager.pauseSounds();
    }

    /**
     * Resume a previously paused game.
     */
    public void resume() {
        paused = false;

        soundManager.forwardBgm(bgmPos);
        soundManager.startBgm();
        soundManager.resumeSounds();
    }

    /**
     * Handle an incoming touch event.
     *
     * @param event A motion event
     * @return True if event has been handled
     */
    public boolean handleTouch(MotionEvent event) {
        final int x = (int)event.getX();
        final int y = (int)event.getY();

        // Choose appropriate action
        switch(event.getAction()) {
            case MotionEvent.ACTION_UP:
                resolution.toScreenRect(audioIconPosition, positionCalc);
                if (positionCalc.contains(x, y)) {
                    if (audioIcon == 1) {
                        soundManager.muteSounds();
                        audioIcon = 0;
                    } else {
                        soundManager.unmuteSounds();
                        audioIcon = 1;
                    }
                    return true;
                }

                // Killer queen has already touched that pause icon
                resolution.toScreenRect(pauseIconPosition, positionCalc);
                if (positionCalc.contains(x, y)) {
                    pause();
                    activityContext.startActivity(pauseIntent);
                } else {
                    player.setTouchX(x);
                    player.setTouchY(y);
                }
                break;
            default:
                break;
        }
        return true;
    }
}

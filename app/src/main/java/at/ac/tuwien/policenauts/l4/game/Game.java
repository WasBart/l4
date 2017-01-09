package at.ac.tuwien.policenauts.l4.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;

import at.ac.tuwien.policenauts.l4.android.GameOverActivity;
import at.ac.tuwien.policenauts.l4.android.GameWonActivity;
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
    private GestureDetector detector;

    // Resource management
    private final Context context;
    private TextureManager textureManager = null;
    private SoundManager soundManager = null;
    private LevelLoader levelLoader = null;

    // Intents and transition handling
    private Context activityContext;
    private Intent pauseIntent;
    private Intent gameOverIntent;
    private Intent winIntent;
    private boolean paused = false;

    // Enable/disable audio
    private int audioIcon = 1;

    // Game state
    private int bgmPos = 0;
    private int reachedLevel = 0;
    private int currentlyActiveLevel = -1;
    private float timer = 0.0f;
    private final Player player = new Player();
    private float startTime = 0f;
    private float elapsedTime = 0f;
    private float curTime = 0f;

    // Paints for gameplay UI
    private final Rect oxygenOuter = new Rect(20, 900, 920, 980);
    private final Rect oxygenInner = new Rect(25, 905, 915, 975);
    private final Rect ammoPos = new Rect(1500, 900, 1600, 980);
    private final Rect livesPos = new Rect(20, 100, 0, 0);
    private final Rect livesIconPos = new Rect(90, 30, 140, 100);
    private final Paint stroke = new Paint();
    private final Paint fill = new Paint();

    // Sound handles
    private int worldID;

    /**
     * Initialize game object with application context.
     *
     * @param context Application context
     */
    public Game(Context context) {
        this.context = context;

        // Start resource managers
        textureManager = new TextureManager(context, resolution);
        soundManager = new SoundManager(context);
        levelLoader = new LevelLoader(context, textureManager, player, soundManager);

        // Initialize paint for gameplay UI
        stroke.setColor(Color.WHITE);
        stroke.setStyle(Paint.Style.STROKE);
        fill.setColor(Color.WHITE);
        fill.setStyle(Paint.Style.FILL);
    }

    /**
     * Initialize all important resource managers.
     *
     * @param activityContext Activity context of the GameActivity
     */
    public void initialize(Context activityContext) {
        // Initialize the pause intent
        this.activityContext = activityContext;
        pauseIntent = new Intent(activityContext, PauseMenuActivity.class);
        gameOverIntent = new Intent(activityContext, GameOverActivity.class);
        winIntent = new Intent(activityContext, GameWonActivity.class);
        detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return handleSingleTap(e);
            }
        });
    }

    /**
     * Start the next level, based on the reached level.
     */
    public void startGame() {
        currentlyActiveLevel = reachedLevel;
        levelLoader.getLevel(currentlyActiveLevel).restartLevel();
        startTime = (float) System.nanoTime() / 1000000000;
        player.resetStats();
        player.resetLives();
    }

    /**
     * Update the game logic.
     *
     * @param tpf Time per frame in milliseconds
     */
    void updateLogic(float tpf) {
        if (paused || currentlyActiveLevel != reachedLevel)
            return;

        // Update all objects in the level
        levelLoader.getLevel(currentlyActiveLevel).updateLevel(tpf);

        // Play sound
        /*timer += tpf;
        if (timer > 5000.0f)
            if(worldID != 0)
                soundManager.playSound(worldID,1,1,1,0,1.0f);
        timer %= 5000.0f;*/

        // Calculate elapsed time
        curTime = (float) System.nanoTime() / 1000000000;
        elapsedTime = (curTime - startTime);

        // Check, whether the player has been killed
        if (player.getOxygen() < 0.01f && player.getLives() == 1) {
            player.decreaseLives();
            activityContext.startActivity(gameOverIntent);
        } else if (player.getOxygen() < 0.01f) {
            player.decreaseLives();
            player.setRespawned(true);
            levelLoader.getLevel(currentlyActiveLevel).restartLevel();
            player.resetStats();
        }

        // Check, whether the level has been cleared
        if (levelLoader.getLevel(currentlyActiveLevel).isOver()
                && player.currentPosition().right > 1600) {
            // If this is the last level, display winning screen
            if (currentlyActiveLevel == levelLoader.numLevels() - 1) {
                activityContext.startActivity(winIntent);
            } else {
                reachedLevel++;
                currentlyActiveLevel = reachedLevel;
                levelLoader.getLevel(currentlyActiveLevel).restartLevel();
                player.resetStats();
            }
        }
    }

    /**
     * Rendering all objects to the canvas here.
     *
     * @param canvas Drawing canvas
     */
    public void render(Canvas canvas) {
        // Quit here if level hasn't started yet
        if (paused || currentlyActiveLevel != reachedLevel)
            return;

        // Pass canvas to texture manager
        textureManager.setCanvas(canvas);
        levelLoader.getLevel(currentlyActiveLevel).renderLevel(textureManager);
        renderUI(canvas);

        textureManager.drawSprite(player.currentSprite(), player.currentPosition());
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

        // Draw player lives
        fill.setTextSize(60 * resolution.density());
        fill.setColor(Color.WHITE);
        resolution.toScreenRect(livesPos, positionCalc);
        canvas.drawText(player.getLives() + "", positionCalc.left, positionCalc.top, fill);
        textureManager.drawTexture(4, livesIconPos);

        // Check player health
        if (player.getOxygen() <= 0.4) {
            stroke.setColor(Color.RED);
            fill.setColor(Color.RED);
        } else {
            stroke.setColor(Color.WHITE);
            fill.setColor(Color.WHITE);
        }

        // Draw oxygen bars
        resolution.toScreenRect(oxygenOuter, positionCalc);
        canvas.drawRect(positionCalc, stroke);
        oxygenInner.right = (int) (oxygenInner.left + 890.0f * player.getOxygen());
        resolution.toScreenRect(oxygenInner, positionCalc);
        canvas.drawRect(positionCalc, fill);

        // Draw ammo
        int originalAmmoPos = ammoPos.left;
        for (int i = 0; i < player.getRailgunAmmo(); i++) {
            textureManager.drawTexture(3, ammoPos);
            ammoPos.offset(-100, 0);
        }
        ammoPos.offset(originalAmmoPos - ammoPos.left, 0);
    }

    /**
     * Pause the game, save the state and release unnecessary resources.
     */
    public void pause() {
        // Set the pause state
        if (paused)
            return;
        paused = true;

        // Pause sounds
        soundManager.pauseBgm();
        bgmPos = soundManager.getBgmPos();
        soundManager.pauseSounds();

        // Release the loaded resources
        textureManager.unloadTextures();
        soundManager.releaseBgm();
        soundManager.releaseSounds();
    }

    /**
     * Resume a previously paused game.
     *
     */
    public void resume() {
        paused = false;

        // Initialize sounds and textures
        levelLoader.loadResources();
        soundManager.setBgm();
        soundManager.initSp(5);
        worldID = soundManager.loadSound(context, "world");

        // Start playing sounds
        soundManager.forwardBgm(bgmPos);
        soundManager.startBgm();
        soundManager.resumeSounds();
    }

    /**
     * Handle a single tap event.
     *
     * @param event The motion event
     */
    private boolean handleSingleTap(MotionEvent event) {
        final int x = (int)event.getX();
        final int y = (int)event.getY();

        // Audio icon has been clicked
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
            activityContext.startActivity(pauseIntent);
            return true;
        }

        // Shoot railgun
        levelLoader.getLevel(currentlyActiveLevel).railgunShot();
        return false;
    }

    /**
     * Handle an incoming touch event.
     *
     * @param event A motion event
     * @return True if event has been handled
     */
    public boolean handleTouch(MotionEvent event) {
        detector.onTouchEvent(event);
        final float x = event.getX();
        final float y = event.getY();

        // Choose appropriate action
        switch(event.getAction()) {
            case MotionEvent.ACTION_UP:
                player.setRespawned(false);
                return true;
            case MotionEvent.ACTION_DOWN:
                // Set initial player touch position
                player.setTouchX(x * resolution.factorX());
                player.setTouchY(y * resolution.factorY());
                return true;
            case MotionEvent.ACTION_MOVE:
                player.applyDeltaTransformation(x * resolution.factorX(), y * resolution.factorY());
                return true;
            default:
                break;
        }
        return false;
    }

    /**
     * Get elapsed time of the game.
     *
     * @return elapsed time in seconds
     */
    public float getElapsedTime() {
        return elapsedTime;
    }
}

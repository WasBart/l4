package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Rect;
import android.util.Log;

/**
 * Main class for controlling the player and interaction of the player
 * with the surrounding objects.
 *
 * @author Wassily Bartuska
 */
class Player extends GameObject {
    private static final Rect SIZE = new Rect(100, 200, 0, 0);
    private static final int RAILGUN_AMMO_MAX = 5;
    private int railgunAmmo = 5;
    private int lives = 3;
    private float oxygen = 1.0f;
    private float invincibilityTime = 0.0f;
    private float hitTime = 0.0f;
    private float oldTouchX;
    private float oldTouchY;
    private float deltaTouchX;
    private float deltaTouchY;
    private boolean respawned = false;

    /**
     * The size of this object in indepdentent pixels
     *
     * @return The sizes of the object stored as Rect
     */
    @Override
    Rect size() {
        return SIZE;
    }

    /**
     * Update the game logic and frame of the game object.
     *
     * @param tpf Time per frame as calculated in the game loop
     * @param baseMovement Basic level movement amount, frame independent
     */
    @Override
    void update(float tpf, float baseMovement) {
        invincibilityTime = Math.max(0.0f, invincibilityTime - tpf);
        hitTime = Math.max(0.0f, hitTime - tpf);
        if (invincibilityTime < 0.01f && currentSpriteID() == 1)
            setCurrentSprite(0);
        if (hitTime < 0.01f && currentSpriteID() == 2)
            setCurrentSprite(0);
        currentSprite().update(tpf);

        // Make deltas frame independent
        final float frameDeltaX = deltaTouchX * tpf / 1000.0f;
        final float frameDeltaY = deltaTouchY * tpf / 1000.0f;

        // Make sure frame deltas don't exceed the borders
        float actualDeltaX = Math.max(frameDeltaX, -position.left);
        float actualDeltaY = Math.min(Math.max(frameDeltaY, -position.top),
                ResolutionConverter.HEIGHT - position.bottom);

        // Update the player position
        if (baseMovement > 0.0f) {
            position.offset((int) (Math.max(actualDeltaX, 0.0f) + baseMovement),
                    (int) actualDeltaY);
        } else {
            position.offset((int) actualDeltaX, (int) actualDeltaY);
        }

        // Decrease the deltas
        deltaTouchX -= frameDeltaX;
        deltaTouchY -= frameDeltaY;
    }

    /**
     * Reset the position to the original position.
     */
    @Override
    void resetPosition() {
        super.resetPosition();
        deltaTouchX = 0;
        deltaTouchY = 0;
        oldTouchX = 0;
        oldTouchY = 0;
    }

    /**
     * Reset player stats.
     */
    void resetStats() {
        railgunAmmo = RAILGUN_AMMO_MAX;
        oxygen = 1.0f;
        invincibilityTime = 0.0f;
    }

    /**
     * Check whether the player is currently invincible.
     *
     * @return True if the player is invincible
     */
    boolean isInvincible() {
        return invincibilityTime > 0.0f;
    }

    /**
     * Enable the invincibility timer by setting a counter value.
     *
     * @param invincibilityTime Time till invincibility effect wears off, milliseconds.
     */
    void setInvincibilityTime(int invincibilityTime) {
        this.invincibilityTime = invincibilityTime;
        setCurrentSprite(1);
    }

    /**
     * Retrieve the amount of railgun ammo currently available to the player.
     *
     * @return Current amount of railgun ammo
     */
    int getRailgunAmmo() {
        return railgunAmmo;
    }

    /**
     * Change the amount of railgun ammo by a given value.
     *
     * @param deltaRailgunAmmo increase or decrease railgun ammo
     */
    void changeRailgunAmmo(int deltaRailgunAmmo) {
        railgunAmmo = Math.min(Math.max(railgunAmmo + deltaRailgunAmmo, 0), RAILGUN_AMMO_MAX);
    }

    /**
     * Retrieve the currently available amount of oxygen.
     *
     * @return The current amount of oxygen, between 0.0 and 1.0
     */
    float getOxygen() {
        return oxygen;
    }

    /**
     * Increase/decrease the amount of oxygen.
     *
     * @param deltaOxygen Change the amount of oxygen by this value
     */
    void changeOxygen(float deltaOxygen) {
        oxygen = Math.min(Math.max(oxygen + deltaOxygen, 0.0f), 1.0f);
        Log.e("oxygen", oxygen + "");
    }

    /**
     * Calculate the delta position from the last touch input.
     *
     * @param x The new x-Coordinate
     * @param y The new y-Coordinate
     */
    void applyDeltaTransformation(float x, float y) {
        if (respawned)
            return;
        deltaTouchX += x - oldTouchX;
        deltaTouchY += y - oldTouchY;
        oldTouchX = x;
        oldTouchY = y;
    }

    /**
     * Set the x-coordinate where the screen was touched.
     *
     * @param x x-coordinate of point where screen was touched.
     */
    void setTouchX(float x) {
        oldTouchX = x;
    }

    /**
     * Set the y-coordinate where the screen was touched.
     *
     * @param y y-coordinate of point where screen was touched.
     */
    void setTouchY(float y) {
        oldTouchY = y;
    }

    /**
     * Get the current amount of lives.
     *
     * @return The current number of lives
     */
    int getLives() {
        return lives;
    }

    /**
     * Decrease the amount of lives by 1.
     */
    void decreaseLives() {
        lives--;
    }

    /**
     * Reset the lives to the original amount.
     */
    void resetLives() {
        lives = 3;
    }

    /**
     * Mark the player as hit.
     */
    void hit() {
        hitTime = 1000.0f;
        setCurrentSprite(2);
    }

    /**
     * Set the respawned flag to true, if it's true a touch input
     * won't be received till the user stopped the input one time.
     *
     * @param respawned Set the respawn flag to temporarily disable input
     */
    void setRespawned(boolean respawned) {
        this.respawned = respawned;
    }
}

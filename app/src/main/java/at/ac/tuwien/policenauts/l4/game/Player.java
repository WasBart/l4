package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Rect;

/**
 * Main class for controlling the player and interaction of the player
 * with the surrounding objects.
 *
 * @author Wassily Bartuska
 */
class Player extends GameObject {
    private static final int RAILGUN_AMMO_MAX = 5;
    private int railgunAmmo = 5;
    private float oxygen = 1.0f;
    private float invincibilityTime = 0.0f;
    int touchX;
    int touchY;

    /**
     * The size of this object in indepdentent pixels
     *
     * @return The sizes of the object stored as Rect
     */
    @Override
    Rect size() {
        return new Rect(100,300,0,0);
    }

    /**
     * Update the game logic and frame of the game object.
     *
     * @param tpf Time per frame as calculated in the game loop
     * @param baseMovement Basic level movement amount, frame independent
     */
    @Override
    void update(float tpf, float baseMovement) {
        invincibilityTime = Math.min(0.0f, invincibilityTime - tpf);

        int posX = position.centerX();
        int posY = position.centerY();
        if(touchX > posX) {
            position.offset((int) (posX + baseMovement * tpf), posY);
        }
        else if(touchX < posX) {
            position.offset((int) (posX - baseMovement * tpf), posY);
        }

        if(touchY > posY) {
            position.offset(posX, (int) (posY + baseMovement * tpf));
        }
        else if(touchY < posY) {
            position.offset(posX, (int) (posY - baseMovement * tpf));
        }

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
        oxygen = Math.min(Math.max(oxygen + deltaOxygen, 0), 1.0f);
    }

    /**
     * Set the x-coordinate where the screen was touched.
     *
     * @param x x-coordinate of point where screen was touched.
     */
    public void setTouchX(int x) {
        touchX = x;
    }

    /**
     * Set the y-coordinate where the screen was touched.
     *
     * @param y y-coordinate of point where screen was touched.
     */
    public void setTouchY(int y) {
        touchY = y;
    }
}

package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Rect;

/**
 * Main class for controlling the player and interaction of the player
 * with the surrounding objects.
 *
 * @author Wassily Bartuska
 */
class Player implements GameObject {
    /**
     * Update the game logic and frame of the game object.
     *
     * @param tpf Time per frame as calculated in the game loop
     */
    @Override
    public void update(float tpf) {

    }

    /**
     * Retrieve the current sprite used for drawing.
     *
     * @return The current sprite
     */
    @Override
    public Sprite currentSprite() {
        return null;
    }

    /**
     * Retrieve the current position where to draw the sprite.
     *
     * @return Current position as rect
     */
    @Override
    public Rect currentPosition() {
        return null;
    }
}

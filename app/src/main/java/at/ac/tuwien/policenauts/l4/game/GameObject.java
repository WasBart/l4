package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Rect;

/**
 * Interface for every object being drawn in the game view.
 *
 * @author Michael Pucher
 */
interface GameObject {
    /**
     * Update the game logic and frame of the game object.
     *
     * @param tpf Time per frame as calculated in the game loop
     */
    void update(float tpf);

    /**
     * Retrieve the current sprite used for drawing.
     *
     * @return The current sprite
     */
    Sprite currentSprite();

    /**
     * Retrieve the current position where to draw the sprite.
     *
     * @return Current position as rect
     */
    Rect currentPosition();
}

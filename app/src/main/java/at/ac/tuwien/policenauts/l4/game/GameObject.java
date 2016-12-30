package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for every object being drawn in the game view.
 *
 * @author Michael Pucher
 */
abstract class GameObject {
    private final List<Sprite> sprites = new ArrayList<>(1);
    private int currentSprite = 0;

    /**
     * Add a list of sprites to this game object.
     *
     * @param sprites A list of sprites
     */
    void addSprites(List<Sprite> sprites) {
        this.sprites.addAll(sprites);
    }

    /**
     * Retrieve the current sprite used for drawing.
     *
     * @return The current sprite
     */
    Sprite currentSprite() {
        return sprites.get(currentSprite);
    }

    /**
     * Change the current sprite/spritesheet to a new spritesheet.
     *
     * @param sprite The spritesheet to change to
     */

    /**
     * Update the game logic and frame of the game object.
     *
     * @param tpf Time per frame as calculated in the game loop
     */
    abstract void update(float tpf);

    /**
     * Retrieve the current position where to draw the sprite.
     *
     * @return Current position as rect
     */
    abstract Rect currentPosition();
}

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
    private final Rect originalPosition = new Rect();
    final Rect position = new Rect();

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
     * Set the original position
     *
     * @param x x-Coordinate in independent pixels
     * @param y y-Coordinate in independent pixels
     */
    void setOriginalPosition(int x, int y) {
        Rect size = size();
        originalPosition.set(x - size.left / 2, y - size.top / 2,
                x + size.left / 2, y + size.left / 2);
        position.set(originalPosition);
    }

    /**
     * Reset the position to the original position.
     */
    void resetPosition() {
        position.set(originalPosition);
    }

    /**
     * Retrieve the current position where to draw the sprite.
     *
     * @return Current position as rect
     */
    Rect currentPosition() {
        return position;
    }

    /**
     * Change the current sprite/spritesheet to a new spritesheet.
     *
     * @param sprite The spritesheet to change to
     */
    void setCurrentSprite(int sprite) {
        currentSprite = sprite;
    }

    /**
     * Update the game logic and frame of the game object.
     *
     * @param tpf Time per frame as calculated in the game loop
     * @param baseMovement Basic level movement amount, frame independent
     */
    abstract void update(float tpf, float baseMovement);

    /**
     * The size of this object in indepdentent pixels
     *
     * @return The sizes of the object stored as Rect
     */
    abstract Rect size();
}

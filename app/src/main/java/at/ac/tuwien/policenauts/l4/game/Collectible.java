package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Rect;

/**
 * An object which can be picked up by a player, having a
 * positive effect in most cases.
 *
 * @author Michael Pucher
 */
abstract class Collectible implements NonPlayerObject {
    /**
     * The object can apply a positive or negative effect on the player.
     *
     * @param player Apply the effect on this player
     */
    @Override
    public void applyEffect(Player player) {

    }

    /**
     * Checks, whether an object is visible or not. If this function
     * returns false, the object will be skipped in the render loop
     * and removed from the render list.
     *
     * @return True, if object is visible or possibly visible
     */
    @Override
    public boolean visible() {
        return false;
    }

    /**
     * This function is called, if a collision is being detected. It
     * contains effects, which are applied on the object itself rather
     * than the player, e.g. an asteroid splitting up on collision or
     * a power up being consumed.
     */
    @Override
    public void collisionEffect() {

    }

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

package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Rect;

/**
 * An object which can be picked up by a player, having a
 * positive effect in most cases.
 *
 * @author Michael Pucher
 */
abstract class Collectible extends NonPlayerObject {
    /**
     * This function is called, if a collision is being detected. It
     * contains effects, which are applied on the object itself rather
     * than the player, e.g. an asteroid splitting up on collision or
     * a power up being consumed.
     */
    @Override
    public void collisionEffect() {
        setVisible(false);
    }
}

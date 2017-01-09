package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Rect;

/**
 * An object which can be picked up by a player, having a
 * positive effect in most cases.
 *
 * @author Michael Pucher
 */
abstract class Collectible extends NonPlayerObject {
    private static final Rect SIZE = new Rect(100, 100, 0, 0);
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
     * This function is called, if a collision is being detected. It
     * contains effects, which are applied on the object itself rather
     * than the player, e.g. an asteroid splitting up on collision or
     * a power up being consumed.
     */
    @Override
    public void collisionEffect() {
        setVisible(false);
        soundManager.playSound(soundManager.getPickupId(),1,1,1,0,1.0f);
    }

    /**
     * This function is called, when the object is being hit with a railgun shot.
     */
    @Override
    void railgunHit() {
        // Do nothing
    }
}

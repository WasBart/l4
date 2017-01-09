package at.ac.tuwien.policenauts.l4.game;

import android.util.Log;

/**
 * Interface for all GameObjects on the screen which are not the player.
 *
 * @author Michael Pucher
 */
abstract class NonPlayerObject extends GameObject {
    private boolean visible = true;

    /**
     * Checks, whether an object is visible or not. If this function
     * returns false, the object will be skipped in the render loop
     * and removed from the render list.
     *
     * @return True, if object is visible or possibly visible
     */
    boolean isVisible() {
        return visible;
    }

    /**
     * Change the visibility of an object, from inside the object.
     *
     * @param visible The visibility of the object.
     */
    void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Update the game logic and frame of the game object.
     *
     * @param tpf Time per frame as calculated in the game loop
     * @param baseMovement Basic level movement amount, frame indepedent
     */
    @Override
    public void update(float tpf, float baseMovement) {
        currentSprite().update(tpf);
        position.offset(- (int) baseMovement, 0);
    }

    /**
     * The object can apply a positive or negative effect on the player.
     *
     * @param player Apply the effect on this player
     */
    abstract void applyEffect(Player player);

    /**
     * This function is called, if a collision is being detected. It
     * contains effects, which are applied on the object itself rather
     * than the player, e.g. an asteroid splitting up on collision or
     * a power up being consumed.
     */
    abstract void collisionEffect();

    /**
     * This function is called, when the object is being hit with a railgun shot.
     */
    abstract void railgunHit();
}

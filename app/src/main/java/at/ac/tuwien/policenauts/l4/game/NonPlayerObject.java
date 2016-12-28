package at.ac.tuwien.policenauts.l4.game;

/**
 * Interface for all GameObjects on the screen which are not the player.
 *
 * @author Michael Pucher
 */
interface NonPlayerObject extends GameObject {
    /**
     * The object can apply a positive or negative effect on the player.
     *
     * @param player Apply the effect on this player
     */
    void applyEffect(Player player);

    /**
     * Checks, whether an object is visible or not. If this function
     * returns false, the object will be skipped in the render loop
     * and removed from the render list.
     *
     * @return True, if object is visible or possibly visible
     */
    boolean visible();

    /**
     * This function is called, if a collision is being detected. It
     * contains effects, which are applied on the object itself rather
     * than the player, e.g. an asteroid splitting up on collision or
     * a power up being consumed.
     */
    void collisionEffect();
}

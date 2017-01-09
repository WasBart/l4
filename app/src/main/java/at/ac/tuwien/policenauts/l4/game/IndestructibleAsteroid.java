package at.ac.tuwien.policenauts.l4.game;

/**
 * An asteroid, not destructable by a railgun shot.
 *
 * @author Michael Pucher
 */
class IndestructibleAsteroid extends Asteroid {
    /**
     * The object can apply a positive or negative effect on the player.
     *
     * @param player Apply the effect on this player
     */
    @Override
    void applyEffect(Player player) {
        player.changeOxygen(-0.3f);
    }

    /**
     * This function is called, when the object is being hit with a railgun shot.
     */
    @Override
    void railgunHit() {
        // DO NOTHING
    }
}

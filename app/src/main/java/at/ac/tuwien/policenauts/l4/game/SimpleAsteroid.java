package at.ac.tuwien.policenauts.l4.game;

/**
 * A simple, destructible asteroid.
 *
 * @author Michael Pucher
 */
class SimpleAsteroid extends Asteroid {
    /**
     * The object can apply a positive or negative effect on the player.
     *
     * @param player Apply the effect on this player
     */
    @Override
    void applyEffect(Player player) {
        player.changeOxygen(-0.2f);
        player.hit();
    }

    /**
     * This function is called, when the object is being hit with a railgun shot.
     */
    @Override
    void railgunHit() {
        setVisible(false);
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
    }
}

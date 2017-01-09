package at.ac.tuwien.policenauts.l4.game;

/**
 * A simple, destructible asteroid.
 *
 * @author Michael Pucher
 */
public class SimpleAsteroid extends Asteroid {
    /**
     * The object can apply a positive or negative effect on the player.
     *
     * @param player Apply the effect on this player
     */
    @Override
    void applyEffect(Player player) {
        player.changeOxygen(-0.2f);
    }
}

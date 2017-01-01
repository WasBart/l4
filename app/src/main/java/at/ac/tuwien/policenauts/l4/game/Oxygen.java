package at.ac.tuwien.policenauts.l4.game;

/**
 * A powerup restoring depleted oxygen by a few points.
 *
 * @author Michael Pucher
 */
class Oxygen extends Collectible {
    /**
     * The object can apply a positive or negative effect on the player.
     *
     * @param player Apply the effect on this player
     */
    @Override
    void applyEffect(Player player) {
        player.changeOxygen(0.3f);
    }
}

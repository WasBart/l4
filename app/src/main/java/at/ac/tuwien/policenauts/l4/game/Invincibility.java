package at.ac.tuwien.policenauts.l4.game;

/**
 * A powerup causing the player to be temporarily invincible.
 *
 * @author Michael Pucher
 */
class Invincibility extends Collectible {
    /**
     * The object can apply a positive or negative effect on the player.
     *
     * @param player Apply the effect on this player
     */
    @Override
    void applyEffect(Player player) {
        player.setInvincibilityTime(5000);
    }
}

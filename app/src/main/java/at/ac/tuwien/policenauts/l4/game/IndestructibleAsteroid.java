package at.ac.tuwien.policenauts.l4.game;

/**
 * An asteroid, not destructable by a railgun shot.
 *
 * @author Michael Pucher
 */
class IndestructibleAsteroid extends Asteroid {
    private boolean collided = false;

    /**
     * The object can apply a positive or negative effect on the player.
     *
     * @param player Apply the effect on this player
     */
    @Override
    void applyEffect(Player player) {
        if (!collided) {
            player.changeOxygen(-0.3f);
            player.hit();
        }
    }

    /**
     * This function is called, if a collision is being detected. It
     * contains effects, which are applied on the object itself rather
     * than the player, e.g. an asteroid splitting up on collision or
     * a power up being consumed.
     */
    @Override
    public void collisionEffect() {
        if (!collided)
            soundManager.playSound(soundManager.getColId(),1,1,1,0,1.0f);
        collided = true;
    }

    /**
     * This function is called, when the object is being hit with a railgun shot.
     */
    @Override
    void railgunHit() {
        // DO NOTHING
    }

    /**
     * Reset the position to the original position.
     */
    @Override
    void resetPosition() {
        super.resetPosition();
        collided = false;
    }
}

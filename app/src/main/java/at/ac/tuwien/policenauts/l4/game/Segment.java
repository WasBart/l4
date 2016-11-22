package at.ac.tuwien.policenauts.l4.game;

/**
 * A segment of a level, containing references to all visible objects.
 *
 * @author Michael Pucher
 */
class Segment {
    private final int backgroundTexture;

    /**
     * Initialize a segment of a level.
     *
     * @param backgroundTexture
     */
    Segment(int backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }
}

package at.ac.tuwien.policenauts.l4.game;

import java.util.ArrayList;
import java.util.List;

/**
 * A segment of a level, containing references to all visible objects.
 *
 * @author Michael Pucher
 */
class Segment {
    private final int backgroundTexture;
    private final List<NonPlayerObject> levelObjects = new ArrayList<>();

    /**
     * Initialize a segment of a level.
     *
     * @param backgroundTexture The background image of a level segment
     */
    Segment(int backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }

    /**
     * Get the list of non player objects on this segement.
     *
     * @return List of non player objects
     */
    List<NonPlayerObject> getObjects() {
        return levelObjects;
    }

    /**
     * Get the background texture of this segment.
     *
     * @return Background texture ID
     */
    int getBackgroundTexture() {
        return backgroundTexture;
    }
}

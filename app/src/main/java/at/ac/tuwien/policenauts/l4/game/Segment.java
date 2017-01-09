package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Rect;

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
    private final static Rect basePosition = new Rect(0, 0, ResolutionConverter.WIDTH,
            ResolutionConverter.HEIGHT);
    private final Rect position = new Rect(basePosition);

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

    /**
     * Reset the position to the base position.
     */
    void resetPosition() {
        position.set(basePosition);
        for (NonPlayerObject object : levelObjects)
            object.resetPosition();
    }

    /**
     * Reset the state of the game objects.
     */
    void resetObjects() {
        for (NonPlayerObject object : levelObjects)
            object.setVisible(true);
    }

    /**
     * Get the current segment position on the screen.
     *
     * @return Current segment (rendering) position
     */
    Rect currentPosition() {
        return position;
    }
}

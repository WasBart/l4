package at.ac.tuwien.policenauts.l4.game;

import java.util.List;

/**
 * Container for all level related information.
 *
 * @author Michael Pucher
 */
class Level {
    private final List<Segment> segments;

    /**
     * Initialize a level.
     *
     * @param segments The segments of a level
     */
    Level(List<Segment> segments) {
        this.segments = segments;
    }
}

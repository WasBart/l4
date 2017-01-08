package at.ac.tuwien.policenauts.l4.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for all level related information.
 *
 * @author Michael Pucher
 */
class Level {
    private final List<Segment> segments;
    private final int segmentLimit;
    private List<NonPlayerObject> currentObjects = new ArrayList<>(10);
    private List<NonPlayerObject> nextObjects = new ArrayList<>(10);
    private Segment currentSegment = null;
    private Segment nextSegment = null;
    private int nextSegmentId = 0;
    private float currentMovementSpeed = 0.1f;

    /**
     * Initialize a level.
     *
     * @param segments The segments of a level
     */
    Level(List<Segment> segments) {
        this.segments = segments;
        segmentLimit = segments.size() + 1;
    }

    /**
     * Start the level, initialize starting conditions.
     */
    void startLevel() {
        resetLevel();
        nextSegmentId = 0;
        loadSegment(0, true);
        loadSegment(1, false);
    }

    /**
     * Reset all level logic stats.
     */
    void resetLevel() {
        for (Segment segment : segments)
            segment.resetPosition();
    }

    /**
     * Load a new segment, discarding an old one and moving another one back.
     *
     * @param segmentId Identifier of the segment
     * @param first Check, whether the segment ist the first segment (don't apply offset)
     */
    private void loadSegment(int segmentId, boolean first) {
        nextSegmentId = segmentId + 1;

        // Swap object lists for inserting new segment
        List<NonPlayerObject> oldObjects = currentObjects;
        currentObjects = nextObjects;
	    nextObjects = oldObjects;
        currentSegment = nextSegment;

        // Check if segment was already the last one in the list
        if (nextSegmentId == segmentLimit) {
            nextSegment = null;
            nextObjects.clear();
            return;
        }

        // Add new segment and load objects into list
        nextSegment = segments.get(segmentId);
        nextObjects.clear();
        nextObjects.addAll(nextSegment.getObjects());

        // Don't apply offset if this is the first segment
        if (first)
            return;

        // Reset positions and add offsets to segment objects
        nextSegment.currentPosition().offset(ResolutionConverter.WIDTH, 0);
        for (NonPlayerObject obj : nextObjects)
            obj.currentPosition().offset(ResolutionConverter.WIDTH, 0);
    }

    /**
     * Update all (visible) level objects.
     *
     * @param tpf Time per frame in millseconds
     */
    void updateLevel(float tpf) {
        int segmentsLeft = Math.min(1, segmentLimit - nextSegmentId);
        float independentMovement = tpf * currentMovementSpeed * segmentsLeft;

        // Work on the current segment
        for (NonPlayerObject obj : currentObjects)
            obj.update(tpf, independentMovement);
        currentSegment.currentPosition().offset(- (int) independentMovement, 0);

        // Skip next segment if null
        if (nextSegment == null)
            return;

        // Work on the next segment
        for (NonPlayerObject obj : nextObjects)
            obj.update(tpf, independentMovement);
        nextSegment.currentPosition().offset(- (int) independentMovement, 0);

        // Check whether the current segment is still visible
        if (currentSegment.currentPosition().right <= 0)
            loadSegment(nextSegmentId, false);
    }

    /**
     * Render a level, using the texture manager.
     *
     * @param textureManager Texture manager, needs to have the canvas aquired already
     */
    void renderLevel(TextureManager textureManager) {
        // Draw level backgrounds
        textureManager.drawTexture(currentSegment.getBackgroundTexture(),
                currentSegment.currentPosition());
        if (nextSegment != null) {
            textureManager.drawTexture(nextSegment.getBackgroundTexture(),
                    nextSegment.currentPosition());
        }

        // Draw object sprites
        for (NonPlayerObject obj : currentObjects)
            textureManager.drawSprite(obj.currentSprite(), obj.currentPosition());
        for (NonPlayerObject obj : nextObjects)
            textureManager.drawSprite(obj.currentSprite(), obj.currentPosition());
    }
}

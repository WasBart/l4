package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Rect;
import android.util.Log;

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
    private final Player player;
    private final Rect playerStart;
    private final Sprite railgunSprite = new Sprite(1, 6, 50);
    private final Rect railgunPos = new Rect(0, 0, ResolutionConverter.WIDTH, 0);
    private float railgunTime = 0.0f;
    private List<NonPlayerObject> currentObjects = new ArrayList<>(10);
    private List<NonPlayerObject> nextObjects = new ArrayList<>(10);
    private Segment currentSegment = null;
    private Segment nextSegment = null;
    private int nextSegmentId = 0;
    private int segmentsLeft;

    /**
     * Initialize a level.
     *
     * @param segments The segments of a level
     * @param player The player
     * @param playerStart The starting position of the player
     */
    Level(List<Segment> segments, Player player, Rect playerStart) {
        this.segments = segments;
        this.player = player;
        this.playerStart = playerStart;
        segmentLimit = segments.size() + 1;
    }

    /**
     * Start the level, initialize starting conditions.
     */
    void restartLevel() {
        // Initialize segments
        nextSegmentId = 0;
        segmentsLeft = Math.min(1, segmentLimit - nextSegmentId);
        loadSegment(0, true);
        loadSegment(1, false);

        // Initialize player
        player.setOriginalPosition(playerStart.left, playerStart.top);
        player.resetPosition();
        player.resetStats();
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
        nextSegment.resetPosition();
        nextSegment.resetObjects();
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
        segmentsLeft = Math.min(1, segmentLimit - nextSegmentId);
        final float speedUp = (player.currentPosition().left /
                (float) ResolutionConverter.WIDTH) * segmentsLeft;
        final float baseMovement = tpf * (0.1f + 0.7f * speedUp);
        final float independentMovement = baseMovement * segmentsLeft;
        final float playerMovement = baseMovement * (1 - segmentsLeft);

        // Work on the current segment
        for (NonPlayerObject obj : currentObjects)
            obj.update(tpf, independentMovement);
        currentSegment.currentPosition().offset(- (int) independentMovement, 0);

        // Skip next segment if null
        if (nextSegment != null) {
            // Work on the next segment
            for (NonPlayerObject obj : nextObjects)
                obj.update(tpf, independentMovement);
            nextSegment.currentPosition().offset(-(int) independentMovement, 0);

            // Check whether the current segment is still visible
            if (currentSegment.currentPosition().right <= 0)
                loadSegment(nextSegmentId, false);
        }

        // Update the player
        player.update(tpf, playerMovement);

        // Process collision detection events
        for (NonPlayerObject obj : currentObjects) {
            if (obj.isVisible() && Rect.intersects(obj.currentPosition(),player.currentPosition())) {
                if (!player.isInvincible())
                    obj.applyEffect(player);
                obj.collisionEffect();
            }
        }

        // Skip next segment if null
        if (nextSegment != null) {
            // Work on the next segment
            for (NonPlayerObject obj : nextObjects) {
                if (obj.isVisible() && Rect.intersects(obj.currentPosition(),player.currentPosition())) {
                    if (!player.isInvincible())
                        obj.applyEffect(player);
                    obj.collisionEffect();
                }
            }
        }

        // Update railgun
        if (railgunTime > 0.0f) {
            railgunSprite.update(tpf);
            railgunPos.left += independentMovement;
            railgunTime = Math.max(0.0f, railgunTime - tpf);

            // Process collision detection events
            for (NonPlayerObject obj : currentObjects) {
                if (obj.isVisible() && Rect.intersects(obj.currentPosition(),railgunPos))
                    obj.railgunHit();
            }

            // Skip next segment if null
            if (nextSegment != null) {
                // Work on the next segment
                for (NonPlayerObject obj : nextObjects) {
                    if (obj.isVisible() && Rect.intersects(obj.currentPosition(),railgunPos))
                        obj.railgunHit();
                }
            }
        }
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
        for (NonPlayerObject obj : currentObjects) {
            if (obj.isVisible())
                textureManager.drawSprite(obj.currentSprite(), obj.currentPosition());
        }
        for (NonPlayerObject obj : nextObjects) {
            if (obj.isVisible())
                textureManager.drawSprite(obj.currentSprite(), obj.currentPosition());
        }

        // Draw the player
        Log.e("TEST", player.currentSprite().textureId + " " + player.currentSprite().currentFrame);
        textureManager.drawSprite(player.currentSprite(), player.currentPosition());

        // Draw the railgun shot
        if (railgunTime > 0.0f)
            textureManager.drawSprite(railgunSprite, railgunPos);
    }

    /**
     * Process a railgun shot.
     */
    void railgunShot() {
        // Decrease railgun ammo
        if (player.getRailgunAmmo() == 0)
            return;
        player.changeRailgunAmmo(-1);

        // Add railgun shot timer
        railgunTime = 400.0f;
        railgunSprite.reset();
        railgunPos.left = player.currentPosition().right + 5;
        railgunPos.top = player.currentPosition().centerY() - 18;
        railgunPos.bottom = player.currentPosition().centerY() - 8;
    }

    /**
     * Check, whether there are any segments left in the level.
     *
     * @return True, if there are segments left
     */
    boolean isOver() {
        return segmentsLeft == 0;
    }
}

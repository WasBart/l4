package at.ac.tuwien.policenauts.l4.game;

/**
 * Sprite class, store information about sprite rendering.
 *
 * @author Michael Pucher
 */
class Sprite {
    private final int textureId;
    private final int frameCount;
    private int currentFrame = 0;
    private float delta = 0.0f;

    /**
     * Initialize a sprite.
     *
     * @param textureId Id in the texture manager
     * @param frameCount The number of frames on the spritesheet
     */
    public Sprite(int textureId, int frameCount) {
        this.textureId = textureId;
        this.frameCount = frameCount;
    }

    /**
     * Update the frame, according to delta from game loop.
     *
     * @param tpf Time per frame (in milliseconds) from the game loop
     */
    public void update(float tpf) {
        this.delta += delta;
        if (this.delta > 1) {
            this.delta = 0.0f;

            // Update frame
            currentFrame = (currentFrame + 1) % frameCount;
        }
    }
}

package at.ac.tuwien.policenauts.l4.game;

/**
 * Sprite class, store information about sprite rendering.
 *
 * @author Michael Pucher
 */
class Sprite {
    final int textureId;
    int currentFrame = 0;

    private final int frameCount;
    private final float frameDuration;
    private float delta = 0.0f;

    /**
     * Initialize a sprite.
     *
     * @param textureId Id in the texture manager
     * @param frameCount The number of frames on the spritesheet
     * @param duration Duration of one cycle in millisecond
     */
    public Sprite(int textureId, int frameCount, int duration) {
        this.textureId = textureId;
        this.frameCount = frameCount;
        this.frameDuration = duration / (float) frameCount;
    }

    /**
     * Update the frame, according to delta from game loop.
     *
     * @param tpf Time per frame (in milliseconds) from the game loop
     */
    public void update(float tpf) {
        delta += tpf;
        if (delta > frameDuration) {
            delta = 0.0f;

            // Update frame
            currentFrame = (currentFrame + 1) % frameCount;
        }
    }
}

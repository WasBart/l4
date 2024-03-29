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
    private final int frameDuration;
    private float delta = 0.0f;

    /**
     * Initialize a sprite.
     *
     * @param textureId Id in the texture manager
     * @param frameCount The number of frames on the spritesheet
     * @param duration Duration of a frame in milliseconds
     */
    Sprite(int textureId, int frameCount, int duration) {
        this.textureId = textureId;
        this.frameCount = frameCount;
        this.frameDuration = duration;
    }

    /**
     * Update the frame, according to delta from game loop.
     *
     * @param tpf Time per frame (in milliseconds) from the game loop
     */
    void update(float tpf) {
        delta += tpf;

        // Pick next frame
        currentFrame = (currentFrame + (int) (delta / frameDuration)) % frameCount;
        delta %= frameDuration;
    }

    /**
     * Reset the sprite animation.
     */
    void reset() {
        delta = 0.0f;
    }
}

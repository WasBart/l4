package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Rect;

/**
 * Helper class for using resolution independent pixel calculation.
 *
 * @author Michael Pucher
 */
public class ResolutionConverter {
    static final int WIDTH = 1600;
    static final int HEIGHT = 1000;
    private int actualWidth;
    private int actualHeight;
    private float densityFactor;

    /**
     * Set the actual width and height of the drawing canvas.
     *
     * @param width Width of the canvas
     * @param height Height of the canvas
     */
    public void setDimensions(int width, int height) {
        actualWidth = width;
        actualHeight = height;
    }

    /**
     * Set density factor for density independent pixel calculations.
     *
     * @param scale Density scale as optained from the context
     */
    public void setDensity(float scale) {
        final float GESTURE_THRESHOLD_DIP = 16.0f;
        this.densityFactor = (GESTURE_THRESHOLD_DIP * scale + 0.5f) / 30.0f;
    }

    /**
     * Convert a src rectangle in the independent resolution to the actual resolution.
     *
     * @param source Source rectangle in independent resolution
     * @param target Target rectangle on screen
     */
    void toScreenRect(Rect source, Rect target) {
        int left = source.left * actualWidth / WIDTH;
        int top = source.top * actualHeight / HEIGHT;
        int right = source.right * actualWidth / WIDTH;
        int bottom = source.bottom * actualHeight / HEIGHT;
        target.set(left, top, right, bottom);
    }

    /**
     * Get the density factor for one pixel.
     *
     * @return 1px in pixel independent scaling
     */
    float density() {
        return densityFactor;
    }

    /**
     * Factor for x coordinate conversion
     *
     * @return Factor for x coordinate conversion
     */
    float factorX() {
        return WIDTH / (float)actualWidth;
    }

    /**
     * Factor for y coordinate conversion
     *
     * @return Factor for y coordinate conversion
     */
    float factorY() {
        return HEIGHT / (float)actualHeight;
    }
}

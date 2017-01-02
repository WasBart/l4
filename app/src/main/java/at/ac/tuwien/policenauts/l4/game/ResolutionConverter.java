package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Rect;

/**
 * Helper class for using resolution independent pixel calculation.
 *
 * @author Michael Pucher
 */
public class ResolutionConverter {
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 1000;
    private int actualWidth;
    private int actualHeight;

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
}

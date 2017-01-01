package at.ac.tuwien.policenauts.l4.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.List;

import at.ac.tuwien.policenauts.l4.R;

/**
 * Load and store textures for reuse across objects.
 *
 * @author Michael Pucher
 */
class TextureManager {
    /**
     * Initialize the texture manager with a context.
     *
     * @param context The application context
     */
    TextureManager(Context context) {
        this.context = context;
    }

    /**
     * Assign a canvas to the texture manager for drawing.
     *
     * @param canvas Canvas to be assigned (needs to be locked by calling thread)
     */
    void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    /**
     * Load a list of sprites and a list of textures from the resources.
     *
     * @param sprites A list of sprite resource names
     * @param spriteFrameCount A list containing the number of frames for each sprite
     * @param textureList A list of texture resource names
     * @return True if everything loaded successfully, False otherwise
     */
    boolean loadTextures(List<String> sprites, List<Integer> spriteFrameCount,
                         List<String> textureList) {
        // Load sprites
        if (sprites != null && sprites.size() > 0) {
            // Initialize data structures
            spriteSheets = new Bitmap[sprites.size()];
            spriteFrameWidth = new int[sprites.size()];
            spriteFrameRects = new Rect[sprites.size()];

            // Load each sprite
            for (int i = 0; i < sprites.size(); i++) {
                // Find the resource ID of the drawable
                int drawableId;
                try {
                    drawableId = res.getField(sprites.get(i)).getInt(null);
                } catch (Exception ex) {
                    Log.e(TAG, "Could not get drawable ID of " + sprites.get(i), ex);
                    return false;
                }

                // Load bitmap and store it
                spriteSheets[i] = BitmapFactory.decodeResource(context.getResources(), drawableId);
                spriteFrameWidth[i] = spriteSheets[i].getWidth() / spriteFrameCount.get(i);
                spriteFrameRects[i] = new Rect(0, 0, spriteFrameWidth[i],
                        spriteSheets[i].getHeight());
            }
        }

        // Load textures
        if (textureList != null && textureList.size() > 0) {
            // Initialize data structures
            textures = new Bitmap[textureList.size()];
            textureSrcRect = new Rect[textureList.size()];

            // Load each texture
            for (int i = 0; i < textureList.size(); i++) {
                // Find the resource ID of the drawable
                int drawableId;
                try {
                    drawableId = res.getField(textureList.get(i)).getInt(null);
                } catch (Exception ex) {
                    Log.e(TAG, "Could not get drawable ID of " + textureList.get(i), ex);
                    return false;
                }

                // Load bitmap and store it
                textures[i] = BitmapFactory.decodeResource(context.getResources(), drawableId);
                textureSrcRect[i] = new Rect(0, 0, textures[i].getWidth(), textures[i].getHeight());
            }
        }
        return true;
    }

    /**
     * Draw a sprite from a spritesheet.
     *
     * @param sprite Sprite to be drawn on the canvas
     * @param rect Target rectangle for bitmap drawing
     */
    void drawSprite(Sprite sprite, Rect rect) {
        Rect src = spriteFrameRects[sprite.textureId];
        src.offsetTo(sprite.currentFrame * spriteFrameWidth[sprite.textureId], 0);
        canvas.drawBitmap(spriteSheets[sprite.textureId], src, rect, defaultPaint);
    }

    /**
     * Draw a stored texture.
     *
     * @param index Texture index
     * @param rect Target rectangle for bitmap drawing
     */
    void drawTexture(int index, Rect rect) {
        canvas.drawBitmap(textures[index], textureSrcRect[index], rect, defaultPaint);
    }

    /**
     * Remove all textures from memory and reset texture counter.
     */
    void unloadTextures() {
        // Free all sprite sheets and textures
        for (Bitmap bm : spriteSheets)
                bm.recycle();
        for (Bitmap tex : textures)
                tex.recycle();
        
        // Reset all arrays
        spriteSheets = null;
        spriteFrameWidth = null;
        spriteFrameRects = null;
        textures = null;
        textureSrcRect = null;
    }

    // Private members
    private static final String TAG = "TextureManager";
    private final Context context;

    // Store class for dynamic resource loading through reflection
    private final Class res = R.drawable.class;

    // Sprite storage
    private Bitmap[] spriteSheets = null;
    private int[] spriteFrameWidth = null;
    private Rect[] spriteFrameRects = null;

    // Texture storage
    private Bitmap[] textures = null;
    private Rect[] textureSrcRect = null;

    // Information needed for drawing
    private final Paint defaultPaint = new Paint();
    private Canvas canvas = null;
}

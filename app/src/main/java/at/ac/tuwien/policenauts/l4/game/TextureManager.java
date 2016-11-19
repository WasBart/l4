package at.ac.tuwien.policenauts.l4.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.util.List;

import at.ac.tuwien.policenauts.l4.R;

/**
 * Load and store textures for reuse accross objects.
 *
 * @author Michael Pucher
 */
class TextureManager {
    enum TextureManagerState {
        LOADED, NOT_LOADED
    }

    /**
     * Initialize the texture manager with a context.
     *
     * @param context The application context
     */
    TextureManager(Context context) {
        this.context = context;
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
            spriteFrameHeight = new int[sprites.size()];

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
                spriteFrameHeight[i] = spriteSheets[i].getHeight();
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
     * Remove all textures from memory and reset texture counter.
     */
    void unloadTextures() {
        // Don't unload anything if no textures have been loaded
        if (state == TextureManagerState.NOT_LOADED)
            return;

        // Reset all arrays
        spriteSheets = null;
        spriteFrameWidth = null;
        spriteFrameHeight = null;
        textures = null;
        textureSrcRect = null;
        state = TextureManagerState.NOT_LOADED;
    }

    /**
     * Retrieve state of the texture manager.
     *
     * @return LOADED if textures are currently loaded, NOT_LOADED otherwise
     */
    TextureManagerState getState() {
        return state;
    }

    // Private members
    private static final String TAG = "TextureManager";
    private final Context context;
    private TextureManagerState state = TextureManagerState.NOT_LOADED;

    // Store class for dynamic resource loading through reflection
    private final Class res = R.drawable.class;

    // Sprite storage
    private Bitmap[] spriteSheets = null;
    private int[] spriteFrameWidth = null;
    private int[] spriteFrameHeight = null;

    // Texture storage
    private Bitmap[] textures = null;
    private Rect[] textureSrcRect = null;
}

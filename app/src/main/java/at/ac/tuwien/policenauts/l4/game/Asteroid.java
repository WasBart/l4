package at.ac.tuwien.policenauts.l4.game;

import android.graphics.Rect;

/**
 * The asteroid class, representing an asteroid game object.
 *
 * @author Michael Pucher
 */
abstract class Asteroid extends NonPlayerObject {
    private static final Rect SIZE = new Rect(150, 169, 0, 0);
    /**
     * The size of this object in indepdentent pixels
     *
     * @return The sizes of the object stored as Rect
     */
    @Override
    Rect size() {
        return SIZE;
    }
}

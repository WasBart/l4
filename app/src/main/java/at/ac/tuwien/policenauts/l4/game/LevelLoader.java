package at.ac.tuwien.policenauts.l4.game;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Load a level from a specified xml file.
 *
 * @author Michael Pucher
 */
class LevelLoader {
    /**
     * Load a level, identified by a name.
     *
     * @param level The name/identifier of a level
     * @return A level object with complete information about the level
     */
    public Level loadLevel(String level) {
        return null;
    }

    /**
     * Parse a level file (top level) and create segments/game objects.
     *
     * @param parser The XML parser for the specified resource
     * @throws XmlPullParserException if there was an exception during parsing
     * @throws IOException if there was an exception while reading the resource file
     */
    public void readLevel(XmlPullParser parser) throws XmlPullParserException, IOException {
        // Make sure the xml file starts with a level tag
        parser.require(XmlPullParser.START_TAG, null, "level");

        // Loop till end tag
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            // Check if keyword is any of the top level words
            switch (parser.getName()) {
                case "player":
                    break;
                case "background-images":
                    break;
                case "sprites":
                    break;
                case "background-music":
                    break;
                case "sounds":
                    break;
                case "segment":
                    break;
                default:
                    // Skip other tags
            }
        }
    }
}

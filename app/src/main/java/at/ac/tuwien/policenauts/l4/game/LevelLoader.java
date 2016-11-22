package at.ac.tuwien.policenauts.l4.game;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private void readLevel(XmlPullParser parser) throws XmlPullParserException, IOException {
        // Make sure the xml file starts with a level tag
        parser.require(XmlPullParser.START_TAG, null, "level");

        // Loop till end tag
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            // Check if keyword is any of the top level words
            switch (parser.getName()) {
                case "player":
                    skipTag(parser);
                    break;
                case "background-images":
                    backgroundImages(parser);
                    break;
                case "sprites":
                    sprites(parser);
                    break;
                case "background-music":
                    backgroundMusic(parser);
                    break;
                case "sounds":
                    sounds(parser);
                case "segment":
                    segment(parser);
                    break;
                default:
                    skipTag(parser);
            }
        }
    }

    /**
     * Parse a segment for the game objects inside.
     *
     * @param parser The XML parser for the specified resource
     * @throws XmlPullParserException if there was an exception during parsing
     * @throws IOException if there was an exception while reading the resource file
     */
    private void segment(XmlPullParser parser) throws XmlPullParserException, IOException {
        // Make sure the xml file starts with a level tag
        parser.require(XmlPullParser.START_TAG, null, "segment");

        // Create new segment with image id
        Segment segment;
        String imageId = parser.getAttributeValue(null, "image-id");
        try {
            segment = new Segment(Integer.parseInt(imageId));
        } catch (NumberFormatException ex) {
            throw new XmlPullParserException("Invalid image-id in segment: " + imageId);
        }
        segments.add(segment);

        // Loop till end tag
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            // Check if keyword is any of the top level words
            switch (parser.getName()) {
                case "asteroid":
                    skipTag(parser);
                    break;
                case "collectible":
                    skipTag(parser);
                    break;
                default:
                    skipTag(parser);
            }
        }
    }

    /**
     * Read the background-image area.
     *
     * @param parser The parser with the input level stream
     * @throws XmlPullParserException if there was an exception during parsing
     * @throws IOException if there was an exception while reading the resource file
     */
    private void backgroundImages(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "background-images");

        // Loop till end tag
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            // Skip invalid elements
            if (!parser.getName().equals("image"))
                continue;
            textures.add(parser.getAttributeValue(null, "src"));
        }
    }

    /**
     * Read the background-music area.
     *
     * @param parser The parser with the input level stream
     * @throws XmlPullParserException if there was an exception during parsing
     * @throws IOException if there was an exception while reading the resource file
     */
    private void backgroundMusic(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "background-music");

        // Loop till end tag
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            // Skip invalid elements
            if (!parser.getName().equals("soundfile"))
                continue;
            backgroundMusic.add(parser.getAttributeValue(null, "src"));
        }
    }

    /**
     * Read all sounds from the level file.
     *
     * @param parser The parser with the input level stream
     * @throws XmlPullParserException if there was an exception during parsing
     * @throws IOException if there was an exception while reading the resource file
     */
    private void sounds(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "sounds");

        // Loop till end tag
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            // Skip invalid elements
            if (!parser.getName().equals("soundfile"))
                continue;
            sounds.add(parser.getAttributeValue(null, "src"));
        }
    }

    /**
     * Read the sprite area.
     *
     * @param parser The parser with the input level stream
     * @throws XmlPullParserException if there was an exception during parsing
     * @throws IOException if there was an exception while reading the resource file
     */
    private void sprites(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "sprites");

        // Loop till end tag
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            // Skip invalid elements
            if (!parser.getName().equals("sheet"))
                continue;

            // Get sprite name and deduce frame count
            String name = parser.getAttributeValue(null, "src");
            sprites.add(name);

            // Deduce frame count from name
            String[] parts = name.split("_");
            try {
                spritesFrameCount.add(Integer.parseInt(parts[parts.length - 1]));
            } catch (NumberFormatException ex) {
                throw new XmlPullParserException("Could not parse frame count: " + name);
            }
        }
    }

    /**
     * Skip unwanted tags.
     *
     * @param parser The parser with the input level stream
     * @throws XmlPullParserException if there was an exception during parsing
     * @throws IOException if there was an exception while reading the resource file
     */
    private void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG)
            throw new XmlPullParserException("Invalid parser state");

        // Consume the tag as a whole
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth -= 1;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    // State needed for parsing
    private List<String> sprites = new ArrayList<>();
    private List<Integer> spritesFrameCount = new ArrayList<>();
    private List<String> textures = new ArrayList<>();
    private List<String> sounds = new ArrayList<>();
    private List<String> backgroundMusic = new ArrayList<>();
    private List<Segment> segments = new ArrayList<>();
}

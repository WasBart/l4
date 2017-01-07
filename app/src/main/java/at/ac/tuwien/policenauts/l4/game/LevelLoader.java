package at.ac.tuwien.policenauts.l4.game;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.policenauts.l4.R;

/**
 * Load a level from a specified xml file.
 *
 * @author Michael Pucher
 */
class LevelLoader {
    /**
     * Initialize the level loader with the application context.
     *
     * @param context Application context
     * @param textureManager Texture manager of this application
     */
    LevelLoader(Context context, TextureManager textureManager) {
        this.context = context;
        this.textureManager = textureManager;

        // Resources that stay the same across all levels
        textures.add("audio_icon");
        textures.add("pause_icon");
        textureOffset = textures.size();

        // Load the resources we want to load
        loadLevel("level01");
        loadResources();
    }

    /**
     * Return a loaded level by its id
     *
     * @param levelId Id of the level to be returned
     * @return Level corresponding to the passed id
     */
    Level getLevel(int levelId) {
        return levels.get(levelId);
    }

    /**
     * Load a level, identified by a name.
     *
     * @param level The name/identifier of a level
     */
    private void loadLevel(String level) {
        // Get the level resource ID
        int resId;
        try {
            resId = res.getField(level).getInt(null);
        } catch (Exception ex) {
            Log.e(TAG, "Could not get resource ID of " + level, ex);
            return;
        }

        // Open the xml file and parse the level
        try {
            readLevel(context.getResources().getXml(resId));
        } catch (Exception ex) {
            Log.e(TAG, "Could not load the level " + level, ex);
            return;
        }

        // Use the state to create the level object
        levels.add(new Level(new ArrayList<>(segments)));

        // Add offsets for loaded resources
        segments.clear();
        spriteOffset = sprites.size();
        textureOffset = textures.size();
        soundOffset = sounds.size();
        backgroundMusicOffset = backgroundMusic.size();
    }

    /**
     * Load resources for all levels using texture and sound manager.
     *
     * @return False in case of error
     */
    boolean loadResources() {
        // Use the texture manager to load sprites and textures
        if (!textureManager.loadTextures(sprites, spritesFrameCount, textures))
            return false;

        // Clear the state
        sprites.clear();
        spritesFrameCount.clear();
        spritesDuration.clear();
        textures.clear();
        sounds.clear();
        backgroundMusic.clear();

        // Add offsets for loaded resources
        spriteOffset = sprites.size();
        textureOffset = textures.size();
        soundOffset = sounds.size();
        backgroundMusicOffset = backgroundMusic.size();
        return true;
    }

    /**
     * Parse a level file (top level) and create segments/game objects.
     *
     * @param parser The XML parser for the specified resource
     * @throws XmlPullParserException if there was an exception during parsing
     * @throws IOException if there was an exception while reading the resource file
     */
    private void readLevel(XmlPullParser parser) throws XmlPullParserException, IOException {
        // Skip the first two tags
        parser.next();
        parser.next();

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
                    readBackgroundMusic(parser);
                    break;
                case "sounds":
                    sounds(parser);
                    break;
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
            segment = new Segment(Integer.parseInt(imageId) + textureOffset);
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
                    segment.getObjects().add(readAsteroid(parser));
                    break;
                case "collectible":
                    segment.getObjects().add(readCollectible(parser));
                    break;
                default:
                    skipTag(parser);
            }
        }
    }

    /**
     * Read an asteroid from the level file
     *
     * @param parser The parser with the input level stream
     * @throws XmlPullParserException if there was an exception during parsing
     * @throws IOException if there was an exception while reading the resource file
     */
    private NonPlayerObject readAsteroid(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        // Make sure the xml file starts with a level tag
        parser.require(XmlPullParser.START_TAG, null, "asteroid");

        // Find the type of the asteroid
        String type = parser.getAttributeValue(null, "type");
        List<Sprite> spriteIds = new ArrayList<>(1);
        int collisionSound = 0;
        int xPosition = 0;
        int yPosition = 0;

        // Loop till end tag
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            // Check if keyword is any of the top level words
            switch (parser.getName()) {
                case "sprite":
                    try {
                        int spriteId = Integer.parseInt(parser.getAttributeValue(null, "id")) +
                                        spriteOffset;
                        spriteIds.add(new Sprite(spriteId, spritesFrameCount.get(spriteId),
                                spritesDuration.get(spriteId)));
                        parser.next();
                    } catch (NumberFormatException ex) {
                        throw new IOException("Could not parse sprite-id");
                    }
                    break;
                case "collision":
                    try {
                        collisionSound = Integer.parseInt(parser.getAttributeValue(null, "sound") +
                                soundOffset);
                        parser.next();
                    } catch (NumberFormatException ex) {
                        throw new IOException("Could not parse collision sound");
                    }
                    break;
                case "position":
                    try {
                        xPosition = Integer.parseInt(parser.getAttributeValue(null, "x"));
                        yPosition = Integer.parseInt(parser.getAttributeValue(null, "y"));
                        parser.next();
                    } catch (NumberFormatException ex) {
                        throw new IOException("Could not parse position");
                    }
                    break;
                default:
                    skipTag(parser);
            }
        }

        // Build object from the given information
        NonPlayerObject asteroid;
        switch (type) {
            case "simple":
                asteroid = new Oxygen();
                break;
            case "indestructible":
                asteroid = new Oxygen();
                break;
            case "splitting":
            default:
                asteroid = new Oxygen();
                break;
        }
        asteroid.addSprites(spriteIds);
        return asteroid;
    }

    /**
     * Read a collectible from the level file
     *
     * @param parser The parser with the input level stream
     * @throws XmlPullParserException if there was an exception during parsing
     * @throws IOException if there was an exception while reading the resource file
     */
    private NonPlayerObject readCollectible(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        // Make sure the xml file starts with a level tag
        parser.require(XmlPullParser.START_TAG, null, "collectible");

        // Find the type of the asteroid
        String type = parser.getAttributeValue(null, "type");
        List<Sprite> spriteIds = new ArrayList<>();
        int collisionSound = 0;
        int xPosition = 0;
        int yPosition = 0;

        // Loop till end tag
        while (parser.next() != XmlPullParser.END_TAG) {
            // Check if keyword is any of the top level words
            switch (parser.getName()) {
                case "sprite":
                    try {
                        int spriteId = Integer.parseInt(parser.getAttributeValue(null, "id")) +
                                spriteOffset;
                        spriteIds.add(new Sprite(spriteId, spritesFrameCount.get(spriteId),
                                spritesDuration.get(spriteId)));
                        parser.next();
                    } catch (NumberFormatException ex) {
                        throw new IOException("Could not parse sprite-id");
                    }
                    break;
                case "collision":
                    try {
                        collisionSound = Integer.parseInt(parser.getAttributeValue(null, "sound") +
                                soundOffset);
                        parser.next();
                    } catch (NumberFormatException ex) {
                        throw new IOException("Could not parse collision sound");
                    }
                    break;
                case "position":
                    try {
                        xPosition = Integer.parseInt(parser.getAttributeValue(null, "x"));
                        yPosition = Integer.parseInt(parser.getAttributeValue(null, "y"));
                        parser.next();
                    } catch (NumberFormatException ex) {
                        throw new IOException("Could not parse position");
                    }
                    break;
                default:
                    skipTag(parser);
            }
        }

        // Build object from the given information
        NonPlayerObject collectible;
        switch (type) {
            case "invincibility":
                collectible = new Invincibility();
                break;
            case "ammo":
                collectible = new RailgunAmmo();
                break;
            case "oxygen":
            default:
                collectible = new Oxygen();
                break;
        }

        // Add position to object
        collectible.setOriginalPosition(xPosition, yPosition);
        collectible.addSprites(spriteIds);
        return collectible;
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

            // Skip closing tag
            parser.next();
        }
    }

    /**
     * Read the background-music area.
     *
     * @param parser The parser with the input level stream
     * @throws XmlPullParserException if there was an exception during parsing
     * @throws IOException if there was an exception while reading the resource file
     */
    private void readBackgroundMusic(XmlPullParser parser) throws XmlPullParserException,
            IOException {
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

            // Get sprite frame duration
            String duration = parser.getAttributeValue(null, "duration");
            try {
                spritesDuration.add(Integer.parseInt(duration));
            } catch (NumberFormatException ex) {
                throw new XmlPullParserException("Could not parse frame duration: " + name);
            }

            // Consume ending tag
            parser.next();
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

    // Private members
    private static final String TAG = "LevelLoader";
    private final Context context;
    private final TextureManager textureManager;

    // Store class for dynamic resource loading through reflection
    private final Class res = R.xml.class;

    // State needed for parsing
    private final List<String> sprites = new ArrayList<>();
    private final List<Integer> spritesFrameCount = new ArrayList<>();
    private final List<Integer> spritesDuration = new ArrayList<>();
    private final List<String> textures = new ArrayList<>();
    private final List<String> sounds = new ArrayList<>();
    private final List<String> backgroundMusic = new ArrayList<>();
    private final List<Segment> segments = new ArrayList<>();

    // Offsets for resource parsing
    private int spriteOffset = 0;
    private int textureOffset = 0;
    private int soundOffset = 0;
    private int backgroundMusicOffset = 0;

    // State as needed for game logic
    private final List<Level> levels = new ArrayList<>(2);
}

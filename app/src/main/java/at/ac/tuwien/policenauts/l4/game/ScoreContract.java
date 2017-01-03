package at.ac.tuwien.policenauts.l4.game;


import android.provider.BaseColumns;

/**
 * Contract class containing definitions of constants for the db.
 *
 * @author Wassily Bartuska
 */
public class ScoreContract implements BaseColumns {

    /**
     * Provides constants for an entry in Score table.
     */
    public static abstract class ScoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "score";

        public static final String COLUMN_TYPE_ID = "INTEGER PRIMARY KEY";

        public static final String COLUMN_NAME_PLAYERNAME = "playername";
        public static final String COLUMN_TYPE_PLAYERNAME = "TEXT";

        public static final String COLUMN_NAME_SCORE = "score";
        public static final String COLUMN_TYPE_SCORE = "INTEGER";
    }
}

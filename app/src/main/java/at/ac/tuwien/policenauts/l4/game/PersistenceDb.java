package at.ac.tuwien.policenauts.l4.game;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import at.ac.tuwien.policenauts.l4.game.ScoreContract.ScoreEntry;


/**
 * Class handling persistence layer database access. Adapted from the lecture.
 *
 * @author Wassily Bartuska
 */
public class PersistenceDb extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GameScore.db";

    private final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS" + ScoreEntry.TABLE_NAME +
            " (" + ScoreEntry._ID + " " + ScoreEntry.COLUMN_TYPE_ID + " autoincrement," +
            ScoreEntry.COLUMN_NAME_PLAYERNAME + " " + ScoreEntry.COLUMN_TYPE_PLAYERNAME + "," +
            ScoreEntry.COLUMN_NAME_SCORE + " " + ScoreEntry.COLUMN_TYPE_SCORE + ");";

    private final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ScoreEntry.TABLE_NAME;

    /**
     * Constructor for the persistence database
     *
     * @param context context used.
     */
    public PersistenceDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Invoked when an instance is created.
     *
     * @param db database object used.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(PersistenceDb.class.getName(), "Creating tables in DB");
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * Invoked when database is upgraded.
     *
     * @param db database used
     * @param oldVersion oldVersion number of db.
     * @param newVersion newVersion number of db.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(PersistenceDb.class.getName(), "Upgrading database from version " +
                oldVersion + " to " + newVersion);
        dropTables(db);
        onCreate(db);
    }

    private void dropTables(SQLiteDatabase db) {
        Log.d(PersistenceDb.class.getName(), "Dropping all tables");
        db.execSQL(SQL_DELETE_ENTRIES);
    }
}

package at.ac.tuwien.policenauts.l4.android;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import at.ac.tuwien.policenauts.l4.R;
import at.ac.tuwien.policenauts.l4.game.ScoreContract;
import at.ac.tuwien.policenauts.l4.game.ScoreProvider;


/**
 * Activity handling Score View. Paraphrased from the lecture.
 *
 * @author Wassily Bartuska
 */
public class ScoreActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView scoreListView;
    private View progressView;
    private SimpleCursorAdapter cursorAdapter;

    private static final int DELETE_ID = Menu.FIRST + 1;

    /**
     * Invoked when an instance of the class is created.
     *
     * @param savedInstanceState Bundle object passed to this method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_score);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        scoreListView = (ListView) findViewById(R.id.scoreListView);
        progressView = findViewById(R.id.progressView);

        fillData();
        registerForContextMenu(scoreListView);
    }

    /**
     * Displays the progress.
     *
     * @param progress current progress.
     */
    private void showProgress(boolean progress) {
        scoreListView.setVisibility(progress ? View.GONE : View.VISIBLE);
        progressView.setVisibility(progress ? View.VISIBLE : View.GONE);
    }

    /**
     * Fills the data in.
     */
    private void fillData() {

        showProgress(true);

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] {ScoreContract.ScoreEntry.COLUMN_NAME_PLAYERNAME, ScoreContract.ScoreEntry.COLUMN_NAME_SCORE};
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.textViewName, R.id.textViewScore };

        //start a new loader or re-connect to existing one
        getLoaderManager().initLoader(0, null, this);
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item_score, null, from, to, 0);

        scoreListView.setAdapter(cursorAdapter);
    }

    /**
     * Invoked when context menu is created.
     *
     * @param menu menu created.
     * @param v view.
     * @param menuInfo info for the menu.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.Delete);
    }

    /**
     * Invoked when an item is selected.
     *
     * @param item selected item.
     * @return boolean flag.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                        .getMenuInfo();
                Uri uri = Uri.parse(ScoreProvider.CONTENT_URI + "/"
                        + info.id);
                getContentResolver().delete(uri, null, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Invoked when options menu is created.
     *
     * @param menu menu created.
     * @return boolean flag.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read_score, menu);
        return true;
    }

    /**
     * Invoked when an item is selected.
     *
     * @param item selected item.
     * @return boolean flag.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Invoked when a loader is created.
     *
     * @param id id of the loader.
     * @param args arguments used.
     * @return loader.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { ScoreContract.ScoreEntry._ID, ScoreContract.ScoreEntry.COLUMN_NAME_PLAYERNAME, ScoreContract.ScoreEntry.COLUMN_NAME_SCORE};
        CursorLoader cursorLoader = new CursorLoader(this,
                ScoreProvider.CONTENT_URI, projection, null, null, ScoreContract.ScoreEntry.COLUMN_NAME_SCORE + " DESC");
        return cursorLoader;
    }

    /**
     * Invoked when loading has finished.
     *
     * @param loader loader used for loading.
     * @param data data used.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);//notifies the SimpleCursorAdapter about new data
        showProgress(false);
    }

    /**
     * Invoked when the loader is reset.
     *
     * @param loader loader that is reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        cursorAdapter.swapCursor(null);
    }
}

package at.ac.tuwien.policenauts.l4.android;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import at.ac.tuwien.policenauts.l4.R;
import at.ac.tuwien.policenauts.l4.game.ScoreContract;
import at.ac.tuwien.policenauts.l4.game.ScoreProvider;


/**
 * Activity for creating a new HighScoreEntry. Taken from the lecture.
 *
 * @author Wassily Bartuska
 */
public class CreateHighScoreActivity extends AppCompatActivity {

    private Button addButton, readScore;
    private EditText nameEditText, scoreEditText;

    /**
     * Invoked when an instance of the class is created.
     *
     * @param savedInstanceState Bundle object passed to this method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_high_score);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        nameEditText = (EditText) findViewById(R.id.editText);
        scoreEditText = (EditText) findViewById(R.id.editText2);
        addButton = (Button) findViewById(R.id.button);
        readScore = (Button) findViewById(R.id.readScoreButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveScore();
            }
        });

        readScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readScores();
            }
        });

    }

    /**
     * Saving a score entry with the name and score.
     */
    private void saveScore() {
        String username = nameEditText.getText().toString();
        String scoreStr = scoreEditText.getText().toString();
        int score = Integer.parseInt(scoreStr);

        ContentValues values = new ContentValues();
        values.put(ScoreContract.ScoreEntry.COLUMN_NAME_PLAYERNAME, username);
        values.put(ScoreContract.ScoreEntry.COLUMN_NAME_SCORE, score);

        getContentResolver().insert(ScoreProvider.CONTENT_URI, values);
    }

    /**
     * Starts the score activity to read the scores.
     */
    private void readScores() {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }

    /**
     * Invoked when the optionsmenu is created.
     *
     * @param menu menu created
     * @return boolean flag
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_high_score, menu);
        return true;
    }

    /**
     * Invoked when an item is selected.
     *
     * @param item selected item
     * @return boolean flag
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
}

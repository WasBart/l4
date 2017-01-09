package at.ac.tuwien.policenauts.l4.android;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import at.ac.tuwien.policenauts.l4.R;
import at.ac.tuwien.policenauts.l4.game.Game;
import at.ac.tuwien.policenauts.l4.game.GameLoop;
import at.ac.tuwien.policenauts.l4.game.ScoreContract;
import at.ac.tuwien.policenauts.l4.game.ScoreProvider;

/**
 * Activity displayed when the player reaches the end of the game.
 *
 * @author Wassily Bartuska
 */
public class GameWonActivity extends AppCompatActivity {
    private Button addButton, readScore;
    private EditText nameEditText, scoreEditText;
    private float elapsedTime;

    /**
     * Invoked when an instance of the class is created.
     *
     * @param savedInstanceState Bundle object passed to this method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_won);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        TextView textView = (TextView) findViewById(R.id.game_won_view);
        AnimatorSet animatorSet= (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.wonpropertyanimation);
        animatorSet.setTarget(textView);
        animatorSet.start();

        Game game = ((GameApplication) getApplicationContext()).getGame();
        elapsedTime = game.getElapsedTime();

        TextView scoreView = (TextView) findViewById(R.id.score_view);
        scoreView.setText(getString(R.string.text_score) + " " + elapsedTime + " " + getString(R.string.sec_score));


        //Score buttons/text
        nameEditText = (EditText) findViewById(R.id.editText);
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
        float score = elapsedTime;

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
}

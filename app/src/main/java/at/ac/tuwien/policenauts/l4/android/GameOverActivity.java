package at.ac.tuwien.policenauts.l4.android;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import at.ac.tuwien.policenauts.l4.R;

/**
 * Activity displayed when the player reaches a game over.
 *
 * @author Wassily Bartuska
 */
public class GameOverActivity extends AppCompatActivity {

    /**
     * Invoked when an instance of the class is created.
     *
     * @param savedInstanceState Bundle object passed to this method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        TextView textView = (TextView) findViewById(R.id.game_over_view);
        AnimatorSet animatorSet= (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.propertyanimation);
        animatorSet.setTarget(textView);
        animatorSet.start();
    }
}

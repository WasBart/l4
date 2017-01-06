package at.ac.tuwien.policenauts.l4.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import at.ac.tuwien.policenauts.l4.R;

/**
 * The main menu of L4.
 *
 * @author Michael Pucher
 */
public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
    }
}

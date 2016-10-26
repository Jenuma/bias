package io.whitegoldlabs.bias.views;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import io.whitegoldlabs.bias.R;

/**
 * This page is reserved for future features.
 *
 * @author Clifton Roberts
 */
public class MainActivity extends BaseActivity
{
    // Fields -------------------------------------------------------------------------//
    private static final String TAG = "[MainActivity]";                                //
    // --------------------------------------------------------------------------------//

    /**
     * Sets the content view to the main layout and initializes Firebase authentication.
     *
     * @param savedInstanceState The dynamic state of the activity, if provided.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "Creating MainActivity...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        super.initAuth();

        Log.d(TAG, "MainActivity created.");
    }

    // --------------------------------------------------------------------------------//
    // Overridden Events                                                               //
    // --------------------------------------------------------------------------------//

    /**
     * Inflates the action bar overflow menu for this activity.
     *
     * @param menu The menu to be inflated.
     * @return true if the menu was inflated as expected, false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handles which action to take based on what menu item was selected.
     *
     * @param menuItem The menu item selected by the user.
     * @return true if the event was handled as expected, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        return super.onOptionsItemSelected(menuItem);
    }
}

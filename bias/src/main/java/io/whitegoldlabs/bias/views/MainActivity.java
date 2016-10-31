package io.whitegoldlabs.bias.views;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

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

        AccountHeader headerResult = new AccountHeaderBuilder()
            .withActivity(MainActivity.this)
            .withHeaderBackground(R.color.colorPrimary)
            .addProfiles
                (
                        new ProfileDrawerItem().withEmail("jenuma@live.com")
                )
            .build();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new DrawerBuilder()
            .withAccountHeader(headerResult)
            .withActivity(MainActivity.this)
            .withToolbar(toolbar)
            .addDrawerItems
                (
                    new PrimaryDrawerItem().withIdentifier(1).withName("First"),
                    new PrimaryDrawerItem().withIdentifier(2).withName("Second")
                )
            .withSelectedItem(-1)
            .build();

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

package io.whitegoldlabs.bias.features;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

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

    private Drawer result;
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

        //TODO: Organize this
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

        result = new DrawerBuilder()
            .withAccountHeader(headerResult)
            .withActivity(MainActivity.this)
            .withToolbar(toolbar)
            .addDrawerItems
                (
                    //TODO: Figure out how to get list fragment in here
                        //TODO: Extend FastAdapter?
                        //TODO: .withCustomView?
                    new PrimaryDrawerItem()
                        .withIdentifier(1)
                        .withIcon(FontAwesome.Icon.faw_home)
                        .withName("Home")
                        .withSelectable(false),
                    new PrimaryDrawerItem()
                        .withIdentifier(2)
                        .withIcon(FontAwesome.Icon.faw_shopping_cart)
                        .withName("Edit Cart")
                        .withSelectable(false),
                    new ExpandableDrawerItem()
                        .withIdentifier(3)
                        .withIcon(FontAwesome.Icon.faw_list)
                        .withName("[Your List]")
                        .withDescription("X of Y items crossed.")
                        .withSubItems(getListItems())
                        .withSelectable(false),
                    new PrimaryDrawerItem()
                        .withIdentifier(4)
                        .withIcon(FontAwesome.Icon.faw_users)
                        .withName("Manage Sharing")
                        .withSelectable(false),
                    new PrimaryDrawerItem()
                        .withIdentifier(5)
                        .withIcon(FontAwesome.Icon.faw_cog)
                        .withName("Settings")
                        .withSelectable(false),
                    new PrimaryDrawerItem()
                        .withIdentifier(6)
                        .withIcon(FontAwesome.Icon.faw_sign_out)
                        .withName("Sign Out")
                        .withSelectable(false)
                )
            .withSelectedItem(-1)
            .withCloseOnClick(false)
            .build();

        super.initAuth();

        Log.d(TAG, "MainActivity created.");
    }

    private List<IDrawerItem> getListItems()
    {
        List<IDrawerItem> subItems = new ArrayList<>();

        PrimaryDrawerItem item1 = new PrimaryDrawerItem()
            .withName("Item 1")
            .withSelectable(false);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem()
            .withName("Item 2")
            .withSelectable(false);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem()
            .withName("Item 3")
            .withSelectable(false);

        subItems.add(item1);
        subItems.add(item2);
        subItems.add(item3);

        return subItems;
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

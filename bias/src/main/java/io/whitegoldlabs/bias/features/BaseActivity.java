package io.whitegoldlabs.bias.features;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import static android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.common.collect.HashBiMap;

import com.google.firebase.auth.FirebaseAuth;
import static com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener;
import com.mikepenz.materialdrawer.Drawer.OnDrawerListener;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;

import io.whitegoldlabs.bias.Bias;
import io.whitegoldlabs.bias.R;
import io.whitegoldlabs.bias.common.DrawerWrapper;
import io.whitegoldlabs.bias.common.IObserver;
import io.whitegoldlabs.bias.models.Item;

/**
 * Acts as a repository for all functionality shared between sub-activities.
 *
 * @author Clifton Roberts
 */
public abstract class BaseActivity extends AppCompatActivity implements IObserver
{
    // Fields -------------------------------------------------------------------------//
    protected Bias app;                                                                //
                                                                                       //
    private FirebaseAuth auth;                                                         //
    private FirebaseAuth.AuthStateListener authListener;                               //
                                                                                       //
    protected Drawer drawer;                                                           //

    private static final HashBiMap<String, Integer> DRAWER_ITEM_ID_MAP;
    static
    {
        DRAWER_ITEM_ID_MAP = HashBiMap.create();
        DRAWER_ITEM_ID_MAP.put("MainActivity", 1);
        DRAWER_ITEM_ID_MAP.put("MyList", 2);
        DRAWER_ITEM_ID_MAP.put("ItemLocatorActivity", 3);
        DRAWER_ITEM_ID_MAP.put("ManageSharingActivity", 4);
        DRAWER_ITEM_ID_MAP.put("EditCartActivity", 5);
        DRAWER_ITEM_ID_MAP.put("EditMapActivity", 6);
        DRAWER_ITEM_ID_MAP.put("SettingsActivity", 7);
        DRAWER_ITEM_ID_MAP.put("SignOut", 8);
    }

    private static String TAG;                                                         //
    // --------------------------------------------------------------------------------//

    /**
     * Chains the sub-activity's call to AppCompatActivity.
     *
     * @param savedInstanceState The dynamic state of the activity, if provided.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        TAG = "[" + this.getClass().getSimpleName() + "]";
        super.onCreate(savedInstanceState);
    }

    /**
     * Adds the authentication state listener to the activity (except LoginActivity)
     * when it starts up.
     */
    @Override
    public void onStart()
    {
        super.onStart();

        if(!this.getClass().getSimpleName().equals("LoginActivity"))
        {
            app = ((Bias)getApplication());

            auth.addAuthStateListener(authListener);
        }
    }

    //TODO: Document this.
    @Override
    public void onRestart()
    {
        super.onRestart();

        if(drawer != null)
        {
            drawer.closeDrawer();
        }
    }

    /**
     * Removes the authentication state listener from the activity when it stops,
     * if it has one.
     */
    @Override
    public void onStop()
    {
        super.onStop();

        if(app != null)
        {
            app.removeObserver(this);
        }

        if(authListener != null)
        {
            Log.d(TAG, "Removing auth state listener...");

            auth.removeAuthStateListener(authListener);

            Log.d(TAG, "Auth state listener removed.");
        }
    }

    //TODO: Document this.
    @Override
    public void update(ArrayList<Item> newItems)
    {
        ArrayList<IDrawerItem> items = new ArrayList<>();

        for(Item item : newItems)
        {
            items.add
            (
                new PrimaryDrawerItem()
                    .withName(item.getName())
                    .withSelectable(false)
            );
        }

        int myListId = DRAWER_ITEM_ID_MAP.get("MyList");
        ((ExpandableDrawerItem)drawer.getDrawerItem(myListId)).withSubItems(items);
        drawer.getAdapter().notifyAdapterSubItemsChanged(myListId);
    }

    /**
     * Builds the navigation drawer for the calling activity.
     */
    protected void initDrawer() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = DrawerWrapper.build
        (
            this,
            toolbar,
            DRAWER_ITEM_ID_MAP,
            getOnDrawerItemClickListener(this),
            getOnDrawerListener(this)
        );
    }

    /**
     * Initializes the authentication state listener which will subsequently begin
     * listening for authentication state change events; if no user is detected as being
     * logged in, they will get redirected to the login page.
     */
    protected void initAuth()
    {
        auth = FirebaseAuth.getInstance();
        authListener = getAuthStateListener();
    }

    /**
     * Takes a string of text and makes a Toast dialog of it for the calling
     * sub-activity.
     *
     * @param text The text to be loaded into the Toast dialog.
     */
    protected void toast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    /**
     * Hides the soft keyboard from the calling sub-activity.
     */
    protected void hideSoftKeyboard()
    {
        Log.d(TAG, "Hiding soft keyboard...");

        InputMethodManager imm = (InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();

        if(view == null)
        {
            view = new View(this);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        Log.d(TAG, "Soft keyboard hidden.");
    }

    /**
     * Creates a new OnDrawerItemClickListener for clicking an item in the drawer.
     *
     * @param activity The activity the drawer resides in.
     * @return The new OnDrawerItemClickListener.
     */
    private OnDrawerItemClickListener getOnDrawerItemClickListener
    (
            final Activity activity
    )
    {
        return new Drawer.OnDrawerItemClickListener()
        {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem)
            {
                Class dest;
                switch((int)drawerItem.getIdentifier())
                {
                    //TODO: Clean this up so these numbers aren't hardcoded.
                    case 1:
                        dest = MainActivity.class;
                        break;
                    case 2:
                        return false;
                    case 3:
                        dest = ItemLocatorActivity.class;
                        break;
                    case 4:
                        return false;
                    case 5:
                        dest = EditCartActivity.class;
                        break;
                    case 6:
                        return false;
                    case 7:
                        return false;
                    case 8:
                        auth.signOut();
                        return false;
                    default:
                        return false;
                }

                if(!activity.getClass().getSimpleName().equals(dest.getSimpleName()))
                {
                    Intent intent = new Intent(activity, dest);
                    activity.startActivity(intent);
                }
                return false;
            }
        };
    }

    //TODO: Document this.
    private OnDrawerListener getOnDrawerListener(final Activity activity)
    {
        return new Drawer.OnDrawerListener()
        {
            @Override
            public void onDrawerClosed(View drawerView)
            {
                String activityName = activity.getClass().getSimpleName();
                drawer.setSelection(DRAWER_ITEM_ID_MAP.get(activityName));
            }

            @Override
            public void onDrawerOpened(View drawerView) {}
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {}
        };
    }

    /**
     * Creates a new OnEditorActionListener for pressing the "done" key on the soft
     * keyboard or pressing the "return" key on a hard keyboard.
     *
     * @param button The Button to click after hitting "done" or "return".
     * @return The new OnEditorActionListener.
     */
    protected OnEditorActionListener getOnEditorActionListener
    (
        final Button button
    )
    {
        return new OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                boolean softKeyboardDone = actionId == EditorInfo.IME_ACTION_DONE;

                if(softKeyboardDone)
                {
                    Log.d(TAG, "Software keyboard DONE action observed.");
                    button.performClick();
                }

                if(event != null)
                {
                    boolean isReturnKey = event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
                    boolean keyPressed = event.getAction() == KeyEvent.ACTION_DOWN;

                    if(isReturnKey && keyPressed)
                    {
                        Log.d(TAG, "Hardware keyboard RETURN key pressed down.");
                        button.performClick();
                    }
                }

                hideSoftKeyboard();
                findViewById(android.R.id.content).requestFocus();
                return false;
            }
        };
    }

    /**
     * Creates a new AuthStateListener for when a user logs in or out.
     *
     * @return The new AuthStateListener.
     */
    private AuthStateListener getAuthStateListener()
    {
        return new AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                Log.d(TAG, "User's authentication state change observed.");

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null)
                {
                    Log.w(TAG, "No user logged in. Redirecting to LoginActivity.");

                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
    }
}

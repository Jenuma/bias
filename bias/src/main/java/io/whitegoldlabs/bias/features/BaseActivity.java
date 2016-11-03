package io.whitegoldlabs.bias.features;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import static com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;

import io.whitegoldlabs.bias.R;
import io.whitegoldlabs.bias.common.DrawerBuilder;

/**
 * Acts as a repository for all functionality shared between sub-activities.
 *
 * @author Clifton Roberts
 */
public abstract class BaseActivity extends AppCompatActivity
{
    // Fields -------------------------------------------------------------------------//
    private FirebaseAuth auth;                                                         //
    private FirebaseAuth.AuthStateListener authListener;                               //
                                                                                       //
    private static final String TAG = "[BaseActivity]";                                //
    // --------------------------------------------------------------------------------//

    /**
     * Chains the sub-activity's call to AppCompatActivity.
     *
     * @param savedInstanceState The dynamic state of the activity, if provided.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "Creating BaseActivity...");

        super.onCreate(savedInstanceState);

        Log.d(TAG, "BaseActivity created.");
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
            Log.d(TAG, "Starting auth state listener...");

            auth.addAuthStateListener(authListener);

            Log.d(TAG, "Auth state listener started.");
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

        if(authListener != null)
        {
            Log.d(TAG, "Removing auth state listener...");

            auth.removeAuthStateListener(authListener);

            Log.d(TAG, "Auth state listener removed.");
        }
    }

    // --------------------------------------------------------------------------------//
    // Overridden Events                                                               //
    // --------------------------------------------------------------------------------//

    /**
     * Inflates the action bar overflow menu and hides certain menu items based on which
     * sub-activity it is inflating for.
     *
     * @param menu The menu to be inflated.
     * @return true if the menu was inflated as expected, false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.d(TAG, "Creating action bar overflow menu.");

        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_action_bar, menu);

        switch(this.getClass().getSimpleName())
        {
            case "MainActivity":
                break;
            case "EditCartActivity":
                MenuItem goToListItem = menu.findItem(R.id.action_go_to_list);
                goToListItem.setVisible(false);

                break;
            case "ItemLocatorActivity":
                MenuItem goToLayoutItem = menu.findItem(R.id.action_go_to_layout);
                goToLayoutItem.setVisible(false);

                break;
            default:
                return false;
        }
        Log.d(TAG, "Action bar overflow menu created.");
        return true;
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
        Log.d(TAG, "Action bar overflow menu item selected.");

        super.onOptionsItemSelected(menuItem);

        switch(menuItem.getItemId())
        {
            case R.id.action_go_to_list:
                goToList();
                return true;
            case R.id.action_go_to_layout:
                goToLayout();
                return true;
            case R.id.action_sign_out:
                signOut();
                return true;
            default:
                String error = "Invalid menu action!";
                Log.e(TAG, error);
                toast(error);
                return false;
        }
    }

    // --------------------------------------------------------------------------------//
    // Protected Methods                                                               //
    // --------------------------------------------------------------------------------//

    protected void initDrawer() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerBuilder.build(this, toolbar);
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

    // --------------------------------------------------------------------------------//
    // Private Methods                                                                 //
    // --------------------------------------------------------------------------------//

    /**
     * Brings the user to the shopping list page.
     */
    private void goToList()
    {
        Log.d(TAG, "Switching to EditCartActivity...");

        Intent intent = new Intent(BaseActivity.this, EditCartActivity.class);
        startActivity(intent);
    }

    /**
     * Brings the user to the test layout page.
     */
    private void goToLayout()
    {
        Log.d(TAG, "Switching to ItemLocatorActivity...");

        Intent intent = new Intent(BaseActivity.this, ItemLocatorActivity.class);
        startActivity(intent);
    }

    /**
     * Signs the user out of this Firebase instance; the user will be redirected to the
     * login page subsequently.
     */
    private void signOut()
    {
        Log.d(TAG, "Signing out user...");

        auth.signOut();

        Log.d(TAG, "User signed out.");
    }

    // --------------------------------------------------------------------------------//
    // Listeners                                                                       //
    // --------------------------------------------------------------------------------//

    /**
     * Creates a new OnEditorActionListener for pressing the "done" key on the soft
     * keyboard or pressing the "return" key on a hard keyboard.
     *
     * @param button The Button to click after hitting "done" or "return".
     * @return The new OnEditorActionListener.
     */
    protected TextView.OnEditorActionListener getOnEditorActionListener
    (
        final Button button
    )
    {
        return new TextView.OnEditorActionListener()
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
                    Log.d(TAG, "No user logged in. Redirecting to LoginActivity.");

                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
    }
}

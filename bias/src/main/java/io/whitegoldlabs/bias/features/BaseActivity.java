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

import com.google.firebase.auth.FirebaseAuth;
import static com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.Drawer;

import io.whitegoldlabs.bias.R;
import io.whitegoldlabs.bias.common.DrawerWrapper;

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
    protected Drawer drawer;                                                           //
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
    // Protected Methods                                                               //
    // --------------------------------------------------------------------------------//

    /**
     * Builds the navigation drawer for the calling activity.
     */
    protected void initDrawer() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = DrawerWrapper.build(this, toolbar);
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
    // Listeners                                                                       //
    // --------------------------------------------------------------------------------//

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
                    Log.d(TAG, "No user logged in. Redirecting to LoginActivity.");

                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
    }
}

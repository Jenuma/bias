package io.whitegoldlabs.bias.features;

import android.os.Bundle;
import android.util.Log;

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

        initAuth();
        initDrawer();

        Log.d(TAG, "MainActivity created.");
    }

    //TODO: Document this.
    @Override
    public void onStart()
    {
        super.onStart();

        app.addObserver(this);
    }
}

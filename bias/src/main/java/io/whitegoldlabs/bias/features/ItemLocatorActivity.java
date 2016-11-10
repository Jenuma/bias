package io.whitegoldlabs.bias.features;

import android.os.Bundle;
import android.util.Log;

import io.whitegoldlabs.bias.R;

/**
 * This page currently just displays a picture of my test layout. I will use this
 * activity to test the upcoming map highlight features.
 *
 * @author Clifton Roberts
 */
public class ItemLocatorActivity extends BaseActivity
{
    // Fields -------------------------------------------------------------------------//
    private static final String TAG = "[ItemLocatorActivity]";                         //
    // --------------------------------------------------------------------------------//

    /**
     * Sets the content view to the main layout and initializes Firebase authentication.
     *
     * @param savedInstanceState The dynamic state of the activity, if provided.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "Creating ItemLocatorActivity...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_locator);

        initAuth();
        initDrawer();

        Log.d(TAG, "ItemLocatorActivity created.");
    }

    //TODO: Document this.
    @Override
    public void onStart()
    {
        super.onStart();

        app.addObserver(this);
    }

    //TODO: Document this.
    @Override
    public void onStop()
    {
        super.onStop();
    }
}

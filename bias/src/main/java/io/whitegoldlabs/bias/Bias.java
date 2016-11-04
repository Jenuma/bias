package io.whitegoldlabs.bias;

import com.google.firebase.FirebaseApp;

/**
 * Base class for maintaining global application state.
 *
 * @author Clifton Roberts
 */
public class Bias extends android.app.Application
{
    /**
     * Initializes Bias for use with Firebase when the application is starting.
     */
    @Override
    public void onCreate()
    {
        super.onCreate();

        FirebaseApp.initializeApp(this);

        //TODO: Add documentation to everything missing it
        //TODO: Add log statements where needed
        //TODO: Change toolbar title to reflect current activity
    }
}

package io.whitegoldlabs.bias;

import com.google.firebase.FirebaseApp;

public class Bias extends android.app.Application
{
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
    }
}

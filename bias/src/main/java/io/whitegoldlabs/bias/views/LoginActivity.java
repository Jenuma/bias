package io.whitegoldlabs.bias.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.whitegoldlabs.bias.R;

/**
 * This page prompts the user to enter their authorized email/password pair to gain read
 * and write access to the shopping list database.
 *
 * @author Clifton Roberts
 */
public class LoginActivity extends BaseActivity
{
    // Fields -------------------------------------------------------------------------//
    private FirebaseAuth auth;                                                         //
                                                                                       //
    private static final String ERROR = "Incorrect email or password.";                //
    private static final String TAG = "[LoginActivity]";                               //
    // --------------------------------------------------------------------------------//

    /**
     * Sets the content view to the login layout and initializes login form and
     * Firebase authentication instance.
     *
     * @param savedInstanceState The dynamic state of the activity, if provided.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "Creating LoginActivity...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLoginForm();

        auth = FirebaseAuth.getInstance();

        Log.d(TAG, "LoginActivity created.");
    }

    // --------------------------------------------------------------------------------//
    // Behavior                                                                        //
    // --------------------------------------------------------------------------------//

    /**
     * Attempts to authenticate the user's input email and password pair.
     *
     * @param view The widget that called this method.
     */
    public void login(View view)
    {
        Log.d(TAG, "Attempting to authenticate user...");

        EditText editEmail = (EditText)findViewById(R.id.editEmail);
        EditText editPassword = (EditText)findViewById(R.id.editPassword);

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if(email.equals("") || password.equals(""))
        {
            Log.e(TAG, ERROR);
            toast(ERROR);
            return;
        }

        ProgressBar pbLoginLoading = (ProgressBar)findViewById(R.id.pbLoginLoading);
        pbLoginLoading.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(LoginActivity.this, getOnCompleteListener());
    }

    // --------------------------------------------------------------------------------//
    // Private Methods                                                                 //
    // --------------------------------------------------------------------------------//

    /**
     * Initializes the login form so that it listens for the return key and "done"
     * soft keyboard action; when triggered, it will attempt to log the user in and
     * hide the soft keyboard from the window.
     */
    private void initLoginForm()
    {
        Log.d(TAG, "Initializing login form...");

        final EditText editPassword = (EditText)findViewById(R.id.editPassword);
        final EditText editEmail = (EditText)findViewById(R.id.editEmail);
        final Button btnLogin = (Button)findViewById(R.id.btnLogin);

        editEmail.setOnEditorActionListener(getOnEditorActionListener(btnLogin));
        editPassword.setOnEditorActionListener(getOnEditorActionListener(btnLogin));

        Log.d(TAG, "Login form initialized.");
    }

    // --------------------------------------------------------------------------------//
    // Listeners                                                                       //
    // --------------------------------------------------------------------------------//

    /**
     * Creates a new OnCompleteListener for authenticating users.
     *
     * @return The new OnCompleteListener.
     */
    private OnCompleteListener<AuthResult> getOnCompleteListener()
    {
        return new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    Log.d(TAG, "User authenticated successfully.");

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Log.e(TAG, ERROR);
                    LoginActivity.super.toast(ERROR);
                }
            }
        };
    }
}

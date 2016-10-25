package io.whitegoldlabs.bias.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.whitegoldlabs.bias.R;

public class LoginActivity extends BaseActivity
{
    // Fields -------------------------------------------------------------------------//
    private FirebaseAuth auth;                                                         //
    // --------------------------------------------------------------------------------//

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLoginForm();

        auth = FirebaseAuth.getInstance();
    }

    public void login(View view)
    {
        EditText editEmail = (EditText)findViewById(R.id.editEmail);
        EditText editPassword = (EditText)findViewById(R.id.editPassword);

        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if(email.equals("") || password.equals(""))
        {
            LoginActivity.super.toast("Incorrect email or password.");
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        LoginActivity.super.toast("Incorrect email or password.");
                    }
                }
            });
    }

    private void initLoginForm()
    {
        final EditText editPassword = (EditText)findViewById(R.id.editPassword);
        final EditText editEmail = (EditText)findViewById(R.id.editEmail);
        final Button btnLogin = (Button)findViewById(R.id.btnLogin);

        EditText.OnEditorActionListener listener = new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    btnLogin.performClick();
                }

                if(event != null)
                {
                    if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                    {
                        btnLogin.performClick();
                    }
                }

                LoginActivity.super.hideSoftKeyboard();
                return false;
            }
        };

        editEmail.setOnEditorActionListener(listener);
        editPassword.setOnEditorActionListener(listener);
    }
}

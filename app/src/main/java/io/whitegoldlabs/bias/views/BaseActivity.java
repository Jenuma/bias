package io.whitegoldlabs.bias.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.whitegoldlabs.bias.R;

public abstract class BaseActivity extends AppCompatActivity
{
    // Fields -------------------------------------------------------------------------//
    private FirebaseAuth auth;                                                         //
    private FirebaseAuth.AuthStateListener authListener;                               //
    // --------------------------------------------------------------------------------//

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if(!this.getClass().getSimpleName().equals("LoginActivity"))
        {
            auth.addAuthStateListener(authListener);
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();

        if(authListener != null)
        {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_action_bar, menu);

        switch(this.getClass().getSimpleName())
        {
            case "MainActivity":
                return true;
            case "ShoppingListActivity":
                MenuItem goToListItem = menu.findItem(R.id.action_go_to_list);
                goToListItem.setVisible(false);

                return true;
            case "TestLayoutActivity":
                MenuItem goToLayoutItem = menu.findItem(R.id.action_go_to_layout);
                goToLayoutItem.setVisible(false);

                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
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
                //TODO: Error logging
                return false;
        }
    }

    public void initAuth()
    {
        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null)
                {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    protected void toast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    protected void hideSoftKeyboard()
    {
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();

        if(view == null)
        {
            view = new View(this);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void goToList()
    {
        Intent intent = new Intent(BaseActivity.this, ShoppingListActivity.class);
        startActivity(intent);
    }

    private void goToLayout()
    {
        Intent intent = new Intent(BaseActivity.this, TestLayoutActivity.class);
        startActivity(intent);
    }

    private void signOut()
    {
        auth.signOut();
    }
}

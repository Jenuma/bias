package io.whitegoldlabs.bias.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import io.whitegoldlabs.bias.R;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToList(View view)
    {
        Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
        startActivity(intent);
    }
}

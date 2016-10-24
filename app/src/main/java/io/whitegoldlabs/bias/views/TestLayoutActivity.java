package io.whitegoldlabs.bias.views;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import io.whitegoldlabs.bias.R;

public class TestLayoutActivity extends BaseActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_layout);

        super.initAuth();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        super.onOptionsItemSelected(menuItem);
        return true;
    }
}

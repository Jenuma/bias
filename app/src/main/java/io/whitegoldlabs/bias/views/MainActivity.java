package io.whitegoldlabs.bias.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.whitegoldlabs.bias.R;

public class MainActivity extends AppCompatActivity {
    private TextView txtHello = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.activity_main);

        txtHello = new TextView(MainActivity.this);
        txtHello.setText("Here's some new text.");

        mainLayout.addView(txtHello);
    }
}

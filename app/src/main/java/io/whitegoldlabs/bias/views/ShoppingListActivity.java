package io.whitegoldlabs.bias.views;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.whitegoldlabs.bias.R;

public class ShoppingListActivity extends AppCompatActivity
{
    private DatabaseReference db;

    private EditText editItem;

    private ArrayAdapter adapter;
    private List<String> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        initItemForm();
        initList();
        connectToDatabase();
        listenForListChanges();
    }

    public void addItem(View view)
    {
        String newItem = editItem.getText().toString();

        db.child("items").child(getNewId()).setValue(newItem);

        editItem.setText("");
    }

    private void initItemForm()
    {
        final Button btnAddItem = (Button)findViewById(R.id.btnAddItem);

        editItem = (EditText)findViewById(R.id.editItem);
        editItem.setOnEditorActionListener
        (
            new EditText.OnEditorActionListener()
            {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                {
                    if(actionId == EditorInfo.IME_ACTION_DONE)
                    {
                        btnAddItem.performClick();
                    }

                    if(event != null)
                    {
                        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                        {
                            btnAddItem.performClick();
                        }
                    }

                    hideSoftKeyboard(ShoppingListActivity.this);
                    return false;
                }
            }
        );
    }

    private void initList()
    {
        entries = new ArrayList<>();

        adapter = new ArrayAdapter<>
        (
                ShoppingListActivity.this,
                android.R.layout.simple_list_item_1,
                entries
        );

        ListView ulShoppingList = (ListView)findViewById(R.id.ulShoppingList);
        ulShoppingList.setAdapter(adapter);
    }

    private void connectToDatabase()
    {
        db = FirebaseDatabase
            .getInstance()
            .getReferenceFromUrl("https://bias-7675c.firebaseio.com/");
    }

    private void listenForListChanges()
    {
        db.child("items").addValueEventListener
        (
            new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    entries.clear();

                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        String item = snapshot.getValue(String.class);
                        entries.add(item);
                        adapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    System.out.println("ERROR: " + databaseError.getMessage());
                }
            }
        );
    }

    private String getNewId()
    {
        return Integer.toString(entries.size());
    }

    private void hideSoftKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if(view == null)
        {
            view = new View(activity);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

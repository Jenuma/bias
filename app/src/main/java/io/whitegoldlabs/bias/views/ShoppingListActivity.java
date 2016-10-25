package io.whitegoldlabs.bias.views;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
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

import io.whitegoldlabs.bias.R;
import io.whitegoldlabs.bias.common.ItemAdapter;
import io.whitegoldlabs.bias.models.Item;

import static android.widget.AdapterView.AdapterContextMenuInfo;

public class ShoppingListActivity extends BaseActivity
{
    // Fields -------------------------------------------------------------------------//
    private DatabaseReference db;                                                      //
    private final String DB_URL = "https://bias-7675c.firebaseio.com/";                //
                                                                                       //
    private EditText editItem;                                                         //
                                                                                       //
    private ArrayAdapter adapter;                                                      //
    private ArrayList<Item> items;                                                     //
                                                                                       //
    private Item selectedItem;                                                         //
                                                                                       //
    private final int MAX_CHARS = 35;                                                  //
    private int latestId;                                                              //
    // --------------------------------------------------------------------------------//

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        initItemForm();
        initList();
        connectToDatabase();
        listenForListChanges();

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

    /**
     * Adds an item to the list.
     *
     * @param view The view that called this method.
     */
    public void addItem(View view)
    {
        String newName = editItem.getText().toString();

        if(newName.length() > MAX_CHARS)
        {
            super.toast("Item cannot contain more than " + MAX_CHARS + " characters.");
            return;
        }

        if(newName.length() > 0)
        {
            String newId = Integer.toString(latestId + 1);

            DatabaseReference.CompletionListener listener = new DatabaseReference.CompletionListener()
            {
                @Override
                public void onComplete(DatabaseError error, DatabaseReference ref)
                {
                    if(error != null)
                    {
                        System.out.println("ERROR: " + error);
                        ShoppingListActivity.super.toast(error.getMessage());
                    }
                }
            };

            db.child("items").child(newId).child("name").setValue(newName, listener);
            db.child("items").child(newId).child("crossed").setValue(false, listener);

            editItem.setText("");
        }
    }

    /**
     * Removes an item from the list.
     *
     * @param menuItem The menu item that called this method.
     */
    public void removeItem(MenuItem menuItem)
    {
        db.child("items").child(Integer.toString(selectedItem.getId())).removeValue();
    }

    /**
     * Removes all items from the list.
     *
     * @param menuItem The menu item that called this method.
     */
    public void removeAll(MenuItem menuItem)
    {
        db.child("items").removeValue();
        latestId = -1;
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

                    ShoppingListActivity.super.hideSoftKeyboard();
                    return false;
                }
            }
        );
    }

    private void initList()
    {
        items = new ArrayList<>();

        adapter = new ItemAdapter
        (
                items,
                ShoppingListActivity.this
        );

        ListView ulShoppingList = (ListView)findViewById(R.id.ulShoppingList);
        ulShoppingList.setAdapter(adapter);

        ulShoppingList.setOnItemClickListener
        (
            new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    crossItem(position, view);
                }
            }
        );

        registerForContextMenu(ulShoppingList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
        selectedItem = items.get(info.position);
        menu.setHeaderTitle(selectedItem.getName());

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shopping_list, menu);
    }

    private void connectToDatabase()
    {
        db = FirebaseDatabase
            .getInstance()
            .getReferenceFromUrl(DB_URL);
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
                    items.clear();

                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Item item = snapshot.getValue(Item.class);
                        item.setId(Integer.parseInt(snapshot.getKey()));

                        items.add(item);

                        latestId = item.getId();
                    }

                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(DatabaseError error)
                {
                    System.out.println("ERROR: " + error.getMessage());
                    ShoppingListActivity.super.toast(error.getMessage());
                }
            }
        );
    }

    private void crossItem(int position, View view)
    {
        Item item = items.get(position);

        db.child("items")
            .child(Integer.toString(item.getId()))
            .child("crossed")
            .setValue(item.toggleCrossed());

        ((TextView)view).setPaintFlags(((TextView)view).getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
    }
}

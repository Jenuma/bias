package io.whitegoldlabs.bias.views;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import static android.widget.AdapterView.OnItemClickListener;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import static com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.whitegoldlabs.bias.R;
import io.whitegoldlabs.bias.common.ItemAdapter;
import io.whitegoldlabs.bias.models.Item;

import static android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * This page allows the user to read and update their shopping list. The shopping list
 * is automatically refreshed upon any change, whether it be adding a new item,
 * removing an item, or crossing an item off. The user can only interact with the
 * database if they are authenticated; if they log out, they will be redirected to the
 * login page.
 *
 * @author Clifton Roberts
 */
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
    private CompletionListener completionListener;                                     //
                                                                                       //
    private Item selectedItem;                                                         //
                                                                                       //
    private final int MAX_CHARS = 35;                                                  //
    private int latestId;                                                              //
    // --------------------------------------------------------------------------------//

    /**
     * Sets the content view to the shopping list layout and initializes the various
     * components needed to use this activity.
     *
     * @param savedInstanceState The dynamic state of the activity, if provided.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        initItemForm();
        initList();
        connectToDatabase();

        db.child("items").addValueEventListener(getValueEventListener());

        initAuth();
    }

    // --------------------------------------------------------------------------------//
    // Overridden Events                                                               //
    // --------------------------------------------------------------------------------//

    /**
     * Inflates the action bar overflow menu for this activity.
     *
     * @param menu The menu to be inflated.
     * @return true if the menu was inflated as expected, false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Listens for the creation of the item list's context menu, gets the selected item,
     * and inflates the menu.
     *
     * @param menu The menu to be inflated.
     * @param v The view for which the context menu is being built.
     * @param menuInfo Extra information about the selected item.
     */
    @Override
    public void onCreateContextMenu
    (
            ContextMenu menu,
            View v,
            ContextMenu.ContextMenuInfo menuInfo
    )
    {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
        selectedItem = items.get(info.position);
        menu.setHeaderTitle(selectedItem.getName());

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shopping_list, menu);
    }

    /**
     * Handles which action to take based on what menu item was selected.
     *
     * @param menuItem The menu item selected by the user.
     * @return true if the event was handled as expected, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        return super.onOptionsItemSelected(menuItem);
    }

    // --------------------------------------------------------------------------------//
    // Behavior                                                                        //
    // --------------------------------------------------------------------------------//

    /**
     * Adds an item to the list. The item cannot contain more than MAX_CHARS characters.
     * The item must also have a name. This method automatically increments the ID of the
     * new item.
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

            db.child("items")
                .child(newId)
                .child("name")
                .setValue(newName, getCompletionListener());
            db.child("items")
                .child(newId)
                .child("crossed")
                .setValue(false, getCompletionListener());

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
        db.child("items")
            .child(Integer.toString(selectedItem.getId()))
            .removeValue(getCompletionListener());
    }

    /**
     * Removes all items from the list.
     *
     * @param menuItem The menu item that called this method.
     */
    public void removeAll(MenuItem menuItem)
    {
        db.child("items").removeValue(getCompletionListener());
        latestId = -1;
    }

    // --------------------------------------------------------------------------------//
    // Private Methods                                                                 //
    // --------------------------------------------------------------------------------//

    /**
     * Initializes the new item form so that it listens for the return key and "done"
     * soft keyboard action; when triggered, it will add the item to the database
     * and hide the soft keyboard from the window.
     */
    private void initItemForm()
    {
        final Button btnAddItem = (Button)findViewById(R.id.btnAddItem);

        editItem = (EditText)findViewById(R.id.editItem);
        editItem.setOnEditorActionListener(getOnEditorActionListener(btnAddItem));
    }

    /**
     * Initializes the list of items and listens for click events. The list will
     * when the user touches or clicks an item in the list, it will toggle that item's
     * "crossed" state. When the user long-presses an item, a context menu with the
     * options to remove said item or all items will inflate.
     */
    private void initList()
    {
        items = new ArrayList<>();
        adapter = new ItemAdapter(items, ShoppingListActivity.this);

        ListView ulShoppingList = (ListView)findViewById(R.id.ulShoppingList);
        ulShoppingList.setAdapter(adapter);
        ulShoppingList.setOnItemClickListener(getOnItemClickListener());
        registerForContextMenu(ulShoppingList);
    }

    /**
     * Connects this activity to Firebase.
     */
    private void connectToDatabase()
    {
        db = FirebaseDatabase
            .getInstance()
            .getReferenceFromUrl(DB_URL);
    }

    /**
     * Toggles the "crossed" state of the item in the indicated position of the list.
     *
     * @param position The position of the item in the list.
     * @param view The text view that renders the item to the window.
     */
    private void crossItem(int position, View view)
    {
        Item item = items.get(position);

        db.child("items")
                .child(Integer.toString(item.getId()))
                .child("crossed")
                .setValue(item.toggleCrossed());

        ((TextView)view).setPaintFlags
        (
            ((TextView)view).getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG
        );
    }

    // --------------------------------------------------------------------------------//
    // Listeners                                                                       //
    // --------------------------------------------------------------------------------//

    /**
     * Creates a new CompletionListener for adding new items to the database.
     *
     * @return The new CompletionListener.
     */
    private CompletionListener getCompletionListener()
    {
        if(completionListener != null)
        {
            return completionListener;
        }
        else
        {
            completionListener = new CompletionListener()
            {
                @Override
                public void onComplete(DatabaseError error, DatabaseReference ref)
                {
                    if(error != null)
                    {
                        System.out.println("ERROR: " + error);
                        toast(error.getMessage());
                    }
                }
            };
            return completionListener;
        }
    }

    /**
     * Creates a new OnItemClickListener for clicking on an item in the shopping list.
     *
     * @return The new OnItemClickListener.
     */
    private OnItemClickListener getOnItemClickListener()
    {
        return new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick
            (
                AdapterView<?> parent,
                 View view,
                 int position,
                 long id
            )
            {
                crossItem(position, view);
            }
        };
    }

    /**
     * Creates a new ValueEventListener for database changes.
     *
     * @return The new ValueEventListener.
     */
    private ValueEventListener getValueEventListener()
    {
        return new ValueEventListener()
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
                ShoppingListActivity.this.toast(error.getMessage());
            }
        };
    }
}

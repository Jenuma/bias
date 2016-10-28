package io.whitegoldlabs.bias.views;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import static com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.whitegoldlabs.bias.BuildConfig;
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
                                                                                       //
    private EditText editItem;                                                         //
    private ProgressBar pbListLoading;                                                 //
                                                                                       //
    private ArrayAdapter adapter;                                                      //
    private ArrayList<Item> items;                                                     //
                                                                                       //
    private CompletionListener completionListener;                                     //
                                                                                       //
    private Item selectedItem;                                                         //
                                                                                       //
    private int latestId;                                                              //
                                                                                       //
    private static final int MAX_CHARS = 35;                                           //
    private static final String TAG = "[ShoppingListActivity]";                        //
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
        Log.d(TAG, "Creating ShoppingListActivity...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        initItemForm();
        initList();
        connectToDatabase();

        db.child("items").addValueEventListener(getValueEventListener());

        initAuth();

        Log.d(TAG, "ShoppingListActivity created.");
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
        Log.d(TAG, "Creating context menu...");

        AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
        selectedItem = items.get(info.position);
        menu.setHeaderTitle(selectedItem.getName());

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shopping_list, menu);

        Log.d(TAG, "Context menu created.");
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
            Log.d(TAG, "User tried to create new item with too long of a name.");

            super.toast("Item cannot contain more than " + MAX_CHARS + " characters.");
            return;
        }

        if(newName.length() > 0)
        {
            Log.d(TAG, "User adding new item...");

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
        Log.d(TAG, "User removing an item...");

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
        Log.d(TAG, "User removing all items...");

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
        Log.d(TAG, "Initializing new item form...");

        final Button btnAddItem = (Button)findViewById(R.id.btnAddItem);

        editItem = (EditText)findViewById(R.id.editItem);
        editItem.setOnEditorActionListener(getOnEditorActionListener(btnAddItem));

        Log.d(TAG, "New item form initialized.");
    }

    /**
     * Initializes the list of items and listens for click events. The list will
     * when the user touches or clicks an item in the list, it will toggle that item's
     * "crossed" state. When the user long-presses an item, a context menu with the
     * options to remove said item or all items will inflate.
     */
    private void initList()
    {
        Log.d(TAG, "Initializing shopping list...");

        items = new ArrayList<>();
        adapter = new ItemAdapter(items, ShoppingListActivity.this);

        ListView ulShoppingList = (ListView)findViewById(R.id.ulShoppingList);
        ulShoppingList.setAdapter(adapter);
        ulShoppingList.setOnItemClickListener(getOnItemClickListener());
        registerForContextMenu(ulShoppingList);

        Log.d(TAG, "Shopping list initialized.");
    }

    /**
     * Connects this activity to Firebase.
     */
    private void connectToDatabase()
    {
        Log.d(TAG, "Connecting to Firebase...");

        db = FirebaseDatabase
            .getInstance()
            .getReferenceFromUrl(BuildConfig.DB_URL);

        Log.d(TAG, "Connected to Firebase.");
    }

    /**
     * Toggles the "crossed" state of the item in the indicated position of the list.
     *
     * @param position The position of the item in the list.
     * @param view The text view that renders the item to the window.
     */
    private void crossItem(int position, View view)
    {
        Log.d(TAG, "User crossing item off the list...");

        Item item = items.get(position);

        db.child("items")
                .child(Integer.toString(item.getId()))
                .child("crossed")
                .setValue(item.toggleCrossed());

        ((TextView)view).setPaintFlags
        (
            ((TextView)view).getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG
        );

        Log.d(TAG, "Item crossed off the list successfully.");
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
                        Log.e(TAG, "Database write failed! Details: " + error);
                        toast("Database write failed!");
                        return;
                    }
                    Log.d(TAG, "Database write occurred successfully.");
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
                Log.d(TAG, "Notified of database change...");

                new DataChangeThread().execute(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                Log.e(TAG, "Database read failed! Details: " + error);
                ShoppingListActivity.this.toast("Database read failed!");
            }
        };
    }

    /**
     * Worker thread for getting the shopping list items from the database. Starts a
     * progress bar in case retrieval takes more than a second and removes it when done.
     */
    private class DataChangeThread extends AsyncTask<DataSnapshot, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            pbListLoading = (ProgressBar)findViewById(R.id.pbListLoading);
            pbListLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(DataSnapshot... params)
        {
            items.clear();

            Log.d(TAG, "DataChangeTask => Getting shopping list items from database...");

            for(DataSnapshot snapshot : params[0].getChildren())
            {
                Item item = snapshot.getValue(Item.class);
                item.setId(Integer.parseInt(snapshot.getKey()));

                items.add(item);

                latestId = item.getId();
            }

            Log.d(TAG, "DataChangeTask => Shopping list items retrieved successfully.");
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            pbListLoading.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
}

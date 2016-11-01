package io.whitegoldlabs.bias.features;

import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class ShoppingListFragment extends Fragment
{
    // Fields -------------------------------------------------------------------------//
    private DatabaseReference db;                                                      //
                                                                                       //
    private ProgressBar pbListLoading;                                                 //
                                                                                       //
    private ArrayAdapter adapter;                                                      //
    ArrayList<Item> items;                                                             //
                                                                                       //
    int latestId;                                                                      //
                                                                                       //
    private static final String TAG = "[ShoppingListFragment]";                        //
    // --------------------------------------------------------------------------------//

    public ShoppingListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        connectToDatabase(args.getString("connectionString", ""));

        items = new ArrayList<>();
        adapter = new ItemAdapter(items, getContext());

        db.child("items").addValueEventListener(getValueEventListener());
    }

    @Override
    public View onCreateView
    (
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    )
    {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        ListView lvShoppingList = (ListView)view.findViewById(R.id.lvShoppingList);
        pbListLoading = (ProgressBar)view.findViewById(R.id.pbListLoading);

        lvShoppingList.setAdapter(adapter);
        lvShoppingList.setOnItemClickListener(getOnItemClickListener());
        registerForContextMenu(lvShoppingList);

        return view;
    }

    public static ShoppingListFragment newInstance(String connectionString)
    {
        ShoppingListFragment frag = new ShoppingListFragment();
        Bundle args = new Bundle();
        args.putString("connectionString", connectionString);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
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

    /**
     * Connects this activity to Firebase.
     */
    private void connectToDatabase(String url)
    {
        Log.d(TAG, "Connecting to Firebase...");

        db = FirebaseDatabase
                .getInstance()
                .getReferenceFromUrl(url);

        Log.d(TAG, "Connected to Firebase.");
    }

    /**
     * Creates a new OnItemClickListener for clicking on an item in the shopping list.
     *
     * @return The new OnItemClickListener.
     */
    private AdapterView.OnItemClickListener getOnItemClickListener()
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

                new ShoppingListFragment.DataChangeThread().execute(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                Log.e(TAG, "Database read failed! Details: " + error);
                //TODO: toast("Database read failed!");
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

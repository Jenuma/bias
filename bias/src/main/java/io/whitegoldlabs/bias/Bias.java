package io.whitegoldlabs.bias;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.whitegoldlabs.bias.common.IObserver;
import io.whitegoldlabs.bias.models.Item;

/**
 * Base class for maintaining global application state.
 *
 * @author Clifton Roberts
 */
public class Bias extends android.app.Application
{
    // Fields -------------------------------------------------------------------------//
    private DatabaseReference db;                                                      //
                                                                                       //
    private ArrayList<IObserver> observers;                                             //
    private ArrayList<Item> items;                                                     //
    private int latestId;                                                              //
                                                                                       //
    private static final String TAG = "[BIAS]";                                        //
    // --------------------------------------------------------------------------------//

    /**
     * Initializes Bias for use with Firebase when the application is starting.
     */
    @Override
    public void onCreate()
    {
        super.onCreate();

        observers = new ArrayList<>();
        items = new ArrayList<>();

        FirebaseApp.initializeApp(this);
        connectToDatabase(BuildConfig.DB_URL);
        db.child("items").addValueEventListener(getValueEventListener());

        //TODO: Add documentation to everything missing it
        //TODO: Add log statements where needed
        //TODO: Change toolbar title to reflect current activity
        //TODO: LOTS of code needs to be cleaned up after adding observer pattern
        //TODO: Add removeObserver method and remove activities as they're destroyed
    }

    // Accessors ----------------------------------------------------------------------//
    public DatabaseReference getDb() {return db;}                                      //
    public ArrayList<Item> getItems() {return items;}                                  //
    public int getLatestId() {return latestId;}                                        //
    // --------------------------------------------------------------------------------//

    //TODO: Document this.
    public void addObserver(IObserver observer)
    {
        observers.add(observer);
    }

    //TODO: Document this.
    public void notifyObservers(ArrayList<Item> result)
    {
        for(IObserver observer : observers)
        {
            observer.update(result);
        }
    }

    /**
     * Connects this application to Firebase.
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

                new Bias.DataChangeThread().execute(dataSnapshot);
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
     * Worker thread for getting the shopping list items from the database.
     */
    private class DataChangeThread extends AsyncTask<DataSnapshot, Void, ArrayList<Item>>
    {
        @Override
        protected ArrayList<Item> doInBackground(DataSnapshot... params)
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
            return items;
        }

        @Override
        protected void onPostExecute(ArrayList<Item> result)
        {
            notifyObservers(result);
        }
    }
}

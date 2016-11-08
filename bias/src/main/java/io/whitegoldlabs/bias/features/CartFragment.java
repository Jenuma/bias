package io.whitegoldlabs.bias.features;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import io.whitegoldlabs.bias.Bias;
import io.whitegoldlabs.bias.R;
import io.whitegoldlabs.bias.common.ItemAdapter;
import io.whitegoldlabs.bias.common.IObserver;
import io.whitegoldlabs.bias.models.Item;

public class CartFragment extends Fragment implements IObserver
{
    //TODO: Get progress bar working or remove it.
    // Fields -------------------------------------------------------------------------//
    private static final String TAG = "[CartFragment]";                                //
    private ProgressBar pbListLoading;                                                 //
    private ItemAdapter adapter;                                                       //
    int nextId;                                                                        //
    ArrayList<Item> items;                                                             //
    // --------------------------------------------------------------------------------//

    //TODO: Document this
    public CartFragment() {}

    //TODO: Document this
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //TODO: Remove these if not necessary...
        Bias app = ((Bias)getActivity().getApplication());
        items = app.getItems();
        adapter = new ItemAdapter(items, getContext());
    }

    //TODO: Document this.
    @Override
    public View onCreateView
    (
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    )
    {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        ListView lvShoppingList = (ListView)view.findViewById(R.id.lvShoppingList);
        pbListLoading = (ProgressBar)view.findViewById(R.id.pbListLoading);

        lvShoppingList.setAdapter(adapter);
        lvShoppingList.setOnItemClickListener(getOnItemClickListener());
        registerForContextMenu(lvShoppingList);

        return view;
    }

    //TODO: Document this.
    @Override
    public void onStart()
    {
        super.onStart();

        ((Bias)getActivity().getApplication()).addObserver(this);
    }

    //TODO: Can probably remove this... otherwise document
    public static CartFragment newInstance(String connectionString)
    {
        CartFragment frag = new CartFragment();
        Bundle args = new Bundle();
        args.putString("connectionString", connectionString);
        frag.setArguments(args);

        return frag;
    }

    //TODO: What are these? They appeared on their own... document them or remove them
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

    @Override
    public void update(ArrayList<Item> newItems)
    {
        nextId = getNextId();

        adapter.notifyDataSetChanged();

        Log.d(TAG, "Adapter notified of data set change.");
    }

    private int getNextId()
    {
        if(items.size() == 0)
        {
            return 0;
        }
        else
        {
            return items.get(items.size() - 1).getId() + 1;
        }
    }

    /**
     * Toggles the "crossed" state of the item in the indicated position of the list.
     *
     * @param position The position of the item in the list.
     * @param view The text view that renders the item to the window.
     */
    private void crossItem(int position, View view)
    {
        Log.d(TAG, "User un/crossing item...");

        DatabaseReference db = ((Bias)getActivity().getApplication()).getDb();
        Item item = items.get(position);

        db.child("items")
            .child(Integer.toString(item.getId()))
            .child("crossed")
            .setValue(item.toggleCrossed());

        ((TextView)view).setPaintFlags
        (
            ((TextView)view).getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG
        );

        Log.d(TAG, "Item un/crossed successfully.");
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
}

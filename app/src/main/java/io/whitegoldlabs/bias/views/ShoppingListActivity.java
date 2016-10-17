package io.whitegoldlabs.bias.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
    ArrayAdapter adapter;
    private List<String> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        entries = new ArrayList<String>();

        adapter = new ArrayAdapter<>
                (
                        ShoppingListActivity.this,
                        android.R.layout.simple_list_item_1,
                        entries
                );

        ListView ulShoppingList = (ListView)findViewById(R.id.ulShoppingList);
        ulShoppingList.setAdapter(adapter);

        db = FirebaseDatabase
                .getInstance()
                .getReferenceFromUrl("https://bias-7675c.firebaseio.com/");

        initList();
    }

    private void initList()
    {
        db.child("items").addValueEventListener
        (
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
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

    public void addItem(View view)
    {
        TextView editItem = (TextView)findViewById(R.id.editItem);
        String newItem = editItem.getText().toString();

        entries.add(newItem);
        db.child("items").child(getNewId()).setValue(newItem);
        adapter.notifyDataSetChanged();
    }

    private String getNewId()
    {
        return Integer.toString(entries.size());
    }
}

package io.whitegoldlabs.bias.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.whitegoldlabs.bias.R;
import io.whitegoldlabs.bias.models.Contact;

public class MainActivity extends AppCompatActivity
{
    private TextView txtHello;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase
                .getInstance()
                .getReferenceFromUrl("https://bias-7675c.firebaseio.com/contacts");

        db.child("0").addListenerForSingleValueEvent
        (
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        Contact contact = dataSnapshot.getValue(Contact.class);
                        txtHello = (TextView)findViewById(R.id.txtHello);
                        txtHello.setText(contact.getName());
                        System.out.println(contact.getName());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                }
        );
    }
}

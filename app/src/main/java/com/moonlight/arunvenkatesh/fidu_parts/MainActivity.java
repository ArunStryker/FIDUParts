package com.moonlight.arunvenkatesh.fidu_parts;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DatabaseReference mDatabase;
    private DatabaseReference mSelection;
    List<String> listItems = new ArrayList<>();
    List<String> keys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Menu List");
        mSelection = FirebaseDatabase.getInstance().getReference().child("Selection List");

        // Drop Down List
        Spinner dropdown = findViewById(R.id.fire_list);
        listItems.add("<<Select Any>>");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);


        // Retrieve Updated Data
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //Map<String, String> dataMap = (Map<String, String>) dataSnapshot.getValue();
                //String value = dataMap.getvalue();
                String value = dataSnapshot.getValue(String.class);

                String key = dataSnapshot.getKey();

                // Getting Current Index
                int index = keys.indexOf(key);
                index = index + 1;

                //String value = dataMap.get(key);

                //Log
                Log.d(TAG, "onChildAdded: value: " + value);

                listItems.add(value);

                //Log
                for (int i = 0; i < listItems.size(); i++) {
                    Log.d(TAG, "onChildAdded: list: " + listItems.get(i));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                String key = dataSnapshot.getKey();

                // Getting Current Index
                int index = keys.indexOf(key);
                index = index + 1;

                //Log
                Log.d(TAG, "onChildChanged: index: " + index);

                // Updating the value
                listItems.set(index, value);

                //Log
                for (int i = 0; i < listItems.size(); i++) {
                    Log.d(TAG, "onChildChanged: change: " + listItems.get(i));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();

                // Getting Current Index
                int index = keys.indexOf(key);
                index = index + 1;

                //Log
                Log.d(TAG, "onChildRemoved: index_r: " + index);

                // Updating the value
                //listItems.set(index, value);
                listItems.remove(index);

                //Log
                for (int i = 0; i < listItems.size(); i++) {
                    Log.d(TAG, "onChildRemoved: change_r: " + listItems.get(i));
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A data item has changed position
                Comment movedComment = dataSnapshot.getValue(Comment.class);
                Message message = dataSnapshot.getValue(Message.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
                Toast.makeText(MainActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });

        // Ascending Order
        mDatabase.orderByChild("Menu List/ID");

        // After Selecting an Item
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).toString().equals("<<Select Any>>"))
                {
                    Toast.makeText(MainActivity.this, "Select the desired one from the List", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "YOUR SELECTION IS : " + adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                    String content = adapterView.getItemAtPosition(i).toString();
                    Log.d(TAG, "onItemSelected: con" + content);
                    pushContent(content);
                }
            }

            private void pushContent(String content) {
                SelectedItem selItem = new SelectedItem(content);

                mSelection.setValue(selItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}

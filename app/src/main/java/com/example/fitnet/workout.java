package com.example.fitnet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class workout extends Fragment {

    Button addButton;
    EditText itemEditText;
    EditText weightEditText;
    EditText repsEditText;
    EditText recordEditText;
    Spinner categorySpinner;
    ListView itemList;
    final String[] categories = {"Chest", "Back", "Legs", "Arms", "Shoulders"};
    final Map<String, List<String>> items = new HashMap<>();
    private DatabaseReference databaseRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);

        for (String category : categories) {
            items.put(category, new ArrayList<String>());
        }

        databaseRef = FirebaseDatabase.getInstance().getReference().child("workout_items");

        categorySpinner = view.findViewById(R.id.category_spinner);
        itemEditText = view.findViewById(R.id.item_edit_text);
        weightEditText = view.findViewById(R.id.weight_edit_text);
        repsEditText = view.findViewById(R.id.reps_edit_text);
        recordEditText = view.findViewById(R.id.record_edit_text);
        addButton = view.findViewById(R.id.add_button);
        itemList = view.findViewById(R.id.item_list);

        // Set up the spinner to display the categories
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(spinnerAdapter);


        // Set up the list view to display the items
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, items.get(categories[0]));
        itemList.setAdapter(listAdapter);

        // Retrieve the items from Firebase
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.get("Chest").clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    String item = dataSnapshot.getKey();
                    String details = itemSnapshot.getValue(String.class);
                    String[] detailParts = details.split(",");
                    String weight = detailParts[0].trim().split(":")[1].trim();
                    String reps = detailParts[1].trim().split(":")[1].trim();
                    String record = detailParts[2].trim().split(":")[1].trim();
                    String newItem = item + " (Weight: " + weight + ", Reps: " + reps + ", Record: " + record + ")";
                    items.get("Chest").add(details);
                }

                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("workout", databaseError.getMessage());
            }
        });


        // Set up the add button to add items to the selected category
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "Chest";
                String item = itemEditText.getText().toString();
                String weight = weightEditText.getText().toString();
                String reps = repsEditText.getText().toString();
                String record = recordEditText.getText().toString();
                String newItem = item + " (Weight: " + weight + ", Reps: " + reps + ", Record: " + record + ")";
                items.get(category).add(newItem);
                listAdapter.notifyDataSetChanged();
                itemEditText.setText("");
                weightEditText.setText("");
                repsEditText.setText("");
                recordEditText.setText("");

                // Save the new item to Firebase
                databaseRef.push().setValue(newItem);
            }
        });

        // Set up the list view to allow the user to edit the items
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                String[] parts = selectedItem.split("\\(");
                String item = parts[0].trim();
                String details = parts[1].substring(0, parts[1].length() - 1).trim();
                String[] detailParts = details.split(",");
                String weight = detailParts[0].trim().split(":")[1].trim();
                String reps = detailParts[1].trim().split(":")[1].trim();
                String record = detailParts[2].trim().split(":")[1].trim();
                final int position = i;

                // Create a dialog to allow the user to edit or delete the item's details
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit Item");
                View dialogView = getLayoutInflater().inflate(R.layout.edit_item_dialog, null);
                final EditText itemEditText = dialogView.findViewById(R.id.item_edit_text);
                final EditText weightEditText = dialogView.findViewById(R.id.weight_edit_text);
                final EditText repsEditText = dialogView.findViewById(R.id.reps_edit_text);
                final EditText recordEditText = dialogView.findViewById(R.id.record_edit_text);
                itemEditText.setText(item);
                weightEditText.setText(weight);
                repsEditText.setText(reps);
                recordEditText.setText(record);
                builder.setView(dialogView);

                // Set up the list view to allow the user to delete the items on long click
                itemList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedItem = (String) adapterView.getItemAtPosition(i);
                        String[] parts = selectedItem.split("\\(");
                        String item = parts[0].trim();
                        String details = parts[1].substring(0, parts[1].length() - 1).trim();

                        // Create a dialog to confirm item deletion
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Delete Item");
                        builder.setMessage("Are you sure you want to delete this item?");

                        // Set up the delete button to delete the item
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                items.get("Chest").remove(i);
                                listAdapter.notifyDataSetChanged();

                                // Remove the item from Firebase
                                DatabaseReference itemRef = databaseRef.child(item);
                                itemRef.removeValue();
                            }
                        });

                        // Set up the cancel button to close the dialog without deleting
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                        return true; // Return true to consume the long click event
                    }
                });

                // Set up the save button to save the edited item
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newWeight = weightEditText.getText().toString();
                        String newReps = repsEditText.getText().toString();
                        String newRecord = recordEditText.getText().toString();
                        String newItem = item + " (Weight: " + newWeight + ", Reps: " + newReps + ", Record: " + newRecord + ")";
                        items.get("Chest").set(position, newItem);

                        listAdapter.notifyDataSetChanged();

                        databaseRef.setValue(items.get("Chest"));
                    }
                });


                // Set up the cancel button to close the dialog without saving
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        return view;
    }
}
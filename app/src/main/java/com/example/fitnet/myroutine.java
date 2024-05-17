package com.example.fitnet;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class myroutine extends AppCompatActivity {

    private ListView mondayListView, tuesdayListView, wednesdayListView, thursdayListView,
            fridayListView, saturdayListView, sundayListView;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myroutine);

        // Initialize views
        mondayListView = findViewById(R.id.listview_monday);
        tuesdayListView = findViewById(R.id.listview_tuesday);
        wednesdayListView = findViewById(R.id.listview_wednesday);
        thursdayListView = findViewById(R.id.listview_thursday);
        fridayListView = findViewById(R.id.listview_friday);
        saturdayListView = findViewById(R.id.listview_saturday);
        sundayListView = findViewById(R.id.listview_sunday);

        // Get reference to Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference();


        Button addExerciseMondayButton = findViewById(R.id.add_exercise_monday);
        Button addExerciseTuesdayButton = findViewById(R.id.add_exercise_tuesday);
        Button addExerciseWednesdayButton = findViewById(R.id.add_exercise_wednesday);
        Button addExerciseThursdayButton = findViewById(R.id.add_exercise_thursday);
        Button addExerciseFridayButton = findViewById(R.id.add_exercise_friday);
        Button addExerciseSaturdayButton = findViewById(R.id.add_exercise_saturday);
        Button addExerciseSundayButton = findViewById(R.id.add_exercise_sunday);

        // Set on click listeners for buttons
        addExerciseMondayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExerciseDialog("Monday", mondayListView);
            }
        });

        addExerciseTuesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExerciseDialog("Tuesday", tuesdayListView);
            }
        });

        addExerciseWednesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExerciseDialog("Wednesday", wednesdayListView);
            }
        });

        addExerciseThursdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExerciseDialog("Thursday", thursdayListView);
            }
        });

        addExerciseFridayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExerciseDialog("Friday", fridayListView);
            }
        });

        addExerciseSaturdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExerciseDialog("Saturday", saturdayListView);
            }
        });

        addExerciseSundayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExerciseDialog("Sunday", sundayListView);
            }
        });
    }


    // Show dialog to add exercise
    private void showAddExerciseDialog(final String day, final ListView listView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(myroutine.this);
        builder.setTitle("Add Exercise");

        // Set custom layout for dialog
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_exercise, null);
        builder.setView(dialogView);

        // Get views from dialog
        final EditText exerciseEditText = dialogView.findViewById(R.id.edittext_exercise);
        final EditText setsEditText = dialogView.findViewById(R.id.edittext_sets);
        final EditText repsEditText = dialogView.findViewById(R.id.edittext_reps);

        // Set positive button to add
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get input values from edit texts
                String exerciseName = exerciseEditText.getText().toString();
                String sets = setsEditText.getText().toString();
                String reps = repsEditText.getText().toString();

                // Create Exercise object with input values
                Exercise newExercise = new Exercise(exerciseName, sets, reps);

                // Get reference to exercises for specific day in Firebase Realtime Database
                DatabaseReference dayExercisesRef = mDatabase.child(day);

                // Push new exercise to database
                dayExercisesRef.push().setValue(newExercise);
                // Update list view for the corresponding day
                updateListView(dayExercisesRef, listView);
            }
        });

        // Set negative button to cancel dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel dialog
                dialog.cancel();
            }
        });

        // Show dialog
        AlertDialog dialog = builder.create();
        builder.show();
    }

    private Activity getActivity() {
        return null;
    }

    // Update listview with exercises from Firebase Realtime Database
    private void updateListView(final DatabaseReference exercisesRef, final ListView listView) {
        exercisesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<Exercise> exercises = new ArrayList<>();
                for (DataSnapshot exerciseSnapshot : snapshot.getChildren()) {
                    Exercise exercise = exerciseSnapshot.getValue(Exercise.class);
                    exercise.setExerciseId(exerciseSnapshot.getKey()); // Set the exerciseId
                    exercises.add(exercise);
                }

                final ExerciseListAdapter adapter = new ExerciseListAdapter(myroutine.this, exercises);
                listView.setAdapter(adapter);

                adapter.setOnDeleteClickListener(new ExerciseListAdapter.OnDeleteClickListener() {
                    @Override
                    public void onDeleteClick(Exercise exercise) {
                        String exerciseId = exercise.getExerciseId();

                        // Remove exercise from the database
                        exercisesRef.child(exerciseId).removeValue();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}



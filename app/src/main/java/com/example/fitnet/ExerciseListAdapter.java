package com.example.fitnet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

// ExerciseListAdapter class
public class ExerciseListAdapter extends ArrayAdapter<Exercise> {
    private Context context;
    private ArrayList<Exercise> exercises;
    private OnDeleteClickListener deleteClickListener;

    public ExerciseListAdapter(Context context, ArrayList<Exercise> exercises) {
        super(context, 0, exercises);
        this.context = context;
        this.exercises = exercises;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_exercise, parent, false);
        }

        Exercise exercise = exercises.get(position);

        TextView nameTextView = convertView.findViewById(R.id.textview_exercise);
        TextView setsTextView = convertView.findViewById(R.id.textview_sets);
        TextView repsTextView = convertView.findViewById(R.id.textview_reps);
        ImageButton deleteButton = convertView.findViewById(R.id.delete_button);

        nameTextView.setText(exercise.getName());
        setsTextView.setText("Sets: " + exercise.getSets());
        repsTextView.setText("Reps: " + exercise.getReps());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(exercise);
                }
            }
        });

        return convertView;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Exercise exercise);
    }
}






package com.example.fitnet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class presetroutine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presetroutine);

        // Get the table layout from the XML
        TableLayout tableLayout = findViewById(R.id.table_layout);

        // Set the header
        TextView headerTextView = findViewById(R.id.header_text_view);
        headerTextView.setText("Preset Routine");

        // Create rows for the Push section
        TableRow pushHeaderRow = createHeaderRow("Push");
        TableRow benchPressRow = createDataRow("Bench Press", "3", "10");
        TableRow cableFlyRow = createDataRow("Cable Fly", "4", "10");
        TableRow tricepPulldownsRow = createDataRow("Tricep Pulldowns", "3", "10");
        TableRow shoulderPressRow = createDataRow("Shoulder Press", "3", "12");

        // Create rows for the Pull section
        TableRow pullHeaderRow = createHeaderRow("Pull");
        TableRow barbellRowsRow = createDataRow("Barbell Rows", "3", "10");
        TableRow PullupsRow = createDataRow("Pull ups", "2", "15");
        TableRow dumbellcurlRow = createDataRow("Dumbbell Curl", "3", "10");
        TableRow facepullsRow = createDataRow("Shoulder Press", "3", "10");

        // Create rows for the Legs section
        TableRow legsHeaderRow = createHeaderRow("Legs");
        TableRow squatsRow = createDataRow("Squats", "3", "8");
        TableRow legextensionsRow = createDataRow("Leg Extensions", "3", "10");
        TableRow hamstringcurlsRow = createDataRow("Hamstring Curls", "3", "10");
        TableRow calfraisesRow = createDataRow("Calf Raises", "3", "12");

        // Add the rows to the table layout
        tableLayout.addView(pushHeaderRow);
        tableLayout.addView(benchPressRow);
        tableLayout.addView(cableFlyRow);
        tableLayout.addView(tricepPulldownsRow);
        tableLayout.addView(shoulderPressRow);
        tableLayout.addView(pullHeaderRow);
        tableLayout.addView(barbellRowsRow);
        tableLayout.addView(PullupsRow);
        tableLayout.addView(dumbellcurlRow);
        tableLayout.addView(facepullsRow);
        tableLayout.addView(legsHeaderRow);
        tableLayout.addView(squatsRow);
        tableLayout.addView(legextensionsRow);
        tableLayout.addView(hamstringcurlsRow);
        tableLayout.addView(calfraisesRow);
    }

    // Helper method to create header rows
    private TableRow createHeaderRow(String sectionName) {
        TableRow headerRow = new TableRow(this);

        TextView sectionTextView = new TextView(this);
        sectionTextView.setText(sectionName);
        sectionTextView.setPadding(20, 10, 10, 10);
        headerRow.addView(sectionTextView);

        TextView setsTextView = new TextView(this);
        setsTextView.setText("Sets");
        setsTextView.setPadding(20, 10, 10, 10);
        headerRow.addView(setsTextView);

        TextView repsTextView = new TextView(this);
        repsTextView.setText("Reps");
        repsTextView.setPadding(20, 10, 10, 10);
        headerRow.addView(repsTextView);

        return headerRow;
    }

    // Helper method to create data rows
    private TableRow createDataRow(String exerciseName, String sets, String reps) {
        TableRow dataRow = new TableRow(this);

        TextView exerciseTextView = new TextView(this);
        exerciseTextView.setText(exerciseName);
        exerciseTextView.setPadding(20, 10, 10, 10);
        dataRow.addView(exerciseTextView);

        TextView setsTextView = new TextView(this);
        setsTextView.setText(sets);
        setsTextView.setPadding(20, 10, 10, 10);
        dataRow.addView(setsTextView);

        TextView repsTextView = new TextView(this);
        repsTextView.setText(reps);
        repsTextView.setPadding(20, 10, 10, 10);
        dataRow.addView(repsTextView);

        return dataRow;
    }
}
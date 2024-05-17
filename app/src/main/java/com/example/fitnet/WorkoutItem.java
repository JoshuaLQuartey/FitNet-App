package com.example.fitnet;



public class WorkoutItem {
    public String item;
    public String weight;
    public String reps;
    public String record;

    public WorkoutItem() {
        // Default constructor required for calls to DataSnapshot.getValue(WorkoutItem.class)
    }

    public WorkoutItem(String item, String weight, String reps, String record) {
        this.item = item;
        this.weight = weight;
        this.reps = reps;
        this.record = record;
    }
}

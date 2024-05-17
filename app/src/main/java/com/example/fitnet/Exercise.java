package com.example.fitnet;

public class Exercise {
    private String exerciseId;
    private String name;
    private String sets;
    private String reps;

    public Exercise() {
        // Default constructor required for Firebase
    }

    public Exercise(String name, String sets, String reps) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getName() {
        return name;
    }

    public String getSets() {
        return sets;
    }

    public String getReps() {
        return reps;
    }
}









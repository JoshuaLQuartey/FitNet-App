package com.example.fitnet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {

    private static final DataManager instance = new DataManager();

    private Map<String, List<Exercise>> exercisesByDay = new HashMap<>();

    private DataManager() {
        // Initialize exercises for each day
        exercisesByDay.put("Monday", new ArrayList<>());
        exercisesByDay.put("Tuesday", new ArrayList<>());
        exercisesByDay.put("Wednesday", new ArrayList<>());
        exercisesByDay.put("Thursday", new ArrayList<>());
        exercisesByDay.put("Friday", new ArrayList<>());
        exercisesByDay.put("Saturday", new ArrayList<>());
        exercisesByDay.put("Sunday", new ArrayList<>());
    }

    public static DataManager getInstance() {
        return instance;
    }

    public List<Exercise> getMondayExercises() {
        return exercisesByDay.get("Monday");
    }

    public List<Exercise> getTuesdayExercises() {
        return exercisesByDay.get("Tuesday");
    }

    public List<Exercise> getWednesdayExercises() {
        return exercisesByDay.get("Wednesday");
    }

    public List<Exercise> getThursdayExercises() {
        return exercisesByDay.get("Thursday");
    }

    public List<Exercise> getFridayExercises() {
        return exercisesByDay.get("Friday");
    }

    public List<Exercise> getSaturdayExercises() {
        return exercisesByDay.get("Saturday");
    }

    public List<Exercise> getSundayExercises() {
        return exercisesByDay.get("Sunday");
    }

    public void addExercise(String day, Exercise exercise) {
        List<Exercise> exercises = exercisesByDay.get(day);
        if (exercises != null) {
            exercises.add(exercise);
        }
    }

}

package com.example.fitnet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    private EditText heightEditText, weightEditText, weightGoalEditText;
    private RadioGroup genderRadioGroup;
    private SharedPreferences sharedPreferences;
    private static final String PROFILE_PREFERENCES = "ProfilePreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        heightEditText = findViewById(R.id.editTextHeight);
        weightEditText = findViewById(R.id.editTextWeight);
        weightGoalEditText = findViewById(R.id.editTextWeightGoal);
        genderRadioGroup = findViewById(R.id.radioGroupGender);
        Button saveButton = findViewById(R.id.buttonSave);

        textView = findViewById(R.id.textView);

        sharedPreferences = getSharedPreferences(PROFILE_PREFERENCES, MODE_PRIVATE);

        heightEditText.setText(sharedPreferences.getString("height", ""));
        weightEditText.setText(sharedPreferences.getString("weight", ""));
        weightGoalEditText.setText(sharedPreferences.getString("weightGoal", ""));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveProfile() {
        String heightString = heightEditText.getText().toString();
        String weightString = weightEditText.getText().toString();
        String weightGoalString = weightGoalEditText.getText().toString();

        if (heightString.isEmpty() || weightString.isEmpty() || weightGoalString.isEmpty()) {
            Toast.makeText(Profile.this, "Please enter your height, weight, and weight goal", Toast.LENGTH_SHORT).show();
            return;
        }

        int height = Integer.parseInt(heightString);
        int weight = Integer.parseInt(weightString);
        int weightGoal = Integer.parseInt(weightGoalString);

        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedGenderRadioButton = findViewById(selectedGenderId);

        String gender = selectedGenderRadioButton.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("height", heightString);
        editor.putString("weight", weightString);
        editor.putString("weightGoal", weightGoalString);
        editor.apply();

        Toast.makeText(Profile.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
    }
}

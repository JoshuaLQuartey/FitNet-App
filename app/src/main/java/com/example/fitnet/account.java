package com.example.fitnet;

import static com.example.fitnet.MainActivity.NOTIFICATION_HOUR;
import static com.example.fitnet.MainActivity.NOTIFICATION_MINUTE;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnet.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class account extends Fragment {
    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    private DatabaseReference mDatabase;

    private SwitchMaterial notificationsSwitch;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        Button ProfileButton = view.findViewById(R.id.profile_button);
        Button ShareButton = view.findViewById(R.id.share_button);

        // Get reference to Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Set up notifications switch
        notificationsSwitch = view.findViewById(R.id.notifications_switch);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        notificationsSwitch.setChecked(sharedPreferences.getBoolean("notifications_enabled", true));
        notificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("notifications_enabled", isChecked);
                editor.apply();

                if (isChecked) {
                    // Enable notifications
                    enableNotifications();
                } else {
                    // Disable notifications
                    disableNotifications();
                }
            }
        });

        ProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Profile.class);
            startActivity(intent);
        });

        ShareButton.setOnClickListener(v -> {
            // Retrieve workout_items from Firebase Realtime Database
            mDatabase.child("workout_items").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> workoutItemsList = new ArrayList<>();
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        workoutItemsList.add(itemSnapshot.getValue().toString());
                    }
                    String workoutItems = TextUtils.join(", ", workoutItemsList);

                    // Create share intent
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Workout Items");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, workoutItems);

                    // Start share activity
                    startActivity(Intent.createChooser(shareIntent, "Share via"));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        });

        return view;
    }

    private void enableNotifications() {
        // Schedule daily notification to remind user to go to the gym
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR);
        calendar.set(Calendar.MINUTE, NOTIFICATION_MINUTE);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void disableNotifications() {
        // Cancel the notification
        Intent intent = new Intent(getContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}

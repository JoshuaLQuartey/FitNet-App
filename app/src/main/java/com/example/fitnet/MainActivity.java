package com.example.fitnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;

    public static final int NOTIFICATION_HOUR = 12; // 12 PM
    public static final int NOTIFICATION_MINUTE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNav);
        viewPager2 = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.bottom_workout:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.bottom_routine:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.bottom_account:
                        viewPager2.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_workout).setChecked(true);
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_routine).setChecked(true);
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_account).setChecked(true);
                }
                super.onPageSelected(position);
            }
        });
        // Schedule daily notification to remind user to go to the gym
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR);
        calendar.set(Calendar.MINUTE, NOTIFICATION_MINUTE);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}






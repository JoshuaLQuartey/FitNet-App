package com.example.fitnet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class RoutineFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine, container, false);

        Button myRoutineButton = view.findViewById(R.id.myRoutineButton);
        Button presetRoutineButton = view.findViewById(R.id.presetRoutineButton);

        myRoutineButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), myroutine.class);
            startActivity(intent);
        });

        presetRoutineButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), presetroutine.class);
            startActivity(intent);
        });



        return view;
    }
}
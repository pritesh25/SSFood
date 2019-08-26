package com.example.myapplication.fragments;


import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MethodFragment extends Fragment {

    private String rec_steps;
    private ConstraintLayout method_main;

    public MethodFragment(String rec_steps) {
        this.rec_steps = rec_steps;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_method, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        TextView stepTextView = view.findViewById(R.id.stepTextView);
        method_main = view.findViewById(R.id.method_main);
        method_main.setBackgroundColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.colorWhite));

        stepTextView.setMovementMethod(new ScrollingMovementMethod());
        stepTextView.setText(rec_steps.replace("Step ", "\n\nStep "));

    }
}

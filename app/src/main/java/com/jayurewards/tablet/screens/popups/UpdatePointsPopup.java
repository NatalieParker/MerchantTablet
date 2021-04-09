package com.jayurewards.tablet.screens.popups;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jayurewards.tablet.R;

public class UpdatePointsPopup extends DialogFragment {
    private static final String TAG = "UpdatePointsPopup";

    private SwitchMaterial timeoutSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_update_points, container, false);


        timeoutSwitch = view.findViewById(R.id.switchUpdatePtsPopup);
        timeoutSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Log.i(TAG, "onCreateView: \n SWITCH ON: " + isChecked);
            } else {
                Log.i(TAG, "onCreateView: \n SWITCH OFF " + isChecked);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null || getActivity() == null) return;

        // Set width of popup
        int screenWidth = (getActivity().getResources().getDisplayMetrics().widthPixels);
        int width = (int) Math.round(screenWidth * 0.60);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width, height);

        // Make dialog background transparent if using rounded corners
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
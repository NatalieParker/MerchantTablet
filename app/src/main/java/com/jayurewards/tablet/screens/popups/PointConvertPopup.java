package com.jayurewards.tablet.screens.popups;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jayurewards.tablet.R;

public class PointConvertPopup extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_point_convert, container, false);



        // Show Keyboard
//        pinEditText.post(() -> {
//            pinEditText.requestFocus();
//            InputMethodManager imm = (InputMethodManager)pinEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (imm != null) imm.showSoftInput(pinEditText, InputMethodManager.SHOW_IMPLICIT);
//        });



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
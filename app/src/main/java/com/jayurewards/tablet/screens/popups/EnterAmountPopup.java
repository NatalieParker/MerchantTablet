package com.jayurewards.tablet.screens.popups;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;
import com.jayurewards.tablet.helpers.LogHelper;

import java.text.NumberFormat;
import java.util.Locale;

public class EnterAmountPopup extends DialogFragment {
    private static final String TAG = "EnterAmountPopup";

    public interface EnterAmountInterface {
        void onEnterAmountSubmit(long points);
    }

    private EnterAmountInterface listener;

    private TextView desc;
    private EditText amountInput;
    private MaterialButton submitBtn;

    private String phonePassed;
    private long pointsPassed;
    private long amountPassed;

    private long pointsToGive;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_enter_amount, container, false);

        desc = view.findViewById(R.id.textPopupEnterAmountDesc);
        amountInput = view.findViewById(R.id.editTextEnterAmount);
        submitBtn = view.findViewById(R.id.buttonPopupEnterAmountSubmit);
        MaterialButton cancelBtn = view.findViewById(R.id.buttonPopupEnterAmountCancel);

        amountInput.addTextChangedListener(textWatcher);

        setCancelable(false);
        enableEmailSubmit(false);

        if (getArguments() != null) {
            phonePassed = getArguments().getString(GlobalConstants.ENTERED_PHONE);
            pointsPassed = getArguments().getLong(GlobalConstants.PT_CONVERT_POINTS);
            amountPassed = getArguments().getLong(GlobalConstants.PT_CONVERT_AMOUNT);

            String initialDesc = "Calculate the points to give customer " + phonePassed + ".";
            desc.setText(initialDesc);
        }

        // Show Keyboard
        amountInput.post(() -> {
            amountInput.requestFocus();
            InputMethodManager imm = (InputMethodManager) amountInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.showSoftInput(amountInput, InputMethodManager.SHOW_IMPLICIT);
        });

        submitBtn.setOnClickListener(v -> {
            listener.onEnterAmountSubmit(pointsToGive);
            dismiss();
        });

        cancelBtn.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null || getActivity() == null) return;

        // Set width of popup
        int screenWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
        int width = (int) Math.round(screenWidth * 0.60);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width, height);

        // Make dialog background transparent if using rounded corners
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }



    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String amount = amountInput.getText().toString();
            if (!amount.equals("") && !amount.endsWith(".") && !amount.endsWith(".0")) {
                amountInput.removeTextChangedListener(textWatcher);
                String amountStripped = stripNumberFormatting(amount);
                try {
                    double amountDouble = Double.parseDouble(amountStripped);
                    String amountFormatted = NumberFormat.getNumberInstance(Locale.getDefault()).format(amountDouble);

                    calculatePoints(amountDouble);

                    amountInput.setText(amountFormatted);
                    amountInput.setSelection(amountInput.getText().toString().length());
                    amountInput.addTextChangedListener(textWatcher);

                } catch (Throwable t) {
                    String errorMessage = "Parsing amount to long error";
                    LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.PARSING);
                    AlertHelper.showAlert(getContext(), "Number Error", "Please only enter numbers into the fields.");
                }
            }

            checkForEmptyField();
        }
    };

    private void checkForEmptyField() {
        String amountString = amountInput.getText().toString();
        enableEmailSubmit(!amountString.isEmpty());
    }

    private void enableEmailSubmit(boolean enabled) {
        if (getActivity() == null) return;

        if (!enabled) {
            submitBtn.setBackgroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.colorPrimaryLight)));
            submitBtn.setEnabled(false);
        } else {
            submitBtn.setBackgroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.colorPrimary)));
            submitBtn.setEnabled(true);
        }
    }

    private void calculatePoints(double amountDouble) {
        double pointsDouble = amountDouble / amountPassed;
        pointsToGive = (long) pointsDouble * (long) pointsPassed;

        String pointString = pointsToGive + " points";
        if (pointsToGive == 1) {
            pointString = "1 point";
        }

        Log.i(TAG, "AMOUNT PASSED IN FUNCTION: " + amountDouble);
        Log.i(TAG, "AMOUNT BASE: " + amountPassed);
        Log.i(TAG, "AMOUNT PASSED / AMOUNT BASE: " + pointsDouble);
        Log.i(TAG, "PASSED POINTS: " + pointsPassed);
        Log.i(TAG, "PASSED POINTS * MULTIPLIER: " + pointsToGive);

        String description = "Give " + pointString + " to customer " + phonePassed + ".";
        desc.setText(description);
    }

    private String stripNumberFormatting(String number) {
        String numberStripped1 = number.replaceAll(",", "");
        return numberStripped1.replaceAll(" ", "");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof EnterAmountInterface) {
            listener = (EnterAmountInterface) context;

        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
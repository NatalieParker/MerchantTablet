package com.jayurewards.tablet.screens.popups;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;
import com.jayurewards.tablet.helpers.LogHelper;

import java.text.NumberFormat;
import java.util.Locale;

public class PointConvertPopup extends DialogFragment {
    private static final String TAG = "PointConvertPopup";

    public interface PointConvertInterface {
        void onPointConvertSubmit(boolean activated);
    }

    private PointConvertInterface listener;

    private EditText ptsInput;
    private EditText amountInput;
    private TextView calculatedExample;
    private MaterialButton submitBtn;

    private String dialingCode;
    private boolean activated;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_point_convert, container, false);

        ptsInput = view.findViewById(R.id.editTextPtsConvertPoints);
        amountInput = view.findViewById(R.id.editTextPtsConvertAmount);
        SwitchMaterial convertSwitch = view.findViewById(R.id.switchPtsConvertActivate);
        calculatedExample = view.findViewById(R.id.textPtsConvertPopupDisclaimer);
        submitBtn = view.findViewById(R.id.buttonPtsConvertPopupSubmit);
        MaterialButton cancelBtn = view.findViewById(R.id.buttonPtsConvertPopupCancel);

        ptsInput.addTextChangedListener(textWatcher);
        amountInput.addTextChangedListener(textWatcher);


        setCancelable(false);

        if (getArguments() != null) {
            dialingCode = getArguments().getString(GlobalConstants.DIALING_CODE);
            long convertPoints = getArguments().getLong(GlobalConstants.PT_CONVERT_POINTS);
            long convertAmount = getArguments().getLong(GlobalConstants.PT_CONVERT_AMOUNT);
            activated = getArguments().getBoolean(GlobalConstants.PT_CONVERT_ACTIVATED);

            if (convertPoints >= 1) {
                String pointFormatted = NumberFormat.getNumberInstance(Locale.getDefault()).format(convertPoints);
                ptsInput.setText(pointFormatted);
            }

            if (convertAmount >= 1) {
                String amountFormatted = NumberFormat.getNumberInstance(Locale.getDefault()).format(convertAmount);
                amountInput.setText(amountFormatted);
            }

            convertSwitch.setChecked(activated);
        }

        generateCalculatedExampleText();
        enableSubmitBtn(false);

        convertSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activated = isChecked;
            checkForEmptyField();
        });

        submitBtn.setOnClickListener(v -> {
            if (getActivity() == null) return;

            String pointStripped = stripNumberFormatting(ptsInput.getText().toString());
            String amountStripped = stripNumberFormatting(amountInput.getText().toString());

            try {
                long pointLong = !"".equals(pointStripped) ? Long.parseLong(pointStripped) : 0;
                long amountLong = !"".equals(amountStripped) ? Long.parseLong(amountStripped) : 0;

                SharedPreferences sp = getActivity().getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putLong(GlobalConstants.PT_CONVERT_POINTS, pointLong);
                editor.putLong(GlobalConstants.PT_CONVERT_AMOUNT, amountLong);
                editor.putBoolean(GlobalConstants.PT_CONVERT_ACTIVATED, activated);
                editor.apply();

                listener.onPointConvertSubmit(activated);

                dismiss();

            } catch (Throwable t) {
                String errorMessage = "Parsing point or amount to long error";
                LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.PARSING);
                AlertHelper.showAlert(getContext(), "Number Error", "Please only enter numbers into the fields.");
            }
        });

        cancelBtn.setOnClickListener(v -> dismiss());

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

    /**
     * EditText methods
     */
    private final TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            ptsInput.removeTextChangedListener(textWatcher);
            amountInput.removeTextChangedListener(textWatcher);

            String point = ptsInput.getText().toString();
            String amount = amountInput.getText().toString();

            // Format number based on location (e.g add commas)
            if (!point.equals("")) {
                String pointStripped = stripNumberFormatting(point);
                try {
                    long pointLong = Long.parseLong(pointStripped);
                    String pointFormatted = NumberFormat.getNumberInstance(Locale.getDefault()).format(pointLong);
                    ptsInput.setText(pointFormatted);
                    ptsInput.setSelection(ptsInput.getText().toString().length());

                } catch (Throwable t) {
                    String errorMessage = "Parsing point to long error";
                    LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.PARSING);
                    AlertHelper.showAlert(getContext(), "Number Error", "Please only enter numbers into the fields.");
                }
            }

            if (!amount.equals("")) {
                String amountStripped = stripNumberFormatting(amount);
                try {
                    long amountLong = Long.parseLong(amountStripped);
                    String amountFormatted = NumberFormat.getNumberInstance(Locale.getDefault()).format(amountLong);
                    amountInput.setText(amountFormatted);
                    amountInput.setSelection(amountInput.getText().toString().length());

                } catch (Throwable t) {
                    String errorMessage = "Parsing amount to long error";
                    LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.PARSING);
                    AlertHelper.showAlert(getContext(), "Number Error", "Please only enter numbers into the fields.");
                }
            }

            ptsInput.addTextChangedListener(textWatcher);
            amountInput.addTextChangedListener(textWatcher);

            checkForEmptyField();
            generateCalculatedExampleText();
        }
    };

    private void checkForEmptyField() {
        String point = ptsInput.getText().toString();
        String amount = amountInput.getText().toString();

        if (activated) {
            enableSubmitBtn(!"".equals(point) && !"0".equals(point)
                    && !"".equals(amount) && !"0".equals(amount));
        } else {
            enableSubmitBtn(true);
        }
    }

    private String stripNumberFormatting(String number) {
        String numberStripped1 = number.replaceAll(",", "");
        String numberStripped2 = numberStripped1.replaceAll("/.", "");
        return numberStripped2.replaceAll(" ", "");
    }

    private void enableSubmitBtn(boolean enabled) {
        if (getActivity() == null) return;

        if (!enabled) {
            submitBtn.setBackgroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.colorPrimaryLight)));
            submitBtn.setEnabled(false);
        } else {
            submitBtn.setBackgroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.colorPrimary)));
            submitBtn.setEnabled(true);
        }
    }

    private void generateCalculatedExampleText() {
        String point = !"".equals(ptsInput.getText().toString()) ? ptsInput.getText().toString() : "0";
        String amount = !"".equals(amountInput.getText().toString()) ? amountInput.getText().toString() : "0";

        String pointString = point + " points";
        if (point.equals("1")) {
            pointString = "1 point";
        }

        String amountString;
        if (amount.equals("1")) {
            if (dialingCode != null && dialingCode.equals("63")){
                amountString = "1 peso";
            } else {
                amountString = "1 dollar";
            }
        } else {
            if (dialingCode != null && dialingCode.equals("63")){
                amountString = amount + " pesos";
            } else {
                amountString = amount + " dollars";
            }
        }

        String example = "Customers get " + pointString + " for every " + amountString + " spent.";
        calculatedExample.setText(example);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PointConvertInterface) {
            listener = (PointConvertInterface) context;

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
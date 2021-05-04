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
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;

import java.text.NumberFormat;
import java.util.Locale;

public class UpdatePointsPopup extends DialogFragment {
    private static final String TAG = "UpdatePointsPopup";

    public interface UpdatePtsPopupInterface {
        void onUpdatePoints(int points, int adminLevel);
    }

    private UpdatePtsPopupInterface listener;

    private int adminLevel = 1;

    private MaterialButton updateBtn;
    private TextInputLayout ptsTextInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_update_points, container, false);

        TextView ptsAmtString = view.findViewById(R.id.textPopupUpdatePtsAmount);
        SwitchMaterial timeoutSwitch = view.findViewById(R.id.switchUpdatePtsPopup);
        updateBtn = view.findViewById(R.id.buttonUpdatePtsPopupSubmit);
        MaterialButton cancelBtn = view.findViewById(R.id.buttonUpdatePtsPopupCancel);
        ptsTextInput = view.findViewById(R.id.textInputPopupUpdatePtsAmount);

        setCancelable(false);

        if (ptsTextInput.getEditText() != null) {
            ptsTextInput.getEditText().addTextChangedListener(textWatcher);
        }

        if (getArguments() != null) {
            long defaultPoints = getArguments().getLong(GlobalConstants.POINT_AMOUNT);
            adminLevel = getArguments().getInt(GlobalConstants.ADMIN_LEVEL);

            String ptsAmount = defaultPoints + " points per customer";
            if (defaultPoints == 1) {
                ptsAmount = "1 point per customer";
            }

            ptsAmtString.setText(ptsAmount);
            ptsTextInput.getEditText().setText(String.valueOf(defaultPoints));
            timeoutSwitch.setChecked(adminLevel == 1);
        }

        timeoutSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                adminLevel = 1;
            } else {
                adminLevel = 0;
            }

            updateBtn.setEnabled(true);
        });

        updateBtn.setOnClickListener(v -> {
            String pointsFormatted = ptsTextInput.getEditText().getText().toString();
            String points = stripNumberFormatting(pointsFormatted);

            int amount;
            try {
                amount = Integer.parseInt(points);
            } catch (Throwable t) {
                Log.e(TAG, "Parsing point amount to integer error");
                AlertHelper.showAlert(getActivity(), "Parsing Error", "Please ensure the points field only has numbers.");
                return;
            }

            if (amount < 1) {
                AlertHelper.showAlert(getActivity(), "Point Error", "You must give the customer at least 1 point.");
                return;
            }

            listener.onUpdatePoints(amount, adminLevel);
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
        int screenWidth = (getActivity().getResources().getDisplayMetrics().widthPixels);
        int width = (int) Math.round(screenWidth * 0.60);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width, height);

        // Make dialog background transparent if using rounded corners
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    /**
     * Edit Text
     */
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (ptsTextInput.getEditText() == null) return;

            ptsTextInput.getEditText().removeTextChangedListener(this);
            String numberString = ptsTextInput.getEditText().getText().toString();

            // Format number based on location (e.g add commas)
            if (!numberString.equals("")) {
                String numberStripped = stripNumberFormatting(numberString);

                int numberDouble;
                try {
                    numberDouble = Integer.parseInt(numberStripped);
                    String formattedNumber = NumberFormat.getNumberInstance(Locale.getDefault()).format(numberDouble);
                    ptsTextInput.getEditText().setText(formattedNumber);
                    ptsTextInput.getEditText().setSelection(ptsTextInput.getEditText().getText().toString().length());

                } catch (Throwable t) {
                    Log.e(TAG, "Parsing point amount to double error");
                }
            }

            ptsTextInput.getEditText().addTextChangedListener(this);

            checkForEmptyField();
        }
    };

    private void checkForEmptyField() {
        String points = ptsTextInput.getEditText().getText().toString();
        boolean goEnableButton = points.length() >= 1;
        enableEmailSubmit(goEnableButton);
    }

    private void enableEmailSubmit(boolean enabled) {
        if (getActivity() == null) return;

        if (!enabled) {
            updateBtn.setBackgroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.colorPrimaryLight)));
            updateBtn.setEnabled(false);
        } else {
            updateBtn.setBackgroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.colorPrimary)));
            updateBtn.setEnabled(true);
        }
    }

    private String stripNumberFormatting(String number) {
        String numberStripped1 = number.replaceAll(",", "");
        String numberStripped2 = numberStripped1.replaceAll("/.", "");
        return numberStripped2.replaceAll(" ", "");
    }

    /**
     * Interface listener
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UpdatePtsPopupInterface) {
            listener = (UpdatePtsPopupInterface) context;

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
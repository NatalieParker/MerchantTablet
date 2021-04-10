package com.jayurewards.tablet.screens.popups;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;


public class LockScreenPopup extends DialogFragment {
    private static final String TAG = "LockScreenPopup";

    public interface LockScreenInterface {
        void onUpdate(boolean isLocked);
    }
    private LockScreenInterface listener;

    private TextView header;
    private TextView desc;
    private EditText pinEditText;
    private MaterialButton cancelBtn;
    private MaterialButton submitBtn;

    private SharedPreferences sp;
    private int savedPin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_lock_screen, container, false);

        header = view.findViewById(R.id.textPopupLockScreenHeader);
        desc = view.findViewById(R.id.textPopupLockScreenDesc);
        pinEditText = view.findViewById(R.id.editTextLockScreen);
        cancelBtn = view.findViewById(R.id.buttonPopupLockScreenCancel);
        submitBtn = view.findViewById(R.id.buttonPopupLockScreenSubmit);

        pinEditText.addTextChangedListener(textWatcher);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        savedPin = sp.getInt(GlobalConstants.PIN_CODE, 0);

        if (savedPin == 0) {
            header.setText(R.string.lock_screen);
            desc.setText(R.string.lock_screen_popup_desc_lock);
        } else {
            header.setText(R.string.unlock_screen);
            desc.setText(R.string.lock_screen_popup_desc_unlock);
        }

        enableEmailSubmit(false);


        submitBtn.setOnClickListener(v -> {

            int pinCode;
            try {
                pinCode = Integer.parseInt(pinEditText.getText().toString());
            } catch (Throwable t) {
                Log.e(TAG, "Parsing pin code amount to integer error");
                AlertHelper.showAlert(getActivity(), "Parsing Error", "Please ensure the pin code only has numbers.");
                return;
            }

            SharedPreferences.Editor editor = sp.edit();
            if (savedPin == 0) {
                editor.putInt(GlobalConstants.PIN_CODE, pinCode);
                editor.apply();
                listener.onUpdate(true);
                dismiss();
            } else if (savedPin == pinCode) {
                editor.putInt(GlobalConstants.PIN_CODE, 0);
                editor.apply();
                listener.onUpdate(false);
                dismiss();
            } else {
                AlertHelper.showAlert(getActivity(), "Incorrect Pin",
                        "The pin number entered was incorrect. Please try again or logout to reset the device.");
            }



            Log.i(TAG, "onCreateView: PIN CODE SUBMITTED: " + pinCode);

//            listener.onUpdate(amount, adminLevel);
//            dismiss();
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
            checkForEmptyField();
        }
    };

    private void checkForEmptyField() {
        String pinString = pinEditText.getText().toString();
        boolean goEnableButton = pinString.length() >= 4;
        enableEmailSubmit(goEnableButton);
    }

    private void enableEmailSubmit(boolean enabled) {
        if (!enabled) {
            submitBtn.setBackgroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.colorPrimaryLight)));
            submitBtn.setEnabled(false);
        } else {
            submitBtn.setBackgroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.colorPrimary)));
            submitBtn.setEnabled(true);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LockScreenInterface) {
            listener = (LockScreenInterface) context;

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
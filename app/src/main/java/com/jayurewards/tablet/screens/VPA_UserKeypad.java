package com.jayurewards.tablet.screens;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jayurewards.tablet.UKFeedFragment;
import com.jayurewards.tablet.UKOffersListFragment;

public class VPA_UserKeypad extends FragmentStateAdapter {

    private static final String TAG = "VPA_UserKeypad";
    int storeId;
    String[] strings;

    public VPA_UserKeypad(@NonNull FragmentActivity fragmentActivity, int storeId, String[] strings) {
        super(fragmentActivity);

        this.storeId = storeId;
        this.strings = strings;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.i(TAG, "\n\n\n IN FRAGEMENT ADAPTER: " + position);
        Log.i(TAG, "STRINGS: " + strings);
        switch (position) {
            case 0:
                return new UKFeedFragment();
            default:
                return UKOffersListFragment.newInstance(storeId);
        }


    }

    @Override
    public int getItemCount() {return 2;}


}

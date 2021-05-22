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
    int firstFragmentIndex = 1;


    public VPA_UserKeypad(@NonNull FragmentActivity fragmentActivity, int storeId, String[] strings) {
        super(fragmentActivity);

        this.storeId = storeId;
        this.strings = strings;
    }

    @Override
    public int getItemCount() {
        return strings.length + firstFragmentIndex;
    }

    @Override
    public boolean containsItem(long itemId) {
        return super.containsItem(itemId);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.i(TAG, "\n\n\n IN FRAGEMENT ADAPTER: " + position);
        Log.i(TAG, "STRINGS: " + strings);
        switch (position) {
            case 0:
                return UKOffersListFragment.newInstance(storeId);
            default:
                String url = strings[position - firstFragmentIndex];
                return UKFeedFragment.newInstance(url);
        }

    }


}

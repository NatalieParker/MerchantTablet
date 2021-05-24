package com.jayurewards.tablet.screens;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class VPA_UserKeypad extends FragmentStateAdapter {
    private static final String TAG = "VPA_UserKeypad";

    int storeId;
    String[] imageUrls;
    int firstFragmentIndex = 1;


    public VPA_UserKeypad(@NonNull FragmentActivity fragmentActivity, int storeId, String[] strings) {
        super(fragmentActivity);

        this.storeId = storeId;
        this.imageUrls = strings;
    }

    @Override
    public int getItemCount() {
        return imageUrls.length + firstFragmentIndex;
    }

    @Override
    public boolean containsItem(long itemId) {
        return super.containsItem(itemId);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return UKOffersListFragment.newInstance(storeId);
        }

        String url = imageUrls[position - firstFragmentIndex];
        return UKFeedFragment.newInstance(url);
    }


}

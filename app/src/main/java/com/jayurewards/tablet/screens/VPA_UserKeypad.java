package com.jayurewards.tablet.screens;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jayurewards.tablet.UKFeedFragment;
import com.jayurewards.tablet.UKOffersListFragment;

public class VPA_UserKeypad extends FragmentStateAdapter {

    int storeId;

    public VPA_UserKeypad(@NonNull FragmentActivity fragmentActivity, int storeId) {
        super(fragmentActivity);

        this.storeId = storeId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.i("TAG", "\n\n\n IN FRAGEMENT ADAPTER: " + position);
        switch (position) {
            case 0:
                return new UKFeedFragment();
            default:
                return new UKOffersListFragment().newInstance(storeId);
        }


    }

    @Override
    public int getItemCount() {return 2;}


}

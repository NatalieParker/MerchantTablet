package com.jayurewards.tablet.screens;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.gms.common.util.Strings;
import com.jayurewards.tablet.ViewPagerCard1Fragment;
import com.jayurewards.tablet.ViewPagerCard2Fragment;
import com.jayurewards.tablet.models.ShopAdminModel;
import com.jayurewards.tablet.networking.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VPA_UserKeypad extends FragmentStateAdapter {

    public VPA_UserKeypad(@NonNull FragmentActivity fragmentActivity) {super(fragmentActivity);}

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.i("TAG", "\n\n\n IN FRAGEMENT ADAPTER: " + position);
        switch (position) {
            case 0:
                return new ViewPagerCard1Fragment();
            default:
                return new ViewPagerCard2Fragment();
        }
    }

    @Override
    public int getItemCount() {return 2;}


}

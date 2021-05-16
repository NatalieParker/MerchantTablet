package com.jayurewards.tablet.screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jayurewards.tablet.R;

public class ViewPager2UserKeypad extends RecyclerView.Adapter<ViewPager2UserKeypad.MyViewHolder> {
    int[] images;

    public ViewPager2UserKeypad(int[] images){
        this.images = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_pager_2_user_keypad, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView topText;
        TextView money;
        TextView credit;
        ImageView design;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            design = itemView.findViewById(R.id.imageViewUserKeypadViewPagerDesign);
//            topText = itemView.findViewById(R.id.textViewUserKeypadViewPagerTryJayuWith);
//            money = itemView.findViewById(R.id.textViewUserKeypadViewPagerRewardsMoney);
//            credit = itemView.findViewById(R.id.textViewUserKeypadViewPagerRewardsCredit);
        }
    }
}

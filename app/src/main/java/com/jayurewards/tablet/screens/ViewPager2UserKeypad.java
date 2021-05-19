package com.jayurewards.tablet.screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jayurewards.tablet.R;
import com.jayurewards.tablet.models.OffersModel;

import java.util.ArrayList;

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

        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;

            if (images[position] == 0) {
                viewHolder.design.setVisibility(View.VISIBLE);
                viewHolder.scrollView.setVisibility(View.GONE);
                viewHolder.scrollView.setEnabled(false);
            } else {
                viewHolder.design.setVisibility(View.GONE);
                viewHolder.scrollView.setVisibility(View.VISIBLE);
                viewHolder.scrollView.setEnabled(true);
            }
        }

    }

    @Override
    public int getItemCount() {
        return images.length;
    }

//    private void startRecyclerView(ArrayList<OffersModel> offersList) {
//        RA_UserKeypad adapter = new RA_UserKeypad(offersList, this);
//        LinearLayoutManager lm = new LinearLayoutManager(this);
//        rv.setLayoutManager(lm);
//        rv.setAdapter(adapter);
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView topText;
        TextView money;
        TextView credit;
        ImageView design;
        NestedScrollView scrollView;
        RecyclerView rv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            design = itemView.findViewById(R.id.imageViewUserKeypadViewPagerDesign);
            scrollView = itemView.findViewById(R.id.scrollViewUserKeypadViewPager2Cards);
            rv = itemView.findViewById(R.id.recyclerViewUserKeypadViewPager2Cards);
//            topText = itemView.findViewById(R.id.textViewUserKeypadViewPagerTryJayuWith);
//            money = itemView.findViewById(R.id.textViewUserKeypadViewPagerRewardsMoney);
//            credit = itemView.findViewById(R.id.textViewUserKeypadViewPagerRewardsCredit);
        }
    }
}

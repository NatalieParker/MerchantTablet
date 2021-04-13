package com.jayurewards.tablet.screens;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jayurewards.tablet.GlideApp;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.models.OffersModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RA_UserKeypad extends RecyclerView.Adapter<RA_UserKeypad.VH_Rewards> {
    private static final String TAG = "RecyclerViewAdapter";

    ArrayList<OffersModel> offers;
    Context context;

    public RA_UserKeypad(ArrayList<OffersModel> offers, Context context) {
        this.offers = offers;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    @NonNull
    @Override
    public VH_Rewards onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_rewards_card, parent, false);


        VH_Rewards holder = new VH_Rewards(view);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "REWARDS CARD PRESSED");
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH_Rewards holder, int position) {
        Log.d(TAG, "onBindViewHolder: CALLED.");
        OffersModel offer = offers.get(position);

        holder.cardText.setText(offer.getDescription());
        holder.coin.setText(String.valueOf(offer.getPtsRequired()));

        if (offer.getType().equals("general")) {
            holder.pts.setText("Pts");
            holder.coin.setVisibility(View.VISIBLE);
        } else {
            holder.pts.setText(offer.getType());
            holder.coin.setVisibility(View.GONE);
        }
    }



    public class VH_Rewards extends RecyclerView.ViewHolder {

        CardView parentLayout;
        ConstraintLayout card;
        TextView coin;
        TextView pts;
        TextView cardText;

        public VH_Rewards(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.cardViewRecyclerRewardsRewardsCard);
            card = itemView.findViewById(R.id.constraintLayoutRecycleRewardsCard);
            coin = itemView.findViewById(R.id.textViewRecyclerRewardsPurpleCoin);
            pts = itemView.findViewById(R.id.textViewRecyclerRewardsPointsText);
            cardText = itemView.findViewById(R.id.textViewRecyclerRewardsCardText);

        }
    }
}

package com.jayurewards.tablet.screens;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.GlobalConstants;
import com.jayurewards.tablet.helpers.UtilsHelper;
import com.jayurewards.tablet.models.OffersModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RA_UserKeypad extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private final ArrayList<OffersModel> offers;
    private boolean bigScreen;

    public RA_UserKeypad(ArrayList<OffersModel> offers) {
        this.offers = offers;

        bigScreen = !(UtilsHelper.isScreenLarge());
    }

    @Override
    public int getItemViewType(int position) {
        if (offers.get(position).getType().equals(GlobalConstants.OFFER_TYPE_GENERAL)) {
            return R.layout.recycler_rewards_card;
        } else {
            return R.layout.recycler_specials_card;
        }
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final RecyclerView.ViewHolder holder;
        View view;

        if (viewType == R.layout.recycler_specials_card) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_specials_card, parent, false);
            holder = new VH_Specials(view);
            VH_Specials svh = (VH_Specials) holder;

            if (bigScreen) {
                svh.iconSpecials.getLayoutParams().width = 40;
                svh.iconSpecials.getLayoutParams().height = 40;

                svh.cardTextSpecials.setTextSize(30);
                svh.typeSpecials.setTextSize(25);
            }
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_rewards_card, parent, false);
            holder = new VH_Rewards(view);
            VH_Rewards rvh = (VH_Rewards) holder;

            if (bigScreen) {
                rvh.coinRewards.getLayoutParams().width = 40;
                rvh.coinRewards.getLayoutParams().height = 40;

                rvh.cardTextRewards.setTextSize(30);
                rvh.ptsRewards.setTextSize(25);
            }
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OffersModel offer = offers.get(position);

        if (holder instanceof VH_Specials) {
            VH_Specials specialsVH = (VH_Specials) holder;

            String type = offer.getType().substring(0, 1).toUpperCase() + offer.getType().substring(1).replaceAll("[_]", " ");

            specialsVH.cardTextSpecials.setText(offer.getDescription());
            specialsVH.typeSpecials.setText(type);

            switch (offers.get(position).getType()) {

                case GlobalConstants.OFFER_TYPE_SIGNUP:
                    specialsVH.iconSpecials.setImageResource(R.drawable.ic_gift);
                    specialsVH.typeSpecials.setText(R.string.signup_offer);
                    break;

                case GlobalConstants.OFFER_TYPE_REFERRAL:
                    specialsVH.iconSpecials.setImageResource(R.drawable.ic_user_plus);
                    break;

                case GlobalConstants.OFFER_TYPE_PROMO_HOURS:
                    specialsVH.iconSpecials.setImageResource(R.drawable.ic_clock);
                    specialsVH.typeSpecials.setText(R.string.happy_hours);
                    break;

                case GlobalConstants.OFFER_TYPE_PROMOTION:
                default:
                    specialsVH.iconSpecials.setImageResource(R.drawable.ic_tag);
                    break;
            }

        } else if (holder instanceof VH_Rewards) {
            VH_Rewards rewardsVH = (VH_Rewards) holder;
            rewardsVH.cardTextRewards.setText(offer.getDescription());

            String points = NumberFormat.getNumberInstance(Locale.getDefault()).format(offer.getPtsRequired());
            String offerPoints = points + " Pts";
            if (offer.getPtsRequired() == 1) offerPoints = "1 Pt";
            rewardsVH.ptsRewards.setText(offerPoints);
        }
    }


    public static class VH_Rewards extends RecyclerView.ViewHolder {

        CardView parentLayoutRewards;
        ConstraintLayout cardRewards;
        ImageView coinRewards;
        TextView ptsRewards;
        TextView cardTextRewards;

        public VH_Rewards(@NonNull View itemView) {
            super(itemView);
            parentLayoutRewards = itemView.findViewById(R.id.cardViewRecyclerRewardsRewardsCard);
            cardRewards = itemView.findViewById(R.id.constraintLayoutRecycleRewardsCard);
            coinRewards = itemView.findViewById(R.id.imageRecyclerRewardsCoin);
            ptsRewards = itemView.findViewById(R.id.textRecyclerRewardsPts);
            cardTextRewards = itemView.findViewById(R.id.textRecyclerRewardsDesc);

        }
    }

    public static class VH_Specials extends RecyclerView.ViewHolder {

        CardView parentLayoutSpecials;
        ConstraintLayout cardSpecials;
        TextView typeSpecials;
        TextView cardTextSpecials;
        ImageView iconSpecials;

        public VH_Specials(@NonNull View itemView) {
            super(itemView);

            parentLayoutSpecials = itemView.findViewById(R.id.cardViewRecyclerSpecialsSpecialsCard);
            cardSpecials = itemView.findViewById(R.id.constraintLayoutRecyclerSpecialsCard);
            typeSpecials = itemView.findViewById(R.id.textRecyclerSpecialsType);
            cardTextSpecials = itemView.findViewById(R.id.textRecyclerSpecialsDesc);
            iconSpecials = itemView.findViewById(R.id.imageRecyclerSpecialsIcon);

        }
    }
}

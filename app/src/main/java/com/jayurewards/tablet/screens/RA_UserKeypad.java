package com.jayurewards.tablet.screens;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jayurewards.tablet.GlideApp;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.GlobalConstants;
import com.jayurewards.tablet.models.OffersModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RA_UserKeypad extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    ArrayList<OffersModel> offers;
    Context context;

    public RA_UserKeypad(ArrayList<OffersModel> offers, Context context) {
        this.offers = offers;
        this.context = context;
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
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_rewards_card, parent, false);
            holder = new VH_Rewards(view);
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
                    specialsVH.typeSpecials.setText("Sign up Offer");
                    break;

                case GlobalConstants.OFFER_TYPE_REFERRAL:
                    specialsVH.iconSpecials.setImageResource(R.drawable.ic_user_plus);
                    break;

                case GlobalConstants.OFFER_TYPE_PROMO_HOURS:
                    specialsVH.iconSpecials.setImageResource(R.drawable.ic_clock);
                    specialsVH.typeSpecials.setText("Happy Hours");
                    break;

                case GlobalConstants.OFFER_TYPE_PROMOTION:
                default:
                    specialsVH.iconSpecials.setImageResource(R.drawable.ic_tag);
                    break;


            }

        } else if (holder instanceof VH_Rewards) {
            VH_Rewards rewardsVH = (VH_Rewards) holder;

            String offerPoints = offer.getPtsRequired() + " Pts";

            rewardsVH.cardTextRewards.setText(offer.getDescription());
            rewardsVH.ptsRewards.setText(offerPoints);

//            if (offer.getType().equals("general")) {
//                holder.ptsRewards.setText("Pts");
//                holder.coinRewards.setVisibility(View.VISIBLE);
//            } else {
//                holder.ptsRewards.setText(offer.getType());
//                holder.coinRewards.setVisibility(View.GONE);
//            }
        }

    }


    public class VH_Rewards extends RecyclerView.ViewHolder {

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

    public class VH_Specials extends RecyclerView.ViewHolder {

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

package com.jayurewards.tablet.screens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.DateTimeHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;
import com.jayurewards.tablet.helpers.LogHelper;
import com.jayurewards.tablet.helpers.UtilsHelper;
import com.jayurewards.tablet.models.OffersModel;
import com.jayurewards.tablet.networking.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UKOffersListFragment extends Fragment {
    private static final String TAG = "UkOffersListFragment";

    private UserKeypadActivity uka;
    private RecyclerView rv;
    private NestedScrollView nsv;

    private int storeId;  // Passed
    private ArrayList<OffersModel> offers = new ArrayList<>();

    // Required empty public constructor
    public UKOffersListFragment() {}

    public static UKOffersListFragment newInstance(int storeId) {
        UKOffersListFragment fragment = new UKOffersListFragment();
        Bundle args = new Bundle();
        args.putInt(GlobalConstants.STORE_ID, storeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            storeId = getArguments().getInt(GlobalConstants.STORE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uk_offers_list, container, false);
        uka = (UserKeypadActivity) getActivity();
        rv = view.findViewById(R.id.recyclerViewUKViewOffersList);
        nsv = view.findViewById(R.id.scrollViewUKOffersList);
        TextView header = view.findViewById(R.id.textUKOffersListHeader);

        getBusinessOffers(storeId);
        startRecyclerView(offers);

        LocalBroadcastManager.getInstance(uka).registerReceiver(scrollToTop,
                new IntentFilter(GlobalConstants.OFFERS_SCROLL_TOP));

        if (UtilsHelper.isScreenLarge()) header.setTextSize(60);

        return view;
    }

    private final BroadcastReceiver scrollToTop = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> nsv.smoothScrollTo(0, 0), 2000);
        }
    };

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(uka).unregisterReceiver(scrollToTop);
        super.onDestroy();
    }

    private void getBusinessOffers(int storeId) {
        Call<ArrayList<OffersModel>> call = RetrofitClient.getInstance().getRestOffers().getBusinessOffers(storeId);
        call.enqueue(new Callback<ArrayList<OffersModel>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<OffersModel>> call, @NonNull Response<ArrayList<OffersModel>> response) {
                offers = response.body();

                List<String> types = Arrays.asList(GlobalConstants.OFFER_TYPES_ARRAY);
                ArrayList<OffersModel> rewards = new ArrayList<>();
                ArrayList<OffersModel> specials = new ArrayList<>();
                OffersModel signUp = new OffersModel();

                for (int i = 0; i < offers.size(); i++) {
                    OffersModel offer = offers.get(i);

                    if (offer.getType().equals(GlobalConstants.OFFER_TYPE_GENERAL)) {
                        rewards.add(offer);
                    } else if (offer.getType().equals(GlobalConstants.OFFER_TYPE_SIGNUP)) {
                        signUp = offer;
                    } else {
                        specials.add(offer);
                    }

                    if (offer.getStartDate() != null && !"".equals(offer.getStartDate())
                            && offer.getEndDate() != null && !"".equals(offer.getEndDate())) {

                        Date startDate = DateTimeHelper.parseDateStringToDate(offer.getStartDate());
                        Date endDate = DateTimeHelper.parseDateStringToDate(offer.getEndDate());

                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault());
                        String startDateString = dateFormat.format(startDate);
                        String endDateString = dateFormat.format(endDate);

                        offers.get(i).setStartDate(startDateString);
                        offers.get(i).setEndDate(endDateString);
                    }
                }

                rewards.sort((o1, o2) -> Integer.compare(o1.getPtsRequired(), o2.getPtsRequired()));
                specials.sort((o1, o2) -> types.indexOf(o1.getType()) - types.indexOf(o2.getType()));

                ArrayList<OffersModel> of = new ArrayList<>();
                if (signUp.getOfferId() != 0) of.add(signUp);
                if (rewards.size() >= 1) of.addAll(rewards);
                if (specials.size() >= 1) of.addAll(specials);
                uka.hideSpinner();
                startRecyclerView(of);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<OffersModel>> call, @NonNull Throwable t) {
                String errorMessage = "Get Business Offers Error";
                LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.NETWORK);
                uka.hideSpinner();
                AlertHelper.showNetworkAlert(getActivity());
            }
        });
    }

    private void startRecyclerView(ArrayList<OffersModel> offersList) {
        RA_UserKeypad adapter = new RA_UserKeypad(offersList);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
    }

}
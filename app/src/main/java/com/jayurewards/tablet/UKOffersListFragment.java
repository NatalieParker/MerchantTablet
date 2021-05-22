package com.jayurewards.tablet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.DateTimeHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;
import com.jayurewards.tablet.helpers.LogHelper;
import com.jayurewards.tablet.models.OffersModel;
import com.jayurewards.tablet.networking.RetrofitClient;
import com.jayurewards.tablet.screens.RA_UserKeypad;
import com.jayurewards.tablet.screens.UserKeypadActivity;

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

    private int storeId;  // Passed
    private UserKeypadActivity uka;
    private ArrayList<OffersModel> offers = new ArrayList<>();
    private RecyclerView rv;

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
        rv = view.findViewById(R.id.recyclerViewUserKeypadViewPagerCard2Rewards);
        uka = (UserKeypadActivity) getActivity();

        getBusinessOffers(storeId);
        startRecyclerView(offers);

        return view;
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
        RA_UserKeypad adapter = new RA_UserKeypad(offersList, getActivity());
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
    }

}
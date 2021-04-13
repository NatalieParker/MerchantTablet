package com.jayurewards.tablet.networking;

import com.jayurewards.tablet.models.MerchantModel;
import com.jayurewards.tablet.models.OffersModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestOffers {

    @GET("/merchant-portal/business-offers/{storeId}")
    Call<ArrayList<OffersModel>> getBusinessOffers(@Path("storeId") int storeId);

}

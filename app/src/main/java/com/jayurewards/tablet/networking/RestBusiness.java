package com.jayurewards.tablet.networking;

import com.jayurewards.tablet.models.ShopAdminModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestBusiness {

    @GET("/merchant-app/get-shops/{merchantId}")
    Call<ArrayList<ShopAdminModel>> getMerchantShops(@Path("merchantId") int merchantId);
}

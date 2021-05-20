package com.jayurewards.tablet.networking;

import com.google.android.gms.common.util.Strings;
import com.jayurewards.tablet.models.ShopAdminModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestShops {
    // Get merchant shop information
    @GET("/merchant-app/get-shops/{merchantId}")
    Call<ArrayList<ShopAdminModel>> getMerchantShops(@Path("merchantId") int merchantId);

    @GET("/merchant-tablet/feed/{storeId}")
    Call<String[]> getTabletFeeds(@Path("storeId") int storeId);

}

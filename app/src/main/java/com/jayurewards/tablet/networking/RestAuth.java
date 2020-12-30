package com.jayurewards.tablet.networking;

import com.jayurewards.tablet.models.MerchantModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestAuth {

//     Get Merchant Data
    @GET("/merchant/get-info/{firebaseUid}")
    Call<MerchantModel> getMerchant(@Path("firebaseUid") String firebaseUid);

}

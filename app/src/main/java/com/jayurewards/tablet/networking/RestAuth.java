package com.jayurewards.tablet.networking;

import com.jayurewards.tablet.models.CheckSubscriptionParams;
import com.jayurewards.tablet.models.CheckSubscriptionResponse;
import com.jayurewards.tablet.models.MerchantModel;
import com.jayurewards.tablet.models.UpdateSubscriptionStatus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestAuth {

//     Get Merchant Data
    @GET("/merchant/get-info/{firebaseUid}")
    Call<MerchantModel> getMerchant(@Path("firebaseUid") String firebaseUid);



    // Check Merchant subscription status
    @Headers("Content-Type: application/json")
    @POST("/stripe-payments/subscription-status")
    Call<CheckSubscriptionResponse>checkSubscription(@Body CheckSubscriptionParams params);

    // Update merchant subscription status
    @Headers("Content-Type: application/json")
    @POST("/merchant/database/update-subscription/")
    Call<String>updateSubscriptionStatus(@Body UpdateSubscriptionStatus params);


}

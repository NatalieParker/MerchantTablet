package com.jayurewards.tablet.networking;

import com.jayurewards.tablet.models.GivePointsRequest;
import com.jayurewards.tablet.models.GivePointsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RestPoints {

    // Give customers points manually
    @Headers("Content-Type: application/json")
    @POST("/point/merchant/add-v3")
    Call<GivePointsResponse> merchantGivePoints(@Body GivePointsRequest params);


}

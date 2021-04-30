package com.jayurewards.tablet.networking;

import com.jayurewards.tablet.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static final String TAG = "RetrofitClient";
    private static final String BASE_URL = BuildConfig.restApiUrl;
//    private static final String BASE_URL = "http://10.0.2.2:8080";

    private static RetrofitClient mInstance;
    private final Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //** Adding Scalars Converter to accept string responses
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

//     Construct Retrofit Rest interfaces
    public RestAuth getRestAuth() {
        return retrofit.create(RestAuth.class);
    }
    public RestPoints getRestPoints(){return retrofit.create(RestPoints.class);}
    public RestTeam getRestTeam(){return retrofit.create(RestTeam.class);}
    public RestShops getRestShops(){return retrofit.create(RestShops.class);}
    public RestOffers getRestOffers(){return retrofit.create(RestOffers.class);}

}

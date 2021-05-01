package com.jayurewards.tablet.networking;

import com.jayurewards.tablet.models.TeamMembers.CheckSMSVerificationModel;
import com.jayurewards.tablet.models.TeamMembers.TeamMemberRequest;
import com.jayurewards.tablet.models.TeamMembers.RegisterUserModel;
import com.jayurewards.tablet.models.TeamMembers.TeamMemberModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestTeam {

    // Request SMS verification
    @GET("team-member/request-sms-verification/{phone}")
    Call<String>requestSMSVerification(@Path("phone") String phone);

    // Check SMS verification
    @Headers("Content-Type: application/json")
    @POST("/team-member/check-sms-verification")
    Call<String>checkSMSVerification(@Body CheckSMSVerificationModel params);

    // Get team member user
    @Headers("Content-Type: application/json")
    @POST("/team-member/get-team-member")
    Call<TeamMemberModel>getTeamMember(@Body TeamMemberRequest params);

    // Register new user
    @Headers("Content-Type: application/json")
    @POST("/user/register")
    Call<String>registerUser(@Body RegisterUserModel params);



}

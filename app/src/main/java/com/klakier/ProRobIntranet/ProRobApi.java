package com.klakier.ProRobIntranet;

import com.klakier.ProRobIntranet.Responses.TimesheetResponse;
import com.klakier.ProRobIntranet.Responses.TokenResponse;
import com.klakier.ProRobIntranet.Responses.UserDataShortResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProRobApi {

    @FormUrlEncoded
    @POST("login")
    Call<TokenResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("api/user/id/{user_id}")
    Call<UserDataShortResponse> getUserShort(
            @Path("user_id") int user,
            @Header("Authorization") String authorization
    );

    @GET("api/timesheet/id/{user_id}")
    Call<TimesheetResponse> getTimesheet(
            @Path("user_id") int user,
            @Header("Authorization") String authorization
    );
}

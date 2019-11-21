package com.klakier.proRobIntranet.api;

import com.klakier.proRobIntranet.api.response.StandardResponse;
import com.klakier.proRobIntranet.api.response.TimesheetResponse;
import com.klakier.proRobIntranet.api.response.TimesheetRowInsertedResponse;
import com.klakier.proRobIntranet.api.response.TokenResponse;
import com.klakier.proRobIntranet.api.response.UserDataShortResponse;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import retrofit2.Call;
import retrofit2.http.DELETE;
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

    @GET("api/timesheet/user_id/{user_id}")
    Call<TimesheetResponse> getTimesheet(
            @Path("user_id") int user,
            @Header("Authorization") String authorization
    );

    @FormUrlEncoded
    @POST("api/timesheet")
    Call<TimesheetRowInsertedResponse> insertTimesheetRow(
            @Field("user_id") int user_id,
            @Field("date") Date date,
            @Field("from") Time from,
            @Field("to") Time to,
            @Field("customer_break") Time customer_break,
            @Field("statutory_break") Time statutory_break,
            @Field("comments") String comments,
            @Field("project_id") int project_id,
            @Field("company_id") int company_id,
            @Field("status") boolean status,
            @Field("created_at") Timestamp created_at,
            @Field("updated_at") Timestamp updated_at,
            @Header("Authorization") String authorization
    );

    @DELETE("api/timesheet/id/{id}")
    Call<StandardResponse> deleteTimesheetRow(
            @Path("id") int id,
            @Header("Authorization") String authorization
    );
}

package com.klakier.ProRobIntranet;

import com.klakier.ProRobIntranet.Response.TokenResponse;

import okhttp3.ResponseBody;
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
    Call<ResponseBody> getUserShort(
            @Path("user_id") int user,
            @Header("Authorization") String authorization
    );
}

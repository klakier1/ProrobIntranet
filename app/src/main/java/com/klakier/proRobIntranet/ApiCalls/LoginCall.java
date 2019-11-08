package com.klakier.proRobIntranet.ApiCalls;

import android.content.Context;

import com.google.gson.Gson;
import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.Responses.StandardResponse;
import com.klakier.proRobIntranet.Responses.TokenResponse;
import com.klakier.proRobIntranet.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginCall implements ApiCall {

    Context context;
    String login;
    String password;

    public LoginCall(Context context, String login, String password) {
        this.context = context;
        this.login = login;
        this.password = password;
    }

    public void enqueue(final OnResponseListener onResponseListener) {

        Call<TokenResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .login(login, password);

        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                try {
                    switch (response.code()) {
                        case 200: {
                            onResponseListener.onSuccess(response.body());

                            break;
                        }
                        default: {
                            StandardResponse errorResponse = new Gson().fromJson(response.errorBody().string(), StandardResponse.class);
                            onResponseListener.onFailure(errorResponse);
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                onResponseListener.onFailure(new StandardResponse(true, context.getString(R.string.error_retrofit_msg)));
            }
        });
    }
}


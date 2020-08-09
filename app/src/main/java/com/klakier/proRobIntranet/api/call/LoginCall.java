package com.klakier.proRobIntranet.api.call;

import android.content.Context;

import com.google.gson.Gson;
import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.RetrofitClient;
import com.klakier.proRobIntranet.api.response.StandardResponse;
import com.klakier.proRobIntranet.api.response.TokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginCall implements ApiCall {

    Context mContext;
    String mLogin;
    String mPassword;
    Call<TokenResponse> mCall;

    public LoginCall(Context context, String login, String password) {
        this.mContext = context;
        this.mLogin = login;
        this.mPassword = password;

        mCall = RetrofitClient
                .getInstance()
                .getApi()
                .login(login, password);
    }

    @Override
    public void enqueue(final OnResponseListener onResponseListener) {
        mCall.enqueue(new Callback<TokenResponse>() {
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
                } catch (Exception e) {
                    StandardResponse errorResponse = new StandardResponse(true, e.getMessage());
                    onResponseListener.onFailure(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                onResponseListener.onFailure(new StandardResponse(true, mContext.getString(R.string.toast_error_retrofit_msg)));
            }
        });
    }

    @Override
    public StandardResponse execute() {
        try {
            Response<TokenResponse> response = mCall.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                return new Gson().fromJson(response.errorBody().string(), StandardResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new StandardResponse(true, e.getMessage());
        }
    }
}


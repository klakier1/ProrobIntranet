package com.klakier.proRobIntranet.api.call;

import android.content.Context;

import com.google.gson.Gson;
import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.RetrofitClient;
import com.klakier.proRobIntranet.Token;
import com.klakier.proRobIntranet.api.response.StandardResponse;
import com.klakier.proRobIntranet.api.response.UserDataShortResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUserShortDataCall implements ApiCall {

    Context mContext;
    Token mToken;
    Call<UserDataShortResponse> mCall;

    public GetUserShortDataCall(Context context, Token token) {
        this.mContext = context;
        this.mToken = token;

        mCall = RetrofitClient
                .getInstance()
                .getApi()
                .getUserShort(mToken.getId(), "Bearer " + mToken.getToken());
    }

    @Override
    public void enqueue(final OnResponseListener onResponseListener) {
        mCall.enqueue(new Callback<UserDataShortResponse>() {
            @Override
            public void onResponse(Call<UserDataShortResponse> call, Response<UserDataShortResponse> response) {
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
            public void onFailure(Call<UserDataShortResponse> call, Throwable t) {
                onResponseListener.onFailure(new StandardResponse(true, mContext.getString(R.string.toast_error_retrofit_msg)));
            }
        });
    }

    @Override
    public StandardResponse execute() {
        try {
            Response<UserDataShortResponse> response = mCall.execute();
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

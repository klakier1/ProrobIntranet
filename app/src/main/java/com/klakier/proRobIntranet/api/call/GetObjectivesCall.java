package com.klakier.proRobIntranet.api.call;

import android.content.Context;

import com.google.gson.Gson;
import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.RetrofitClient;
import com.klakier.proRobIntranet.Token;
import com.klakier.proRobIntranet.api.response.ObjectivesResponse;
import com.klakier.proRobIntranet.api.response.StandardResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetObjectivesCall implements ApiCall {

    Context mContext;
    Token mToken;
    Call<ObjectivesResponse> mCall;

    public GetObjectivesCall(Context context, Token token) {
        this.mContext = context;
        this.mToken = token;

        mCall = RetrofitClient
                .getInstance()
                .getApi()
                .getObjectives("Bearer " + mToken.getToken());
    }

    @Override
    public void enqueue(final OnResponseListener onResponseListener) {
        mCall.enqueue(new Callback<ObjectivesResponse>() {
            @Override
            public void onResponse(Call<ObjectivesResponse> call, Response<ObjectivesResponse> response) {
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
            public void onFailure(Call<ObjectivesResponse> call, Throwable t) {
                onResponseListener.onFailure(new StandardResponse(true, mContext.getString(R.string.error_retrofit_msg)));
            }
        });
    }

    @Override
    public StandardResponse execute() {
        try {
            Response<ObjectivesResponse> response = mCall.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                return new Gson().fromJson(response.errorBody().string(), StandardResponse.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}

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

    Context context;
    Token token;

    public GetObjectivesCall(Context context, Token token) {
        this.context = context;
        this.token = token;
    }

    public void enqueue(final OnResponseListener onResponseListener) {

        Call<ObjectivesResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getObjectives("Bearer " + token.getToken());

        call.enqueue(new Callback<ObjectivesResponse>() {
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
                onResponseListener.onFailure(new StandardResponse(true, context.getString(R.string.error_retrofit_msg)));
            }
        });
    }

    public StandardResponse execute() {

        Call<ObjectivesResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getObjectives("Bearer " + token.getToken());

        try {
            Response<ObjectivesResponse> response = call.execute();
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

package com.klakier.proRobIntranet.ApiCalls;

import android.content.Context;

import com.google.gson.Gson;
import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.Responses.StandardResponse;
import com.klakier.proRobIntranet.RetrofitClient;
import com.klakier.proRobIntranet.Token;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteTimesheetRowCall implements ApiCall {

    Context context;
    int extId;
    Token token;

    public DeleteTimesheetRowCall(Context context, Token token, int extId) {
        this.context = context;
        this.extId = extId;
        this.token = token;
    }

    public void enqueue(final OnResponseListener onResponseListener) {

        Call<StandardResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .deleteTimesheetRow(
                        extId,
                        "Bearer " + token.getToken());

        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                try {
                    switch (response.code()) {
                        case 200:
                        case 201: {
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
            public void onFailure(Call<StandardResponse> call, Throwable t) {
                onResponseListener.onFailure(new StandardResponse(true, context.getString(R.string.error_retrofit_msg)));
            }
        });
    }
}

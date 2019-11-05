package com.klakier.proRobIntranet.ApiCalls;

import android.content.Context;

import com.google.gson.Gson;
import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.Responses.StandardResponse;
import com.klakier.proRobIntranet.Responses.TimesheetResponse;
import com.klakier.proRobIntranet.RetrofitClient;
import com.klakier.proRobIntranet.Token;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetTimesheetCall implements ApiCall {

    Context context;
    Token token;

    public GetTimesheetCall(Context context, Token token) {
        this.context = context;
        this.token = token;
    }

    public void execute(final OnResponseListener onResponseListener) {

        int id = token.getId();

        Call<TimesheetResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getTimesheet(id, "Bearer " + token.getToken());

        call.enqueue(new Callback<TimesheetResponse>() {
            @Override
            public void onResponse(Call<TimesheetResponse> call, Response<TimesheetResponse> response) {
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
            public void onFailure(Call<TimesheetResponse> call, Throwable t) {
                onResponseListener.onFailure(new StandardResponse(true, context.getString(R.string.error_retrofit_msg)));
            }
        });
    }
}

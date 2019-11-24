package com.klakier.proRobIntranet.api.call;

import android.content.Context;

import com.google.gson.Gson;
import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.RetrofitClient;
import com.klakier.proRobIntranet.Token;
import com.klakier.proRobIntranet.api.response.StandardResponse;
import com.klakier.proRobIntranet.api.response.TimesheetRow;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTimesheetCall implements ApiCall {

    Context context;
    Token token;
    TimesheetRow mTsr;

    public UpdateTimesheetCall(Context context, Token token, TimesheetRow timesheetRow) {
        this.context = context;
        this.token = token;
        this.mTsr = timesheetRow;
    }

    public void enqueue(final OnResponseListener onResponseListener) {

        Call<StandardResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateTimesheet(
                        mTsr.getIdExternal(),
                        mTsr.getDate(),
                        mTsr.getFrom(),
                        mTsr.getTo(),
                        mTsr.getCustomerBreak(),
                        mTsr.getStatutoryBreak(),
                        mTsr.getComments(),
                        mTsr.getProjectId(),
                        mTsr.getCompanyId(),
                        mTsr.getUpdatedAt(),
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

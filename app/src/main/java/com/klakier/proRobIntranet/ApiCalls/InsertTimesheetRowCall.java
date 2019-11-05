package com.klakier.proRobIntranet.ApiCalls;

import android.content.Context;

import com.google.gson.Gson;
import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.Responses.StandardResponse;
import com.klakier.proRobIntranet.Responses.TimesheetRow;
import com.klakier.proRobIntranet.Responses.TimesheetRowInsertedResponse;
import com.klakier.proRobIntranet.RetrofitClient;
import com.klakier.proRobIntranet.Token;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertTimesheetRowCall implements ApiCall {

    Context context;
    TimesheetRow tsr;
    Token token;

    public InsertTimesheetRowCall(Context context, Token token, TimesheetRow timesheetRow) {
        this.context = context;
        this.tsr = timesheetRow;
        this.token = token;
    }

    public void execute(final OnResponseListener onResponseListener) {

        Call<TimesheetRowInsertedResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .insertTimesheetRow(
                        tsr.getUserId(),
                        tsr.getDate(),
                        tsr.getFrom(),
                        tsr.getTo(),
                        tsr.getCustomerBreak(),
                        tsr.getStatutoryBreak(),
                        tsr.getComments(),
                        tsr.getProjectId(),
                        tsr.getCompanyId(),
                        tsr.getStatus(),
                        tsr.getCreatedAt(),
                        tsr.getUpdatedAt(),
                        "Bearer " + token.getToken());

        call.enqueue(new Callback<TimesheetRowInsertedResponse>() {
            @Override
            public void onResponse(Call<TimesheetRowInsertedResponse> call, Response<TimesheetRowInsertedResponse> response) {
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
            public void onFailure(Call<TimesheetRowInsertedResponse> call, Throwable t) {
                onResponseListener.onFailure(new StandardResponse(true, context.getString(R.string.error_retrofit_msg)));
            }
        });
    }
}

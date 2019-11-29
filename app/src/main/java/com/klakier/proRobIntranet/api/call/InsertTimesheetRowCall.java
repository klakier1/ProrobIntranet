package com.klakier.proRobIntranet.api.call;

import android.content.Context;

import com.google.gson.Gson;
import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.RetrofitClient;
import com.klakier.proRobIntranet.Token;
import com.klakier.proRobIntranet.api.response.StandardResponse;
import com.klakier.proRobIntranet.api.response.TimesheetRow;
import com.klakier.proRobIntranet.api.response.TimesheetRowInsertedResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertTimesheetRowCall implements ApiCall {

    Context mContext;
    TimesheetRow mTsr;
    Token mToken;
    Call<TimesheetRowInsertedResponse> mCall;

    public InsertTimesheetRowCall(Context context, Token token, TimesheetRow timesheetRow) {
        this.mContext = context;
        this.mTsr = timesheetRow;
        this.mToken = token;

        mCall = RetrofitClient
                .getInstance()
                .getApi()
                .insertTimesheetRow(
                        mTsr.getUserId(),
                        mTsr.getDate(),
                        mTsr.getFrom(),
                        mTsr.getTo(),
                        mTsr.getCustomerBreak(),
                        mTsr.getStatutoryBreak(),
                        mTsr.getComments(),
                        mTsr.getProjectId(),
                        mTsr.getCompanyId(),
                        mTsr.getStatus(),
                        mTsr.getCreatedAt(),
                        mTsr.getUpdatedAt(),
                        mTsr.getProject(),
                        "Bearer " + mToken.getToken());
    }

    @Override
    public void enqueue(final OnResponseListener onResponseListener) {
        mCall.enqueue(new Callback<TimesheetRowInsertedResponse>() {
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
                onResponseListener.onFailure(new StandardResponse(true, mContext.getString(R.string.error_retrofit_msg)));
            }
        });
    }

    @Override
    public StandardResponse execute() {
        try {
            Response<TimesheetRowInsertedResponse> response = mCall.execute();
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

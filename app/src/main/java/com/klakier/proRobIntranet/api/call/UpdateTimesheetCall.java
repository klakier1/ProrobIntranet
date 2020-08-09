package com.klakier.proRobIntranet.api.call;

import android.content.Context;

import com.google.gson.Gson;
import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.RetrofitClient;
import com.klakier.proRobIntranet.Token;
import com.klakier.proRobIntranet.api.response.StandardResponse;
import com.klakier.proRobIntranet.api.response.TimesheetRow;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTimesheetCall implements ApiCall {

    Context mContext;
    Token mToken;
    TimesheetRow mTsr;
    Call<StandardResponse> mCall;

    public UpdateTimesheetCall(Context context, Token token, TimesheetRow timesheetRow) {
        this.mContext = context;
        this.mToken = token;
        this.mTsr = timesheetRow;

        mCall = RetrofitClient
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
                        mTsr.getProject(),
                        "Bearer " + mToken.getToken());
    }

    @Override
    public void enqueue(final OnResponseListener onResponseListener) {
        mCall.enqueue(new Callback<StandardResponse>() {
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
                } catch (Exception e) {
                    StandardResponse errorResponse = new StandardResponse(true, e.getMessage());
                    onResponseListener.onFailure(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {
                onResponseListener.onFailure(new StandardResponse(true, mContext.getString(R.string.toast_error_retrofit_msg)));
            }
        });
    }

    @Override
    public StandardResponse execute() {
        try {
            Response<StandardResponse> response = mCall.execute();
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

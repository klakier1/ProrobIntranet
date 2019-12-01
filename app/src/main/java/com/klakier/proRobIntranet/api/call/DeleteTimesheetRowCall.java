package com.klakier.proRobIntranet.api.call;

import android.content.Context;

import com.google.gson.Gson;
import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.RetrofitClient;
import com.klakier.proRobIntranet.Token;
import com.klakier.proRobIntranet.api.response.StandardResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteTimesheetRowCall implements ApiCall {

    private Context mContext;
    private int mExtId;
    private Token mToken;
    private Call<StandardResponse> mCall;

    public DeleteTimesheetRowCall(Context context, Token token, int extId) {
        this.mContext = context;
        this.mExtId = extId;
        this.mToken = token;

        mCall = RetrofitClient
                .getInstance()
                .getApi()
                .deleteTimesheetRow(
                        mExtId,
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
                onResponseListener.onFailure(new StandardResponse(true, mContext.getString(R.string.error_retrofit_msg)));
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

package com.klakier.proRobIntranet.ApiCalls;

import android.content.Context;

import com.google.gson.Gson;
import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.Responses.StandardResponse;
import com.klakier.proRobIntranet.Responses.UserDataShortResponse;
import com.klakier.proRobIntranet.RetrofitClient;
import com.klakier.proRobIntranet.Token;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUserShortDataCall implements ApiCall {

    Context context;
    Token token;

    public GetUserShortDataCall(Context context, Token token) {
        this.context = context;
        this.token = token;
    }

    public void execute(final OnResponseListener onResponseListener) {

        int id = token.getId();

        Call<UserDataShortResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getUserShort(id, "Bearer " + token.getToken());

        call.enqueue(new Callback<UserDataShortResponse>() {
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
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserDataShortResponse> call, Throwable t) {
                onResponseListener.onFailure(new StandardResponse(true, context.getString(R.string.error_retrofit_msg)));
            }
        });
    }
}

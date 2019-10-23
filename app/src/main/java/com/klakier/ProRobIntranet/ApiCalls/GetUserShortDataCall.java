package com.klakier.ProRobIntranet.ApiCalls;

import android.content.Context;

import com.google.gson.Gson;
import com.klakier.ProRobIntranet.R;
import com.klakier.ProRobIntranet.Responses.StandardResponse;
import com.klakier.ProRobIntranet.Responses.UserDataShortResponse;
import com.klakier.ProRobIntranet.RetrofitClient;
import com.klakier.ProRobIntranet.Token;

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
//                                String result = response.body().getMessage();
//                                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
//                                prepareInfo(response.body());
                            onResponseListener.onSuccess(response.body());
                            break;
                        }
                        default: {
                            StandardResponse errorResponse = new Gson().fromJson(response.errorBody().string(), StandardResponse.class);
//                                Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                                MainActivity activity = (MainActivity) getActivity();
//                                activity.logOut();
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
//                    Toast.makeText(getActivity(), getActivity().getString(R.string.error_retrofit_msg), Toast.LENGTH_LONG).show();
//                    MainActivity activity = (MainActivity) getActivity();
//                    activity.logOut();
                onResponseListener.onFailure(new StandardResponse(true, context.getString(R.string.error_retrofit_msg)));


            }
        });

    }

}

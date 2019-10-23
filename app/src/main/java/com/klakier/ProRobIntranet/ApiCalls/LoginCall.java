package com.klakier.ProRobIntranet.ApiCalls;

import android.content.Context;

import com.google.gson.Gson;
import com.klakier.ProRobIntranet.R;
import com.klakier.ProRobIntranet.Responses.StandardResponse;
import com.klakier.ProRobIntranet.Responses.TokenResponse;
import com.klakier.ProRobIntranet.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginCall implements ApiCall {

    Context context;
    String login;
    String password;

    public LoginCall(Context context, String login, String password) {
        this.context = context;
        this.login = login;
        this.password = password;
    }

    public void execute(final OnResponseListener onResponseListener) {

        Call<TokenResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .login(login, password);

        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                try {
                    switch (response.code()) {
                        case 200: {

//                            Toast.makeText(context, response.body().getToken(), Toast.LENGTH_SHORT).show();
//                                Token token = new Token(context);
//                                token.setToken(response.body().getToken());
//                                MainActivity activity = (MainActivity)getActivity();
//                                activity.signIn();
                            onResponseListener.onSuccess(response.body());

                            break;
                        }
                        default: {
                            StandardResponse errorResponse = new Gson().fromJson(response.errorBody().string(), StandardResponse.class);
//                            Toast.makeText(context, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            onResponseListener.onFailure(errorResponse);
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } finally {
//                    buttonLogIn.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
//                Toast.makeText(getActivity(), getActivity().getString(R.string.error_retrofit_msg), Toast.LENGTH_LONG).show();
//                buttonLogIn.setEnabled(true);
                onResponseListener.onFailure(new StandardResponse(true, context.getString(R.string.error_retrofit_msg)));
            }
        });


    }

}

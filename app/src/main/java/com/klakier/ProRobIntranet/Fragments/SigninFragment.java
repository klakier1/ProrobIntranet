package com.klakier.ProRobIntranet.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.klakier.ProRobIntranet.MainActivity;
import com.klakier.ProRobIntranet.R;
import com.klakier.ProRobIntranet.Response.StandardResponse;
import com.klakier.ProRobIntranet.Response.TokenResponse;
import com.klakier.ProRobIntranet.RetrofitClient;
import com.klakier.ProRobIntranet.Token;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninFragment extends Fragment {

    EditText editTextLoginUser;
    EditText editTextPasswordUser;
    Button buttonLogIn;
    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.signin_fragment, container, false);

        editTextLoginUser = v.findViewById(R.id.editTextLoginUser);
        editTextPasswordUser = v.findViewById(R.id.editTextPasswordUser);
        editTextPasswordUser.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_ENTER){
                    buttonLogIn.performClick();

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextPasswordUser.getWindowToken(), 0);

                    return true;
                }

                return false;
            }
        });
        buttonLogIn = v.findViewById(R.id.buttonLogIn);
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonLogIn.setEnabled(false);
                Call<TokenResponse> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .login(editTextLoginUser.getText().toString().trim(), editTextPasswordUser.getText().toString().trim());


                call.enqueue(new Callback<TokenResponse>() {
                    @Override
                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                        try {
                            switch (response.code()) {
                                case 200: {

                                    Toast.makeText(getActivity(), response.body().getToken(), Toast.LENGTH_SHORT).show();
                                    if(context != null) {
                                        Token token = new Token(context);
                                        token.setToken(response.body().getToken());
                                        MainActivity activity = (MainActivity)getActivity();
                                        activity.signIn();
                                    }

                                    break;
                                }
                                default: {
                                    StandardResponse errorResponse = new Gson().fromJson(response.errorBody().string(), StandardResponse.class);
                                    Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }finally {
                            buttonLogIn.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), getActivity().getString(R.string.error_retrofit_msg), Toast.LENGTH_LONG).show();
                        buttonLogIn.setEnabled(true);
                    }
                });

            }
        });

        return v;
    }

}

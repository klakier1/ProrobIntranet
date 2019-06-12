package com.klakier.ProRobIntranet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
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
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .login(editTextLoginUser.getText().toString(), editTextPasswordUser.getText().toString());


                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            switch (response.code()) {
                                case 200: {
                                    String result = response.body().string();
                                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                                    JSONObject reader = new JSONObject(result);
                                    String tokenString = reader.getString("token");
                                    if(context != null) {
                                        Token token = new Token(context);
                                        token.setToken(tokenString);
                                        MainActivity activity = (MainActivity)getActivity();
                                        activity.signIn();
                                    }

                                    break;
                                }
                                default: {
                                    Toast.makeText(getActivity(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e){
                            e.printStackTrace();
                        }finally {
                            buttonLogIn.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        buttonLogIn.setEnabled(true);
                    }
                });

            }
        });

        return v;
    }

}

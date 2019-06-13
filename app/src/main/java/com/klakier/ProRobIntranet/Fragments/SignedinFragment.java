package com.klakier.ProRobIntranet.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.klakier.ProRobIntranet.MainActivity;
import com.klakier.ProRobIntranet.R;
import com.klakier.ProRobIntranet.Response.StandardResponse;
import com.klakier.ProRobIntranet.Response.UserDataShort;
import com.klakier.ProRobIntranet.Response.UserDataShortResponse;
import com.klakier.ProRobIntranet.RetrofitClient;
import com.klakier.ProRobIntranet.Token;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignedinFragment extends Fragment {

    TextView textViewInfo1;
    TextView textViewInfo2;
    TextView textViewInfo3;
    TextView textViewInfo4;
    Button buttonLogout;
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

        View v = inflater.inflate(R.layout.signedin_fragment, container, false);

        textViewInfo1 = v.findViewById(R.id.textViewInfo1);
        textViewInfo2 = v.findViewById(R.id.textViewInfo2);
        textViewInfo3 = v.findViewById(R.id.textViewInfo3);
        textViewInfo4 = v.findViewById(R.id.textViewInfo4);
        buttonLogout = v.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.logOut();
            }
        });

        getInfo();

        return v;
    }

    private void prepareInfo(UserDataShortResponse response) {

        UserDataShort user = response.getData().get(0);
        textViewInfo1.setText("Zalogowany jako: " + user.getFirstName() + " " + user.getLastName());
        textViewInfo2.setText("Rola: " + user.getRole());
        textViewInfo3.setText("Email: " + user.getEmail());
        textViewInfo4.setText("Nr tel: " + user.getPhone());

/*            email": "testowy1@pro-rob.pl",
                    "avatar_file_name": null,
                    "avatar_content_type": null,
                    "avatar_file_size": null,
                    "role": "employee",
                    "active": true,
                    "first_name": "Tommy Lee",
                    "last_name": "Jones",
                    "title": "Ścigany",
                    "phone": "+48123456789"*/
    }

    private void getInfo() {
        if (context != null) {
            Token token = new Token(context);
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
                                String result = response.body().getMessage();
                                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                                prepareInfo(response.body());
                                break;
                            }
                            default: {
                                StandardResponse errorResponse = new Gson().fromJson(response.errorBody().string(), StandardResponse.class);
                                Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                MainActivity activity = (MainActivity) getActivity();
                                activity.logOut();
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
                    Toast.makeText(getActivity(), getActivity().getString(R.string.error_retrofit_msg), Toast.LENGTH_LONG).show();
                    MainActivity activity = (MainActivity) getActivity();
                    activity.logOut();

                }
            });
        }
    }
}

package com.klakier.ProRobIntranet;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
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
                MainActivity activity = (MainActivity)getActivity();
                activity.logOut();
            }
        });

        getInfo();

        return v;
    }

    private void prepareInfo(JSONObject jsonObject){
        try {
            JSONArray array =  jsonObject.getJSONArray("data");
            JSONObject item = array.getJSONObject(0);
            textViewInfo1.setText("Zalogowany jako: " + item.getString("first_name") + " " + item.getString("last_name"));
            textViewInfo2.setText("Rola: " + item.getString("role"));
            textViewInfo3.setText("Email: " + item.getString("email"));
            textViewInfo4.setText("Nr tel: " + item.getString("phone"));

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


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getInfo() {
        if (context != null) {
            Token token = new Token(context);
            int id = token.getId();

            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getUserShort(id, "Bearer " + token.getToken());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        switch (response.code()) {
                            case 200: {
                                String result = response.body().string();
                                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                                JSONObject reader = new JSONObject(result);
                                prepareInfo(reader);
                                break;
                            }
                            default: {
                                Toast.makeText(getActivity(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                MainActivity activity = (MainActivity)getActivity();
                                activity.logOut();
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    MainActivity activity = (MainActivity)getActivity();
                    activity.logOut();

                }
            });
        }
    }
}

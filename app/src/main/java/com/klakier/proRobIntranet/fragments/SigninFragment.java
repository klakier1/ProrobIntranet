package com.klakier.proRobIntranet.fragments;

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

import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.Token;
import com.klakier.proRobIntranet.api.call.GetUserShortDataCall;
import com.klakier.proRobIntranet.api.call.LoginCall;
import com.klakier.proRobIntranet.api.call.OnResponseListener;
import com.klakier.proRobIntranet.api.response.StandardResponse;
import com.klakier.proRobIntranet.api.response.TokenResponse;
import com.klakier.proRobIntranet.api.response.UserDataShort;
import com.klakier.proRobIntranet.api.response.UserDataShortResponse;
import com.klakier.proRobIntranet.database.DBProRob;

public class SigninFragment extends Fragment {

    public static final String SIGN_IN_ACTION = "signInAction";

    private EditText editTextLoginUser;
    private EditText editTextPasswordUser;
    private Button buttonLogIn;
    private Context context;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename method, update argument and hook method into UI event
    public void onAction(String action) {
        if (mListener != null) {
            mListener.onFragmentInteraction(action);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        this.context = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.signin_fragment, container, false);

        editTextLoginUser = v.findViewById(R.id.editTextLoginUser);
        editTextPasswordUser = v.findViewById(R.id.editTextPasswordUser);
        editTextPasswordUser.setOnKeyListener(new View.OnKeyListener() {
            //hide keyboard and click button on enter
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
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

                //disable login button
                buttonLogIn.setEnabled(false);
                //get login and password from editText
                String userLogin = editTextLoginUser.getText().toString().trim();
                String userPassword = editTextPasswordUser.getText().toString().trim();

                LoginCall loginCall = new LoginCall(context, userLogin, userPassword);
                loginCall.enqueue(new OnResponseListener() {
                    @Override
                    public void onSuccess(StandardResponse response) {
                        //cast response to tokenResponse
                        TokenResponse tokenResponse = (TokenResponse) response;
                        //get token to preferences
                        Token token = new Token(context);
                        token.setToken(tokenResponse.getToken());

                        //get userDara to DB
                        GetUserShortDataCall getUserShortDataCall = new GetUserShortDataCall(context, token);
                        getUserShortDataCall.enqueue(new OnResponseListener() {
                            @Override
                            public void onSuccess(StandardResponse response) {
                                //cast response to userDataShortResponse
                                UserDataShortResponse userDataShortResponse = (UserDataShortResponse) response;
                                //get first user, response should have one user anyway
                                UserDataShort userDataShort = userDataShortResponse.getData().get(0);
                                //get DB
                                DBProRob dbProRob = new DBProRob(context, null);
                                dbProRob.setUser(userDataShort);
                                //inform mainActivity to change fragment
                                onAction(SIGN_IN_ACTION);
                            }

                            @Override
                            public void onFailure(StandardResponse response) {
                                Toast.makeText(context, response.getMessage(), Toast.LENGTH_LONG).show();
                                //unset token
                                new Token(context).resetToken();
                                //enable login button
                                buttonLogIn.setEnabled(true);
                            }
                        });

                    }

                    @Override
                    public void onFailure(StandardResponse response) {
                        Toast.makeText(context, response.getMessage(), Toast.LENGTH_LONG).show();
                        //enable login button
                        buttonLogIn.setEnabled(true);
                    }
                });
            }
        });

        return v;
    }


}

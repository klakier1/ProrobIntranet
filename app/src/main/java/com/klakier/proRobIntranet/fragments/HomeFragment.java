package com.klakier.proRobIntranet.fragments;

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

import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.api.response.UserDataShort;
import com.klakier.proRobIntranet.database.DBProRob;

public class HomeFragment extends Fragment {

    public static final String LOGOUT_ACTION = "logoutAction";

    private TextView textViewInfo1;
    private TextView textViewInfo2;
    private TextView textViewInfo3;
    private TextView textViewInfo4;
    private Button buttonLogout;
    private Context context;

    public HomeFragment() {
        setHasOptionsMenu(false);
    }

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

        View v = inflater.inflate(R.layout.signedin_fragment, container, false);

        textViewInfo1 = v.findViewById(R.id.textViewInfo1);
        textViewInfo2 = v.findViewById(R.id.textViewInfo2);
        textViewInfo3 = v.findViewById(R.id.textViewInfo3);
        textViewInfo4 = v.findViewById(R.id.textViewInfo4);
        buttonLogout = v.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAction(LOGOUT_ACTION);
            }
        });

        fillTextViews();

        return v;
    }

    private void fillTextViews() {
        DBProRob dbProRob = new DBProRob(context, null);
        UserDataShort user = dbProRob.getUser();
        if (user != null) {
            textViewInfo1.setText(String.format("Zalogowany jako: %s %s", user.getFirstName(), user.getLastName()));
            textViewInfo2.setText(String.format("Rola: %s", user.getRole()));
            textViewInfo3.setText(String.format("Email: %s", user.getEmail()));
            textViewInfo4.setText(String.format("Nr tel: %s", user.getPhone()));
        } else {
            onAction(LOGOUT_ACTION);
        }
    }
}

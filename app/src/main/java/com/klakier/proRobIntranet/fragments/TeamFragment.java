package com.klakier.proRobIntranet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.klakier.proRobIntranet.R;
import com.klakier.proRobIntranet.TeamViewAdapter;
import com.klakier.proRobIntranet.Token;
import com.klakier.proRobIntranet.api.call.GetAllUsersShortDataCall;
import com.klakier.proRobIntranet.api.call.OnResponseListener;
import com.klakier.proRobIntranet.api.response.StandardResponse;
import com.klakier.proRobIntranet.api.response.UserDataShort;
import com.klakier.proRobIntranet.database.DBProRob;

import java.util.ArrayList;
import java.util.List;

public class TeamFragment extends Fragment {

    private Context mContext;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private TeamViewAdapter mTeamViewAdapter;
    private List<UserDataShort> mTeamList = new ArrayList<>();

    public TeamFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_get_team:{
                new GetAllUsersShortDataCall(mContext, new Token(mContext)).enqueue(new OnResponseListener() {
                    @Override
                    public void onSuccess(StandardResponse response) {
                        refreshList();
                    }

                    @Override
                    public void onFailure(StandardResponse response) {
                        Toast.makeText(mContext, response.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                return true;
            }
            default:{
                return false;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_working_team_fragment, menu);
    }

    public void refreshList() {
        if (mTeamViewAdapter != null) {
            mTeamList.clear();
            mTeamList.addAll(new DBProRob(mContext, null).getTeam());
            mTeamViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        mTeamList.addAll(new DBProRob(mContext, null).getTeam());
        mTeamViewAdapter = new TeamViewAdapter(mTeamList);
        mRecyclerView = view.findViewById(R.id.recyclerViewTeam);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mTeamViewAdapter);
        return view;
    }

    public void onAction(String action) {
        if (mListener != null) {
            mListener.onFragmentInteraction(action);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
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
        this.mContext = null;
    }
}

package com.klakier.ProRobIntranet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SubscribersFragment extends Fragment {

    RecyclerView recyclerViewSubscribers;
    SubscribersRecyclerViewAdapter subscribersRecyclerViewAdapter;

    public RecyclerView getRecyclerViewSubscribers() {
        return recyclerViewSubscribers;
    }

    public void setRecyclerViewSubscribers(RecyclerView recyclerViewSubscribers) {
        this.recyclerViewSubscribers = recyclerViewSubscribers;
    }

    public SubscribersRecyclerViewAdapter getSubscribersRecyclerViewAdapter() {
        return subscribersRecyclerViewAdapter;
    }

    public void setSubscribersRecyclerViewAdapter(SubscribersRecyclerViewAdapter subscribersRecyclerViewAdapter) {
        this.subscribersRecyclerViewAdapter = subscribersRecyclerViewAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.subscribers_fragment, container);

        recyclerViewSubscribers = v.findViewById(R.id.recyclerViewSubscribers);

        return v;
    }

}

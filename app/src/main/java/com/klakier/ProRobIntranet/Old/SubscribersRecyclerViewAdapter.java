package com.klakier.ProRobIntranet.Old;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.klakier.ProRobIntranet.R;

import java.util.List;

public class SubscribersRecyclerViewAdapter extends RecyclerView.Adapter<SubscribersRecyclerViewAdapter.SubscriberHolder> {

    List<DbNewsletter> subscribers;

    public SubscribersRecyclerViewAdapter(List<DbNewsletter> subscribers) {
        this.subscribers = subscribers;
    }

    @NonNull
    @Override
    public SubscriberHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.subscriber_item, viewGroup, false);
        SubscriberHolder viewHolder = new SubscriberHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriberHolder subscriberHolder, int i) {
        DbNewsletter subscriber = subscribers.get(i);
        subscriberHolder.textViewSubscriberId.setText(Integer.toString(subscriber.getId()));
        subscriberHolder.textViewSubscriberEmail.setText(subscriber.getEmail());
    }

    @Override
    public int getItemCount() {
        return subscribers.size();
    }

    class SubscriberHolder extends RecyclerView.ViewHolder {

        public TextView textViewSubscriberId;
        public TextView textViewSubscriberEmail;

        public SubscriberHolder(@NonNull View itemView) {
            super(itemView);
            textViewSubscriberId = itemView.findViewById(R.id.textViewSubscriberId);
            textViewSubscriberEmail = itemView.findViewById(R.id.textViewSubscriberEmail);
        }
    }
}

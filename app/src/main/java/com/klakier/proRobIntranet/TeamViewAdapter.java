package com.klakier.proRobIntranet;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.klakier.proRobIntranet.api.response.UserDataShort;

import java.util.List;

public class TeamViewAdapter extends RecyclerView.Adapter<TeamViewAdapter.UserItemViewHolder> {

    private List<UserDataShort> mUserList;

    public TeamViewAdapter(List<UserDataShort> userList) {
        super();
        mUserList = userList;
    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.user_item, viewGroup, false);
        return new UserItemViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemViewHolder userItemViewHolder, int i) {
        UserDataShort userDataShort = mUserList.get(i);
        userItemViewHolder.textViewUserItemName.setText(String.format("%s %s", userDataShort.getFirstName(), userDataShort.getLastName()));
        userItemViewHolder.textViewUserItemEmail.setText(userDataShort.getEmail());
        userItemViewHolder.textViewUserItemPhone.setText(userDataShort.getPhone());
        userItemViewHolder.textViewUserItemTitle.setText(userDataShort.getTitle());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    class UserItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewUserItem;
        TextView textViewUserItemName;
        TextView textViewUserItemTitle;
        TextView textViewUserItemPhone;
        TextView textViewUserItemEmail;

        UserItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewUserItem = itemView.findViewById(R.id.imageViewUserItem);
            textViewUserItemName = itemView.findViewById(R.id.textViewUserItemName);
            textViewUserItemTitle = itemView.findViewById(R.id.textViewUserItemTitle);
            textViewUserItemPhone = itemView.findViewById(R.id.textViewUserItemPhone);
            textViewUserItemEmail = itemView.findViewById(R.id.textViewUserItemEmail);
        }
    }
}

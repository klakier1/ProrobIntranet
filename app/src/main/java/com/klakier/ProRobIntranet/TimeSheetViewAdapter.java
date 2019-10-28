package com.klakier.ProRobIntranet;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.klakier.ProRobIntranet.Responses.TimesheetRow;

import java.util.List;

public class TimeSheetViewAdapter extends RecyclerView.Adapter<TimeSheetViewAdapter.TimeSheetRowViewHolder> {

    List<TimesheetRow> mList;

    public TimeSheetViewAdapter(List<TimesheetRow> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public TimeSheetRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.timesheet_item, viewGroup, false);
        TimeSheetRowViewHolder viewHolder = new TimeSheetRowViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSheetRowViewHolder timeSheetRowView, int i) {
        TimesheetRow tsr = mList.get(i);
        timeSheetRowView.textViewDate.setText(tsr.getDate().toString());
        timeSheetRowView.textViewFrom.setText(tsr.getFrom().toString());
        timeSheetRowView.textViewTo.setText(tsr.getTo().toString());
        timeSheetRowView.textViewCustomerBreak.setText(tsr.getCustomerBreak().toString());
        timeSheetRowView.textViewStatutoryBreak.setText(tsr.getStatutoryBreak().toString());
        timeSheetRowView.textViewCompany.setText(tsr.getCompanyId().toString());
        timeSheetRowView.textViewProject.setText(tsr.getProjectId().toString());
        timeSheetRowView.textViewComment.setText(tsr.getComments());
        timeSheetRowView.textViewUserID.setText(tsr.getUserId().toString());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class TimeSheetRowViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewDate;
        public TextView textViewFrom;
        public TextView textViewTo;
        public TextView textViewCompany;
        public TextView textViewProject;
        public TextView textViewStatutoryBreak;
        public TextView textViewCustomerBreak;
        public TextView textViewComment;
        public TextView textViewUserID;

        public TimeSheetRowViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewFrom = itemView.findViewById(R.id.textViewFrom);
            textViewTo = itemView.findViewById(R.id.textViewTo);
            textViewCompany = itemView.findViewById(R.id.textViewCompany);
            textViewProject = itemView.findViewById(R.id.textViewProject);
            textViewStatutoryBreak = itemView.findViewById(R.id.textViewStatutoryBreak);
            textViewCustomerBreak = itemView.findViewById(R.id.textViewCustomerBreak);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            textViewUserID = itemView.findViewById(R.id.textViewUserId);


        }
    }
}

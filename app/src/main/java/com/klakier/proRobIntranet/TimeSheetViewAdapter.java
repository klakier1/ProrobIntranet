package com.klakier.proRobIntranet;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.klakier.proRobIntranet.api.response.TimesheetRow;

import java.util.List;

public class TimeSheetViewAdapter extends RecyclerView.Adapter<TimeSheetViewAdapter.TimeSheetRowViewHolder> {

    private List<TimesheetRow> mList;
    private OnTimeSheetItemListener mOnTimeSheetItemListener;

    public TimeSheetViewAdapter(List<TimesheetRow> mList, OnTimeSheetItemListener onTimeSheetItemListener) {
        this.mList = mList;
        mOnTimeSheetItemListener = onTimeSheetItemListener;
    }

    @NonNull
    @Override
    public TimeSheetRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.timesheet_item, viewGroup, false);
        return new TimeSheetRowViewHolder(listItem, mOnTimeSheetItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSheetRowViewHolder timeSheetRowView, int i) {
        TimesheetRow tsr = mList.get(i);
        timeSheetRowView.textViewDate.setText(tsr.getDate().toString());
        timeSheetRowView.textViewFrom.setText(tsr.getFrom().toString());
        timeSheetRowView.textViewTo.setText(tsr.getTo().toString());
        timeSheetRowView.textViewCustomerBreak.setText(tsr.getCustomerBreak().toString());
        timeSheetRowView.textViewStatutoryBreak.setText(tsr.getStatutoryBreak().toString());
        timeSheetRowView.textViewComment.setText(tsr.getComments());
        timeSheetRowView.textViewUserID.setText(String.format("Id:%s  IdExt:%s  IdUser:%s", tsr.getIdLocal().toString(), tsr.getIdExternal().toString(), tsr.getUserId().toString()));
        timeSheetRowView.textViewCreatedAt.setText(tsr.getCreatedAt().toString());
        timeSheetRowView.textViewUpdatedAt.setText(tsr.getUpdatedAt().toString());
        timeSheetRowView.textViewProjectName.setText(tsr.getProject());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnTimeSheetItemListener {
        void onTimeSheetItemClick(int position, View v);
    }

    public class TimeSheetRowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardViewTimesheetItem;
        TextView textViewDate;
        TextView textViewFrom;
        TextView textViewTo;
        TextView textViewProjectName;
        TextView textViewStatutoryBreak;
        TextView textViewCustomerBreak;
        TextView textViewComment;
        TextView textViewUserID;
        TextView textViewCreatedAt;
        TextView textViewUpdatedAt;
        OnTimeSheetItemListener onTimeSheetItemListener;

        TimeSheetRowViewHolder(@NonNull View itemView, OnTimeSheetItemListener onTimeSheetItemListener) {
            super(itemView);
            cardViewTimesheetItem = itemView.findViewById(R.id.cardViewTimesheetItem);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewFrom = itemView.findViewById(R.id.textViewFrom);
            textViewTo = itemView.findViewById(R.id.textViewTo);
            textViewProjectName = itemView.findViewById(R.id.textViewProjectName);
            textViewStatutoryBreak = itemView.findViewById(R.id.textViewStatutoryBreak);
            textViewCustomerBreak = itemView.findViewById(R.id.textViewCustomerBreak);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            textViewUserID = itemView.findViewById(R.id.textViewUserId);
            textViewCreatedAt = itemView.findViewById(R.id.textViewCreatedAt);
            textViewUpdatedAt = itemView.findViewById(R.id.textViewUpdatedAt);

            this.onTimeSheetItemListener = onTimeSheetItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTimeSheetItemListener.onTimeSheetItemClick(getAdapterPosition(), v);
        }
    }

}

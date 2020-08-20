package com.klakier.proRobIntranet;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.klakier.proRobIntranet.api.response.TimesheetRow;
import com.klakier.proRobIntranet.database.DBProRob;

import java.lang.ref.WeakReference;
import java.util.List;

public class TimeSheetViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int TYPE_TIMESHEET = 66;
    private final static int TYPE_FILTER = 67;
    private boolean mFilterEnable = true;
    private List<TimesheetRow> mList;
    private OnTimeSheetItemListener mOnTimeSheetItemListener;
    private OnFilterDateChangedListener mOnFilterDateChangedListener;
    private WeakReference<Context> mContextRef;
    private FilterDate mFilterDate;

    public TimeSheetViewAdapter(Context context, List<TimesheetRow> list, OnTimeSheetItemListener onTimeSheetItemListener) {
        mContextRef = new WeakReference<>(context);
        mList = list;
        mOnTimeSheetItemListener = onTimeSheetItemListener;
    }

    public void setFilterDate(FilterDate fd, OnFilterDateChangedListener listener) {
        mFilterEnable = true;
        mFilterDate = fd;
        mOnFilterDateChangedListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType) {
            default:
            case TYPE_TIMESHEET: {
                View listItem = layoutInflater.inflate(R.layout.timesheet_item, viewGroup, false);
                return new TimeSheetRowViewHolder(listItem, mOnTimeSheetItemListener);
            }
            case TYPE_FILTER: {
                View listItem = layoutInflater.inflate(R.layout.timesheet_filter_item, viewGroup, false);
                return new FilterItemViewHolder(listItem);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        switch (getItemViewType(pos)) {
            case TYPE_TIMESHEET: {
                TimesheetRow tsr = mFilterEnable ? mList.get(pos - 1) : mList.get(pos);

                TimeSheetRowViewHolder timeSheetRowView = (TimeSheetRowViewHolder) holder;
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

                break;
            }
            case TYPE_FILTER: {
                FilterItemViewHolder filterItemViewHolder = (FilterItemViewHolder) holder;

                filterItemViewHolder.spinnerAdapterProject.clear();
                filterItemViewHolder.spinnerAdapterProject.add("Wszystkie");
                filterItemViewHolder.spinnerAdapterProject.addAll(new DBProRob(mContextRef.get(), null).readObjectives());
                filterItemViewHolder.spinnerAdapterProject.notifyDataSetChanged();

                filterItemViewHolder.spinnerAdapterMonth.clear();
                filterItemViewHolder.spinnerAdapterMonth.addAll(mContextRef.get().getResources().getStringArray(R.array.spinner_filter_month));
                filterItemViewHolder.spinnerAdapterMonth.notifyDataSetChanged();

                filterItemViewHolder.spinnerAdapterYear.clear();
                filterItemViewHolder.spinnerAdapterYear.addAll(Util.generateStringIntRange(2019, 2100));
                filterItemViewHolder.spinnerAdapterYear.notifyDataSetChanged();

                //FilterDate fd = new FilterDate();
                //fd.setRangeMonth(5, 2037);
                int month = mFilterDate.getMonthOfRangeMonth();
                int year = mFilterDate.getYearOfRangeMonth();

                int selectedYearPosition = filterItemViewHolder.spinnerAdapterYear.getPosition(String.valueOf(year));

                filterItemViewHolder.spinnerMonth.setSelection(month);
                filterItemViewHolder.spinnerYear.setSelection(selectedYearPosition);
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mFilterEnable && position == 0)
            return TYPE_FILTER;
        else
            return TYPE_TIMESHEET;
    }

    @Override
    public int getItemCount() {
        return mFilterEnable ? mList.size() + 1 : mList.size();
    }

    public interface OnTimeSheetItemListener {
        void onTimeSheetItemClick(int adapterPosition, int contentPosition, View v);
    }

    public interface OnFilterDateChangedListener {
        void onFilterDateChange();
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
            int adapterPosition = getAdapterPosition();

            int contentPosition = mFilterEnable ? adapterPosition - 1 : adapterPosition;

            onTimeSheetItemListener.onTimeSheetItemClick(adapterPosition, contentPosition, v);
        }
    }

    public class FilterItemViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener, View.OnClickListener {

        Spinner spinnerMonth;
        Spinner spinnerYear;
        Spinner spinnerProject;
        ArrayAdapter<String> spinnerAdapterMonth;
        ArrayAdapter<String> spinnerAdapterYear;
        ArrayAdapter<String> spinnerAdapterProject;
        Button buttonFilterStart;

        public FilterItemViewHolder(@NonNull View itemView) {
            super(itemView);
            spinnerMonth = itemView.findViewById(R.id.spinner_filter_month);
            spinnerYear = itemView.findViewById(R.id.spinner_filter_year);
            spinnerProject = itemView.findViewById(R.id.spinner_filter_project_name);
            buttonFilterStart = itemView.findViewById(R.id.button_filter_timesheet);

            spinnerAdapterMonth = new ArrayAdapterWithTextAlignment<String>(mContextRef.get(), android.R.layout.simple_spinner_item);
            spinnerAdapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAdapterYear = new ArrayAdapterWithTextAlignment<String>(mContextRef.get(), android.R.layout.simple_spinner_item);
            spinnerAdapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAdapterProject = new ArrayAdapterWithTextAlignment<String>(mContextRef.get(), android.R.layout.simple_spinner_item);
            spinnerAdapterProject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerProject.setAdapter(spinnerAdapterProject);
            spinnerYear.setAdapter(spinnerAdapterYear);
            spinnerMonth.setAdapter(spinnerAdapterMonth);

            spinnerYear.setOnItemSelectedListener(this);
            spinnerMonth.setOnItemSelectedListener(this);
            spinnerProject.setOnItemSelectedListener(this);

            buttonFilterStart.setOnClickListener(this);
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
            switch (adapterView.getId()) {
                case R.id.spinner_filter_month: {
                    int month = pos;
                    mFilterDate.setRangeMonth(month, -1);
                    break;
                }
                case R.id.spinner_filter_year: {
                    String sYear = spinnerAdapterYear.getItem(pos);
                    int year = Integer.parseInt(sYear);
                    mFilterDate.setRangeMonth(-1, year);
                    break;

                }
                default: {
                    break;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

        @Override
        public void onClick(View view) {
            mOnFilterDateChangedListener.onFilterDateChange();
        }
    }

    class ArrayAdapterWithTextAlignment<T> extends ArrayAdapter<T> {
        public ArrayAdapterWithTextAlignment(@NonNull Context context, int resource) {
            super(context, resource);
        }

        public ArrayAdapterWithTextAlignment(@NonNull Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        public ArrayAdapterWithTextAlignment(@NonNull Context context, int resource, @NonNull T[] objects) {
            super(context, resource, objects);
        }

        public ArrayAdapterWithTextAlignment(@NonNull Context context, int resource, int textViewResourceId, @NonNull T[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public ArrayAdapterWithTextAlignment(@NonNull Context context, int resource, @NonNull List<T> objects) {
            super(context, resource, objects);
        }

        public ArrayAdapterWithTextAlignment(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<T> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            }
            //textView.setTextColor(Color.GREEN);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            return textView;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView textView = (TextView) super.getView(position, convertView, parent);
            textView.setGravity(Gravity.END);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.END;
            textView.setLayoutParams(params);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                textView.setGravity(Gravity.END);

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setForegroundGravity(Gravity.END);
            }
            //textView.setTextColor(Color.BLUE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            ((Spinner) parent).setGravity(Gravity.END);

            return textView;
        }
    }
}

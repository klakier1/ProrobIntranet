package com.klakier.proRobIntranet;

import android.support.annotation.Nullable;

import java.sql.Date;
import java.util.Calendar;

public class FilterDate {

    static public final int RANGE_MONTH = 748;
    static public final int RANGE_DATE_TO_DATE = 749;
    static public final int RANGE_WEEK = 750;
    private int mSelected;
    private Date mRangeMonth;
    private Date mRangeDateFrom;
    private Date mRangeDateTo;
    private Date mRangeWeek;

    public FilterDate() {
        this(Calendar.getInstance().getTime());
    }

    public FilterDate(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        mRangeMonth = new Date(calendar.getTimeInMillis());
        mRangeWeek = new Date(calendar.getTimeInMillis());
        mRangeDateFrom = getFirstDayOfMonth(calendar.getTime());
        mRangeDateTo = getLastDayOfMonth(calendar.getTime());

        mSelected = RANGE_MONTH;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj instanceof FilterDate) {
            if (mSelected != ((FilterDate) obj).mSelected) return false;
            if (!mRangeDateTo.equals(((FilterDate) obj).mRangeDateTo)) return false;
            if (!mRangeDateFrom.equals(((FilterDate) obj).mRangeDateFrom)) return false;
            if (!mRangeMonth.equals(((FilterDate) obj).mRangeMonth)) return false;
            return mRangeWeek.equals(((FilterDate) obj).mRangeWeek);
        } else {
            return false;
        }
    }

    public void setRangeDateToDate(Date from, Date to) {
        mRangeDateFrom = from;
        mRangeDateTo = to;
    }

    public void setRangeMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mRangeMonth);
        calendar.set(Calendar.DATE, 1);
        if (year >= calendar.getActualMinimum(Calendar.YEAR) && year <= calendar.getActualMaximum(Calendar.YEAR))
            calendar.set(Calendar.YEAR, year);
        if (month >= calendar.getActualMinimum(Calendar.MONTH) && month <= calendar.getActualMaximum(Calendar.MONTH))
            calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        mRangeMonth.setTime(calendar.getTimeInMillis());
    }

    public void setRangeWeek(int week, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        mRangeWeek.setTime(calendar.getTimeInMillis());
    }

    public int getRangeTypeSelected() {
        return mSelected;
    }

    public void setRangeTypeSelected(int mSelected) {
        this.mSelected = mSelected;
    }

    private Date getFirstDayOfMonth(java.util.Date baseDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(baseDate);
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));

        return new Date(cal.getTimeInMillis());
    }

    private Date getLastDayOfMonth(java.util.Date baseDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(baseDate);
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));

        return new Date(cal.getTimeInMillis());
    }

    private Date getFirstDayOfWeek(java.util.Date baseDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(baseDate);
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return new Date(cal.getTimeInMillis());
    }

    private Date getLastDayOfWeek(java.util.Date baseDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(baseDate);
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return new Date(cal.getTimeInMillis());
    }

    public Date getRangeStartDate() {
        switch (mSelected) {
            case RANGE_DATE_TO_DATE: {
                return mRangeDateFrom;
            }
            case RANGE_WEEK: {
                return getFirstDayOfWeek(mRangeWeek);
            }
            case RANGE_MONTH:
            default: {
                return getFirstDayOfMonth(mRangeMonth);
            }
        }
    }

    public Date getRangeEndDate() {
        switch (mSelected) {
            case RANGE_DATE_TO_DATE: {
                return mRangeDateTo;
            }
            case RANGE_WEEK: {
                return getLastDayOfWeek(mRangeWeek);
            }
            case RANGE_MONTH:
            default: {
                return getLastDayOfMonth(mRangeMonth);
            }
        }
    }

    public int getMonthOfRangeMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(mRangeMonth);
        return cal.get(Calendar.MONTH);
    }

    public int getYearOfRangeMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(mRangeMonth);
        return cal.get(Calendar.YEAR);
    }

    public int getWeekOfRangeWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(mRangeWeek);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public int getYearOfRangeWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(mRangeWeek);
        return cal.get(Calendar.YEAR);
    }
}

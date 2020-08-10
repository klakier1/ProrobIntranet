package com.klakier.proRobIntranet;

import java.sql.Date;
import java.util.Calendar;

public class FilterData {

    static public final int RANGE_MONTH = 748;
    static public final int RANGE_DATE_TO_DATE = 749;
    static public final int RANGE_WEEK = 750;
    private int mSelected;
    private Date mRangeMonth;
    private Date mRangeDateFrom;
    private Date mRangeDateTo;
    private Date mRangeWeek;

    public FilterData() {
        this(Calendar.getInstance().getTime());
    }

    public FilterData(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mRangeMonth = new Date(calendar.getTimeInMillis());
        mRangeWeek = new Date(calendar.getTimeInMillis());
        mRangeDateFrom = getFirstDayOfMonth(calendar.getTime());
        mRangeDateTo = getLastDayOfMonth(calendar.getTime());

        mSelected = RANGE_MONTH;
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

}

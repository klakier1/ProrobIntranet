package com.klakier.proRobIntranet;

import com.google.gson.Gson;

import org.junit.Test;

import java.sql.Date;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will enqueue on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void filterDate_isWorkCorrect() {
        FilterData fd = new FilterData(Date.valueOf("2021-02-09"));
        fd.setRangeWeek(53,2020);
        fd.setRangeDateToDate(Date.valueOf("2010-04-05"), Date.valueOf("2031-05-12"));
        fd.setRangeMonth(Calendar.APRIL,2021);
        Date d1 = fd.getRangeStartDate();
        Date d2 = fd.getRangeEndDate();

        fd.setRangeTypeSelected(FilterData.RANGE_WEEK);
        Date d3 = fd.getRangeStartDate();
        Date d4 = fd.getRangeEndDate();

        fd.setRangeTypeSelected(FilterData.RANGE_DATE_TO_DATE);
        Date d5 = fd.getRangeStartDate();
        Date d6 = fd.getRangeEndDate();

        Gson gson = new Gson();
        String fdJson = gson.toJson(fd, FilterData.class);

        FilterData newFd = gson.fromJson(fdJson, FilterData.class);

        assertTrue(newFd.equals(fd));
    }
}
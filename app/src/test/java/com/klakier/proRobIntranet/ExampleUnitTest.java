package com.klakier.proRobIntranet;

import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.assertEquals;

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
        fd.setRangeTypeSelected(FilterData.RANGE_MONTH);
        Date d1 = fd.getRangeStartDate();
        Date d2 = fd.getRangeEndDate();

        fd.setRangeTypeSelected(FilterData.RANGE_WEEK);
        Date d3 = fd.getRangeStartDate();
        Date d4 = fd.getRangeEndDate();

        fd.setRangeTypeSelected(FilterData.RANGE_DATE_TO_DATE);
        Date d5 = fd.getRangeStartDate();
        Date d6 = fd.getRangeEndDate();
    }
}
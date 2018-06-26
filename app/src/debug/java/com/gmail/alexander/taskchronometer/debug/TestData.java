package com.gmail.alexander.taskchronometer.debug;

import android.content.ContentResolver;

import java.util.GregorianCalendar;

/**
 * Created by:
 *
 * @author Alexander Vladimirov
 * <alexandervladimirov1902@gmail.com>
 * Generates random data.
 */
public class TestData {

    public static void generateTestData(ContentResolver contentResolver) {

    }

    private static int getRandomInt(int max) {
        return (int) Math.round(Math.random() * max);
    }

    private static long randomDateTime() {
        final int startYear = 2017;
        final int endYear = 2018;
        int sec = getRandomInt(59);
        int min = getRandomInt(59);
        int hour = getRandomInt(23);
        int month = getRandomInt(11);
        int year = startYear + getRandomInt(endYear - startYear);

        GregorianCalendar gregorianCalendar = new GregorianCalendar(year,month,1);
        int day = 1+ getRandomInt(gregorianCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)-1);
        gregorianCalendar.set(year,month,day,hour,min,sec);
        return gregorianCalendar.getTimeInMillis();
    }
}

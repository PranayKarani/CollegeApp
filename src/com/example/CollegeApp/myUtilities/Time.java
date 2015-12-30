package com.example.CollegeApp.myUtilities; // 19 Dec, 09:34 AM

import java.util.Calendar;

public class Time {

    public int hour;
    public int minute;

    public Time(int h, int m) {
        hour = h;
        minute = m;
    }

    /**
     * For 12 Hour format input
     * @param h - hours (in 12 hour format)
     * @param m - minutes
     * @param am - whether am or pm
     */
    public Time(int h, int m, boolean am) {
        if (am) {
            hour = h;
        } else {
            // avoiding (12+12):45 pm lecture timing
            // if time is like 12:45 pm, let hour be h and don't add 12 to it
            hour = h == 12?h:12 + h;
        }
        minute = m;
    }

    public int absoluteTime() {
        return hour * 100 + minute;
    }

    //NOTE: not to be used when specifing excact time like "lecture at 12:35pm"
    public int inMinutes(){
        return hour*60 + minute;
    }

    public static int getAbsoluteTimeNow() {
        return (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 100) + Calendar.getInstance().get(Calendar.MINUTE);
    }

}

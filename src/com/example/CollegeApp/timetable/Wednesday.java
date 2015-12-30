package com.example.CollegeApp.timetable; // 21 Dec, 04:33 AM

import com.example.CollegeApp.myUtilities.Time;
import com.example.CollegeApp.others.Subject;

import java.util.Calendar;

public class Wednesday extends WeekDay {

    public Wednesday() {
        super();

        dayCode = Calendar.WEDNESDAY;

        // for testing purposes
        addSubject(
                new Subject(4, "Quantitative Techniques", "Q. T.", "R. J. Pawar", new Time(11, 05, true), new Time(11, 50, true))
        );

        addSubject(
                new Subject(2, "MultiMedia", "M. M. ", "Huma Khan", new Time(11, 50, true), new Time(12, 35, false))
        );

        addSubject(
                new Subject(5, "Embedded Systems","E. S.", "Patwardhan", new Time(12, 35, false), new Time(2, 25, false))
        );

    }

}

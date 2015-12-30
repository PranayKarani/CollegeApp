package com.example.CollegeApp.timetable; // 19 Dec, 07:02 PM

import com.example.CollegeApp.myUtilities.Time;
import com.example.CollegeApp.others.Subject;

import java.util.Calendar;

public class Tuesday extends WeekDay {

    public Tuesday() {
        super();

        dayCode = Calendar.TUESDAY;

        // for testing purposes
        addSubject(
                new Subject(2,
                            "Multimedia",
                            "M. M.",
                            "Huma Khan",
                            new Time(11, 50),
                            new Time(1, 20, false))
        );

        addSubject(
                new Subject(1,
                            "Software Engineering",
                            "Soft. Engg.",
                            "Neha Patel",
                            new Time(1, 40, false),
                            new Time(3, 55, false))
        );

    }
}

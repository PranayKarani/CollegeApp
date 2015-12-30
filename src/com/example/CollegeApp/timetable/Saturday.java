package com.example.CollegeApp.timetable; // 20 Dec, 06:34 AM

import com.example.CollegeApp.myUtilities.Time;
import com.example.CollegeApp.others.Subject;

import java.util.Calendar;

public class Saturday extends WeekDay {

    public Saturday(){

        dayCode = Calendar.SATURDAY;

        addSubject(
                new Subject(3, "Java and Data Structures", "Java", "Kaushal Shah", new Time(7, 30, true), new Time(9, 45, true))
        );
    }

}

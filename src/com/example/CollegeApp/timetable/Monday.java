package com.example.CollegeApp.timetable; // 19 Dec, 07:37 AM

import com.example.CollegeApp.myUtilities.Time;
import com.example.CollegeApp.others.Subject;

import java.util.Calendar;

public class Monday extends WeekDay {

    //TODO visit: https://trello.com/c/t0hEOJn2/1-predefined-subjects

    public Monday() {
        super();
        dayCode = Calendar.MONDAY;

        // for testing purposes
        // donot these subject creation methods later on
        addSubject(
                new Subject(4, "Qunatitative Techniques", "Q.T.", "R. J. Pawar")
                        .setTimings(new Time(11, 05), new Time(12, 35))
        );

        addSubject(new Subject(5, "Embedded Systems", "E. S.", "Patwardhan", new Time(12, 35), new Time(14, 25)));

    }


}

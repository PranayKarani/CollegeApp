package com.example.CollegeApp.timetable; // 19 Dec, 07:44 AM

import com.example.CollegeApp.others.Subject;

public class WeekDay {

    public int dayCode;
    public Subject[] subjectsToday;// all subjects OR noof unique subject today

    public WeekDay(){
        subjectsToday = new Subject[5];// not more than 5 unique subjects expected this day
    }

    public void addSubject(Subject subject){

        // TODO later sort according to subject timings automatically
        //  and visit https://trello.com/c/Ft4DHsHd/2-random-order-subject-insertion

        for(int i = 0; i < subjectsToday.length; i++){
            if(subjectsToday[i] == null){
                subjectsToday[i] = subject;
                break;
            }
        }
    }

    public Subject getLastSubjectToday(){

        int subIn = 0;
        for(int i = 0; i < subjectsToday.length; i++){

            if(subjectsToday[i] != null){
                subIn = i;
            }

        }

        return subjectsToday[subIn];
    }

}

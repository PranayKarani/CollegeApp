package com.example.CollegeApp.others; // 19 Dec, 07:20 AM

import android.graphics.Color;
import android.widget.Toast;
import com.example.CollegeApp.R;
import com.example.CollegeApp.myUtilities.Time;

public class Subject {

    // current main purpose: used for timetable

    public int    subject_code;
    public String full_name;
    public String short_name;
    public String teacher;
    public Time   startTime, endTime;
    public Time duration;

    //NOTE: to be deprecated soon, beacuse https://trello.com/c/t0hEOJn2/1-predefined-subjects
    public Subject(int subject_code, String full_name, String short_name, String teacher, Time startTime, Time endTime) {
        this.subject_code = subject_code;
        this.full_name = full_name;
        this.short_name = short_name;
        this.teacher = teacher;
        this.startTime = startTime;
        this.endTime = endTime;


        int durH = endTime.hour - startTime.hour, durM;
        if (endTime.minute < startTime.minute) {
            durM = (endTime.minute + 60) - startTime.minute;
            durH--;
        } else {
            durM = endTime.minute - startTime.minute;
        }

        duration = new Time(durH, durM);
    }

    public Subject(int subject_code, String full_name, String short_name, String teacher) {
        this.subject_code = subject_code;
        this.full_name = full_name;
        this.short_name = short_name;
        this.teacher = teacher;
    }

    public Subject setTimings(Time startTime, Time endTime) {
        this.startTime = startTime;
        this.endTime = endTime;

        int durH = endTime.hour - startTime.hour, durM;
        if (endTime.minute < startTime.minute) {
            durM = (endTime.minute + 60) - startTime.minute;
            durH--;
        } else {
            durM = endTime.minute - startTime.minute;
        }

        duration = new Time(durH, durM);
        return this;
    }

    public String timeIn12String(Time t) {
        String time;

        // checking if greater than 12:00 pm
        if (t.absoluteTime() > 1200) {

            // is a single digit? yes, concate 0
            if (t.minute < 10) {

                // is hour > 12? yes, subtract 12 from it
                if (t.hour > 12) {
                    time = (t.hour - 12) + ":0" + t.minute + " pm";
                } else {
                    time = t.hour + ":0" + t.minute + " pm";
                }

            } else {
                if (t.hour > 12) {
                    time = (t.hour - 12) + ":" + t.minute + " pm";
                } else {
                    time = t.hour + ":" + t.minute + " pm";
                }
            }
        } else {
            if (t.minute < 10) {
                time = t.hour + ":0" + t.minute + " am";
            } else {
                time = t.hour + ":" + t.minute + " am";
            }
        }

        return time;
    }

    public String timeIn24String(Time t) {
        String time;

        // is a single digit? yes, concate 0
        if (t.minute < 10) {

            // is hour > 12? yes, subtract 12 from it
            time = t.hour + ":0" + t.minute;

        } else {

            time = t.hour + ":" + t.minute;

        }
        return time;
    }

    public int getAbsoluteStartTime() {
        return startTime.hour * 100 + startTime.minute;
    }

    public int getAbsoluteEndTime() {
        return endTime.hour * 100 + endTime.minute;
    }

    public int getColor(){
        switch(subject_code){
            case 1:
                return R.color.sub_1;
            case 2:
                return R.color.sub_2;
            case 3:
                return R.color.sub_3;
            case 4:
                return R.color.sub_4;
            case 5:
                return R.color.sub_5;
            default:
                return R.color.sub_1;// TODO change this default color
        }
    }

    public int getColor(int sub_code){
        switch(sub_code){
            case 1:
                return R.color.sub_1;
            case 2:
                return R.color.sub_2;
            case 3:
                return R.color.sub_3;
            case 4:
                return R.color.sub_4;
            case 5:
                return R.color.sub_5;
            default:
                return R.color.sub_1;// TODO change this default color
        }
    }

}

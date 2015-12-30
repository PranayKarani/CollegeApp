package com.example.CollegeApp.database; // 23 Dec, 06:17 AM

import com.example.CollegeApp.others.Student;

public class TStudent {

    public static final String table_name = "Student";

    public static final String s_id     = "_ID";
    public static final String s_rollNo = "roll_no";
    public static final String s_name   = "name";
    public static final String s_sem    = "sem";
    public static final String s_batch  = "batch";
    public static final String s_marks  = "marks";
    public static final String s_attend = "attendance";

    public String createTable() {
        return "CREATE TABLE " + table_name + " ( " +
                s_id + " INTEGER PRIMARY KEY, " +
                s_rollNo + " INTEGER, " + // NOTE: students in same sem should have unique rollnos
                s_name + " TEXT NOT NULL, " +
                s_sem + " INTEGER, " +
                s_batch + " INTEGER NOT NULL, " +
                s_marks + " INTEGER NOT NULL, " +
                s_attend + " REAL" +
                " )";

    }

}

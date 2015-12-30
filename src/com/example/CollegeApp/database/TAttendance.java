package com.example.CollegeApp.database; // 23 Dec, 06:18 AM

public class TAttendance {

    public final static String table_name = "Attendance";

    public final static String s_id = TStudent.s_id;
    public static final String a_sub1 = "Java";
    public static final String a_sub2 = "Maths";
    public static final String a_sub3 = "Multimedia";
    public static final String a_total = "average";

    public String createTable() {
        return "CREATE TABLE " + table_name + " ( " +
                s_id + " INTEGER PRIMARY KEY, " +
                a_sub1 + " INTEGER, " +
                a_sub2 + " INTEGER, " +
                a_sub3 + " INTEGER, " +
                a_total + " INTEGER," +
                "FOREIGN KEY("+s_id+") REFERENCES " + TStudent.table_name+"("+TStudent.s_id+")"+
                " )";

    }

}

package com.example.CollegeApp.database; // 25 Dec, 04:46 AM

public class TNotice {

    public static final String table_name = "Notice";

    public static final String n_id = "_ID";
    public static final String n_header = "header";
    public static final String n_body = "body";
    public static final String n_read = "read";
    public static final String n_fav = "favorite";
    public static final String n_date = "date";
    public static final String n_time = "time";

    public String createTable() {
        return "CREATE TABLE " + table_name + " ( " +
                n_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                n_header + " TEXT NOT NULL, " +
                n_body + " TEXT NOT NULL, " +
                n_read + " INTEGER DEFAULT 0," +
                n_fav + " INTEGER DEFAULT 0," +
                n_date + " INTEGER, " +
                n_time + " INTEGER" +
                " )";

    }


}

package com.example.CollegeApp.database; // 23 Dec, 06:19 AM

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int    DATABASE_VERSION = 2;
    public static final String DATABASE_NAME    = "College";

    TStudent    student_table;
    TMarks      marks_table;
    TAttendance attendance_table;
    TNotice     notice_table;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        student_table = new TStudent();
        marks_table = new TMarks();
        attendance_table = new TAttendance();
        notice_table = new TNotice();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateStudentTable(db, 0, DATABASE_VERSION);
        updateMarksTable(db, 0, DATABASE_VERSION);
        updateAttendanceTable(db, 0, DATABASE_VERSION);
        updateNoticeTable(db, 0, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateStudentTable(db, oldVersion, newVersion);
        updateMarksTable(db, oldVersion, newVersion);
        updateAttendanceTable(db, oldVersion, newVersion);
        updateNoticeTable(db, oldVersion, newVersion);
    }

    void updateStudentTable(SQLiteDatabase db, int oldVer, int newVer) {

        String q = null;

        if (oldVer < 1) {
            q = student_table.createTable();
        }

        if(q != null)
        db.execSQL(q);

    }

    void updateMarksTable(SQLiteDatabase db, int oldVer, int newVer) {

        String q = null;

        if (oldVer < 1) {
            q = marks_table.createTable();
        }

        if(q != null)
        db.execSQL(q);

    }

    void updateAttendanceTable(SQLiteDatabase db, int oldVer, int newVer) {

        String q = null;

        if (oldVer < 1) {
            q = attendance_table.createTable();
        }

        if(q != null)
        db.execSQL(q);

    }

    void updateNoticeTable(SQLiteDatabase db, int oldVer, int newVer) {

        String q = null;

        if (oldVer < 2) {
            q = notice_table.createTable();
        }

        if(q != null)
        db.execSQL(q);

    }

}

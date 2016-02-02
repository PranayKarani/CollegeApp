package com.example.CollegeApp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.CollegeApp.database.DatabaseHelper;
import com.example.CollegeApp.database.TAttendance;
import com.example.CollegeApp.database.TMarks;
import com.example.CollegeApp.database.TStudent;
import com.example.CollegeApp.others.Student;

public class ASignIn extends Activity {

    DatabaseHelper dbh;

    public static final String filename = "stud_data";

    boolean demo = true;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(!readDataFailed(getIntent().getBooleanExtra("logged_out",false))){
            Toast.makeText(this,"Welcome! " + Student.NAME,Toast.LENGTH_SHORT).show();// todo remove this
            startActivity(new Intent(this, AMain.class));
        }

        setContentView(R.layout.a_signin);

        dbh = new DatabaseHelper(this);

        // insert database entries here...

        demo = readDemoData();
        if (demo) {
            try {
                SQLiteDatabase db = dbh.getWritableDatabase();
                db.execSQL("INSERT INTO " + TStudent.table_name + " VALUES (102, 3, 'Mickey Mouse', 3, 1, 10, 10.3);");
                db.execSQL("INSERT INTO " + TMarks.table_name + " VALUES (102, 83, 76, 80, 10);");
                db.execSQL("INSERT INTO " + TAttendance.table_name + " VALUES (102, 80, 70, 80, 20);");
                db.close();
                Toast.makeText(this, "Demo row inserted.\nInsert: 102", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                demo = false;
                Toast.makeText(this, "Catched!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        writeDemoData(demo);

    }

    public void getIn(View view) {

        EditText id_et = (EditText) findViewById(R.id.id_editText);
        int id = Integer.parseInt(id_et.getText().toString());

        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM " + TStudent.table_name + " WHERE " + TStudent.s_id + " = " + id + ";"
                , null);

        if (c.getCount() > 0) {

            c.moveToFirst();

            // student exists
            Student.NAME = c.getString(c.getColumnIndex(TStudent.s_name));
            Student.ID = c.getInt(c.getColumnIndex(TStudent.s_id));
            Student.ROLLNO = c.getInt(c.getColumnIndex(TStudent.s_rollNo));
            Student.SEM = c.getInt(c.getColumnIndex(TStudent.s_sem));
            Student.BATCH = c.getInt(c.getColumnIndex(TStudent.s_batch));
            updateMarks(db, c);
            updateAttendance(db, c);
            writeData();
            startActivity(new Intent(this, AMain.class));
            Toast.makeText(this, "Welcome! " + Student.NAME, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "No such student found.\nPlease re-check and enter your ID again", Toast.LENGTH_LONG).show();
        }

        c.close();
        db.close();
    }

    void writeDemoData(boolean d) {
        SharedPreferences sp = getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("demo", d);
        editor.apply();
    }

    boolean readDemoData() {
        SharedPreferences sp = getSharedPreferences(filename, Context.MODE_PRIVATE);
        return sp.getBoolean("demo", true);
    }


    void writeData() {
        SharedPreferences sp = getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(Student.file_id, Student.ID);
        editor.putString(Student.file_name, Student.NAME);
        editor.putInt(Student.file_roll, Student.ROLLNO);
        editor.putInt(Student.file_sem, Student.SEM);
        editor.putInt(Student.file_batch, Student.BATCH);
        editor.putInt(Student.file_marks, Student.TOTALMARKS);
        editor.putFloat(Student.file_attend, Student.ATTEND);
        editor.apply();
    }

    public boolean readDataFailed(boolean loggedOut){
        SharedPreferences sp = getSharedPreferences(filename, Context.MODE_PRIVATE);

        if (loggedOut) {
            SharedPreferences.Editor edit = sp.edit();
            edit.clear();
            edit.apply();
        }

        Student.ID = sp.getInt(Student.file_id, -154);
        Student.NAME = sp.getString(Student.file_name, "no_name");
        Student.ROLLNO = sp.getInt(Student.file_roll, 0);
        Student.SEM = sp.getInt(Student.file_sem, 0);
        Student.BATCH = sp.getInt(Student.file_batch, 0);
        Student.TOTALMARKS = sp.getInt(Student.file_marks, 0);
        Student.ATTEND = sp.getFloat(Student.file_attend, 0);
        return Student.ID < 0;

    }

    void updateMarks(SQLiteDatabase db, Cursor c ){
        int totalMarks = 0;
        c = db.rawQuery("SELECT " + TMarks.m_sub1 + " FROM " + TMarks.table_name + " WHERE " + TMarks.s_id + " = " + Student.ID, null);
        c.moveToFirst();
        totalMarks += c.getInt(0);
        c = db.rawQuery("SELECT " + TMarks.m_sub2 + " FROM " + TMarks.table_name + " WHERE " + TMarks.s_id + " = " + Student.ID, null);
        c.moveToFirst();
        totalMarks += c.getInt(0);
        c = db.rawQuery("SELECT " + TMarks.m_sub3 + " FROM " + TMarks.table_name + " WHERE " + TMarks.s_id + " = " + Student.ID, null);
        c.moveToFirst();
        totalMarks += c.getInt(0);
        Student.TOTALMARKS = totalMarks;// Todo later, read this direct from some database instend of calculating

        // todo "updating database", this is not the Job of this app, remove this later
        ContentValues cv = new ContentValues();
        cv.put(TStudent.s_marks, Student.TOTALMARKS);
        db.update(TStudent.table_name, cv, TStudent.s_id + " = " + Student.ID, null);// NOTE: this is readonly database and hence error prone
        cv.clear();
        cv.put(TMarks.m_total, Student.TOTALMARKS);
        db.update(TMarks.table_name, cv, TMarks.s_id + " = " + Student.ID, null);// NOTE: this is readonly database and hence error prone

    }

    void updateAttendance(SQLiteDatabase db, Cursor c ){
        float att = 0;
        c = db.rawQuery("SELECT " + TAttendance.a_sub1 + " FROM " + TAttendance.table_name + " WHERE " + TAttendance.s_id + " = " + Student.ID, null);
        c.moveToFirst();
        att += c.getFloat(0);
        c = db.rawQuery("SELECT " + TAttendance.a_sub2 + " FROM " + TAttendance.table_name + " WHERE " + TAttendance.s_id + " = " + Student.ID, null);
        c.moveToFirst();
        att += c.getFloat(0);
        c = db.rawQuery("SELECT " + TAttendance.a_sub3 + " FROM " + TAttendance.table_name + " WHERE " + TAttendance.s_id + " = " + Student.ID, null);
        c.moveToFirst();
        att += c.getFloat(0);
        float avgAtt = att/3;
        Student.ATTEND = avgAtt;// Todo later, read this direct from some database instend of calculating

        // todo "updating database", this is not the Job of this app, remove this later
        ContentValues cv = new ContentValues();
        cv.put(TStudent.s_attend, Student.ATTEND);
        db.update(TStudent.table_name, cv, TStudent.s_id + " = " + Student.ID, null);// NOTE: this is readonly database and hence error prone
        cv.clear();
        cv.put(TAttendance.a_total, Student.ATTEND);
        db.update(TAttendance.table_name, cv, TAttendance.s_id + " = " + Student.ID, null);// NOTE: this is readonly database and hence error prone

    }

}

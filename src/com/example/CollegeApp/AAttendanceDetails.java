package com.example.CollegeApp;

import android.app.Activity;
import android.os.Bundle;
import com.example.CollegeApp.Workers.WAttendanceDetailsReader;
import com.example.CollegeApp.database.TAttendance;
import com.example.CollegeApp.others.Student;

public class AAttendanceDetails extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_attaendancedetails);

        new WAttendanceDetailsReader(this).execute("SELECT * FROM " + TAttendance.table_name + " WHERE " + TAttendance.s_id + " = " + Student.ID);

    }

}

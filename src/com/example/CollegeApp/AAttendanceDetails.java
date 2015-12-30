package com.example.CollegeApp;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.CollegeApp.database.DatabaseHelper;
import com.example.CollegeApp.database.TAttendance;
import com.example.CollegeApp.others.Student;

public class AAttendanceDetails extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_attaendancedetails);

        DatabaseHelper dbh = new DatabaseHelper(this);
        SQLiteDatabase db = dbh.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TAttendance.table_name + " WHERE " + TAttendance.s_id + " = " + Student.ID, null);
        c.moveToFirst();

        int[] attArray = new int[3];// TODO potential bug, later change this array size
        String[] subNames = new String[3];

        for(int i = 1; i <= attArray.length; i++){
            subNames[i - 1] = c.getColumnName(i);
            attArray[i - 1] = c.getInt(i);

            LinearLayout layout = (LinearLayout) findViewById(R.id.attendanceDetails);
            layout.addView(inflateMarksCell(subNames[i-1], attArray[i-1]));
            layout.addView(getLayoutInflater().inflate(R.layout.x_space, null));

        }

        LinearLayout layout = (LinearLayout) findViewById(R.id.attendanceDetails);
        layout.addView(inflateMarksCell("Overall",Student.ATTEND));
        layout.addView(getLayoutInflater().inflate(R.layout.x_space, null));

        c.close();
        db.close();

    }

    CardView inflateMarksCell(String subname, float att) {
        LayoutInflater linf = getLayoutInflater();
        CardView lf = null;
//        try {
        lf = (CardView) linf.inflate(R.layout.x_marks_cell, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        TextView nameTV = (TextView) lf.findViewById(R.id.sub_name);
        nameTV.setText(subname);

        TextView marksTV = (TextView) lf.findViewById(R.id.sub_marks);
        marksTV.setText(att+"");

        return lf;
    }

}

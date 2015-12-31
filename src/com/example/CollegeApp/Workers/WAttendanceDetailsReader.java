package com.example.CollegeApp.Workers; // 31 Dec, 10:22 PM

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.CollegeApp.R;
import com.example.CollegeApp.database.DatabaseHelper;
import com.example.CollegeApp.others.Student;

public class WAttendanceDetailsReader extends AsyncTask<String, Void, String> {

    Activity activity;

    int[]    attArray = new int[3];// TODO potential bug, later change this array size
    String[] subNames = new String[3];

    public WAttendanceDetailsReader(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {

        DatabaseHelper dbh = new DatabaseHelper(activity);
        SQLiteDatabase db = dbh.getReadableDatabase();

        Cursor c = db.rawQuery(params[0], null);
        c.moveToFirst();

        for (int i = 1; i <= attArray.length; i++) {
            subNames[i - 1] = c.getColumnName(i);
            attArray[i - 1] = c.getInt(i);

        }

        c.close();
        db.close();

        return "Done";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        for (int i = 1; i <= attArray.length; i++) {

            LinearLayout layout = (LinearLayout) activity.findViewById(R.id.attendanceDetails);
            layout.addView(inflateMarksCell(subNames[i - 1], attArray[i - 1]));
            layout.addView(activity.getLayoutInflater().inflate(R.layout.x_space, null));

        }

        LinearLayout layout = (LinearLayout) activity.findViewById(R.id.attendanceDetails);
        layout.addView(inflateMarksCell("Overall", Student.ATTEND));
        layout.addView(activity.getLayoutInflater().inflate(R.layout.x_space, null));

    }

    CardView inflateMarksCell(String subname, float att) {
        LayoutInflater linf = activity.getLayoutInflater();
        CardView lf = (CardView) linf.inflate(R.layout.x_marks_cell, null);


        TextView nameTV = (TextView) lf.findViewById(R.id.sub_name);
        nameTV.setText(subname);

        TextView marksTV = (TextView) lf.findViewById(R.id.sub_marks);
        marksTV.setText(att + "");

        return lf;
    }

}

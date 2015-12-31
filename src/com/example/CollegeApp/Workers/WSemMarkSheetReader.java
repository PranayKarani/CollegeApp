package com.example.CollegeApp.Workers; // 31 Dec, 05:14 PM

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.CollegeApp.R;
import com.example.CollegeApp.database.DatabaseHelper;
import com.example.CollegeApp.others.Student;

public class WSemMarkSheetReader extends AsyncTask<String, Integer, String> {

    Activity activity;
    int[]    marksArray = new int[3];// TODO potential bug, later change this array size
    String[] subNames   = new String[3];// TODO potential bug, later change this array size

    public WSemMarkSheetReader(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("log", "about to start new thread");
    }

    @Override
    protected String doInBackground(String... params) {
        DatabaseHelper dbh = new DatabaseHelper(activity);
        SQLiteDatabase db = dbh.getReadableDatabase();

        // TODO load marksheet from specifie sem later
        Cursor c = db.rawQuery(params[0], null);
        c.moveToFirst();

        for (int i = 1; i <= marksArray.length; i++) {
            subNames[i - 1] = c.getColumnName(i);
            marksArray[i - 1] = c.getInt(i);
            publishProgress(i);

        }

        c.close();
        db.close();
        return "Done";
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Toast.makeText(activity, values[0] + "%", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        for (int i = 1; i <= marksArray.length; i++) {

            LinearLayout layout = (LinearLayout) activity.findViewById(R.id.subject_marks_container);
            layout.addView(inflateMarksCell(subNames[i - 1], marksArray[i - 1]));
            layout.addView(activity.getLayoutInflater().inflate(R.layout.x_space, null));

        }


        LinearLayout layout = (LinearLayout) activity.findViewById(R.id.subject_marks_container);
        layout.addView(inflateMarksCell("Total", Student.TOTALMARKS));
        layout.addView(activity.getLayoutInflater().inflate(R.layout.x_space, null));

        Log.d("log", "Done updating the view");

    }

    CardView inflateMarksCell(String subname, int marks) {

        // TODO add subject color to cards

        LayoutInflater linf = activity.getLayoutInflater();
        CardView lf = (CardView) linf.inflate(R.layout.x_marks_cell, null);


        TextView nameTV = (TextView) lf.findViewById(R.id.sub_name);
        nameTV.setText(subname);

        TextView marksTV = (TextView) lf.findViewById(R.id.sub_marks);
        marksTV.setText(marks + "");

        lf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(activity,
                               "this will be replaced by dialog fragment,\nthat will show:\n " +
                                       "- your grade\n" +
                                       "- your practical marks\n" +
                                       "- your internal marks\n" +
                                       "- your theory marks", Toast.LENGTH_LONG).show();

            }
        });

        return lf;
    }

}

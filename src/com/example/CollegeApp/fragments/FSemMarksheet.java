package com.example.CollegeApp.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.CollegeApp.R;
import com.example.CollegeApp.database.DatabaseHelper;
import com.example.CollegeApp.database.TMarks;
import com.example.CollegeApp.others.Student;

public class FSemMarksheet extends Fragment {

    int selectedSem = -1;// default should be last sem. 0 = 1st sem, 5 = 6th sem

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selectedSem = getArguments().getInt("selected_sem");
        Log.d("log", "selected: " + selectedSem);
        return inflater.inflate(R.layout.f_selected_sem_marks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView) getActivity().findViewById(R.id.selected_sem_header)).setText("Selected semester: " + selectedSem);
        loadMarksheetForSem();

    }

    void loadMarksheetForSem(){
        DatabaseHelper dbh = new DatabaseHelper(getActivity());
        SQLiteDatabase db = dbh.getReadableDatabase();

        // TODO load marksheet from specifie sem later
        Cursor c = db.rawQuery("SELECT * FROM " + TMarks.table_name + " WHERE " + TMarks.s_id + " = " + Student.ID, null);
        c.moveToFirst();
        int[] marksArray = new int[3];// TODO potential bug, later change this array size
        String[] subNames = new String[3];// TODO potential bug, later change this array size

        for(int i = 1; i <= marksArray.length; i++){
            subNames[i - 1] = c.getColumnName(i);
            marksArray[i - 1] = c.getInt(i);

            LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.subject_marks_container);
            layout.addView(inflateMarksCell(subNames[i-1],marksArray[i-1]));
            layout.addView(getActivity().getLayoutInflater().inflate(R.layout.x_space, null));

        }

        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.subject_marks_container);
        layout.addView(inflateMarksCell("Total",Student.TOTALMARKS));
        layout.addView(getActivity().getLayoutInflater().inflate(R.layout.x_space, null));

        c.close();
        db.close();


    }

    CardView inflateMarksCell(String subname, int marks) {

        // TODO add subject color to cards

        LayoutInflater linf = getActivity().getLayoutInflater();
        CardView lf = (CardView) linf.inflate(R.layout.x_marks_cell, null);


        TextView nameTV = (TextView) lf.findViewById(R.id.sub_name);
        nameTV.setText(subname);

        TextView marksTV = (TextView) lf.findViewById(R.id.sub_marks);
        marksTV.setText(marks+"");

        lf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(),
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

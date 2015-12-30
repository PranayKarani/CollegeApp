package com.example.CollegeApp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.CollegeApp.AAttendanceDetails;
import com.example.CollegeApp.AMain;
import com.example.CollegeApp.AMarksDetails;
import com.example.CollegeApp.R;
import com.example.CollegeApp.myUtilities.MySurfaceView;
import com.example.CollegeApp.others.Student;

public class FMyDetails extends Fragment {

    public final static int ID = 1;

    MySurfaceView msv;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AMain.CURRENT_FRAGMENT = "FMyDetails";// CAUTION! particluar activity dependency

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        msv = (MySurfaceView) getActivity().findViewById(R.id.surfaceView);
        return inflater.inflate(R.layout.f_mydetails, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView) (getActivity().findViewById(R.id.details_name))).setText(Student.NAME);
        ((TextView) (getActivity().findViewById(R.id.details_rollNo))).setText("Roll no: " + Student.ROLLNO);
        ((TextView) (getActivity().findViewById(R.id.details_batch))).setText("Batch: " + Student.BATCH);

        Button att = (Button) getActivity().findViewById(R.id.att_button);
        att.setText("Overall Attendance: " + Student.ATTEND);
        att.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AAttendanceDetails.class));
            }
        });

        Button marks = (Button) getActivity().findViewById(R.id.marks_button);
        marks.setText("Total Marks: " + Student.TOTALMARKS + "\n(replace with graph)");
        marks.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AMarksDetails.class));
            }
        });

    }
}

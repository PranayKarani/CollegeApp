package com.example.CollegeApp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.CollegeApp.R;
import com.example.CollegeApp.Workers.WSemMarkSheetReader;
import com.example.CollegeApp.database.TMarks;
import com.example.CollegeApp.others.Student;

public class FSemMarksheet extends Fragment {

    int selectedSem = -1;// default should be last sem. 0 = 1st sem, 5 = 6th sem

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selectedSem = getArguments().getInt("selected_sem");
        return inflater.inflate(R.layout.f_selected_sem_marks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView) getActivity().findViewById(R.id.selected_sem_header)).setText("Selected semester: " + selectedSem);
        loadMarksheetForSem();

    }

    void loadMarksheetForSem(){

        new WSemMarkSheetReader(getActivity()).execute("SELECT * FROM " + TMarks.table_name + " WHERE " + TMarks.s_id + " = " + Student.ID);

    }


}

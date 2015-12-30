package com.example.CollegeApp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import com.example.CollegeApp.fragments.FSemMarksheet;
import com.example.CollegeApp.others.Student;

public class AMarksDetails extends Activity {

    int selectedSem = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_marksdetails);

        // if student is currently in 1st sem, no marksheet to be shown
        if(Student.SEM > 0){
            selectedSem = Student.SEM;
        }

        loadMarksFragForSem(selectedSem);

    }

    // TODO load new marksheet fragment on click event on graph, passing in selected sem on graph

    void loadMarksFragForSem(int sem){
        FSemMarksheet fsm = new FSemMarksheet();
        Bundle b = new Bundle();
        b.putInt("selected_sem", sem);
        Log.d("log", "selected sem: " + sem);
        fsm.setArguments(b);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.selected_sem_marks_container,fsm);
        ft.commit();
    }

}

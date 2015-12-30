package com.example.CollegeApp.fragments;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.CollegeApp.AMain;
import com.example.CollegeApp.R;
import com.example.CollegeApp.myUtilities.Time;
import com.example.CollegeApp.others.Subject;
import com.example.CollegeApp.timetable.Monday;
import com.example.CollegeApp.timetable.Tuesday;
import com.example.CollegeApp.timetable.Wednesday;
import com.example.CollegeApp.timetable.WeekDay;

import java.util.Calendar;

public class FTimeTable extends Fragment {

    static final int DAYS = 6;

    MyAdapter adapter;
    ViewPager viewPager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        AMain.CURRENT_FRAGMENT = "FTimeTable";// CAUTION! particluar activity dependency
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.f_timetable, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MyAdapter(getActivity().getSupportFragmentManager());

        viewPager = (ViewPager) getActivity().findViewById(R.id.time_table_pager);
        viewPager.setAdapter(adapter);


    }

    public static class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return FTimeTableDay.newInstance(i);
        }

        @Override
        public int getCount() {
            return DAYS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Monday";
                case 1:
                    return "Tuesday";
                case 2:
                    return "Wednesday";
                case 3:
                    return "Thrusday";
                case 4:
                    return "Friday";
                case 5:
                    return "Saturday";
                default:
                    return "Sunday";
            }
        }
    }

    public static class FTimeTableDay extends Fragment {

        int ID;

        static FTimeTableDay newInstance(int ID) {
            FTimeTableDay ftd = new FTimeTableDay();
            Bundle b = new Bundle();
            b.putInt("ID", ID);
            ftd.setArguments(b);
            return ftd;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getArguments() != null) {
                ID = getArguments().getInt("ID");
            } else {
                ID = 0;
            }

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.x_timetable_day, container, false);
            WeekDay dayToday = getSelectedDay();

            // filling the layout
            for (int i = 0; i < dayToday.subjectsToday.length; i++) {


                // if today is the lecture's actual day and ...


                    if (dayToday.subjectsToday[i] != null) {


                        // show the lecture on home screen
                        LinearLayout layout = (LinearLayout) v.findViewById(R.id.tt_holder);
                        CardView lec_cell = inflateLectureCell(dayToday.subjectsToday[i]);
                        lec_cell.setCardBackgroundColor(getResources().getColor(dayToday.subjectsToday[i].getColor()));
                        layout.addView(lec_cell);
                        layout.addView(getActivity().getLayoutInflater().inflate(R.layout.x_space, null));


                    }

            }

            return v;
        }

        private WeekDay getSelectedDay() {
            switch (ID) {
                case 0:
                    return new Monday();
                case 1:
                    return new Tuesday();
                case 2:
                    return new Wednesday();
                // TODO add new days
                default:
                    return new Monday();
            }

        }

        CardView inflateLectureCell(final Subject s) {
            LayoutInflater linf = getActivity().getLayoutInflater();
            CardView lf = null;
            lf = (CardView) linf.inflate(R.layout.x_timetable_lec_cell, null);


            // Lecture start time
            TextView stimeTV = (TextView) lf.findViewById(R.id.tt_start_time);
            stimeTV.setText(s.timeIn24String(s.startTime));

            // Lecture start time
            TextView etimeTV = (TextView) lf.findViewById(R.id.tt_end_time);
            etimeTV.setText(s.timeIn24String(s.endTime));

            // lecture name
            TextView nameTV = (TextView) lf.findViewById(R.id.tt_name);
            nameTV.setText(s.full_name);

            // lecture name
            TextView teachernameTV = (TextView) lf.findViewById(R.id.tt_teacher);
            teachernameTV.setText(s.teacher);

            lf.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    String duration;
                    if (s.duration.minute > 0) {
                        if (s.duration.hour > 0) {
                            duration = s.duration.hour + "h " + s.duration.minute + "m";
                        } else {
                            duration = s.duration.minute + "m";
                        }
                    } else {
                        duration = s.duration.hour + "h";
                    }

                    Toast.makeText(getActivity(),
                                   s.full_name + "\n"
                                           + s.teacher + "\n"
                                           + "from " + s.timeIn12String(s.startTime) + " to " + s.timeIn12String(s.endTime) + "\n"
                                           + "duration: " + duration,
                                   Toast.LENGTH_LONG)
                            .show();

                }
            });

            return lf;
        }
    }

}

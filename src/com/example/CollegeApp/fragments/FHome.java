package com.example.CollegeApp.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.example.CollegeApp.database.DatabaseHelper;
import com.example.CollegeApp.database.TNotice;
import com.example.CollegeApp.myUtilities.Time;
import com.example.CollegeApp.others.Subject;
import com.example.CollegeApp.timetable.Monday;
import com.example.CollegeApp.timetable.Saturday;
import com.example.CollegeApp.timetable.Tuesday;
import com.example.CollegeApp.timetable.Wednesday;
import com.example.CollegeApp.timetable.WeekDay;

import java.util.Calendar;

public class FHome extends Fragment {

    public final static int ID = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AMain.CURRENT_FRAGMENT = "FHome";// CAUTION! particluar activity dependency
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().invalidateOptionsMenu();
        return inflater.inflate(R.layout.f_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO check the semester of the student then load...
        ((TextView) getActivity().findViewById(R.id.schedule_header)).setText("Today's schedule");

        WeekDay dayToday = getDayToday();

        // filling the layout
        for (int i = 0; i < dayToday.subjectsToday.length; i++) {

            int actualDayToday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

            // if today is the lecture's actual day and ...
            if (dayToday.dayCode == actualDayToday) {

                if (dayToday.subjectsToday[i] != null) {

                    // ... if start time is greater than current time...
                    if (dayToday.subjectsToday[i].getAbsoluteStartTime() > Time.getAbsoluteTimeNow()) {

                        // show the lecture on home screen
                        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.schedule_container);
                        CardView lec_cell = inflateLectureCell(dayToday.subjectsToday[i]);
                        lec_cell.setCardBackgroundColor(getResources().getColor(dayToday.subjectsToday[i].getColor()));
                        layout.addView(lec_cell);
                        layout.addView(getActivity().getLayoutInflater().inflate(R.layout.x_space, null));

                    }
                }
            } else { // if not, show all lectures irrespective of timings

                if (dayToday.subjectsToday[i] != null) {

                    // show the lecture on home screen
                    LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.schedule_container);
                    CardView lec_cell = inflateLectureCell(dayToday.subjectsToday[i]);
                    lec_cell.setCardBackgroundColor(getResources().getColor(dayToday.subjectsToday[i].getColor()));
                    layout.addView(lec_cell);
                    layout.addView(getActivity().getLayoutInflater().inflate(R.layout.x_space, null));

                }

            }

        }

        loadNotices();

    }

    @Override
    public void onStop() {
        super.onStop();

        ((LinearLayout) getActivity().findViewById(R.id.home_notice_bucket)).removeAllViews();

    }

    CardView inflateLectureCell(final Subject s) {
        LayoutInflater linf = getActivity().getLayoutInflater();
        CardView lf = null;
//        try {
        lf = (CardView) linf.inflate(R.layout.x_lec_cell, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // Lecture start time
        TextView timeTV = (TextView) lf.findViewById(R.id.default_lec_time);
        timeTV.setText(s.timeIn24String(s.startTime));

        // lecture name
        TextView nameTV = (TextView) lf.findViewById(R.id.default_lec_name);
        nameTV.setText(s.short_name);

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

    public WeekDay getDayToday() {

        WeekDay today = null;

        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {

            case Calendar.MONDAY:
                Monday mon = new Monday();
                if (Time.getAbsoluteTimeNow() > mon.getLastSubjectToday().startTime.absoluteTime()) {
                    ((TextView) getActivity().findViewById(R.id.schedule_header)).setText("Tomorrow's schedule");
                    today = new Tuesday();
                } else {
                    today = mon;
                }
                break;

            case Calendar.TUESDAY:
                Tuesday tue = new Tuesday();

                // checking the today's last subject time
                if (Time.getAbsoluteTimeNow() > tue.getLastSubjectToday().endTime.absoluteTime()) {
                    ((TextView) getActivity().findViewById(R.id.schedule_header)).setText("Tomorrow's schedule");
                    today = new Wednesday();
                } else {
                    today = tue;
                }
                break;

            case Calendar.WEDNESDAY:
                Wednesday wed = new Wednesday();
                if (Time.getAbsoluteTimeNow() > wed.getLastSubjectToday().endTime.absoluteTime()) {
                    ((TextView) getActivity().findViewById(R.id.schedule_header)).setText("Tomorrow's schedule");
                    today = new Monday();// change to thrusday
                } else {
                    today = wed;
                }
                break;

            case Calendar.THURSDAY:
                today = new Monday();
                break;

            case Calendar.FRIDAY:
                today = new Monday();
                break;

            case Calendar.SATURDAY:
                Saturday sat = new Saturday();

                // checking the today's last subject time
                if (Time.getAbsoluteTimeNow() > sat.getLastSubjectToday().endTime.absoluteTime()) {
                    ((TextView) getActivity().findViewById(R.id.schedule_header)).setText("Next weeks's schedule");
                    today = new Monday();
                } else {
                    today = sat;
                }
                break;

        }

        return today;

    }

    public void loadNotices() {

        SQLiteDatabase db = new DatabaseHelper(getActivity()).getReadableDatabase();

        int unread = 0;

        // SELECT * FROM Notice ORDER BY read, date,time,favorite DESC
        Cursor c = db.rawQuery(
                "SELECT * FROM " + TNotice.table_name + " ORDER BY "
                        + TNotice.n_read + " ASC,"
                        + TNotice.n_fav + " DESC,"
                        + TNotice.n_date + " DESC,"
                        + TNotice.n_time + " DESC", null);

        // do stuff only when cursor contains something
        if (c.getCount() > 0) {

            LinearLayout urb = (LinearLayout) getActivity().findViewById(R.id.home_notice_bucket);

            c.moveToFirst();

            for (int i = 0; i < c.getCount(); i++) {

                // adding unread notice cells to notice_list scrollView
                int r = c.getInt(c.getColumnIndex(TNotice.n_read));
                if (r == 0) {

                    unread++;

                    urb.addView(inflateReadNoticeCell(c.getString(c.getColumnIndex(TNotice.n_header)), false));
                    urb.addView(getActivity().getLayoutInflater().inflate(R.layout.x_space, null));
                }

                c.moveToNext();

            }

        }

        getActivity().findViewById(R.id.notification_container).setVisibility(unread == 0 ? View.GONE : View.VISIBLE);

        c.close();
        db.close();

    }

    CardView inflateReadNoticeCell(final String header, final boolean fav) {


        CardView cv = (CardView) getActivity().getLayoutInflater().inflate(R.layout.x_notice_read_cell, null);

        // set header
        ((TextView) cv.findViewById(R.id.read_notice_header)).setText(header);

        // set fav star
        cv.findViewById(R.id.fav_star).setVisibility(fav ? View.VISIBLE : View.INVISIBLE);

        cv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction ft = FHome.this.getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new FNoticeboard());
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();

            }
        });

        return cv;

    }

}

package com.example.CollegeApp.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.CollegeApp.AMain;
import com.example.CollegeApp.ANotice;
import com.example.CollegeApp.R;
import com.example.CollegeApp.database.DatabaseHelper;
import com.example.CollegeApp.database.TNotice;

public class FNoticeboard extends Fragment {

    public static final int ID = 2;

    DatabaseHelper dbh;
    SQLiteDatabase db;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AMain.CURRENT_FRAGMENT = "FNoticeboard";// CAUTION! particluar activity dependency

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.f_noticeboard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("log", "View CRated!");

    }

    @Override
    public void onStart() {
        super.onStart();
        dbh = new DatabaseHelper(getActivity());
        db = dbh.getReadableDatabase();
        loadNotices();
        Log.d("log", "FNoticeB started");
    }

    @Override
    public void onStop() {
        super.onStop();

        db.close();

        // clearing buckets
        ((LinearLayout) getActivity().findViewById(R.id.unread_bucket)).removeAllViews();
        ((LinearLayout) getActivity().findViewById(R.id.fav_bucket)).removeAllViews();
        ((LinearLayout) getActivity().findViewById(R.id.read_bucket)).removeAllViews();

        Log.d("log", "FNoticeB stopped");

    }

    public void loadNotices() {

        int unread = 0, fav = 0, read = 0;

        // SELECT * FROM Notice ORDER BY read, date,time,favorite DESC
        Cursor c = db.rawQuery(
                "SELECT * FROM " + TNotice.table_name + " ORDER BY "
                        + TNotice.n_read + " ASC,"
                        + TNotice.n_fav + " DESC,"
                        + TNotice.n_date + " DESC,"
                        + TNotice.n_time + " DESC", null);

        // do stuff only when cursor contains something
        if (c.getCount() > 0) {

            c.moveToFirst();

            for (int i = 0; i < c.getCount(); i++) {

                // declare buckets
                LinearLayout urb = (LinearLayout) getActivity().findViewById(R.id.unread_bucket);
                LinearLayout fb = (LinearLayout) getActivity().findViewById(R.id.fav_bucket);
                LinearLayout rb = (LinearLayout) getActivity().findViewById(R.id.read_bucket);

                // adding unread notice cells to notice_list scrollView
                int r = c.getInt(c.getColumnIndex(TNotice.n_read));
                if (r == 0) {
                    unread++;
                    urb.addView(inflateUnreadNoticeCell(
                            c.getInt(c.getColumnIndex(TNotice.n_id)),
                            c.getString(c.getColumnIndex(TNotice.n_header)),
                            c.getString(c.getColumnIndex(TNotice.n_body)),
                            false,
                            c.getInt(c.getColumnIndex(TNotice.n_date)),
                            c.getInt(c.getColumnIndex(TNotice.n_time))
                    ));
                    urb.addView(getActivity().getLayoutInflater().inflate(R.layout.x_space, null));
                }

                int f = c.getInt(c.getColumnIndex(TNotice.n_fav));
                if (f != 0) {
                    fav++;
                    fb.addView(inflateReadNoticeCell(
                            c.getInt(c.getColumnIndex(TNotice.n_id)),
                            c.getString(c.getColumnIndex(TNotice.n_header)),
                            c.getString(c.getColumnIndex(TNotice.n_body)),
                            true,
                            c.getInt(c.getColumnIndex(TNotice.n_date)),
                            c.getInt(c.getColumnIndex(TNotice.n_time))
                    ));
                    fb.addView(getActivity().getLayoutInflater().inflate(R.layout.x_space, null));
                }

                if (r != 0 && f == 0) {
                    read++;
                    rb.addView(inflateReadNoticeCell(
                            c.getInt(c.getColumnIndex(TNotice.n_id)),
                            c.getString(c.getColumnIndex(TNotice.n_header)),
                            c.getString(c.getColumnIndex(TNotice.n_body)),
                            false,
                            c.getInt(c.getColumnIndex(TNotice.n_date)),
                            c.getInt(c.getColumnIndex(TNotice.n_time))
                    ));
                    rb.addView(getActivity().getLayoutInflater().inflate(R.layout.x_space, null));
                }

                c.moveToNext();

            }

        }

        // show buckets only if there are any unread notices
        (getActivity().findViewById(R.id.unread_container)).setVisibility(unread == 0 ? View.GONE : View.VISIBLE);
        (getActivity().findViewById(R.id.fav_container)).setVisibility(fav == 0 ? View.GONE : View.VISIBLE);
        (getActivity().findViewById(R.id.read_container)).setVisibility(read == 0 ? View.GONE : View.VISIBLE);

        c.close();

    }

    CardView inflateUnreadNoticeCell(final int ID, final String header, final String body, final boolean fav, final int date, final int time) {

        int noofLetters = 100;

        CardView cv = (CardView) getActivity().getLayoutInflater().inflate(R.layout.x_notice_unread_cell, null);

        // set header
        ((TextView) cv.findViewById(R.id.unread_notice_header)).setText(header);

        // set short desc
        if (body.length() > noofLetters) {
            ((TextView) cv.findViewById(R.id.unread_notice_short_desc)).setText(body.substring(0, noofLetters).concat("..."));
        } else {
            ((TextView) cv.findViewById(R.id.unread_notice_short_desc)).setText(body);
        }


        cv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO activate this to read the unread notice
                Intent i = new Intent(getActivity(), ANotice.class);

                i.putExtra("ID", ID);
                i.putExtra("header", header);
                i.putExtra("body", body);
                i.putExtra("fav", fav);
                i.putExtra("date", date);
                i.putExtra("time", time);
                startActivity(i);

            }
        });

        return cv;

    }

    CardView inflateReadNoticeCell(final int ID, final String header, final String body, final boolean fav, final int date, final int time) {


        CardView cv = (CardView) getActivity().getLayoutInflater().inflate(R.layout.x_notice_read_cell, null);

        // set header
        ((TextView) cv.findViewById(R.id.read_notice_header)).setText(header);

        // set fav star
        cv.findViewById(R.id.fav_star).setVisibility(fav ? View.VISIBLE : View.INVISIBLE);

        cv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO activate this to read the unread notice
                Intent i = new Intent(getActivity(), ANotice.class);
                i.putExtra("ID", ID)
                        .putExtra("header", header)
                        .putExtra("body", body)
                        .putExtra("fav", fav)
                        .putExtra("date", date)
                        .putExtra("time", time);
                startActivity(i);

            }
        });

        return cv;

    }

}

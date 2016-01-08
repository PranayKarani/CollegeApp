package com.example.CollegeApp.services; // 25 Dec, 06:40 AM

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.NotificationCompat;
import android.support.v7.app.NotificationCompat.Builder;
import com.example.CollegeApp.AMain;
import com.example.CollegeApp.R;
import com.example.CollegeApp.database.DatabaseHelper;
import com.example.CollegeApp.database.TNotice;
import com.example.CollegeApp.fragments.FNoticeboard;
import com.example.CollegeApp.myUtilities.Time;

import java.util.Calendar;

public class SNotice extends IntentService {

    public SNotice() {
        super("SNotice");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        synchronized (this){
            try{
                wait(10000);
                generateNotice(
                        "Awesome Notice header text",
                        "Notice body, yaydya khf kauj aka yada yaday ayda aj blah blhab blhab lhab lha lajslfsflasifjsja" +
                                "blah blhab lhab lhab lah bla hlahf la lsl  blha blah blha blha blah blah balh b lahld hksh sakhfsh" +
                                "blah blhab lhab lhab lah bla hlahf la lsl  blha blah blha blha blah blah balh b lahld hksh sakhfsh" +
                                "blah blhab lhab lhab lah bla hlahf la lsl  blha blah blha blha blah blah balh b lahld hksh sakhfsh" +
                                "blah blhab lhab lhab lah bla hlahf la lsl  blha blah blha blha blah blah balh b lahld hksh sakhfsh");
            } catch(Exception e){

            }
        }

    }

    void generateNotice(String header, String body) {


        // Define Notification builder
        NotificationCompat.Builder nb = new Builder(this);
        nb.setSmallIcon(R.drawable.ic_stat_name);
        nb.setContentTitle(header);
        nb.setAutoCancel(true);

        // define notifiation action
        Intent intent = new Intent(this, AMain.class);
        intent.putExtra("open_this", FNoticeboard.ID);

        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // set notification click behaviour
        nb.setContentIntent(pi);

        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();

        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR) - 2000;

        int absoluteDateToday = (year * 10000) + (month * 100) + date;

        ContentValues cv = new ContentValues();
        cv.put(TNotice.n_header, header);
        cv.put(TNotice.n_body, body);
        cv.put(TNotice.n_date, absoluteDateToday);
        cv.put(TNotice.n_time, Time.getAbsoluteTimeNow());// TODO take in 2 timings, recieved by user and sent by sender

        db.insert(TNotice.table_name, null, cv);

        Cursor cur = db.rawQuery("SELECT " + TNotice.n_id + " FROM " + TNotice.table_name + " WHERE "
                                         + TNotice.n_time + " = " + Time.getAbsoluteTimeNow() + " AND "
                                         + TNotice.n_header + " = '" + header + "' AND "
                                         + TNotice.n_date + " = " + absoluteDateToday
                , null);

        cur.moveToFirst();
        for (int i = 0; i < cur.getCount(); i++) {

            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(cur.getInt(cur.getColumnIndex(TNotice.n_id)), nb.build());

            cur.moveToNext();
        }

        cur.close();
        db.close();

    }

}

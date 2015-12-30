package com.example.CollegeApp;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.app.NotificationCompat.Builder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.example.CollegeApp.database.DatabaseHelper;
import com.example.CollegeApp.database.TNotice;
import com.example.CollegeApp.fragments.FHome;
import com.example.CollegeApp.fragments.FMyDetails;
import com.example.CollegeApp.fragments.FNoticeboard;
import com.example.CollegeApp.fragments.FTimeTable;
import com.example.CollegeApp.myUtilities.Time;
import com.example.CollegeApp.services.SNotice;

import java.util.Calendar;
import java.util.Random;

public class AMain extends AppCompatActivity {

    String[]              drawerListNames;
    DrawerLayout          drawerLayout;
    LinearLayout          drawerLinearLayout;
    ListView              drawerListView;
    ActionBarDrawerToggle drawerToggle;

    public static String CURRENT_FRAGMENT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.a_main);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));

        int open_this = getIntent().getIntExtra("open_this", FHome.ID);

        Fragment open_fragment;
        switch (open_this) {
            case FHome.ID:
                open_fragment = new FHome();
                break;
            case FMyDetails.ID:
                open_fragment = new FMyDetails();
                break;
            case FNoticeboard.ID:
                open_fragment = new FNoticeboard();
                break;
            default:
                open_fragment = new FHome();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, open_fragment).commit();


        drawerListNames = getResources().getStringArray(R.array.drawer_list);

        // main drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);


        // linear layout to contain various other layouts with in the main drawer layout
        drawerLinearLayout = (LinearLayout) findViewById(R.id.drawer_linearlayout);

        drawerListView = (ListView) findViewById(R.id.drawer_listview);
        drawerListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawerListNames));
        drawerListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment f;

                switch (position) {
                    case 0:
                        f = new FMyDetails();
                        break;
                    case 1:
                        // switch to noticeboard fragment
                        f = new FNoticeboard();
                        break;
                    case 2:
                        // switch to timetable fragment
                        f = new FTimeTable();
                        break;
                    /*case 3:
                        // switch staff contact fragment
                        break;*/
                    default:
                        f = new FHome();
                        // switch to home fragment
                        break;
                }

                ft.replace(R.id.fragment_container, f);
//                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();

                setTitle(drawerListNames[position]);//TODO potention bug when default case selected
                drawerListView.setItemChecked(position, true);
                drawerLayout.closeDrawer(drawerLinearLayout);

                invalidateOptionsMenu();

            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        if (CURRENT_FRAGMENT.equals("FNoticeboard")) {
            menu.findItem(R.id.delete_all_read).setVisible(true);
        } else {
            menu.findItem(R.id.delete_all_read).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.new_notification_generator:
                startService(new Intent(this, SNotice.class));
//                refreshFragment(new FHome());
                return true;
            case R.id.log_out:
                Intent intent = new Intent(this, ASignIn.class);
                intent.putExtra("logged_out", true);
                startActivity(intent);
                return true;
            case R.id.delete_all_read:

                SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();
                db.delete(TNotice.table_name, TNotice.n_read + " > 0 AND " + TNotice.n_fav + " < 1", null);
                refreshFragment(new FNoticeboard());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }



    @Override
    public void onBackPressed() {

        if (CURRENT_FRAGMENT.equals("FHome")) {
            // exit app

            new DialogFragment() {

                @NonNull
                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {

                    AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                    ab.setMessage("Exit app?")
                            .setPositiveButton("Yes", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dismiss();
                                    getActivity().finish();
                                }
                            }).setNegativeButton("No", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    });

                    return ab.create();
                }

            }.show(getSupportFragmentManager(), "ExitConfirmation");

        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new FHome());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            invalidateOptionsMenu();
        }

    }

    void refreshFragment(Fragment f) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, f)
                // NOTE! no transition animations while refreshing fragments
                .commit();
        invalidateOptionsMenu();
    }

}

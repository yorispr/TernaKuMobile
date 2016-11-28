package com.fintech.ternaku.TernakPerah.Alarm;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.fintech.ternaku.DatabaseHandler;
import com.fintech.ternaku.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ShowCalendarActivity extends AppCompatActivity {

    private Button btnSet;
    private CalendarView calendar;

    String id_sapi;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Jadwal");
        }
        id_sapi = db.getAlarmBirahiById(getIntent().getExtras().getString("id_alarm")).getId_sapi();
        Log.d("idsapi",id_sapi);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });fab.hide();
        calendar = (CalendarView)findViewById(R.id.calendarView);

        btnSet = (Button)findViewById(R.id.btnSetTanggal);
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                String selectedDate = sdf.format(new Date(calendar.getDate()));
                Log.d("selecteddate",selectedDate);

                final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                final int _id = (int) System.currentTimeMillis();

                Alarm al = new Alarm(0,String.valueOf(_id),"inseminasi_schedule",String.valueOf(new Date()),selectedDate,id_sapi);
                db.addAlarm(al);
                AlarmScheduler as = new AlarmScheduler();
                as.setAlarm(al,getApplicationContext());

                new SweetAlertDialog(ShowCalendarActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Jadwal berhasil ditambahkan")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();
            }
        });
/*
        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        final int _id = (int) System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH,21);

        Date date = cal.getTime();
        String formatteddate = new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(date);
        Log.d("calendar2",String.valueOf(cal.get(Calendar.DATE) +" "+ cal.get(Calendar.MONTH)+" "+ cal.get(Calendar.YEAR) + " "+cal.get(Calendar.HOUR_OF_DAY) +":"+cal.get(Calendar.MINUTE)));
        Log.d("calendar3",formatteddate);
        Alarm al = new Alarm(0,String.valueOf(_id),"heat",String.valueOf(new Date()),formatteddate,id_sapi);
        db.addAlarm(al);
        AlarmScheduler as = new AlarmScheduler();
        as.setAlarm(al,getApplicationContext());
        cancelNotification(getApplicationContext(),0);*/
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }
}

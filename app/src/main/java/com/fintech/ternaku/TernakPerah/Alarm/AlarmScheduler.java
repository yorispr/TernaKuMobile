package com.fintech.ternaku.TernakPerah.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fintech.ternaku.TernakPerah.Main.Scheduler.AlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.content.Context.ALARM_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by YORIS on 11/10/16.
 */

public class AlarmScheduler {

    public void setAlarm(Alarm alarm, Context context){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        Date date = new Date();
        try {
            date = dateFormat.parse(alarm.getAlarm_date());
            Log.d("TanggalReminder", date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cur_cal = Calendar.getInstance();
        cur_cal.setTime(date);
        Calendar cal = new GregorianCalendar();

        cal.set(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
        cal.set(Calendar.HOUR_OF_DAY, cur_cal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cur_cal.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cur_cal.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cur_cal.get(Calendar.MILLISECOND));
        cal.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
        cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));
        cal.set(Calendar.YEAR, cur_cal.get(Calendar.YEAR));

        PendingIntent pendingIntent;

        Intent myIntent = new Intent(context, AlarmReceiver.class);
        Bundle b = new Bundle();
        b.putString("extra",alarm.getJenis());
        b.putString("id_alarm",alarm.getId_alarm());
        b.putString("id_sapi",alarm.getId_sapi());

        myIntent.putExtras(b);

        //final int _id = (int) System.currentTimeMillis();
        pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(alarm.getId_alarm()), myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
        Log.d("calendar",String.valueOf(cal.get(Calendar.DATE) +" "+ cal.get(Calendar.MONTH)+" "+ cal.get(Calendar.YEAR) + " "+cal.get(Calendar.HOUR_OF_DAY) +":"+cal.get(Calendar.MINUTE)));

        /*
        long yourmilliseconds = cal.getTimeInMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(yourmilliseconds);


        Intent intentt = new Intent();
        intentt.setAction("REFRESH_DATE");
        sendBroadcast(intentt);*/
    }

    public void turnOffAlarm(String id_alarm){
        Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(id_alarm), myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT).cancel();
    }

}

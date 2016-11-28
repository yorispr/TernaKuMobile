package com.fintech.ternaku.TernakPerah.Main.Scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fintech.ternaku.TernakPerah.Alarm.Alarm;
import com.fintech.ternaku.TernakPerah.Alarm.AlarmScheduler;

import java.util.Calendar;
import java.util.Date;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent service1 = new Intent(context, AlarmService.class);
        service1.putExtras(intent.getExtras());
        try{
            if (intent.hasExtra("reminder")) {
                service1.putExtra("reminder", "reminder");
                Log.d("EXTRAReminder","masuk");
            }
            else if (intent.hasExtra("extendheat")) {
                AlarmScheduler as = new AlarmScheduler();
                final int _id = (int) System.currentTimeMillis();
                Alarm al = new Alarm();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY,12);
                al.setId(0);
                al.setId_alarm(String.valueOf(_id));
                al.setAlarm_date(cal.getTime().toString());
                al.setJenis("extendheat_alarm");
                al.setCreated_date(String.valueOf(new Date()));

                as.setAlarm(al,context);
                Log.d("ExtendHeat","masuk");
            }
            else if (intent.hasExtra("extendheat_alarm")) {
                service1.putExtra("extendheat_alarm", "extendheat_alarm");
                Bundle b = intent.getExtras();
                service1.putExtras(b);
                Log.d("ExtendHeatAlarm","masuk");
            }

            else if(intent.hasExtra("extra")){
                Bundle b = intent.getExtras();
                String ex = intent.getExtras().getString("extra");
                if(ex.equalsIgnoreCase("inseminasi_schedule")){
                    service1.putExtra("inseminasi_schedule", "inseminasi_schedule");
                    service1.putExtras(b);
                    Log.d("EXTRAInseminasi","masuk");
                }else if(ex.equalsIgnoreCase("heat")){
                    service1.putExtra("heat", "heat");
                    service1.putExtras(b);
                    Log.d("EXTRAHeat","masuk");
                }else if(ex.equalsIgnoreCase("inseminasi")){
                    service1.putExtra("inseminasi", "inseminasi");
                    service1.putExtras(b);
                    Log.d("EXTRAInseminasi","masuk");
                }else if(ex.equalsIgnoreCase("ins_melahirkan")){
                    service1.putExtra("ins_melahirkan", "ins_melahirkan");
                    service1.putExtras(b);
                    Log.d("EXTRAins_melahirkan","masuk");
                }
            }
            context.startService(service1);
        }catch(NullPointerException e)
        {
            e.printStackTrace();
        }

    }
}

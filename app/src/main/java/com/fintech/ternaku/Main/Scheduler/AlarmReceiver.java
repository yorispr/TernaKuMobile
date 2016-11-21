package com.fintech.ternaku.Main.Scheduler;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


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
            context.startService(service1);
        }catch(NullPointerException e)
        {
            e.printStackTrace();
        }

    }
}

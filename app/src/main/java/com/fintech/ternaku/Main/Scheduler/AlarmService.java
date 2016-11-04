package com.fintech.ternaku.Main.Scheduler;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.fintech.ternaku.Main.MainActivity;
import com.fintech.ternaku.R;

public class AlarmService extends Service {
    private NotificationManager mManager;
    WindowManager mWindowManager;
    Ringtone ringtoneAlarm;
    View mView;
    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (intent != null) {
            Log.d("reminder","masukintentnull");

            if (getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).contains("keyIdPengguna")) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                Log.d("reminder","masukcekpreference");

                //if (!prefs.getBoolean("checkBoxReminder", true)) {
                if (intent.hasExtra("reminder")){
                    Log.d("reminder","masukservice");
                    Bundle b = intent.getExtras();
                    final int _id = (int) System.currentTimeMillis();
                    Intent intent2 = new Intent(this, MainActivity.class);

                    PendingIntent pendingIntent = PendingIntent.getActivity(this, _id, intent2, PendingIntent.FLAG_ONE_SHOT);

                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                            .setContentTitle(b.getString("judul"))
                            .setContentText(b.getString("isi"))
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setSmallIcon(R.drawable.ic_check)
                            //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                            .setVibrate(new long[]{500, 15000})
                            .setLights(0x0000FF, 1000, 3500)
                            .setContentIntent(pendingIntent)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setOngoing(true);
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, notificationBuilder.build());
                }
            }
        }
    }


    private void hideDialog(){
        if(mView != null && mWindowManager != null){
            mWindowManager.removeView(mView);
            mView = null;
        }
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}

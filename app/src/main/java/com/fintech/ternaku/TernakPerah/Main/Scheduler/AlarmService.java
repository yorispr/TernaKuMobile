package com.fintech.ternaku.TernakPerah.Main.Scheduler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.fintech.ternaku.TernakPerah.Alarm.ShowCalendarActivity;
import com.fintech.ternaku.DatabaseHandler;
import com.fintech.ternaku.TernakPerah.Main.MainActivity;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Kesuburan.AddInseminasi;
import com.fintech.ternaku.TernakPerah.Main.TambahData.Kesuburan.AddMelahirkan;
import com.fintech.ternaku.TernakPerah.Main.TambahData.PindahTernak.PindahTernak;
import com.fintech.ternaku.R;

public class AlarmService extends Service {
    private NotificationManager mManager;
    WindowManager mWindowManager;
    Ringtone ringtoneAlarm;
    NotificationManager notificationManager;
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    DatabaseHandler db = new DatabaseHandler(this);

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

                    ShowNotification(b,pendingIntent);
                }
                else if (intent.hasExtra("heat")){
                    Log.d("heat","masukservice");
                    final int _id = (int) System.currentTimeMillis();
                    Intent intent2 = new Intent(this, MainActivity.class);

                    PendingIntent pendingIntent = PendingIntent.getActivity(this, _id, intent2, PendingIntent.FLAG_ONE_SHOT);
                    String id_alarm = db.getAlarmBirahiById(intent.getStringExtra("id_alarm")).getId_alarm();
                    //String id_sapi = db.getAlarmBirahiById(intent.getStringExtra("id_alarm")).getId_sapi();
                    String id_sapi  = intent.getExtras().getString("id_sapi");
                    ShowNotificationAlarmHeat(id_alarm,"Periksa Sapi Birahi","Sapi No: "+id_sapi,pendingIntent);
                }
                else if (intent.hasExtra("extendheat_alarm")){
                    Log.d("extendheat_alarm","masukservice");
                    final int _id = (int) System.currentTimeMillis();
                    Intent intent2 = new Intent(this, MainActivity.class);

                    PendingIntent pendingIntent = PendingIntent.getActivity(this, _id, intent2, PendingIntent.FLAG_ONE_SHOT);
                    String id_alarm = db.getAlarmBirahiById(intent.getStringExtra("id_alarm")).getId_alarm();
                    //String id_sapi = db.getAlarmBirahiById(intent.getStringExtra("id_alarm")).getId_sapi();
                    String id_sapi  = intent.getExtras().getString("id_sapi");
                    ShowNotificationAlarmHeat(id_alarm,"Periksa Sapi Birahi","Sapi No: "+id_sapi,pendingIntent);
                }
                else if (intent.hasExtra("inseminasi_schedule")){
                    Log.d("inseminasi_schedule","masukservice");
                    final int _id = (int) System.currentTimeMillis();
                    Intent intent2 = new Intent(this, AddInseminasi.class);
                    intent2.putExtras(intent.getExtras());
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, _id, intent2, PendingIntent.FLAG_ONE_SHOT);
                    String id_alarm = db.getAlarmBirahiById(intent.getStringExtra("id_alarm")).getId_alarm();
                    String id_sapi  = intent.getExtras().getString("id_sapi");
                    ShowNotificationAlarmInseminasi(id_alarm,"Sapi Inseminasi Hari ini","Sapi No: "+id_sapi, id_sapi,pendingIntent);
                }
                else if (intent.hasExtra("inseminasi")){
                    Log.d("inseminasi","masukservice");
                    final int _id = (int) System.currentTimeMillis();
                    Intent intent2 = new Intent(this, AddMelahirkan.class);
                    intent2.putExtras(intent.getExtras());
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, _id, intent2, PendingIntent.FLAG_ONE_SHOT);
                    String id_alarm = db.getAlarmBirahiById(intent.getStringExtra("id_alarm")).getId_alarm();
                    String id_sapi  = intent.getExtras().getString("id_sapi");
                    ShowNotificationAlarmInseminasiSukses(id_alarm,"Periksa Keberhasilan Inseminasi","Sapi No: "+id_sapi, id_sapi,pendingIntent);
                }
                else if (intent.hasExtra("ins_melahirkan")){
                    Log.d("ins_melahirkan","masukservice");
                    final int _id = (int) System.currentTimeMillis();
                    Intent intent2 = new Intent(this, AddMelahirkan.class);
                    intent2.putExtras(intent.getExtras());
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, _id, intent2, PendingIntent.FLAG_ONE_SHOT);
                    String id_alarm = db.getAlarmBirahiById(intent.getStringExtra("id_alarm")).getId_alarm();
                    String id_sapi  = intent.getExtras().getString("id_sapi");
                    ShowNotificationAlarmMelahirkanSukses(id_alarm,"Sapi akan segera melahirkan","Sapi No: "+id_sapi, id_sapi,pendingIntent);
                }
            }
        }
    }

    private void ShowNotificationAlarmMelahirkanSukses(String id_alarm, String judul, String isi, String id_sapi, PendingIntent pendingIntent){
        Intent intentMelahirkanPindah = new Intent(this, PindahTernak.class);
        intentMelahirkanPindah.putExtra("id_alarm",id_alarm);
        Bundle b = new Bundle();
        b.putString("id_sapi",id_sapi);
        intentMelahirkanPindah.putExtras(b);
        PendingIntent childPIntentBerhasil = PendingIntent.getActivity(this, 0, intentMelahirkanPindah, PendingIntent.FLAG_ONE_SHOT);

        Intent intentInseminasiGagal = new Intent(this, AddMelahirkan.class);
        intentInseminasiGagal.putExtra("id_alarm",id_alarm);
        intentInseminasiGagal.putExtras(b);

        PendingIntent childPIntentGagal = PendingIntent.getActivity(this, 0, intentInseminasiGagal, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(judul)
                .setContentText(isi)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.ic_notifications_active)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setVibrate(new long[]{500, 15000})
                .setLights(0x0000FF, 1000, 3500)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .addAction(R.drawable.ic_check, "Pindah Kandang", childPIntentBerhasil)

                .setOngoing(true);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }


    private void ShowNotificationAlarmInseminasiSukses(String id_alarm, String judul, String isi, String id_sapi, PendingIntent pendingIntent){
        Intent intentInseminasiBerhasil = new Intent(this, AddMelahirkan.class);
        intentInseminasiBerhasil.putExtra("id_alarm",id_alarm);
        Bundle b = new Bundle();
        b.putString("id_sapi",id_sapi);
        intentInseminasiBerhasil.putExtras(b);
        PendingIntent childPIntentBerhasil = PendingIntent.getActivity(this, 0, intentInseminasiBerhasil, PendingIntent.FLAG_ONE_SHOT);

        Intent intentInseminasiGagal = new Intent(this, AddMelahirkan.class);
        intentInseminasiGagal.putExtra("id_alarm",id_alarm);
        intentInseminasiGagal.putExtras(b);

        PendingIntent childPIntentGagal = PendingIntent.getActivity(this, 0, intentInseminasiGagal, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(judul)
                .setContentText(isi)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.ic_notifications_active)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setVibrate(new long[]{500, 15000})
                .setLights(0x0000FF, 1000, 3500)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .addAction(R.drawable.ic_check, "Berhasil", childPIntentBerhasil)
                .addAction(R.drawable.ic_action_cancel_holo_light, "Gagal", childPIntentGagal)

                .setOngoing(true);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void ShowNotificationAlarmInseminasi(String id_alarm, String judul, String isi, String id_sapi, PendingIntent pendingIntent){
        Intent intentInseminasi = new Intent(this, AddInseminasi.class);
        intentInseminasi.putExtra("id_alarm",id_alarm);
        Bundle b = new Bundle();
        b.putString("id_sapi",id_sapi);
        intentInseminasi.putExtras(b);
        PendingIntent childPIntent = PendingIntent.getActivity(this, 0, intentInseminasi, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(judul)
                .setContentText(isi)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.ic_notifications_active)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setVibrate(new long[]{500, 15000})
                .setLights(0x0000FF, 1000, 3500)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .addAction(R.drawable.ic_action_warning, "Inseminasi", childPIntent)
                .setOngoing(true);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void ShowNotificationAlarmHeat(String id_alarm, String judul, String isi, PendingIntent pendingIntent){
        Intent intentCalendar = new Intent(this, ShowCalendarActivity.class);
        intentCalendar.putExtra("id_alarm",id_alarm);
        PendingIntent childPIntent = PendingIntent.getActivity(this, 0, intentCalendar, PendingIntent.FLAG_ONE_SHOT);
        Intent intentExtend = new Intent(this, AlarmReceiver.class);
        intentCalendar.putExtra("extendheat","extendheat");
        PendingIntent extend = PendingIntent.getActivity(this, 0, intentExtend, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(judul)
                .setContentText(isi)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.ic_notifications_active)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setVibrate(new long[]{500, 15000})
                .setLights(0x0000FF, 1000, 3500)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .addAction(R.drawable.ic_action_phone_start, "Hubungi Dokter", childPIntent)
                .addAction(R.drawable.ic_action_warning, "Inseminasi", childPIntent)
                .addAction(R.drawable.ic_action_clock_holo, "Perpanjang waktu birahi", extend)

                .setOngoing(true);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void ShowNotification(Bundle b, PendingIntent pendingIntent){
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
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void hideDialog(){
        if(mView != null && mWindowManager != null){
            mWindowManager.removeView(mView);
            mView = null;
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}

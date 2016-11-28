package com.fintech.ternaku.TernakPerah.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fintech.ternaku.DatabaseHandler;
import com.fintech.ternaku.TernakPerah.Main.MainActivity;
import com.fintech.ternaku.TernakPerah.Main.Pengingat.ReminderModel;
import com.fintech.ternaku.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by YORIS on 9/29/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        DatabaseHandler db = new DatabaseHandler(this);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            ReminderModel reminder = new ReminderModel();
            reminder.setId_protocol(remoteMessage.getData().get("id_reminder"));
            reminder.setJudul(remoteMessage.getData().get("judul"));
            reminder.setIsi(remoteMessage.getData().get("isi"));
            if(remoteMessage.getData().get("isimportant").equalsIgnoreCase("true")){
                reminder.setImportant(true);
            }else{reminder.setImportant(false);}
            reminder.setCreator(remoteMessage.getData().get("creator"));
            reminder.setCreator_id(remoteMessage.getData().get("creator_id"));
            reminder.setTimestamp(remoteMessage.getData().get("timestamp"));
            reminder.setIsread(0);
            reminder.setSchedule_time(remoteMessage.getData().get("scheduletime"));
            db.addReminder(reminder);
            String idpengguna = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);

            if(!reminder.getCreator_id().equalsIgnoreCase(idpengguna)) {
                sendNotification(reminder);
            }

            Intent intent = new Intent();
            intent.setAction("com.tutorialspoint.CUSTOM_INTENT");
            sendBroadcast(intent);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]



    /**
     * Create and show a simple notification containing the received FCM message.
     *
     */
    private void sendNotification(ReminderModel pm) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("position","2");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_action_clock)
                .setContentTitle(pm.getJudul())
                .setContentText(pm.getIsi())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}

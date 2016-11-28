package com.fintech.ternaku;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fintech.ternaku.TernakPerah.Alarm.Alarm;
import com.fintech.ternaku.TernakPerah.Main.Pengingat.ReminderModel;
import com.fintech.ternaku.TernakPerah.Main.Scheduler.AlarmReceiver;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by YORIS on 9/29/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db_ternaku";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TBL_REMINDER = "CREATE TABLE TBL_REMINDER (ID_REMINDER TEXT , JUDUL TEXT, ISI TEXT, ISIMPORTANT INTEGER, CREATOR_ID TEXT, CREATOR TEXT, TIMESTAMP TEXT, ISREAD INTEGER, SCHEDULETIME TEXT)";
        String CREATE_TBL_ALARM = "CREATE TABLE TBL_ALARM(ID INTEGER AUTO INCREMENT , ID_ALARM TEXT, JENIS_ALARM TEXT,CREATED_DATE TEXT, ALARM_DATE TEXT, ID_SAPI TEXT)";

        db.execSQL(CREATE_TBL_ALARM);
        db.execSQL(CREATE_TBL_REMINDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "TBL_REMINDER");
        db.execSQL("DROP TABLE IF EXISTS " + "TBL_ALARM");

        onCreate(db);
    }

    public void addReminder(ReminderModel reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.setForeignKeyConstraintsEnabled(false);
        ContentValues values = new ContentValues();
        Log.d("Datasse",reminder.getJudul());
        values.put("ID_REMINDER", reminder.getId_protocol()); // Contact Name
        values.put("JUDUL", reminder.getJudul()); // Contact Phone Number
        values.put("ISI", reminder.getIsi());
        if(reminder.isImportant()){
            values.put("ISIMPORTANT", 1);
        }else{
            values.put("ISIMPORTANT", 0);
        }
        values.put("CREATOR_ID", reminder.getCreator_id());
        values.put("CREATOR", reminder.getCreator());
        values.put("TIMESTAMP", reminder.getTimestamp());
        values.put("ISREAD", 0);
        values.put("SCHEDULETIME",reminder.getSchedule_time());

        // Inserting Row
        db.insert("TBL_REMINDER", null, values);

        db.close(); // Closing database connection
    }


    public void addAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.setForeignKeyConstraintsEnabled(false);
        ContentValues values = new ContentValues();
        values.put("ID_ALARM", alarm.getId_alarm());
        values.put("JENIS_ALARM", alarm.getJenis());
        values.put("CREATED_DATE", alarm.getCreated_date());
        values.put("ALARM_DATE", alarm.getAlarm_date());
        values.put("ID_SAPI", alarm.getId_sapi());

        // Inserting Row
        db.insert("TBL_ALARM", null, values);
        db.close(); // Closing database connection
    }


    public Alarm getAlarmBirahiById(String id_alarm) {
        ArrayList<Alarm> AlarmList = new ArrayList<Alarm>();
        // Select All Query
        String selectQuery = "SELECT * FROM TBL_ALARM WHERE ID_ALARM = "+id_alarm;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Alarm al = new Alarm();
        // looping through all rows and adding to list
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    al.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                    al.setId_alarm(cursor.getString(cursor.getColumnIndex("ID_ALARM")));
                    al.setJenis(cursor.getString(cursor.getColumnIndex("JENIS_ALARM")));
                    al.setCreated_date(cursor.getString(cursor.getColumnIndex("CREATED_DATE")));
                    al.setAlarm_date(cursor.getString(cursor.getColumnIndex("ALARM_DATE")));
                    al.setId_sapi(cursor.getString(cursor.getColumnIndex("ID_SAPI")));

                    AlarmList.add(al);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return al;
    }

    public void TurnOffAlarmByIdSapi(String id_sapi) {
        ArrayList<Alarm> AlarmList = new ArrayList<Alarm>();
        String heat = "heat";
        // Select All Query
        String selectQuery = "SELECT * FROM TBL_ALARM WHERE ID_SAPI = "+id_sapi+" AND JENIS_ALARM = "+heat;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Alarm al = new Alarm();
        // looping through all rows and adding to list
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    al.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                    al.setId_alarm(cursor.getString(cursor.getColumnIndex("ID_ALARM")));
                    al.setJenis(cursor.getString(cursor.getColumnIndex("JENIS_ALARM")));
                    al.setCreated_date(cursor.getString(cursor.getColumnIndex("CREATED_DATE")));
                    al.setAlarm_date(cursor.getString(cursor.getColumnIndex("ALARM_DATE")));
                    al.setId_sapi(cursor.getString(cursor.getColumnIndex("ID_SAPI")));

                    AlarmList.add(al);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        for(int i=0;i<AlarmList.size();i++) {
            turnOffAlarm(AlarmList.get(i).getId_alarm());
        }
    }

    public void turnOffAlarm(String id_alarm){
        Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(id_alarm), myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT).cancel();
    }


    public ArrayList<ReminderModel> getReminder() {
        ArrayList<ReminderModel> reminderList = new ArrayList<ReminderModel>();
        // Select All Query
        String selectQuery = "SELECT * FROM TBL_REMINDER ORDER BY TIMESTAMP DESC";
        Log.d("QUERY", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    ReminderModel r = new ReminderModel();
                    r.setId_protocol(cursor.getString(cursor.getColumnIndex("ID_REMINDER")));
                    r.setJudul(cursor.getString(cursor.getColumnIndex("JUDUL")));
                    r.setIsi(cursor.getString(cursor.getColumnIndex("ISI")));
                    r.setCreator_id(cursor.getString(cursor.getColumnIndex("CREATOR_ID")));
                    r.setCreator(cursor.getString(cursor.getColumnIndex("CREATOR")));
                    if(cursor.getColumnIndex("ISIMPORTANT")==1){
                        r.setImportant(true);
                    }else{
                        r.setImportant(false);
                    }
                   r.setTimestamp(cursor.getString(cursor.getColumnIndex("TIMESTAMP")));
                    r.setSchedule_time(cursor.getString(cursor.getColumnIndex("SCHEDULETIME")));
                    Log.d("REMINDERDATA", cursor.getString(cursor.getColumnIndex("ID_REMINDER")));

                    // Adding contact to list
                    reminderList.add(r);
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return reminderList;
    }

    public ReminderModel GetReminderById(String id) {
        String selectQuery = "SELECT * FROM TBL_REMINDER WHERE ID_REMINDER = '"+id+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ReminderModel r = new ReminderModel();

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    r.setId_protocol(cursor.getString(cursor.getColumnIndex("ID_REMINDER")));
                    r.setJudul(cursor.getString(cursor.getColumnIndex("JUDUL")));
                    r.setIsi(cursor.getString(cursor.getColumnIndex("ISI")));
                    r.setCreator_id(cursor.getString(cursor.getColumnIndex("CREATOR_ID")));
                    r.setCreator(cursor.getString(cursor.getColumnIndex("CREATOR")));
                    if(cursor.getColumnIndex("ISIMPORTANT")==1){
                        r.setImportant(true);
                    }else{
                        r.setImportant(false);
                    }
                    r.setTimestamp(cursor.getString(cursor.getColumnIndex("TIMESTAMP")));
                    r.setSchedule_time(cursor.getString(cursor.getColumnIndex("SCHEDULETIME")));
                } while (cursor.moveToNext());
            }
        }

        db.close();
        return r;
    }

    public void DropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE TBL_REMINDER");
    }

    public void ClearReminder() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM TBL_REMINDER");
    }

    public void updateRead(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE TBL_REMINDER SET ISREAD=1 WHERE  ID_REMINDER='" + id +"'");
        db.close();
    }

}

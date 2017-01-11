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
import com.fintech.ternaku.TernakPerah.Main.Dashboard.ModelDashboard;
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
        String CREATE_TBL_DASHBOARD = "CREATE TABLE TBL_DASHBOARD(ID INTEGER AUTO INCREMENT , PERIKSA INTEGER,  SUBUR INTEGER,  SAPI_DEWASA INTEGER,  TOTAL_SAPI INTEGER,  JUMLAH_HEIFERS INTEGER,  JUMLAH_DEWASA INTEGER,  JUMLAH_CALV INTEGER,  JUMLAH_TERNAKMELAHIRKAN INTEGER,  JUMLAH_TERNAKHAMIL INTEGER,  JUMLAH_TERNAKMENYUSUI INTEGER,  JUMLAH_TIDAKHAMILMENYUSUIMELAHIRKAN INTEGER,  JUMLAH_SEHAT INTEGER,  JUMLAH_BBSEMPURNA INTEGER,  JUMLAH_BBBAGUS INTEGER,  JUMLAH_BBSEDANG INTEGER,  JUMLAH_BBKURANG INTEGER,  JUMLAH_BBLAINNYA INTEGER,  JUMLAH_PAKAN REAL,  HARGA REAL,  PRODUKSI_SUSU REAL,  DATE TEXT)";
        db.execSQL(CREATE_TBL_ALARM);
        db.execSQL(CREATE_TBL_DASHBOARD);
        db.execSQL(CREATE_TBL_REMINDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "TBL_REMINDER");
        db.execSQL("DROP TABLE IF EXISTS " + "TBL_ALARM");
        db.execSQL("DROP TABLE IF EXISTS " + "TBL_DASHBOARD");

        onCreate(db);
    }

    public void AddDashboardData(ModelDashboard dashboard) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.setForeignKeyConstraintsEnabled(false);
        ContentValues values = new ContentValues();
        values.put("PERIKSA",dashboard.getPeriksa());
        values.put("SUBUR",dashboard.getSubur());
        values.put("SAPI_DEWASA",dashboard.getSapi_dewasa());
        values.put("TOTAL_SAPI",dashboard.getTotal_sapi());
        values.put("JUMLAH_HEIFERS",dashboard.getJumlah_heifers());
        values.put("JUMLAH_DEWASA",dashboard.getJumlah_dewasa());
        values.put("JUMLAH_CALV",dashboard.getJumlah_calv());
        values.put("JUMLAH_TERNAKMELAHIRKAN",dashboard.getJumlah_ternakmelahirkan());
        values.put("JUMLAH_TERNAKHAMIL",dashboard.getJumlah_ternakhamil());
        values.put("JUMLAH_TERNAKMENYUSUI",dashboard.getJumlah_ternakmenyusui());
        values.put("JUMLAH_TIDAKHAMILMENYUSUIMELAHIRKAN",dashboard.getJumlah_tidakhamilmenyusuimelahirkan());
        values.put("JUMLAH_SEHAT",dashboard.getJumlah_sehat());
        values.put("JUMLAH_BBSEMPURNA",dashboard.getJumlah_bbsempurna());
        values.put("JUMLAH_BBBAGUS",dashboard.getJumlah_bbbagus());
        values.put("JUMLAH_BBSEDANG",dashboard.getJumlah_bbsedang());
        values.put("JUMLAH_BBKURANG",dashboard.getJumlah_bbkurang());
        values.put("JUMLAH_BBLAINNYA",dashboard.getJumlah_bblainnya());
        values.put("JUMLAH_PAKAN",dashboard.getJumlah_pakan());
        values.put("HARGA",dashboard.getHarga());
        values.put("PRODUKSI_SUSU",dashboard.getProduksi_susu());
        values.put("DATE",dashboard.getDate());
        // Inserting Row
        db.insert("TBL_DASHBOARD", null, values);
        db.close(); // Closing database connection
    }

    public ModelDashboard GetDashboardData() {
        ArrayList<ModelDashboard> dashboardList = new ArrayList<ModelDashboard>();
        // Select All Query
        String selectQuery = "SELECT * FROM TBL_DASHBOARD ORDER BY DATE DESC LIMIT 1";
        Log.d("QUERY", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        ModelDashboard d = new ModelDashboard();

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    d.setPeriksa(cursor.getInt(cursor.getColumnIndex("PERIKSA")));
                    d.setSubur(cursor.getInt(cursor.getColumnIndex("SUBUR")));
                    d.setSapi_dewasa(cursor.getInt(cursor.getColumnIndex("SAPI_DEWASA")));
                    d.setTotal_sapi(cursor.getInt(cursor.getColumnIndex("TOTAL_SAPI")));
                    d.setJumlah_heifers(cursor.getInt(cursor.getColumnIndex("JUMLAH_HEIFERS")));
                    d.setJumlah_dewasa(cursor.getInt(cursor.getColumnIndex("JUMLAH_DEWASA")));
                    d.setJumlah_calv(cursor.getInt(cursor.getColumnIndex("JUMLAH_CALV")));
                    d.setJumlah_ternakmelahirkan(cursor.getInt(cursor.getColumnIndex("JUMLAH_TERNAKMELAHIRKAN")));
                    d.setJumlah_ternakhamil(cursor.getInt(cursor.getColumnIndex("JUMLAH_TERNAKHAMIL")));
                    d.setJumlah_ternakmenyusui(cursor.getInt(cursor.getColumnIndex("JUMLAH_TERNAKMENYUSUI")));
                    d.setJumlah_tidakhamilmenyusuimelahirkan(cursor.getInt(cursor.getColumnIndex("JUMLAH_TIDAKHAMILMENYUSUIMELAHIRKAN")));
                    d.setJumlah_sehat(cursor.getInt(cursor.getColumnIndex("JUMLAH_SEHAT")));
                    d.setJumlah_bbsempurna(cursor.getInt(cursor.getColumnIndex("JUMLAH_BBSEMPURNA")));
                    d.setJumlah_bbbagus(cursor.getInt(cursor.getColumnIndex("JUMLAH_BBBAGUS")));
                    d.setJumlah_bbsedang(cursor.getInt(cursor.getColumnIndex("JUMLAH_BBSEDANG")));
                    d.setJumlah_bbkurang(cursor.getInt(cursor.getColumnIndex("JUMLAH_BBKURANG")));
                    d.setJumlah_bblainnya(cursor.getInt(cursor.getColumnIndex("JUMLAH_BBLAINNYA")));
                    d.setJumlah_pakan(cursor.getFloat(cursor.getColumnIndex("JUMLAH_PAKAN")));
                    d.setHarga(cursor.getFloat(cursor.getColumnIndex("HARGA")));
                    d.setProduksi_susu(cursor.getFloat(cursor.getColumnIndex("PRODUKSI_SUSU")));
                    d.setDate(cursor.getString(cursor.getColumnIndex("DATE")));

                    Log.d("DASHBOARDDATA", cursor.getString(cursor.getColumnIndex("DATE")));

                    // Adding contact to list
                    dashboardList.add(d);
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return d;
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
        //db.execSQL("DROP TABLE TBL_REMINDER");
        db.execSQL("DROP TABLE TBL_DASHBOARD");
    }

    public void ClearReminder() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM TBL_DASHBOARD");
    }

    public void updateRead(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE TBL_REMINDER SET ISREAD=1 WHERE  ID_REMINDER='" + id +"'");
        db.close();
    }

}

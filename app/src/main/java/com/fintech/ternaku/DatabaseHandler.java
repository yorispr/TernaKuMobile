package com.fintech.ternaku;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fintech.ternaku.Main.Pengingat.ReminderModel;
import com.fintech.ternaku.Main.TambahData.Kesuburan.InjeksiHormon.ModelAddProtokolInjeksi;

import java.util.ArrayList;

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
        db.execSQL(CREATE_TBL_REMINDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "TBL_REMINDER");
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

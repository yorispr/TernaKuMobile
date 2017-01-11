package com.fintech.ternaku.TernakPerah.Alarm;

/**
 * Created by YORIS on 11/10/16.
 */

public class Alarm {
    private int id;
    private String id_alarm;
    private String jenis;
    private String created_date;
    private String alarm_date;
    private String id_sapi;


    public Alarm() {

    }

    public Alarm(int id, String id_alarm, String jenis, String created_date, String alarm_date, String id_sapi) {
       this.setId(id);
        this.setId_alarm(id_alarm);
        this.setJenis(jenis);
        this.setCreated_date(created_date);
        this.setAlarm_date(alarm_date);
        this.setId_sapi(id_sapi);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_alarm() {
        return id_alarm;
    }

    public void setId_alarm(String id_alarm) {
        this.id_alarm = id_alarm;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getAlarm_date() {
        return alarm_date;
    }

    public void setAlarm_date(String alarm_date) {
        this.alarm_date = alarm_date;
    }

    public String getId_sapi() {
        return id_sapi;
    }

    public void setId_sapi(String id_sapi) {
        this.id_sapi = id_sapi;
    }
}


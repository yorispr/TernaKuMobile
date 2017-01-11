package com.fintech.ternaku.TernakPerah.Main.Pengingat;

/**
 * Created by YORIS on 10/5/16.
 */

/**
 * Created by Pandhu on 10/3/16.
 */

public class ReminderModel {

    private String id_protocol;
    private String judul;
    private String isi;
    private boolean isImportant;
    private String creator_id;
    private String creator;
    private String timestamp;
    private String schedule_time;
    private String is_repeat;
    private int isread;

    public ReminderModel() {
    }

    public ReminderModel(String id_protocol, String judul, String isi, boolean isImportant, String creator_id, String creator, String timestamp, String is_repeat,int isread,String schedule_time) {
        this.setId_protocol(id_protocol);
        this.setJudul(judul);
        this.setIsi(isi);
        this.setImportant(isImportant);
        this.setCreator(creator);
        this.setCreator_id(creator_id);
        this.setTimestamp(timestamp);
        this.setIsread(isread);
        this.setSchedule_time(schedule_time);
        this.setIs_repeat(is_repeat);
    }



    public String getId_protocol() {
        return id_protocol;
    }

    public void setId_protocol(String id_protocol) {
        this.id_protocol = id_protocol;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }

    public String getSchedule_time() {
        return schedule_time;
    }

    public void setSchedule_time(String schedule_time) {
        this.schedule_time = schedule_time;
    }

    public String getIs_repeat() {
        return is_repeat;
    }

    public void setIs_repeat(String is_repeat) {
        this.is_repeat = is_repeat;
    }
}

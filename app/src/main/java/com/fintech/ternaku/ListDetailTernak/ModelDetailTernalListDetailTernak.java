package com.fintech.ternaku.ListDetailTernak;

/**
 * Created by Pandhu on 9/28/16.
 */

public class ModelDetailTernalListDetailTernak {
    private String id_ternak;
    private String nama_ternak;
    private String body_condition_score;
    private String id_peternakan;
    private String kesuburan;
    private String tgl_terakhir_check;
    public ModelDetailTernalListDetailTernak() {

    }

    public ModelDetailTernalListDetailTernak(String id_ternak, String nama_ternak, String body_condition_score,String id_peternakan,String kesuburan,String tgl_terakhir_check) {
        this.setId_ternak(id_ternak);
        this.setNama_ternak(nama_ternak);
        this.setBody_condition_score(body_condition_score);
        this.setId_peternakan(id_peternakan);
        this.setKesuburan(kesuburan);
        this.setTgl_terakhir_check(tgl_terakhir_check);
    }

    public void setTgl_terakhir_check(String tgl_terakhir_check) {
        this.tgl_terakhir_check = tgl_terakhir_check;
    }

    public String getTgl_terakhir_check() {
        return tgl_terakhir_check;
    }

    public void setKesuburan(String kesuburan) {
        this.kesuburan = kesuburan;
    }

    public String getKesuburan() {
        return kesuburan;
    }

    public void setId_peternakan(String id_peternakan) {
        this.id_peternakan = id_peternakan;
    }

    public String getId_peternakan() {
        return id_peternakan;
    }

    public void setId_ternak(String id_ternak) {
        this.id_ternak = id_ternak;
    }

    public void setNama_ternak(String nama_ternak) {
        this.nama_ternak = nama_ternak;
    }

    public void setBody_condition_score(String body_condition_score) {
        this.body_condition_score = body_condition_score;
    }

    public String getId_ternak() {
        return id_ternak;
    }

    public String getNama_ternak() {
        return nama_ternak;
    }

    public String getBody_condition_score() {
        return body_condition_score;
    }
}

package com.fintech.ternaku.TernakPerah.ListDetailTernak;

/**
 * Created by Pandhu on 9/28/16.
 */

public class ModelDetailTernalListDetailTernak {
    private String id_ternak;
    private String nama_ternak;
    private String body_condition_score;
    private String id_peternakan;
    private String tgl_lahir;

    private String rfid;
    private int is_dry;
    private int is_heat;
    private int is_menyusui;


    private double berat;
    private String umur;

    private double produksisusu;
    public ModelDetailTernalListDetailTernak() {

    }

    public ModelDetailTernalListDetailTernak(String id_ternak, String nama_ternak, String body_condition_score,String id_peternakan,String tgl_lahir,String rfid, int is_dry, int is_heat, int is_menyusui, double berat, String umur, double produksisusu) {
        this.setId_ternak(id_ternak);
        this.setNama_ternak(nama_ternak);
        this.setBody_condition_score(body_condition_score);
        this.setId_peternakan(id_peternakan);
        this.setTgl_lahir(tgl_lahir);
        this.setRfid(rfid);
        this.setIs_dry(is_dry);
        this.setIs_heat(is_heat);
        this.setIs_menyusui(is_menyusui);
        this.setBerat(berat);
        this.setUmur(umur);
        this.setProduksisusu(produksisusu);
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

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public void setTgl_lahir(String tgl_lahir) {
        this.tgl_lahir = tgl_lahir;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public int getIs_dry() {
        return is_dry;
    }

    public void setIs_dry(int is_dry) {
        this.is_dry = is_dry;
    }

    public int getIs_heat() {
        return is_heat;
    }

    public void setIs_heat(int is_heat) {
        this.is_heat = is_heat;
    }

    public int getIs_menyusui() {
        return is_menyusui;
    }

    public void setIs_menyusui(int is_menyusui) {
        this.is_menyusui = is_menyusui;
    }

    public double getBerat() {
        return berat;
    }

    public void setBerat(double berat) {
        this.berat = berat;
    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    public double getProduksisusu() {
        return produksisusu;
    }

    public void setProduksisusu(double produksisusu) {
        this.produksisusu = produksisusu;
    }
}

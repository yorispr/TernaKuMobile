package com.fintech.ternaku.TernakPerah.Main.Dashboard;

/**
 * Created by YORIS on 11/30/16.
 */

public class ModelDashboard {

    private int periksa;
    private int subur;
    private int sapi_dewasa;
    private int total_sapi;
    private int jumlah_heifers;
    private int jumlah_dewasa;
    private int jumlah_calv;
    private int jumlah_ternakmelahirkan;
    private int jumlah_ternakhamil;
    private int jumlah_ternakmenyusui;
    private int jumlah_tidakhamilmenyusuimelahirkan;
    private int jumlah_sehat;
    private int jumlah_bbsempurna;
    private int jumlah_bbbagus;
    private int jumlah_bbsedang;
    private int jumlah_bbkurang;
    private int jumlah_bblainnya;
    private float jumlah_pakan;
    private float harga;
    private float produksi_susu;
    private String date;

    public ModelDashboard(){

    }

    public ModelDashboard(int periksa, int subur, int sapi_dewasa, int total_sapi, int jumlah_heifers, int jumlah_dewasa, int jumlah_calv, int jumlah_ternakmelahirkan, int jumlah_ternakhamil, int jumlah_ternakmenyusui, int jumlah_tidakhamilmenyusuimelahirkan, int jumlah_sehat, int jumlah_bbsempurna, int jumlah_bbbagus, int jumlah_bbsedang, int jumlah_bbkurang, int jumlah_bblainnya, float jumlah_pakan, float harga, float produksi_susu, String date){
        this.setPeriksa(periksa);
        this.setSubur(subur);
        this.setSapi_dewasa(sapi_dewasa);
        this.setTotal_sapi(total_sapi);
        this.setJumlah_heifers(jumlah_heifers);
        this.setJumlah_dewasa(jumlah_dewasa);
        this.setJumlah_calv(jumlah_calv);
        this.setJumlah_ternakmelahirkan(jumlah_ternakmelahirkan);
        this.setJumlah_ternakhamil(jumlah_ternakhamil);
        this.setJumlah_ternakmenyusui(jumlah_ternakmenyusui);
        this.setJumlah_tidakhamilmenyusuimelahirkan(jumlah_tidakhamilmenyusuimelahirkan);
        this.setJumlah_sehat(jumlah_sehat);
        this.setJumlah_bbsempurna(jumlah_bbsempurna);
        this.setJumlah_bbbagus(jumlah_bbbagus);
        this.setJumlah_bbsedang(jumlah_bbsedang);
        this.setJumlah_bbkurang(jumlah_bbkurang);
        this.setJumlah_bblainnya(jumlah_bblainnya);
        this.setJumlah_pakan(jumlah_pakan);
        this.setHarga(harga);
        this.setProduksi_susu(produksi_susu);
        this.setDate(date);
    }

    public int getPeriksa() {
        return periksa;
    }

    public void setPeriksa(int periksa) {
        this.periksa = periksa;
    }

    public int getSubur() {
        return subur;
    }

    public void setSubur(int subur) {
        this.subur = subur;
    }

    public int getSapi_dewasa() {
        return sapi_dewasa;
    }

    public void setSapi_dewasa(int sapi_dewasa) {
        this.sapi_dewasa = sapi_dewasa;
    }

    public int getTotal_sapi() {
        return total_sapi;
    }

    public void setTotal_sapi(int total_sapi) {
        this.total_sapi = total_sapi;
    }

    public int getJumlah_heifers() {
        return jumlah_heifers;
    }

    public void setJumlah_heifers(int jumlah_heifers) {
        this.jumlah_heifers = jumlah_heifers;
    }

    public int getJumlah_dewasa() {
        return jumlah_dewasa;
    }

    public void setJumlah_dewasa(int jumlah_dewasa) {
        this.jumlah_dewasa = jumlah_dewasa;
    }

    public int getJumlah_calv() {
        return jumlah_calv;
    }

    public void setJumlah_calv(int jumlah_calv) {
        this.jumlah_calv = jumlah_calv;
    }

    public int getJumlah_ternakmelahirkan() {
        return jumlah_ternakmelahirkan;
    }

    public void setJumlah_ternakmelahirkan(int jumlah_ternakmelahirkan) {
        this.jumlah_ternakmelahirkan = jumlah_ternakmelahirkan;
    }

    public int getJumlah_ternakhamil() {
        return jumlah_ternakhamil;
    }

    public void setJumlah_ternakhamil(int jumlah_ternakhamil) {
        this.jumlah_ternakhamil = jumlah_ternakhamil;
    }

    public int getJumlah_ternakmenyusui() {
        return jumlah_ternakmenyusui;
    }

    public void setJumlah_ternakmenyusui(int jumlah_ternakmenyusui) {
        this.jumlah_ternakmenyusui = jumlah_ternakmenyusui;
    }

    public int getJumlah_tidakhamilmenyusuimelahirkan() {
        return jumlah_tidakhamilmenyusuimelahirkan;
    }

    public void setJumlah_tidakhamilmenyusuimelahirkan(int jumlah_tidakhamilmenyusuimelahirkan) {
        this.jumlah_tidakhamilmenyusuimelahirkan = jumlah_tidakhamilmenyusuimelahirkan;
    }

    public int getJumlah_sehat() {
        return jumlah_sehat;
    }

    public void setJumlah_sehat(int jumlah_sehat) {
        this.jumlah_sehat = jumlah_sehat;
    }

    public int getJumlah_bbsempurna() {
        return jumlah_bbsempurna;
    }

    public void setJumlah_bbsempurna(int jumlah_bbsempurna) {
        this.jumlah_bbsempurna = jumlah_bbsempurna;
    }

    public int getJumlah_bbbagus() {
        return jumlah_bbbagus;
    }

    public void setJumlah_bbbagus(int jumlah_bbbagus) {
        this.jumlah_bbbagus = jumlah_bbbagus;
    }

    public int getJumlah_bbsedang() {
        return jumlah_bbsedang;
    }

    public void setJumlah_bbsedang(int jumlah_bbsedang) {
        this.jumlah_bbsedang = jumlah_bbsedang;
    }

    public int getJumlah_bbkurang() {
        return jumlah_bbkurang;
    }

    public void setJumlah_bbkurang(int jumlah_bbkurang) {
        this.jumlah_bbkurang = jumlah_bbkurang;
    }

    public int getJumlah_bblainnya() {
        return jumlah_bblainnya;
    }

    public void setJumlah_bblainnya(int jumlah_bblainnya) {
        this.jumlah_bblainnya = jumlah_bblainnya;
    }

    public float getJumlah_pakan() {
        return jumlah_pakan;
    }

    public void setJumlah_pakan(float jumlah_pakan) {
        this.jumlah_pakan = jumlah_pakan;
    }

    public float getHarga() {
        return harga;
    }

    public void setHarga(float harga) {
        this.harga = harga;
    }

    public float getProduksi_susu() {
        return produksi_susu;
    }

    public void setProduksi_susu(float produksi_susu) {
        this.produksi_susu = produksi_susu;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

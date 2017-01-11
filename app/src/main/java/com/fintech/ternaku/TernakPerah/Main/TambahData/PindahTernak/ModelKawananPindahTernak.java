package com.fintech.ternaku.TernakPerah.Main.TambahData.PindahTernak;

/**
 * Created by Pandhu on 10/4/16.
 */

public class ModelKawananPindahTernak {
    private String id_kawanan;
    private String id_peternakan;
    private String nama_kawanan;
    private String umur;
    private String keterangan;

    public ModelKawananPindahTernak() {
    }

    public ModelKawananPindahTernak(String id_kawanan, String id_peternakan, String nama_kawanan, String umur, String keterangan) {
        this.setId_kawanan(id_kawanan);
        this.setId_peternakan(id_peternakan);
        this.setNama_kawanan(nama_kawanan);
        this.setUmur(umur);
        this.setKeterangan(keterangan);
    }

    public String getId_kawanan() {
        return id_kawanan;
    }

    public void setId_kawanan(String id_kawanan) {
        this.id_kawanan = id_kawanan;
    }

    public String getId_peternakan() {
        return id_peternakan;
    }

    public void setId_peternakan(String id_peternakan) {
        this.id_peternakan = id_peternakan;
    }

    public String getNama_kawanan() {
        return nama_kawanan;
    }

    public void setNama_kawanan(String nama_kawanan) {
        this.nama_kawanan = nama_kawanan;
    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}

package com.fintech.ternaku.TernakPerah.Main.TambahData.PindahTernak;

/**
 * Created by Pandhu on 10/4/16.
 */

public class ModelKandangPindahTernak {
    private String id_kandang;
    private String id_peternakan;
    private String nama_kandang;
    private String lokasi_kandang;
    private int kapasitas_kandang;
    private String status_aktif;

    public ModelKandangPindahTernak(){

    }
    public ModelKandangPindahTernak(String id_kandang, String id_peternakan, String nama_kandang, String lokasi_kandang, int kapasitas_kandang, String status_aktif){
        this.setId_kandang(id_kandang);
        this.setId_peternakan(id_peternakan);
        this.setNama_kandang(nama_kandang);
        this.setLokasi_kandang(lokasi_kandang);
        this.setKapasitas_kandang(kapasitas_kandang);
        this.setStatus_aktif(status_aktif);
    }

    public String getId_kandang() {
        return id_kandang;
    }

    public void setId_kandang(String id_kandang) {
        this.id_kandang = id_kandang;
    }

    public String getId_peternakan() {
        return id_peternakan;
    }

    public void setId_peternakan(String id_peternakan) {
        this.id_peternakan = id_peternakan;
    }

    public String getNama_kandang() {
        return nama_kandang;
    }

    public void setNama_kandang(String nama_kandang) {
        this.nama_kandang = nama_kandang;
    }

    public String getLokasi_kandang() {
        return lokasi_kandang;
    }

    public void setLokasi_kandang(String lokasi_kandang) {
        this.lokasi_kandang = lokasi_kandang;
    }

    public int getKapasitas_kandang() {
        return kapasitas_kandang;
    }

    public void setKapasitas_kandang(int kapasitas_kandang) {
        this.kapasitas_kandang = kapasitas_kandang;
    }

    public String getStatus_aktif() {
        return status_aktif;
    }

    public void setStatus_aktif(String status_aktif) {
        this.status_aktif = status_aktif;
    }
}


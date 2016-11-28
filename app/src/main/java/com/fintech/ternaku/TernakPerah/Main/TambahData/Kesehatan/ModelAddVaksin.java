package com.fintech.ternaku.TernakPerah.Main.TambahData.Kesehatan;

/**
 * Created by Pandhu on 10/3/16.
 */

public class ModelAddVaksin {
    private String id;
    private String nama;
    private String ukuran;
    private String satuan;
    private String kegunaan;
    private String metode_simpan;
    private String produsen;

    public ModelAddVaksin(){

    }

    public ModelAddVaksin(String id, String nama, String ukuran, String satuan, String kegunaan, String metode_simpan, String produsen){
        this.setId(id);
        this.setNama(nama);
        this.setUkuran(ukuran);
        this.setSatuan(satuan);
        this.setKegunaan(kegunaan);
        this.setMetode_simpan(metode_simpan);
        this.setProdusen(produsen);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUkuran() {
        return ukuran;
    }

    public void setUkuran(String ukuran) {
        this.ukuran = ukuran;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getKegunaan() {
        return kegunaan;
    }

    public void setKegunaan(String kegunaan) {
        this.kegunaan = kegunaan;
    }

    public String getMetode_simpan() {
        return metode_simpan;
    }

    public void setMetode_simpan(String metode_simpan) {
        this.metode_simpan = metode_simpan;
    }

    public String getProdusen() {
        return produsen;
    }

    public void setProdusen(String produsen) {
        this.produsen = produsen;
    }
}


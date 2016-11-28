package com.fintech.ternaku.TernakPerah.Main.NavBar.BatasProduksiSusu;

/**
 * Created by Pandhu on 9/29/16.
 */

public class ModelGetKawananAddBatasProduksi {


    private String id_kawanan;
    private String nama_kawanan;

    public ModelGetKawananAddBatasProduksi() {

    }

    public ModelGetKawananAddBatasProduksi(String id_kawanan, String nama_kawanan) {
        this.setId_kawanan(id_kawanan);
        this.setNama_kawanan(nama_kawanan);
    }

    public void setId_kawanan(String id_kawanan) {
        this.id_kawanan = id_kawanan;
    }

    public String getId_kawanan() {
        return id_kawanan;
    }

    public void setNama_kawanan(String nama_kawanan) {
        this.nama_kawanan = nama_kawanan;
    }

    public String getNama_kawanan() {
        return nama_kawanan;
    }
}

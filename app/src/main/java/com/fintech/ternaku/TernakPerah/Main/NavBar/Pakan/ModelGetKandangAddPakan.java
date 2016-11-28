package com.fintech.ternaku.TernakPerah.Main.NavBar.Pakan;

/**
 * Created by Pandhu on 9/29/16.
 */

public class ModelGetKandangAddPakan {
    private String id_kandang;
    private String nama_kandang;

    public ModelGetKandangAddPakan() {

    }

    public ModelGetKandangAddPakan(String id_kandang, String nama_kandang) {
        this.setId_kandang(id_kandang);
        this.setNama_kandang(nama_kandang);
    }

    public String getId_kandang() {
        return id_kandang;
    }

    public void setId_kandang(String id_kandang) {
        this.id_kandang = id_kandang;
    }

    public String getNama_kandang() {
        return nama_kandang;
    }

    public void setNama_kandang(String nama_kandang) {
        this.nama_kandang = nama_kandang;
    }
}

package com.fintech.ternaku.TernakPerah.Main.NavBar.Pakan;

/**
 * Created by Pandhu on 9/29/16.
 */

public class ModelGetPakanAddPakan {

    private String id_pakan;
    private String nama_pakan;

    public ModelGetPakanAddPakan() {

    }

    public ModelGetPakanAddPakan(String id_pakan, String nama_pakan) {
        this.setId_pakan(id_pakan);
        this.setNama_pakan(nama_pakan);
    }

    public String getId_pakan() {
        return id_pakan;
    }

    public void setId_pakan(String id_pakan) {
        this.id_pakan = id_pakan;
    }

    public String getNama_pakan() {
        return nama_pakan;
    }

    public void setNama_pakan(String nama_pakan) {
        this.nama_pakan = nama_pakan;
    }
}

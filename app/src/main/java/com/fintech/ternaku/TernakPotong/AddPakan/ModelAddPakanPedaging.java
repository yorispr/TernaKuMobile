package com.fintech.ternaku.TernakPotong.AddPakan;

/**
 * Created by Pandhu on 12/13/16.
 */

public class ModelAddPakanPedaging {
    private String id_ternak;
    private String sesi_makan;
    private String tgl_makan;
    private String jenis_konsentrat;

    public ModelAddPakanPedaging(){

    }

    public ModelAddPakanPedaging(String id_ternak,String sesi_makan,String tgl_makan,String jenis_konsentrat){
        setId_ternak(this.id_ternak);
        setSesi_makan(this.sesi_makan);
        setJenis_konsentrat(this.jenis_konsentrat);
        setTgl_makan(this.tgl_makan);
    }

    public void setTgl_makan(String tgl_makan) {
        this.tgl_makan = tgl_makan;
    }

    public String getTgl_makan() {
        return tgl_makan;
    }

    public void setJenis_konsentrat(String jenis_konsentrat) {
        this.jenis_konsentrat = jenis_konsentrat;
    }

    public String getJenis_konsentrat() {
        return jenis_konsentrat;
    }

    public void setSesi_makan(String sesi_makan) {
        this.sesi_makan = sesi_makan;
    }

    public String getSesi_makan() {
        return sesi_makan;
    }

    public void setId_ternak(String id_ternak) {
        this.id_ternak = id_ternak;
    }

    public String getId_ternak() {
        return id_ternak;
    }
}

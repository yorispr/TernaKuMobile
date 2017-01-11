package com.fintech.ternaku.TernakPotong.ADG;

/**
 * Created by Pandhu on 11/29/16.
 */

public class ModelAdg {
    private String id_ternak;
    private String tgl_timbang_terakhir;
    private String berat_terakhir;
    private String tgl_timbang_sebelum;
    private String berat_sebelum;
    private String value_adg;

    public ModelAdg(){

    }

    public ModelAdg(String id_ternak,String tgl_timbang_terakhir,String berat_terakhir,String tgl_timbang_sebelum,String berat_sebelum,String value_adg){
        this.setBerat_sebelum(berat_sebelum);
        this.setBerat_terakhir(berat_terakhir);
        this.setTgl_timbang_sebelum(tgl_timbang_sebelum);
        this.setTgl_timbang_terakhir(tgl_timbang_terakhir);
        this.setId_ternak(id_ternak);
        this.setValue_adg(value_adg);
    }

    public String getValue_adg() {
        return value_adg;
    }

    public void setValue_adg(String value_adg) {
        this.value_adg = value_adg;
    }

    public String getTgl_timbang_terakhir() {
        return tgl_timbang_terakhir;
    }

    public void setTgl_timbang_terakhir(String tgl_timbang_terakhir) {
        this.tgl_timbang_terakhir = tgl_timbang_terakhir;
    }

    public String getTgl_timbang_sebelum() {
        return tgl_timbang_sebelum;
    }

    public void setTgl_timbang_sebelum(String tgl_timbang_sebelum) {
        this.tgl_timbang_sebelum = tgl_timbang_sebelum;
    }

    public String getId_ternak() {
        return id_ternak;
    }

    public void setId_ternak(String id_ternak) {
        this.id_ternak = id_ternak;
    }

    public String getBerat_terakhir() {
        return berat_terakhir;
    }

    public void setBerat_terakhir(String berat_terakhir) {
        this.berat_terakhir = berat_terakhir;
    }

    public String getBerat_sebelum() {
        return berat_sebelum;
    }

    public void setBerat_sebelum(String berat_sebelum) {
        this.berat_sebelum = berat_sebelum;
    }
}

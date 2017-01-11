package com.fintech.ternaku.TernakPotong.AddPakan;

/**
 * Created by Pandhu on 12/15/16.
 */

public class ModelAddPakanPedagingListKomposisi {
    private String id_komposisi;
    private String umur_bawah;
    private String umur_atas;
    private String jenis_sapi;
    private String kg_konsentrat;
    private String kg_hijauan;
    private String total_harga;

    public ModelAddPakanPedagingListKomposisi(){

    }

    public ModelAddPakanPedagingListKomposisi(String id_komposisi,String umur_bawah,String umur_atas,String jenis_sapi,
                                              String kg_konsentrat,String kg_hijauan,String total_harga){
        setId_komposisi(this.id_komposisi);
        setJenis_sapi(this.jenis_sapi);
        setKg_hijauan(this.kg_hijauan);
        setKg_konsentrat(this.kg_konsentrat);
        setUmur_bawah(this.umur_bawah);
        setUmur_atas(this.umur_atas);
        setTotal_harga(this.total_harga);
    }

    public void setUmur_bawah(String umur_bawah) {
        this.umur_bawah = umur_bawah;
    }

    public String getUmur_bawah() {
        return umur_bawah;
    }

    public void setUmur_atas(String umur_atas) {
        this.umur_atas = umur_atas;
    }

    public String getUmur_atas() {
        return umur_atas;
    }

    public void setTotal_harga(String total_harga) {
        this.total_harga = total_harga;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public void setKg_konsentrat(String kg_konsentrat) {
        this.kg_konsentrat = kg_konsentrat;
    }

    public String getKg_konsentrat() {
        return kg_konsentrat;
    }

    public void setKg_hijauan(String kg_hijauan) {
        this.kg_hijauan = kg_hijauan;
    }

    public String getKg_hijauan() {
        return kg_hijauan;
    }

    public void setJenis_sapi(String jenis_sapi) {
        this.jenis_sapi = jenis_sapi;
    }

    public String getJenis_sapi() {
        return jenis_sapi;
    }

    public void setId_komposisi(String id_komposisi) {
        this.id_komposisi = id_komposisi;
    }

    public String getId_komposisi() {
        return id_komposisi;
    }
}

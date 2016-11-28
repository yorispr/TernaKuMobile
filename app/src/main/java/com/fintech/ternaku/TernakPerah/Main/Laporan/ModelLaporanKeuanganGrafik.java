package com.fintech.ternaku.TernakPerah.Main.Laporan;

import com.github.mikephil.charting.data.LineData;

/**
 * Created by Pandhu on 11/16/16.
 */

public class ModelLaporanKeuanganGrafik {
    String Judul;
    LineData Grafik;
    String Bulan;

    public ModelLaporanKeuanganGrafik(){

    }

    public ModelLaporanKeuanganGrafik(String Judul, LineData Grafik, String Bulan){
        this.setGrafik(Grafik);
        this.setJudul(Judul);
        this.setBulan(Bulan);
    }

    public void setBulan(String bulan) {
        Bulan = bulan;
    }

    public String getBulan() {
        return Bulan;
    }

    public String getJudul() {
        return Judul;
    }

    public void setJudul(String judul) {
        Judul = judul;
    }

    public LineData getGrafik() {
        return Grafik;
    }

    public void setGrafik(LineData grafik) {
        Grafik = grafik;
    }
}

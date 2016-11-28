package com.fintech.ternaku.TernakPerah.DetailTernak.Event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pandhu on 9/27/16.
 */

public class ModelDetailTernakEvent implements Parcelable{

    //Judul Expander-------------------------------------
    public String title;
    public String tgl;
    public String key_event;
    public boolean isExpanded;

    //Pengelompokan--------------------------------------
    public String pengelompokan_kandang;
    public String pengelompokan_kawanan;

    //Pemerahan------------------------------------------
    public String pemerahan_sesi;
    public String pemerahan_durasi;
    public String pemerahan_kapasitas;

    //Pemeriksaan Kesehatan------------------------------
    public String kesehatan_diagnosis;
    public String kesehatan_perawatan;
    public String kesehatan_berat;
    public String kesehatan_statusfisik;
    public String kesehatan_statusstress;
    public String kesehatan_suhu;

    //Kuku-----------------------------------------------
    public String kuku_diagnosis;
    public String kuku_perawatan;
    public String kuku_statusfisik;

    //Mastitis-------------------------------------------
    public String mastitis_diagnosis;
    public String mastitis_perawatan;
    public String mastitis_statusfisik;

    //Lameness-------------------------------------------
    public String lameness_diagnosis;
    public String lameness_perawatan;
    public String lameness_statusfisik;

    //Pemeriksaan Reproduksi-----------------------------
    public String reproduksi_kondisi;

    //Pemeriksaa Heat------------------------------------
    public String heat_tglmulai;
    public String heat_tglselesai;

    //Vaksinasi------------------------------------------
    public String vaksinasi_nama;
    public String vaksinasi_satuan;
    public String vaksinasi_dosis;
    public String vaksinasi_repetisi;

    //Inseminasi-----------------------------------------
    public String inseminasi_metode;
    public String inseminasi_status;
    public String inseminasi_tgl_perkiraan_melahirkan;

    //Melahirkan-----------------------------------------
    public String melahirkan_kondisi;
    public String melahirkan_jumlahanak;

    //Aborsi---------------------------------------------
    public String aborsi_penyebab;


    public ModelDetailTernakEvent(){}

    public ModelDetailTernakEvent(Parcel in){
        title = in.readString();
        tgl = in.readString();
        key_event = in.readString();
        isExpanded = in.readInt() == 1;

        //Pengelompokan--------------------------------------
        pengelompokan_kandang = in.readString();
        pengelompokan_kawanan = in.readString();

        //Pemerahan------------------------------------------
        pemerahan_sesi = in.readString();
        pemerahan_durasi = in.readString();
        pemerahan_kapasitas = in.readString();

        //Pemeriksaan Kesehatan------------------------------
        kesehatan_diagnosis = in.readString();
        kesehatan_perawatan = in.readString();
        kesehatan_berat = in.readString();
        kesehatan_statusfisik = in.readString();
        kesehatan_statusstress = in.readString();
        kesehatan_suhu = in.readString();

        //Kuku-----------------------------------------------
        kuku_diagnosis = in.readString();
        kuku_perawatan = in.readString();
        kuku_statusfisik = in.readString();

        //Mastitis-------------------------------------------
        mastitis_diagnosis = in.readString();
        mastitis_perawatan = in.readString();
        mastitis_statusfisik = in.readString();

        //Lameness-------------------------------------------
        lameness_diagnosis = in.readString();
        lameness_perawatan = in.readString();
        lameness_statusfisik = in.readString();

        //Pemeriksaan Reproduksi-----------------------------
        reproduksi_kondisi = in.readString();

        //Pemeriksaa Heat------------------------------------
        heat_tglmulai = in.readString();
        heat_tglselesai = in.readString();

        //Vaksinasi------------------------------------------
        vaksinasi_nama = in.readString();
        vaksinasi_satuan = in.readString();
        vaksinasi_dosis = in.readString();
        vaksinasi_repetisi = in.readString();

        //Inseminasi-----------------------------------------
        inseminasi_metode = in.readString();
        inseminasi_status = in.readString();
        inseminasi_tgl_perkiraan_melahirkan = in.readString();

        //Melahirkan-----------------------------------------
        melahirkan_kondisi = in.readString();
        melahirkan_jumlahanak = in.readString();

        //Aborsi---------------------------------------------
        aborsi_penyebab = in.readString();
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(title);
        dest.writeString(tgl);
        dest.writeString(key_event);
        dest.writeInt(isExpanded ? 1 : 0);

        //Pengelompokan--------------------------------------
        dest.writeString(pengelompokan_kandang);
        dest.writeString(pengelompokan_kawanan);

        //Pemerahan------------------------------------------
        dest.writeString(pemerahan_sesi);
        dest.writeString(pemerahan_durasi);
        dest.writeString(pemerahan_kapasitas);

        //Pemeriksaan Kesehatan------------------------------
        dest.writeString(kesehatan_diagnosis);
        dest.writeString(kesehatan_perawatan);
        dest.writeString(kesehatan_berat);
        dest.writeString(kesehatan_statusfisik);
        dest.writeString(kesehatan_statusstress);
        dest.writeString(kesehatan_suhu);

        //Kuku-----------------------------------------------
        dest.writeString(kuku_diagnosis);
        dest.writeString(kuku_perawatan);
        dest.writeString(kuku_statusfisik);

        //Mastitis-------------------------------------------
        dest.writeString(mastitis_diagnosis);
        dest.writeString(mastitis_perawatan);
        dest.writeString(mastitis_statusfisik);

        //Lameness-------------------------------------------
        dest.writeString(lameness_diagnosis);
        dest.writeString(lameness_perawatan);
        dest.writeString(lameness_statusfisik);

        //Pemeriksaan Reproduksi-----------------------------
        dest.writeString(reproduksi_kondisi);

        //Pemeriksaa Heat------------------------------------
        dest.writeString(heat_tglmulai);
        dest.writeString(heat_tglselesai);

        //Vaksinasi------------------------------------------
        dest.writeString(vaksinasi_nama);
        dest.writeString(vaksinasi_satuan);
        dest.writeString(vaksinasi_dosis);
        dest.writeString(vaksinasi_repetisi);

        //Inseminasi-----------------------------------------
        dest.writeString(inseminasi_metode);
        dest.writeString(inseminasi_status);
        dest.writeString(inseminasi_tgl_perkiraan_melahirkan);

        //Melahirkan-----------------------------------------
        dest.writeString(melahirkan_kondisi);
        dest.writeString(melahirkan_jumlahanak);

        //Aborsi---------------------------------------------
        dest.writeString(aborsi_penyebab);


    }

    public static final Creator<ModelDetailTernakEvent> CREATOR = new Creator<ModelDetailTernakEvent>(){
        @Override
        public ModelDetailTernakEvent createFromParcel(Parcel source){
            return new ModelDetailTernakEvent(source);
        }

        @Override
        public ModelDetailTernakEvent[] newArray(int size) {
            return new ModelDetailTernakEvent[size];
        }

    };
}
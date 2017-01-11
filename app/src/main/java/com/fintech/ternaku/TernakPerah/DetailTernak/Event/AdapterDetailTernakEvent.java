package com.fintech.ternaku.TernakPerah.DetailTernak.Event;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fintech.ternaku.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Pandhu on 9/27/16.
 */

public class AdapterDetailTernakEvent extends BaseAdapter {
    List<ModelDetailTernakEvent> items;
    Context context;
    String Key_Events;



    public class Row{
        TextView Judul;
        TextView Tgl;
        LinearLayout Wrapper,linearLayout_detailternak_activity_event_judul,
                linearLayout_detailternal_activity_pengelompokkan,
                linearLayout_detailternal_activity_pemerahan,
                linearLayout_detailternal_activity_pemeriksaankesehatan,
                linearLayout_detailternal_activity_pemeriksaankuku,
                linearLayout_detailternal_activity_pemeriksaanmastitis,
                linearLayout_detailternal_activity_pemeriksaanlameness,
                linearLayout_detailternal_activity_pemeriksaanreproduksi,
                linearLayout_detailternal_activity_pemeriksaanheat,
                linearLayout_detailternal_activity_vaksinasi,
                linearLayout_detailternal_activity_inseminasi,
                linearLayout_detailternal_activity_melahirkan,
                linearLayout_detailternal_activity_aborsi;
        ImageView Arrow,ic_detailternak_activity_event_expander;

        //Pengelompokan--------------------------------------
        TextView txt_detailternak_activity_event_kandang;
        TextView txt_detailternak_activity_event_kawanan;

        //Pemerahan------------------------------------------
        TextView txt_detailternak_activity_event_sesiperah;
        TextView txt_detailternak_activity_event_durasi;
        TextView txt_detailternak_activity_event_kapasitas;

        //Pemeriksaan Kesehatan------------------------------
        TextView txt_detailternak_activity_event_beratbadan;
        TextView txt_detailternak_activity_event_suhubadan;
        TextView txt_detailternak_activity_event_statusfisikkesehatan;
        TextView txt_detailternak_activity_event_statusstress;
        TextView txt_detailternak_activity_event_diagnosiskesehatan;
        TextView txt_detailternak_activity_event_perawatankesehatan;

        //Kuku-----------------------------------------------
        TextView txt_detailternak_activity_event_statusfisikkuku;
        TextView txt_detailternak_activity_event_diagnosiskuku;
        TextView txt_detailternak_activity_event_perawatankuku;

        //Mastitis-------------------------------------------
        TextView txt_detailternak_activity_event_statusfisikmastitis;
        TextView txt_detailternak_activity_event_diagnosismastitis;
        TextView txt_detailternak_activity_event_perawatanmastitis;

        //Lameness-------------------------------------------
        TextView txt_detailternak_activity_event_statusfisiklameness;
        TextView txt_detailternak_activity_event_diagnosislameness;
        TextView txt_detailternak_activity_event_perawatanlameness;

        //Pemeriksaan Reproduksi-----------------------------
        TextView txt_detailternak_activity_event_statusfisikreproduksi;

        //Pemeriksaa Heat------------------------------------
        TextView txt_detailternak_activity_event_tglmulai;
        TextView txt_detailternak_activity_event_tglselesai;

        //Vaksinasi------------------------------------------
        TextView txt_detailternak_activity_event_namavaksin;
        TextView txt_detailternak_activity_event_repetisi;
        TextView txt_detailternak_activity_event_dosis;
        TextView txt_detailternak_activity_event_satuan;

        //Inseminasi-----------------------------------------
        TextView txt_detailternak_activity_event_metodeinseminasi;
        TextView txt_detailternak_activity_event_statuskeberhasilan;
        TextView txt_detailternak_activity_event_tglperkiraanlahir;

        //Melahirkan-----------------------------------------
        TextView txt_detailternak_activity_event_kondisimelahirkan;
        TextView txt_detailternak_activity_event_jumlahanakdilahirkan;

        //Aborsi---------------------------------------------
        TextView txt_detailternak_activity_event_penyebababorsi;
    }

    public AdapterDetailTernakEvent(Context context, List<ModelDetailTernakEvent> items){
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount(){
        return items.size();
    }

    @Override
    public Object getItem(int position){
        return items.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Row row;

        if(convertView == null){
            row = new Row();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_event_list, parent, false);
            row.Wrapper = (LinearLayout) convertView.findViewById(R.id.wrapper);
            row.linearLayout_detailternak_activity_event_judul = (LinearLayout)convertView.findViewById(R.id.linearLayout_detailternak_activity_event_judul);

            //Set Linear Data------------------------------------
            row.linearLayout_detailternal_activity_pengelompokkan = (LinearLayout)convertView.findViewById(R.id.linearLayout_detailternal_activity_pengelompokkan);
            row.linearLayout_detailternal_activity_pemerahan = (LinearLayout)convertView.findViewById(R.id.linearLayout_detailternal_activity_pemerahan);
            row.linearLayout_detailternal_activity_pemeriksaankesehatan = (LinearLayout)convertView.findViewById(R.id.linearLayout_detailternal_activity_pemeriksaankesehatan);
            row.linearLayout_detailternal_activity_pemeriksaankuku = (LinearLayout)convertView.findViewById(R.id.linearLayout_detailternal_activity_pemeriksaankuku);
            row.linearLayout_detailternal_activity_pemeriksaanmastitis = (LinearLayout)convertView.findViewById(R.id.linearLayout_detailternal_activity_pemeriksaanmastitis);
            row.linearLayout_detailternal_activity_pemeriksaanlameness = (LinearLayout)convertView.findViewById(R.id.linearLayout_detailternal_activity_pemeriksaanlameness);
            row.linearLayout_detailternal_activity_pemeriksaanreproduksi  = (LinearLayout)convertView.findViewById(R.id.linearLayout_detailternal_activity_pemeriksaanreproduksi);
            row.linearLayout_detailternal_activity_pemeriksaanheat = (LinearLayout)convertView.findViewById(R.id.linearLayout_detailternal_activity_pemeriksaanheat);
            row.linearLayout_detailternal_activity_vaksinasi = (LinearLayout)convertView.findViewById(R.id.linearLayout_detailternal_activity_vaksinasi);
            row.linearLayout_detailternal_activity_inseminasi  = (LinearLayout)convertView.findViewById(R.id.linearLayout_detailternal_activity_inseminasi);
            row.linearLayout_detailternal_activity_melahirkan = (LinearLayout)convertView.findViewById(R.id.linearLayout_detailternal_activity_melahirkan);
            row.linearLayout_detailternal_activity_aborsi = (LinearLayout)convertView.findViewById(R.id.linearLayout_detailternal_activity_aborsi);

            //Judul Expander-------------------------------------
            row.Judul = (TextView) convertView.findViewById(R.id.judul);
            row.Tgl = (TextView) convertView.findViewById(R.id.tanggal);
            row.Arrow = (ImageView) convertView.findViewById(R.id.arrow);
            row.ic_detailternak_activity_event_expander = (ImageView) convertView.findViewById(R.id.ic_detailternak_activity_event_expander);

            //Pengelompokan--------------------------------------
            row.txt_detailternak_activity_event_kandang = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_kandang);
            row.txt_detailternak_activity_event_kawanan = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_kawanan);

            //Pemerahan------------------------------------------
            row.txt_detailternak_activity_event_sesiperah = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_sesiperah);
            row.txt_detailternak_activity_event_durasi = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_durasi);
            row.txt_detailternak_activity_event_kapasitas = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_kapasitas);

            //Pemeriksaan Kesehatan------------------------------
            row.txt_detailternak_activity_event_beratbadan = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_beratbadan);
            row.txt_detailternak_activity_event_suhubadan = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_suhubadan);
            row.txt_detailternak_activity_event_statusfisikkesehatan = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_statusfisikkesehatan);
            row.txt_detailternak_activity_event_statusstress = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_statusstress);
            row.txt_detailternak_activity_event_diagnosiskesehatan = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_diagnosiskesehatan);
            row.txt_detailternak_activity_event_perawatankesehatan = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_perawatankesehatan);

            //Kuku-----------------------------------------------
            row.txt_detailternak_activity_event_statusfisikkuku = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_statusfisikkuku);
            row.txt_detailternak_activity_event_diagnosiskuku = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_diagnosiskuku);
            row.txt_detailternak_activity_event_perawatankuku = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_perawatankuku);

            //Mastitis-------------------------------------------
            row.txt_detailternak_activity_event_statusfisikmastitis = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_statusfisikmastitis);
            row.txt_detailternak_activity_event_diagnosismastitis = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_diagnosismastitis);
            row.txt_detailternak_activity_event_perawatanmastitis = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_perawatanmastitis);

            //Lameness-------------------------------------------
            row.txt_detailternak_activity_event_statusfisiklameness = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_statusfisiklameness);
            row.txt_detailternak_activity_event_diagnosislameness = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_diagnosislameness);
            row.txt_detailternak_activity_event_perawatanlameness = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_perawatanlameness);

            //Pemeriksaan Reproduksi-----------------------------
            row.txt_detailternak_activity_event_statusfisikreproduksi = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_statusfisikreproduksi);

            //Pemeriksaa Heat------------------------------------
            row.txt_detailternak_activity_event_tglmulai = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_tglmulai);
            row.txt_detailternak_activity_event_tglselesai = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_tglselesai);

            //Vaksinasi------------------------------------------
            row.txt_detailternak_activity_event_namavaksin = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_namavaksin);
            row.txt_detailternak_activity_event_repetisi = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_repetisi);
            row.txt_detailternak_activity_event_dosis = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_dosis);
            row.txt_detailternak_activity_event_satuan = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_satuan);

            //Inseminasi-----------------------------------------
            row.txt_detailternak_activity_event_metodeinseminasi = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_metodeinseminasi);
            row.txt_detailternak_activity_event_statuskeberhasilan = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_statuskeberhasilan);
            row.txt_detailternak_activity_event_tglperkiraanlahir = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_tglperkiraanlahir);

            //Melahirkan-----------------------------------------
            row.txt_detailternak_activity_event_kondisimelahirkan = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_kondisimelahirkan);
            row.txt_detailternak_activity_event_jumlahanakdilahirkan = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_jumlahanakdilahirkan);

            //Aborsi---------------------------------------------
            row.txt_detailternak_activity_event_penyebababorsi = (TextView) convertView.findViewById(R.id.txt_detailternak_activity_event_penyebababorsi);

            convertView.setTag(row);
        } else {
            row = (Row) convertView.getTag();
        }

        //Memperbaharui tampilan--------------------------------
        ModelDetailTernakEvent item = items.get(position);

        Log.d("TesHeat","MASUK");
        if(item.isExpanded){
            row.Wrapper.setVisibility(View.VISIBLE);
            row.Arrow.setRotation(180f);
        }else {
            row.Wrapper.setVisibility(View.GONE);
            row.Arrow.setRotation(0f);
        }

        if(item.key_event.equalsIgnoreCase("1")){
            //Set Layout Aktif-----------------------------------------------------------------
            row.linearLayout_detailternal_activity_pengelompokkan.setVisibility(View.VISIBLE);
            row.linearLayout_detailternal_activity_pemerahan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankesehatan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankuku.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanmastitis.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanlameness.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanreproduksi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanheat.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_vaksinasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_inseminasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_melahirkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_aborsi.setVisibility(View.GONE);

            //Set Background dan Drawable------------------------------------------------------
            row.ic_detailternak_activity_event_expander.setImageResource(R.drawable.ic_pengelompokkan_white);
            row.linearLayout_detailternak_activity_event_judul.setBackgroundColor(Color.parseColor("#1E88E5"));

        }else if(item.key_event.equalsIgnoreCase("2")){
            //Set Layout Aktif-----------------------------------------------------------------
            row.linearLayout_detailternal_activity_pengelompokkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemerahan.setVisibility(View.VISIBLE);
            row.linearLayout_detailternal_activity_pemeriksaankesehatan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankuku.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanmastitis.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanlameness.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanreproduksi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanheat.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_vaksinasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_inseminasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_melahirkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_aborsi.setVisibility(View.GONE);

            //Set Background dan Drawable------------------------------------------------------
            row.ic_detailternak_activity_event_expander.setImageResource(R.drawable.ic_pemerahan_white);
            row.linearLayout_detailternak_activity_event_judul.setBackgroundColor(Color.parseColor("#29B6F6"));
        }else if(item.key_event.equalsIgnoreCase("3")){
            //Set Layout Aktif-----------------------------------------------------------------
            row.linearLayout_detailternal_activity_pengelompokkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemerahan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankesehatan.setVisibility(View.VISIBLE);
            row.linearLayout_detailternal_activity_pemeriksaankuku.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanmastitis.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanlameness.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanreproduksi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanheat.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_vaksinasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_inseminasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_melahirkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_aborsi.setVisibility(View.GONE);

            //Set Background dan Drawable------------------------------------------------------
            row.ic_detailternak_activity_event_expander.setImageResource(R.drawable.ic_action_heart_white);
            row.linearLayout_detailternak_activity_event_judul.setBackgroundColor(Color.parseColor("#009688"));
        }else if(item.key_event.equalsIgnoreCase("4")){
            //Set Layout Aktif-----------------------------------------------------------------
            row.linearLayout_detailternal_activity_pengelompokkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemerahan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankesehatan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankuku.setVisibility(View.VISIBLE);
            row.linearLayout_detailternal_activity_pemeriksaanmastitis.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanlameness.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanreproduksi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanheat.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_vaksinasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_inseminasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_melahirkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_aborsi.setVisibility(View.GONE);

            //Set Background dan Drawable------------------------------------------------------
            row.ic_detailternak_activity_event_expander.setImageResource(R.drawable.ic_action_heart_white);
            row.linearLayout_detailternak_activity_event_judul.setBackgroundColor(Color.parseColor("#66BB6A"));
        }else if(item.key_event.equalsIgnoreCase("5")){
            //Set Layout Aktif-----------------------------------------------------------------
            row.linearLayout_detailternal_activity_pengelompokkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemerahan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankesehatan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankuku.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanmastitis.setVisibility(View.VISIBLE);
            row.linearLayout_detailternal_activity_pemeriksaanlameness.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanreproduksi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanheat.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_vaksinasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_inseminasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_melahirkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_aborsi.setVisibility(View.GONE);

            //Set Background dan Drawable------------------------------------------------------
            row.ic_detailternak_activity_event_expander.setImageResource(R.drawable.ic_action_heart_white);
            row.linearLayout_detailternak_activity_event_judul.setBackgroundColor(Color.parseColor("#9CCC65"));
        }else if(item.key_event.equalsIgnoreCase("6")){
            //Set Layout Aktif-----------------------------------------------------------------
            row.linearLayout_detailternal_activity_pengelompokkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemerahan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankesehatan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankuku.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanmastitis.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanlameness.setVisibility(View.VISIBLE);
            row.linearLayout_detailternal_activity_pemeriksaanreproduksi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanheat.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_vaksinasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_inseminasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_melahirkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_aborsi.setVisibility(View.GONE);

            //Set Background dan Drawable------------------------------------------------------
            row.ic_detailternak_activity_event_expander.setImageResource(R.drawable.ic_action_heart_white);
            row.linearLayout_detailternak_activity_event_judul.setBackgroundColor(Color.parseColor("#CDDC39"));
        }else if(item.key_event.equalsIgnoreCase("7")){
            //Set Layout Aktif-----------------------------------------------------------------
            row.linearLayout_detailternal_activity_pengelompokkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemerahan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankesehatan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankuku.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanmastitis.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanlameness.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanreproduksi.setVisibility(View.VISIBLE);
            row.linearLayout_detailternal_activity_pemeriksaanheat.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_vaksinasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_inseminasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_melahirkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_aborsi.setVisibility(View.GONE);

            //Set Background dan Drawable------------------------------------------------------
            row.ic_detailternak_activity_event_expander.setImageResource(R.drawable.ic_reproduksi_white);
            row.linearLayout_detailternak_activity_event_judul.setBackgroundColor(Color.parseColor("#FF8A65"));
        }else if(item.key_event.equalsIgnoreCase("8")){
            //Set Layout Aktif-----------------------------------------------------------------
            row.linearLayout_detailternal_activity_pengelompokkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemerahan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankesehatan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankuku.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanmastitis.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanlameness.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanreproduksi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanheat.setVisibility(View.VISIBLE);
            row.linearLayout_detailternal_activity_vaksinasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_inseminasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_melahirkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_aborsi.setVisibility(View.GONE);

            //Set Background dan Drawable------------------------------------------------------
            row.ic_detailternak_activity_event_expander.setImageResource(R.drawable.ic_reproduksi_white);
            row.linearLayout_detailternak_activity_event_judul.setBackgroundColor(Color.parseColor("#ff75c9"));
        }else if(item.key_event.equalsIgnoreCase("9")){
            //Set Layout Aktif-----------------------------------------------------------------
            row.linearLayout_detailternal_activity_pengelompokkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemerahan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankesehatan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankuku.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanmastitis.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanlameness.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanreproduksi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanheat.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_vaksinasi.setVisibility(View.VISIBLE);
            row.linearLayout_detailternal_activity_inseminasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_melahirkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_aborsi.setVisibility(View.GONE);

            //Set Background dan Drawable------------------------------------------------------
            row.ic_detailternak_activity_event_expander.setImageResource(R.drawable.ic_vaksin_white);
            row.linearLayout_detailternak_activity_event_judul.setBackgroundColor(Color.parseColor("#FFA726"));
        }else if(item.key_event.equalsIgnoreCase("10")){
            //Set Layout Aktif-----------------------------------------------------------------
            row.linearLayout_detailternal_activity_pengelompokkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemerahan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankesehatan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankuku.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanmastitis.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanlameness.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanreproduksi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanheat.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_vaksinasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_inseminasi.setVisibility(View.VISIBLE);
            row.linearLayout_detailternal_activity_melahirkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_aborsi.setVisibility(View.GONE);

            //Set Background dan Drawable------------------------------------------------------
            row.ic_detailternak_activity_event_expander.setImageResource(R.drawable.ic_inseminasi_white);
            row.linearLayout_detailternak_activity_event_judul.setBackgroundColor(Color.parseColor("#FF5722"));
        }else if(item.key_event.equalsIgnoreCase("11")){
            //Set Layout Aktif-----------------------------------------------------------------
            row.linearLayout_detailternal_activity_pengelompokkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemerahan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankesehatan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankuku.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanmastitis.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanlameness.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanreproduksi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanheat.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_vaksinasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_inseminasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_melahirkan.setVisibility(View.VISIBLE);
            row.linearLayout_detailternal_activity_aborsi.setVisibility(View.GONE);

            //Set Background dan Drawable------------------------------------------------------
            row.ic_detailternak_activity_event_expander.setImageResource(R.drawable.ic_calves_white);
            row.linearLayout_detailternak_activity_event_judul.setBackgroundColor(Color.parseColor("#FF5252"));
        }else if(item.key_event.equalsIgnoreCase("12")){
            //Set Layout Aktif-----------------------------------------------------------------
            row.linearLayout_detailternal_activity_pengelompokkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemerahan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankesehatan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaankuku.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanmastitis.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanlameness.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanreproduksi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_pemeriksaanheat.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_vaksinasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_inseminasi.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_melahirkan.setVisibility(View.GONE);
            row.linearLayout_detailternal_activity_aborsi.setVisibility(View.VISIBLE);

            //Set Background dan Drawable------------------------------------------------------
            row.ic_detailternak_activity_event_expander.setImageResource(R.drawable.ic_calves_white);
            row.linearLayout_detailternak_activity_event_judul.setBackgroundColor(Color.parseColor("#607D8B"));
        }

        row.Judul.setText(item.title);
        //Convert Date-----------------------------------------------------
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date testDate = null;
        try {
            testDate = sdf.parse(item.tgl);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String newFormat = formatter.format(testDate);
        row.Tgl.setText(newFormat);

        //Pengelompokan--------------------------------------
        row.txt_detailternak_activity_event_kandang.setText(item.pengelompokan_kandang);
        row.txt_detailternak_activity_event_kawanan.setText(item.pengelompokan_kawanan);

        //Pemerahan------------------------------------------
        row.txt_detailternak_activity_event_sesiperah.setText(item.pemerahan_sesi);
        row.txt_detailternak_activity_event_durasi.setText(item.pemerahan_durasi);
        row.txt_detailternak_activity_event_kapasitas.setText(item.pemerahan_kapasitas);

        //Pemeriksaan Kesehatan------------------------------
        row.txt_detailternak_activity_event_beratbadan.setText(item.kesehatan_berat);
        row.txt_detailternak_activity_event_suhubadan.setText(item.kesehatan_suhu);
        row.txt_detailternak_activity_event_statusfisikkesehatan.setText(item.kesehatan_statusfisik);
        row.txt_detailternak_activity_event_statusstress.setText(item.kesehatan_statusstress);
        row.txt_detailternak_activity_event_diagnosiskesehatan.setText(item.kesehatan_diagnosis);
        row.txt_detailternak_activity_event_perawatankesehatan.setText(item.kesehatan_perawatan);

        //Kuku-----------------------------------------------
        row.txt_detailternak_activity_event_statusfisikkuku.setText(item.kuku_statusfisik);
        row.txt_detailternak_activity_event_diagnosiskuku.setText(item.kuku_diagnosis);
        row.txt_detailternak_activity_event_perawatankuku.setText(item.kuku_perawatan);

        //Mastitis-------------------------------------------
        row.txt_detailternak_activity_event_statusfisikmastitis.setText(item.mastitis_statusfisik);
        row.txt_detailternak_activity_event_diagnosismastitis.setText(item.mastitis_diagnosis);
        row.txt_detailternak_activity_event_perawatanmastitis.setText(item.mastitis_perawatan);

        //Lameness-------------------------------------------
        row.txt_detailternak_activity_event_statusfisiklameness.setText(item.lameness_statusfisik);
        row.txt_detailternak_activity_event_diagnosislameness.setText(item.lameness_diagnosis);
        row.txt_detailternak_activity_event_perawatanlameness.setText(item.lameness_perawatan);

        //Pemeriksaan Reproduksi-----------------------------
        row.txt_detailternak_activity_event_statusfisikreproduksi.setText(item.reproduksi_kondisi);

        //Pemeriksaa Heat------------------------------------
        row.txt_detailternak_activity_event_tglmulai.setText(item.heat_tglmulai);
        row.txt_detailternak_activity_event_tglselesai.setText(item.heat_tglselesai);

        //Vaksinasi------------------------------------------
        row.txt_detailternak_activity_event_namavaksin.setText(item.vaksinasi_nama);
        row.txt_detailternak_activity_event_repetisi.setText(item.vaksinasi_repetisi);
        row.txt_detailternak_activity_event_dosis.setText(item.vaksinasi_dosis);
        row.txt_detailternak_activity_event_satuan.setText(item.vaksinasi_satuan);

        //Inseminasi-----------------------------------------
        row.txt_detailternak_activity_event_metodeinseminasi.setText(item.inseminasi_metode);
        row.txt_detailternak_activity_event_statuskeberhasilan.setText(item.inseminasi_status);
        row.txt_detailternak_activity_event_tglperkiraanlahir.setText(item.inseminasi_tgl_perkiraan_melahirkan);

        //Melahirkan-----------------------------------------
        row.txt_detailternak_activity_event_kondisimelahirkan.setText(item.melahirkan_kondisi);
        row.txt_detailternak_activity_event_jumlahanakdilahirkan.setText(item.melahirkan_jumlahanak);

        //Aborsi---------------------------------------------
        row.txt_detailternak_activity_event_penyebababorsi.setText(item.aborsi_penyebab);


        return convertView;
    }
}
package com.fintech.ternaku.ListDetailTernak;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fintech.ternaku.ListDetailTernak.*;
import com.fintech.ternaku.R;

import java.util.ArrayList;
import java.util.List;

import cn.fanrunqi.waveprogress.WaveProgressView;

/**
 * Created by Pandhu on 9/28/16.
 */

public class AdapterDetailTernakListDetailTernak extends ArrayAdapter<ModelDetailTernalListDetailTernak> {
    private Activity activity;
    private List<ModelDetailTernalListDetailTernak> ternak;
    private static LayoutInflater inflater=null;
    private int layout;
    private List<ModelDetailTernalListDetailTernak> data = new ArrayList<ModelDetailTernalListDetailTernak>();
    Activity act;

    public AdapterDetailTernakListDetailTernak(Activity a, int layout, List<ModelDetailTernalListDetailTernak>items) {
        super(a, layout, items);
        this.activity = a;
        this.layout = layout;
        data = items;
        act = a;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);

        holder = new ViewHolder();
        view.setTag(holder);

        holder.id_ternak = (TextView)view.findViewById(R.id.txt_listdetailternak_activity_idternak);
        holder.kondisi_kesuburan = (TextView)view.findViewById(R.id.txt_listdetailternak_activity_kondisikesuburan);
        holder.tgl_terakhir_periksa = (TextView)view.findViewById(R.id.txt_listdetailternak_activity_terkahircek);
        holder.kondisi_kesehatan = (TextView)view.findViewById(R.id.txt_listdetailternak_activity_kondisitubuh);
        holder.img_kondisi_tubuh = (WaveProgressView) view.findViewById(R.id.img_listdetailternak_activity_kondisitubuh);


        ModelDetailTernalListDetailTernak ternak = new ModelDetailTernalListDetailTernak();
        ternak = data.get(position);
        // Setting all values in listview

        holder.id_ternak.setText(ternak.getId_ternak());
        holder.kondisi_kesuburan.setText(ternak.getKesuburan());
        holder.tgl_terakhir_periksa.setText(ternak.getTgl_terakhir_check());
        holder.kondisi_kesehatan.setText(getString_kondisi_tubuh(Integer.parseInt(ternak.getBody_condition_score())));
        int set = Integer.parseInt(ternak.getBody_condition_score());
        int count = set*10;
        holder.img_kondisi_tubuh.setCurrent(count, count+"%");
        holder.img_kondisi_tubuh.setText("#000000", 50);
        if(set>=7)
        {
            holder.img_kondisi_tubuh.setWaveColor("#2ecc71");
        }
        if(set<7 && set>=5)
        {
            holder.img_kondisi_tubuh.setWaveColor("#e67e22");
        }
        if(set<5&&set>0)
        {
            holder.img_kondisi_tubuh.setWaveColor("#e74c3c");
        }
        return view;
    }

    public class ViewHolder {
        public TextView id_ternak,kondisi_kesuburan,kondisi_kesehatan,tgl_terakhir_periksa;
        public WaveProgressView img_kondisi_tubuh;
    }

    public String getString_kondisi_tubuh(int kondisi){
        String string_kondisi="N/A";
        if(kondisi>=7){
                string_kondisi = "Baik";
        }else if(kondisi<7&&kondisi>=5){
            string_kondisi = "Cukup Baik";
        }else if(kondisi>0&&kondisi<5){
            string_kondisi = "Buruk";
        }

        return string_kondisi;
    }
}

package com.fintech.ternaku.Konsentrat;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fintech.ternaku.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YORIS on 1/19/17.
 */

public class AdapterBahan extends ArrayAdapter<ModelBahan> implements Filterable {
    private Activity activity;
    private List<ModelBahan> ternak;
    private static LayoutInflater inflater=null;
    private int layout;
    private List<ModelBahan> data = new ArrayList<ModelBahan>();
    private List<ModelBahan> dataorigin = new ArrayList<ModelBahan>();

    Activity act;
    ViewHolder holder = new ViewHolder();


    public AdapterBahan(Activity a, int layout, List<ModelBahan>items) {
        super(a, layout, items);
        this.activity = a;
        this.layout = layout;
        data = items;
        dataorigin = items;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);

        view.setTag(holder);

        holder.nama = (TextView)view.findViewById(R.id.txtBahan);
        holder.jumlah = (TextView)view.findViewById(R.id.txtJumlah);
        holder.satuan = (TextView)view.findViewById(R.id.txtSatuan);
        holder.totalbiaya = (TextView)view.findViewById(R.id.txtBiaya);
        holder.btnHapus = (Button)view.findViewById(R.id.btnHapus);

        ModelBahan bahan = new ModelBahan();
        bahan = data.get(position);
        //Setting all values in listview

        holder.nama.setText(bahan.getNama());
        holder.jumlah.setText(bahan.getJumlah());
        holder.satuan.setText(bahan.getSatuan());
        holder.totalbiaya.setText("Rp. "+bahan.getHarga());

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    public class ViewHolder {
        public TextView nama,jumlah,satuan,totalbiaya;
        public Button btnHapus;
    }

    public ModelBahan getData(int position){
        return data.get(position);
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


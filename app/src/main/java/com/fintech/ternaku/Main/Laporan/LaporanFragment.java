package com.fintech.ternaku.Main.Laporan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.Main.Laporan.PenggunaanPakan.LaporanPenggunaanPakan;
import com.fintech.ternaku.Main.Laporan.ProduksiSusu.LaporanProduksiSusuGrafik;
import com.fintech.ternaku.R;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Currency;

public class LaporanFragment extends Fragment {
    private Button button_laporan_fragment_lihat_grafik_produksisusu,button_laporan_fragment_lihat_grafik_penggunaanpakan,
            button_laporan_fragment_lihat_grafik_keuangan,
            button_laporan_fragment_produksisusu,button_laporan_fragment_pakan,
            button_laporan_fragment_keuangan;
    private ExpandableRelativeLayout expanderlayout_laporan_fragment_produsisusu,expanderlayout_laporan_fragment_keuangan,
            expanderlayout_laporan_fragment_penggunaanpakan;
    private TextView input_laporan_fragment_lihat_grafik_produksisusu,input_laporan_fragment_lihat_grafik_penggunaanpakan,
            input_laporan_fragment_lihat_grafik_pengeluaran_keuangan,input_laporan_fragment_lihat_grafik_pemasukan_keuangan;

    public LaporanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_laporan, container, false);

        //Set Data------------------------------------------------------------
        String param = "uid=" + getActivity().getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetProduksiSusuTotal().execute("http://ternaku.com/index.php/C_Laporan/TotalProduksiSusu_TERNAK_TAHUN_INI",param);

        //Set Expander--------------------------------------------------------
        button_laporan_fragment_produksisusu=(Button)view.findViewById(R.id.button_laporan_fragment_produksisusu);
        expanderlayout_laporan_fragment_produsisusu = (ExpandableRelativeLayout) view.findViewById(R.id.expanderlayout_laporan_fragment_produsisusu);
        button_laporan_fragment_produksisusu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanderlayout_laporan_fragment_produsisusu.toggle();
            }
        });
        button_laporan_fragment_pakan=(Button)view.findViewById(R.id.button_laporan_fragment_pakan);
        expanderlayout_laporan_fragment_penggunaanpakan = (ExpandableRelativeLayout) view.findViewById(R.id.expanderlayout_laporan_fragment_penggunaanpakan);
        button_laporan_fragment_pakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanderlayout_laporan_fragment_penggunaanpakan.toggle();
            }
        });
        button_laporan_fragment_keuangan=(Button)view.findViewById(R.id.button_laporan_fragment_keuangan);
        expanderlayout_laporan_fragment_keuangan = (ExpandableRelativeLayout) view.findViewById(R.id.expanderlayout_laporan_fragment_keuangan);
        button_laporan_fragment_keuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanderlayout_laporan_fragment_keuangan.toggle();
            }
        });


        //Set Value Total----------------------------------------------------------
        input_laporan_fragment_lihat_grafik_produksisusu = (TextView)view.findViewById(R.id.input_laporan_fragment_lihat_grafik_produksisusu);
        input_laporan_fragment_lihat_grafik_penggunaanpakan = (TextView)view.findViewById(R.id.input_laporan_fragment_lihat_grafik_penggunaanpakan);
        input_laporan_fragment_lihat_grafik_pengeluaran_keuangan = (TextView)view.findViewById(R.id.input_laporan_fragment_lihat_grafik_pengeluaran_keuangan);
        input_laporan_fragment_lihat_grafik_pemasukan_keuangan = (TextView)view.findViewById(R.id.input_laporan_fragment_lihat_grafik_pemasukan_keuangan);

        //Set Button Function------------------------------------------------------
        button_laporan_fragment_lihat_grafik_produksisusu = (Button)view.findViewById(R.id.button_laporan_fragment_lihat_grafik_produksisusu);
        button_laporan_fragment_lihat_grafik_produksisusu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i = new Intent(getActivity(),LaporanProduksiSusuGrafik.class);
                startActivity(i);
            }
        });
        button_laporan_fragment_lihat_grafik_penggunaanpakan = (Button)view.findViewById(R.id.button_laporan_fragment_lihat_grafik_penggunaanpakan);
        button_laporan_fragment_lihat_grafik_penggunaanpakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i = new Intent(getActivity(),LaporanPenggunaanPakan.class);
                startActivity(i);
            }
        });
        button_laporan_fragment_lihat_grafik_keuangan = (Button)view.findViewById(R.id.button_laporan_fragment_lihat_grafik_keuangan);
        button_laporan_fragment_lihat_grafik_keuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i = new Intent(getActivity(),LaporanKeuangan.class);
                startActivity(i);

            }
        });

        return  view;
    }

    //Convert Currency---------------------------------------------------
    public String Convert(String jumlahbiaya)
    {
        String currencyCode = "IDR";
        Currency currency = Currency.getInstance(currencyCode);
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(currency);
        String formattedAmount = format.format(jumlahbiaya);

        return formattedAmount;
    }

    //Get Total Produksi Susu------------------------------------------------
    private class GetProduksiSusuTotal extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0],params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("RES", result);
            GetDataTotalProduksi(result);
            String param2 = "uid=" + getActivity().getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
            new GetUangMasuk().execute("http://ternaku.com/index.php/C_Laporan/TotalUangMasuk_TAHUN_INI", param2);
        }
    }
    private void GetDataTotalProduksi(String result){
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                input_laporan_fragment_lihat_grafik_produksisusu.setText(jObj.getString("Jumlah Produksi")+" Liter");
            }
        }
        catch (JSONException e){e.printStackTrace();}    }

    //Get Penggunaan Pakan Total------------------------------------
    private class GetPenggunaanPakanTotal extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0],params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("RES",result);
            GetPengeluaranFunc(result);
        }
    }
    private void GetPengeluaranFunc(String result2){
        try{
            JSONArray jArray = new JSONArray(result2);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                input_laporan_fragment_lihat_grafik_penggunaanpakan.setText(jObj.getString("Jumlah Pakan")+ " Kilogram");
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }


    //Get Pemasukan Total---------------------------------------
    private class GetUangMasuk extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0],params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("RES",result);
            GetUangMasukFunc(result);
            String param2 = "uid=" + getActivity().getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
            new GetUangKeluar().execute("http://ternaku.com/index.php/C_Laporan/TotalUangKeluar_TAHUN_INI",param2);
        }
    }

    //Get Pengeluaran Total---------------------------------------
    private class GetUangKeluar extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0],params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("RES", result);
            GetUangKeluarFunc(result);
            String param2 = "uid=" + getActivity().getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
            new GetPenggunaanPakanTotal().execute("http://ternaku.com/index.php/C_Laporan/TotalPakan_TERNAK_TAHUN_INI", param2);
        }
    }
    private void GetUangMasukFunc(String result2){
        try{
            JSONArray jArray = new JSONArray(result2);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                input_laporan_fragment_lihat_grafik_pemasukan_keuangan.setText("Rp. " + jObj.getString("JumlahUangMasuk"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }
    private void GetUangKeluarFunc(String result3){
        try{
            JSONArray jArray = new JSONArray(result3);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                input_laporan_fragment_lihat_grafik_pengeluaran_keuangan.setText("Rp. " + jObj.getString("JumlahUangKeluar"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }
}

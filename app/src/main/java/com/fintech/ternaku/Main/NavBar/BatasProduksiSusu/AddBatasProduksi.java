package com.fintech.ternaku.Main.NavBar.BatasProduksiSusu;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.Main.NavBar.BatasProduksiSusu.ModelGetKawananAddBatasProduksi;
import com.fintech.ternaku.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddBatasProduksi extends AppCompatActivity {
    private Spinner spinner_addbatasproduksi_activity_namakawanan;
    private EditText input_addbatasproduksi_activity_rendah,input_addbatasproduksi_activity_sedang,input_addbatasproduksi_activity_tinggi;
    private Button button_addbatasproduksi_activity_simpan;
    ArrayList<String> list_kawanan = new ArrayList<String>();
    public String temp_id_kawanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_batas_produksi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Batas Produksi Susu");
        }

        //Set Kawanan--------------------------------------------
        String urlParameter_kawanan = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetIdKawanan().execute("http://ternaku.com/index.php/C_BatasProduksi/getKawanan", urlParameter_kawanan);

        //Set Batas----------------------------------------------
        input_addbatasproduksi_activity_rendah = (EditText) findViewById(R.id.input_addbatasproduksi_activity_rendah);
        input_addbatasproduksi_activity_sedang = (EditText) findViewById(R.id.input_addbatasproduksi_activity_sedang);
        input_addbatasproduksi_activity_tinggi = (EditText) findViewById(R.id.input_addbatasproduksi_activity_tinggi);

        //Insert To Database-------------------------------------
        button_addbatasproduksi_activity_simpan = (Button) findViewById(R.id.button_addbatasproduksi_activity_simpan);
        button_addbatasproduksi_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Insert Database--
                new SweetAlertDialog(AddBatasProduksi.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Simpan")
                        .setContentText("Data Yang Dimasukkan Sudah Benar?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                String urlParameters_insert_batas_produksi = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null)+
                                        "&idkawanan=" + temp_id_kawanan.trim()+
                                        "&rendah="+ input_addbatasproduksi_activity_rendah.getText().toString().trim()+
                                        "&sedang="+ input_addbatasproduksi_activity_sedang.getText().toString().trim()+
                                        "&tinggi="+ input_addbatasproduksi_activity_tinggi.getText().toString().trim();
                                new InsertToBatasProduksi().execute("http://ternaku.com/index.php/C_BatasProduksi/InsertBatasProduksiSusu", urlParameters_insert_batas_produksi);

                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        })
                        .show();
            }
        });

    }

    //AsyncTast Insert To Database Batas Produksi---------------
    private class InsertToBatasProduksi extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(AddBatasProduksi.this, SweetAlertDialog.PROGRESS_TYPE);

        @Override
        protected void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#fa6900"));
            pDialog.setTitleText("Menyimpan Data");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0],params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("InsertToDbBatasProduksi",s);
            pDialog.dismiss();
            if (s.trim().equals("1")){
                new SweetAlertDialog(AddBatasProduksi.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan..")
                        .show();
            }
            else if(s.trim().equals("2")){

                new SweetAlertDialog(AddBatasProduksi.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Penambahan Gagal!")
                        .setContentText("Silahkan Simpan Data Kembali")
                        .show();
            }
            else {
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
            }
        }
    }

    //AsyncTask get Kawanan-------------------------------------
    private class GetIdKawanan extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(AddBatasProduksi.this, SweetAlertDialog.PROGRESS_TYPE);

        @Override
        protected void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#fa6900"));
            pDialog.setTitleText("Memuat Data");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0],params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("GetKawanan",s);
            pDialog.dismiss();
            AddKawananToList(s);
        }
    }

    private void AddKawananToList(String res){
        try{
            JSONArray jArray = new JSONArray(res);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                ModelGetKawananAddBatasProduksi kan = new ModelGetKawananAddBatasProduksi();
                kan.setId_kawanan(jObj.getString("ID_KAWANAN"));
                kan.setNama_kawanan(jObj.getString("NAMA_KAWANAN"));

                list_kawanan.add("("+kan.getId_kawanan()+") "+kan.getNama_kawanan());
            }
            spinner_addbatasproduksi_activity_namakawanan = (Spinner)findViewById(R.id.spinner_addbatasproduksi_activity_namakawanan);
            ArrayAdapter<String> adapter_nama_kawanan= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,list_kawanan);
            adapter_nama_kawanan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_addbatasproduksi_activity_namakawanan.setAdapter(adapter_nama_kawanan);
            spinner_addbatasproduksi_activity_namakawanan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String id_kawanan = list_kawanan.get(i);
                    id_kawanan = id_kawanan.substring(id_kawanan.indexOf("(")+1);
                    id_kawanan = id_kawanan.substring(0,id_kawanan.indexOf(")"));
                    temp_id_kawanan=id_kawanan;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        catch (JSONException e){e.printStackTrace();}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

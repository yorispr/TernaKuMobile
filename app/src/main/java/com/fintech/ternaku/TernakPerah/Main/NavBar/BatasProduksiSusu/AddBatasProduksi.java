package com.fintech.ternaku.TernakPerah.Main.NavBar.BatasProduksiSusu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.TernakPerah.Main.TambahData.PindahTernak.AddKawanan;
import com.fintech.ternaku.R;
import com.fintech.ternaku.UrlList;

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

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

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
        new GetIdKawanan().execute(url.getUrl_GetKawanan(), urlParameter_kawanan);

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
                if(checkform()){
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
                                    new InsertToBatasProduksi().execute(url.getUrl_InsertBatasProduksi(), urlParameters_insert_batas_produksi);

                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                }
                            })
                            .show();
                }else{
                    new SweetAlertDialog(AddBatasProduksi.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Isikan Semua Data")
                            .show();
                }
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
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(AddBatasProduksi.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Batas Produksi Susu")
                                        .setContentText("Apakah Ingin Menambah Data Batas Produksi Lagi?")
                                        .setConfirmText("Ya")
                                        .setCancelText("Tidak")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                                cleartext();
                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.cancel();
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        })
                        .show();
            }else if(s.trim().equals("0")){
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
            if(s.trim().equals("kosong")){
                new SweetAlertDialog(AddBatasProduksi.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Koneksi Terputus!")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                            }
                        })
                        .show();
            }else if (s.trim().equals("404")){
                new SweetAlertDialog(AddBatasProduksi.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Gagal Memuat Data")
                        .setContentText("Data Kawanan Masih Kosong" + "\nApakah Ingin Memasukkan Data Kawanan?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                finish();
                                startActivity(new Intent(AddBatasProduksi.this, AddKawanan.class));
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                finish();
                            }
                        })
                        .show();
            }
            else{
                AddKawananToList(s);
            }
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

    public void cleartext(){

        input_addbatasproduksi_activity_rendah.setText("");
        input_addbatasproduksi_activity_sedang.setText("");
        input_addbatasproduksi_activity_tinggi.setText("");
        input_addbatasproduksi_activity_rendah.setHint("Batas Rendah (Liter)");
        input_addbatasproduksi_activity_sedang.setHint("Batas Sedang (Liter)");
        input_addbatasproduksi_activity_tinggi.setText("Batas Tinggi (Liter)");
    }

    public boolean checkform(){
        boolean value = true;
        if(TextUtils.isEmpty(input_addbatasproduksi_activity_rendah.getText().toString())){
            value= false;
            input_addbatasproduksi_activity_rendah.setError("Isikan Batas Rendah Produksi Susu");
        }
        if(TextUtils.isEmpty(input_addbatasproduksi_activity_sedang.getText().toString())){
            value= false;
            input_addbatasproduksi_activity_sedang.setError("Isikan Batas Sedang Produksi Susu");
        }
        if(TextUtils.isEmpty(input_addbatasproduksi_activity_tinggi.getText().toString())){
            value= false;
            input_addbatasproduksi_activity_tinggi.setError("Isikan Batas Tinggi Produksi Susu");
        }

        return  value;
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
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}

package com.fintech.ternaku.TernakPotong.KompisisiPakan;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class KomposisiPakanActivity extends AppCompatActivity {

    private EditText input_komposisi_activity_umurmin, input_komposisi_activity_umurmax, input_komposisi_activity_hijauanjumlah,
            input_komposisi_activity_hijauanbiaya, input_komposisi_activity_konsentratjumlah, input_komposisi_activity_konsentratbiaya,
            input_komposisi_activity_jenissapi;

    private RadioGroup radiogroup_komposisipakan_activity_insertupdate;
    private RadioButton radiobutton_komposisipakan_activity_insert,radiobutton_komposisipakan_activity_update;

    private LinearLayout linearlayout_komposisi_activity_konsentrat,layout_komposisipakan_form;

    boolean isUpdate = false;


    private SearchableSpinner spinner_komposisi_activity_konsentrat;
    ArrayAdapter<String> myAdapter;
    int selectedindex = -1;

    ArrayList<String> list_komposisipakan_pakan = new ArrayList<String>();
    ArrayList<String> list_komposisipakan_idpakan = new ArrayList<String>();

    private Button button_komposisipakan_activity_simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komposisi_pakan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Komposisi Konsentrat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        input_komposisi_activity_umurmin = (EditText) findViewById(R.id.input_komposisi_activity_umurmin);
        input_komposisi_activity_umurmax = (EditText) findViewById(R.id.input_komposisi_activity_umurmax);

        input_komposisi_activity_hijauanjumlah = (EditText) findViewById(R.id.input_komposisi_activity_hijauanjumlah);
        input_komposisi_activity_hijauanbiaya = (EditText) findViewById(R.id.input_komposisi_activity_hijauanbiaya);
        input_komposisi_activity_konsentratjumlah = (EditText) findViewById(R.id.input_komposisi_activity_konsentratjumlah);
        input_komposisi_activity_konsentratbiaya = (EditText) findViewById(R.id.input_komposisi_activity_konsentratbiaya);

        input_komposisi_activity_jenissapi = (EditText)findViewById(R.id.input_komposisi_activity_jenissapi);

        radiogroup_komposisipakan_activity_insertupdate = (RadioGroup)findViewById(R.id.radiogroup_komposisipakan_activity_insertupdate);
        radiobutton_komposisipakan_activity_insert = (RadioButton)findViewById(R.id.radiobutton_komposisipakan_activity_insert);
        radiobutton_komposisipakan_activity_update = (RadioButton)findViewById(R.id.radiobutton_komposisipakan_activity_update);

        linearlayout_komposisi_activity_konsentrat = (LinearLayout)findViewById(R.id.linearlayout_komposisi_activity_konsentrat);
        layout_komposisipakan_form = (LinearLayout)findViewById(R.id.layout_komposisipakan_form);
        layout_komposisipakan_form.setVisibility(View.GONE);
        spinner_komposisi_activity_konsentrat = (SearchableSpinner)findViewById(R.id.spinner_komposisi_activity_konsentrat);
        myAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,list_komposisipakan_pakan);
        spinner_komposisi_activity_konsentrat.setAdapter(myAdapter);
        spinner_komposisi_activity_konsentrat.setTitle("Pilih Komposisi");

        spinner_komposisi_activity_konsentrat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedindex = i;
                if(isUpdate){
                    if(selectedindex !=- 1) {
                        String urlParameters2 =
                                "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                                        + "&idkomposisi=" + list_komposisipakan_idpakan.get(selectedindex);
                        new GetKonsentratById().execute("http://service.ternaku.com/C_Pedaging/GetKomposisiPakanByIdKomposisi", urlParameters2);
                        Log.d("paramupdate",urlParameters2);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button_komposisipakan_activity_simpan = (Button)findViewById(R.id.button_komposisipakan_activity_simpan);
        button_komposisipakan_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {
                    if(isUpdate) {
                        if (selectedindex == -1 || selectedindex == 0) {
                            new SweetAlertDialog(KomposisiPakanActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Peringatan!")
                                    .setContentText("Silahkan Pilih Komposisi yang ingin dirubah")
                                    .show();
                        }
                        else{
                            String urlParameters2;
                            float totalbiaya = 0;
                            totalbiaya = Float.parseFloat(input_komposisi_activity_konsentratbiaya.getText().toString()) + Float.parseFloat(input_komposisi_activity_hijauanbiaya.getText().toString());
                            urlParameters2 =
                                    "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                                            +"&kgkonsentrat=" + input_komposisi_activity_konsentratjumlah.getText().toString()
                                            +"&kghijauan="+input_komposisi_activity_hijauanjumlah.getText().toString()
                                            +"&hargakonsentrat="+input_komposisi_activity_konsentratbiaya.getText().toString()
                                            +"&hargahijauan="+input_komposisi_activity_hijauanbiaya.getText().toString()
                                            +"&totalharga="+totalbiaya
                                            +"&usiabawah="+input_komposisi_activity_umurmin.getText().toString()
                                            +"&usiaatas="+input_komposisi_activity_umurmax.getText().toString()
                                            +"&jenisapi="+input_komposisi_activity_jenissapi.getText().toString();
                            urlParameters2 += "&idkomposisi="+list_komposisipakan_idpakan.get(selectedindex);
                            new AddKonsentrasiPakan().execute("http://service.ternaku.com/C_Pedaging/updateKomposisiPakan", urlParameters2);
                            Log.d("paramupdate",urlParameters2);
                        }
                    }
                    else{
                        new SweetAlertDialog(KomposisiPakanActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Simpan")
                                .setContentText("Data Yang Dimasukkan Sudah Benar?")
                                .setConfirmText("Ya")
                                .setCancelText("Tidak")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        String urlParameters2;
                                        float totalbiaya = 0;
                                        totalbiaya = Float.parseFloat(input_komposisi_activity_konsentratbiaya.getText().toString()) + Float.parseFloat(input_komposisi_activity_hijauanbiaya.getText().toString());
                                        urlParameters2 =
                                                "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                                                        +"&kgkonsentrat=" + input_komposisi_activity_konsentratjumlah.getText().toString()
                                                        +"&kghijauan="+input_komposisi_activity_hijauanjumlah.getText().toString()
                                                        +"&hargakonsentrat="+input_komposisi_activity_konsentratbiaya.getText().toString()
                                                        +"&hargahijauan="+input_komposisi_activity_hijauanbiaya.getText().toString()
                                                        +"&totalharga="+totalbiaya
                                                        +"&usiabawah="+input_komposisi_activity_umurmin.getText().toString()
                                                        +"&usiaatas="+input_komposisi_activity_umurmax.getText().toString()
                                                        +"&jenisapi="+input_komposisi_activity_jenissapi.getText().toString();

                                        Log.d("param",urlParameters2);

                                        if(!isUpdate) {
                                            new AddKonsentrasiPakan().execute("http://service.ternaku.com/C_Pedaging/insertKomposisiPakan", urlParameters2);
                                        }else{
                                            urlParameters2 += "&idkomposisi"+list_komposisipakan_idpakan.get(selectedindex);
                                            new AddKonsentrasiPakan().execute("http://service.ternaku.com/C_Pedaging/updateKomposisiPakan", urlParameters2);
                                        }

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
                }

                else {
                    new SweetAlertDialog(KomposisiPakanActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Isikan Semua Data")
                            .show();
                }
            }
        });


        radiogroup_komposisipakan_activity_insertupdate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radiobutton_komposisipakan_activity_insert) {
                    clearInput();
                    setEnable(true);
                    isUpdate = false;
                    linearlayout_komposisi_activity_konsentrat.setVisibility(View.GONE);
                    layout_komposisipakan_form.setVisibility(View.VISIBLE);

                    //   String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                    //           +"&fungsi=insert";
                    //   new GetKawanan().execute(url.getUrl_GetKawananBB(), urlParameters);
                } else if(checkedId == R.id.radiobutton_komposisipakan_activity_update) {
                    setEnable(true);
                    isUpdate = true;
                    String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                            ;
                    new GetDataKonsentrat().execute("http://service.ternaku.com/C_Pedaging/GetKomposisiPakanByPeternakanUpdate", urlParameters);
                }


            }
        });

    }

    private void clearInput(){
        input_komposisi_activity_umurmin.setText("");
        input_komposisi_activity_umurmin.setHint("0");

        input_komposisi_activity_umurmax.setText("");
        input_komposisi_activity_umurmax.setHint("0");

        input_komposisi_activity_hijauanjumlah.setText("");
        input_komposisi_activity_hijauanjumlah.setHint("0");

        input_komposisi_activity_hijauanbiaya.setText("");
        input_komposisi_activity_hijauanbiaya.setHint("0");

        input_komposisi_activity_konsentratjumlah.setText("");
        input_komposisi_activity_konsentratjumlah.setHint("0");

        input_komposisi_activity_konsentratbiaya.setText("");
        input_komposisi_activity_konsentratbiaya.setHint("0");

        input_komposisi_activity_jenissapi.setText("");
        input_komposisi_activity_jenissapi.setHint("cth : limousin");

    }

    private boolean checkForm() {
        boolean value = true;
        if(TextUtils.isEmpty(input_komposisi_activity_umurmin.getText().toString())){
            value= false;
            input_komposisi_activity_umurmin.setError("Isikan Umur Minimum");
        }
        if(TextUtils.isEmpty(input_komposisi_activity_umurmax.getText().toString())){
            value= false;
            input_komposisi_activity_umurmax.setError("Isikan Umur Maksimum");
        }
        if(TextUtils.isEmpty(input_komposisi_activity_hijauanjumlah.getText().toString())){
            value= false;
            input_komposisi_activity_hijauanjumlah.setError("Isikan Jumlah");
        }
        if(TextUtils.isEmpty(input_komposisi_activity_hijauanbiaya.getText().toString())){
            value= false;
            input_komposisi_activity_hijauanbiaya.setError("Isikan Jumlah");
        }
        if(TextUtils.isEmpty(input_komposisi_activity_konsentratjumlah.getText().toString())){
            value= false;
            input_komposisi_activity_konsentratjumlah.setError("Isikan Jumlah");
        }
        if(TextUtils.isEmpty(input_komposisi_activity_konsentratbiaya.getText().toString())){
            value= false;
            input_komposisi_activity_konsentratbiaya.setError("Isikan Jumlah");
        }

        if(TextUtils.isEmpty(input_komposisi_activity_jenissapi.getText().toString())){
            value= false;
            input_komposisi_activity_jenissapi.setError("Isikan Jenis Ternak");
        }
        return value;
    }


    private void setEnable(boolean b){
    }


    private class AddKonsentrasiPakan extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(KomposisiPakanActivity.this, SweetAlertDialog.PROGRESS_TYPE);

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
        protected void onPostExecute(String result) {
            Log.d("RES",result);
            pDialog.dismiss();
            if (result.trim().equals("1")){
                new SweetAlertDialog(KomposisiPakanActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(KomposisiPakanActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Batas Berat")
                                        .setContentText("Apakah Ingin Menambah Batas Berat Lagi?")
                                        .setConfirmText("Ya")
                                        .setCancelText("Tidak")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();

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
            }
            else {
                Toast.makeText(getApplication(),"Terjadi kesalahan", Toast.LENGTH_LONG).show();
            }
        }
    }


    private class GetKonsentratById extends AsyncTask<String,Integer,String> {
        ProgressDialog pDialog = new ProgressDialog(KomposisiPakanActivity.this);
        @Override
        protected void onPreExecute() {
            pDialog.show();
            pDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0],params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("result",result);
            if(!result.trim().equalsIgnoreCase("404")) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jobj = jsonArray.getJSONObject(0);
                    input_komposisi_activity_umurmin.setText(jobj.getString("USIA_BAWAH"));
                    input_komposisi_activity_umurmax.setText(jobj.getString("USIA_ATAS"));
                    input_komposisi_activity_konsentratbiaya.setText(jobj.getString("HARGA_KONSENTRAT"));
                    input_komposisi_activity_konsentratjumlah.setText(jobj.getString("KG_KONSENTRAT"));
                    input_komposisi_activity_hijauanbiaya.setText(jobj.getString("HARGA_HIJAUAN"));
                    input_komposisi_activity_hijauanjumlah.setText(jobj.getString("KG_HIJAUAN"));
                    input_komposisi_activity_jenissapi.setText(jobj.getString("JENIS_SAPI"));
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                clearInput();
                pDialog.dismiss();
            }
        }
    }


    private class GetDataKonsentrat extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(KomposisiPakanActivity.this, SweetAlertDialog.PROGRESS_TYPE);

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
        protected void onPostExecute(String result) {
            Log.d("RES",result);
            pDialog.dismiss();
            if(result.trim().equals("kosong")){
                new SweetAlertDialog(KomposisiPakanActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Koneksi Terputus!")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }else if (result.trim().equals("404")){
                new SweetAlertDialog(KomposisiPakanActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Data tidak ditemukan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
            else{
                addKonsentratToList(result);
                layout_komposisipakan_form.setVisibility(View.VISIBLE);
                linearlayout_komposisi_activity_konsentrat.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addKonsentratToList(String result){
        list_komposisipakan_pakan.clear();
        list_komposisipakan_idpakan.clear();


        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            list_komposisipakan_idpakan.add("0");
            list_komposisipakan_pakan.add("--- Pilih Komposisi ---");
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_komposisipakan_idpakan.add(jObj.getString("ID_KOMPOSISI"));
                list_komposisipakan_pakan.add("Umur "+jObj.getString("USIA_BAWAH") +" - "+ jObj.getString("USIA_ATAS") + " bulan");
            }
            myAdapter.notifyDataSetChanged();
            spinner_komposisi_activity_konsentrat.setSelection(0);

        }
        catch (JSONException e){e.printStackTrace();}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

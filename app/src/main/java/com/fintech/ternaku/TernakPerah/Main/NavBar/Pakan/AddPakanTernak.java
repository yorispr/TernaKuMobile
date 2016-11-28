package com.fintech.ternaku.TernakPerah.Main.NavBar.Pakan;

import android.app.DatePickerDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.TernakPerah.Main.MainActivity;
import com.fintech.ternaku.TernakPerah.Main.TambahData.PindahTernak.AddKandang;
import com.fintech.ternaku.R;
import com.fintech.ternaku.UrlList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddPakanTernak extends AppCompatActivity {
    private EditText input_addpakan_activity_jumlahmakan,input_addpakan_activity_hargamakan;
    private Spinner spinner_addapakan_activity_sesimakan,spinner_addpakan_activity_namakandang,spinner_addpakan_activity_namapakan;
    private Button button_addpakan_activity_simpan;
    private TextView input_addpakan_activity_tanggalmakan;
    ArrayList<String> list_kandang = new ArrayList<String>();
    ArrayList<String> list_pakan = new ArrayList<String>();
    private int choosenindex=1;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    public String temp_id_kandang,temp_id_pakan;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pakan_ternak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(false);
            actionbar.setTitle("Penggunaan Pakan");
        }
        hideSoftKeyboard();

        //Set Prog Dialog------------------------------
        SweetAlertDialog pDialog = new SweetAlertDialog(AddPakanTernak.this, SweetAlertDialog.PROGRESS_TYPE);

        //Set Kandang Ternak---------------------------
        String urlParameter_kandang = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetTernakId().execute(url.getUrl_GetKandang(), urlParameter_kandang);


        //Set Date-------------------------------------
        input_addpakan_activity_tanggalmakan = (TextView) findViewById(R.id.input_addpakan_activity_tanggalmakan);
        setDateTimeField();
        input_addpakan_activity_tanggalmakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDatePickerDialog.show();
            }
        });
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addpakan_activity_tanggalmakan.setText(dateFormatter.format(Calendar.getInstance().getTime()));

        //Set Sesi Makan---------------------------------
        spinner_addapakan_activity_sesimakan = (Spinner)findViewById(R.id.spinner_addpakan_activity_sesimakan);
        final String[] spinner_sesi_makan_data = {"Silahkan Pilih Sesi Makan","Pagi","Siang","Sore"};
        ArrayAdapter<String> adapater_sesi_makan= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,spinner_sesi_makan_data);
        spinner_addapakan_activity_sesimakan.setAdapter(adapater_sesi_makan);

        //Set Informasi dan Harga Pakan------------------------------
        input_addpakan_activity_jumlahmakan = (EditText) findViewById(R.id.input_addpakan_activity_jumlahmakan);
        input_addpakan_activity_hargamakan = (EditText) findViewById(R.id.input_addpakan_activity_hargamakan);

        //Insert To Database---------------------------------------
        button_addpakan_activity_simpan = (Button) findViewById(R.id.button_addpakan_activity_simpan);
        button_addpakan_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkform()){
                    if(spinner_addapakan_activity_sesimakan.getSelectedItemId()==0){
                        new SweetAlertDialog(AddPakanTernak.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Silahkan Pilih Sesi Makan")
                                .show();
                    }else {
                        new SweetAlertDialog(AddPakanTernak.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Simpan")
                            .setContentText("Data Yang Dimasukkan Sudah Benar?")
                            .setConfirmText("Ya")
                            .setCancelText("Tidak")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                    String urlParameters_insert = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null)+
                                            "&idkandang=" + temp_id_kandang.trim()+
                                            "&idpakan="+ temp_id_pakan.trim()+
                                            "&jumlahpakan="+ input_addpakan_activity_jumlahmakan.getText().toString().trim()+
                                            "&tglmakan="+ input_addpakan_activity_tanggalmakan.getText().toString().trim()+
                                            "&sesimakan="+ spinner_addapakan_activity_sesimakan.getSelectedItem().toString().trim()+
                                            "&satuanpakan="+ "kilogram"+
                                            "&biaya="+ input_addpakan_activity_hargamakan.getText().toString().trim();
                                    new InsertToDbPakan().execute(url.getUrl_InsertPenggunaanPakan(), urlParameters_insert);
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
                }else{
                    new SweetAlertDialog(AddPakanTernak.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Isikan Semua Data")
                            .show();
                }
                            }
        });

    }

    //AsyncTask Insert Database ------------------------------------------
    private class InsertToDbPakan extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(AddPakanTernak.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("InserToDb",s);
            pDialog.dismiss();
            if (s.trim().equals("1")){
                new SweetAlertDialog(AddPakanTernak.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(AddPakanTernak.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Penggunaan Pakan")
                                        .setContentText("Apakah Ingin Menambah Data Penggunaan Pakan Lagi?")
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
                new SweetAlertDialog(AddPakanTernak.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Penambahan Gagal!")
                        .setContentText("Silahkan Simpan Data Kembali")
                        .show();
            }
        }
    }

    //AsyncTask get Id Pakan AutoComplete--------------------------------
    private class GetPakanId extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(AddPakanTernak.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("GetIdpakan",s);
            pDialog.dismiss();
            if (s.trim().equals("404")){
                new SweetAlertDialog(AddPakanTernak.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Gagal Memuat Data")
                        .setContentText("Data Pakan Masih Kosong")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                            }
                        })
                        .show();
            }
            else{
                AddPakanToList(s);
            }
        }
    }

    //AsyncTask get Id Ternak AutoComplete--------------------------------
    private class GetTernakId extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(AddPakanTernak.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("GetIdKandang",s);
            pDialog.dismiss();
            if(s.trim().equals("kosong")){
                new SweetAlertDialog(AddPakanTernak.this, SweetAlertDialog.ERROR_TYPE)
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
                new SweetAlertDialog(AddPakanTernak.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Gagal Memuat Data")
                        .setContentText("Data Kandang Masih Kosong" + "\nApakah Ingin Memasukkan Data Kandang?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                finish();
                                startActivity(new Intent(AddPakanTernak.this, AddKandang.class));
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
                AddKandangToList(s);

                //Set Pakan Ternak---------------------------
                String urlParameter_pakan = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
                new GetPakanId().execute(url.getUrl_GetPakan(), urlParameter_pakan);
            }
        }
    }

    private void AddPakanToList(String res){
        try{
            JSONArray jArray = new JSONArray(res);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                ModelGetPakanAddPakan kan = new ModelGetPakanAddPakan();
                kan.setId_pakan(jObj.getString("id_pakan"));
                kan.setNama_pakan(jObj.getString("nama_pakan"));

                list_pakan.add("("+kan.getId_pakan()+") "+kan.getNama_pakan());
            }
            spinner_addpakan_activity_namapakan = (Spinner)findViewById(R.id.spinner_addpakan_activity_namapakan);
            ArrayAdapter<String> adapter_nama_pakan= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,list_pakan);
            adapter_nama_pakan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_addpakan_activity_namapakan.setAdapter(adapter_nama_pakan);
            spinner_addpakan_activity_namapakan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String id_pakan = list_pakan.get(i);
                    id_pakan = id_pakan.substring(id_pakan.indexOf("(")+1);
                    id_pakan = id_pakan.substring(0,id_pakan.indexOf(")"));
                    temp_id_pakan=id_pakan;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        catch (JSONException e){e.printStackTrace();}
    }


    private void AddKandangToList(String res){
        try{
            JSONArray jArray = new JSONArray(res);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                ModelGetKandangAddPakan kan = new ModelGetKandangAddPakan();
                kan.setId_kandang(jObj.getString("ID_KANDANG"));
                kan.setNama_kandang(jObj.getString("NAMA_KANDANG"));

                list_kandang.add("("+kan.getId_kandang()+") "+kan.getNama_kandang());
            }
            spinner_addpakan_activity_namakandang = (Spinner)findViewById(R.id.spinner_addpakan_activity_namakandang);
            ArrayAdapter<String> adapter_nama_kandang= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,list_kandang);
            adapter_nama_kandang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_addpakan_activity_namakandang.setAdapter(adapter_nama_kandang);
            spinner_addpakan_activity_namakandang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String id_kandang = list_kandang.get(i);
                    id_kandang = id_kandang.substring(id_kandang.indexOf("(")+1);
                    id_kandang = id_kandang.substring(0,id_kandang.indexOf(")"));
                    temp_id_kandang=id_kandang;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private void setDateTimeField() {
        //toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addpakan_activity_tanggalmakan.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            finish();
            Intent i = new Intent(AddPakanTernak.this, MainActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void cleartext(){
        input_addpakan_activity_jumlahmakan.setText("");
        input_addpakan_activity_hargamakan.setText("");
        input_addpakan_activity_jumlahmakan.setHint("Jumlah Makan");
        input_addpakan_activity_hargamakan.setHint("Harga Makan(Rupiah)");
        spinner_addapakan_activity_sesimakan.setSelection(0);
    }

    public boolean checkform(){
        boolean value = true;
        if(TextUtils.isEmpty(input_addpakan_activity_jumlahmakan.getText().toString())){
            value= false;
            input_addpakan_activity_jumlahmakan.setError("Isikan Jumlah Makan");
        }
        if(TextUtils.isEmpty(input_addpakan_activity_hargamakan.getText().toString())){
            value= false;
            input_addpakan_activity_hargamakan.setError("Isikan Harga Makan");
        }

        return  value;
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

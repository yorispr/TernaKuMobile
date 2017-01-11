package com.fintech.ternaku.TernakPerah.Main.TambahData.Kesehatan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.TernakPerah.Main.TambahData.PindahTernak.AddKandang;
import com.fintech.ternaku.R;
import com.fintech.ternaku.Setting.Bluetooth;
import com.fintech.ternaku.UrlList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddKarantina extends AppCompatActivity {
    private AutoCompleteTextView input_addkarantina_activity_idternak;
    private RadioGroup radiogroup_addkarantina_activity_mlaiselesai;
    private RadioButton radiobutton_addkarantina_activity_mlai,radiobutton_addkarantina_activity_selesai;
    private TextView input_addkarantina_activity_tglpemeriksaan;
    private EditText input_addkarantina_activity_diagnosis,input_addkarantina_activity_perawatan;
    private Spinner spinner_addkarantina_activity_kawanan,spinner_addkarantina_activity_kandang;
    private Button button_addkarantina_activity_simpan;
    private DatePickerDialog fromDatePickerDialog_tglperiksa;
    private TimePickerDialog mTimePicker;
    private SimpleDateFormat dateFormatter_tglperiksa;
    private String datetime;
    private LinearLayout linearLayout_addkarantina_activity_diagnosis;



    private Bluetooth bt;
    public final String TAG = "AddInseminasi";
    ArrayList<String> list_addkarantina_kandang = new ArrayList<String>();
    ArrayList<String> list_addkarantina_kawanan = new ArrayList<String>();
    ArrayList<String> list_addkarantina_idternak = new ArrayList<String>();
    ArrayList<String> list_addkarantina_karantina = new ArrayList<String >();
    ArrayList<String> list_addkarantina_id_ternak_autocomplete = new ArrayList<String >();
    ArrayAdapter<String> adp;
    ArrayAdapter<String> myAdapter_kawanan,myAdapter_kandang;
    private boolean cekKarantina;
    int flag_radio=0;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_karantina);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Karantina");

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_kesehatan)));
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //Set Auto Text-----------------------------------------
        String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
        new GetTernakId().execute(url.getUrl_GetTernakPengelompokkan(), param);
        input_addkarantina_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addkarantina_activity_idternak);
        input_addkarantina_activity_idternak.setEnabled(false);
        adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_addkarantina_idternak);
        input_addkarantina_activity_idternak.setAdapter(adp);
        input_addkarantina_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

        //Set Radio Button--------------------------------------
        radiogroup_addkarantina_activity_mlaiselesai = (RadioGroup)findViewById(R.id.radiogroup_addkarantina_activity_mlaiselesai);
        linearLayout_addkarantina_activity_diagnosis = (LinearLayout)findViewById(R.id.linearLayout_addkarantina_activity_diagnosis);
        radiogroup_addkarantina_activity_mlaiselesai.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.radiobutton_addkarantina_activity_mlai){
                    flag_radio=1;
                    linearLayout_addkarantina_activity_diagnosis.setVisibility(View.VISIBLE);
                    cekKarantina = true;
                }else if(i==R.id.radiobutton_addkarantina_activity_selesai){
                    flag_radio=1;
                    linearLayout_addkarantina_activity_diagnosis.setVisibility(View.GONE);
                    cekKarantina = false;
                }
            }
        });

        //Set Tanggal Pemeriksaan---------------------------------------------------------
        setDateTimeField();
        setTime();
        input_addkarantina_activity_tglpemeriksaan = (TextView)findViewById(R.id.input_addkarantina_activity_tglpemeriksaan);
        dateFormatter_tglperiksa = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addkarantina_activity_tglpemeriksaan.setText(dateFormatter_tglperiksa.format(Calendar.getInstance().getTime()));
        input_addkarantina_activity_tglpemeriksaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddKarantina.this);
                fromDatePickerDialog_tglperiksa.show();
            }
        });

        //Set Kawanan----------------------------------------------------------
        myAdapter_kawanan= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,list_addkarantina_kawanan);
        spinner_addkarantina_activity_kawanan = (Spinner)findViewById(R.id.spinner_addkarantina_activity_kawanan);
        spinner_addkarantina_activity_kawanan.setAdapter(myAdapter_kawanan);

        //Set Kandang----------------------------------------------------------
        myAdapter_kandang= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,list_addkarantina_kandang);
        spinner_addkarantina_activity_kandang = (Spinner)findViewById(R.id.spinner_addkarantina_activity_kandang);
        spinner_addkarantina_activity_kandang.setAdapter(myAdapter_kandang);

        //Insert To Database---------------------------------------------------
        button_addkarantina_activity_simpan = (Button)findViewById(R.id.button_addkarantina_activity_simpan);
        input_addkarantina_activity_diagnosis = (EditText)findViewById(R.id.input_addkarantina_activity_diagnosis);
        input_addkarantina_activity_perawatan = (EditText)findViewById(R.id.input_addkarantina_activity_perawatan);
        button_addkarantina_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cekForm()) {
                    if(flag_radio==1){
                        final String idkawanan = spinner_addkarantina_activity_kawanan.getSelectedItem().toString().trim();
                        final String idkawanan_2 = idkawanan.substring(idkawanan.indexOf("(") + 1, idkawanan.indexOf(")"));

                        final String idkandang = spinner_addkarantina_activity_kandang.getSelectedItem().toString().trim();
                        final String idkandang_2 = idkandang.substring(idkandang.indexOf("(") + 1, idkandang.indexOf(")"));
                        if(cekKarantina){
                            if(!isKarantina(input_addkarantina_activity_idternak.getText().toString().trim())){
                                new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Simpan")
                                        .setContentText("Data Yang Dimasukkan Sudah Benar?")
                                        .setConfirmText("Ya")
                                        .setCancelText("Tidak")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();

                                                //Cek RFID---------------------------------
                                                Connection c = new Connection();
                                                String urlParameters2;
                                                urlParameters2 = "id=" + input_addkarantina_activity_idternak.getText().toString() +
                                                        "&idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null);
                                                String json = c.GetJSONfromURL(url.getUrlGet_RFIDanIdCek(), urlParameters2);
                                                if(json.trim().equals("1")) {
                                                    String diagnosis = "N/A", perawatan = "N/A";
                                                    if (!input_addkarantina_activity_diagnosis.getText().toString().matches("")) {
                                                        diagnosis = input_addkarantina_activity_diagnosis.getText().toString();
                                                    }
                                                    if (!input_addkarantina_activity_perawatan.getText().toString().matches("")) {
                                                        perawatan = input_addkarantina_activity_perawatan.getText().toString();
                                                    }
                                                    String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                                            + "&idternak=" + input_addkarantina_activity_idternak.getText().toString()
                                                            + "&tglmulaikarantina=" + input_addkarantina_activity_tglpemeriksaan.getText().toString()
                                                            + "&perawatan=" + perawatan
                                                            + "&diagnosis=" + diagnosis
                                                            + "&idkawanan=" + idkawanan_2
                                                            + "&idkandang=" + idkandang_2;

                                                    new AddKarantinatoDatabase().execute(url.getUrl_InsertKarantinaMulai(), param);
                                                }else{
                                                    new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.WARNING_TYPE)
                                                            .setTitleText("Peringatan!")
                                                            .setContentText("Tidak Ada RFID Ditemukan")
                                                            .show();
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
                            }else{
                                new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Peringatan!")
                                        .setContentText("Ternak Tersebut Belum Selesai Dikarantina")
                                        .show();
                            }
                        }else{
                            if(isKarantina(input_addkarantina_activity_idternak.getText().toString().trim())) {
                                new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Simpan")
                                        .setContentText("Data Yang Dimasukkan Sudah Benar?")
                                        .setConfirmText("Ya")
                                        .setCancelText("Tidak")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();

                                                //Cek RFID---------------------------------
                                                Connection c = new Connection();
                                                String urlParameters2;
                                                urlParameters2 = "id=" + input_addkarantina_activity_idternak.getText().toString().trim() +
                                                        "&idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null);
                                                String json = c.GetJSONfromURL(url.getUrlGet_RFIDanIdCek(), urlParameters2);
                                                if(json.trim().equals("1")) {
                                                String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                                        + "&idternak=" + input_addkarantina_activity_idternak.getText().toString().trim()
                                                        + "&TglSelesaiKarantina=" + input_addkarantina_activity_tglpemeriksaan.getText().toString()
                                                        + "&idkawanan=" + idkawanan_2
                                                        + "&idkandang=" + idkandang_2;
                                                new AddKarantinatoDatabase().execute(url.getUrl_InsertKarantinaSelesai(), param);
                                                }else{
                                                    new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.WARNING_TYPE)
                                                            .setTitleText("Peringatan!")
                                                            .setContentText("Tidak Ada RFID Ditemukan")
                                                            .show();
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
                            }else{
                                new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Peringatan!")
                                        .setContentText("Ternak Tersebut Belum Mulai Dikarantina")
                                        .show();
                            }
                        }
                    }else if(flag_radio==0){
                        new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Pilih Mode Ganti Data Mulai Karantina atau Selesai Karantina")
                                .show();
                    }
                }
                else {
                    new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Isikan Semua Data")
                            .show();
                }
            }

        });

        bt = new Bluetooth(this, mHandler);
        bt.start();
        bt.connectDevice("HC-06");

    }


    @Override
    protected void onResume(){
        super.onResume();
        bt = new Bluetooth(this, mHandler);
        bt.start();
        bt.connectDevice("HC-06");
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Bluetooth.MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    break;
                case Bluetooth.MESSAGE_WRITE:
                    Log.d(TAG, "MESSAGE_WRITE ");
                    break;
                case Bluetooth.MESSAGE_READ:
                    Log.d(TAG, "MESSAGE_READ : "+msg.obj.toString());
                    input_addkarantina_activity_idternak.setText(msg.obj.toString().trim());
                    break;
                case Bluetooth.MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "MESSAGE_DEVICE_NAME "+msg);
                    break;
                case Bluetooth.MESSAGE_TOAST:
                    Log.d(TAG, "MESSAGE_TOAST "+msg);

                    break;
            }
        }
    };

    //Set AutoComplete-----------------------------------------------------------
    private class GetTernakId extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.ERROR_TYPE)
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
            } else{
                AddTernakToList(result);
                input_addkarantina_activity_idternak.setEnabled(true);

                //Set Ternak Karantina-------------------------------------------------
                String param_2 = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
                new GetTernakKarantina().execute(url.getUrl_GetKarantina(), param_2);

            }
        }
    }
    private void AddTernakToList(String result) {
        list_addkarantina_idternak.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addkarantina_idternak.add(jObj.getString("id_ternak"));
            }
            adp.notifyDataSetChanged();
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Get Data Ternak Karantina------------------------------------------
    private class GetTernakKarantina extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("RES2",result);
            pDialog.dismiss();
            if(result.trim().equals("kosong")){
                new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.ERROR_TYPE)
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
            }  else{
                AddTernakKarantina(result);

                //Set Kawanan-------------------------------------------------
                String param2 = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
                new GetKawanan().execute(url.getUrl_GetKawanan(), param2);
            }
        }
    }
    private void AddTernakKarantina(String result)
    {
        list_addkarantina_karantina.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addkarantina_karantina.add(jObj.getString("id_ternak"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Get Data Kawanan------------------------------------------------------
    private class GetKawanan extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("RESKawanan",result);
            pDialog.dismiss();

            if(result.trim().equals("kosong")){
                new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.ERROR_TYPE)
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
            }else if (result.trim().equals("404")){
                new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Gagal Memuat Data")
                        .setContentText("Data Kawanan Masih Kosong" + "\nApakah Ingin Memasukkan Data Kawanan?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                finish();
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
            else {
                AddKawananToList(result);

                //Get Data Kandang--------------------------------------------------
                String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
                new GetKandang().execute(url.getUrl_GetKandang(), urlParameters);
            }
        }
    }

    private void AddKawananToList(String result) {
        list_addkarantina_kawanan.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addkarantina_kawanan.add("("+jObj.getString("ID_KAWANAN")+") "+ jObj.getString("NAMA_KAWANAN"));
            }
            myAdapter_kawanan.notifyDataSetChanged();
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Get Data Kandang------------------------------------------------------
    private class GetKandang extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.ERROR_TYPE)
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
            }else if (result.trim().equals("404")){
                new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Gagal Memuat Data")
                        .setContentText("Data Kandang Masih Kosong" + "\nApakah Ingin Memasukkan Data Kandang?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                finish();
                                startActivity(new Intent(AddKarantina.this, AddKandang.class));
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
                AddKandangToList(result);
            }
        }
    }

    private void AddKandangToList(String result) {
        list_addkarantina_kandang.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addkarantina_kandang.add("("+jObj.getString("ID_KANDANG")+") "+ jObj.getString("NAMA_KANDANG"));
            }
            myAdapter_kandang.notifyDataSetChanged();
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Insert In To Database------------------------------------------
    private class AddKarantinatoDatabase extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(AddKarantina.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Karantina")
                                        .setContentText("Apakah Ingin Menambah Data Karantina Lagi?")
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
            }
            else {
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isKarantina(String id){
        boolean cek=false;
        for(int i=0;i<list_addkarantina_karantina.size();i++){
            if(id.equals(list_addkarantina_karantina.get(i))){
                cek = true;
            }
        }
        return cek;
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog_tglperiksa = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addkarantina_activity_tglpemeriksaan.setText(dateFormatter_tglperiksa.format(newDate.getTime()));
                datetime = dateFormatter_tglperiksa.format(newDate.getTime());
                mTimePicker.show();
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(AddKarantina.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(timePicker.isShown()) {
                    datetime += " " + selectedHour + ":" + selectedMinute + ":00";
                    input_addkarantina_activity_tglpemeriksaan.setText(datetime);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public boolean cekForm(){
        boolean cek = true;
        if(input_addkarantina_activity_idternak.getText().toString().matches("")){
            cek = false;
            input_addkarantina_activity_idternak.setError("Data belum diisi");
        }
        if(input_addkarantina_activity_tglpemeriksaan.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            cek=false;
            input_addkarantina_activity_tglpemeriksaan.setError("Data belum diisi");
        }
        return cek;
    }

    public void cleartext(){
        input_addkarantina_activity_idternak.setText("");
        input_addkarantina_activity_diagnosis.setText("");
        input_addkarantina_activity_perawatan.setText("");
        input_addkarantina_activity_tglpemeriksaan.setText("01 Januari 1970");
        radiogroup_addkarantina_activity_mlaiselesai.clearCheck();
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}

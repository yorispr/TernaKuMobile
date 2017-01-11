package com.fintech.ternaku.TernakPerah.Main.TambahData.PindahTernak;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fintech.ternaku.TernakPerah.Main.NavBar.Ternak.InsertTernak;
import com.fintech.ternaku.Setting.Bluetooth;
import com.fintech.ternaku.UrlList;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PindahTernak extends AppCompatActivity {
    private ArrayAdapter<String> listAdapter;
    ListView itemList;
    Dialog dialog, dialog2;
    AdapterKandangPindahTernak adapter;
    AdapterKawananPindahTernak adapter2;

    boolean isRFID = false;
    private Bluetooth bt;
    public final String TAG = "PindahTernak";
    ArrayAdapter<String> adp;
    boolean kandangclick = false;
    boolean kawananclick = false;
    ArrayList<String> namaList = new ArrayList<String>();
    List<ModelTernakPindahTernak> TernakList  = new ArrayList<ModelTernakPindahTernak>();
    ArrayList<ModelKandangPindahTernak> KandangList  = new ArrayList<ModelKandangPindahTernak>();
    ArrayList<ModelKawananPindahTernak> KawananList  = new ArrayList<ModelKawananPindahTernak>();
    int flag_kandang=0;
    int flag_kawanan=0;

    private TextView input_pindahternak_activity_iddetail,input_pindahternak_activity_namadetail,
            input_pindahternak_activity_breed,input_pindahternak_activity_namakandang,
            input_pindahternak_activity_idkandang,input_pindahternak_activity_namakawanan,
            input_pindahternak_activity_idkawanan;
    private Button button_pindahternak_activity_hapus, button_pindahternak_activity_simpan;
    private EditText input_pindahternak_activity_kawanan,input_pindahternak_activity_kandang;
    LinearLayout linearLayout_pindahternak_activity_informsapi;
    int choosenindex;
    int choosenindexkandang;
    int choosenindexkawanan;
    String idpeternakan;
    AutoCompleteTextView input_pindahternak_activity_idternak;
String id_sapi="";
    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pindah_ternak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                    | ActionBar.DISPLAY_SHOW_TITLE
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pindah Ternak");


            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_pindahternak)));


            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }




        //Initial----------------------------------------------------
        idpeternakan =  getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan",null);
        choosenindex=-1;
        choosenindexkandang = -1;
        choosenindexkawanan = -1;


        //Detail Information-----------------------------------------------------------
        input_pindahternak_activity_iddetail = (TextView)findViewById(R.id.input_pindahternak_activity_iddetail);
        input_pindahternak_activity_namadetail = (TextView)findViewById(R.id.input_pindahternak_activity_namadetail);
        input_pindahternak_activity_breed = (TextView)findViewById(R.id.input_pindahternak_activity_breed);
        input_pindahternak_activity_namakandang = (TextView)findViewById(R.id.input_pindahternak_activity_namakandang);
        input_pindahternak_activity_idkandang = (TextView)findViewById(R.id.input_pindahternak_activity_idkandang);
        input_pindahternak_activity_namakawanan = (TextView)findViewById(R.id.input_pindahternak_activity_namakawanan);
        input_pindahternak_activity_idkawanan = (TextView)findViewById(R.id.input_pindahternak_activity_idkawanan);

        //Spinner Initial---------------------------------------------------------------
        input_pindahternak_activity_kandang = (EditText) findViewById(R.id.input_pindahternak_activity_kandang);
        input_pindahternak_activity_kawanan = (EditText) findViewById(R.id.input_pindahternak_activity_kawanan);
        input_pindahternak_activity_kawanan.setEnabled(false);
        input_pindahternak_activity_kandang.setEnabled(false);
        input_pindahternak_activity_kawanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
                    new GetKawanan().execute(url.getUrl_GetKawanan(), urlParameters);
                    kawananclick = true;

            }
        });
        input_pindahternak_activity_kandang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
                    new GetKandang().execute(url.getUrl_GetKandang(), urlParameters);

            }
        });
        linearLayout_pindahternak_activity_informsapi = (LinearLayout) findViewById(R.id.linearLayout_pindahternak_activity_informsapi);
        linearLayout_pindahternak_activity_informsapi.setVisibility(View.GONE);




        button_pindahternak_activity_hapus = (Button)findViewById(R.id.button_pindahternak_activity_hapus);
        button_pindahternak_activity_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout_pindahternak_activity_informsapi.setVisibility(View.GONE);
                input_pindahternak_activity_kandang.setText("");
                input_pindahternak_activity_kawanan.setText("");
                choosenindex = -1;
            }
        });

        button_pindahternak_activity_simpan = (Button)findViewById(R.id.button_pindahternak_activity_simpan);
        button_pindahternak_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForm()){
                    new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.WARNING_TYPE)
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
                                    urlParameters2 = "id=" + input_pindahternak_activity_idternak.getText().toString().trim() +
                                            "&idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null);
                                    new CheckRFID().execute(url.getUrlGet_RFIDanIdCek(), urlParameters2);
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
                    new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Isikan Semua Data")
                            .show();
                }
            }
        });

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.colorPrimary));


        input_pindahternak_activity_idternak=(AutoCompleteTextView) findViewById(R.id.input_pindahternak_activity_idternak);

        input_pindahternak_activity_idternak.setEnabled(false);
        adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,namaList);

        input_pindahternak_activity_idternak.setAdapter(adp);


        input_pindahternak_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(PindahTernak.this,namaList.get(i),Toast.LENGTH_LONG).show();
                String idter = namaList.get(i).trim();
                choosenindex = i;
                setDetailToTextView(idter);
                hideSoftKeyboard(PindahTernak.this);
            }
        });

        input_pindahternak_activity_idternak.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() == 10){
                    isRFID = true;
                    setDetailToTextView(charSequence.toString().trim());
                    Log.d("rfid",charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetAllTernak().execute(url.getUrl_GetTernakPengelompokkan(), urlParameters);

        if(getIntent().getExtras()!=null){
            id_sapi = getIntent().getExtras().getString("id_sapi");
        }


        bt = new Bluetooth(this, mHandler);
        bt.start();
        bt.connectDevice("HC-06");
    }



    @Override
    protected void onResume(){
        super.onResume();
       // bt = new Bluetooth(this, mHandler);
        //bt.stop();
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
                    final String rfid = msg.obj.toString();
                    input_pindahternak_activity_idternak.setText(rfid);
                    Log.d(TAG, "MESSAGE_READ : "+msg.obj.toString());
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

    private void setDetailToTextView(String idTernak) {
        int counter = 0;
        boolean isexist = false;
        for(ModelTernakPindahTernak t : TernakList){
            if(t.getId_ternak().contains(idTernak) || t.getRfid().contains(idTernak))
            {
                linearLayout_pindahternak_activity_informsapi.setVisibility(View.VISIBLE);
                input_pindahternak_activity_iddetail.setText(t.getId_ternak());
                input_pindahternak_activity_namadetail.setText(t.getNama_ternak());
                input_pindahternak_activity_breed.setText(t.getBreed());
                input_pindahternak_activity_namakawanan.setText(t.getKawanan());
                input_pindahternak_activity_namakandang.setText(t.getKandang());
                input_pindahternak_activity_idkawanan.setText(t.getId_kawanan());
                input_pindahternak_activity_idkandang.setText(t.getId_kandang());

                input_pindahternak_activity_kawanan.setText(t.getKawanan());
                input_pindahternak_activity_kandang.setText(t.getKandang());
                input_pindahternak_activity_kandang.setEnabled(true);
                input_pindahternak_activity_kawanan.setEnabled(true);
                if(isRFID) {
                    choosenindex = counter;
                }
                isexist = true;
            }
            counter++;
        }
        if(!isexist){
            new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Peringatan!")
                    .setContentText("RFID Tidak Terdaftar")
                    .show();
            input_pindahternak_activity_idternak.setText("");
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private class GetAllTernak extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.ERROR_TYPE)
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
                new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Gagal Memuat Data")
                        .setContentText("Data Ternak Masih Kosong" + "\nApakah Ingin Memasukkan Data Ternak?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                startActivity(new Intent(PindahTernak.this, InsertTernak.class));
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
            else{
                AddTernakToList(result);
                input_pindahternak_activity_idternak.setEnabled(true);
            }
        }
    }

    //Get Data Kawanan--------------------------------------------------------
    private class GetKawanan extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.ERROR_TYPE)
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
                new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Gagal Memuat Data")
                        .setContentText("Data Kawanan Masih Kosong" + "\nApakah Ingin Memasukkan Data Kawanan?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                startActivity(new Intent(PindahTernak.this, AddKawanan.class));
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
            else{
                AddKawananToList(result);
            }
        }
    }

    //Get Data Kandang--------------------------------------------------------
    private class GetKandang extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.ERROR_TYPE)
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
                new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Gagal Memuat Data")
                        .setContentText("Data Kandang Masih Kosong" + "\nApakah Ingin Memasukkan Data Kandang?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                finish();
                                startActivity(new Intent(PindahTernak.this, AddKandang.class));
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

    private class CheckRFID extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("RES_Insert",result);
            pDialog.dismiss();
            if(result.trim().equals("1")) {
                //Insert To Database------------------------------------------------
                String idternak = TernakList.get(choosenindex).getId_ternak();
                String idkawanan= input_pindahternak_activity_namakawanan.getText().toString();
                String idkandang= input_pindahternak_activity_namakandang.getText().toString();

                if(choosenindexkawanan != -1) {
                    idkawanan = KawananList.get(choosenindexkawanan).getId_kawanan();
                    Log.d("res"," index kawanan != -1  choosenindex = " + String.valueOf(choosenindex) );

                }else if (choosenindexkawanan == -1){
                    idkawanan = TernakList.get(choosenindex).getId_kawanan();
                    Log.d("res"," index kawanan == -1  choosenindex = " + String.valueOf(choosenindex) );

                }
                if(choosenindexkandang != -1) {
                    idkandang = KandangList.get(choosenindexkandang).getId_kandang();
                    Log.d("res"," index kandang != -1 choosenindex = " + String.valueOf(choosenindex));

                }else if(choosenindexkandang == -1){
                    idkandang = TernakList.get(choosenindex).getId_kandang();
                    Log.d("res"," index kandang == -1 choosenindex = " + String.valueOf(choosenindex));

                }
                String status = "Aktif";
                String urlParameters = "idternak="+idternak
                        +"&idpeternakan="+idpeternakan
                        +"&idkawanan="+idkawanan
                        +"&idkandang="+idkandang
                        +"&statusaktif="+status;
                new InsertPengelompokkanTask().execute(url.getUrl_InsertPindahTernak(), urlParameters);
                Log.d("param",urlParameters);
            }else{
                new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("RFID Sudah Terpakai atau Tidak Ada RFID Ditemukan")
                        .show();
            }
        }
    }

    private class InsertPengelompokkanTask extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.PROGRESS_TYPE);

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
                input_pindahternak_activity_namakandang.setText(input_pindahternak_activity_kandang.getText().toString());
                input_pindahternak_activity_namakawanan.setText(input_pindahternak_activity_kawanan.getText().toString());
                //input_pindahternak_activity_idkandang.setText(KandangList.get(choosenindexkandang).getId_kandang());
                //input_pindahternak_activity_idkawanan.setText(KawananList.get(choosenindexkawanan).getId_kawanan());

                if(choosenindexkandang != -1) {
                    TernakList.get(choosenindex).setKandang(KandangList.get(choosenindexkandang).getNama_kandang());
                    TernakList.get(choosenindex).setId_kandang(KandangList.get(choosenindexkandang).getId_kandang());
                    Log.d("choosenindexkandang", String.valueOf(choosenindexkandang));
                }
                if(choosenindexkawanan != -1) {
                    TernakList.get(choosenindex).setKawanan(KawananList.get(choosenindexkawanan).getNama_kawanan());
                    TernakList.get(choosenindex).setId_kawanan(KawananList.get(choosenindexkawanan).getId_kawanan());
                    Log.d("choosenindexkawanan", String.valueOf(choosenindexkawanan));
                }

                setDetailToTextView(input_pindahternak_activity_idternak.getText().toString());
                new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Pindah Ternak")
                                        .setContentText("Apakah Ingin Memindah Ternak Lagi?")
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
                new SweetAlertDialog(PindahTernak.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Penambahan Gagal!")
                        .setContentText("Silahkan Simpan Data Kembali")
                        .show();
            }
        }
    }

    private void AddKandangToList(String result) {
        KandangList.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);

                if(jObj.getString("STATUS_AKTIF").trim().equalsIgnoreCase("Aktif")) {
                    ModelKandangPindahTernak k = new ModelKandangPindahTernak();
                    k.setId_kandang(jObj.getString("ID_KANDANG"));
                    k.setId_peternakan(jObj.getString("ID_PETERNAKAN"));
                    k.setKapasitas_kandang(jObj.getInt("KAPASITAS"));
                    k.setLokasi_kandang(jObj.getString("LOKASI"));
                    k.setNama_kandang(jObj.getString("NAMA_KANDANG"));
                    k.setStatus_aktif(jObj.getString("STATUS_AKTIF"));
                    KandangList.add(k);
                }
            }
            ShowDialogKandang(KandangList);
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private void AddKawananToList(String result) {
        KawananList.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);

                ModelKawananPindahTernak k = new ModelKawananPindahTernak();
                k.setId_kawanan(jObj.getString("ID_KAWANAN"));
                k.setId_peternakan(jObj.getString("ID_PETERNAKAN"));
                k.setNama_kawanan(jObj.getString("NAMA_KAWANAN"));
                k.setUmur(jObj.getString("UMUR"));
                k.setKeterangan(jObj.getString("KETERANGAN"));

                KawananList.add(k);
            }

            ShowDialogKawanan(KawananList);
        }

        catch (JSONException e){e.printStackTrace();}
    }

    private void AddTernakToList(String result) {
        TernakList.clear();
        namaList.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                ModelTernakPindahTernak t = new ModelTernakPindahTernak();

                t.setId_ternak(jObj.getString("id_ternak"));
                t.setNama_ternak(jObj.getString("nama_ternak"));
                t.setBreed(jObj.getString("breed"));
                t.setKandang(jObj.getString("nama_kandang"));
                t.setKawanan(jObj.getString("nama_kawanan"));
                t.setId_kandang(jObj.getString("id_kandang"));
                t.setId_kawanan(jObj.getString("id_kawanan"));
                t.setRfid(jObj.getString("rfid_code"));

                namaList.add(jObj.getString("id_ternak"));
                //Log.d("RESP",jObj.getString("NAMA_KAWANAN"));

                TernakList.add(t);
            }

            setDetailToTextView(id_sapi);

        }
        catch (JSONException e){e.printStackTrace();}
    }

    private void ShowDialogKawanan(final ArrayList<ModelKawananPindahTernak> kawanan) {
        dialog = new Dialog(PindahTernak.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.list_kawanan_pindah_ternak);
        ListView list = (ListView) dialog.findViewById(R.id.list_kawanan_dial);
        EditText edtsearch = (EditText)dialog.findViewById(R.id.edtSearchDialogKawanan);

        Button btnbtl = (Button)dialog.findViewById(R.id.btnbtl);

        btnbtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        adapter2 = new AdapterKawananPindahTernak(getApplicationContext(),kawanan);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosenindexkawanan = i;
                input_pindahternak_activity_kawanan.setText(KawananList.get(choosenindexkawanan).getNama_kawanan());
                Log.d("kawanan",kawanan.get(choosenindexkawanan).getNama_kawanan()+" index: "+String.valueOf(choosenindexkawanan));
                hideSoftKeyboard(PindahTernak.this);
                dialog.dismiss();
            }
        });
        list.setAdapter(adapter2);

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter2.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        dialog.show();
    }

    private void ShowDialogKandang(final ArrayList<ModelKandangPindahTernak> kandang) {
        dialog2 = new Dialog(PindahTernak.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.list_kandang_pindah_ternak);

        ListView list = (ListView) dialog2.findViewById(R.id.list_kandang_dial);
        EditText edtsearch = (EditText)dialog2.findViewById(R.id.edtSearchDialogKandang);
        Button btnbtl = (Button)dialog2.findViewById(R.id.btnbtl);

        btnbtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });

        adapter = new AdapterKandangPindahTernak(getApplicationContext(),kandang);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosenindexkandang = i;
                input_pindahternak_activity_kandang.setText(kandang.get(choosenindexkandang).getNama_kandang());
                hideSoftKeyboard(PindahTernak.this);
                dialog2.dismiss();
            }
        });

        list.setAdapter(adapter);

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //InsTernakToKandangActivity.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        dialog2.show();
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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private boolean checkForm() {
        boolean value = true;
        if(TextUtils.isEmpty(input_pindahternak_activity_idternak.getText().toString())){
            value= false;
            input_pindahternak_activity_idternak.setError("Isikan Id Ternak");
        }
        if(TextUtils.isEmpty(input_pindahternak_activity_kandang.getText().toString())){
            value= false;
            input_pindahternak_activity_kandang.setError("Pilih Data Kandang");
        }
        if(TextUtils.isEmpty(input_pindahternak_activity_kawanan.getText().toString())){
            value= false;
            input_pindahternak_activity_kawanan.setError("Pilih Data Kawanan");
        }


        return value;
    }

    public void cleartext(){
        input_pindahternak_activity_idternak.setText("");
        input_pindahternak_activity_kandang.setText("");
        input_pindahternak_activity_kawanan.setText("");
        input_pindahternak_activity_idternak.setHint("Ketikan ID Sapi atau Scan RFID");
        input_pindahternak_activity_kandang.setHint("Pilih Kandang");
        input_pindahternak_activity_kawanan.setHint("Pilih Kawanan");
        linearLayout_pindahternak_activity_informsapi.setVisibility(View.GONE);
    }

    /*
    @Override
    protected void onPause(){
        super.onPause();
        choosenindex = -1;
        choosenindexkandang = -1;
        choosenindexkawanan = -1;
    }*/
}

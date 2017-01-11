package com.fintech.ternaku.TernakPerah.Main.TambahData.PindahTernak;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.UrlList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddKawanan extends AppCompatActivity {
    private EditText input_addkawanan_activity_nama,input_addkawanan_activity_keterangan,
            input_addkawanan_activity_umur;
    private Button button_addkawanan_activity_simpan;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kawanan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Kawanan Baru");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_pindahternak)));

        }


        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        input_addkawanan_activity_nama = (EditText)findViewById(R.id.input_addkawanan_activity_nama);
        input_addkawanan_activity_keterangan = (EditText)findViewById(R.id.input_addkawanan_activity_keterangan);
        input_addkawanan_activity_umur = (EditText)findViewById(R.id.input_addkawanan_activity_umur);
        button_addkawanan_activity_simpan = (Button)findViewById(R.id.button_addkawanan_activity_simpan);
        button_addkawanan_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForm()){
                    new SweetAlertDialog(AddKawanan.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Simpan")
                            .setContentText("Data Yang Dimasukkan Sudah Benar?")
                            .setConfirmText("Ya")
                            .setCancelText("Tidak")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                    String umurtemp = "0";
                                    if(!input_addkawanan_activity_umur.getText().toString().equalsIgnoreCase(""))
                                    {
                                        umurtemp =  input_addkawanan_activity_umur.getText().toString();
                                    }
                                    String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null)
                                            +"&namakawanan=" + input_addkawanan_activity_nama.getText().toString()
                                            +"&umur=" + umurtemp
                                            +"&keterangan=" + input_addkawanan_activity_keterangan.getText().toString();

                                    new InsertKawananTask().execute(url.getUrl_InsertKawanan(), urlParameters);
                                    Log.d("param",urlParameters);
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
                    new SweetAlertDialog(AddKawanan.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Isikan Semua Data")
                            .show();
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private class InsertKawananTask extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddKawanan.this, SweetAlertDialog.PROGRESS_TYPE);

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
            if (result.trim().equals("1")){
                new SweetAlertDialog(AddKawanan.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(AddKawanan.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Kawanan")
                                        .setContentText("Apakah Ingin Menambah Data Kawanan Lagi?")
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
                new SweetAlertDialog(AddKawanan.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Penambahan Gagal!")
                        .setContentText("Silahkan Simpan Data Kembali")
                        .show();
            }
        }
    }

    private boolean checkForm()
    {
        boolean cek = true;

        if(input_addkawanan_activity_nama.getText().toString().matches(""))
        {input_addkawanan_activity_nama.setError("Nama Kawanan Belum Diisi");cek = false;}
        if(input_addkawanan_activity_keterangan.getText().toString().matches(""))
        {input_addkawanan_activity_keterangan.setError("Keterangan Belum Diisi");cek = false;}

        return cek;
    }

    public void cleartext(){
        input_addkawanan_activity_nama.setText("");
        input_addkawanan_activity_keterangan.setText("");
        input_addkawanan_activity_umur.setText("");
        input_addkawanan_activity_nama.setHint("Masukkan Nama Kawanan");
        input_addkawanan_activity_keterangan.setHint("Masukkan Keterangan Kawanan");
        input_addkawanan_activity_umur.setHint("Masukkan Umur Kawanan");
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

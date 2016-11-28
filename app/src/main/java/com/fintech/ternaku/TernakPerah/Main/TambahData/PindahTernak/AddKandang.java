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

public class AddKandang extends AppCompatActivity {
    private EditText input_addkandang_activity_nama,input_addkandang_activity_lokasi,
            input_addkandang_activity_kapasitas;
    private Button button_addkandang_activity_simpan;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kandang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Kandang Baru");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_pindahternak)));

        }


        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        input_addkandang_activity_nama = (EditText)findViewById(R.id.input_addkandang_activity_nama);
        input_addkandang_activity_lokasi = (EditText)findViewById(R.id.input_addkandang_activity_lokasi);
        input_addkandang_activity_kapasitas = (EditText)findViewById(R.id.input_addkandang_activity_kapasitas);
        button_addkandang_activity_simpan = (Button)findViewById(R.id.button_addkandang_activity_simpan);
        button_addkandang_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForm()){
                    new SweetAlertDialog(AddKandang.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Simpan")
                            .setContentText("Data Yang Dimasukkan Sudah Benar?")
                            .setConfirmText("Ya")
                            .setCancelText("Tidak")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                    String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null)
                                            +"&namakandang=" + input_addkandang_activity_nama.getText().toString()
                                            +"&lokasi=" + input_addkandang_activity_lokasi.getText().toString()
                                            +"&kapasitas=" + input_addkandang_activity_kapasitas.getText().toString()
                                            +"&statusaktif=" + "Aktif";
                                    new InsertKandangTask().execute(url.getUrl_InsertKandang(), urlParameters);
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                }
                            })
                            .show();
                }else
                {
                    new SweetAlertDialog(AddKandang.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Isikan Semua Data")
                            .show();
                }
            }
        });
    }

    private class InsertKandangTask extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddKandang.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddKandang.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(AddKandang.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Kandang")
                                        .setContentText("Apakah Ingin Menambah Data Kandang Lagi?")
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
                new SweetAlertDialog(AddKandang.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Penambahan Gagal!")
                        .setContentText("Silahkan Simpan Data Kembali")
                        .show();
            }
        }
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

    private boolean checkForm()
    {
        boolean cek = true;

        if(input_addkandang_activity_nama.getText().toString().matches(""))
        {input_addkandang_activity_nama.setError("Nama Kandang Belum Diisi");cek = false;}
        if(input_addkandang_activity_lokasi.getText().toString().matches(""))
        {input_addkandang_activity_lokasi.setError("Lokasi Kandang Belum Diisi");cek = false;}
        if(input_addkandang_activity_kapasitas.getText().toString().matches(""))
        {input_addkandang_activity_kapasitas.setError("Kapasitas Kandang Belum Diisi");cek = false;}

        return cek;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
    public void cleartext(){
        input_addkandang_activity_nama.setText("");
        input_addkandang_activity_lokasi.setText("");
        input_addkandang_activity_kapasitas.setText("");
        input_addkandang_activity_nama.setHint("Masukkan Nama Kandang");
        input_addkandang_activity_lokasi.setHint("Masukkan Lokasi Kandang");
        input_addkandang_activity_kapasitas.setHint("Masukkan Kapasitas Kandang");
    }

}

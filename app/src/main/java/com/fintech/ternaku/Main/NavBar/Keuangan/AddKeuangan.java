package com.fintech.ternaku.Main.NavBar.Keuangan;

import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddKeuangan extends AppCompatActivity {
    private EditText input_addpakan_activity_harga,input_addpakan_activity_keterangantrans;
    private Button button_addkeuangan_activity_simpan;
    private TextView input_addpkeuangan_activity_tanggaltransaksi;
    private Spinner spinner_addkeuangan_activity_jenis;
    private RadioGroup radiogroup_addkeuangan_activity_kategori;
    private RadioButton radiobutton_addkeuangan_activity_kategori;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_keuangan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Pengeluaran dan Pemasukan");
        }

        //Set Date-------------------------------------
        input_addpkeuangan_activity_tanggaltransaksi = (TextView) findViewById(R.id.input_addpkeuangan_activity_tanggaltransaksi);
        setDateTimeField();
        input_addpkeuangan_activity_tanggaltransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDatePickerDialog.show();
            }
        });
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addpkeuangan_activity_tanggaltransaksi.setText(dateFormatter.format(Calendar.getInstance().getTime()));

        //Set Jenis Transaksi-----------------------------
        spinner_addkeuangan_activity_jenis = (Spinner)findViewById(R.id.spinner_addkeuangan_activity_jenis);
        final String[] spinner_jenis_transaksi = {"Pembelian Pakan",
                "Pembelian Obat",
                "Pembelian Vaksin",
                "Pembelian Semen",
                "Pemeriksaan Kesehatan Sapi",
                "Pembelian Perlengkapan",
                "Pembelian Ternak",
                "Pembelian Susu",
                "Pembelian Kompos",
                "Lain-lain"};
        ArrayAdapter<String> adapater_jenis_transaksi= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,spinner_jenis_transaksi);
        spinner_addkeuangan_activity_jenis.setAdapter(adapater_jenis_transaksi);

        //Set Jumlah,Radio Button,dan Keterangan-------------------------
        input_addpakan_activity_harga = (EditText) findViewById(R.id.input_addpakan_activity_harga);
        input_addpakan_activity_keterangantrans = (EditText) findViewById(R.id.input_addpakan_activity_keterangantrans);
        radiogroup_addkeuangan_activity_kategori = (RadioGroup) findViewById(R.id.radiogroup_addkeuangan_activity_kategori);

        //Insert To Database--------------------------------
        button_addkeuangan_activity_simpan = (Button) findViewById(R.id.button_addkeuangan_activity_simpan);
        button_addkeuangan_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(AddKeuangan.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Simpan")
                        .setContentText("Data Yang Dimasukkan Sudah Benar?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                int selectedId=radiogroup_addkeuangan_activity_kategori.getCheckedRadioButtonId();
                                radiobutton_addkeuangan_activity_kategori=(RadioButton) findViewById(selectedId);
                                Log.d("LogAll",input_addpkeuangan_activity_tanggaltransaksi.getText().toString().trim()+
                                        spinner_addkeuangan_activity_jenis.getSelectedItem().toString().trim()+
                                        input_addpakan_activity_harga.getText().toString().trim()+
                                        input_addpakan_activity_keterangantrans.getText().toString().trim()+
                                        radiobutton_addkeuangan_activity_kategori.getText().toString().trim());
                                String urlParameters_insert ="uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null)+
                                        "&tglTransaksi=" + input_addpkeuangan_activity_tanggaltransaksi.getText().toString().trim()+
                                        "&jenisTransaksi="+ spinner_addkeuangan_activity_jenis.getSelectedItem().toString().trim()+
                                        "&kategori=" + radiobutton_addkeuangan_activity_kategori.getText().toString().trim()+
                                        "&jumlah="+ input_addpakan_activity_harga.getText().toString().trim()+
                                        "&keterangan="+ input_addpakan_activity_keterangantrans.getText().toString().trim();
                                new insertToDbKeuangan().execute("http://ternaku.com/index.php/C_Keuangan/InsertKeuangan", urlParameters_insert);
                                Log.d("tes",urlParameters_insert);
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

    private class insertToDbKeuangan extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(AddKeuangan.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("InsertKeuangan",s);
            pDialog.dismiss();
            if (s.trim().equals("1")){
                new SweetAlertDialog(AddKeuangan.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan..")
                        .show();
            }
            else if(s.trim().equals("2")){

                new SweetAlertDialog(AddKeuangan.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Penambahan Gagal!")
                        .setContentText("Silahkan Simpan Data Kembali")
                        .show();
            }
            else {
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
            }
        }
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

    private void setDateTimeField() {
        //toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addpkeuangan_activity_tanggaltransaksi.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
}

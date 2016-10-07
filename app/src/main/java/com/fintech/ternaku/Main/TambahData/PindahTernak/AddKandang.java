package com.fintech.ternaku.Main.TambahData.PindahTernak;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;

public class AddKandang extends AppCompatActivity {
    private EditText input_addkandang_activity_nama,input_addkandang_activity_lokasi,
            input_addkandang_activity_kapasitas;
    private Button button_addkandang_activity_simpan;

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
        }

        input_addkandang_activity_nama = (EditText)findViewById(R.id.input_addkandang_activity_nama);
        input_addkandang_activity_lokasi = (EditText)findViewById(R.id.input_addkandang_activity_lokasi);
        input_addkandang_activity_kapasitas = (EditText)findViewById(R.id.input_addkandang_activity_kapasitas);
        button_addkandang_activity_simpan = (Button)findViewById(R.id.button_addkandang_activity_simpan);
        button_addkandang_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForm()){
                    String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null)
                            +"&namakandang=" + input_addkandang_activity_nama.getText().toString()
                            +"&lokasi=" + input_addkandang_activity_lokasi.getText().toString()
                            +"&kapasitas=" + input_addkandang_activity_kapasitas.getText().toString()
                            +"&statusaktif=Aktif";
                    new InsertKandangTask().execute("http://ternaku.com/index.php/C_Ternak/insertKandang", urlParameters);
                }else
                {
                    Toast.makeText(getApplicationContext(),"Ada data yang belum terisi!!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class InsertKandangTask extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(AddKandang.this);
            progDialog.setMessage("Tunggu Sebentar...");
            progDialog.show();
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
            if (result.trim().equals("1")){
                Toast.makeText(getApplication(),"Kandang Berhasil Ditambahkan",Toast.LENGTH_LONG).show();
                progDialog.dismiss();
            }
            else {
                Toast.makeText(getApplication(),"Gagal Menambahkan Kandang",Toast.LENGTH_LONG).show();
                progDialog.dismiss();
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
        else if(input_addkandang_activity_lokasi.getText().toString().matches(""))
        {input_addkandang_activity_lokasi.setError("Lokasi Kandang Belum Diisi");cek = false;}
        else if(input_addkandang_activity_kapasitas.getText().toString().matches(""))
        {input_addkandang_activity_kapasitas.setError("Kapasitas Kandang Belum Diisi");cek = false;}

        return cek;
    }

}

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

public class AddKawanan extends AppCompatActivity {
    private EditText input_addkawanan_activity_nama,input_addkawanan_activity_keterangan,
            input_addkawanan_activity_umur;
    private Button button_addkawanan_activity_simpan;

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
        }

        input_addkawanan_activity_nama = (EditText)findViewById(R.id.input_addkawanan_activity_nama);
        input_addkawanan_activity_keterangan = (EditText)findViewById(R.id.input_addkawanan_activity_keterangan);
        input_addkawanan_activity_umur = (EditText)findViewById(R.id.input_addkawanan_activity_umur);
        button_addkawanan_activity_simpan = (Button)findViewById(R.id.button_addkawanan_activity_simpan);

        button_addkawanan_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForm()){
                    String umurtemp = "0";
                    if(!input_addkawanan_activity_umur.getText().toString().equalsIgnoreCase(""))
                    {
                        umurtemp =  input_addkawanan_activity_umur.getText().toString();
                    }
                    String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null)
                            +"&namakawanan=" + input_addkawanan_activity_nama.getText().toString()
                            +"&umur=" + umurtemp
                            +"&keterangan=" + input_addkawanan_activity_keterangan.getText().toString();

                    new InsertKawananTask().execute("http://ternaku.com/index.php/C_Ternak/insertKawanan", urlParameters);
                    Log.d("param",urlParameters);

                }else{
                    Toast.makeText(getApplicationContext(),"Ada data yang belum terisi!!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class InsertKawananTask extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(AddKawanan.this);
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
                Toast.makeText(getApplication(),"Kawanan Berhasil Ditambahkan",Toast.LENGTH_LONG).show();
                progDialog.dismiss();
            }
            else {
                Toast.makeText(getApplication(),"Gagal Menambahkan Kawanan",Toast.LENGTH_LONG).show();
                progDialog.dismiss();
            }
        }
    }

    private boolean checkForm()
    {
        boolean cek = true;

        if(input_addkawanan_activity_nama.getText().toString().matches(""))
        {input_addkawanan_activity_nama.setError("Nama Kawanan Belum Diisi");cek = false;}
        else if(input_addkawanan_activity_keterangan.getText().toString().matches(""))
        {input_addkawanan_activity_keterangan.setError("Keterangan Belum Diisi");cek = false;}

        return cek;
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

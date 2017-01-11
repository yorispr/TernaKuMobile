package com.fintech.ternaku.Setting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.fintech.ternaku.UrlList;

public class UpdatePeternakActivity extends AppCompatActivity {

    private EditText edtnama,edtalamat,edttelpon;
    private Button btnSimpan;
    String idpeternak,nama,alamat,telpon;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_peternak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Perbarui Peternak");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); fab.hide();

        SharedPreferences preferences = getSharedPreferences(getString(R.string.userpref), 0);

        idpeternak = preferences.getString("keyIdPengguna",null);
        nama = preferences.getString("keyNama",null);
        alamat = preferences.getString("keyAlamat",null);
        telpon = preferences.getString("keyTelpon",null);

        edtnama = (EditText)findViewById(R.id.edtnama);
        edtalamat = (EditText)findViewById(R.id.edtalamat);
        edttelpon = (EditText)findViewById(R.id.edttelepon);

        edtnama.setText(nama);
        edtalamat.setText(alamat);
        edttelpon.setText(telpon);

        btnSimpan = (Button)findViewById(R.id.btnUbah);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(CekForm()) {
                            /*String postnama = edtnama.getText().toString(),
                            postalamat = edtalamat.getText().toString(),
                            posttelpon = edttelpon.getText().toString();*/

                    if (!edtnama.getText().toString().matches("")) {
                        nama = edtnama.getText().toString();
                    }
                    if (!edtalamat.getText().toString().matches("")) {
                        alamat = edtalamat.getText().toString();
                    }
                    if (!edttelpon.getText().toString().matches("")) {
                        telpon = edttelpon.getText().toString();
                    }

                    String urlParameters = "idpeternak=" + idpeternak
                            + "&namalengkap=" + nama
                            + "&alamat=" + alamat
                            + "&telepon=" + telpon;

                    new UpdateProfil().execute(url.getUrl_UpdatePeternak(), urlParameters);
                    Log.d("param", urlParameters);
                }else{
                    Toast.makeText(getApplication(),"Lengkapi Data!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class UpdateProfil extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(UpdatePeternakActivity.this);
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
            progDialog.dismiss();
            if (result.trim().equals("1")){
                Toast.makeText(getApplication(),"Berhasil Merubah Data",Toast.LENGTH_LONG).show();
                updatePreference(edtnama.getText().toString(),edtalamat.getText().toString(),edttelpon.getText().toString());
            }
            else if(result.trim().equals("0")){
                Toast.makeText(getApplication(),"Gagal",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updatePreference(String nama, String alamat, String telpon){
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(!nama.matches("")) {
            editor.putString("keyNama", nama);
        }
        if(!alamat.matches("")) {
            editor.putString("keyAlamat", alamat);
        }
        if(!telpon.matches("")) {
            editor.putString("keyTelpon", telpon);
        }
        editor.apply();
    }

    private boolean CekForm(){
        boolean cek = true;
        if(edtnama.getText().toString().matches("")){
            cek = false;
        }
        if(edtalamat.getText().toString().matches("")){
            cek = false;
        }
        if(edttelpon.getText().toString().matches("")){
            cek = false;
        }

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

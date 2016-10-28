package com.fintech.ternaku.Setting;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePeternakanActivity extends AppCompatActivity {


    private EditText edtnama,edtalamat,edttelpon,edtjumlah;
    private Button btnSimpan;
    String idpeternakan,nama,alamat,telpon, jumlahternak;

    private TextInputLayout inputnama,inputalamat,inputtelpon,inputjumlah;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_peternakan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences preferences = getSharedPreferences(getString(R.string.userpref), 0);

        idpeternakan = preferences.getString("keyIdPeternakan",null);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Profil Peternakan");
        }

        inputnama = (TextInputLayout)findViewById(R.id.inputlayoutnama);
        inputalamat = (TextInputLayout)findViewById(R.id.inputlayoutalamat);
        inputtelpon = (TextInputLayout)findViewById(R.id.inputlayouttelpon);
        inputjumlah = (TextInputLayout)findViewById(R.id.inputlayoutjumlahternak);



        edtnama = (EditText)findViewById(R.id.edtnama);
        edtalamat = (EditText)findViewById(R.id.edtalamat);
        edttelpon = (EditText)findViewById(R.id.edttelepon);
        edtjumlah = (EditText)findViewById(R.id.edtjmlhternak);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });fab.hide();

        String urlParameters = "idpeternakan=" + idpeternakan;
        new GetPeternakan().execute(url.getUrl_GetPeternakanSetting(), urlParameters);

        btnSimpan = (Button)findViewById(R.id.btnUbah);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cekform()) {
                    String urlParameters = "idpeternakan=" + idpeternakan
                            + "&namapeternakan=" + edtnama.getText().toString()
                            + "&alamatpeternakan=" + edtalamat.getText().toString()
                            + "&teleponpeternakan=" + edttelpon.getText().toString()
                            + "&jumlahternak=" + edtjumlah.getText().toString();

                    new UpdatePeternakan().execute(url.getUrl_UpdatePeternakan(), urlParameters);
                }
            }
        });

    }
    private class UpdatePeternakan extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(UpdatePeternakanActivity.this);
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
            }
            else if(result.trim().equals("0")){
                Toast.makeText(getApplication(),"Gagal",Toast.LENGTH_LONG).show();
            }
        }
    }

    private class GetPeternakan extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPreExecute() {

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
                try{
                    JSONArray jArray = new JSONArray(result);
                    for(int i=0;i<jArray.length();i++)
                    {
                        JSONObject jObj = jArray.getJSONObject(i);
                        nama = jObj.getString("NAMA_PETERNAKAN");
                        alamat = jObj.getString("ALAMAT_PETERNAKAN");
                        telpon = jObj.getString("TELPON_PETERNAKAN");
                        jumlahternak = jObj.getString("JUMLAH_TERNAK");
                        edtnama.setText(nama);
                        edtalamat.setText(alamat);
                        edttelpon.setText(telpon);
                        edtjumlah.setText(jumlahternak);
                    }
                }
                catch (JSONException e){e.printStackTrace();}

        }
    }

    public boolean cekform(){
        boolean cek = true;

        if(edtnama.getText().toString().matches("")){
            inputnama.setError("Data tidak boleh kosong");
            cek = false;
        }else {
            inputnama.setError(null);
        }

        if (edtalamat.getText().toString().matches("")){
            inputalamat.setError("Data tidak boleh kosong");
            cek = false;
        }else{
            inputalamat.setError(null);
        }

        if (edttelpon.getText().toString().matches("")){
            inputtelpon.setError("Data tidak boleh kosong");
            cek = false;
        }else{
            inputtelpon.setError(null);
        }

        if (edtjumlah.getText().toString().matches("")){
            inputjumlah.setError("Data tidak boleh kosong");
            cek = false;
        }else{
            inputjumlah.setError(null);
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

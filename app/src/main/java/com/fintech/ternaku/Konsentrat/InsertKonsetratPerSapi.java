package com.fintech.ternaku.Konsentrat;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.Setting.EditTernakActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InsertKonsetratPerSapi extends AppCompatActivity  implements FragmentTernak.OnFragmentInteractionListener{

    private Button btnTambahBahan,btnSimpan;
    private ListView listViewBahan;

    private AdapterBahan adapter;
    FloatingActionButton fab;
    List<ModelBahan> bahanlist = new ArrayList<ModelBahan>();
    FragmentTernak fragmentTernak;
    EditText nama,jumlah,satuan,totalbiaya;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_konsetrat_per_sapi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentTernak = new FragmentTernak();

         fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragmentTernak.isHidden()) {
                    getSupportFragmentManager().beginTransaction().show(fragmentTernak).commit();
                }else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentPlaceHolder, fragmentTernak).commit();
                }
            }
        });

        nama = (EditText)findViewById(R.id.edtBahan);
        jumlah = (EditText)findViewById(R.id.edtJumlah);
        satuan = (EditText)findViewById(R.id.edtSatuan);
        totalbiaya = (EditText)findViewById(R.id.edtTotalBiaya);

        adapter = new AdapterBahan(InsertKonsetratPerSapi.this, R.layout.list_bahan, bahanlist);
        listViewBahan = (ListView)findViewById(R.id.list_bahan);

        listViewBahan.setAdapter(adapter);

        btnTambahBahan = (Button)findViewById(R.id.btnTambahBahan);
        btnTambahBahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelBahan mb = new ModelBahan();
                mb.setNama(nama.getText().toString());
                mb.setHarga(totalbiaya.getText().toString());
                mb.setJumlah(jumlah.getText().toString());
                mb.setSatuan(satuan.getText().toString());
                bahanlist.add(mb);
                adapter.notifyDataSetChanged();
                clearform();
            }
        });

        btnSimpan = (Button)findViewById(R.id.btnSimpanKonsentrat);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> arrayIDTernak = new ArrayList<String>();
                arrayIDTernak = fragmentTernak.getArrayTernak();
                if(arrayIDTernak.size() != 0 && bahanlist.size() !=0) {
                    Gson gson = new GsonBuilder().create();
                    JsonArray arrayBahan = gson.toJsonTree(bahanlist).getAsJsonArray();
                    JsonArray arrayTernak = gson.toJsonTree(arrayIDTernak).getAsJsonArray();

                    String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                            +"&namakonsentrat="+nama.getText().toString().trim()
                            +"&bahan="+arrayBahan
                            +"&idternak="+arrayTernak;

                    Log.d("param konsentrat",param);
                    new InsertKonsentrat().execute("http://developer.ternaku.com/C_HistoryKonsentrat/insertKonsentrat", param);

                }else{
                    Toast.makeText(getApplicationContext(),"Data kosong!",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private class InsertKonsentrat extends AsyncTask<String, Integer, String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(InsertKonsetratPerSapi.this, SweetAlertDialog.PROGRESS_TYPE);

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
            String json = c.GetJSONfromURL(params[0], params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("RES_Insert", result);
            if(result.trim().equals("1")){
                pDialog.dismissWithAnimation();
                new SweetAlertDialog(InsertKonsetratPerSapi.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Sukses!")
                        .setContentText("Data Berhasil Ditambahkan")
                        .show();
                fragmentTernak.setSelesai();
            }else{
                new SweetAlertDialog(InsertKonsetratPerSapi.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Gagal!")
                        .setContentText("Gagal Menambah Konsentrat")
                        .show();
                pDialog.dismissWithAnimation();
            }

        }
    }

    public void clearform(){
        nama.setText("");
        jumlah.setText("");
        satuan.setText("");
        totalbiaya.setText("");
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    public void hideFab(){
        fab.hide();
    }
    public void showFab(){
        fab.show();
    }
    public void hideFragment(){
        getSupportFragmentManager().beginTransaction().hide(fragmentTernak).commit();
    }

    @Override
    public void onResume(){
        super.onResume();
        fab.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            if(!fragmentTernak.isVisible()){
                finish();
                return true;
            }else{
                getSupportFragmentManager().beginTransaction().hide(fragmentTernak).commit();
            }
        }

        return super.onOptionsItemSelected(item);
    }

}

package com.fintech.ternaku.TernakPotong;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.TernakPerah.Main.TambahData.PindahTernak.AddKawanan;
import com.fintech.ternaku.UrlList;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BeratBadanActivity extends AppCompatActivity {
    UrlList url = new UrlList();
    private SearchableSpinner spinner_beratbadan_activity_namakawanan;
    ArrayAdapter<String> myAdapter;
    ArrayList<String> list_beratbadanactivity_kawanan = new ArrayList<String>();
    ArrayList<String> list_beratbadanactivity_idkawanan = new ArrayList<String>();
    int selectedindex = -1;

    boolean isUpdate = false;
    private RadioGroup radiogroup_beratbadan_activity_insertupdate;
    private RadioButton radiobutton_beratbadan_activity_insert,radiobutton_beratbadan_activity_update;


    private LinearLayout layoutKawanan, layoutBatas;
    //view
    private EditText input_beratbadan_activity_overweight,input_beratbadan_activity_underweight,input_beratbadan_activity_bataskenaikan;
    private Button button_beratbadan_activity_simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berat_badan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tambah Batas Berat Badan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_produksi)));
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });fab.hide();


        radiogroup_beratbadan_activity_insertupdate = (RadioGroup)findViewById(R.id.radiogroup_beratbadan_activity_insertupdate);
        radiobutton_beratbadan_activity_insert = (RadioButton)findViewById(R.id.radiobutton_beratbadan_activity_insert);
        radiobutton_beratbadan_activity_update = (RadioButton)findViewById(R.id.radiobutton_beratbadan_activity_update);

        layoutKawanan = (LinearLayout)findViewById(R.id.layoutKawanan);
        layoutBatas = (LinearLayout)findViewById(R.id.layoutBatas);


        input_beratbadan_activity_overweight = (EditText)findViewById(R.id.input_beratbadan_activity_overweight);
        input_beratbadan_activity_underweight = (EditText)findViewById(R.id.input_beratbadan_activity_underweight);
        input_beratbadan_activity_bataskenaikan = (EditText)findViewById(R.id.input_beratbadan_activity_bataskenaikan);

        input_beratbadan_activity_overweight.getBackground().setColorFilter(getResources().getColor(R.color.color_primary_produksi), PorterDuff.Mode.SRC_IN);
        input_beratbadan_activity_underweight.getBackground().setColorFilter(getResources().getColor(R.color.color_primary_produksi), PorterDuff.Mode.SRC_IN);
        input_beratbadan_activity_bataskenaikan.getBackground().setColorFilter(getResources().getColor(R.color.color_primary_produksi), PorterDuff.Mode.SRC_IN);


        button_beratbadan_activity_simpan = (Button)findViewById(R.id.button_beratbadan_activity_simpan);

        spinner_beratbadan_activity_namakawanan = (SearchableSpinner)findViewById(R.id.spinner_beratbadan_activity_namakawanan);
        myAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,list_beratbadanactivity_kawanan);
        spinner_beratbadan_activity_namakawanan.setAdapter(myAdapter);
        spinner_beratbadan_activity_namakawanan.setTitle("Pilih Kawanan");
        spinner_beratbadan_activity_namakawanan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedindex = i;

                if(isUpdate){
                    if(selectedindex !=- 1) {
                        String urlParameters2 =
                                "&idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                                        + "&idkawanan=" + list_beratbadanactivity_idkawanan.get(selectedindex);
                        new GetDataBB().execute(url.getUrl_GetBatasBBByID(), urlParameters2);
                        Log.d("paramupdate",urlParameters2);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button_beratbadan_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {
                    if(selectedindex==-1 || selectedindex == 0){
                        new SweetAlertDialog(BeratBadanActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Silahkan Pilih Kawanan")
                                .show();
                    }else{
                        new SweetAlertDialog(BeratBadanActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Simpan")
                                .setContentText("Data Yang Dimasukkan Sudah Benar?")
                                .setConfirmText("Ya")
                                .setCancelText("Tidak")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        String urlParameters2;
                                        urlParameters2 =
                                                "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                                                +"&idkawanan=" + list_beratbadanactivity_idkawanan.get(selectedindex)
                                                +"&batasoverweight="+input_beratbadan_activity_overweight.getText().toString()
                                                +"&batasunderweight="+input_beratbadan_activity_underweight.getText().toString()
                                                +"&bataskenaikan="+input_beratbadan_activity_bataskenaikan.getText().toString();
                                        Log.d("param",urlParameters2);

                                        if(!isUpdate) {
                                            new AddBatasBerat().execute(url.getUrlInsertBB(), urlParameters2);
                                        }else{
                                            new AddBatasBerat().execute(url.getUrlUpdatetBB(), urlParameters2);
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
                    }
                }

                else {
                    new SweetAlertDialog(BeratBadanActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Isikan Semua Data")
                            .show();
                }
            }
        });

        radiogroup_beratbadan_activity_insertupdate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radiobutton_beratbadan_activity_insert) {
                    clearInput();
                    setEnable(true);
                    isUpdate = false;
                    String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                            +"&fungsi=insert";
                    new GetKawanan().execute(url.getUrl_GetKawananBB(), urlParameters);
                } else if(checkedId == R.id.radiobutton_beratbadan_activity_update) {
                    setEnable(true);
                    isUpdate = true;
                    String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                            +"&fungsi=update";
                    new GetKawanan().execute(url.getUrl_GetKawananBB(), urlParameters);
                }


            }
        });
        setEnable(false);
    }
    private boolean checkForm() {
        boolean value = true;
        if(TextUtils.isEmpty(input_beratbadan_activity_overweight.getText().toString())){
            value= false;
            input_beratbadan_activity_overweight.setError("Isikan Batas Atas");
        }
        if(TextUtils.isEmpty(input_beratbadan_activity_underweight.getText().toString())){
            value= false;
            input_beratbadan_activity_underweight.setError("Isikan Batas Bawah");
        }
        if(TextUtils.isEmpty(input_beratbadan_activity_bataskenaikan.getText().toString())){
            value= false;
            input_beratbadan_activity_bataskenaikan.setError("Isikan Batas Kenaikan");
        }


        return value;
    }

    private void clearInput(){
        input_beratbadan_activity_underweight.setText("");
        input_beratbadan_activity_underweight.setHint("0");

        input_beratbadan_activity_overweight.setText("");
        input_beratbadan_activity_overweight.setHint("0");

        input_beratbadan_activity_bataskenaikan.setText("");
        input_beratbadan_activity_bataskenaikan.setHint("0");
    }

    private void setEnable(boolean b){
        spinner_beratbadan_activity_namakawanan.setEnabled(b);
        input_beratbadan_activity_bataskenaikan.setEnabled(b);
        input_beratbadan_activity_overweight.setEnabled(b);
        input_beratbadan_activity_underweight.setEnabled(b);
    }

    private class GetKawanan extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(BeratBadanActivity.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(BeratBadanActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Koneksi Terputus!")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }else if (result.trim().equals("404")){
                new SweetAlertDialog(BeratBadanActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Gagal Memuat Data")
                        .setContentText("Data Kawanan Masih Kosong" + "\nApakah Ingin Memasukkan Data Kawanan?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                startActivity(new Intent(BeratBadanActivity.this, AddKawanan.class));
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
                addKawananToList(result);
            }
        }
    }

    private class GetDataBB extends AsyncTask<String,Integer,String> {
        ProgressDialog pDialog = new ProgressDialog(BeratBadanActivity.this);
        @Override
        protected void onPreExecute() {
            pDialog.show();
            pDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0],params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("result",result);
            if(!result.trim().equalsIgnoreCase("404")) {
                try {
                    JSONObject jobj = new JSONObject(result);
                    input_beratbadan_activity_overweight.setText(jobj.getString("batas_overweight"));
                    input_beratbadan_activity_underweight.setText(jobj.getString("batas_underweight"));
                    input_beratbadan_activity_bataskenaikan.setText(jobj.getString("batas_kenaikan"));
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                clearInput();
                pDialog.dismiss();
            }
        }
    }

    private class AddBatasBerat extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(BeratBadanActivity.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(BeratBadanActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(BeratBadanActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Batas Berat")
                                        .setContentText("Apakah Ingin Menambah Batas Berat Lagi?")
                                        .setConfirmText("Ya")
                                        .setCancelText("Tidak")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
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

    private void addKawananToList(String result){
        list_beratbadanactivity_kawanan.clear();
        list_beratbadanactivity_idkawanan.clear();


        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            list_beratbadanactivity_idkawanan.add("0");
            list_beratbadanactivity_kawanan.add("--- Pilih Kawanan ---");
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                    list_beratbadanactivity_idkawanan.add(jObj.getString("ID_KAWANAN"));
                    list_beratbadanactivity_kawanan.add(jObj.getString("NAMA_KAWANAN"));
            }
            myAdapter.notifyDataSetChanged();
        }
        catch (JSONException e){e.printStackTrace();}
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

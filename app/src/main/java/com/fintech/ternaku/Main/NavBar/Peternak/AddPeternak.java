package com.fintech.ternaku.Main.NavBar.Peternak;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddPeternak extends AppCompatActivity {
    private EditText input_addpeternak_activity_namaternak,input_addpeternak_activity_alamat,
    input_addpeternak_activity_notelp;
    private Spinner spinner_addpeternak_activity_jabatan;
    private Button button_addpeternak_activity_simpan;
    String u,p;

    ArrayAdapter<String> myAdapter;
    ArrayList<String> spinner_addpeternak_jabatan = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_peternak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Peternak");
        }

        //Set All EditText-----------------------------------------------
        input_addpeternak_activity_namaternak = (EditText)findViewById(R.id.input_addpeternak_activity_namaternak);
        input_addpeternak_activity_alamat = (EditText)findViewById(R.id.input_addpeternak_activity_alamat);
        input_addpeternak_activity_notelp = (EditText)findViewById(R.id.input_addpeternak_activity_notelp);

        //Set Spinner----------------------------------------------------
        spinner_addpeternak_activity_jabatan = (Spinner)findViewById(R.id.spinner_addpeternak_activity_jabatan);
        String urlParameters = "";
        new GetJabatan().execute("http://ternaku.com/index.php/C_Pengguna/getListRole", urlParameters);
        myAdapter= new ArrayAdapter<String> (AddPeternak.this, android.R.layout.simple_spinner_dropdown_item,spinner_addpeternak_jabatan);
        spinner_addpeternak_activity_jabatan.setAdapter(myAdapter);

        //Insert To Database----------------------------------------------
        button_addpeternak_activity_simpan = (Button)findViewById(R.id.button_addpeternak_activity_simpan);
        button_addpeternak_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = input_addpeternak_activity_namaternak.getText().toString();
                Random rand;
                int randomNumber = (int)(Math.random()*9000)+1000;
                String output ="";
                if(username.contains(" ")) {
                    output = username.substring(0, username.indexOf(" "));
                }else{
                    output = username;
                }
                output += randomNumber;
                final String temp_output = output;
                new SweetAlertDialog(AddPeternak.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Simpan")
                        .setContentText("Data Yang Dimasukkan Sudah Benar?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                String username = input_addpeternak_activity_namaternak.getText().toString();
                                Random rand;
                                int randomNumber = (int)(Math.random()*9000)+1000;
                                String output ="";
                                if(username.contains(" ")) {
                                    output = username.substring(0, username.indexOf(" "));
                                }else{
                                    output = username;
                                }
                                output += randomNumber;
                                String pass = generateHash(input_addpeternak_activity_notelp.getText().toString()+output);
                                String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                                        + "&namarole="+spinner_addpeternak_activity_jabatan.getSelectedItem().toString()
                                        + "&namalengkap="+input_addpeternak_activity_namaternak.getText().toString()
                                        + "&alamat="+input_addpeternak_activity_alamat.getText().toString()
                                        + "&telepon="+input_addpeternak_activity_notelp.getText().toString()
                                        + "&username="+output
                                        + "&password="+pass;

                                u = output;
                                p = input_addpeternak_activity_notelp.getText().toString();
                                Log.d("data",urlParameters);
                                new InsertPeternak().execute("http://ternaku.com/index.php/C_Pengguna/insertPeternak", urlParameters);
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

    public String generateHash(String password) {
        //String toHash = "someRandomString";
        MessageDigest md = null;
        byte[] hash = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            hash = md.digest(password.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return convertToHex(hash);
    }

    private String convertToHex(byte[] raw) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < raw.length; i++) {
            sb.append(Integer.toString((raw[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    //Get Jabatan Spinner-------------------------------------------
    private class GetJabatan extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddPeternak.this, SweetAlertDialog.PROGRESS_TYPE);

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
            if (result.trim().equals("404")){
                pDialog.dismiss();
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
            }
            else {
                try{
                    pDialog.dismiss();
                    JSONArray jArray = new JSONArray(result);
                    for(int i=0;i<jArray.length();i++)
                    {
                        JSONObject jObj = jArray.getJSONObject(i);
                        if(!jObj.getString("ID_ROLE").equalsIgnoreCase("RL-1")) {
                            spinner_addpeternak_jabatan.add(jObj.getString("NAMA_ROLE"));
                        }
                    }
                    myAdapter.notifyDataSetChanged();
                }
                catch (JSONException e){e.printStackTrace();}

            }
        }
    }

    //Insert To Database-------------------------------------------
    private class InsertPeternak extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddPeternak.this, SweetAlertDialog.PROGRESS_TYPE);

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
                showCredential(u,p);
                cleartext();
            }
            else if(result.trim().equals("2")){

                new SweetAlertDialog(AddPeternak.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Penambahan Gagal!")
                        .setContentText("Silahkan Upgrade Layanan Untuk Menambah User Baru..")
                        .show();
            }
            else {
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showCredential(String username, String pass){
        //Set Alert-------------------------------------------------------------
        new SweetAlertDialog(AddPeternak.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Berhasil!")
                .setContentText("User Baru Sudah Ditambahkan")
                .show();

        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddPeternak.this);
        builder1.setTitle("Detail Login");
        builder1.setMessage("Data pengguna : "+"\n Username : "+username+"\n Password : "+pass+"\n\n* Segera catat username dan password!");
        builder1.setCancelable(true);
        final String un = username, pas = pass;
        builder1.setPositiveButton(
                "Copy data",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Data login", "Username : "+un+"\n Password : "+pas);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getApplicationContext(),"Data sudah disalin",Toast.LENGTH_LONG).show();
                    }
                });

        builder1.setNegativeButton(
                "Tutup",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
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

    public void cleartext(){
        input_addpeternak_activity_namaternak.setText("");
        input_addpeternak_activity_alamat.setText("");
        input_addpeternak_activity_notelp.setText("");
        input_addpeternak_activity_namaternak.setHint("Masukkan Nama Peternak");
        input_addpeternak_activity_alamat.setHint("Alamat");
        input_addpeternak_activity_notelp.setHint("Nomor Telepon");
    }
}

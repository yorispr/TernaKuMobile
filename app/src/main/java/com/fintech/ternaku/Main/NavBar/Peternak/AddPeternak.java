package com.fintech.ternaku.Main.NavBar.Peternak;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class AddPeternak extends AppCompatActivity {
    private EditText input_addpeternak_activity_namaternak,input_addpeternak_activity_alamat,
    input_addpeternak_activity_notelp;
    private Spinner spinner_addpeternak_activity_jabatan;
    private Button button_addpeternak_activity_simpan;

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
            actionbar.setTitle("Add Peternak Baru");
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
                String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null)
                        + "&namarole="+spinner_addpeternak_activity_jabatan.getSelectedItem().toString()
                        + "&namalengkap="+input_addpeternak_activity_namaternak.getText().toString()
                        + "&alamat="+input_addpeternak_activity_alamat.getText().toString()
                        + "&telepon="+input_addpeternak_activity_notelp.getText().toString()
                        + "&username="+output
                        + "&password="+generateHash(input_addpeternak_activity_notelp.getText().toString()+output);

                Log.d("data",urlParameters);
                new InsertPeternak().execute("http://ternaku.com/index.php/C_Pengguna/insertPeternak", urlParameters);

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
        ProgressDialog progDialog;

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
            if (result.trim().equals("404")){
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
            }
            else {
                try{

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
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(AddPeternak.this);
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
                Toast.makeText(getApplication(),"Berhasil Menambah Data",Toast.LENGTH_LONG).show();
            }
            else if(result.trim().equals("2")){

                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddPeternak.this);
                builder1.setTitle("Peringatan");
                builder1.setMessage("Silahkan upgrade layanan untuk menambah pengguna baru!");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Tambah Layanan",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Nanti",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            else {
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
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
}

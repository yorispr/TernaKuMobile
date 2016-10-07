package com.fintech.ternaku.LoginAndRegister;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.LoginAndRegister.RegisterActivity;
import com.fintech.ternaku.Main.MainActivity;
import com.fintech.ternaku.Main.NavBar.Keuangan.AddKeuangan;
import com.fintech.ternaku.Main.NavBar.Pakan.AddPakanTernak;
import com.fintech.ternaku.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {
    private EditText input_login_activity_username,input_login_activity_password;
    private Button button_login_activity_masuk,button_login_activity_linktoregister;
    private int attempt = 0;

    private SharedPreferences shr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shr = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE);
        if(!shr.contains("keyUsername") || shr.getString("keyUsername",null)==null) {
            setContentView(R.layout.activity_login);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            input_login_activity_username = (EditText) findViewById(R.id.input_login_activity_username);
            input_login_activity_password = (EditText) findViewById(R.id.input_login_activity_password);
            button_login_activity_masuk = (Button) findViewById(R.id.button_login_activity_masuk);
            button_login_activity_linktoregister = (Button) findViewById(R.id.button_login_activity_linktoregister);

            button_login_activity_linktoregister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(i);
                }
            });

            button_login_activity_masuk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String password = generateHash(input_login_activity_password.getText().toString()+input_login_activity_username.getText().toString());
                        String urlParameters = "username=" + URLEncoder.encode(input_login_activity_username.getText().toString(), "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8");
                        new LoginTask().execute("http://ternaku.com/index.php/C_Pengguna/cekLogin", urlParameters);
                        Log.d("HASH",generateHash(input_login_activity_password.getText().toString()+input_login_activity_username.getText().toString()));

                    } catch (UnsupportedEncodingException u) {
                        u.printStackTrace();
                    }
                }
            });
        }
        else{
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }
    private class LoginTask extends AsyncTask<String, Integer, String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);

        @Override
        protected void onPreExecute(){
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#fa6900"));
            pDialog.setTitleText("Tunggu Sebentar");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(urls[0],urls[1]);
            return json;
        }

        protected void onPostExecute(String result) {
            Log.d("RES",result);
            pDialog.dismiss();
            if (result.trim().equals("1")) {
                if(attempt < 3)
                {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Password Yang Anda Masukkan Salah")
                            .show();
                    attempt++;
                    pDialog.dismiss();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });
                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setTitle("Peringatan");
                    dialog.setMessage("Apakah anda ingin menggunakan fitur lupa password?");
                    pDialog.dismiss();
                    dialog.show();
                    attempt = 0;
                }
            }
            else if(result.trim().equals("2")){
                Toast.makeText(getApplication(),"Username dan password belum terdaftar",Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
            else{
                JSONArray jArray;
                JSONObject jObj;
                try {
                    jArray  = new JSONArray(result);
                    jObj = jArray.getJSONObject(0);
                    if(jObj.has("ID_PENGGUNA"))
                    {
                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                        savetoLocal(result);
                        pDialog.dismiss();
                        finish();
                    }

                }catch(JSONException e){
                    Toast.makeText(getApplication(),"Terjadi Kesalahan..",Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                }


            }
        }

    }

    private void savetoLocal(String user)
    {
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE);
        try {
            JSONArray jArray = new JSONArray(user);
            JSONObject jObj = jArray.getJSONObject(0);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("keyNama",jObj.get("NAMA").toString());
            editor.putString("keyRole",jObj.get("ID_ROLE").toString());
            editor.putString("keyNamaRole",jObj.get("NAMA_ROLE").toString());
            editor.putString("keyIdPengguna",jObj.get("ID_PENGGUNA").toString());
            editor.putString("keyAlamat",jObj.get("ALAMAT").toString());
            editor.putString("keyTelpon",jObj.get("TELPON").toString());
            editor.putString("keyUsername",jObj.get("USERNAME").toString());
            editor.putString("keyIdPeternakan",jObj.get("ID_PETERNAKAN").toString());

            editor.apply();
        }
        catch (JSONException e){e.printStackTrace();}
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

    /**
     * Converts the given byte[] to a hex string.
     * @param raw the byte[] to convert
     * @return the string the given byte[] represents
     */
    private String convertToHex(byte[] raw) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < raw.length; i++) {
            sb.append(Integer.toString((raw[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
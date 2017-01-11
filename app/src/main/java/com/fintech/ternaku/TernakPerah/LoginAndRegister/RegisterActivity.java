package com.fintech.ternaku.TernakPerah.LoginAndRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {
    private EditText input_register_activity_namapanjang,input_register_activity_notelp,input_register_activity_alamat,
            input_register_activity_username,input_register_activity_password;
    private Button button_register_activity_masuk,button_register_activity_linktologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        input_register_activity_namapanjang = (EditText)findViewById(R.id.input_register_activity_namapanjang);
        input_register_activity_username = (EditText)findViewById(R.id.input_register_activity_username);
        input_register_activity_alamat = (EditText)findViewById(R.id.input_register_activity_alamat);
        input_register_activity_notelp = (EditText)findViewById(R.id.input_register_activity_notelp);
        input_register_activity_password = (EditText)findViewById(R.id.input_register_activity_password);

        button_register_activity_masuk = (Button)findViewById(R.id.button_register_activity_masuk) ;
        button_register_activity_linktologin = (Button)findViewById(R.id.button_register_activity_linktologin);
        button_register_activity_linktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        button_register_activity_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG",input_register_activity_username.getText().toString()+input_register_activity_alamat.getText().toString()+
                        input_register_activity_notelp.getText().toString()+input_register_activity_namapanjang.getText().toString());
                String urlParameters = "namalengkap=" + input_register_activity_namapanjang.getText().toString()
                        +"&alamat=" + input_register_activity_alamat.getText().toString()
                        +"&telepon=" + input_register_activity_notelp.getText().toString()
                        +"&username=" + input_register_activity_username.getText().toString()
                        +"&password=" + input_register_activity_password.getText().toString();
                new RegisterTask().execute("http://ternaku.com/index.php/C_Pengguna/insertRegister", urlParameters);
            }
        });

    }

    private class RegisterTask extends AsyncTask<String,Integer,String>{
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(RegisterActivity.this);
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
                Toast.makeText(getApplication(),"Username Telah Digunakan!!!",Toast.LENGTH_LONG).show();
                progDialog.dismiss();
            }
            else {
                Toast.makeText(getApplication(),"Selamat Anda Berhasil Mendaftar!!!",Toast.LENGTH_LONG).show();
                progDialog.dismiss();
            }
        }
    }

    public String get_SHA_512_SecurePassword(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes("UTF-8"));
            byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (UnsupportedEncodingException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }
}

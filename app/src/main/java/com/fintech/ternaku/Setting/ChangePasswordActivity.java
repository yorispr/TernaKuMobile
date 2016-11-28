package com.fintech.ternaku.Setting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.fintech.ternaku.TernakPerah.LoginAndRegister.LoginActivity;
import com.fintech.ternaku.R;
import com.fintech.ternaku.UrlList;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText passlama, passbaru, konfirmasipassbaru;
    private TextInputLayout lama,baru,konfirmasi;
    private Button btnUbah;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Ganti Password");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();
        lama = (TextInputLayout)findViewById(R.id.inputlayoutlama);
        baru = (TextInputLayout)findViewById(R.id.inputlayoutbaru);
        konfirmasi = (TextInputLayout)findViewById(R.id.inputlayoutkonfirmasi);

        passlama = (EditText)findViewById(R.id.edtpasslama);
        passbaru = (EditText)findViewById(R.id.edtpassbaru);
        konfirmasipassbaru = (EditText)findViewById(R.id.edtkonfirmasi);
        btnUbah = (Button)findViewById(R.id.btnUbahPass);
        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CekForm()){
                        String username = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyUsername", null);
                        String passwordlama = generateHash(passlama.getText().toString() + username);
                        String passwordbaru = generateHash(passbaru.getText().toString() + username);
                        String urlParameters = "idpengguna=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                + "&passlama=" + passwordlama
                                + "&passbaru=" + passwordbaru;
                        new UpdatePassword().execute(url.getUrl_ChangePassword(), urlParameters);
                        Log.d("param", urlParameters);
                    }
            }
        });
    }


    private class UpdatePassword extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(ChangePasswordActivity.this);
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
                Toast.makeText(getApplication(),"Berhasil Merubah Password",Toast.LENGTH_LONG).show();
                SharedPreferences preferences = getSharedPreferences(getString(R.string.userpref), 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent i = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
            else if(result.trim().equals("2")){
                Toast.makeText(getApplication(),"Password Lama Salah",Toast.LENGTH_LONG).show();
            }
            else if(result.trim().equals("0")){
                Toast.makeText(getApplication(),"Gagal",Toast.LENGTH_LONG).show();
            }
        }
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
    public boolean CekForm(){
        boolean cek = true;

        if(passlama.getText().toString().matches("")){
            //lama.setErrorEnabled(true);
            //passlama.setError("Isikan password lama");
            lama.setError("Isikan password lama");
            cek = false;
        }else{
            lama.setError(null);
        }
        if(passbaru.getText().toString().matches("")){
            //baru.setErrorEnabled(true);
            baru.setError("Isikan password baru");
            cek = false;
        }else {
            baru.setError(null);
        }

        if(konfirmasipassbaru.getText().toString().matches("")){
            //konfirmasi.setErrorEnabled(true);
            konfirmasi.setError("Isikan password baru");
            cek = false;
        }
        else if(!konfirmasipassbaru.getText().toString().equals(passbaru.getText().toString())){
            //konfirmasi.setErrorEnabled(true);
            konfirmasi.setError("Password baru tidak sama!");
            cek = false;
        }else{
            konfirmasi.setError(null);
        }
        return cek;
    }
    public boolean IsSama(){
        boolean cek = false;
        if(konfirmasipassbaru.getText().toString().equals(passlama.getText().toString())){
            cek = true;
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

package com.fintech.ternaku.TernakPerah.Main.TambahData.Produksi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.Setting.Bluetooth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UpdateWeightActivity extends AppCompatActivity {
    private AutoCompleteTextView input_updateweight_activity_idternak;
    private TextView input_weightupdate_activity_beratsebelum,input_weightupdate_activity_beratsesudah;
    private Button button_weightupdate_activity_simpan;
    private EditText input_weightupdate_activity_panjangbadansapi,input_weightupdate_activity_lebarbadansapi;
    ArrayList<String> list_updateweight_activity_idternak = new ArrayList<String>();
    ArrayList<String> list_updateweight_activity_berat = new ArrayList<String>();
    private int flag_hit = 0;
    private int choosenindex=-1;
    private Bluetooth bt;
    public final String TAG = "AddInseminasi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_weight);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                    | ActionBar.DISPLAY_SHOW_TITLE
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_produksi)));
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //Set Id Ternak dan Berat Sebelum---------------------------
        String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetTernak().execute("http://ternaku.com/index.php/C_Ternak/getTernakForPengelompokkan", urlParameters);
        input_updateweight_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_updateweight_activity_idternak);
        input_weightupdate_activity_beratsebelum = (TextView)findViewById(R.id.input_weightupdate_activity_beratsebelum);
        input_updateweight_activity_idternak.setEnabled(false);
        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_updateweight_activity_idternak);
        input_updateweight_activity_idternak.setAdapter(adp);
        input_updateweight_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosenindex = i;
                input_weightupdate_activity_beratsebelum.setText(list_updateweight_activity_berat.get(choosenindex).toString());
            }
        });

        //Set Berat Sesudah-------------------------------------------
        input_weightupdate_activity_panjangbadansapi = (EditText)findViewById(R.id.input_weightupdate_activity_panjangbadansapi);
        input_weightupdate_activity_lebarbadansapi = (EditText)findViewById(R.id.input_weightupdate_activity_lebarbadansapi);
        input_weightupdate_activity_beratsesudah = (TextView)findViewById(R.id.input_weightupdate_activity_beratsesudah);


        input_weightupdate_activity_panjangbadansapi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!input_weightupdate_activity_panjangbadansapi.getText().toString().isEmpty()&&
                        !input_weightupdate_activity_lebarbadansapi.getText().toString().isEmpty()){
                    String panjang_badan = input_weightupdate_activity_panjangbadansapi.getText().toString();
                    String lebar_dada = input_weightupdate_activity_lebarbadansapi.getText().toString();
                    input_weightupdate_activity_beratsesudah.setText(hitBerat(Float.parseFloat(lebar_dada),Float.parseFloat(panjang_badan)));
                }
            }
        });
        input_weightupdate_activity_lebarbadansapi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!input_weightupdate_activity_panjangbadansapi.getText().toString().isEmpty()&&
                        !input_weightupdate_activity_lebarbadansapi.getText().toString().isEmpty()){
                    String panjang_badan = input_weightupdate_activity_panjangbadansapi.getText().toString();
                    String lebar_dada = input_weightupdate_activity_lebarbadansapi.getText().toString();
                    input_weightupdate_activity_beratsesudah.setText(hitBerat(Float.parseFloat(lebar_dada),Float.parseFloat(panjang_badan)));
                }
            }
        });

        bt = new Bluetooth(this, mHandler);
        bt.start();
        bt.connectDevice("HC-06");
    }

    @Override
    protected void onResume(){
        super.onResume();
        bt = new Bluetooth(this, mHandler);
        bt.start();
        bt.connectDevice("HC-06");
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Bluetooth.MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    break;
                case Bluetooth.MESSAGE_WRITE:
                    Log.d(TAG, "MESSAGE_WRITE ");
                    break;
                case Bluetooth.MESSAGE_READ:
                    Log.d(TAG, "MESSAGE_READ : "+msg.obj.toString());
                    input_updateweight_activity_idternak.setText(msg.obj.toString().trim());
                    break;
                case Bluetooth.MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "MESSAGE_DEVICE_NAME "+msg);
                    break;
                case Bluetooth.MESSAGE_TOAST:
                    Log.d(TAG, "MESSAGE_TOAST "+msg);

                    break;
            }
        }
    };


    //Hitung Berat---------------------------------------------------
    private String hitBerat(float lingkar_dada,float panjang_badan){
        float hasil;
        String hasil_string;
        hasil = (float) (((lingkar_dada/2.53)*(lingkar_dada/2.53))*panjang_badan)/300;

        return String.format("%.2f",hasil);
    }

    //AsycnTask Get Id Ternak Auto Complete--------------------------
    private class GetTernak extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(UpdateWeightActivity.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(UpdateWeightActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Koneksi Terputus!")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                            }
                        })
                        .show();
            } else{
                AddTernakToList(result);
                input_updateweight_activity_idternak.setEnabled(true);
            }
        }
    }

    private void AddTernakToList(String result)
    {
        list_updateweight_activity_idternak.clear();
        list_updateweight_activity_berat.clear();
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_updateweight_activity_berat.add(jObj.getString("berat_badan"));
                list_updateweight_activity_idternak.add(jObj.getString("id_ternak"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }

    public boolean cekForm(){
        boolean cek = true;
        if(TextUtils.isEmpty(input_updateweight_activity_idternak.getText().toString())){
            cek = false;
            input_updateweight_activity_idternak.setError("Data belum diisi");
        }
        if(input_weightupdate_activity_beratsesudah.getText().toString().equalsIgnoreCase("")){
            cek=false;
            input_weightupdate_activity_beratsesudah.setError("Masukkan Panjang dan Lebar Sapi Untuk Menghitung Berat");
        }
        return cek;
    }

    public void cleartext(){
        input_updateweight_activity_idternak.setText("");
        input_weightupdate_activity_beratsesudah.setText("");
        input_weightupdate_activity_beratsesudah.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


}

package com.fintech.ternaku.TernakPerah.Main.TambahData.Produksi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.Setting.Bluetooth;
import com.fintech.ternaku.UrlList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UpdateCulling extends AppCompatActivity {
    private AutoCompleteTextView input_updateculling_activity_idternak;
    private TextView input_updateculling_activity_tanggal;
    private EditText input_updateculling_activity_alasan;
    private TimePickerDialog mTimePicker;
    private DatePickerDialog DatePickerDialog_tglculling;
    private SimpleDateFormat dateFormatter_tglculling;
    private Button button_updateculling_activity_simpan;
    String datetime;
    private Bluetooth bt;
    public final String TAG = "AddInseminasi";
    private int choosenindex =-1;
    ArrayList<String> list_updateculling_idternak = new ArrayList<String >();
    ArrayAdapter<String> adp;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_culling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Culling");

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_produksi)));
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //Set Auto Text--------------------------------------------
        String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
        new GetTernakId().execute(url.getUrl_GetTernakPengelompokkan(), param);
        input_updateculling_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_updateculling_activity_idternak);
        input_updateculling_activity_idternak.setEnabled(false);
        adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_updateculling_idternak);
        input_updateculling_activity_idternak.setAdapter(adp);
        input_updateculling_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });


        //Set Date Culling------------------------------------------------------
        setDateTimeField();
        setTime();
        input_updateculling_activity_tanggal = (TextView)findViewById(R.id.input_updateculling_activity_tanggal);
        dateFormatter_tglculling = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_updateculling_activity_tanggal.setText(dateFormatter_tglculling.format(Calendar.getInstance().getTime()));
        input_updateculling_activity_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(UpdateCulling.this);
                DatePickerDialog_tglculling.show();
            }
        });

        //Insert Into Database------------------------------------------------
        button_updateculling_activity_simpan = (Button)findViewById(R.id.button_updateculling_activity_simpan);
        input_updateculling_activity_alasan = (EditText)findViewById(R.id.input_updateculling_activity_alasan);
        button_updateculling_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cekForm()) {
                    new SweetAlertDialog(UpdateCulling.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Simpan")
                            .setContentText("Data Yang Dimasukkan Sudah Benar?")
                            .setConfirmText("Ya")
                            .setCancelText("Tidak")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();

                                    //Cek RFID---------------------------------
                                    Connection c = new Connection();
                                    String urlParameters2;
                                    urlParameters2 = "id=" + input_updateculling_activity_idternak.getText().toString().trim() +
                                            "&idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null);
                                    new CheckRFID().execute(url.getUrlGet_RFIDanIdCek(), urlParameters2);

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
                else{
                    new SweetAlertDialog(UpdateCulling.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Isikan Semua Data")
                            .show();
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
                    input_updateculling_activity_idternak.setText(msg.obj.toString().trim());
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

    //Get Data Ternak Dry-------------------------------------------
    private class GetTernakId extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(UpdateCulling.this, SweetAlertDialog.PROGRESS_TYPE);

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
            String json = c.GetJSONfromURL(params[0], params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("RES", result);
            pDialog.dismiss();
            if (result.trim().equals("kosong")) {
                new SweetAlertDialog(UpdateCulling.this, SweetAlertDialog.ERROR_TYPE)
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
            } else {
                AddTernakToList(result);
                input_updateculling_activity_idternak.setEnabled(true);
            }
        }
    }

    private void AddTernakToList(String result) {
        list_updateculling_idternak.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_updateculling_idternak.add(jObj.getString("id_ternak"));
            }
            adp.notifyDataSetChanged();
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private class CheckRFID extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(UpdateCulling.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("RES_Insert",result);
            pDialog.dismiss();
            if(result.trim().equals("1")) {
                String idter = input_updateculling_activity_idternak.getText().toString();
                String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                        + "&idternak=" + idter
                        + "&tglculling=" + input_updateculling_activity_tanggal.getText().toString()
                        + "&alasan=" + input_updateculling_activity_alasan.getText().toString();
                new AddCulling().execute(url.getUrlInsertCulling(), param);
            }else{
                new SweetAlertDialog(UpdateCulling.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("RFID Sudah Terpakai atau Tidak Ada RFID Ditemukan")
                        .show();
            }
        }
    }

    //Insert In To Database---------------------------------------------------
    private class AddCulling extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(UpdateCulling.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(UpdateCulling.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(UpdateCulling.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Penggunaan Pakan")
                                        .setContentText("Apakah Ingin Menambah Data Penggunaan Pakan Lagi?")
                                        .setConfirmText("Ya")
                                        .setCancelText("Tidak")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                                cleartext();
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
            }else if(result.trim().equals("0")){
                new SweetAlertDialog(UpdateCulling.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Penambahan Gagal!")
                        .setContentText("Silahkan Simpan Data Kembali")
                        .show();
            }
        }
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog_tglculling = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_updateculling_activity_tanggal.setText(dateFormatter_tglculling.format(newDate.getTime()));
                datetime = dateFormatter_tglculling.format(newDate.getTime());
                mTimePicker.show();
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(timePicker.isShown()) {

                    datetime += " " + selectedHour + ":" + selectedMinute + ":00";
                    input_updateculling_activity_tanggal.setText(datetime);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
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


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public boolean cekForm(){
        boolean cek = true;
        if(input_updateculling_activity_idternak.getText().toString().matches("")){
            cek = false;
            input_updateculling_activity_idternak.setError("Data belum diisi");
        }
        if(input_updateculling_activity_tanggal.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            cek=false;
            input_updateculling_activity_tanggal.setError("Data belum diisi");
        }
        if(input_updateculling_activity_alasan.getText().toString().equalsIgnoreCase("")){
            cek=false;
            input_updateculling_activity_alasan.setError("Data belum diisi");
        }
        return cek;
    }

    public void cleartext(){
        input_updateculling_activity_idternak.setText("");
        input_updateculling_activity_tanggal.setText("");
        input_updateculling_activity_alasan.setText("");
        input_updateculling_activity_tanggal.setHint("01 Januari 1970");
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

package com.fintech.ternaku.TernakPerah.Main.TambahData.Kesuburan;

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
import android.text.TextUtils;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.fintech.ternaku.TernakPerah.Alarm.Alarm;
import com.fintech.ternaku.TernakPerah.Alarm.AlarmScheduler;
import com.fintech.ternaku.Connection;
import com.fintech.ternaku.DatabaseHandler;
import com.fintech.ternaku.R;
import com.fintech.ternaku.Setting.Bluetooth;
import com.fintech.ternaku.UrlList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddInseminasi extends AppCompatActivity {
    private TextView input_addinseminasi_activity_tglinseminasi;
    private AutoCompleteTextView input_addinseminasi_activity_idternak;
    private EditText input_addinseminasi_activity_biaya,input_addinseminasi_activity_jumlah,input_addinseminasi_activity_metode;
    private Spinner spinner_addinseminasi_activity_semen;
    private DatePickerDialog DatePickerDialog_tglinseminasi;
    private SimpleDateFormat dateFormatter_tglinseminasi;
    private Button button_addinseminasi_activity_simpan;
    private TimePickerDialog mTimePicker;
    String datetime;
    int choosenindex=-1;
    DatabaseHandler db = new DatabaseHandler(this);
    String id_sapi="";
    ArrayList<String> list_addinseminasi_semen = new ArrayList<String>();
    ArrayList<String> list_addinseminasi_idternak = new ArrayList<String>();
    ArrayAdapter<String> myAdapter;
    private Bluetooth bt;
    public final String TAG = "AddInseminasi";

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inseminasi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Data Inseminasi");
            //actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f578c5")));
            /*
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(Color.parseColor("#f578c5"));
                }
            */

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_kesuburan)));
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }



        //Set Tanggal Inseminasi--------------------------------------
        setDateTimeField();
        setTime();
        input_addinseminasi_activity_tglinseminasi = (TextView)findViewById(R.id.input_addinseminasi_activity_tglinseminasi);
        dateFormatter_tglinseminasi = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addinseminasi_activity_tglinseminasi.setText(dateFormatter_tglinseminasi.format(Calendar.getInstance().getTime()));
        input_addinseminasi_activity_tglinseminasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog_tglinseminasi.show();
            }
        });

        //Set Data Id Ternak-----------------------------------------------
        String urlParameters_idternak = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetTernakHeat().execute(url.getUrl_GetTernakPengelompokkan(), urlParameters_idternak);
        input_addinseminasi_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addinseminasi_activity_idternak);
        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_addinseminasi_idternak);
        input_addinseminasi_activity_idternak.setAdapter(adp);
        input_addinseminasi_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosenindex = i;
            }
        });
        if(getIntent().getExtras()!=null){
            id_sapi = getIntent().getExtras().getString("id_sapi");
            input_addinseminasi_activity_idternak.setText(id_sapi);
        }
        input_addinseminasi_activity_idternak.setEnabled(false);




        //Set Spinner Daftar Semen------------------------------------
        spinner_addinseminasi_activity_semen = (Spinner)findViewById(R.id.spinner_addinseminasi_activity_semen);
        myAdapter= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,list_addinseminasi_semen);
        spinner_addinseminasi_activity_semen.setAdapter(myAdapter);

        //Set Harga dan Jumlah----------------------------------------
        input_addinseminasi_activity_biaya = (EditText)findViewById(R.id.input_addinseminasi_activity_biaya);
        input_addinseminasi_activity_jumlah = (EditText)findViewById(R.id.input_addinseminasi_activity_jumlah);
        input_addinseminasi_activity_metode = (EditText)findViewById(R.id.input_addinseminasi_activity_metode);


        //Simpan Data------------------------------------------------
        button_addinseminasi_activity_simpan = (Button)findViewById(R.id.button_addinseminasi_activity_simpan);
        button_addinseminasi_activity_simpan.setBackgroundColor(Color.parseColor("#f578c5"));
        button_addinseminasi_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForm()) {
                    new SweetAlertDialog(AddInseminasi.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Simpan")
                            .setContentText("Data Yang Dimasukkan Sudah Benar?")
                            .setConfirmText("Ya")
                            .setCancelText("Tidak")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();

                                    //Cek RFID---------------------------------
                                    String urlParameters2;
                                    urlParameters2 = "id=" + input_addinseminasi_activity_idternak.getText().toString().trim() +
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
                }else{
                    new SweetAlertDialog(AddInseminasi.this, SweetAlertDialog.ERROR_TYPE)
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
                    input_addinseminasi_activity_idternak.setText(msg.obj.toString().trim());
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

    //Set Input AutoComplete Id Ternak-----------------------------------------------
    private class GetTernakHeat extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddInseminasi.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddInseminasi.this, SweetAlertDialog.ERROR_TYPE)
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
            }else if (result.trim().equals("404")){
                new SweetAlertDialog(AddInseminasi.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Gagal Memuat Data, Data Ternak Masih Kosong")
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
                input_addinseminasi_activity_idternak.setEnabled(true);

                //Set Data Spinner----------------------------------------
                String urlParameters_semen = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
                new GetSemen().execute(url.getUrl_GetSemen(), urlParameters_semen);
            }
        }
    }

    private void AddTernakToList(String result)
    {
        list_addinseminasi_idternak.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addinseminasi_idternak.add(jObj.getString("id_ternak"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Set Daftar Semen----------------------------------------------
    private class GetSemen extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddInseminasi.this, SweetAlertDialog.PROGRESS_TYPE);

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
            if (result.trim().equals("404")){
                new SweetAlertDialog(AddInseminasi.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Gagal Memuat Data")
                        .setContentText("Data Semen Masih Kosong")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                            }
                        })
                        .show();
            }
            else {
                AddSemenToList(result);
                input_addinseminasi_activity_idternak.setEnabled(true);
            }
        }
    }
    private void AddSemenToList(String result) {
        list_addinseminasi_semen.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addinseminasi_semen.add("("+jObj.getString("ID_SEMEN")+") "+jObj.getString("GENOM")+", "+jObj.getString("JENIS_SEMEN"));
            }
            myAdapter.notifyDataSetChanged();
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog_tglinseminasi = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addinseminasi_activity_tglinseminasi.setText(dateFormatter_tglinseminasi.format(newDate.getTime()));
                datetime = dateFormatter_tglinseminasi.format(newDate.getTime());
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
                    input_addinseminasi_activity_tglinseminasi.setText(datetime);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
    }

    private class CheckRFID extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(AddInseminasi.this, SweetAlertDialog.PROGRESS_TYPE);

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
                String urlParameters;
                String spinstr = spinner_addinseminasi_activity_semen.getSelectedItem().toString();
                String idsemen = spinstr.substring(spinstr.indexOf("(") + 1, spinstr.indexOf(")"));
                urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                        + "&idternak=" + input_addinseminasi_activity_idternak.getText().toString().trim()
                        + "&tglinseminasi=" + input_addinseminasi_activity_tglinseminasi.getText().toString()
                        + "&metodeinseminasi=" + input_addinseminasi_activity_metode.getText().toString()
                        + "&idsemen=" + idsemen.trim()
                        + "&jumlahsemen=" + input_addinseminasi_activity_jumlah.getText().toString()
                        + "&biayasemen=" + input_addinseminasi_activity_biaya.getText().toString();
                new InsertInseminasi().execute(url.getUrl_InsertInseminasi(), urlParameters);
                Log.d("Param", urlParameters.toString());
            }else{
                new SweetAlertDialog(AddInseminasi.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("Tidak Ada RFID Ditemukan")
                        .show();
            }
        }
    }

    //Insert Inseminasi-----------------------------------------
    private class InsertInseminasi extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddInseminasi.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("RESAddInseminasi",result);
            pDialog.dismiss();
            if (result.trim().equals("1")){
                new SweetAlertDialog(AddInseminasi.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();

                                final int _id = (int) System.currentTimeMillis();
                                Calendar cal = Calendar.getInstance();
                                Date date = cal.getTime();
                                String formatteddate = new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(date);
                                cal.add(Calendar.MINUTE,1);
                                Log.d("calendar3",formatteddate);

                                Alarm al = new Alarm(0,String.valueOf(_id),"inseminasi",String.valueOf(new Date()),formatteddate,input_addinseminasi_activity_idternak.getText().toString().trim());
                                db.addAlarm(al);
                                AlarmScheduler as = new AlarmScheduler();
                                as.setAlarm(al,getApplicationContext());
                                Log.d("id_sapi2",al.getId_sapi());

                                new SweetAlertDialog(AddInseminasi.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Ubah Data Inseminasi")
                                        .setContentText("Apakah Ingin Menambah Data Inseminasi Lagi?")
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
            }
            else {
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkForm()
    {
        boolean value = true;

        if(TextUtils.isEmpty(input_addinseminasi_activity_idternak.getText().toString())){
            value= false;
            input_addinseminasi_activity_idternak.setError("ID Ternak belum diisi");
        }
        if(TextUtils.isEmpty(input_addinseminasi_activity_biaya.getText().toString())){
            value= false;
            input_addinseminasi_activity_biaya.setError("Biaya belum diisi");
        }
        if(TextUtils.isEmpty(input_addinseminasi_activity_jumlah.getText().toString())){
            value= false;
            input_addinseminasi_activity_jumlah.setError("Jumlah belum diisi");
        }
        if(TextUtils.isEmpty(input_addinseminasi_activity_metode.getText().toString())){
            value= false;
            input_addinseminasi_activity_metode.setError("Metode belum diisi");
        }
        if(input_addinseminasi_activity_tglinseminasi.getText().toString().matches(""))
        {
            input_addinseminasi_activity_tglinseminasi.setError("Tanggal belum diisi");
            value = false;
        }

        return value;
    }

    public void cleartext(){
        input_addinseminasi_activity_idternak.setText("");
        input_addinseminasi_activity_biaya.setText("");
        input_addinseminasi_activity_jumlah.setText("");
        input_addinseminasi_activity_metode.setText("");
        input_addinseminasi_activity_tglinseminasi.setText("01 Januari 1970");
        spinner_addinseminasi_activity_semen.clearFocus();
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


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}

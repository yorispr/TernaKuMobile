package com.fintech.ternaku.TernakPerah.Main.TambahData.Kesuburan;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

public class AddPemeriksaanReproduksi extends AppCompatActivity {
    private TextView input_addpemeriksaansubur_activity_tglpemeriksaan,input_addpemeriksaansubur_activity_tglperiksaberikut;
    private AutoCompleteTextView input_addpemeriksaansubur_activity_idternak;
    private Spinner spinner_addpemeriksaansubur_activity_statuskesehatan;
    private EditText input_addpemeriksaansubur_activity_diagnosis,input_addpemeriksaansubur_activity_perawatan,
            input_addpemeriksaansubur_activity_biaya;
    private Button button_addpemeriksaansubur_activity_simpan;
    private LinearLayout linearLayout_addpemeriksaansubur_activity_diagnosis;
    private DatePickerDialog DatePickerDialog_tglpemeriksaan,DatePickerDialog_tglperiksaberikut;
    private SimpleDateFormat dateFormatter_tglpemeriksaan,dateFormatter_tglperiksaberikut;
    int choosenindex=-1;
    private TimePickerDialog mTimePicker_tglpemeriksaan;
    String datetime_tglpemeriksaan;
    private TimePickerDialog mTimePicker_tglperiksaberikut;
    String datetime_tglperiksaberikut;

    private Bluetooth bt;
    public final String TAG = "AddInseminasi";
    int isSehat = 1;
    ArrayList<String> list_addipemeriksaansubur_idternak = new ArrayList<String>();
    ArrayAdapter<String> myAdapter;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pemeriksaan_reproduksi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Periksa Reproduksi");

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_kesuburan)));
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //Set Id Ternak Auto Complete-------------------------------------
        String urlParameters_idternak = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetTernakHeat().execute(url.getUrl_GetTernakPengelompokkan(), urlParameters_idternak);
        input_addpemeriksaansubur_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addpemeriksaansubur_activity_idternak);
        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_addipemeriksaansubur_idternak);
        input_addpemeriksaansubur_activity_idternak.setAdapter(adp);
        input_addpemeriksaansubur_activity_idternak.setEnabled(false);

        setDateTimeField();
        setTime();

        //Set Tanggal Pemeriksaan-----------------------------------------
        input_addpemeriksaansubur_activity_tglpemeriksaan = (TextView)findViewById(R.id.input_addpemeriksaansubur_activity_tglpemeriksaan);
        dateFormatter_tglpemeriksaan = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addpemeriksaansubur_activity_tglpemeriksaan.setText(dateFormatter_tglpemeriksaan.format(Calendar.getInstance().getTime()));
        input_addpemeriksaansubur_activity_tglpemeriksaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddPemeriksaanReproduksi.this);
                DatePickerDialog_tglpemeriksaan.show();
            }
        });

        //Set Spinner Status Kesehatan-------------------------------------
        spinner_addpemeriksaansubur_activity_statuskesehatan = (Spinner)findViewById(R.id.spinner_addpemeriksaansubur_activity_statuskesehatan);
        String [] pilihan= {"Pilih Status Kesehatan","Sehat","Tidak Sehat"};
        myAdapter= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,pilihan);
        spinner_addpemeriksaansubur_activity_statuskesehatan.setAdapter(myAdapter);
        spinner_addpemeriksaansubur_activity_statuskesehatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==1){
                    linearLayout_addpemeriksaansubur_activity_diagnosis.setVisibility(View.GONE);
                    isSehat = 1;
                }
                else if(i==2){
                    linearLayout_addpemeriksaansubur_activity_diagnosis.setVisibility(View.VISIBLE);
                    isSehat = 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Set Linear Diagnosis---------------------------------------------
        linearLayout_addpemeriksaansubur_activity_diagnosis = (LinearLayout)findViewById(R.id.linearLayout_addpemeriksaansubur_activity_diagnosis);
        input_addpemeriksaansubur_activity_diagnosis = (EditText)findViewById(R.id.input_addpemeriksaansubur_activity_diagnosis);
        input_addpemeriksaansubur_activity_perawatan = (EditText)findViewById(R.id.input_addpemeriksaansubur_activity_perawatan);

        //Set Tanggal Periksa Berikutnya-----------------------------------
        input_addpemeriksaansubur_activity_tglperiksaberikut = (TextView) findViewById(R.id.input_addpemeriksaansubur_activity_tglperiksaberikut);
        dateFormatter_tglperiksaberikut = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addpemeriksaansubur_activity_tglperiksaberikut.setText(dateFormatter_tglperiksaberikut.format(Calendar.getInstance().getTime()));
        input_addpemeriksaansubur_activity_tglperiksaberikut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddPemeriksaanReproduksi.this);
                DatePickerDialog_tglperiksaberikut.show();
            }
        });

        //Set Biaya--------------------------------------------------------
        input_addpemeriksaansubur_activity_biaya = (EditText)findViewById(R.id.input_addpemeriksaansubur_activity_biaya);
        button_addpemeriksaansubur_activity_simpan = (Button)findViewById(R.id.button_addpemeriksaansubur_activity_simpan);
        button_addpemeriksaansubur_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForm()) {
                    if(spinner_addpemeriksaansubur_activity_statuskesehatan.getSelectedItemId()==0){
                        new SweetAlertDialog(AddPemeriksaanReproduksi.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Silahkan Pilih Status Kesehatan")
                                .show();
                    }else{
                        new SweetAlertDialog(AddPemeriksaanReproduksi.this, SweetAlertDialog.WARNING_TYPE)
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
                                        urlParameters2 = "id=" + input_addpemeriksaansubur_activity_idternak.getText().toString().trim() +
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
                }else{
                    new SweetAlertDialog(AddPemeriksaanReproduksi.this, SweetAlertDialog.ERROR_TYPE)
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
                    input_addpemeriksaansubur_activity_idternak.setText(msg.obj.toString().trim());
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
        SweetAlertDialog pDialog = new SweetAlertDialog(AddPemeriksaanReproduksi.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddPemeriksaanReproduksi.this, SweetAlertDialog.ERROR_TYPE)
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
                input_addpemeriksaansubur_activity_idternak.setEnabled(true);
            }
        }
    }
    private void AddTernakToList(String result)
    {
        list_addipemeriksaansubur_idternak.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addipemeriksaansubur_idternak.add(jObj.getString("id_ternak"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private class CheckRFID extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(AddPemeriksaanReproduksi.this, SweetAlertDialog.PROGRESS_TYPE);

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
                String perawatan = "N/A";
                String diagnosis = "N/A";

                if (isSehat == 0) {
                    diagnosis = input_addpemeriksaansubur_activity_diagnosis.getText().toString();
                    perawatan = input_addpemeriksaansubur_activity_perawatan.getText().toString();
                }
                String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                        + "&idternak=" + input_addpemeriksaansubur_activity_idternak.getText().toString().trim()
                        + "&tglperiksa=" + input_addpemeriksaansubur_activity_tglpemeriksaan.getText().toString()
                        + "&perawatan=" + perawatan
                        + "&diagnosis=" + diagnosis
                        + "&tglperiksaberikutnya=" + input_addpemeriksaansubur_activity_tglperiksaberikut.getText().toString()
                        + "&statusreproduksi=" + String.valueOf(isSehat)
                        + "&biayaperiksa=" + input_addpemeriksaansubur_activity_biaya.getText().toString();
                new InsertPeriksaReproduksi().execute(url.getUrl_InsertPemeriksaanReproduksi(), urlParameters);
                Log.d("Param",urlParameters);
            }else{
                new SweetAlertDialog(AddPemeriksaanReproduksi.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("RFID Sudah Terpakai atau Tidak Ada RFID Ditemukan")
                        .show();
            }
        }
    }

    //Insert To Database----------------------------------------
    private class InsertPeriksaReproduksi extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddPemeriksaanReproduksi.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddPemeriksaanReproduksi.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(AddPemeriksaanReproduksi.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Pemeriksaan Reproduksi")
                                        .setContentText("Apakah Ingin Menambah Data Pemeriksaaan Reproduksi Lagi?")
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

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog_tglpemeriksaan = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addpemeriksaansubur_activity_tglpemeriksaan.setText(dateFormatter_tglpemeriksaan.format(newDate.getTime()));
                datetime_tglpemeriksaan = dateFormatter_tglpemeriksaan.format(newDate.getTime());
                mTimePicker_tglpemeriksaan.show();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog_tglperiksaberikut = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addpemeriksaansubur_activity_tglperiksaberikut.setText(dateFormatter_tglperiksaberikut.format(newDate.getTime()));
                datetime_tglperiksaberikut = dateFormatter_tglperiksaberikut.format(newDate.getTime());
                mTimePicker_tglperiksaberikut.show();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker_tglpemeriksaan = new TimePickerDialog(AddPemeriksaanReproduksi.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(timePicker.isShown()) {
                    //txtJam.setText( selectedHour + ":" + selectedMinute+":00");
                    datetime_tglpemeriksaan += " " + selectedHour + ":" + selectedMinute + ":00";
                    input_addpemeriksaansubur_activity_tglpemeriksaan.setText(datetime_tglpemeriksaan);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker_tglpemeriksaan.setTitle("Select Time");

        mTimePicker_tglperiksaberikut = new TimePickerDialog(AddPemeriksaanReproduksi.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(timePicker.isShown()) {
                    //txtJam.setText( selectedHour + ":" + selectedMinute+":00");
                    datetime_tglperiksaberikut += " " + selectedHour + ":" + selectedMinute + ":00";
                    input_addpemeriksaansubur_activity_tglperiksaberikut.setText(datetime_tglperiksaberikut);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker_tglperiksaberikut.setTitle("Select Time");
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

    private boolean checkForm()
    {
        boolean cek = true;

        if(input_addpemeriksaansubur_activity_idternak.getText().toString().matches(""))
        {
            input_addpemeriksaansubur_activity_idternak.setError("ID Ternak belum diisi");
            cek = false;
        }
        if(input_addpemeriksaansubur_activity_biaya.getText().toString().matches(""))
        {
            input_addpemeriksaansubur_activity_biaya.setError("Biaya belum diisi");
            cek = false;
        }
        if(input_addpemeriksaansubur_activity_tglperiksaberikut.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            cek=false;
            input_addpemeriksaansubur_activity_tglperiksaberikut.setError("Tanggal belum diisi");
        }
        if(input_addpemeriksaansubur_activity_tglpemeriksaan.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            cek=false;
            input_addpemeriksaansubur_activity_tglpemeriksaan.setError("Tanggal belum diisi");
        }
        if(isSehat==0){
            if(TextUtils.isEmpty(input_addpemeriksaansubur_activity_diagnosis.getText().toString())){
                input_addpemeriksaansubur_activity_diagnosis.setError("Isikan Diagnosis Penyakit");
                cek = false;
            }
            if(TextUtils.isEmpty(input_addpemeriksaansubur_activity_perawatan.getText().toString())){
                input_addpemeriksaansubur_activity_perawatan.setError("Isikan Perawatan Yang Harus DiBerikan");
                cek = false;
            }
        }


        return cek;
    }

    public void cleartext(){
        input_addpemeriksaansubur_activity_idternak.setText("");
        input_addpemeriksaansubur_activity_biaya.setText("");
        input_addpemeriksaansubur_activity_diagnosis.setText("");
        input_addpemeriksaansubur_activity_perawatan.setText("");
        input_addpemeriksaansubur_activity_tglpemeriksaan.setText("01 Januari 1970");
        input_addpemeriksaansubur_activity_tglperiksaberikut.setText("01 Januari 1970");
        spinner_addpemeriksaansubur_activity_statuskesehatan.setSelection(0);
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
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

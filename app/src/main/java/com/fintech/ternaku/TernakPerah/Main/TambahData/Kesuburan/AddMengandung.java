package com.fintech.ternaku.TernakPerah.Main.TambahData.Kesuburan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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

public class AddMengandung extends AppCompatActivity {
    private TextView input_addmengandung_activity_tglpemeriksaan,input_addmengandung_activity_tglperkiraanhamil;
    private AutoCompleteTextView input_addmengandung_activity_idternak;
    private Spinner spinner_addmengandung_activity_statuskeberhasilan;
    private Button button_addmengandung_activity_simpan;
    ArrayList<String> list_addmengandung_idternak = new ArrayList<String>();
    ArrayList<String> list_addmengandung_tglinseminasi = new ArrayList<String>();
    private DatePickerDialog DatePickerDialog_tglpemeriksaan,DatePickerDialog_tglperkiraanhamil;
    private int choosenindex=-1;
    private TimePickerDialog mTimePicker_tglpemeriksaan;
    private TimePickerDialog mTimePicker_tglperkiraanhamil;
    String datetime_tglpemeriksaan;
    String datetime_tglperkiraanhamil;
    private SimpleDateFormat dateFormatter_tglpemeriksaan,dateFormatter_tglperkiraanhamil;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    private Bluetooth bt;
    public final String TAG = "AddInseminasi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mengandung);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Mengandung");

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_kesuburan)));
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //Set Data Id Ternak-----------------------------------------------
        String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetTernakSudahInseminasi().execute(url.getUrl_GetTernakInseminasi(), urlParameters);
        input_addmengandung_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addmengandung_activity_idternak);
        input_addmengandung_activity_idternak.setEnabled(false);
        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_addmengandung_idternak);
        input_addmengandung_activity_idternak.setAdapter(adp);
        input_addmengandung_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosenindex = i;
            }
        });
        input_addmengandung_activity_idternak.setEnabled(false);

        //Set Tgl Pemeriksaan-------------------------------------------------
        setDateTimeField();
        setTime();
        input_addmengandung_activity_tglpemeriksaan = (TextView) findViewById(R.id.input_addmengandung_activity_tglpemeriksaan);
        dateFormatter_tglpemeriksaan = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addmengandung_activity_tglpemeriksaan.setText(dateFormatter_tglpemeriksaan.format(Calendar.getInstance().getTime()));
        input_addmengandung_activity_tglpemeriksaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog_tglpemeriksaan.show();
            }
        });

        //Set Spinner Status-------------------------------------------------
        spinner_addmengandung_activity_statuskeberhasilan = (Spinner)findViewById(R.id.spinner_addmengandung_activity_statuskeberhasilan);
        final String[] spinData= {"Pilih Status Keberhasilan","Berhasil","Gagal","Belum Diketahui"};
        ArrayAdapter<String> myAdapter= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,spinData);
        spinner_addmengandung_activity_statuskeberhasilan.setAdapter(myAdapter);

        //Set Tgl Perkiraan-------------------------------------------------
        input_addmengandung_activity_tglperkiraanhamil = (TextView) findViewById(R.id.input_addmengandung_activity_tglperkiraanhamil);
        dateFormatter_tglperkiraanhamil = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addmengandung_activity_tglperkiraanhamil.setText(dateFormatter_tglperkiraanhamil.format(Calendar.getInstance().getTime()));
        input_addmengandung_activity_tglperkiraanhamil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog_tglperkiraanhamil.show();
            }
        });

        //Simpan Data------------------------------------------------------
        button_addmengandung_activity_simpan = (Button)findViewById(R.id.button_addmengandung_activity_simpan);
        button_addmengandung_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dateFormatter = new SimpleDateFormat("YYYY", Locale.US);
                if(checkForm()){
                    if(spinner_addmengandung_activity_statuskeberhasilan.getSelectedItemId()==0){
                        new SweetAlertDialog(AddMengandung.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Silahkan Pilih Status Keberhasilan...")
                                .show();
                    }else {
                        new SweetAlertDialog(AddMengandung.this, SweetAlertDialog.WARNING_TYPE)
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
                                        urlParameters2 = "id=" + input_addmengandung_activity_idternak.getText().toString().trim() +
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
                    new SweetAlertDialog(AddMengandung.this, SweetAlertDialog.ERROR_TYPE)
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
                    input_addmengandung_activity_idternak.setText(msg.obj.toString().trim());
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
    private class GetTernakSudahInseminasi extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddMengandung.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddMengandung.this, SweetAlertDialog.ERROR_TYPE)
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
                new SweetAlertDialog(AddMengandung.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Simpan")
                        .setContentText("Data Ternak Yang Sudah Di Inseminasi Masih Kosong" +
                                "\nApakah Ingin Memasukkan Data Ternak Inseminasi?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                finish();
                                startActivity(new Intent(AddMengandung.this,AddInseminasi.class));
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
            } else{
                AddTernakToList(result);
                input_addmengandung_activity_idternak.setEnabled(true);
            }
        }
    }

    private class CheckRFID extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(AddMengandung.this, SweetAlertDialog.PROGRESS_TYPE);

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
                String idternak = input_addmengandung_activity_idternak.getText().toString().trim();
                String tglinseminasi = getTglInseminasi(idternak);
                String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null)
                        +"&tglcekkeberhasilan="+input_addmengandung_activity_tglpemeriksaan.getText().toString().trim()
                        +"&statuskeberhasilan="+spinner_addmengandung_activity_statuskeberhasilan.getSelectedItem().toString()
                        +"&tglperkiraanmelahirkan="+input_addmengandung_activity_tglperkiraanhamil.getText().toString()
                        +"&idternak="+idternak
                        +"&tglinseminasi="+tglinseminasi;
                Log.d("param",urlParameters);
                new InsertStatusKehamilan().execute(url.getUrl_InsertMengandung(), urlParameters);
            }else{
                new SweetAlertDialog(AddMengandung.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("RFID Sudah Terpakai atau Tidak Ada RFID Ditemukan")
                        .show();
            }
        }
    }

    //Input to Database------------------------------------------
    private class InsertStatusKehamilan extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddMengandung.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddMengandung.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(AddMengandung.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Mengandung")
                                        .setContentText("Apakah Ingin Menambah Data Mengandung Lagi?")
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
            else if (result.trim().equals("0")){
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void AddTernakToList(String result)
    {
        list_addmengandung_idternak.clear();
        list_addmengandung_tglinseminasi.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addmengandung_tglinseminasi.add(jObj.getString("tgl_inseminasi"));
                list_addmengandung_idternak.add(jObj.getString("id_ternak"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog_tglpemeriksaan = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addmengandung_activity_tglpemeriksaan.setText(dateFormatter_tglpemeriksaan.format(newDate.getTime()));
                datetime_tglpemeriksaan = dateFormatter_tglpemeriksaan.format(newDate.getTime());
                mTimePicker_tglpemeriksaan.show();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog_tglperkiraanhamil = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addmengandung_activity_tglperkiraanhamil.setText(dateFormatter_tglperkiraanhamil.format(newDate.getTime()));
                datetime_tglperkiraanhamil = dateFormatter_tglperkiraanhamil.format(newDate.getTime());
                mTimePicker_tglperkiraanhamil.show();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker_tglpemeriksaan = new TimePickerDialog(AddMengandung.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(timePicker.isShown()) {
                    //txtJam.setText( selectedHour + ":" + selectedMinute+":00");
                    datetime_tglpemeriksaan += " " + selectedHour + ":" + selectedMinute + ":00";
                    input_addmengandung_activity_tglpemeriksaan.setText(datetime_tglpemeriksaan);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker_tglpemeriksaan.setTitle("Select Time");

        mTimePicker_tglperkiraanhamil = new TimePickerDialog(AddMengandung.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(timePicker.isShown()) {
                    //txtJam.setText( selectedHour + ":" + selectedMinute+":00");
                    datetime_tglperkiraanhamil += " " + selectedHour + ":" + selectedMinute + ":00";
                    input_addmengandung_activity_tglperkiraanhamil.setText(datetime_tglperkiraanhamil);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker_tglperkiraanhamil.setTitle("Select Time");
    }

    private String getTglInseminasi(String idternak)
    {
        String tgl="";
        for (int i=0;i<list_addmengandung_idternak.size();i++)
        {
            if(list_addmengandung_idternak.get(i).equalsIgnoreCase(idternak)){
                tgl = list_addmengandung_tglinseminasi.get(i);
                break;
            }
        }
        return tgl;
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

    private boolean checkForm()
    {
        boolean value = true;

        if(TextUtils.isEmpty(input_addmengandung_activity_idternak.getText().toString())){
            value= false;
            input_addmengandung_activity_idternak.setError("ID Ternak belum diisi");
        }
        if(input_addmengandung_activity_tglpemeriksaan.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            value=false;
            input_addmengandung_activity_tglpemeriksaan.setError("Tanggal belum diisi");
        }
        if(input_addmengandung_activity_tglperkiraanhamil.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            value=false;
            input_addmengandung_activity_tglperkiraanhamil.setError("Tanggal belum diisi");
        }

        return value;
    }

    public void cleartext(){
        input_addmengandung_activity_idternak.setText("");
        input_addmengandung_activity_tglpemeriksaan.setText("01 Januari 1970");
        input_addmengandung_activity_tglperkiraanhamil.setText("01 Januari 1970");
        spinner_addmengandung_activity_statuskeberhasilan.setSelection(0);
    }

}

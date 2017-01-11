package com.fintech.ternaku.TernakPerah.Main.TambahData.Kesehatan;

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

public class AddCekPenyakit extends AppCompatActivity {
    private AutoCompleteTextView input_addcekpenyakit_activity_idternak;
    private Spinner spinner_addcekpenyakit_activity_jenisperiksa,spinner_addcekpenyakit_activity_kondisi,
            spinner_addcekpenyakit_activity_statuskesehatansusu;
    private TextView input_addcekpenyakit_activity_tglpemeriksaan;
    private EditText input_addcekpenyakit_activity_diagnosis,input_addcekpenyakit_activity_perawatan;
    private Button button_addcekpenyakit_activity_simpan;
    private LinearLayout linearLayout_addcekpenyakit_activity_susu;
    private DatePickerDialog fromDatePickerDialog_tglperiksa;
    private TimePickerDialog mTimePicker;
    private SimpleDateFormat dateFormatter_tglperiksa;
    private String datetime;
    int susuaman;
    ArrayList<String> list_addpenyakit_idternak = new ArrayList<String>();
    ArrayAdapter<String> adp;

    private Bluetooth bt;
    public final String TAG = "AddInseminasi";

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cek_penyakit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Cek Penyakit");

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_kesehatan)));
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //Set Auto Text------------------------------------
        String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
        new GetTernakId().execute(url.getUrl_GetTernakPengelompokkan(), param);
        input_addcekpenyakit_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addcekpenyakit_activity_idternak);
        input_addcekpenyakit_activity_idternak.setEnabled(false);
        adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_addpenyakit_idternak);
        input_addcekpenyakit_activity_idternak.setAdapter(adp);
        input_addcekpenyakit_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

        //Set Tanggal---------------------------------------
        setDateTimeField();
        setTime();
        input_addcekpenyakit_activity_tglpemeriksaan = (TextView)findViewById(R.id.input_addcekpenyakit_activity_tglpemeriksaan);
        dateFormatter_tglperiksa = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addcekpenyakit_activity_tglpemeriksaan.setText(dateFormatter_tglperiksa.format(Calendar.getInstance().getTime()));
        input_addcekpenyakit_activity_tglpemeriksaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddCekPenyakit.this);
                fromDatePickerDialog_tglperiksa.show();
            }
        });

        //Set Spinner All------------------------------------
        initSpinner();

        //Insert To Database--------------------------------
        input_addcekpenyakit_activity_diagnosis=(EditText)findViewById(R.id.input_addcekpenyakit_activity_diagnosis);
        input_addcekpenyakit_activity_perawatan=(EditText)findViewById(R.id.input_addcekpenyakit_activity_perawatan);
        button_addcekpenyakit_activity_simpan = (Button)findViewById(R.id.button_addcekpenyakit_activity_simpan);
        button_addcekpenyakit_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForm()) {
                    if(spinner_addcekpenyakit_activity_jenisperiksa.getSelectedItemId()==0){
                        new SweetAlertDialog(AddCekPenyakit.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Silahkan Pilih Jenis Pemeriksaan Penyakit")
                                .show();
                    }else{
                        if(spinner_addcekpenyakit_activity_kondisi.getSelectedItemId()==0) {
                            new SweetAlertDialog(AddCekPenyakit.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Peringatan!")
                                    .setContentText("Silahkan Pilih Kondisi Sapi")
                                    .show();
                        }else{
                            if(spinner_addcekpenyakit_activity_jenisperiksa.getSelectedItemId()==2){
                                if(spinner_addcekpenyakit_activity_statuskesehatansusu.getSelectedItemId()==0){
                                    new SweetAlertDialog(AddCekPenyakit.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Peringatan!")
                                            .setContentText("Silahkan Pilih Kondisi Kesehatan Susu")
                                            .show();
                                }else{
                                    new SweetAlertDialog(AddCekPenyakit.this, SweetAlertDialog.WARNING_TYPE)
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
                                                    urlParameters2 = "id=" + input_addcekpenyakit_activity_idternak.getText().toString().trim() +
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
                            }
                        }
                    }
                }
                else{
                    new SweetAlertDialog(AddCekPenyakit.this, SweetAlertDialog.ERROR_TYPE)
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
                    input_addcekpenyakit_activity_idternak.setText(msg.obj.toString().trim());
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

    //Set AutoComplete-----------------------------------------------------------
    private class GetTernakId extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddCekPenyakit.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddCekPenyakit.this, SweetAlertDialog.ERROR_TYPE)
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
                input_addcekpenyakit_activity_idternak.setEnabled(true);
            }
        }
    }
    private void AddTernakToList(String result) {
        list_addpenyakit_idternak.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addpenyakit_idternak.add(jObj.getString("id_ternak"));
            }
            adp.notifyDataSetChanged();
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private class CheckRFID extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(AddCekPenyakit.this, SweetAlertDialog.PROGRESS_TYPE);

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
                String diagnosis = "N/A", perawatan = "N/A";
                if (!input_addcekpenyakit_activity_diagnosis.getText().toString().matches("")) {
                    diagnosis = input_addcekpenyakit_activity_diagnosis.getText().toString();
                }
                if (!input_addcekpenyakit_activity_perawatan.getText().toString().matches("")) {
                    perawatan = input_addcekpenyakit_activity_perawatan.getText().toString();
                }
                String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                        + "&idternak=" + input_addcekpenyakit_activity_idternak.getText().toString().trim()
                        + "&tglperiksa=" + input_addcekpenyakit_activity_tglpemeriksaan.getText().toString()
                        + "&perawatan=" + perawatan
                        + "&diagnosis=" + diagnosis
                        + "&tglperiksaberikutnya=0000-00-00 00:00:00"
                        + "&biayaperiksa=0"
                        + "&susuaman=" + susuaman
                        + "&statusfisik=" + spinner_addcekpenyakit_activity_kondisi.getSelectedItem().toString()
                        + "&jenisperiksa=" + spinner_addcekpenyakit_activity_jenisperiksa.getSelectedItem().toString().toUpperCase();
                new InsertCekPenyakit().execute(url.getUrl_InsertPenyakit(), param);
            }else{
                new SweetAlertDialog(AddCekPenyakit.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("Tidak Ada RFID Ditemukan")
                        .show();
            }
        }
    }

    //Insert To Database------------------------------
    private class InsertCekPenyakit extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddCekPenyakit.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddCekPenyakit.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(AddCekPenyakit.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Cek Penyakit")
                                        .setContentText("Apakah Ingin Menambah Data Pengecekan Penyakit Lagi?")
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

    private void initSpinner(){
        spinner_addcekpenyakit_activity_jenisperiksa = (Spinner)findViewById(R.id.spinner_addcekpenyakit_activity_jenisperiksa);
        spinner_addcekpenyakit_activity_statuskesehatansusu = (Spinner)findViewById(R.id.spinner_addcekpenyakit_activity_statuskesehatansusu);
        spinner_addcekpenyakit_activity_kondisi = (Spinner)findViewById(R.id.spinner_addcekpenyakit_activity_kondisi);
        linearLayout_addcekpenyakit_activity_susu = (LinearLayout)findViewById(R.id.linearLayout_addcekpenyakit_activity_susu);

        final String[] spinPemeriksaanData= {"Pilih Jenis Pemeriksaan","Lameness","Mastitis","Kuku"};
        final String[] spinSusuData= {"Pilih Status Kesehatan Susu","Boleh diperah","Tidak boleh diperah"};
        final String[] spinKondisiData= {"Pilih Kondisi Sapi","Sakit","Tidak Sakit"};

        ArrayAdapter<String> myAdapter= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,spinPemeriksaanData);
        ArrayAdapter<String> myAdapter2= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,spinSusuData);
        ArrayAdapter<String> myAdapter3= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,spinKondisiData);

        spinner_addcekpenyakit_activity_jenisperiksa.setAdapter(myAdapter);
        spinner_addcekpenyakit_activity_statuskesehatansusu.setAdapter(myAdapter2);
        spinner_addcekpenyakit_activity_kondisi.setAdapter(myAdapter3);

        spinner_addcekpenyakit_activity_jenisperiksa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==2){
                    linearLayout_addcekpenyakit_activity_susu.setVisibility(View.VISIBLE);
                }else{
                    linearLayout_addcekpenyakit_activity_susu.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_addcekpenyakit_activity_statuskesehatansusu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==1){
                    susuaman = 2;
                }else if(i==2){
                    susuaman = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog_tglperiksa = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addcekpenyakit_activity_tglpemeriksaan.setText(dateFormatter_tglperiksa.format(newDate.getTime()));
                datetime = dateFormatter_tglperiksa.format(newDate.getTime());
                mTimePicker.show();
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(AddCekPenyakit.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(timePicker.isShown()) {
                    datetime += " " + selectedHour + ":" + selectedMinute + ":00";
                    input_addcekpenyakit_activity_tglpemeriksaan.setText(datetime);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
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

        if(TextUtils.isEmpty(input_addcekpenyakit_activity_idternak.getText().toString())){
            value= false;
            input_addcekpenyakit_activity_idternak.setError("ID Ternak belum diisi");
        }
        if(input_addcekpenyakit_activity_tglpemeriksaan.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            value=false;
            input_addcekpenyakit_activity_tglpemeriksaan.setError("Tanggal belum diisi");
        }
        return value;
    }

    public void cleartext(){
        input_addcekpenyakit_activity_idternak.setText("");
        input_addcekpenyakit_activity_diagnosis.setText("");
        input_addcekpenyakit_activity_perawatan.setText("");
        input_addcekpenyakit_activity_tglpemeriksaan.setText("01 Januari 1970");
        spinner_addcekpenyakit_activity_jenisperiksa.setSelection(0);
        spinner_addcekpenyakit_activity_kondisi.setSelection(0);
        spinner_addcekpenyakit_activity_statuskesehatansusu.setSelection(0);
    }


}

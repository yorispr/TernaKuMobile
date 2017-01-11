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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.Setting.Bluetooth;
import com.fintech.ternaku.UrlList;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddVaksinasi extends AppCompatActivity {
    private AutoCompleteTextView input_addvaksinasi_activity_idternak;
    private TextView input_addvaksinasi_activity_tglvaksinasi;
    private SearchableSpinner spinner_addvaksinasi_activity_namavaksin;
    private EditText input_addvaksinasi_activity_dosis,input_addvaksinasi_activity_satuan,input_addvaksinasi_activity_pemberianke;
    private Button button_addvaksinasi_activity_simpan;
    private DatePickerDialog fromDatePickerDialog_tglvaksinasi;
    private TimePickerDialog mTimePicker;
    private SimpleDateFormat dateFormatter_tglvaksinasi;
    String datetime;




    private Bluetooth bt;
    public final String TAG = "AddInseminasi";
    ArrayList<String> list_addvaksinasi_idvaksin = new ArrayList<String>();
    ArrayList<ModelAddVaksin> list_addvaksinasi_namavaksin = new ArrayList<ModelAddVaksin>();
    ArrayList<String> list_addvaksinasi_idternak = new ArrayList<String>();
    ArrayAdapter<String> adp;
    ArrayAdapter<String> myAdapter;
    private boolean cekKarantina;
    private int selectedindex=-1;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vaksinasi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Vaksinasi");

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_kesehatan)));
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //Set Text Input Id----------------------------------------------------------------------------
        String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
        new GetTernakId().execute(url.getUrl_GetTernakPengelompokkan(), param);
        input_addvaksinasi_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addvaksinasi_activity_idternak);
        input_addvaksinasi_activity_idternak.setEnabled(false);
        adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_addvaksinasi_idternak);
        input_addvaksinasi_activity_idternak.setAdapter(adp);
        input_addvaksinasi_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

        //Set Tanggal Vaksinasi--------------------------------------------------------------
        setDateTimeField();
        setTime();
        input_addvaksinasi_activity_tglvaksinasi = (TextView) findViewById(R.id.input_addvaksinasi_activity_tglvaksinasi);
        dateFormatter_tglvaksinasi = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addvaksinasi_activity_tglvaksinasi.setText(dateFormatter_tglvaksinasi.format(Calendar.getInstance().getTime()));
        input_addvaksinasi_activity_tglvaksinasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddVaksinasi.this);
                fromDatePickerDialog_tglvaksinasi.show();
            }
        });

        //Set Spinner Vaksin---------------------------------------------------------------
        myAdapter= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,list_addvaksinasi_idvaksin);
        spinner_addvaksinasi_activity_namavaksin = (SearchableSpinner)findViewById(R.id.spinner_addvaksinasi_activity_namavaksin);
        spinner_addvaksinasi_activity_namavaksin.setAdapter(myAdapter);
        spinner_addvaksinasi_activity_namavaksin.setTitle("Pilih Vaksin");
        spinner_addvaksinasi_activity_namavaksin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(),vaksinList.get(i).getNama(),Toast.LENGTH_LONG).show();
                selectedindex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Set Input Text---------------------------------------------------------------------
        input_addvaksinasi_activity_dosis = (EditText)findViewById(R.id.input_addvaksinasi_activity_dosis);
        input_addvaksinasi_activity_satuan = (EditText)findViewById(R.id.input_addvaksinasi_activity_satuan);
        input_addvaksinasi_activity_pemberianke = (EditText)findViewById(R.id.input_addvaksinasi_activity_pemberianke);

        //Insert To Database------------------------------------------------------------------
        new GetVaksin().execute(url.getUrl_GetVaksin(),"");
        button_addvaksinasi_activity_simpan = (Button)findViewById(R.id.button_addvaksinasi_activity_simpan);
        button_addvaksinasi_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForm()) {
                    if(selectedindex==-1){
                        new SweetAlertDialog(AddVaksinasi.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Silahkan Pilih Nama Vaksin")
                                .show();
                    }else{
                        new SweetAlertDialog(AddVaksinasi.this, SweetAlertDialog.WARNING_TYPE)
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
                                        urlParameters2 = "id=" + input_addvaksinasi_activity_idternak.getText().toString().trim() +
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
                                .show();                    }
                }

                else {
                    new SweetAlertDialog(AddVaksinasi.this, SweetAlertDialog.ERROR_TYPE)
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
                    input_addvaksinasi_activity_idternak.setText(msg.obj.toString().trim());
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
        SweetAlertDialog pDialog = new SweetAlertDialog(AddVaksinasi.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddVaksinasi.this, SweetAlertDialog.ERROR_TYPE)
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
                input_addvaksinasi_activity_idternak.setEnabled(true);
            }
        }
    }
    private void AddTernakToList(String result) {
        list_addvaksinasi_idternak.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addvaksinasi_idternak.add(jObj.getString("id_ternak"));
            }
            adp.notifyDataSetChanged();
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Get Data Vaksin--------------------------------------------------------------------
    private class GetVaksin extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddVaksinasi.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddVaksinasi.this, SweetAlertDialog.ERROR_TYPE)
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
                new SweetAlertDialog(AddVaksinasi.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Gagal Memuat Data")
                        .setContentText("Data Vaksin Masih Kosong")
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
                AddVaksinToList(result);
            }
        }
    }

    private void AddVaksinToList(String result) {
        list_addvaksinasi_namavaksin.clear();
        list_addvaksinasi_idvaksin.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                ModelAddVaksin v = new ModelAddVaksin(jObj.getString("ID_VAKSIN"),jObj.getString("NAMA_VAKSIN"),jObj.getString("UKURAN"),jObj.getString("SATUAN_UKURAN"),jObj.getString("KEGUNAAN"),jObj.getString("METODE_SIMPAN"),jObj.getString("PRODUSEN"));
                list_addvaksinasi_namavaksin.add(v);
                list_addvaksinasi_idvaksin.add(jObj.getString("NAMA_VAKSIN")+"\n"+jObj.getString("KEGUNAAN"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private class CheckRFID extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(AddVaksinasi.this, SweetAlertDialog.PROGRESS_TYPE);

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
                String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                        + "&idternak=" + input_addvaksinasi_activity_idternak.getText().toString().trim()
                        + "&idvaksin=" + list_addvaksinasi_namavaksin.get(selectedindex).getId()
                        + "&dosis=" + input_addvaksinasi_activity_dosis.getText().toString()
                        + "&satuandosis=" + input_addvaksinasi_activity_satuan.getText().toString()
                        + "&repetisi=" + input_addvaksinasi_activity_pemberianke.getText().toString()
                        + "&tglvaksinasi=" + input_addvaksinasi_activity_tglvaksinasi.getText().toString();
                new AddVaksinasitoDatabase().execute(url.getUrl_InsertVaksinasi(), param);
                Log.d("Param",param);
            }else{
                new SweetAlertDialog(AddVaksinasi.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("Tidak Ada RFID Ditemukan")
                        .show();
            }
        }
    }

    //Insert To Database--------------------------------------------------
    private class AddVaksinasitoDatabase extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddVaksinasi.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddVaksinasi.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(AddVaksinasi.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Cek Vaksinasi")
                                        .setContentText("Apakah Ingin Menambah Data Cek Vaksinasi Lagi?")
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
        fromDatePickerDialog_tglvaksinasi = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addvaksinasi_activity_tglvaksinasi.setText(dateFormatter_tglvaksinasi.format(newDate.getTime()));
                datetime = dateFormatter_tglvaksinasi.format(newDate.getTime());
                mTimePicker.show();
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(AddVaksinasi.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (timePicker.isShown()) {
                    datetime += " " + selectedHour + ":" + selectedMinute + ":00";
                    input_addvaksinasi_activity_tglvaksinasi.setText(datetime);
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


    private boolean checkForm()
    {
        boolean value = true;

        if(TextUtils.isEmpty(input_addvaksinasi_activity_idternak.getText().toString())){
            value= false;
            input_addvaksinasi_activity_idternak.setError("ID Ternak belum diisi");
        }
        if(input_addvaksinasi_activity_tglvaksinasi.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            value=false;
            input_addvaksinasi_activity_tglvaksinasi.setError("Tanggal belum diisi");
        }
        if(TextUtils.isEmpty(input_addvaksinasi_activity_dosis.getText().toString())){
            value= false;
            input_addvaksinasi_activity_dosis.setError("Dosis Vaksin Belum Diisi");
        }
        if(TextUtils.isEmpty(input_addvaksinasi_activity_satuan.getText().toString())){
            value= false;
            input_addvaksinasi_activity_satuan.setError("Satuan Dosis Belum Diisi");
        }
        if(TextUtils.isEmpty(input_addvaksinasi_activity_pemberianke.getText().toString())){
            value= false;
            input_addvaksinasi_activity_pemberianke.setError("Data Belum Diisi");
        }

        return value;
    }

    public void cleartext(){
        input_addvaksinasi_activity_idternak.setText("");
        input_addvaksinasi_activity_dosis.setText("");
        input_addvaksinasi_activity_satuan.setText("");
        input_addvaksinasi_activity_pemberianke.setText("");
        input_addvaksinasi_activity_tglvaksinasi.setText("01 Januari 1970");
        spinner_addvaksinasi_activity_namavaksin.clearFocus();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
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

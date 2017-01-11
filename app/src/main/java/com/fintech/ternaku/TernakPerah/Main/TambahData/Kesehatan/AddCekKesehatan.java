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
import android.widget.SeekBar;
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
import java.util.Locale;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.Setting.Bluetooth;
import com.fintech.ternaku.UrlList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddCekKesehatan extends AppCompatActivity {
    private AutoCompleteTextView input_addcekkesehatan_activity_idternak;
    private TextView input_addcekkesehatan_activity_tglpemeriksaan;
    private EditText input_addcekkesehatan_activity_suhubadan,input_addcekkesehatan_activity_beratbadan,
            input_addcekkesehatan_activity_tinggibadan,input_addcekkesehatan_activity_panjangbadan;
    private Spinner spinner_addcekkesehatan_activity_aktivitas,spinner_addcekkesehatan_activity_statusfisik,
            spinner_addcekkesehatan_activity_statusstress,spinner_addcekkesehatan_activity_produksisusu;
    private TextView input_addcekkesehatan_activity_conditionscore;
    private SeekBar seekbar_addcekkesehatan_activity_aktivitas;
    private Button button_addcekkesehatan_activity_simpan;

    private DatePickerDialog fromDatePickerDialog;
    private TimePickerDialog mTimePicker;
    private SimpleDateFormat dateFormatter;
    private ArrayList<String> list_add_cekkesehatan_idternak = new ArrayList<>();
    ArrayAdapter<String> adp;
    String datetime;
    int noOfTimesCalled = 0;

    private Bluetooth bt;
    public final String TAG = "AddInseminasi";

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cek_kesehatan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Cek Kesehatan");

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_kesehatan)));
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //Set AutoComplete--------------------------------------
        String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
        new GetTernakId().execute(url.getUrl_GetTernakPengelompokkan(), param);
        input_addcekkesehatan_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addcekkesehatan_activity_idternak);
        input_addcekkesehatan_activity_idternak.setEnabled(false);
        adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_add_cekkesehatan_idternak);
        input_addcekkesehatan_activity_idternak.setAdapter(adp);
        input_addcekkesehatan_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

        //Set Tanggal Pemeriksaan--------------------------------
        setDateTimeField();
        setTime();
        input_addcekkesehatan_activity_tglpemeriksaan = (TextView)findViewById(R.id.input_addcekkesehatan_activity_tglpemeriksaan);
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addcekkesehatan_activity_tglpemeriksaan.setText(dateFormatter.format(Calendar.getInstance().getTime()));
        input_addcekkesehatan_activity_tglpemeriksaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddCekKesehatan.this);
                fromDatePickerDialog.show();
            }
        });

        //Set Spinner Value--------------------------------------
        setSpinner();

        //Set Input Text Panjang,Berat,dll-----------------------
        input_addcekkesehatan_activity_suhubadan = (EditText)findViewById(R.id.input_addcekkesehatan_activity_suhubadan);
        input_addcekkesehatan_activity_beratbadan = (EditText)findViewById(R.id.input_addcekkesehatan_activity_beratbadan);
        input_addcekkesehatan_activity_tinggibadan = (EditText)findViewById(R.id.input_addcekkesehatan_activity_tinggibadan);
        input_addcekkesehatan_activity_panjangbadan = (EditText)findViewById(R.id.input_addcekkesehatan_activity_panjangbadan);

        //Set Seekbar--------------------------------------------
        input_addcekkesehatan_activity_conditionscore = (TextView)findViewById(R.id.input_addcekkesehatan_activity_conditionscore);
        seekbar_addcekkesehatan_activity_aktivitas = (SeekBar)findViewById(R.id.seekbar_addcekkesehatan_activity_aktivitas);
        seekbar_addcekkesehatan_activity_aktivitas.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                input_addcekkesehatan_activity_conditionscore.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Insert to Database-------------------------------------
        button_addcekkesehatan_activity_simpan = (Button)findViewById(R.id.button_addcekkesehatan_activity_simpan);
        button_addcekkesehatan_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForm()){
                    if(spinner_addcekkesehatan_activity_statusstress.getSelectedItemId()==0){
                        new SweetAlertDialog(AddCekKesehatan.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Silahkan Pilih Status Stress")
                                .show();
                    }else{
                        if(spinner_addcekkesehatan_activity_aktivitas.getSelectedItemId()==0){
                            new SweetAlertDialog(AddCekKesehatan.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Peringatan!")
                                    .setContentText("Silahkan Pilih Jenis Aktivitas")
                                    .show();
                        }else{
                            if(spinner_addcekkesehatan_activity_statusfisik.getSelectedItemId()==0){
                                new SweetAlertDialog(AddCekKesehatan.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Peringatan!")
                                        .setContentText("Silahkan Pilih Status Fisik")
                                        .show();
                            }else{
                                if(spinner_addcekkesehatan_activity_produksisusu.getSelectedItemId()==0){
                                    new SweetAlertDialog(AddCekKesehatan.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Peringatan!")
                                            .setContentText("Silahkan Pilih Tingkat Produksi Susu")
                                            .show();
                                }else{
                                    new SweetAlertDialog(AddCekKesehatan.this, SweetAlertDialog.WARNING_TYPE)
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
                                                    urlParameters2 = "id=" + input_addcekkesehatan_activity_idternak.getText().toString().trim() +
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
                }else{
                    new SweetAlertDialog(AddCekKesehatan.this, SweetAlertDialog.ERROR_TYPE)
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
                    input_addcekkesehatan_activity_idternak.setText(msg.obj.toString().trim());
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
        SweetAlertDialog pDialog = new SweetAlertDialog(AddCekKesehatan.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddCekKesehatan.this, SweetAlertDialog.ERROR_TYPE)
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
                input_addcekkesehatan_activity_idternak.setEnabled(true);
            }
        }
    }
    private void AddTernakToList(String result) {
        list_add_cekkesehatan_idternak.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_add_cekkesehatan_idternak.add(jObj.getString("id_ternak"));
            }
            adp.notifyDataSetChanged();
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private class CheckRFID extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(AddCekKesehatan.this, SweetAlertDialog.PROGRESS_TYPE);

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
                String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                        + "&idternak=" + input_addcekkesehatan_activity_idternak.getText().toString().trim()
                        + "&tglperiksa=" + input_addcekkesehatan_activity_tglpemeriksaan.getText().toString()
                        + "&suhubadan="+input_addcekkesehatan_activity_suhubadan.getText().toString()
                        + "&beratbadan="+input_addcekkesehatan_activity_beratbadan.getText().toString()
                        + "&tinggiternak="+input_addcekkesehatan_activity_tinggibadan.getText().toString()
                        + "&aktivitas="+spinner_addcekkesehatan_activity_aktivitas.getSelectedItem().toString()
                        + "&produksisusu="+spinner_addcekkesehatan_activity_produksisusu.getSelectedItem().toString()
                        + "&statusfisik="+spinner_addcekkesehatan_activity_statusfisik.getSelectedItem().toString()
                        + "&statusStress="+spinner_addcekkesehatan_activity_statusstress.getSelectedItem().toString()
                        + "&bodyscore="+input_addcekkesehatan_activity_conditionscore.getText().toString();

                new InsertKesehatan().execute(url.getUrl_InsertCekKesehatan(), urlParameters);
                Log.d("param",urlParameters);
            }else{
                new SweetAlertDialog(AddCekKesehatan.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("Tidak Ada RFID Ditemukan")
                        .show();
            }
        }
    }

    //Insert To Database--------------------------------------------------
    private class InsertKesehatan extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddCekKesehatan.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddCekKesehatan.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(AddCekKesehatan.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Cek Kesehatan")
                                        .setContentText("Apakah Ingin Menambah Data Cek Kesehatan Lagi?")
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

    private void setSpinner(){
        spinner_addcekkesehatan_activity_aktivitas = (Spinner)findViewById(R.id.spinner_addcekkesehatan_activity_aktivitas);
        spinner_addcekkesehatan_activity_aktivitas.setPrompt("Aktivitas");
        final String[] dataAktivitas= {"Pilih Jenis Aktivitas","Tinggi","Sedang","Rendah"};
        ArrayAdapter adapterAktivitas= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,dataAktivitas);
        spinner_addcekkesehatan_activity_aktivitas.setAdapter(adapterAktivitas);

        spinner_addcekkesehatan_activity_statusfisik = (Spinner)findViewById(R.id.spinner_addcekkesehatan_activity_statusfisik);
        spinner_addcekkesehatan_activity_statusfisik.setPrompt("Status Fisik");
        final String[] dataFisik= {"Pilih Status Fisik","Sehat","Tidak Sehat"};
        ArrayAdapter adapterFisik= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,dataFisik);
        spinner_addcekkesehatan_activity_statusfisik.setAdapter(adapterFisik);

        spinner_addcekkesehatan_activity_statusstress = (Spinner)findViewById(R.id.spinner_addcekkesehatan_activity_statusstress);
        final String[] dataStress= {"Pilih Tingkat Stress Ternak","Stress","Tidak Stress"};
        spinner_addcekkesehatan_activity_statusstress.setPrompt("Status Stress");
        ArrayAdapter adapterStress= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,dataStress);
        spinner_addcekkesehatan_activity_statusstress.setAdapter(adapterStress);

        spinner_addcekkesehatan_activity_produksisusu = (Spinner)findViewById(R.id.spinner_addcekkesehatan_activity_produksisusu);
        final String[] dataProduksi= {"Pilih Tingkat Produksi Susu","Tinggi","Sedang","Rendah","Belum Diketahui"};
        spinner_addcekkesehatan_activity_produksisusu.setPrompt("Produksi Susu");
        ArrayAdapter adapterProduksi= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,dataProduksi);
        spinner_addcekkesehatan_activity_produksisusu.setAdapter(adapterProduksi);
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addcekkesehatan_activity_tglpemeriksaan.setText(dateFormatter.format(newDate.getTime()));
                datetime = dateFormatter.format(newDate.getTime());
                mTimePicker.show();
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(AddCekKesehatan.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    if(timePicker.isShown()) {
                        datetime += " " + selectedHour + ":" + selectedMinute + ":00";
                        input_addcekkesehatan_activity_tglpemeriksaan.setText(datetime);
                        Log.d("Time", datetime);
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

    private boolean checkForm()
    {
        boolean value = true;

        if(TextUtils.isEmpty(input_addcekkesehatan_activity_idternak.getText().toString())){
            value= false;
            input_addcekkesehatan_activity_idternak.setError("ID Ternak belum diisi");
        }
        if(input_addcekkesehatan_activity_tglpemeriksaan.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            value=false;
            input_addcekkesehatan_activity_tglpemeriksaan.setError("Tanggal belum diisi");
        }
        if(TextUtils.isEmpty(input_addcekkesehatan_activity_panjangbadan.getText().toString())){
            value= false;
            input_addcekkesehatan_activity_panjangbadan.setError("Panjang Badan belum diisi");
        }
        if(TextUtils.isEmpty(input_addcekkesehatan_activity_tinggibadan.getText().toString())){
            value= false;
            input_addcekkesehatan_activity_tinggibadan.setError("Tinggi Badan belum diisi");
        }
        if(TextUtils.isEmpty(input_addcekkesehatan_activity_beratbadan.getText().toString())){
            value= false;
            input_addcekkesehatan_activity_beratbadan.setError("Berat Badan belum diisi");
        }
        if(TextUtils.isEmpty(input_addcekkesehatan_activity_suhubadan.getText().toString())){
            value= false;
            input_addcekkesehatan_activity_suhubadan.setError("Suhu Badan belum diisi");
        }

        return value;
    }

    public void cleartext(){
        input_addcekkesehatan_activity_idternak.setText("");
        input_addcekkesehatan_activity_panjangbadan.setText("");
        input_addcekkesehatan_activity_tinggibadan.setText("");
        input_addcekkesehatan_activity_beratbadan.setText("");
        input_addcekkesehatan_activity_suhubadan.setText("");
        input_addcekkesehatan_activity_tglpemeriksaan.setText("01 Januari 1970");
        spinner_addcekkesehatan_activity_aktivitas.setSelection(0);
        spinner_addcekkesehatan_activity_produksisusu.setSelection(0);
        spinner_addcekkesehatan_activity_statusfisik.setSelection(0);
        spinner_addcekkesehatan_activity_statusstress.setSelection(0);
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

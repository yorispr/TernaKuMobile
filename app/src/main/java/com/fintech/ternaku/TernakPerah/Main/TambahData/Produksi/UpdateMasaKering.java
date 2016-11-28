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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class UpdateMasaKering extends AppCompatActivity {
    private AutoCompleteTextView input_updatemasakering_activity_idternak;
    private TextView input_updatemasakering_activity_tglpemeriksaan;
    private RadioGroup radiogroup_updatemasakering_activity_mlaiselesai;
    private RadioButton radiobutton_updatemasakering_activity_mlai,radiobutton_updatemasakering_activity_selesai;
    private Button button_updatemasakering_activity_simpan;
    private TimePickerDialog mTimePicker;
    private DatePickerDialog DatePickerDialog_tgldryoff;
    private SimpleDateFormat dateFormatter_tgldryoff;
    String datetime;
    int flag_radio=0;
    private Bluetooth bt;
    public final String TAG = "UpdateMasaKering";
    boolean isMulai;
    int susuaman;
    private ArrayList<String> list_update_dryoff_idternak = new ArrayList<>();
    private ArrayList<String> list_update_dryoff_dry = new ArrayList<>();
    ArrayAdapter<String> adp;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_masa_kering);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Masa Kering");

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_produksi)));
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        hideSoftKeyboardOnStart();

        //Set Auto Text--------------------------------------------
        String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
        new GetTernakId().execute(url.getUrl_GetTernakPengelompokkan(), param);
        input_updatemasakering_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_updatemasakering_activity_idternak);
        input_updatemasakering_activity_idternak.setEnabled(false);
        adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_update_dryoff_idternak);
        input_updatemasakering_activity_idternak.setAdapter(adp);
        input_updatemasakering_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

        //Set Ternak Dry--------------------------------------------
        String param_2 = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
        new GetTernakDry().execute(url.getUrl_GetKering(), param_2);


        //Set Mulai Or Selesai--------------------------------------
        radiogroup_updatemasakering_activity_mlaiselesai = (RadioGroup)findViewById(R.id.radiogroup_updatemasakering_activity_mlaiselesai);
        radiogroup_updatemasakering_activity_mlaiselesai.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.radiobutton_updatemasakering_activity_mlai){
                    isMulai=true;
                    flag_radio=1;
                }else if(i==R.id.radiobutton_updatemasakering_activity_selesai){
                    isMulai=false;
                    flag_radio=1;
                }
            }
        });

        //Set Tanggal------------------------------------------------
        setDateTimeField();
        setTime();
        input_updatemasakering_activity_tglpemeriksaan = (TextView) findViewById(R.id.input_updatemasakering_activity_tglpemeriksaan);
        dateFormatter_tgldryoff = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_updatemasakering_activity_tglpemeriksaan = (TextView)findViewById(R.id.input_updatemasakering_activity_tglpemeriksaan);
        input_updatemasakering_activity_tglpemeriksaan.setText(dateFormatter_tgldryoff.format(Calendar.getInstance().getTime()));
        input_updatemasakering_activity_tglpemeriksaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(UpdateMasaKering.this);
                DatePickerDialog_tgldryoff.show();
            }
        });

        //Insert To Database------------------------------------------
        button_updatemasakering_activity_simpan = (Button)findViewById(R.id.button_updatemasakering_activity_simpan);
        button_updatemasakering_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cekForm()) {
                    if(flag_radio==1){
                        new SweetAlertDialog(UpdateMasaKering.this, SweetAlertDialog.WARNING_TYPE)
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
                                        urlParameters2 = "id=" + input_updatemasakering_activity_idternak.getText().toString().trim() +
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
                    }else if(flag_radio==0){
                        new SweetAlertDialog(UpdateMasaKering.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Pilih Mode Ganti Data Mulai atau Selesai")
                                .show();
                    }
                }
                else{
                    new SweetAlertDialog(UpdateMasaKering.this, SweetAlertDialog.ERROR_TYPE)
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
                    input_updatemasakering_activity_idternak.setText(msg.obj.toString().trim());
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

    //Get Data Ternak-------------------------------------------
    private class GetTernakId extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(UpdateMasaKering.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(UpdateMasaKering.this, SweetAlertDialog.ERROR_TYPE)
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
                input_updatemasakering_activity_idternak.setEnabled(true);
            }
        }
    }
    private void AddTernakToList(String result) {
        list_update_dryoff_idternak.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_update_dryoff_idternak.add(jObj.getString("id_ternak"));
            }
            adp.notifyDataSetChanged();
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Get Ternak Dry--------------------------------------------------------
    private class GetTernakDry extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(UpdateMasaKering.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("RES2",result);
            pDialog.dismiss();
            if(result.trim().equals("kosong")){
                new SweetAlertDialog(UpdateMasaKering.this, SweetAlertDialog.ERROR_TYPE)
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
                AddTernakHeat(result);
            }
        }
    }
    private void AddTernakHeat(String result)
    {
        list_update_dryoff_dry.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_update_dryoff_dry.add(jObj.getString("id_ternak"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private class CheckRFID extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(UpdateMasaKering.this, SweetAlertDialog.PROGRESS_TYPE);

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
                final String idter = input_updatemasakering_activity_idternak.getText().toString();
                if(isMulai) {
                    if (!isDry(idter)) {
                        String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                + "&idternak=" + idter
                                + "&tglmulaidry=" + input_updatemasakering_activity_tglpemeriksaan.getText().toString();
                        new AddDry().execute(url.getUrlInsertMasaKeringMulai(), param);
                    } else {
                        new SweetAlertDialog(UpdateMasaKering.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Ternak Sedang Masa Kering")
                                .show();
                    }
                }else{
                    if (isDry(idter)) {
                        String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                + "&idternak=" + idter
                                + "&tglselesaidry=" + input_updatemasakering_activity_tglpemeriksaan.getText().toString();
                        new AddDry().execute(url.getUrlInsertMasaKeringSelesai(), param);
                    } else {
                        new SweetAlertDialog(UpdateMasaKering.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Ternak Tidak Sedang Masa Kering")
                                .show();
                    }
                }
                //Set Ternak Dry--------------------------------------------
                String param_2 = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
                new GetTernakDry().execute(url.getUrl_GetKering(), param_2);
            }else{
                new SweetAlertDialog(UpdateMasaKering.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("Tidak Ada RFID Ditemukan")
                        .show();
            }
        }
    }

    //Insert To Database----------------------------------------------------
    private class AddDry extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(UpdateMasaKering.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(UpdateMasaKering.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(UpdateMasaKering.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Ubah Masa Kering")
                                        .setContentText("Apakah Ingin Mengubah Data Masa Kering Lagi?")
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
        }
    }

    private boolean isDry(String id){
        boolean cek=false;
        for(int i=0;i<list_update_dryoff_dry.size();i++){
            if(id.equals(list_update_dryoff_dry.get(i))){
                cek = true;
            }
        }
        return cek;
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog_tgldryoff = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_updatemasakering_activity_tglpemeriksaan.setText(dateFormatter_tgldryoff.format(newDate.getTime()));
                datetime = dateFormatter_tgldryoff.format(newDate.getTime());
                mTimePicker.show();
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(UpdateMasaKering.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(timePicker.isShown()) {

                    datetime += " " + selectedHour + ":" + selectedMinute + ":00";
                    input_updatemasakering_activity_tglpemeriksaan.setText(datetime);
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

    public void hideSoftKeyboardOnStart() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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

    public boolean cekForm(){
        boolean cek = true;
        if(TextUtils.isEmpty(input_updatemasakering_activity_idternak.getText().toString())){
            cek = false;
            input_updatemasakering_activity_idternak.setError("Data belum diisi");
        }
        if(input_updatemasakering_activity_tglpemeriksaan.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            cek=false;
            input_updatemasakering_activity_tglpemeriksaan.setError("Data belum diisi");
        }
        return cek;
    }

    public void cleartext(){
        input_updatemasakering_activity_idternak.setText("");
        input_updatemasakering_activity_tglpemeriksaan.setText("01 Januari 1970");
        radiogroup_updatemasakering_activity_mlaiselesai.clearCheck();
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

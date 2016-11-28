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

public class AddMenyusui extends AppCompatActivity {
    private AutoCompleteTextView input_addmenyusui_activity_idternak;
    private RadioGroup radiogroup_addmenyusui_activity_mlaiselesai;
    private RadioButton radiobutton_addmenyusui_activity_mlai,radiobutton_addmenyusui_activity_selesai;
    private TextView input_addmenyusui_activity_tglpemeriksaan,txt_addmenyusui_activity_ket;
    private TimePickerDialog mTimePicker;
    private DatePickerDialog DatePickerDialog_tglmeyusui;
    private SimpleDateFormat dateFormatter_tglmenyusui;
    private Button button_addmenyusui_activity_simpan;
    String datetime;
    private boolean cekMenyusui=false;
    private int choosenindex =-1;
    int flag_radio=0;

    private Bluetooth bt;
    public final String TAG = "AddInseminasi";

    ArrayList<String> list_addmenyusui_idternak = new ArrayList<String >();
    ArrayList<String> list_addmenyusui_menyusui = new ArrayList<String >();
    ArrayList<String> list_addmenyusui_id_ternak_autocomplete = new ArrayList<String >();
    ArrayAdapter<String> adp;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menyusui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Menyusui");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_kesuburan)));
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //Set Auto Text Id Ternak---------------------------------------------
        String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetTernakId().execute(url.getUrl_GetTernakPengelompokkan(), urlParameters);
        input_addmenyusui_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addmenyusui_activity_idternak);
        input_addmenyusui_activity_idternak.setEnabled(false);
        adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_addmenyusui_idternak);
        input_addmenyusui_activity_idternak.setAdapter(adp);
        input_addmenyusui_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosenindex = i;
            }
        });
        input_addmenyusui_activity_idternak.setEnabled(false);

        //Set Radio Button-------------------------------------------------------
        radiogroup_addmenyusui_activity_mlaiselesai = (RadioGroup) findViewById(R.id.radiogroup_addmenyusui_activity_mlaiselesai);
        txt_addmenyusui_activity_ket = (TextView)findViewById(R.id.txt_addmenyusui_activity_ket);
        radiogroup_addmenyusui_activity_mlaiselesai.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radiobutton_addmenyusui_activity_mlai) {
                    flag_radio=1;
                    cekMenyusui=true;
                    txt_addmenyusui_activity_ket.setText("Tanggal mulai");
                } else if(checkedId == R.id.radiobutton_addmenyusui_activity_selesai) {
                    flag_radio=1;
                    cekMenyusui=false;
                    txt_addmenyusui_activity_ket.setText("Tanggal selesai");
                }
            }
        });

        //Set Tanggal Menyusui------------------------------------------------------
        setDateTimeField();
        setTime();
        input_addmenyusui_activity_tglpemeriksaan = (TextView)findViewById(R.id.input_addmenyusui_activity_tglpemeriksaan);
        dateFormatter_tglmenyusui = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addmenyusui_activity_tglpemeriksaan.setText(dateFormatter_tglmenyusui.format(Calendar.getInstance().getTime()));
        input_addmenyusui_activity_tglpemeriksaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddMenyusui.this);
                DatePickerDialog_tglmeyusui.show();
            }
        });

        //Insert Into Database-----------------------------------------------------
        button_addmenyusui_activity_simpan = (Button)findViewById(R.id.button_addmenyusui_activity_simpan);
        button_addmenyusui_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForm()){
                    if(flag_radio==1) {
                        if(cekMenyusui){

                            Connection c = new Connection();
                            String urlParametersRFID;
                            urlParametersRFID = "idternak=" + input_addmenyusui_activity_idternak.getText().toString().trim() +
                                    "&uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
                            final String idter = c.GetJSONfromURL(url.getUrl_GetIdRFID(), urlParametersRFID);
                            Log.d("IdTer",idter);
                            if(!isMenyusui(idter.trim())){
                                new SweetAlertDialog(AddMenyusui.this, SweetAlertDialog.WARNING_TYPE)
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
                                                urlParameters2 = "id=" + input_addmenyusui_activity_idternak.getText().toString().trim() +
                                                        "&idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null);
                                                String json = c.GetJSONfromURL(url.getUrlGet_RFIDanIdCek(), urlParameters2);
                                                if(json.trim().equals("1")) {
                                                    String status="1";
                                                    String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                                            + "&idternak=" + input_addmenyusui_activity_idternak.getText().toString().trim()
                                                            + "&tglperiksa=" + input_addmenyusui_activity_tglpemeriksaan.getText().toString()
                                                            + "&statusmenyusui=" + status;
                                                    new UpdateMenyusui().execute(url.getUrl_InsertMenyusui(), urlParameters);
                                                    Log.d("param", urlParameters);
                                                }else{
                                                    new SweetAlertDialog(AddMenyusui.this, SweetAlertDialog.WARNING_TYPE)
                                                            .setTitleText("Peringatan!")
                                                            .setContentText("Tidak Ada RFID Ditemukan")
                                                            .show();
                                                }
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
                                new SweetAlertDialog(AddMenyusui.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Peringatan!")
                                        .setContentText("Ternak Tersebut Belum Selesai Menyusui")
                                        .show();
                            }
                        }else{
                            Connection c = new Connection();
                            String urlParametersRFID;
                            urlParametersRFID = "idternak=" + input_addmenyusui_activity_idternak.getText().toString().trim() +
                                    "&uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
                            final String idter = c.GetJSONfromURL(url.getUrl_GetIdRFID(), urlParametersRFID);
                            Log.d("IdTer",idter);
                            if(isMenyusui(idter.trim())){
                                new SweetAlertDialog(AddMenyusui.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Simpan")
                                        .setContentText("Data Yang Dimasukkan Sudah Benar?")
                                        .setConfirmText("Ya")
                                        .setCancelText("Tidak")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();

                                                Connection c = new Connection();
                                                String urlParameters2;
                                                urlParameters2 = "id=" + input_addmenyusui_activity_idternak.getText().toString().trim() +
                                                        "&idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null);
                                                String json = c.GetJSONfromURL(url.getUrlGet_RFIDanIdCek(), urlParameters2);
                                                if(json.trim().equals("1")) {
                                                    String status="0";
                                                    String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                                            + "&idternak=" + input_addmenyusui_activity_idternak.getText().toString().trim()
                                                            + "&tglperiksa=" + input_addmenyusui_activity_tglpemeriksaan.getText().toString()
                                                            + "&statusmenyusui=" + status;
                                                    new UpdateMenyusui().execute(url.getUrl_InsertMenyusui(), urlParameters);
                                                    Log.d("param", urlParameters);
                                                }else{
                                                    new SweetAlertDialog(AddMenyusui.this, SweetAlertDialog.WARNING_TYPE)
                                                            .setTitleText("Peringatan!")
                                                            .setContentText("Tidak Ada RFID Ditemukan")
                                                            .show();
                                                }
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
                                new SweetAlertDialog(AddMenyusui.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Peringatan!")
                                        .setContentText("Ternak Tersebut Belum Mulai Menyusui")
                                        .show();
                            }
                        }
                    }else if(flag_radio==0){
                        new SweetAlertDialog(AddMenyusui.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Pilih Mode Ganti Data Mulai Menyusui atau Selesai Menyusui")
                                .show();
                    }
                }else {
                    new SweetAlertDialog(AddMenyusui.this, SweetAlertDialog.ERROR_TYPE)
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
                    input_addmenyusui_activity_idternak.setText(msg.obj.toString().trim());
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

    //Get Data AutoText------------------------------------------------------
    private class GetTernakId extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddMenyusui.this, SweetAlertDialog.PROGRESS_TYPE);
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
            if (result.trim().equals("kosong")){
                new SweetAlertDialog(AddMenyusui.this, SweetAlertDialog.ERROR_TYPE)
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
            }
            else {
                AddTernakToList(result);
                input_addmenyusui_activity_idternak.setEnabled(true);

                //Set Ternak Melahirkan-------------------------------------------------
                String param_2 = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
                new GetTernakMenyusui().execute(url.getUrl_GetMenyusui(), param_2);

            }
        }
    }
    private void AddTernakToList(String result)
    {
        list_addmenyusui_idternak.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addmenyusui_idternak.add(jObj.getString("id_ternak"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Get Data Ternak Menyusui------------------------------------------
    private class GetTernakMenyusui extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddMenyusui.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddMenyusui.this, SweetAlertDialog.ERROR_TYPE)
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
                AddTernakMenyusui(result);
            }
        }
    }


    private void AddTernakMenyusui(String result)
    {
        int flag=0;
        list_addmenyusui_menyusui.clear();
        list_addmenyusui_id_ternak_autocomplete.clear();

        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addmenyusui_menyusui.add(jObj.getString("id_ternak"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }


    //Insert into Database---------------------------------------------------
    private class UpdateMenyusui extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddMenyusui.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("RESInsertMenyusui",result);
            pDialog.dismiss();
            if (result.trim().equals("1")){
                new SweetAlertDialog(AddMenyusui.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(AddMenyusui.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Ternak Menyusui")
                                        .setContentText("Apakah Ingin Menambah Data Ternak Menyusui Lagi?")
                                        .setConfirmText("Ya")
                                        .setCancelText("Tidak")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                                cleartext();

                                                //Refresh---------------------------------------------------------
                                                String urlParameters_ref = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
                                                new GetTernakId().execute(url.getUrl_GetTernakPengelompokkan(), urlParameters_ref);

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

    private boolean isMenyusui(String id){
        boolean cek=false;
        for(int i=0;i<list_addmenyusui_menyusui.size();i++){
            if(id.equalsIgnoreCase(list_addmenyusui_menyusui.get(i))){
                cek = true;
            }
        }
        return cek;
    }

    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(AddMenyusui.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(timePicker.isShown()) {

                    //txtJam.setText( selectedHour + ":" + selectedMinute+":00");
                    datetime += " " + selectedHour + ":" + selectedMinute + ":00";
                    input_addmenyusui_activity_tglpemeriksaan.setText(datetime);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
    }
    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog_tglmeyusui = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addmenyusui_activity_tglpemeriksaan.setText(dateFormatter_tglmenyusui.format(newDate.getTime()));
                datetime = dateFormatter_tglmenyusui.format(newDate.getTime());
                mTimePicker.show();
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    private boolean checkForm()
    {
        boolean cek = true;

        if(input_addmenyusui_activity_idternak.getText().toString().matches(""))
        {
            input_addmenyusui_activity_idternak.setError("ID Ternak belum diisi");
            cek = false;
        }
        if(input_addmenyusui_activity_tglpemeriksaan.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            cek=false;
            input_addmenyusui_activity_tglpemeriksaan.setError("Tanggal belum diisi");
        }
        return cek;
    }

    public void cleartext(){
        input_addmenyusui_activity_idternak.setText("");
        input_addmenyusui_activity_tglpemeriksaan.setText("01 Januari 1970");
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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}

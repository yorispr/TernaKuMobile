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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fintech.ternaku.TernakPerah.Alarm.Alarm;
import com.fintech.ternaku.TernakPerah.Alarm.AlarmScheduler;
import com.fintech.ternaku.Connection;
import com.fintech.ternaku.DatabaseHandler;
import com.fintech.ternaku.R;
import com.fintech.ternaku.Setting.Bluetooth;
import com.fintech.ternaku.UrlList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddMasaSubur extends AppCompatActivity {
    private AutoCompleteTextView input_addmasasubur_activity_idternak;
    private RadioGroup radiogroup_addmasasubur_activity_mlaiselesai;
    private RadioButton radiobutton_addmasasubur_activity_mlai,radiobutton_addmasasubur_activity_selesai;
    private TextView input_addmasasubur_activity_tglpemeriksaan,txt_addmasasubur_activity_ket;
    private EditText input_addmasasubur_activity_diagnosis,input_addmasasubur_activity_perawatan,input_addmasasubur_activity_biaya;
    private Button button_addmasasubur_activity_simpan;
    private LinearLayout linearLayout_addmasasubur_activity_diagnosis,linearLayout_addmasasubur_activity_biaya;
    private DatePickerDialog DatePickerDialog_tglpemeriksaan;
    private SimpleDateFormat dateFormatter_tglpemeriksaan;
    private TimePickerDialog mTimePicker;
    String datetime;
    int choosenindex=-1;
    public final String TAG = "AddMasaSubur";
    private Bluetooth bt;

    ArrayList<String> list_addmasasubur_idternak = new ArrayList<String>();
    ArrayList<String> list_addmasasubur_ternakheat = new ArrayList<String>();
    ArrayAdapter<String> myAdapter;
    boolean isHeat;
    int flag_radio=0;
    String id_sapi;
    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    private String GetIdRFID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_masa_subur);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        id_sapi = "";
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Masa Subur Ternak");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_kesuburan)));
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }



        //Set Id Ternak Auto Complete-------------------------------------
        String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
        new GetTernakIdTernak().execute(url.getUrl_GetTernakPengelompokkan(), param);
        input_addmasasubur_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addmasasubur_activity_idternak);
        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_addmasasubur_idternak);
        input_addmasasubur_activity_idternak.setAdapter(adp);


        //Set Data Periksa Mulai atau Selesai-----------------------------
        linearLayout_addmasasubur_activity_diagnosis = (LinearLayout)findViewById(R.id.linearLayout_addmasasubur_activity_diagnosis);
        linearLayout_addmasasubur_activity_biaya = (LinearLayout)findViewById(R.id.linearLayout_addmasasubur_activity_biaya);
        radiogroup_addmasasubur_activity_mlaiselesai = (RadioGroup) findViewById(R.id.radiogroup_addmasasubur_activity_mlaiselesai);
        txt_addmasasubur_activity_ket = (TextView)findViewById(R.id.txt_addmasasubur_activity_ket);
        radiogroup_addmasasubur_activity_mlaiselesai.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radiobutton_addmasasubur_activity_mlai) {
                    flag_radio=1;
                    isHeat = true;
                    txt_addmasasubur_activity_ket.setText("Tanggal Heat");
                    linearLayout_addmasasubur_activity_diagnosis.setVisibility(View.VISIBLE);
                    linearLayout_addmasasubur_activity_biaya.setVisibility(View.VISIBLE);

                } else if(checkedId == R.id.radiobutton_addmasasubur_activity_selesai) {
                    flag_radio=1;
                    isHeat = false;
                    txt_addmasasubur_activity_ket.setText("Tanggal Selesai Heat");
                    linearLayout_addmasasubur_activity_diagnosis.setVisibility(View.GONE);
                    linearLayout_addmasasubur_activity_biaya.setVisibility(View.GONE);
                }
            }
        });



        //Set Tanggal Pemeriksaan------------------------------------------
        setDateTimeField();
        setTime();
        input_addmasasubur_activity_tglpemeriksaan = (TextView)findViewById(R.id.input_addmasasubur_activity_tglpemeriksaan);
        dateFormatter_tglpemeriksaan = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addmasasubur_activity_tglpemeriksaan.setText(dateFormatter_tglpemeriksaan.format(Calendar.getInstance().getTime()));
        input_addmasasubur_activity_tglpemeriksaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddMasaSubur.this);
                DatePickerDialog_tglpemeriksaan.show();
            }
        });

        //Set Hasil Pemeriksaan---------------------------------------------
        input_addmasasubur_activity_diagnosis = (EditText)findViewById(R.id.input_addmasasubur_activity_diagnosis);
        input_addmasasubur_activity_perawatan = (EditText)findViewById(R.id.input_addmasasubur_activity_perawatan);
        input_addmasasubur_activity_biaya = (EditText)findViewById(R.id.input_addmasasubur_activity_biaya);

        //Simpan Data-------------------------------------------------------
        button_addmasasubur_activity_simpan = (Button)findViewById(R.id.button_addmasasubur_activity_simpan);
        button_addmasasubur_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForm()) {
                    if(flag_radio==1) {
                        final String idter = input_addmasasubur_activity_idternak.getText().toString().trim();

                        new SweetAlertDialog(AddMasaSubur.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Simpan")
                                .setContentText("Data Yang Dimasukkan Sudah Benar?")
                                .setConfirmText("Ya")
                                .setCancelText("Tidak")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        id_sapi = input_addmasasubur_activity_idternak.getText().toString().trim();

                                        //Cek RFID---------------------------------
                                        Connection c = new Connection();
                                        String urlParameters2;
                                        urlParameters2 = "id=" + input_addmasasubur_activity_idternak.getText().toString().trim() +
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
                        new SweetAlertDialog(AddMasaSubur.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Pilih Mode Ganti Data Mulai atau Selesai")
                                .show();
                    }

                }else{
                    new SweetAlertDialog(AddMasaSubur.this, SweetAlertDialog.ERROR_TYPE)
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

    private void setbluetooth(){

    }

    @Override
    protected void onResume(){
        super.onResume();
        bt = new Bluetooth(this, mHandler);
        bt.start();
        bt.connectDevice("HC-06");    }

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
                    input_addmasasubur_activity_idternak.setText(msg.obj.toString().trim());
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


    //Get Data Ternak AutoComplete------------------------------------------
    private class GetTernakIdTernak extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddMasaSubur.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("RES1",result);
            pDialog.dismiss();
            if(result.trim().equals("kosong")){
                new SweetAlertDialog(AddMasaSubur.this, SweetAlertDialog.ERROR_TYPE)
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
                new SweetAlertDialog(AddMasaSubur.this, SweetAlertDialog.ERROR_TYPE)
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
            }
            else{
                AddTernakToList(result);
                input_addmasasubur_activity_idternak.setEnabled(true);

                //Set Ternak Heat-------------------------------------------------
                String param_2 = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
                new GetTernakHeat().execute(url.getUrl_GetHeat(), param_2);

            }
        }
    }
    private void AddTernakToList(String result){
        list_addmasasubur_idternak.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addmasasubur_idternak.add(jObj.getString("id_ternak"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Get Data Ternak Heat------------------------------------------
    private class GetTernakHeat extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddMasaSubur.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddMasaSubur.this, SweetAlertDialog.ERROR_TYPE)
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
    private void AddTernakHeat(String result) {
        list_addmasasubur_ternakheat.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addmasasubur_ternakheat.add(jObj.getString("id_ternak"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private class CheckRFID extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(AddMasaSubur.this, SweetAlertDialog.PROGRESS_TYPE);

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
            if(result.trim().equals("1")) {
                if(isHeat) {
                    String perawatan = "N/A";
                    String diagnosis = "N/A";
                    String biayaperiksa = "0";


                    Connection c = new Connection();
                    String urlParametersRFID;
                    urlParametersRFID = "idternak=" + input_addmasasubur_activity_idternak.getText().toString().trim() +
                            "&uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
                    String idter = c.GetJSONfromURL(url.getUrl_GetIdRFID(), urlParametersRFID);
                    Log.d("IdTer",idter);

                    if (!isSedangHeat(idter.trim())) {
                        pDialog.dismiss();
                        if (!input_addmasasubur_activity_biaya.getText().toString().matches("")) {
                            biayaperiksa = input_addmasasubur_activity_biaya.getText().toString();
                        }
                        if (!input_addmasasubur_activity_diagnosis.getText().toString().matches("")) {
                            diagnosis = input_addmasasubur_activity_diagnosis.getText().toString();
                        }
                        if (!input_addmasasubur_activity_perawatan.getText().toString().matches("")) {
                            perawatan = input_addmasasubur_activity_perawatan.getText().toString();
                        }
                        String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                + "&idternak=" + idter.trim()
                                + "&tglmulaiheat=" + input_addmasasubur_activity_tglpemeriksaan.getText().toString()
                                + "&diagnosis=" + diagnosis
                                + "&perawatan=" + perawatan
                                + "&biayaperiksa=" + biayaperiksa;
                        new UpdateMasaSubur().execute(url.getUrl_InsertHeatMulai(), urlParameters);
                        Log.d("TglMulai",urlParameters);
                    } else {
                        pDialog.dismiss();
                        new SweetAlertDialog(AddMasaSubur.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Ternak Sedang Masa Subur")
                                .show();
                    }
                }else{
                    Connection c = new Connection();
                    String urlParametersRFID;
                    urlParametersRFID = "idternak=" + input_addmasasubur_activity_idternak.getText().toString().trim() +
                            "&uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
                    String idter = c.GetJSONfromURL(url.getUrl_GetIdRFID(), urlParametersRFID);

                    if (isSedangHeat(idter.trim())) {
                        pDialog.dismiss();
                        String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                + "&idternak=" + idter.trim()
                                + "&tglselesaiheat=" + input_addmasasubur_activity_tglpemeriksaan.getText().toString();
                        new UpdateMasaSubur().execute(url.getUrl_InsertHeatSelesai(), urlParameters);
                        Log.d("TglSelesai",urlParameters);
                    } else {
                        pDialog.dismiss();
                        new SweetAlertDialog(AddMasaSubur.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Ternak Tidak Sedang Masa Subur")
                                .show();
                    }
                }
                //Set Ternak Heat-------------------------------------------------
                String param_2 = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
                new GetTernakHeat().execute(url.getUrl_GetHeat(), param_2);

            }else{
                new SweetAlertDialog(AddMasaSubur.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("Tidak Ada RFID Ditemukan")
                        .show();
            }
        }
    }

    //Insert To Database------------------------------------------------
    private class UpdateMasaSubur extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddMasaSubur.this, SweetAlertDialog.PROGRESS_TYPE);

        @Override
        protected void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#fa6900"));
            pDialog.setTitleText("Menyimapn Data");
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
            final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            Log.d("RESRFID",result);
            pDialog.dismiss();
            if (result.trim().equals("1")){
                new SweetAlertDialog(AddMasaSubur.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                if(isHeat){
                                    final int _id = (int) System.currentTimeMillis();
                                    Calendar cal = Calendar.getInstance();
                                    Date date = cal.getTime();
                                    String formatteddate = new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(date);
                                    cal.add(Calendar.MINUTE,1);
                                    Log.d("calendar3",formatteddate);

                                    Alarm al = new Alarm(0,String.valueOf(_id),"heat",String.valueOf(new Date()),formatteddate,id_sapi);
                                    db.addAlarm(al);
                                    AlarmScheduler as = new AlarmScheduler();
                                    as.setAlarm(al,getApplicationContext());
                                    Log.d("id_sapi2",al.getId_sapi());

                                }
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(AddMasaSubur.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Ubah Masa Subur")
                                        .setContentText("Apakah Ingin Menambah Data Masa Subur Lagi?")
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
                input_addmasasubur_activity_tglpemeriksaan.setText(dateFormatter_tglpemeriksaan.format(newDate.getTime()));
                datetime = dateFormatter_tglpemeriksaan.format(newDate.getTime());
                mTimePicker.show();
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(AddMasaSubur.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(timePicker.isShown()) {
                    //txtJam.setText( selectedHour + ":" + selectedMinute+":00");
                    datetime += " " + selectedHour + ":" + selectedMinute + ":00";
                    input_addmasasubur_activity_tglpemeriksaan.setText(datetime);
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
    private boolean isSedangHeat(String id){
        boolean cek=false;
        for(int i=0;i<list_addmasasubur_ternakheat.size();i++){
            if(id.equalsIgnoreCase(list_addmasasubur_ternakheat.get(i))){
                cek = true;
            }
        }
        return cek;
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

    private boolean checkForm() {
        boolean cek = true;

        if(input_addmasasubur_activity_idternak.getText().toString().matches(""))
        {input_addmasasubur_activity_idternak.setError("ID Ternak belum diisi");cek = false;}
        if(input_addmasasubur_activity_tglpemeriksaan.getText().toString().matches("01 Januari 1970"))
        {input_addmasasubur_activity_tglpemeriksaan.setError("Tanggal belum diisi");cek = false;}

        return cek;
    }

    public void cleartext(){
        input_addmasasubur_activity_idternak.setText("");
        input_addmasasubur_activity_tglpemeriksaan.setText("01 Januari 1970");
        radiogroup_addmasasubur_activity_mlaiselesai.clearCheck();
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

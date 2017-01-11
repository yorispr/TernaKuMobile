package com.fintech.ternaku.TernakPerah.Main.TambahData.Kesuburan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.NumberPicker;
import android.widget.Spinner;
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

public class AddMelahirkan extends AppCompatActivity {
    private AutoCompleteTextView input_addmelahirkan_activity_idternak;
    private Spinner spinner_addmelahirkan_activity_statuskeberhasilan;
    private TextView input_addmelahirkan_activity_tglmelahirkan,txt_addmelahirkan_activity_kondisi,txt_addmelahirkan_activity_tglmelahirkan;
    private EditText input_addmelahirkan_activity_kondisi,input_addmelahirkan_activity_jumlahanak;
    private Button button_addmelahirkan_activity_simpan;
    private DatePickerDialog DatePickerDialog_tglmelahirkan;
    private SimpleDateFormat dateFormatter_tglmelahirkan;
    private LinearLayout linearLayout_addmelahirkan_activity_jmlanak;
    private TimePickerDialog mTimePicker;
    String datetime;

    private int choosenindex=-1;
    private boolean isAborsi=false;
    ArrayList<String> list_addmelahirkan_idternak = new ArrayList<String>();
    ArrayList<String> list_addmelahirkan_tglinseminasi = new ArrayList<String>();
    ArrayAdapter<String> myAdapter;


    private Bluetooth bt;
    public final String TAG = "AddInseminasi";


    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_melahirkan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Melahirkan");

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_primary_kesuburan)));
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //Set Auto Text Id Ternak---------------------------------------------
        String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetTernakSedangHamil().execute(url.getUrl_GetHamil(), urlParameters);
        input_addmelahirkan_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addmelahirkan_activity_idternak);
        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_addmelahirkan_idternak);
        input_addmelahirkan_activity_idternak.setAdapter(adp);
        input_addmelahirkan_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosenindex = i;
            }
        });
        input_addmelahirkan_activity_idternak.setEnabled(false);

        //Set Spinner Status Keberhasilan--------------------------------------
        spinner_addmelahirkan_activity_statuskeberhasilan = (Spinner)findViewById(R.id.spinner_addmelahirkan_activity_statuskeberhasilan);
        final String[] spinData= {"Pilih Status Keberhasilan","Berhasil","Gagal"};
        myAdapter= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,spinData);
        spinner_addmelahirkan_activity_statuskeberhasilan.setAdapter(myAdapter);
        txt_addmelahirkan_activity_kondisi=(TextView)findViewById(R.id.txt_addmelahirkan_activity_kondisi);
        txt_addmelahirkan_activity_tglmelahirkan= (TextView)findViewById(R.id.txt_addmelahirkan_activity_tglmelahirkan);
        linearLayout_addmelahirkan_activity_jmlanak =(LinearLayout)findViewById(R.id.linearLayout_addmelahirkan_activity_jmlanak);
        spinner_addmelahirkan_activity_statuskeberhasilan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==1){
                    txt_addmelahirkan_activity_kondisi.setText("Kondisi Melahirkan");
                    txt_addmelahirkan_activity_tglmelahirkan.setText("Tanggal Melahirkan");
                    linearLayout_addmelahirkan_activity_jmlanak.setVisibility(View.VISIBLE);
                    isAborsi = false;
                }
                else if(i==2){
                    isAborsi = true;
                    linearLayout_addmelahirkan_activity_jmlanak.setVisibility(View.GONE);
                    txt_addmelahirkan_activity_kondisi.setText("Penyebab Aborsi");
                    txt_addmelahirkan_activity_tglmelahirkan.setText("Tanggal Aborsi");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setDateTimeField();
        setTime();

        //Set Tanggal Melahirkan------------------------------------------------
        input_addmelahirkan_activity_tglmelahirkan = (TextView)findViewById(R.id.input_addmelahirkan_activity_tglmelahirkan);
        dateFormatter_tglmelahirkan = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addmelahirkan_activity_tglmelahirkan.setText(dateFormatter_tglmelahirkan.format(Calendar.getInstance().getTime()));
        input_addmelahirkan_activity_tglmelahirkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddMelahirkan.this);
                DatePickerDialog_tglmelahirkan.show();
            }
        });

        //Set Keterangan dan Jumlah Anak----------------------------------
        txt_addmelahirkan_activity_kondisi.setText("");
        input_addmelahirkan_activity_kondisi = (EditText)findViewById(R.id.input_addmelahirkan_activity_kondisi);
        input_addmelahirkan_activity_jumlahanak = (EditText)findViewById(R.id.input_addmelahirkan_activity_jumlahanak);
        input_addmelahirkan_activity_jumlahanak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        //Simpan To Database-----------------------------------------------
        button_addmelahirkan_activity_simpan = (Button)findViewById(R.id.button_addmelahirkan_activity_simpan);
        button_addmelahirkan_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForm()){
                    if(spinner_addmelahirkan_activity_statuskeberhasilan.getSelectedItemId()==0){
                        new SweetAlertDialog(AddMelahirkan.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Peringatan!")
                                .setContentText("Silahkan Pilih Status Keberhasilan")
                                .show();
                    }else{
                        new SweetAlertDialog(AddMelahirkan.this, SweetAlertDialog.WARNING_TYPE)
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
                                        urlParameters2 = "id=" + input_addmelahirkan_activity_idternak.getText().toString().trim() +
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
                    new SweetAlertDialog(AddMelahirkan.this, SweetAlertDialog.ERROR_TYPE)
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
                    input_addmelahirkan_activity_idternak.setText(msg.obj.toString().trim());
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


    //Get Data Ternak Sedang hamil autocomplete-------------------------------
    private class GetTernakSedangHamil extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddMelahirkan.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddMelahirkan.this, SweetAlertDialog.ERROR_TYPE)
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
                new SweetAlertDialog(AddMelahirkan.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Simpan")
                        .setContentText("Tidak Ada Ternak Yang Sedang Hamil" +
                                "\nApakah Ingin Memasukkan Data Ternak Hamil?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                finish();
                                startActivity(new Intent(AddMelahirkan.this,AddMengandung.class));
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
            } else {
                AddTernakToList(result);
                input_addmelahirkan_activity_idternak.setEnabled(true);
            }
        }
    }
    private void AddTernakToList(String result) {
        list_addmelahirkan_idternak.clear();
        list_addmelahirkan_tglinseminasi.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addmelahirkan_tglinseminasi.add(jObj.getString("tgl_inseminasi"));
                list_addmelahirkan_idternak.add(jObj.getString("id_ternak"));
            }
            myAdapter.notifyDataSetChanged();
        }
        catch (JSONException e){e.printStackTrace();}
    }
    private String getTglInseminasi(String idternak) {
        String tgl="";
        for (int i=0;i<list_addmelahirkan_idternak.size();i++)
        {
            if(list_addmelahirkan_idternak.get(i).equalsIgnoreCase(idternak)){
                tgl = list_addmelahirkan_tglinseminasi.get(i);
                break;
            }
        }
        return tgl;
    }

    private class CheckRFID extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(AddMelahirkan.this, SweetAlertDialog.PROGRESS_TYPE);

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
                String idternak = input_addmelahirkan_activity_idternak.getText().toString().trim();
                String tglinseminasi = getTglInseminasi(idternak);

                if(isAborsi) {
                    urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                            + "&tglinseminasi="+tglinseminasi
                            + "&penyebababorsi="+input_addmelahirkan_activity_kondisi.getText().toString()
                            + "&tglaborsi="+input_addmelahirkan_activity_tglmelahirkan.getText().toString();
                }else{
                    urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                            + "&tglinseminasi="+tglinseminasi
                            + "&jumlahanak="+input_addmelahirkan_activity_jumlahanak.getText().toString()
                            + "&kondisimelahirkan="+input_addmelahirkan_activity_kondisi.getText().toString()
                            + "&tglmelahirkanreal="+input_addmelahirkan_activity_tglmelahirkan.getText().toString();
                }
                new InsertTernakMelahirkan().execute(url.getUrl_InsertMelahirkan(), urlParameters);
            }else{
                new SweetAlertDialog(AddMelahirkan.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("RFID Sudah Terpakai atau Tidak Ada RFID Ditemukan")
                        .show();
            }
        }
    }

    //Insert in to Database----------------------------------------
    private class InsertTernakMelahirkan extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddMelahirkan.this, SweetAlertDialog.PROGRESS_TYPE);

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
                new SweetAlertDialog(AddMelahirkan.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                                final int _id = (int) System.currentTimeMillis();
                                Calendar cal = Calendar.getInstance();
                                Date date = cal.getTime();
                                String formatteddate = new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(date);
                                cal.add(Calendar.MONTH,9);
                                Log.d("calendar3",formatteddate);

                                Alarm al = new Alarm(0,String.valueOf(_id),"ins_melahirkan",String.valueOf(new Date()),formatteddate,input_addmelahirkan_activity_idternak.getText().toString().trim());
                                db.addAlarm(al);
                                AlarmScheduler as = new AlarmScheduler();
                                as.setAlarm(al,getApplicationContext());
                                Log.d("id_sapi2",al.getId_sapi());

                                db.TurnOffAlarmByIdSapi(al.getId_sapi());

                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(AddMelahirkan.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Ternak Melahirkan")
                                        .setContentText("Apakah Ingin Menambah Data Ternak Melahirkan Lagi?")
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

    public void show() {

        final Dialog d = new Dialog(AddMelahirkan.this);
        d.setTitle("Jumlah Anak");
        d.setContentView(R.layout.number_picker_dialog);
        Button b1 = (Button) d.findViewById(R.id.btnPilih);
        Button b2 = (Button) d.findViewById(R.id.btnBatal);

        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);


        np.setMaxValue(100); // max value 100
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                input_addmelahirkan_activity_jumlahanak.setText(String.valueOf(np.getValue())); //set the value to textview
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog_tglmelahirkan = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addmelahirkan_activity_tglmelahirkan.setText(dateFormatter_tglmelahirkan.format(newDate.getTime()));
                datetime = dateFormatter_tglmelahirkan.format(newDate.getTime());
                mTimePicker.show();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(AddMelahirkan.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(timePicker.isShown()) {
                    //txtJam.setText( selectedHour + ":" + selectedMinute+":00");
                    datetime += " " + selectedHour + ":" + selectedMinute + ":00";
                    input_addmelahirkan_activity_tglmelahirkan.setText(datetime);
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
        boolean cek = true;

        if(input_addmelahirkan_activity_idternak.getText().toString().matches(""))
        {
            input_addmelahirkan_activity_idternak.setError("ID Ternak belum diisi");
            cek = false;
        }
        if(spinner_addmelahirkan_activity_statuskeberhasilan.getSelectedItemId()==1){
            if(input_addmelahirkan_activity_jumlahanak.getText().toString().matches(""))
            {
                input_addmelahirkan_activity_jumlahanak.setError("Biaya belum diisi");
                cek = false;
            }
        }
        if(input_addmelahirkan_activity_tglmelahirkan.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            cek=false;
            input_addmelahirkan_activity_tglmelahirkan.setError("Tanggal belum diisi");
        }
        if(input_addmelahirkan_activity_kondisi.getText().toString().equalsIgnoreCase("")){
            cek=false;
            input_addmelahirkan_activity_kondisi.setError("Data belum diisi");
        }

        return cek;
    }

    public void cleartext(){
        input_addmelahirkan_activity_idternak.setText("");
        input_addmelahirkan_activity_jumlahanak.setText("");
        input_addmelahirkan_activity_kondisi.setText("");
        input_addmelahirkan_activity_tglmelahirkan.setText("01 Januari 1970");
        spinner_addmelahirkan_activity_statuskeberhasilan.setSelection(0);
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

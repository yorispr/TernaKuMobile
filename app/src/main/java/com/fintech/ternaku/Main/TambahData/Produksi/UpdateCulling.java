package com.fintech.ternaku.Main.TambahData.Produksi;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UpdateCulling extends AppCompatActivity {
    private AutoCompleteTextView input_updateculling_activity_idternak;
    private TextView input_updateculling_activity_tanggal;
    private EditText input_updateculling_activity_alasan;
    private TimePickerDialog mTimePicker;
    private DatePickerDialog DatePickerDialog_tglculling;
    private SimpleDateFormat dateFormatter_tglculling;
    private Button button_updateculling_activity_simpan;
    String datetime;

    private int choosenindex =-1;
    ArrayList<String> list_updateculling_idternak = new ArrayList<String >();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_culling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Culling");
        }

        //Set Id Ternak Auto Complete-------------------------------------------
        input_updateculling_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_updateculling_activity_idternak);

        //Set Date Culling------------------------------------------------------
        setDateTimeField();
        setTime();
        input_updateculling_activity_tanggal = (TextView)findViewById(R.id.input_updateculling_activity_tanggal);
        dateFormatter_tglculling = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_updateculling_activity_tanggal.setText(dateFormatter_tglculling.format(Calendar.getInstance().getTime()));
        input_updateculling_activity_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(UpdateCulling.this);
                DatePickerDialog_tglculling.show();
            }
        });

        //Insert Into Database------------------------------------------------
        button_updateculling_activity_simpan = (Button)findViewById(R.id.button_updateculling_activity_simpan);
        input_updateculling_activity_alasan = (EditText)findViewById(R.id.input_updateculling_activity_alasan);
        button_updateculling_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cekForm()) {
                    String idter = input_updateculling_activity_idternak.getText().toString();
                    String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                            + "&idternak=" + idter
                            + "&tglculling=" + input_updateculling_activity_tanggal.getText().toString()
                            + "&alasan=" + input_updateculling_activity_alasan.getText().toString();

                    new AddCulling().execute("http://ternaku.com/index.php/C_HistoryKesehatan/UpdateCulling", param);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Data belum lengkap!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //Insert In To Database---------------------------------------------------
    private class AddCulling extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(UpdateCulling.this);
            progDialog.setMessage("Tunggu Sebentar...");
            progDialog.show();
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
            progDialog.dismiss();
            if (result.trim().equals("1")){
                Toast.makeText(getApplication(),"Berhasil Menambah Data",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog_tglculling = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_updateculling_activity_tanggal.setText(dateFormatter_tglculling.format(newDate.getTime()));
                datetime = dateFormatter_tglculling.format(newDate.getTime());
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
                datetime+= " "+selectedHour + ":" + selectedMinute+":00";
                input_updateculling_activity_tanggal.setText(datetime);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
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


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public boolean cekForm(){
        boolean cek = true;
        if(input_updateculling_activity_idternak.getText().toString().matches("")){
            cek = false;
            input_updateculling_activity_idternak.setError("Data belum diisi");
        }
        if(input_updateculling_activity_tanggal.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            cek=false;
            input_updateculling_activity_tanggal.setError("Data belum diisi");
        }
        return cek;
    }

}

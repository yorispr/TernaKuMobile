package com.fintech.ternaku.Main.TambahData.Kesehatan;

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
import com.fintech.ternaku.Main.TambahData.Kesehatan.ModelAddVaksin;
import com.fintech.ternaku.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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

    ArrayList<String> list_addvaksinasi_idvaksin = new ArrayList<String>();
    ArrayList<ModelAddVaksin> list_addvaksinasi_namavaksin = new ArrayList<ModelAddVaksin>();
    ArrayAdapter<String> myAdapter;
    private boolean cekKarantina;
    private int selectedindex=-1;

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
        }

        //Set Text Input Id----------------------------------------------------------------------------
        input_addvaksinasi_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addvaksinasi_activity_idternak);

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
        new GetVaksin().execute("http://ternaku.com/index.php/C_Vaksinasi/getVaksin","");
        button_addvaksinasi_activity_simpan = (Button)findViewById(R.id.button_addvaksinasi_activity_simpan);
        button_addvaksinasi_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cekForm()) {
                    String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                            + "&idternak=" + input_addvaksinasi_activity_idternak.getText().toString()
                            + "&idvaksin=" + list_addvaksinasi_namavaksin.get(selectedindex).getId()
                            + "&dosis=" + input_addvaksinasi_activity_dosis.getText().toString()
                            + "&satuandosis=" + input_addvaksinasi_activity_satuan.getText().toString()
                            + "&repetisi=" + input_addvaksinasi_activity_pemberianke.getText().toString()
                            + "&tglvaksinasi=" + input_addvaksinasi_activity_tglvaksinasi.getText().toString();
                    new AddVaksinasitoDatabase().execute("http://ternaku.com/index.php/C_Vaksinasi/insertVaksinasi", param);
                    Log.d("Param",param);
                }

                else {
                    Toast.makeText(getApplicationContext(), "Data belum lengkap!", Toast.LENGTH_LONG).show();
                }
            }

        });


    }

    //Get Data Vaksin--------------------------------------------------------------------
    private class GetVaksin extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(AddVaksinasi.this);
            progDialog.setMessage("Tunggu Sebentar...");
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
            AddVaksinToList(result);
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

    //Insert To Database--------------------------------------------------
    private class AddVaksinasitoDatabase extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(AddVaksinasi.this);
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
                datetime+= " "+selectedHour + ":" + selectedMinute+":00";
                input_addvaksinasi_activity_tglvaksinasi.setText(datetime);
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

    public boolean cekForm(){
        boolean cek = true;
        if(input_addvaksinasi_activity_idternak.getText().toString().matches("")){
            cek = false;
            input_addvaksinasi_activity_idternak.setError("Data belum diisi");
        }
        if(input_addvaksinasi_activity_tglvaksinasi.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            cek=false;
            input_addvaksinasi_activity_tglvaksinasi.setError("Data belum diisi");
        }
        return cek;
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

package com.fintech.ternaku.Main.TambahData.Kesuburan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddPemeriksaanReproduksi extends AppCompatActivity {
    private TextView input_addpemeriksaansubur_activity_tglpemeriksaan,input_addpemeriksaansubur_activity_tglperiksaberikut;
    private AutoCompleteTextView input_addpemeriksaansubur_activity_idternak;
    private Spinner spinner_addpemeriksaansubur_activity_statuskesehatan;
    private EditText input_addpemeriksaansubur_activity_diagnosis,input_addpemeriksaansubur_activity_perawatan,
            input_addpemeriksaansubur_activity_biaya;
    private Button button_addpemeriksaansubur_activity_simpan;
    private LinearLayout linearLayout_addpemeriksaansubur_activity_diagnosis;
    private DatePickerDialog DatePickerDialog_tglpemeriksaan,DatePickerDialog_tglperiksaberikut;
    private SimpleDateFormat dateFormatter_tglpemeriksaan,dateFormatter_tglperiksaberikut;
    int choosenindex=-1;

    int isSehat = 1;
    ArrayList<String> list_addipemeriksaansubur_idternak = new ArrayList<String>();
    ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pemeriksaan_reproduksi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Periksa Reproduksi");
        }

        //Set Id Ternak Auto Complete-------------------------------------
        String urlParameters_idternak = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null)
                + "&idpeternakan=" + "FNT-P1";
        new GetTernakHeat().execute("http://ternaku.com/index.php/C_Ternak/getDetailTernakByUserId", urlParameters_idternak);
        input_addpemeriksaansubur_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addpemeriksaansubur_activity_idternak);
        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_addipemeriksaansubur_idternak);
        input_addpemeriksaansubur_activity_idternak.setAdapter(adp);
        input_addpemeriksaansubur_activity_idternak.setEnabled(false);

        setDateTimeField();

        //Set Tanggal Pemeriksaan-----------------------------------------
        input_addpemeriksaansubur_activity_tglpemeriksaan = (TextView)findViewById(R.id.input_addpemeriksaansubur_activity_tglpemeriksaan);
        dateFormatter_tglpemeriksaan = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addpemeriksaansubur_activity_tglpemeriksaan.setText(dateFormatter_tglpemeriksaan.format(Calendar.getInstance().getTime()));
        input_addpemeriksaansubur_activity_tglpemeriksaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddPemeriksaanReproduksi.this);
                DatePickerDialog_tglpemeriksaan.show();
            }
        });

        //Set Spinner Status Kesehatan-------------------------------------
        spinner_addpemeriksaansubur_activity_statuskesehatan = (Spinner)findViewById(R.id.spinner_addpemeriksaansubur_activity_statuskesehatan);
        String [] pilihan= {"Pilih Status Kesehatan","Sehat","Tidak Sehat"};
        myAdapter= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,pilihan);
        spinner_addpemeriksaansubur_activity_statuskesehatan.setAdapter(myAdapter);
        spinner_addpemeriksaansubur_activity_statuskesehatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    linearLayout_addpemeriksaansubur_activity_diagnosis.setVisibility(View.GONE);
                    isSehat = 1;
                }
                else if(i==1){
                    linearLayout_addpemeriksaansubur_activity_diagnosis.setVisibility(View.VISIBLE);
                    isSehat = 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Set Linear Diagnosis---------------------------------------------
        linearLayout_addpemeriksaansubur_activity_diagnosis = (LinearLayout)findViewById(R.id.linearLayout_addpemeriksaansubur_activity_diagnosis);
        input_addpemeriksaansubur_activity_diagnosis = (EditText)findViewById(R.id.input_addpemeriksaansubur_activity_diagnosis);
        input_addpemeriksaansubur_activity_perawatan = (EditText)findViewById(R.id.input_addpemeriksaansubur_activity_perawatan);

        //Set Tanggal Periksa Berikutnya-----------------------------------
        input_addpemeriksaansubur_activity_tglperiksaberikut = (TextView) findViewById(R.id.input_addpemeriksaansubur_activity_tglperiksaberikut);
        dateFormatter_tglperiksaberikut = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addpemeriksaansubur_activity_tglperiksaberikut.setText(dateFormatter_tglperiksaberikut.format(Calendar.getInstance().getTime()));
        input_addpemeriksaansubur_activity_tglperiksaberikut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddPemeriksaanReproduksi.this);
                DatePickerDialog_tglperiksaberikut.show();
            }
        });

        //Set Biaya--------------------------------------------------------
        input_addpemeriksaansubur_activity_biaya = (EditText)findViewById(R.id.input_addpemeriksaansubur_activity_biaya);
        button_addpemeriksaansubur_activity_simpan = (Button)findViewById(R.id.button_addpemeriksaansubur_activity_simpan);
        button_addpemeriksaansubur_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForm()) {
                    String perawatan = "N/A";
                    String diagnosis = "N/A";

                    if (isSehat == 0) {
                        diagnosis = input_addpemeriksaansubur_activity_diagnosis.getText().toString();
                        perawatan = input_addpemeriksaansubur_activity_perawatan.getText().toString();
                    }
                    String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                            + "&idternak=" + input_addpemeriksaansubur_activity_idternak.getText().toString().trim()
                            + "&tglperiksa=" + input_addpemeriksaansubur_activity_tglpemeriksaan.getText().toString()
                            + "&perawatan=" + perawatan
                            + "&diagnosis=" + diagnosis
                            + "&tglperiksaberikutnya=" + input_addpemeriksaansubur_activity_tglperiksaberikut.getText().toString()
                            + "&statusreproduksi=" + String.valueOf(isSehat)
                            + "&biayaperiksa=" + input_addpemeriksaansubur_activity_biaya.getText().toString();
                    new InsertPeriksaReproduksi().execute("http://ternaku.com/index.php/C_HistoryKesehatan/InsertKesehatanReproduksi", urlParameters);
                    Log.d("Param",urlParameters);
                }
            }
        });


    }

    //Set Input AutoComplete Id Ternak-----------------------------------------------
    private class GetTernakHeat extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {

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
            if (result.trim().equals("404")){
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
            }
            else {
                AddTernakToList(result);
                input_addpemeriksaansubur_activity_idternak.setEnabled(true);
            }
        }
    }
    private void AddTernakToList(String result)
    {
        list_addipemeriksaansubur_idternak.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addipemeriksaansubur_idternak.add(jObj.getString("id_ternak"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Insert To Database----------------------------------------
    private class InsertPeriksaReproduksi extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(AddPemeriksaanReproduksi.this);
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
        DatePickerDialog_tglpemeriksaan = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addpemeriksaansubur_activity_tglpemeriksaan.setText(dateFormatter_tglpemeriksaan.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog_tglperiksaberikut = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addpemeriksaansubur_activity_tglperiksaberikut.setText(dateFormatter_tglperiksaberikut.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
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
        boolean cek = true;

        if(input_addpemeriksaansubur_activity_idternak.getText().toString().matches(""))
        {input_addpemeriksaansubur_activity_idternak.setError("ID Ternak belum diisi");cek = false;}
        else if(input_addpemeriksaansubur_activity_biaya.getText().toString().matches(""))
        {input_addpemeriksaansubur_activity_biaya.setError("Biaya belum diisi");cek = false;}
        else if(spinner_addpemeriksaansubur_activity_statuskesehatan.getSelectedItem().toString().equalsIgnoreCase("Pilih Status Kesehatan"))
        {Toast.makeText(getApplicationContext(),"Pilih Status Kesehatan",Toast.LENGTH_LONG).show();cek=false;}

        return cek;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}

package com.fintech.ternaku.Main.TambahData.Kesuburan;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class AddInseminasi extends AppCompatActivity {
    private TextView input_addinseminasi_activity_tglinseminasi;
    private AutoCompleteTextView input_addinseminasi_activity_idternak;
    private EditText input_addinseminasi_activity_biaya,input_addinseminasi_activity_jumlah,input_addinseminasi_activity_metode;
    private Spinner spinner_addinseminasi_activity_semen;
    private DatePickerDialog DatePickerDialog_tglinseminasi;
    private SimpleDateFormat dateFormatter_tglinseminasi;
    private Button button_addinseminasi_activity_simpan;
    int choosenindex=-1;

    ArrayList<String> list_addinseminasi_semen = new ArrayList<String>();
    ArrayList<String> list_addinseminasi_idternak = new ArrayList<String>();
    ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inseminasi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Data Inseminasi");
        }

        //Set Tanggal Inseminasi--------------------------------------
        input_addinseminasi_activity_tglinseminasi = (TextView)findViewById(R.id.input_addinseminasi_activity_tglinseminasi);
        dateFormatter_tglinseminasi = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addinseminasi_activity_tglinseminasi.setText(dateFormatter_tglinseminasi.format(Calendar.getInstance().getTime()));
        setDateTimeField();
        input_addinseminasi_activity_tglinseminasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog_tglinseminasi.show();
            }
        });

        //Set Data Id Ternak-----------------------------------------------
        String urlParameters_idternak = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetTernakHeat().execute("http://ternaku.com/index.php/C_HistoryKesehatan/getDataTernakHeatByPeternakan", urlParameters_idternak);
        input_addinseminasi_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addinseminasi_activity_idternak);
        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_addinseminasi_idternak);
        input_addinseminasi_activity_idternak.setAdapter(adp);
        input_addinseminasi_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosenindex = i;
            }
        });
        input_addinseminasi_activity_idternak.setEnabled(false);

        //Set Spinner Daftar Semen------------------------------------
        String urlParameters_semen = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetSemen().execute("http://ternaku.com/index.php/C_HistoryInseminasi/GetSemen", urlParameters_semen);
        spinner_addinseminasi_activity_semen = (Spinner)findViewById(R.id.spinner_addinseminasi_activity_semen);
        myAdapter= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,list_addinseminasi_semen);
        spinner_addinseminasi_activity_semen.setAdapter(myAdapter);

        //Set Harga dan Jumlah----------------------------------------
        input_addinseminasi_activity_biaya = (EditText)findViewById(R.id.input_addinseminasi_activity_biaya);
        input_addinseminasi_activity_jumlah = (EditText)findViewById(R.id.input_addinseminasi_activity_jumlah);
        input_addinseminasi_activity_metode = (EditText)findViewById(R.id.input_addinseminasi_activity_metode);


        //Simpan Data------------------------------------------------
        button_addinseminasi_activity_simpan = (Button)findViewById(R.id.button_addinseminasi_activity_simpan);
        button_addinseminasi_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkForm()) {
                    String urlParameters;
                    String spinstr = spinner_addinseminasi_activity_semen.getSelectedItem().toString();
                    String idsemen = spinstr.substring(spinstr.indexOf("(") + 1, spinstr.indexOf(")"));
                    ;
                    urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                            + "&idternak=" + input_addinseminasi_activity_idternak.getText().toString().trim()
                            + "&tglinseminasi=" + input_addinseminasi_activity_tglinseminasi.getText().toString()
                            + "&metodeinseminasi=" + input_addinseminasi_activity_metode.getText().toString()
                            + "&idsemen=" + idsemen.trim()
                            + "&jumlahsemen=" + input_addinseminasi_activity_jumlah.getText().toString()
                            + "&biayasemen=" + input_addinseminasi_activity_biaya.getText().toString();
                    new InsertInseminasi().execute("http://ternaku.com/index.php/C_HistoryInseminasi/insertInseminasi", urlParameters);
                    Log.d("Param", urlParameters.toString());
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
                input_addinseminasi_activity_idternak.setEnabled(true);
            }
        }
    }
    private void AddTernakToList(String result)
    {
        list_addinseminasi_idternak.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addinseminasi_idternak.add(jObj.getString("id_ternak"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Set Daftar Semen----------------------------------------------
    private class GetSemen extends AsyncTask<String,Integer,String> {
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
                AddSemenToList(result);
                //autocomplete.setEnabled(true);
            }
        }
    }
    private void AddSemenToList(String result)
    {
        list_addinseminasi_semen.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addinseminasi_semen.add("("+jObj.getString("ID_SEMEN")+") "+jObj.getString("GENOM")+", "+jObj.getString("JENIS_SEMEN"));
            }
            myAdapter.notifyDataSetChanged();
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog_tglinseminasi = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addinseminasi_activity_tglinseminasi.setText(dateFormatter_tglinseminasi.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    //Insert Inseminasi-----------------------------------------
    private class InsertInseminasi extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(AddInseminasi.this);
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

    private boolean checkForm()
    {
        boolean cek = true;

        /*if(autocomplete.getText().toString().matches(""))
        {autocomplete.setError("ID Ternak belum diisi");cek = false;}
        else */if(input_addinseminasi_activity_biaya.getText().toString().matches(""))
        {input_addinseminasi_activity_biaya.setError("Biaya belum diisi");cek = false;}
        else if(input_addinseminasi_activity_jumlah.getText().toString().matches(""))
        {input_addinseminasi_activity_jumlah.setError("Jumlah belum diisi");cek = false;}
        else if(input_addinseminasi_activity_metode.getText().toString().matches(""))
        {input_addinseminasi_activity_metode.setError("Metode belum diisi");cek = false;}

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

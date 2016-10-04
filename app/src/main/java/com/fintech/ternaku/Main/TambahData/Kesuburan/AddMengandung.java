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

public class AddMengandung extends AppCompatActivity {
    private TextView input_addmengandung_activity_tglpemeriksaan,input_addmengandung_activity_tglperkiraanhamil;
    private AutoCompleteTextView input_addmengandung_activity_idternak;
    private Spinner spinner_addmengandung_activity_statuskeberhasilan;
    private Button button_addmengandung_activity_simpan;
    ArrayList<String> list_addmengandung_idternak = new ArrayList<String>();
    ArrayList<String> list_addmengandung_tglinseminasi = new ArrayList<String>();
    private DatePickerDialog DatePickerDialog_tglpemeriksaan,DatePickerDialog_tglperkiraanhamil;
    private int choosenindex=-1;
    private SimpleDateFormat dateFormatter_tglpemeriksaan,dateFormatter_tglperkiraanhamil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mengandung);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Mengandung");
        }

        //Set Data Id Ternak-----------------------------------------------
        String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetTernakSudahInseminasi().execute("http://ternaku.com/index.php/C_HistoryInseminasi/GetTernakSudahInseminasi", urlParameters);
        input_addmengandung_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addmengandung_activity_idternak);
        input_addmengandung_activity_idternak.setEnabled(false);
        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_addmengandung_idternak);

        input_addmengandung_activity_idternak.setAdapter(adp);

        input_addmengandung_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosenindex = i;
            }
        });
        input_addmengandung_activity_idternak.setEnabled(false);

        setDateTimeField();

        //Set Tgl Pemeriksaan-------------------------------------------------
        input_addmengandung_activity_tglpemeriksaan = (TextView) findViewById(R.id.input_addmengandung_activity_tglpemeriksaan);
        dateFormatter_tglpemeriksaan = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addmengandung_activity_tglpemeriksaan.setText(dateFormatter_tglpemeriksaan.format(Calendar.getInstance().getTime()));
        input_addmengandung_activity_tglpemeriksaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog_tglperkiraanhamil.show();
            }
        });

        //Set Spinner Status-------------------------------------------------
        spinner_addmengandung_activity_statuskeberhasilan = (Spinner)findViewById(R.id.spinner_addmengandung_activity_statuskeberhasilan);
        final String[] spinData= {"Berhasil","Gagal","Belum Diketahui"};
        ArrayAdapter<String> myAdapter= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,spinData);
        spinner_addmengandung_activity_statuskeberhasilan.setAdapter(myAdapter);

        //Set Tgl Perkiraan-------------------------------------------------
        input_addmengandung_activity_tglperkiraanhamil = (TextView) findViewById(R.id.input_addmengandung_activity_tglperkiraanhamil);
        dateFormatter_tglperkiraanhamil = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addmengandung_activity_tglperkiraanhamil.setText(dateFormatter_tglperkiraanhamil.format(Calendar.getInstance().getTime()));
        input_addmengandung_activity_tglperkiraanhamil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog_tglpemeriksaan.show();
            }
        });

        //Simpan Data------------------------------------------------------
        button_addmengandung_activity_simpan = (Button)findViewById(R.id.button_addmengandung_activity_simpan);
        button_addmengandung_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dateFormatter = new SimpleDateFormat("YYYY", Locale.US);

                String idternak = input_addmengandung_activity_idternak.getText().toString().trim();
                String tglinseminasi = getTglInseminasi(idternak);

                String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null)
                        +"&tglcekkeberhasilan="+input_addmengandung_activity_tglpemeriksaan.getText().toString().trim()
                        +"&statuskeberhasilan="+spinner_addmengandung_activity_statuskeberhasilan.getSelectedItem().toString()
                        +"&tglperkiraanmelahirkan="+input_addmengandung_activity_tglperkiraanhamil.getText().toString()
                        +"&idternak="+idternak
                        +"&tglinseminasi="+tglinseminasi;
                Log.d("param",urlParameters);
                new InsertStatusKehamilan().execute("http://ternaku.com/index.php/C_HistoryInseminasi/UpdateStatusKehamilan", urlParameters);
            }
        });


    }

    //Set Input AutoComplete Id Ternak-----------------------------------------------
    private class GetTernakSudahInseminasi extends AsyncTask<String,Integer,String> {
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
                input_addmengandung_activity_idternak.setEnabled(true);
            }
        }
    }

    //Input to Database------------------------------------------
    private class InsertStatusKehamilan extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(AddMengandung.this);
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
            else if (result.trim().equals("0")){
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void AddTernakToList(String result)
    {
        list_addmengandung_idternak.clear();
        list_addmengandung_tglinseminasi.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addmengandung_tglinseminasi.add(jObj.getString("tgl_inseminasi"));
                list_addmengandung_idternak.add(jObj.getString("id_ternak"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog_tglpemeriksaan = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addmengandung_activity_tglpemeriksaan.setText(dateFormatter_tglpemeriksaan.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog_tglperkiraanhamil = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addmengandung_activity_tglperkiraanhamil.setText(dateFormatter_tglperkiraanhamil.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private String getTglInseminasi(String idternak)
    {
        String tgl="";
        for (int i=0;i<list_addmengandung_idternak.size();i++)
        {
            if(list_addmengandung_idternak.get(i).equalsIgnoreCase(idternak)){
                tgl = list_addmengandung_tglinseminasi.get(i);
                break;
            }
        }
        return tgl;
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

package com.fintech.ternaku.Main.TambahData.Kesuburan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.NumberPicker;
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

public class AddMelahirkan extends AppCompatActivity {
    private AutoCompleteTextView input_addmelahirkan_activity_idternak;
    private Spinner spinner_addmelahirkan_activity_statuskeberhasilan;
    private TextView input_addmelahirkan_activity_tglmelahirkan,txt_addmelahirkan_activity_kondisi,txt_addmelahirkan_activity_tglmelahirkan;
    private EditText input_addmelahirkan_activity_kondisi,input_addmelahirkan_activity_jumlahanak;
    private Button button_addmelahirkan_activity_simpan;
    private DatePickerDialog DatePickerDialog_tglmelahirkan;
    private SimpleDateFormat dateFormatter_tglmelahirkan;
    private LinearLayout linearLayout_addmelahirkan_activity_jmlanak;

    private int choosenindex=-1;
    private boolean isAborsi=false;
    ArrayList<String> list_addmelahirkan_idternak = new ArrayList<String>();
    ArrayList<String> list_addmelahirkan_tglinseminasi = new ArrayList<String>();
    ArrayAdapter<String> myAdapter;

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
        }

        //Set Auto Text Id Ternak---------------------------------------------
        String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetTernakSedangHamil().execute("http://ternaku.com/index.php/C_HistoryInseminasi/GetTernakSudahHamil", urlParameters);
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
        final String[] spinData= {"Berhasil","Gagal"};
        myAdapter= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,spinData);
        spinner_addmelahirkan_activity_statuskeberhasilan.setAdapter(myAdapter);
        txt_addmelahirkan_activity_kondisi=(TextView)findViewById(R.id.txt_addmelahirkan_activity_kondisi);
        txt_addmelahirkan_activity_tglmelahirkan= (TextView)findViewById(R.id.txt_addmelahirkan_activity_tglmelahirkan);
        linearLayout_addmelahirkan_activity_jmlanak =(LinearLayout)findViewById(R.id.linearLayout_addmelahirkan_activity_jmlanak);
        spinner_addmelahirkan_activity_statuskeberhasilan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    txt_addmelahirkan_activity_kondisi.setText("Kondisi Melahirkan");
                    txt_addmelahirkan_activity_tglmelahirkan.setText("Tanggal Melahirkan");
                    linearLayout_addmelahirkan_activity_jmlanak.setVisibility(View.VISIBLE);
                    isAborsi = false;
                }
                else if(i==1){
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
                new InsertTernakMelahirkan().execute("http://ternaku.com/index.php/C_HistoryInseminasi/UpdateStatusKelahiran", urlParameters);

            }
        });

    }

    //Get Data Ternak Sedang hamil autocomplete-------------------------------
    private class GetTernakSedangHamil extends AsyncTask<String,Integer,String> {
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
    private String getTglInseminasi(String idternak)
    {
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

    //Insert in to Database----------------------------------------
    private class InsertTernakMelahirkan extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(AddMelahirkan.this);
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

    public void show() {

        final Dialog d = new Dialog(AddMelahirkan.this);
        d.setTitle("NumberPicker");
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
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

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
}

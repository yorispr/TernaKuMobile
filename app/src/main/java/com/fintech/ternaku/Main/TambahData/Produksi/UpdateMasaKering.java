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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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

    boolean isMulai;
    int susuaman;
    private ArrayList<String> list_update_dryoff = new ArrayList<>();
    ArrayAdapter<String> adp;

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
        }

        //Set Auto Text--------------------------------------------
        String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
        new GetTernakDry().execute("http://ternaku.com/index.php/C_HistoryKesehatan/getDataTernakDry", param);
        input_updatemasakering_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_updatemasakering_activity_idternak);
        input_updatemasakering_activity_idternak.setEnabled(false);
        adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_update_dryoff);
        input_updatemasakering_activity_idternak.setAdapter(adp);
        input_updatemasakering_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

        //Set Mulai Or Selesai--------------------------------------
        radiogroup_updatemasakering_activity_mlaiselesai = (RadioGroup)findViewById(R.id.radiogroup_updatemasakering_activity_mlaiselesai);
        radiogroup_updatemasakering_activity_mlaiselesai.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.radiobutton_updatemasakering_activity_mlai){
                    isMulai=true;
                }else if(i==R.id.radiobutton_updatemasakering_activity_selesai){
                    isMulai=false;
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
                    String idter = input_updatemasakering_activity_idternak.getText().toString();
                    if (!isDry(idter)) {
                        if(isMulai) {
                            String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                    + "&idternak=" + idter
                                    + "&tglmulaidry=" + input_updatemasakering_activity_tglpemeriksaan.getText().toString();
                            new AddDry().execute("http://ternaku.com/index.php/C_HistoryKesehatan/MasaKeringMulai", param);
                        }else{
                            String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                    + "&idternak=" + idter
                                    + "&tglselesaidry=" + input_updatemasakering_activity_tglpemeriksaan.getText().toString();
                            new AddDry().execute("http://ternaku.com/index.php/C_HistoryKesehatan/MasaKeringSelesai", param);
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Ternak sedang masa kering!",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Data belum lengkap!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //Get Data Ternak Dry-------------------------------------------
    private class GetTernakDry extends AsyncTask<String,Integer,String> {
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
            if(!result.trim().equalsIgnoreCase("404")) {
                AddTernakToList(result);
            }
            input_updatemasakering_activity_idternak.setEnabled(true);
        }
    }
    private void AddTernakToList(String result) {
        list_update_dryoff.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_update_dryoff.add(jObj.getString("id_ternak"));
            }
            adp.notifyDataSetChanged();
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Insert To Database----------------------------------------------------
    private class AddDry extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(UpdateMasaKering.this);
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

    private boolean isDry(String id){
        for(int i=0;i<list_update_dryoff.size();i++){
            if(list_update_dryoff.get(i).equals(id)){
                return true;
            }
        }
        return false;
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
                datetime+= " "+selectedHour + ":" + selectedMinute+":00";
                input_updatemasakering_activity_tglpemeriksaan.setText(datetime);
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
        if(input_updatemasakering_activity_idternak.getText().toString().matches("")){
            cek = false;
            input_updatemasakering_activity_idternak.setError("Data belum diisi");
        }
        if(input_updatemasakering_activity_tglpemeriksaan.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            cek=false;
            input_updatemasakering_activity_tglpemeriksaan.setError("Data belum diisi");
        }
        return cek;
    }
}

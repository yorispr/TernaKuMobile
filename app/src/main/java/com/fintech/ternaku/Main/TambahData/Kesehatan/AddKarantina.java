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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

public class AddKarantina extends AppCompatActivity {
    private AutoCompleteTextView input_addkarantina_activity_idternak;
    private RadioGroup radiogroup_addkarantina_activity_mlaiselesai;
    private RadioButton radiobutton_addkarantina_activity_mlai,radiobutton_addkarantina_activity_selesai;
    private TextView input_addkarantina_activity_tglpemeriksaan;
    private EditText input_addkarantina_activity_diagnosis,input_addkarantina_activity_perawatan;
    private Spinner spinner_addkarantina_activity_kawanan;
    private Button button_addkarantina_activity_simpan;
    private DatePickerDialog fromDatePickerDialog_tglperiksa;
    private TimePickerDialog mTimePicker;
    private SimpleDateFormat dateFormatter_tglperiksa;
    private String datetime;
    private LinearLayout linearLayout_addkarantina_activity_diagnosis;

    ArrayList<String> list_addkarantina_kawanan = new ArrayList<String>();
    ArrayAdapter<String> myAdapter;
    private boolean cekKarantina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_karantina);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Karantina");
        }

        //Set Auto Text-----------------------------------------
        input_addkarantina_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addkarantina_activity_idternak);

        //Set Radio Button--------------------------------------
        radiogroup_addkarantina_activity_mlaiselesai = (RadioGroup)findViewById(R.id.radiogroup_addkarantina_activity_mlaiselesai);
        linearLayout_addkarantina_activity_diagnosis = (LinearLayout)findViewById(R.id.linearLayout_addkarantina_activity_diagnosis);
        radiogroup_addkarantina_activity_mlaiselesai.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.radiobutton_addkarantina_activity_mlai){
                    linearLayout_addkarantina_activity_diagnosis.setVisibility(View.VISIBLE);
                    cekKarantina = true;
                }else if(i==R.id.radiobutton_addkarantina_activity_selesai){
                    linearLayout_addkarantina_activity_diagnosis.setVisibility(View.GONE);
                    cekKarantina = false;
                }
            }
        });

        //Set Tanggal Pemeriksaan---------------------------------------------------------
        setDateTimeField();
        setTime();
        input_addkarantina_activity_tglpemeriksaan = (TextView)findViewById(R.id.input_addkarantina_activity_tglpemeriksaan);
        dateFormatter_tglperiksa = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addkarantina_activity_tglpemeriksaan.setText(dateFormatter_tglperiksa.format(Calendar.getInstance().getTime()));
        input_addkarantina_activity_tglpemeriksaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddKarantina.this);
                fromDatePickerDialog_tglperiksa.show();
            }
        });

        //Set Kawanan----------------------------------------------------------
        String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
        new GetKawanan().execute("http://ternaku.com/index.php/C_HistoryKesehatan/getKawanan", param);
        myAdapter= new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item,list_addkarantina_kawanan);
        spinner_addkarantina_activity_kawanan = (Spinner)findViewById(R.id.spinner_addkarantina_activity_kawanan);
        spinner_addkarantina_activity_kawanan.setAdapter(myAdapter);

        //Insert To Database---------------------------------------------------
        button_addkarantina_activity_simpan = (Button)findViewById(R.id.button_addkarantina_activity_simpan);
        input_addkarantina_activity_diagnosis = (EditText)findViewById(R.id.input_addkarantina_activity_diagnosis);
        input_addkarantina_activity_perawatan = (EditText)findViewById(R.id.input_addkarantina_activity_perawatan);
        button_addkarantina_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cekForm()) {
                    String idkawanan = spinner_addkarantina_activity_kawanan.getSelectedItem().toString().trim();

                    String idkawanan_2 = idkawanan.substring(idkawanan.indexOf("(") + 1, idkawanan.indexOf(")"));

                    if (cekKarantina) {
                        String diagnosis = "N/A", perawatan = "N/A";
                        if (!input_addkarantina_activity_diagnosis.getText().toString().matches("")) {
                            diagnosis = input_addkarantina_activity_diagnosis.getText().toString();
                        }
                        if (!input_addkarantina_activity_perawatan.getText().toString().matches("")) {
                            perawatan = input_addkarantina_activity_perawatan.getText().toString();
                        }
                        String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                + "&idternak=" + input_addkarantina_activity_idternak.getText().toString()
                                + "&tglmulaikarantina=" + input_addkarantina_activity_tglpemeriksaan.getText().toString()
                                + "&perawatan=" + perawatan
                                + "&diagnosis=" + diagnosis
                                + "&idkawanan=" + idkawanan_2;

                        new AddKarantinatoDatabase().execute("http://ternaku.com/index.php/C_HistoryKesehatan/KarantinaMulai", param);
                    }
                    else{
                        String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                + "&idternak=" + input_addkarantina_activity_idternak.getText().toString()
                                + "&TglSelesaiKarantina=" + input_addkarantina_activity_tglpemeriksaan.getText().toString()
                                + "&idkawanan=" + idkawanan_2;

                        new AddKarantinatoDatabase().execute("http://ternaku.com/index.php/C_HistoryKesehatan/KarantinaSelesai", param);

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Data belum lengkap!", Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    //Get Data Kawanan------------------------------------------------------
    private class GetKawanan extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(AddKarantina.this);
            progDialog.setMessage("Tunggu Sebentar...");
            //progDialog.show();
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
            //progDialog.dismiss();
            AddKawananToList(result);
        }
    }

    private void AddKawananToList(String result) {
        list_addkarantina_kawanan.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addkarantina_kawanan.add("("+jObj.getString("id_kawanan")+") "+ jObj.getString("nama_kawanan"));
            }
            myAdapter.notifyDataSetChanged();
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Insert In To Database------------------------------------------
    private class AddKarantinatoDatabase extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(AddKarantina.this);
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
        fromDatePickerDialog_tglperiksa = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addkarantina_activity_tglpemeriksaan.setText(dateFormatter_tglperiksa.format(newDate.getTime()));
                datetime = dateFormatter_tglperiksa.format(newDate.getTime());
                mTimePicker.show();
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(AddKarantina.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                datetime+= " "+selectedHour + ":" + selectedMinute+":00";
                input_addkarantina_activity_tglpemeriksaan.setText(datetime);
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
        if(input_addkarantina_activity_idternak.getText().toString().matches("")){
            cek = false;
            input_addkarantina_activity_idternak.setError("Data belum diisi");
        }
        if(input_addkarantina_activity_tglpemeriksaan.getText().toString().equalsIgnoreCase("01 Januari 1970")){
            cek=false;
            input_addkarantina_activity_tglpemeriksaan.setError("Data belum diisi");
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

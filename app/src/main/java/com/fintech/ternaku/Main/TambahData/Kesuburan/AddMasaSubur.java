package com.fintech.ternaku.Main.TambahData.Kesuburan;

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
    int choosenindex=-1;

    ArrayList<String> list_addimasasubur_idternak = new ArrayList<String>();
    ArrayAdapter<String> myAdapter;
    boolean isHeat;
    String datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_masa_subur);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Masa Subur Ternak");
        }

        //Set Id Ternak Auto Complete-------------------------------------
        String param = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
        new GetTernakHeat().execute("http://ternaku.com/index.php/C_HistoryKesehatan/getDataTernakHeatByPeternakan", param);
        input_addmasasubur_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addmasasubur_activity_idternak);
        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_addimasasubur_idternak);
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
                    isHeat = true;
                    txt_addmasasubur_activity_ket.setText("Tanggal Heat");
                    linearLayout_addmasasubur_activity_diagnosis.setVisibility(View.VISIBLE);
                    linearLayout_addmasasubur_activity_biaya.setVisibility(View.VISIBLE);

                } else if(checkedId == R.id.radiobutton_addmasasubur_activity_selesai) {
                    isHeat = false;
                    txt_addmasasubur_activity_ket.setText("Tanggal Selesai Heat");
                    linearLayout_addmasasubur_activity_diagnosis.setVisibility(View.GONE);
                    linearLayout_addmasasubur_activity_biaya.setVisibility(View.GONE);
                }
            }
        });

        setDateTimeField();
        setTime();

        //Set Tanggal Pemeriksaan------------------------------------------
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
                    String perawatan = "N/A";
                    String diagnosis = "N/A";
                    String biayaperiksa = "0";

                    String urlParameters;

                    if(isHeat) {
                        if (!isSedangHeat(input_addmasasubur_activity_idternak.getText().toString().trim())) {
                            if (!input_addmasasubur_activity_biaya.getText().toString().matches("")) {
                                biayaperiksa = input_addmasasubur_activity_biaya.getText().toString();
                            }
                            if (!input_addmasasubur_activity_diagnosis.getText().toString().matches("")) {
                                diagnosis = input_addmasasubur_activity_diagnosis.getText().toString();
                            }
                            if (!input_addmasasubur_activity_perawatan.getText().toString().matches("")) {
                                perawatan = input_addmasasubur_activity_perawatan.getText().toString();
                            }
                            urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                    + "&idternak=" + input_addmasasubur_activity_idternak.getText().toString().trim()
                                    + "&tglmulaiheat=" + input_addmasasubur_activity_tglpemeriksaan.getText().toString()
                                    + "&diagnosis=" + diagnosis
                                    + "&perawatan=" + perawatan
                                    + "&biayaperiksa=" + biayaperiksa;
                            new UpdateMasaSubur().execute("http://ternaku.com/index.php/C_HistoryKesehatan/HeatMulai", urlParameters);
                        } else {
                            Toast.makeText(getApplicationContext(), "Sapi yang dipilih sedang heat!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        if (isSedangHeat(input_addmasasubur_activity_idternak.getText().toString().trim())) {
                            urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                                    + "&idternak=" + input_addmasasubur_activity_idternak.getText().toString().trim()
                                    + "&tglselesaiheat=" + input_addmasasubur_activity_tglpemeriksaan.getText().toString();
                            new UpdateMasaSubur().execute("http://ternaku.com/index.php/C_HistoryKesehatan/HeatSelesai", urlParameters);
                        } else {
                            Toast.makeText(getApplicationContext(), "Sapi yang dipilih sedang tidak heat!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

    }

    //Get Data Ternak AutoComplete------------------------------------------
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
                input_addmasasubur_activity_idternak.setEnabled(true);
            }
        }
    }
    private void AddTernakToList(String result)
    {
        list_addimasasubur_idternak.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addimasasubur_idternak.add(jObj.getString("id_ternak"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Insert To Database------------------------------------------------
    private class UpdateMasaSubur extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(AddMasaSubur.this);
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
                input_addmasasubur_activity_tglpemeriksaan.setText(dateFormatter_tglpemeriksaan.format(newDate.getTime()));
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
                //txtJam.setText( selectedHour + ":" + selectedMinute+":00");
                datetime+= " "+selectedHour + ":" + selectedMinute+":00";
                input_addmasasubur_activity_tglpemeriksaan.setText(datetime);
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
        for(int i=0;i<list_addimasasubur_idternak.size();i++){
            if(id.equals(list_addimasasubur_idternak.get(i))){
                cek = true;
            }
        }
        return cek;
    }

    private boolean checkForm() {
        boolean cek = true;

        if(input_addmasasubur_activity_idternak.getText().toString().matches(""))
        {input_addmasasubur_activity_idternak.setError("ID Ternak belum diisi");cek = false;}
        else if(input_addmasasubur_activity_tglpemeriksaan.getText().toString().matches(""))
        {input_addmasasubur_activity_tglpemeriksaan.setError("Tanggal belum diisi");cek = false;}
        else if(!radiobutton_addmasasubur_activity_mlai.isChecked()&&!radiobutton_addmasasubur_activity_selesai.isChecked())
        {radiobutton_addmasasubur_activity_mlai.setError("Pilih Salah Satu");
        radiobutton_addmasasubur_activity_selesai.setError("Pilih Salah Satu");
        cek=false;}

        return cek;
    }
}

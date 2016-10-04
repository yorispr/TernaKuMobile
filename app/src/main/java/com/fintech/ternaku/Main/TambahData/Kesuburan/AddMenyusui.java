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

public class AddMenyusui extends AppCompatActivity {
    private AutoCompleteTextView input_addmenyusui_activity_idternak;
    private RadioGroup radiogroup_addmenyusui_activity_mlaiselesai;
    private RadioButton radiobutton_addmenyusui_activity_mlai,radiobutton_addmenyusui_activity_selesai;
    private TextView input_addmenyusui_activity_tglpemeriksaan,txt_addmenyusui_activity_ket;
    private TimePickerDialog mTimePicker;
    private DatePickerDialog DatePickerDialog_tglmeyusui;
    private SimpleDateFormat dateFormatter_tglmenyusui;
    private Button button_addmenyusui_activity_simpan;
    String datetime;
    private boolean isMenyusui=false;
    private int choosenindex =-1;

    ArrayList<String> list_addmenyusui_idternak = new ArrayList<String >();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menyusui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Menyusui");
        }

        //Set Auto Text Id Ternak---------------------------------------------
        String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetTernakSudahMelahirkan().execute("http://ternaku.com/index.php/C_HistoryInseminasi/GetTernakSudahHamil", urlParameters);
        input_addmenyusui_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addmenyusui_activity_idternak);
        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,list_addmenyusui_idternak);
        input_addmenyusui_activity_idternak.setAdapter(adp);
        input_addmenyusui_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosenindex = i;
            }
        });
        input_addmenyusui_activity_idternak.setEnabled(false);

        //Set Radio Button-------------------------------------------------------
        radiogroup_addmenyusui_activity_mlaiselesai = (RadioGroup) findViewById(R.id.radiogroup_addmenyusui_activity_mlaiselesai);
        txt_addmenyusui_activity_ket = (TextView)findViewById(R.id.txt_addmenyusui_activity_ket);
        radiogroup_addmenyusui_activity_mlaiselesai.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radiobutton_addmenyusui_activity_mlai) {
                    isMenyusui=true;
                    txt_addmenyusui_activity_ket.setText("Tanggal mulai");
                } else if(checkedId == R.id.radiobutton_addmenyusui_activity_selesai) {
                    isMenyusui=false;
                    txt_addmenyusui_activity_ket.setText("Tanggal selesai");
                }
            }
        });

        //Set Tanggal Menyusui------------------------------------------------------
        setDateTimeField();
        setTime();
        input_addmenyusui_activity_tglpemeriksaan = (TextView)findViewById(R.id.input_addmenyusui_activity_tglpemeriksaan);
        dateFormatter_tglmenyusui = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addmenyusui_activity_tglpemeriksaan.setText(dateFormatter_tglmenyusui.format(Calendar.getInstance().getTime()));
        input_addmenyusui_activity_tglpemeriksaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddMenyusui.this);
                DatePickerDialog_tglmeyusui.show();
            }
        });

        //Insert Into Database-----------------------------------------------------
        button_addmenyusui_activity_simpan = (Button)findViewById(R.id.button_addmenyusui_activity_simpan);
        button_addmenyusui_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status;
                if(isMenyusui){status="1";}else{status="0";}
                String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                        + "&idternak=" + input_addmenyusui_activity_idternak.getText().toString().trim()
                        + "&tglperiksa=" + input_addmenyusui_activity_tglpemeriksaan.getText().toString()
                        + "&statusmenyusui="+status;
                new UpdateMenyusui().execute("http://ternaku.com/index.php/C_HistoryKesehatan/StatusMenyusui", urlParameters);
                Log.d("param",urlParameters);
            }
        });
    }

    //Get Data AutoText------------------------------------------------------
    //Nb: ini pake method get ternak sudah hamil-----------------------------
    private class GetTernakSudahMelahirkan extends AsyncTask<String,Integer,String> {
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
                input_addmenyusui_activity_idternak.setEnabled(true);
            }
        }
    }
    private void AddTernakToList(String result)
    {
        list_addmenyusui_idternak.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                list_addmenyusui_idternak.add(jObj.getString("id_ternak"));
            }
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Insert into Database---------------------------------------------------
    private class UpdateMenyusui extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(AddMenyusui.this);
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

    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(AddMenyusui.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                //txtJam.setText( selectedHour + ":" + selectedMinute+":00");
                datetime+= " "+selectedHour + ":" + selectedMinute+":00";
                input_addmenyusui_activity_tglpemeriksaan.setText(datetime);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
    }
    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog_tglmeyusui = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addmenyusui_activity_tglpemeriksaan.setText(dateFormatter_tglmenyusui.format(newDate.getTime()));
                datetime = dateFormatter_tglmenyusui.format(newDate.getTime());
                mTimePicker.show();
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


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}

package com.fintech.ternaku.Main.TambahData.Kesehatan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;

public class AddCekKesehatan extends AppCompatActivity {
    private AutoCompleteTextView input_addcekkesehatan_activity_idternak;
    private TextView input_addcekkesehatan_activity_tglpemeriksaan;
    private EditText input_addcekkesehatan_activity_suhubadan,input_addcekkesehatan_activity_beratbadan,
            input_addcekkesehatan_activity_tinggibadan,input_addcekkesehatan_activity_panjangbadan;
    private Spinner spinner_addcekkesehatan_activity_aktivitas,spinner_addcekkesehatan_activity_statusfisik,
            spinner_addcekkesehatan_activity_statusstress,spinner_addcekkesehatan_activity_produksisusu;
    private TextView input_addcekkesehatan_activity_conditionscore;
    private SeekBar seekbar_addcekkesehatan_activity_aktivitas;
    private Button button_addcekkesehatan_activity_simpan;

    private DatePickerDialog fromDatePickerDialog;
    private TimePickerDialog mTimePicker;
    private SimpleDateFormat dateFormatter;
    String datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cek_kesehatan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Cek Kesehatan");
        }

        //Set AutoComplete--------------------------------------
        input_addcekkesehatan_activity_idternak = (AutoCompleteTextView)findViewById(R.id.input_addcekkesehatan_activity_idternak);

        //Set Tanggal Pemeriksaan--------------------------------
        setDateTimeField();
        setTime();
        input_addcekkesehatan_activity_tglpemeriksaan = (TextView)findViewById(R.id.input_addcekkesehatan_activity_tglpemeriksaan);
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addcekkesehatan_activity_tglpemeriksaan.setText(dateFormatter.format(Calendar.getInstance().getTime()));
        input_addcekkesehatan_activity_tglpemeriksaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddCekKesehatan.this);
                fromDatePickerDialog.show();
            }
        });

        //Set Spinner Value--------------------------------------
        setSpinner();

        //Set Input Text Panjang,Berat,dll-----------------------
        input_addcekkesehatan_activity_suhubadan = (EditText)findViewById(R.id.input_addcekkesehatan_activity_suhubadan);
        input_addcekkesehatan_activity_beratbadan = (EditText)findViewById(R.id.input_addcekkesehatan_activity_beratbadan);
        input_addcekkesehatan_activity_tinggibadan = (EditText)findViewById(R.id.input_addcekkesehatan_activity_tinggibadan);
        input_addcekkesehatan_activity_panjangbadan = (EditText)findViewById(R.id.input_addcekkesehatan_activity_panjangbadan);

        //Set Seekbar--------------------------------------------
        input_addcekkesehatan_activity_conditionscore = (TextView)findViewById(R.id.input_addcekkesehatan_activity_conditionscore);
        seekbar_addcekkesehatan_activity_aktivitas = (SeekBar)findViewById(R.id.seekbar_addcekkesehatan_activity_aktivitas);
        seekbar_addcekkesehatan_activity_aktivitas.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                input_addcekkesehatan_activity_conditionscore.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Insert to Database-------------------------------------
        button_addcekkesehatan_activity_simpan = (Button)findViewById(R.id.button_addcekkesehatan_activity_simpan);
        button_addcekkesehatan_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                        + "&idternak=" + input_addcekkesehatan_activity_idternak.getText().toString().trim()
                        + "&tglperiksa=" + input_addcekkesehatan_activity_tglpemeriksaan.getText().toString()
                        + "&suhubadan="+input_addcekkesehatan_activity_suhubadan.getText().toString()
                        + "&beratbadan="+input_addcekkesehatan_activity_beratbadan.getText().toString()
                        + "&tinggiternak="+input_addcekkesehatan_activity_tinggibadan.getText().toString()
                        + "&aktivitas="+spinner_addcekkesehatan_activity_aktivitas.getSelectedItem().toString()
                        + "&produksisusu="+spinner_addcekkesehatan_activity_produksisusu.getSelectedItem().toString()
                        + "&statusfisik="+spinner_addcekkesehatan_activity_statusfisik.getSelectedItem().toString()
                        + "&statusStress="+spinner_addcekkesehatan_activity_statusstress.getSelectedItem().toString()
                        + "&bodyscore="+input_addcekkesehatan_activity_conditionscore.getText().toString()
                        ;

                new InsertKesehatan().execute("http://ternaku.com/index.php/C_HistoryKesehatan/InsertKesehatanUmum", urlParameters);
                Log.d("param",urlParameters);
            }
        });

    }

    //Insert To Database--------------------------------------------------
    private class InsertKesehatan extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(AddCekKesehatan.this);
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

    private void setSpinner(){
        spinner_addcekkesehatan_activity_aktivitas = (Spinner)findViewById(R.id.spinner_addcekkesehatan_activity_aktivitas);
        spinner_addcekkesehatan_activity_aktivitas.setPrompt("Aktivitas");
        final String[] dataAktivitas= {"Tinggi","Sedang","Rendah"};
        ArrayAdapter adapterAktivitas= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,dataAktivitas);
        spinner_addcekkesehatan_activity_aktivitas.setAdapter(adapterAktivitas);

        spinner_addcekkesehatan_activity_statusfisik = (Spinner)findViewById(R.id.spinner_addcekkesehatan_activity_statusfisik);
        spinner_addcekkesehatan_activity_statusfisik.setPrompt("Status Fisik");
        final String[] dataFisik= {"Sehat","Tidak Sehat"};
        ArrayAdapter adapterFisik= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,dataFisik);
        spinner_addcekkesehatan_activity_statusfisik.setAdapter(adapterFisik);

        spinner_addcekkesehatan_activity_statusstress = (Spinner)findViewById(R.id.spinner_addcekkesehatan_activity_statusstress);
        final String[] dataStress= {"Stress","Tidak Stress"};
        spinner_addcekkesehatan_activity_statusstress.setPrompt("Status Stress");
        ArrayAdapter adapterStress= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,dataStress);
        spinner_addcekkesehatan_activity_statusstress.setAdapter(adapterStress);

        spinner_addcekkesehatan_activity_produksisusu = (Spinner)findViewById(R.id.spinner_addcekkesehatan_activity_produksisusu);
        final String[] dataProduksi= {"Tinggi","Sedang","Rendah","Belum Diketahui"};
        spinner_addcekkesehatan_activity_produksisusu.setPrompt("Produksi Susu");
        ArrayAdapter adapterProduksi= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,dataProduksi);
        spinner_addcekkesehatan_activity_produksisusu.setAdapter(adapterProduksi);
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addcekkesehatan_activity_tglpemeriksaan.setText(dateFormatter.format(newDate.getTime()));
                datetime = dateFormatter.format(newDate.getTime());
                mTimePicker.show();
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(AddCekKesehatan.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                datetime+= " "+selectedHour + ":" + selectedMinute+":00";
                input_addcekkesehatan_activity_tglpemeriksaan.setText(datetime);
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
}

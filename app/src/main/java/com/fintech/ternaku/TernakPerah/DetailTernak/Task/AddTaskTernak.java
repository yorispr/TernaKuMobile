package com.fintech.ternaku.TernakPerah.DetailTernak.Task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.UrlList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddTaskTernak extends AppCompatActivity {
    private TimePickerDialog mTimePicker;
    private DatePickerDialog DatePickerDialog_tgltask;
    private SimpleDateFormat dateFormatter_tglmenyusui;
    private LinearLayout layouttanggal;
    private Button button_addtask_activity_simpan;
    private EditText edttask;
    String datetime,idternak;


    private TextView input_addtask_activity_tanggaltask;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_ternak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Tambah Task");
        }
        hideSoftKeyboard();

        //Get Data Id Ternak-------------------------------------
        idternak = getIntent().getExtras().getString("idternak");
        input_addtask_activity_tanggaltask = (TextView)findViewById(R.id.input_addtask_activity_tanggaltask) ;
        layouttanggal = (LinearLayout)findViewById(R.id.layouttanggal);
        button_addtask_activity_simpan = (Button)findViewById(R.id.button_addtask_activity_simpan);
        edttask = (EditText)findViewById(R.id.edtTask);


        setDateTimeField();
        setTime();
        dateFormatter_tglmenyusui = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        input_addtask_activity_tanggaltask.setText(dateFormatter_tglmenyusui.format(Calendar.getInstance().getTime()));

        layouttanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog_tgltask.show();
            }
        });

        button_addtask_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlParameters_ref = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null)
                        +"&idpeternakan="+getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan",null)
                        +"&idternak="+idternak
                        +"&isi="+edttask.getText().toString()
                        +"&tgltask="+input_addtask_activity_tanggaltask.getText().toString()
                        ;

                new InsertTask().execute(url.getUrl_InsertTask(), urlParameters_ref);

            }
        });

    }

    private class InsertTask extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddTaskTernak.this, SweetAlertDialog.PROGRESS_TYPE);

        @Override
        protected void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#fa6900"));
            pDialog.setTitleText("Menyimpan Data");
            pDialog.setCancelable(false);
            pDialog.show();
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
            pDialog.dismiss();

            if(result.trim().equals("1")){
                Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Terjadi Kesalahan",Toast.LENGTH_LONG).show();
            }

        }
    }
    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(AddTaskTernak.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(timePicker.isShown()) {

                    //txtJam.setText( selectedHour + ":" + selectedMinute+":00");
                    datetime += " " + selectedHour + ":" + selectedMinute + ":00";
                    input_addtask_activity_tanggaltask.setText(datetime);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
    }
    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog_tgltask = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_addtask_activity_tanggaltask.setText(dateFormatter_tglmenyusui.format(newDate.getTime()));
                datetime = dateFormatter_tglmenyusui.format(newDate.getTime());
                mTimePicker.show();
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}

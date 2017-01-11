package com.fintech.ternaku.TernakPerah.Main.Scheduler;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fintech.ternaku.TernakPerah.Main.Pengingat.ReminderModel;
import com.fintech.ternaku.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AddScheduleActivity extends AppCompatActivity {
    private EditText edtjudul,edtisi;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference firebaseDatabase = database.getReference("schedule");
    private DatePickerDialog fromDatePickerDialog_tglperiksa;
    private TimePickerDialog mTimePicker;
    private SimpleDateFormat dateFormatter_tglperiksa;
    private String datetime;
    LinearLayout linearLayout_addreminder_fragment_tglkegiatan;
    Switch chkTime;
    private TextView txtTgl;
    private Spinner spinRepetisi;
    String key;
    String idpeternakan,idpengguna,namapengguna,isi="",judul="";
    String repeat = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tambah Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });fab.hide();

        idpengguna = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        namapengguna = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyNama",null);
        idpeternakan = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan",null);

        edtjudul = (EditText)findViewById(R.id.txtJudul);
        edtisi = (EditText)findViewById(R.id.edtIsi);
        chkTime = (Switch)findViewById(R.id.switchRepetisi);
        txtTgl = (TextView)findViewById(R.id.txtTgl);

        spinRepetisi = (Spinner)findViewById(R.id.spinnerRepeat);
        String spindata[] = {"Harian","Mingguan","Bulanan"};

        ArrayAdapter<String>spinAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,spindata);
        spinRepetisi.setAdapter(spinAdapter);

        spinRepetisi.setEnabled(false);
        chkTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    spinRepetisi.setEnabled(true);
                    repeat = "1";
                }else{
                    spinRepetisi.setEnabled(false);
                    repeat = "0";
                }
            }
        });
        spinRepetisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                repeat = String.valueOf(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        linearLayout_addreminder_fragment_tglkegiatan = (LinearLayout) findViewById(R.id.linearLayout_addreminder_fragment_tglkegiatan);
        setDateTimeField();
        setTime();

        dateFormatter_tglperiksa = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        txtTgl.setText(dateFormatter_tglperiksa.format(Calendar.getInstance().getTime()));
        linearLayout_addreminder_fragment_tglkegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddScheduleActivity.this);
                fromDatePickerDialog_tglperiksa.show();
            }
        });


    }
    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog_tglperiksa = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtTgl.setText(dateFormatter_tglperiksa.format(newDate.getTime()));
                datetime = dateFormatter_tglperiksa.format(newDate.getTime());
                mTimePicker.show();
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

    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        final String hour2 = String.valueOf(mcurrentTime.get(Calendar.HOUR_OF_DAY));
        final  String minute2 = String.valueOf(mcurrentTime.get(Calendar.MINUTE));

        mTimePicker = new TimePickerDialog(AddScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if(timePicker.isShown()) {
                    datetime += " " + selectedHour + ":" + selectedMinute + ":00";
                    txtTgl.setText(datetime);
                }
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_reminder_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;

            case  R.id.send_menu:
                InsertData();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void InsertData()
    {
        key = firebaseDatabase.child(idpeternakan).child(idpengguna).push().getKey();
        SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        String CurrentDate = df1.format(new Date());

        ReminderModel pm = new ReminderModel(key,edtjudul.getText().toString(),edtisi.getText().toString(),false,idpengguna,namapengguna,CurrentDate,repeat,0,txtTgl.getText().toString());
        firebaseDatabase.child(idpeternakan).child(key).setValue(pm);
        SetAlarm(pm,0);
        /*String param = "id_reminder=" + key
                +"&judul=" + judul
                +"&isi=" + isi
                +"&isimportant=" + false
                +"&creator=" + namapengguna
                +"&creator_id=" + idpengguna
                +"&timestamp=" + CurrentDate
                +"&idpeternakan=" + idpeternakan
                +"&scheduletime=" + txtTgl.getText().toString()
                ;
        Log.d("URLReminder",param);
        new AddReminderActivity.SendNotif().execute(url.getUrlInsert_Reminder(), param);*/
    }


    public void SetAlarm(ReminderModel rm, int re) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

        Date date = new Date();
        try {
            date = dateFormat.parse(rm.getSchedule_time());
            Log.d("TanggalReminder", date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cur_cal = Calendar.getInstance();
        cur_cal.setTime(date);
        Calendar cal = new GregorianCalendar();

        cal.set(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
        cal.set(Calendar.HOUR_OF_DAY, cur_cal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cur_cal.get(Calendar.MINUTE)-re);
        cal.set(Calendar.SECOND, cur_cal.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cur_cal.get(Calendar.MILLISECOND));
        cal.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
        cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));
        cal.set(Calendar.YEAR, cur_cal.get(Calendar.YEAR));

        PendingIntent pendingIntent;

        Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);

        myIntent.putExtra("reminder","reminder");

        Bundle b = new Bundle();
        b.putString("id_protocol",rm.getId_protocol());
        b.putString("isi",rm.getIsi());
        b.putString("judul",rm.getJudul());
        b.putString("repetisi",rm.getIs_repeat());

        myIntent.putExtras(b);

        final int _id = (int) System.currentTimeMillis();
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), _id, myIntent,0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
        Log.d("calendar",String.valueOf(cal.get(Calendar.DATE) +" "+ cal.get(Calendar.MONTH)+" "+ cal.get(Calendar.YEAR) + " "+cal.get(Calendar.HOUR_OF_DAY) +":"+cal.get(Calendar.MINUTE)));

        long yourmilliseconds = cal.getTimeInMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(yourmilliseconds);

        Log.d("milis",sdf.format(resultdate));


        Intent intentt = new Intent();
        intentt.setAction("REFRESH_DATE");
        sendBroadcast(intentt);
    }
}

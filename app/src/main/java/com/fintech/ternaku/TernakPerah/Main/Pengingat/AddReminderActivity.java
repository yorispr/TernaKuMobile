package com.fintech.ternaku.TernakPerah.Main.Pengingat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.DatabaseHandler;
import com.fintech.ternaku.R;
import com.fintech.ternaku.UrlList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddReminderActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference firebaseDatabase = database.getReference("reminder");
    private EditText edtjudul,edtisi;
    private TextView txtTgl;
    String idpeternakan,idpengguna,namapengguna,isi="",judul="";
    String key,creator_id;
    LinearLayout linearLayout_addreminder_fragment_tglkegiatan;
    CheckBox chkimportant;
    boolean isEdit = false;
    boolean isimportant;
    private DatePickerDialog fromDatePickerDialog_tglperiksa;
    private TimePickerDialog mTimePicker;
    private SimpleDateFormat dateFormatter_tglperiksa;
    private String datetime;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tambah Reminder");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idpengguna = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        namapengguna = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyNama",null);
        idpeternakan = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan",null);
        FirebaseMessaging.getInstance().subscribeToTopic(idpeternakan);

        edtjudul = (EditText)findViewById(R.id.txtJudul);
        edtisi = (EditText)findViewById(R.id.edtIsi);
        chkimportant = (CheckBox)findViewById(R.id.chkImportant);
        txtTgl = (TextView)findViewById(R.id.txtTgl);
        linearLayout_addreminder_fragment_tglkegiatan = (LinearLayout) findViewById(R.id.linearLayout_addreminder_fragment_tglkegiatan);
        setDateTimeField();
        setTime();
        dateFormatter_tglperiksa = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        txtTgl.setText(dateFormatter_tglperiksa.format(Calendar.getInstance().getTime()));
        linearLayout_addreminder_fragment_tglkegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(AddReminderActivity.this);
                fromDatePickerDialog_tglperiksa.show();
            }
        });

        chkimportant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isimportant = true;
                }else{
                    isimportant = false;
                }
            }
        });

        edtjudul.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                judul = s.toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //doSomething();
                //firebaseDatabase.child(idpeternakan).child(idpengguna).child(key).child("judul").setValue(s.toString());
            }

        });
        edtisi.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                isi = s.toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void InsertData()
    {
        key = firebaseDatabase.child(idpeternakan).child(idpengguna).push().getKey();
        SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy HH:mm");
        String CurrentDate = df1.format(new Date());
        ReminderModel pm = new ReminderModel(key,judul,isi,isimportant,idpengguna,namapengguna,CurrentDate,"0",0,txtTgl.getText().toString());
        DatabaseHandler db = new DatabaseHandler(this);
        //db.addReminder(pm);
        firebaseDatabase.child(idpeternakan).child(key).setValue(pm);

        String param = "id_reminder=" + key
                +"&judul=" + judul
                +"&isi=" + isi
                +"&isimportant=" + isimportant
                +"&creator=" + namapengguna
                +"&creator_id=" + idpengguna
                +"&timestamp=" + CurrentDate
                +"&idpeternakan=" + idpeternakan
                +"&scheduletime=" + txtTgl.getText().toString()
                ;
        Log.d("URLReminder",param);
        new SendNotif().execute(url.getUrlInsert_Reminder(), param);
    }

    private class SendNotif extends AsyncTask<String,Integer,String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AddReminderActivity.this);

            progressDialog.setTitle("Mengirim pesan...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0],params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("adit",result);
            progressDialog.dismiss();
            Intent intent = new Intent();
            intent.setAction("com.tutorialspoint.CUSTOM_INTENT");
            sendBroadcast(intent);

            new SweetAlertDialog(AddReminderActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Berhasil")
                    .setContentText("Reminder Berhasil Ditambahkan")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                        }
                    })
                    .show();
        }
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

        mTimePicker = new TimePickerDialog(AddReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

}

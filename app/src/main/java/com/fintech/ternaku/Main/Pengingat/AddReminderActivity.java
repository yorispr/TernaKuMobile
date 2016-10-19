package com.fintech.ternaku.Main.Pengingat;

import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.DatabaseHandler;
import com.fintech.ternaku.Main.TambahData.Kesuburan.InjeksiHormon.ModelAddProtokolInjeksi;
import com.fintech.ternaku.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddReminderActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference firebaseDatabase = database.getReference("reminder");
    private EditText edtjudul,edtisi;
    String idpeternakan,idpengguna,namapengguna,isi="",judul="";
    String key,creator_id;
    CheckBox chkimportant;
    boolean isEdit = false;
    boolean isimportant;

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
        ModelAddProtokolInjeksi pm = new ModelAddProtokolInjeksi(key,judul,isi,isimportant,idpengguna,namapengguna,CurrentDate,0);
        DatabaseHandler db = new DatabaseHandler(this);
        //db.addReminder(pm);
        firebaseDatabase.child(idpeternakan).child(idpengguna).child(key).setValue(pm);

        String param = "id_reminder=" + key
                +"&judul=" + judul
                +"&isi=" + isi
                +"&isimportant=" + isimportant
                +"&creator=" + namapengguna
                +"&creator_id=" + idpengguna
                +"&timestamp=" + CurrentDate
                +"&idpeternakan=" + idpeternakan
                ;
        new SendNotif().execute("http://ternaku.com/index.php/C_Fcm/send_message", param);
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
            Log.d("RES",result);
            progressDialog.dismiss();
            Intent intent = new Intent();
            intent.setAction("com.tutorialspoint.CUSTOM_INTENT");
            sendBroadcast(intent);
        }
    }

}

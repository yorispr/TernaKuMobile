package com.fintech.ternaku.TernakPerah.Main.TambahData.Kesuburan.InjeksiHormon;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.fintech.ternaku.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddInjeksiHormon extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference firebaseDatabase = database.getReference("protocol");
    private EditText input_injeksiprotokol_activity_judul,input_injeksiprotokol_activity_isi;
    String idpeternakan,idpengguna,namapengguna,isi="",judul="";
    String key,creator_id;
    boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_injeksi_hormon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tambah Protokol Injeksi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idpengguna = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        namapengguna = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyNama",null);
        idpeternakan = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan",null);

        //Set New Data-------------------------------------------------------------
        input_injeksiprotokol_activity_judul = (EditText)findViewById(R.id.input_injeksiprotokol_activity_judul);
        input_injeksiprotokol_activity_isi = (EditText)findViewById(R.id.input_injeksiprotokol_activity_isi);
        if(getIntent().getStringExtra("key").equalsIgnoreCase("0"))
        {
            ModelAddProtokolInjeksi p = new ModelAddProtokolInjeksi();
            Bundle b = getIntent().getExtras();
            p.setId_protocol(b.getString("idprotokol"));
            p.setIsi(b.getString("isi"));
            p.setJudul(b.getString("judul"));
            p.setCreator(b.getString("creator"));
            p.setImportant(b.getBoolean("important"));
            p.setCreator_id(b.getString("creator_id"));

            key = p.getId_protocol();
            input_injeksiprotokol_activity_judul.setText(p.getJudul());
            input_injeksiprotokol_activity_isi.setText(p.getIsi());

            judul = p.getJudul();
            isi = p.getIsi();
            creator_id = p.getCreator_id();
            if (!creator_id.equalsIgnoreCase(idpengguna)) {
                input_injeksiprotokol_activity_judul.setEnabled(false);
                input_injeksiprotokol_activity_isi.setEnabled(false);
            }
            isEdit = true;
        }

        else{key = getIntent().getStringExtra("key");}

        input_injeksiprotokol_activity_judul.addTextChangedListener(new TextWatcher() {
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
        input_injeksiprotokol_activity_isi.addTextChangedListener(new TextWatcher() {
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
                //doSomething();
                // firebaseDatabase.child(idpeternakan).child(idpengguna).child(key).child("isi").setValue(s.toString());
            }

        });
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
        if (isEdit) {
            Log.d("key",key);
            if(creator_id.equals(idpengguna)) {
                firebaseDatabase.child(idpeternakan).child(idpengguna).child(key).child("judul").setValue(judul);
                firebaseDatabase.child(idpeternakan).child(idpengguna).child(key).child("isi").setValue(isi);
                isEdit = false;
            }
        }else{ InsertData();}
    }

    private void InsertData()
    {
        key = firebaseDatabase.child(idpeternakan).child(idpengguna).push().getKey();

        SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yyyy HH:mm");
        String CurrentDate = df1.format(new Date());

        ModelAddProtokolInjeksi pm = new ModelAddProtokolInjeksi(key,judul,isi,false,idpengguna,namapengguna,CurrentDate,0);
        firebaseDatabase.child(idpeternakan).child(idpengguna).child(key).setValue(pm);

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

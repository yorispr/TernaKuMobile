package com.fintech.ternaku.TernakPerah.Main.TambahData.Kesuburan.InjeksiHormon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fintech.ternaku.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowInjeksiHormon extends AppCompatActivity {
    ListView list_showinjeksiprotokol_activity;
    ArrayList<ModelAddProtokolInjeksi> protocolList = new ArrayList<ModelAddProtokolInjeksi>();
    AdapterListProtokolInjeksi adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference firebaseDatabase = database.getReference("protocol");
    Button button_showinjeksiprotokol_activity_tambah;
    ProgressBar prog_showinjeksiprotokol_activity;
    String idpengguna;
    String idpeternakan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_injeksi_hormon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Protokol Injeksi");
        }

        idpeternakan = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan",null);
        idpengguna = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);

        list_showinjeksiprotokol_activity = (ListView) findViewById(R.id.list_showinjeksiprotokol_activity);
        prog_showinjeksiprotokol_activity = (ProgressBar)findViewById(R.id.prog_showinjeksiprotokol_activity);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                protocolList.clear();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    for (DataSnapshot child2 : child.getChildren()) {
                        Log.d("CHANGE", dataSnapshot.getValue().toString());
                        ModelAddProtokolInjeksi newdata = child2.getValue(ModelAddProtokolInjeksi.class);
                        protocolList.add(newdata);
                    }
                }
                adapter.notifyDataSetChanged();
                prog_showinjeksiprotokol_activity.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("GETDATA", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        firebaseDatabase.child(idpeternakan).addValueEventListener(postListener);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    ModelAddProtokolInjeksi newdata = child.getValue(ModelAddProtokolInjeksi.class);
                    protocolList.add(newdata);
                }
                adapter.notifyDataSetChanged();
                prog_showinjeksiprotokol_activity.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                protocolList.clear();

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Log.d("CHANGE",dataSnapshot.getValue().toString());
                    ModelAddProtokolInjeksi newdata = child.getValue(ModelAddProtokolInjeksi.class);
                    protocolList.add(newdata);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("P3Remove",dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        //firebaseDatabase.child(idpeternakan).addChildEventListener(childEventListener);

        list_showinjeksiprotokol_activity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ModelAddProtokolInjeksi pm = protocolList.get(i);
                Intent act = new Intent(ShowInjeksiHormon.this, AddInjeksiHormon.class);
                Bundle b = new Bundle();
                b.putString("idprotokol",pm.getId_protocol());
                b.putString("judul",pm.getJudul());
                b.putString("isi",pm.getIsi());
                b.putString("creator",pm.getCreator());
                b.putBoolean("important",pm.isImportant());
                b.putString("creator_id",pm.getCreator_id());
                b.putString("timestamp",pm.getTimestamp());

                act.putExtras(b);
                String key = "0";
                act.putExtra("key",key);

                startActivity(act);
            }
        });


        adapter = new AdapterListProtokolInjeksi(ShowInjeksiHormon.this, protocolList);
        list_showinjeksiprotokol_activity.setAdapter(adapter);
        list_showinjeksiprotokol_activity.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int post, long l) {
                if(idpengguna.equalsIgnoreCase(protocolList.get(post).getCreator_id())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowInjeksiHormon.this);
                    final int pos = post;
                    builder.setMessage("Apakah anda ingin menghapus protokol ini ?");
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            firebaseDatabase.child(idpeternakan).child(idpengguna).child(protocolList.get(pos).getId_protocol()).removeValue();
                            protocolList.remove(pos);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("Tidak", null);
                    builder.show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Tidak dapat menghapus",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });


        button_showinjeksiprotokol_activity_tambah = (Button)findViewById(R.id.button_showinjeksiprotokol_activity_tambah);
        button_showinjeksiprotokol_activity_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShowInjeksiHormon.this,AddInjeksiHormon.class);
                String key = firebaseDatabase.child(idpeternakan).child(idpengguna).push().getKey();
                i.putExtra("key",key);
                startActivityForResult(i,1);
            }
        });

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

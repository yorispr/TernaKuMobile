package com.fintech.ternaku.TernakPerah.Main.Pengingat;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.fintech.ternaku.DatabaseHandler;
import com.fintech.ternaku.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.wangyuwei.loadingview.LoadingView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowReminderFragment extends Fragment {
    ListView l ;
    ArrayList<ReminderModel> protocolList = new ArrayList<ReminderModel>();
    ArrayList<ReminderModel> protocolListtemp = new ArrayList<ReminderModel>();

    AdapterReminderListProtocol adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference firebaseDatabase = database.getReference("reminder");
    Button btnTambah;
    String idpengguna;
    String idpeternakan;
    List<ReminderModel> ReminderList;
    DatabaseHandler db;
    private DatabaseReference mDatabase;
    private LoadingView loading_view_reminder;


    public ShowReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_reminder, container, false);
        // Inflate the layout for this fragment
        idpengguna = getActivity().getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        idpeternakan = getActivity().getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan",null);

        //Set ListView-------------------------------------------------------
        l = (ListView) view.findViewById(R.id.reminder_list);
        adapter = new AdapterReminderListProtocol(getContext(), protocolList);
        l.setAdapter(adapter);

        //Set Fragment-------------------------------------------------------
        loading_view_reminder = (LoadingView) view.findViewById(R.id.loading_view_reminder);
        InitiateFragment();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                protocolList.clear();
                // Get Post object and use the values to update the UI
                for (DataSnapshot imageSnapshot: dataSnapshot.getChildren()) {
                   ReminderModel r = new ReminderModel();
                    r = imageSnapshot.getValue(ReminderModel.class);
                   protocolList.add(r);
                   Log.d("Loads",  r.getJudul());
                }

                protocolListtemp = db.getReminder();
                Log.d("IsiSQLLite", String.valueOf(protocolListtemp.size()));
                Log.d("IsiFirebase", String.valueOf(protocolList.size()));
                if(protocolListtemp.size() != protocolList.size()){
                    db.ClearReminder();
                    for(int i=0;i<protocolList.size();i++){
                        db.addReminder(protocolList.get(i));
                    }
                }
                Collections.reverse(protocolList);

                adapter.notifyDataSetChanged();
                RefreshFragment();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("Load", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        firebaseDatabase.child(idpeternakan).orderByChild("timestamp").addListenerForSingleValueEvent(postListener);


        db = new DatabaseHandler(getContext());
        //protocolList = db.getReminder();

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ReminderModel pm = protocolList.get(i);
                db.updateRead(pm.getId_protocol());
                Intent act = new Intent(getContext(), ViewReminderActivity.class);
                act.putExtra("id",pm.getId_protocol());
                startActivity(act);
            }
        });

        l.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int post, long l) {
                final int position = post;
                if(idpengguna.equalsIgnoreCase(protocolList.get(post).getCreator_id())) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Peringatan")
                            .setContentText("Apakah Ingin Menghapus Pengingat Ini?")
                            .setConfirmText("Ya")
                            .setCancelText("Tidak")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                    firebaseDatabase.child(idpeternakan).child(idpengguna).child(protocolList.get(position).getId_protocol()).removeValue();
                                    protocolList.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                }
                            })
                            .show();
                }
                else{
                   new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                           .setTitleText("Error")
                           .setContentText("Pengingat Tidak Dapat Dihapus")
                           .setConfirmText("Ok")
                           .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                               @Override
                               public void onClick(SweetAlertDialog sweetAlertDialog) {
                                   sweetAlertDialog.dismiss();
                               }
                           })
                           .show();
                }
                return true;
            }
        });

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("com.tutorialspoint.CUSTOM_INTENT"));



        btnTambah = (Button)view.findViewById(R.id.btnTambahProtokol);
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),AddReminderActivity.class);
                String key = firebaseDatabase.child(idpeternakan).child(idpengguna).push().getKey();
                startActivityForResult(i,1);
            }
        });


        return view;
    }

    //Initiate Fragment----------------------------------------------------
    private void InitiateFragment(){
        loading_view_reminder.setVisibility(View.VISIBLE);
        l.setVisibility(View.GONE);
        loading_view_reminder.start();
    }
    private void RefreshFragment(){
        loading_view_reminder.setVisibility(View.GONE);
        l.setVisibility(View.VISIBLE);
        loading_view_reminder.stop();
    }

    public  void updateList(){
        protocolList.clear();
        protocolList = db.getReminder();
        adapter = new AdapterReminderListProtocol(getContext(), protocolList);
        l.setAdapter(adapter);
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // internet lost alert dialog method call from here...
            Log.d("Intent","Broadcast Received");
            updateList();
        }
    };

    /*public static class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
            Log.d("Intent","Broadcast Received");
            context.sendBroadcast(intent);
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}


package com.fintech.ternaku.Main.Pengingat;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fintech.ternaku.DatabaseHandler;
import com.fintech.ternaku.Main.TambahData.Kesehatan.ProtocolKesehatan.ModelAddProtokolKesehatan;
import com.fintech.ternaku.Main.TambahData.Kesuburan.InjeksiHormon.AdapterListProtokolInjeksi;
import com.fintech.ternaku.Main.TambahData.Kesuburan.InjeksiHormon.ModelAddProtokolInjeksi;
import com.fintech.ternaku.R;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowReminderFragment extends Fragment {
    ListView l ;
    ArrayList<ModelAddProtokolInjeksi> protocolList = new ArrayList<ModelAddProtokolInjeksi>();
    AdapterListProtokolInjeksi adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference firebaseDatabase = database.getReference("reminder");
    Button btnTambah;
    String idpengguna;
    String idpeternakan;
    List<ReminderModel> ReminderList;
    DatabaseHandler db;
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

        l = (ListView) view.findViewById(R.id.reminder_list);
        db = new DatabaseHandler(getContext());
        protocolList = db.getReminder();

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ModelAddProtokolInjeksi pm = protocolList.get(i);
                db.updateRead(pm.getId_protocol());
                Intent act = new Intent(getContext(), ViewReminderActivity.class);
                act.putExtra("id",pm.getId_protocol());
                startActivity(act);
            }
        });

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("com.tutorialspoint.CUSTOM_INTENT"));

        adapter = new AdapterListProtokolInjeksi(getContext(), protocolList);
        l.setAdapter(adapter);

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


    public  void updateList(){
        protocolList.clear();
        protocolList = db.getReminder();
        adapter = new AdapterListProtokolInjeksi(getContext(), protocolList);
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


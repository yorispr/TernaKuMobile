package com.fintech.ternaku.Setting;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fintech.ternaku.R;

public class BluetoothAct extends AppCompatActivity {
    public final String TAG = "Main";

    private TextView status;
    private Bluetooth bt;
    private Button restart,bton;
    BluetoothAdapter bluetoothAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Bluetooth");
        }
        status = (TextView) findViewById(R.id.textStatus);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bton = (Button)findViewById(R.id.buttonOn);
        if(bluetoothAdapter.isEnabled()){
            bton.setVisibility(View.GONE);
        }else{
            bton.setVisibility(View.VISIBLE);
        }

        bton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBluetooth();
            }
        });

        restart = (Button)findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectService();
            }
        });


        bt = new Bluetooth(this, mHandler);
        connectService();

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    /* ... */

        // Unregister broadcast listeners
        unregisterReceiver(mReceiver);
    }

    private void onBluetooth() {
        if(!bluetoothAdapter.isEnabled())
        {
            bluetoothAdapter.enable();
            Log.i("Log", "Bluetooth is Enabled");
        }
    }

    private void offBluetooth() {
        if(bluetoothAdapter.isEnabled())
        {
            bluetoothAdapter.disable();
            Log.i("Log", "Bluetooth is Disabled");
        }
    }
    public void connectService(){
        try {
            status.setText("Sedang menghubungkan..");
            //BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                bt.start();
                bt.connectDevice("HC-06");
                Log.d(TAG, "Btservice started - listening");
                status.setText("Terhubung");
                status.setTextColor(Color.parseColor("#2ecc71"));
            } else {
                Log.w(TAG, "Btservice started - bluetooth is not enabled");
                status.setText("Bluetooth tidak aktif");
                status.setTextColor(Color.parseColor("#e74c3c"));

            }
        } catch(Exception e){
            Log.e(TAG, "Unable to start bt ",e);
            status.setText("Gagal terhubung : " +e);
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                       // setButtonText("Bluetooth off");
                        bton.setVisibility(View.VISIBLE);
                        connectService();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        //setButtonText("Turning Bluetooth off...");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        connectService();
                        bton.setVisibility(View.GONE);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        //setButtonText("Turning Bluetooth on...");
                        break;
                }
            }
        }
    };
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Bluetooth.MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    break;
                case Bluetooth.MESSAGE_WRITE:
                    Log.d(TAG, "MESSAGE_WRITE ");
                    break;
                case Bluetooth.MESSAGE_READ:
                    Log.d(TAG, "MESSAGE_READ ");
                    break;
                case Bluetooth.MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "MESSAGE_DEVICE_NAME "+msg);
                    break;
                case Bluetooth.MESSAGE_TOAST:
                    Log.d(TAG, "MESSAGE_TOAST "+msg);
                    break;
            }
        }
    };

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

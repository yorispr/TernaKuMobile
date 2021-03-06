package com.fintech.ternaku.TernakPotong.AddPakan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.Setting.Bluetooth;
import com.fintech.ternaku.TernakPotong.SapiPotongAddKomposisiPakan;
import com.fintech.ternaku.UrlList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.fintech.ternaku.R.id.textView;
import static com.fintech.ternaku.R.id.time;

public class AddPakanPedaging extends AppCompatActivity {
    private ListView list_addpakanpedaging_activity;
    private EditText input_addpakanpedaging_activity_pilihkonsentrat,input_addpakanpedaging_activity_konsentrat;
    private Button btn_simpan;
    private LinearLayout linearlayout_addpakanpedaging_activity_loading,linearlayout_addpakanpedaging_activity_main;
    private String idpeternakan;
    List<ModelAddPakanPedaging> listdata_addpakanpedaging_activity_temp = new ArrayList<ModelAddPakanPedaging>();
    List<ModelAddPakanPedaging> listdata_addpakanpedaging_activity_main = new ArrayList<ModelAddPakanPedaging>();
    AdapterAddPakanPedaging adapterAddPakanPedaging;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    //Get Current Time----------------------
    public static final String inputFormat = "hh:mm a";

    private Date date;
    private Date dateCompareOne;
    private Date dateCompareTwo;

    private String compareStringOne = "00:01";
    private String compareStringTwo = "11:59";
    String time;
    Calendar calender;
    SimpleDateFormat simpleDateFormat;

    //Set Data Komposisi----------------------
    int choosenindexkomposisi;
    Dialog dialog_list_komposisi;
    AdapterAddPakanPedagingListKomposisi adapter_list_komposisi;
    ArrayList<ModelAddPakanPedagingListKomposisi> KomposisiList  = new ArrayList<ModelAddPakanPedagingListKomposisi>();

    //Set Up RFID-----------------------------
    private Bluetooth bt;
    public final String TAG = "AddPakan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pakan_pedaging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                    | ActionBar.DISPLAY_SHOW_TITLE
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("Insert Pakan");
        }
        hideSoftKeyboard();
        

        //Initiate Input Komposisi-----------------------------------------------------------
        input_addpakanpedaging_activity_konsentrat = (EditText) findViewById(R.id.input_addpakanpedaging_activity_konsentrat);
        input_addpakanpedaging_activity_konsentrat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlParameters = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan",null);
                new GetKomposisi().execute(url.getUrl_GetKomposisi(), urlParameters);
            }
        });
        input_addpakanpedaging_activity_konsentrat.addTextChangedListener(CheckKonsentrat);

        //Initiate TextWatcher-------------------------------------------------------
        input_addpakanpedaging_activity_pilihkonsentrat = (EditText) findViewById(R.id.input_addpakanpedaging_activity_pilihkonsentrat);
        input_addpakanpedaging_activity_pilihkonsentrat.addTextChangedListener(CheckId);

        //Initiate ListView----------------------------------------------------------
        list_addpakanpedaging_activity = (ListView)findViewById(R.id.list_addpakanpedaging_activity);

        //Initiate Button------------------------------------------------------------
        btn_simpan = (Button) findViewById(R.id.btn_simpan);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DelData();
            }
        });

        //Initial----------------------------------------------------
        linearlayout_addpakanpedaging_activity_loading = (LinearLayout)findViewById(R.id.linearlayout_addpakanpedaging_activity_loading);
        linearlayout_addpakanpedaging_activity_main = (LinearLayout) findViewById(R.id.linearlayout_addpakanpedaging_activity_main);
        idpeternakan =  getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan",null);
        choosenindexkomposisi = -1;
        if(input_addpakanpedaging_activity_konsentrat.getText().toString().equalsIgnoreCase("")){
            InitiateUI();
        }

        //Set RFID Bluetooth----------------------------------------------------------
        bt = new Bluetooth(this, mHandler);
        bt.start();
        bt.connectDevice("HC-06");
    }

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
                    final String rfid = msg.obj.toString();
                    input_addpakanpedaging_activity_pilihkonsentrat.setText(rfid);
                    Log.d(TAG, "MESSAGE_READ : "+msg.obj.toString());
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

    private void InitiateUI(){
        linearlayout_addpakanpedaging_activity_loading.setVisibility(View.VISIBLE);
        linearlayout_addpakanpedaging_activity_main.setVisibility(View.GONE);
    }

    private void RefreshUI(){
        linearlayout_addpakanpedaging_activity_loading.setVisibility(View.GONE);
        linearlayout_addpakanpedaging_activity_main.setVisibility(View.VISIBLE);
    }

    //Get Data Kandang--------------------------------------------------------
    private class GetKomposisi extends AsyncTask<String,Integer,String> {
        SweetAlertDialog pDialog = new SweetAlertDialog(AddPakanPedaging.this, SweetAlertDialog.PROGRESS_TYPE);

        @Override
        protected void onPreExecute() {
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#fa6900"));
            pDialog.setTitleText("Memuat Data");
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
            if(result.trim().equals("kosong")){
                new SweetAlertDialog(AddPakanPedaging.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!")
                        .setContentText("Koneksi Terputus!")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                            }
                        })
                        .show();
            }else if (result.trim().equals("404")){
                new SweetAlertDialog(AddPakanPedaging.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Gagal Memuat Data")
                        .setContentText("Data Komposisi Masih Kosong" + "\nApakah Ingin Memasukkan Data Komposisi?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                finish();
                                startActivity(new Intent(AddPakanPedaging.this, SapiPotongAddKomposisiPakan.class));
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                finish();
                            }
                        })
                        .show();
            }
            else{
                AddKomposisiToList(result);
            }
        }
    }

    private void AddKomposisiToList(String result) {
        KomposisiList.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                ModelAddPakanPedagingListKomposisi k = new ModelAddPakanPedagingListKomposisi();
                k.setId_komposisi(jObj.getString("id_komposisi"));
                k.setJenis_sapi(jObj.getString("jenis_sapi"));
                k.setUmur_bawah(jObj.getString("usia_bawah"));
                k.setUmur_atas(jObj.getString("usia_atas"));
                k.setKg_konsentrat(jObj.getString("kg_konsentrat"));
                k.setKg_hijauan(jObj.getString("kg_hijauan"));
                k.setTotal_harga(jObj.getString("total_harga"));
                KomposisiList.add(k);
            }
            ShowDialogKomposisi(KomposisiList);
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private void ShowDialogKomposisi(final ArrayList<ModelAddPakanPedagingListKomposisi> Komposisi) {
        dialog_list_komposisi = new Dialog(AddPakanPedaging.this);
        dialog_list_komposisi.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_list_komposisi.setContentView(R.layout.list_komposisi_addpakanpedaing);

        ListView list = (ListView) dialog_list_komposisi.findViewById(R.id.list_komposisi_dial);
        EditText edtsearch = (EditText) dialog_list_komposisi.findViewById(R.id.edtSearchDialogKomposisi);
        Button btnbtl = (Button) dialog_list_komposisi.findViewById(R.id.btnbtl);

        btnbtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_list_komposisi.dismiss();
            }
        });

        adapter_list_komposisi = new AdapterAddPakanPedagingListKomposisi(getApplicationContext(), Komposisi);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosenindexkomposisi = i;
                input_addpakanpedaging_activity_konsentrat.setText(Komposisi.get(choosenindexkomposisi).getId_komposisi());
                hideSoftKeyboard_event(AddPakanPedaging.this);
                dialog_list_komposisi.dismiss();
            }
        });
        list.setAdapter(adapter_list_komposisi);

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //InsTernakToKandangActivity.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter_list_komposisi.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        dialog_list_komposisi.show();
    }

    private String compareDates(){
        /*Calendar now = Calendar.getInstance();

        int hour = now.get(Calendar.HOUR);
        int minute = now.get(Calendar.MINUTE);
        int status = now.get(Calendar.AM_PM);

        date = parseDate(hour + ":" + minute + " " + status);*/
        calender = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        time = simpleDateFormat.format(calender.getTime());
        date = parseDate(time);
        Log.d("Time:",String.valueOf(time));
        dateCompareOne = parseDate(compareStringOne);
        dateCompareTwo = parseDate(compareStringTwo);

        if ( dateCompareOne.before( date ) && dateCompareTwo.after(date)) {
            return "Pagi";
        }else{
            return "Sore";
        }
    }

    private String getCurrentDate(){
        calender = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("d MMM yyyy hh:mm a");
        time = simpleDateFormat.format(calender.getTime());
        return time;
    }

    private Date parseDate(String date) {
        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

    private final TextWatcher CheckKonsentrat = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                if(listdata_addpakanpedaging_activity_temp.size()==0&&listdata_addpakanpedaging_activity_main.size()==0){
                    RefreshUI();
                }else{
                    SetList();
                    RefreshUI();
                }
                //Toast.makeText(getApplicationContext(),"Isi Huruf : "+ String.valueOf(s.length()),Toast.LENGTH_LONG).show();
            }else{
                InitiateUI();
            }
        }
    };

    private final TextWatcher CheckId = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (s.length() == 10) {
                SetData();

                input_addpakanpedaging_activity_pilihkonsentrat.setText("");
                //Toast.makeText(getApplicationContext(),"Isi Huruf : "+ String.valueOf(s.length()),Toast.LENGTH_LONG).show();
            }
        }
    };

    private void DelData(){

        for(int i=listdata_addpakanpedaging_activity_temp.size();i>0;i--){
            listdata_addpakanpedaging_activity_temp.remove(i-1);
            Log.d("Data ke ", String.valueOf(i-1) + String.valueOf(listdata_addpakanpedaging_activity_temp.size()));
            adapterAddPakanPedaging.notifyDataSetChanged();
        }
    }


    private void SetList(){
        listdata_addpakanpedaging_activity_temp.clear();
        Log.d("IsiList",String.valueOf(listdata_addpakanpedaging_activity_main.size()));
        for(int i=0;i<listdata_addpakanpedaging_activity_main.size();i++) {
            if(listdata_addpakanpedaging_activity_main.get(i).getJenis_konsentrat().trim().equalsIgnoreCase(input_addpakanpedaging_activity_konsentrat.getText().toString().trim())){
                listdata_addpakanpedaging_activity_temp.add(listdata_addpakanpedaging_activity_main.get(i));
            }
        }

        Log.d("IsiList",String.valueOf(listdata_addpakanpedaging_activity_temp.size()));
        adapterAddPakanPedaging = new AdapterAddPakanPedaging(AddPakanPedaging.this, R.layout.holder_list_addpakan_pedaging, listdata_addpakanpedaging_activity_temp);
        list_addpakanpedaging_activity.setAdapter(adapterAddPakanPedaging);
    }

    private void SetData(){
        ModelAddPakanPedaging mAddPakan = new ModelAddPakanPedaging();
        mAddPakan.setId_ternak(String.valueOf(input_addpakanpedaging_activity_pilihkonsentrat.getText()));
        mAddPakan.setSesi_makan(String.valueOf(compareDates()));
        mAddPakan.setTgl_makan(String.valueOf(getCurrentDate()));
        mAddPakan.setJenis_konsentrat(input_addpakanpedaging_activity_konsentrat.getText().toString().trim());
        listdata_addpakanpedaging_activity_temp.add(mAddPakan);
        listdata_addpakanpedaging_activity_main.add(mAddPakan);

        adapterAddPakanPedaging = new AdapterAddPakanPedaging(AddPakanPedaging.this, R.layout.holder_list_addpakan_pedaging, listdata_addpakanpedaging_activity_temp);
        list_addpakanpedaging_activity.setAdapter(adapterAddPakanPedaging);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void hideSoftKeyboard_event (Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
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

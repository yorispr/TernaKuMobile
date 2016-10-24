package com.fintech.ternaku.Main.NavBar.Ternak;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.UrlList;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InsertTernak extends AppCompatActivity {
    private TextView txtNama, txtBrt, txtTgl,txtRFID;
    private Button btnTambah,btnPerbaharui;
    float counter=200;
    private int flag_error_nama=0,flag_error_berat=0;
    private int year;
    private int month;
    private int day;
    private DatePicker dpResult;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private RadioGroup radioKelaminGroup;
    private RadioButton radioKelamin;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_ternak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Ternak Baru");
        }


        txtRFID = (TextView)findViewById(R.id.txtRFID);
        txtRFID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(txtRFID.length()==10){
                    txtRFID.setEnabled(false);
                }
            }
        });
        btnPerbaharui = (Button)findViewById(R.id.btnPerbaharui);
        btnPerbaharui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtRFID.setEnabled(true);
                txtRFID.setText("");
                txtRFID.setHint("Dekatkan Tag RFID");
            }
        });

        txtNama = (TextView)findViewById(R.id.nama_ternak);
        txtBrt = (TextView)findViewById(R.id.berat_ternak);
        txtTgl = (TextView)findViewById(R.id.txtTgl);

        dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.US);

        txtTgl.setText(dateFormatter.format(Calendar.getInstance().getTime()));

        txtNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showinputnama();
            }
        });

        txtBrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showinputberat();
            }
        });
        setDateTimeField();

        txtTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDatePickerDialog.show();
            }
        });

        radioKelaminGroup = (RadioGroup) findViewById(R.id.radioSex);
        btnTambah = (Button)findViewById(R.id.btnInsertTernak);
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check input box---
                if(checkform()){
                    insertDB();
                }else{
                    new SweetAlertDialog(InsertTernak.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Peringatan!")
                            .setContentText("Isikan Semua Data")
                            .show();
                }

            }
        });
    }

    private void insertDB(){

        int selectedId=radioKelaminGroup.getCheckedRadioButtonId();
        radioKelamin=(RadioButton) findViewById(selectedId);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Date testDate = null;
        try {
            testDate = sdf.parse(txtTgl.getText().toString());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final String newFormat = formatter.format(testDate);

        //Insert Database--
        new SweetAlertDialog(InsertTernak.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Simpan")
                .setContentText("Data Yang Dimasukkan Sudah Benar?")
                .setConfirmText("Ya")
                .setCancelText("Tidak")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        Log.d("TAG_INSERT",txtNama.getText().toString()+txtBrt.getText().toString()+
                                newFormat+radioKelamin.getText().toString());
                        String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null).trim()
                                +"&namaternak=" + txtNama.getText().toString()
                                +"&jeniskelamin=" + radioKelamin.getText().toString()
                                +"&tanggallahirternak=" + newFormat
                                +"&rfidcode=" + txtRFID.getText().toString();
                        new insertTernakTask().execute(url.getUrl_InsertTernak(), urlParameters);}
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                })
                .show();

    }


    private class insertTernakTask extends AsyncTask<String,Integer,String>{
        SweetAlertDialog pDialog = new SweetAlertDialog(InsertTernak.this, SweetAlertDialog.PROGRESS_TYPE);

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
            Log.d("RES_Insert",result);
            pDialog.dismiss();
            if (result.trim().equals("1")){
                new SweetAlertDialog(InsertTernak.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Berhasil!")
                        .setContentText("Data Berhasil Dimasukkan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                new SweetAlertDialog(InsertTernak.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Tambah Peternak")
                                        .setContentText("Apakah Ingin Menambah Data Peternak Lagi?")
                                        .setConfirmText("Ya")
                                        .setCancelText("Tidak")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                                txtRFID.setEnabled(true);
                                                txtRFID.setText("");
                                                txtRFID.setHint("Dekatkan Tag RFID");
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
                        })
                        .show();
            }
            else if(result.trim().equals("2")){

                new SweetAlertDialog(InsertTernak.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("Penambahan Gagal, Silahkan Simpan Data Kembali")
                        .show();
            }
            else {
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
            }
        }
    }


    protected void showinputnama() {

        // get prompts.xml view

        LayoutInflater layoutInflater = LayoutInflater.from(InsertTernak.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_input_name, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InsertTernak.this);
        alertDialogBuilder.setView(promptView);
        int a=0;
        final EditText editText = (EditText) promptView.findViewById(R.id.edtnamaternak);

        final InputMethodManager imgr = (InputMethodManager) InsertTernak.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        txtNama.setText(editText.getText());
                        InputMethodManager imm = (InputMethodManager) getSystemService(
                                INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    }
                })
                .setNegativeButton("Batal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //imgr.toggleSoftInput(0,InputMethodManager.HIDE_IMPLICIT_ONLY);
                                InputMethodManager imm = (InputMethodManager) getSystemService(
                                        INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                dialog.cancel();
                            }
                        });
        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    protected void showinputberat() {

        // get prompts.xml view

        LayoutInflater layoutInflater = LayoutInflater.from(InsertTernak.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_numberpicker, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InsertTernak.this);
        alertDialogBuilder.setView(promptView);

        final EditText txtBerat = (EditText) promptView.findViewById(R.id.txtBerat);
        final Button btnAdd = (Button) promptView.findViewById(R.id.btnAdd);
        final Button btnMin = (Button) promptView.findViewById(R.id.btnMin);
        txtBerat.setGravity(Gravity.CENTER);
        final DecimalFormat df = new DecimalFormat("#.#");

        txtBerat.setText(String.valueOf(df.format(counter)));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter+=1;
                txtBerat.setGravity(Gravity.CENTER);
                txtBerat.setText(String.valueOf(df.format(counter)));
            }
        });

        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter-=1;
                txtBerat.setGravity(Gravity.CENTER);

                txtBerat.setText(String.valueOf(df.format(counter)));
            }
        });

        final InputMethodManager imgr = (InputMethodManager) InsertTernak.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        txtBrt.setText(txtBerat.getText());
                        InputMethodManager imm = (InputMethodManager) getSystemService(
                                INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(txtBerat.getWindowToken(), 0);
                    }
                })
                .setNegativeButton("Batal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //imgr.toggleSoftInput(0,InputMethodManager.HIDE_IMPLICIT_ONLY);
                                InputMethodManager imm = (InputMethodManager) getSystemService(
                                        INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(txtBerat.getWindowToken(), 0);
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public boolean checkform(){
        boolean value = true;
        if(txtNama.getText().toString().equalsIgnoreCase("Isikan nama ternak disini")||
                txtNama.getText().toString().equalsIgnoreCase("")){
            value= false;
            txtNama.setError("Isikan Nama Ternak");
        }
        if(TextUtils.isEmpty(txtRFID.getText().toString())){
            value= false;
            txtRFID.setError("Isikan No RFID");
        }
        if(txtBrt.getText().toString().equalsIgnoreCase("Isikan berat ternak disini")){
            value= false;
            txtBrt.setError("Isikan Berat");
        }
        if(TextUtils.isEmpty(txtTgl.getText().toString())){
            value= false;
            txtTgl.setError("Isikan Tanggal");
        }

        return  value;
    }

    private void setDateTimeField() {
        //toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtTgl.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}

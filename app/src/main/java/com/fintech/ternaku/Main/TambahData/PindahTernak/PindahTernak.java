package com.fintech.ternaku.Main.TambahData.PindahTernak;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionButton;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PindahTernak extends AppCompatActivity {
    private ArrayAdapter<String> listAdapter;
    ListView itemList;
    FloatingActionButton fabTambah,fabHapus,fabEdit;
    Dialog dialog, dialog2;
    AdapterKandangPindahTernak adapter;
    AdapterKawananPindahTernak adapter2;

    ArrayAdapter<String> adp;
    boolean kandangclick = false;
    boolean kawananclick = false;
    ArrayList<String> namaList = new ArrayList<String>();
    List<ModelTernakPindahTernak> TernakList  = new ArrayList<ModelTernakPindahTernak>();
    ArrayList<ModelKandangPindahTernak> KandangList  = new ArrayList<ModelKandangPindahTernak>();
    ArrayList<ModelKawananPindahTernak> KawananList  = new ArrayList<ModelKawananPindahTernak>();

    private TextView input_pindahternak_activity_iddetail,input_pindahternak_activity_namadetail,
            input_pindahternak_activity_breed,input_pindahternak_activity_namakandang,
            input_pindahternak_activity_idkandang,input_pindahternak_activity_namakawanan,
            input_pindahternak_activity_idkawanan;
    private Button button_pindahternak_activity_hapus, button_pindahternak_activity_simpan;
    private EditText input_pindahternak_activity_kawanan,input_pindahternak_activity_kandang;
    LinearLayout linearLayout_pindahternak_activity_informsapi;
    int choosenindex=-1;
    int choosenindexkandang=-1;
    int choosenindexkawanan=-1;
    AutoCompleteTextView input_pindahternak_activity_idternak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pindah_ternak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                    | ActionBar.DISPLAY_SHOW_TITLE
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pindah Ternak");
        }

        //Floating Action Button-------------------------------------
        FloatingActionButton fab_kandang = (FloatingActionButton) findViewById(R.id.fabTambah_pindahternak_activity_kandang);
        fab_kandang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i = new Intent(PindahTernak.this,AddKandang.class);
                startActivity(i);
            }
        });
        FloatingActionButton fab_kawanan = (FloatingActionButton) findViewById(R.id.fabTambah_pindahternak_activity_kawanan);
        fab_kawanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i = new Intent(PindahTernak.this,AddKawanan.class);
                startActivity(i);
            }
        });

        input_pindahternak_activity_iddetail = (TextView)findViewById(R.id.input_pindahternak_activity_iddetail);
        input_pindahternak_activity_namadetail = (TextView)findViewById(R.id.input_pindahternak_activity_namadetail);
        input_pindahternak_activity_breed = (TextView)findViewById(R.id.input_pindahternak_activity_breed);
        input_pindahternak_activity_namakandang = (TextView)findViewById(R.id.input_pindahternak_activity_namakandang);
        input_pindahternak_activity_idkandang = (TextView)findViewById(R.id.input_pindahternak_activity_idkandang);
        input_pindahternak_activity_namakawanan = (TextView)findViewById(R.id.input_pindahternak_activity_namakawanan);
        input_pindahternak_activity_idkawanan = (TextView)findViewById(R.id.input_pindahternak_activity_idkawanan);

        input_pindahternak_activity_kandang = (EditText) findViewById(R.id.input_pindahternak_activity_kandang);
        input_pindahternak_activity_kawanan = (EditText) findViewById(R.id.input_pindahternak_activity_kawanan);

        input_pindahternak_activity_kawanan.setEnabled(false);
        input_pindahternak_activity_kandang.setEnabled(false);

        input_pindahternak_activity_kawanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
                    new GetKawanan().execute("http://ternaku.com/index.php/C_Ternak/GetKawananByIdPeternakan", urlParameters);
                    kawananclick = true;

            }
        });

        input_pindahternak_activity_kandang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
                    new GetKandang().execute("http://ternaku.com/index.php/C_Ternak/GetKandangByIdPeternakan", urlParameters);

            }
        });
        linearLayout_pindahternak_activity_informsapi = (LinearLayout) findViewById(R.id.linearLayout_pindahternak_activity_informsapi);
        linearLayout_pindahternak_activity_informsapi.setVisibility(View.GONE);




        button_pindahternak_activity_hapus = (Button)findViewById(R.id.button_pindahternak_activity_hapus);
        button_pindahternak_activity_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout_pindahternak_activity_informsapi.setVisibility(View.GONE);
                input_pindahternak_activity_kandang.setText("");
                input_pindahternak_activity_kawanan.setText("");
                choosenindex = -1;
            }
        });

        button_pindahternak_activity_simpan = (Button)findViewById(R.id.button_pindahternak_activity_simpan);
        button_pindahternak_activity_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idternak = TernakList.get(choosenindex).getId_ternak();
                String idpeternakan = KawananList.get(choosenindexkawanan).getId_peternakan();
                String idkawanan= input_pindahternak_activity_namakawanan.getText().toString();
                String idkandang= input_pindahternak_activity_namakandang.getText().toString();

                if(choosenindexkawanan != -1) {
                    idkawanan = KawananList.get(choosenindexkawanan).getId_kawanan();
                }else if (choosenindexkawanan == -1){
                    idkawanan = TernakList.get(choosenindex).getId_kawanan();
                }
                if(choosenindexkandang != -1) {
                    idkandang = KandangList.get(choosenindexkandang).getId_kandang();
                }else if(choosenindexkandang == -1){
                    idkandang = TernakList.get(choosenindex).getId_kandang();
                }
                String status = "Aktif";
                String urlParameters = "idternak="+idternak
                        +"&idpeternakan="+idpeternakan
                        +"&idkawanan="+idkawanan
                        +"&idkandang="+idkandang
                        +"&statusaktif="+status;
                new InsertPengelompokkanTask().execute("http://ternaku.com/index.php/C_Ternak/insertinsertHistoryPengelompokan", urlParameters);

                Log.d("param",urlParameters);
            }
        });

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.colorPrimary));


        input_pindahternak_activity_idternak=(AutoCompleteTextView) findViewById(R.id.input_pindahternak_activity_idternak);

        input_pindahternak_activity_idternak.setEnabled(false);
        adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,namaList);

        input_pindahternak_activity_idternak.setAdapter(adp);


        input_pindahternak_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(PindahTernak.this,namaList.get(i),Toast.LENGTH_LONG).show();
                String idter = namaList.get(i).trim();
                choosenindex = i;
                setDetailToTextView(idter);
                hideSoftKeyboard(PindahTernak.this);
            }
        });

        String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetAllTernak().execute("http://ternaku.com/index.php/C_Ternak/GetTernakForPengelompokkan", urlParameters);
    }

    private void setDetailToTextView(String idTernak) {
        for(ModelTernakPindahTernak t : TernakList){
            if(t.getId_ternak().contains(idTernak))
            {

                linearLayout_pindahternak_activity_informsapi.setVisibility(View.VISIBLE);
                input_pindahternak_activity_iddetail.setText(t.getId_ternak());
                input_pindahternak_activity_namadetail.setText(t.getNama_ternak());
                input_pindahternak_activity_breed.setText(t.getBreed());
                input_pindahternak_activity_namakawanan.setText(t.getKawanan());
                input_pindahternak_activity_namakandang.setText(t.getKandang());
                input_pindahternak_activity_idkawanan.setText(t.getId_kawanan());
                input_pindahternak_activity_idkandang.setText(t.getId_kandang());

                input_pindahternak_activity_kawanan.setText(t.getKawanan());
                input_pindahternak_activity_kandang.setText(t.getKandang());
                input_pindahternak_activity_kandang.setEnabled(true);
                input_pindahternak_activity_kawanan.setEnabled(true);
            }
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private class GetAllTernak extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(PindahTernak.this);
            progDialog.setMessage("Tunggu Sebentar...");
            //progDialog.show();
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
            if (result.trim().equals("404")){
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
            }
            else {
                AddTernakToList(result);
                input_pindahternak_activity_idternak.setEnabled(true);
            }
        }
    }

    private class GetKawanan extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(PindahTernak.this);
            progDialog.setMessage("Sedang mengambil data...");
            progDialog.show();
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
            if (result.trim().equals("404")){
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
                progDialog.dismiss();
            }
            else {
                AddKawananToList(result);
                progDialog.dismiss();
            }
        }
    }

    private class GetKandang extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(PindahTernak.this);
            progDialog.setMessage("Sedang mengambil data...");
            progDialog.show();
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
            if (result.trim().equals("404")){
                Toast.makeText(getApplication(),"Terjadi kesalahan",Toast.LENGTH_LONG).show();
                progDialog.dismiss();
            }
            else {
                AddKandangToList(result);
                progDialog.dismiss();
            }
        }
    }

    private class InsertPengelompokkanTask extends AsyncTask<String,Integer,String> {
        ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(PindahTernak.this);
            progDialog.setMessage("Tunggu Sebentar...");
            //progDialog.show();
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
            if (result.trim().equals("1")){
                Toast.makeText(getApplication(),"Berhasil memindahkan ternak",Toast.LENGTH_LONG).show();
                input_pindahternak_activity_namakandang.setText(input_pindahternak_activity_kandang.getText().toString());
                input_pindahternak_activity_namakawanan.setText(input_pindahternak_activity_kawanan.getText().toString());
                //input_pindahternak_activity_idkandang.setText(KandangList.get(choosenindexkandang).getId_kandang());
                //input_pindahternak_activity_idkawanan.setText(KawananList.get(choosenindexkawanan).getId_kawanan());

                TernakList.get(choosenindex).setId_kandang(KandangList.get(choosenindexkandang).getId_kandang());
                TernakList.get(choosenindex).setId_kawanan(KawananList.get(choosenindexkawanan).getId_kawanan());
                TernakList.get(choosenindex).setKandang(KandangList.get(choosenindexkandang).getNama_kandang());
                TernakList.get(choosenindex).setKawanan(KawananList.get(choosenindexkawanan).getNama_kawanan());

                setDetailToTextView(input_pindahternak_activity_idternak.getText().toString());
            }
            else {
                Toast.makeText(getApplication(),"Gagal memindahkan ternak",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void AddKandangToList(String result) {
        KandangList.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);

                if(jObj.getString("STATUS_AKTIF").trim().equalsIgnoreCase("Aktif")) {
                    ModelKandangPindahTernak k = new ModelKandangPindahTernak();
                    k.setId_kandang(jObj.getString("ID_KANDANG"));
                    k.setId_peternakan(jObj.getString("ID_PETERNAKAN"));
                    k.setKapasitas_kandang(jObj.getInt("KAPASITAS"));
                    k.setLokasi_kandang(jObj.getString("LOKASI"));
                    k.setNama_kandang(jObj.getString("NAMA_KANDANG"));
                    k.setStatus_aktif(jObj.getString("STATUS_AKTIF"));
                    KandangList.add(k);
                }
            }
            ShowDialogKandang(KandangList);
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private void AddKawananToList(String result) {
        KawananList.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);

                ModelKawananPindahTernak k = new ModelKawananPindahTernak();
                k.setId_kawanan(jObj.getString("ID_KAWANAN"));
                k.setId_peternakan(jObj.getString("ID_PETERNAKAN"));
                k.setNama_kawanan(jObj.getString("NAMA_KAWANAN"));
                k.setUmur(jObj.getString("UMUR"));
                k.setKeterangan(jObj.getString("KETERANGAN"));

                KawananList.add(k);
            }

            ShowDialogKawanan(KawananList);
        }

        catch (JSONException e){e.printStackTrace();}
    }

    private void AddTernakToList(String result) {
        TernakList.clear();
        namaList.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                ModelTernakPindahTernak t = new ModelTernakPindahTernak();

                t.setId_ternak(jObj.getString("id_ternak"));
                t.setNama_ternak(jObj.getString("nama_ternak"));
                t.setBreed(jObj.getString("breed"));
                t.setKandang(jObj.getString("nama_kandang"));
                t.setKawanan(jObj.getString("nama_kawanan"));
                t.setId_kandang(jObj.getString("id_kandang"));
                t.setId_kawanan(jObj.getString("id_kawanan"));

                namaList.add(jObj.getString("id_ternak"));
                //Log.d("RESP",jObj.getString("NAMA_KAWANAN"));

                TernakList.add(t);
            }

        }
        catch (JSONException e){e.printStackTrace();}
    }

    private void ShowDialogKawanan(final ArrayList<ModelKawananPindahTernak> kawanan) {
        dialog = new Dialog(PindahTernak.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.list_kawanan_pindah_ternak);
        ListView list = (ListView) dialog.findViewById(R.id.list_kawanan_dial);
        EditText edtsearch = (EditText)dialog.findViewById(R.id.edtSearchDialogKawanan);

        Button btnbtl = (Button)dialog.findViewById(R.id.btnbtl);

        btnbtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        adapter2 = new AdapterKawananPindahTernak(getApplicationContext(),kawanan);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosenindexkawanan = i;
                input_pindahternak_activity_kawanan.setText(KawananList.get(choosenindexkawanan).getNama_kawanan());
                Log.d("kawanan",kawanan.get(choosenindexkawanan).getNama_kawanan()+" index: "+String.valueOf(choosenindexkawanan));
                hideSoftKeyboard(PindahTernak.this);
                dialog.dismiss();
            }
        });
        list.setAdapter(adapter2);

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter2.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        dialog.show();
    }

    private void ShowDialogKandang(final ArrayList<ModelKandangPindahTernak> kandang) {
        dialog2 = new Dialog(PindahTernak.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.list_kandang_pindah_ternak);

        ListView list = (ListView) dialog2.findViewById(R.id.list_kandang_dial);
        EditText edtsearch = (EditText)dialog2.findViewById(R.id.edtSearchDialogKandang);
        Button btnbtl = (Button)dialog2.findViewById(R.id.btnbtl);

        btnbtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });

        adapter = new AdapterKandangPindahTernak(getApplicationContext(),kandang);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                choosenindexkandang = i;
                input_pindahternak_activity_kandang.setText(kandang.get(choosenindexkandang).getNama_kandang());
                hideSoftKeyboard(PindahTernak.this);
                dialog2.dismiss();
            }
        });

        list.setAdapter(adapter);

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //InsTernakToKandangActivity.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        dialog2.show();
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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    /*
    @Override
    protected void onPause(){
        super.onPause();
        choosenindex = -1;
        choosenindexkandang = -1;
        choosenindexkawanan = -1;
    }*/
}

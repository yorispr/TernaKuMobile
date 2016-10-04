package com.fintech.ternaku.Main.TambahData.PindahTernak;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    ArrayList<String> namaList = new ArrayList<String>();
    List<ModelTernakPindahTernak> TernakList  = new ArrayList<ModelTernakPindahTernak>();
    ArrayList<ModelKandangPindahTernak> KandangList  = new ArrayList<ModelKandangPindahTernak>();
    ArrayList<ModelKawananPindahTernak> KawananList  = new ArrayList<ModelKawananPindahTernak>();

    private TextView txtID,txtNama,txtStatFisik,txtKesuburan,txtJnsSapi,txtKehamilan, txtKawanan, txtKandang;
    private Button button_pindahternak_activity_hapus, button_pindahternak_activity_simpan;
    private EditText input_pindahternak_activity_kawanan,input_pindahternak_activity_kandang;
    RelativeLayout relative_layout_pindahternak_activity_informsapi;
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
        }

        txtID = (TextView)findViewById(R.id.txtInsID);
        txtNama = (TextView)findViewById(R.id.txtInsNama);
        txtStatFisik = (TextView)findViewById(R.id.txtInsStatFisik);
        txtKesuburan = (TextView)findViewById(R.id.txtInsKesuburan);
        txtJnsSapi = (TextView)findViewById(R.id.txtInsJnsSapi);
        txtKehamilan = (TextView)findViewById(R.id.txtInsKehamilan);
        txtKandang = (TextView)findViewById(R.id.txtInsKandang);
        txtKawanan = (TextView)findViewById(R.id.txtInsKawanan);

        input_pindahternak_activity_kandang = (EditText) findViewById(R.id.input_pindahternak_activity_kandang);
        input_pindahternak_activity_kawanan = (EditText) findViewById(R.id.input_pindahternak_activity_kawanan);

        input_pindahternak_activity_kawanan.setEnabled(false);
        input_pindahternak_activity_kandang.setEnabled(false);

        input_pindahternak_activity_kawanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
                new GetKawanan().execute("http://ternaku.com/index.php/C_Ternak/GetKawananByIdPeternakan", urlParameters);
            }
        });

        input_pindahternak_activity_kandang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
                new GetKandang().execute("http://ternaku.com/index.php/C_Ternak/GetKandangByIdPeternakan", urlParameters);
            }
        });
        relative_layout_pindahternak_activity_informsapi = (RelativeLayout)findViewById(R.id.relative_layout_pindahternak_activity_informsapi);
        relative_layout_pindahternak_activity_informsapi.setVisibility(View.GONE);




        button_pindahternak_activity_hapus = (Button)findViewById(R.id.button_pindahternak_activity_hapus);
        button_pindahternak_activity_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_layout_pindahternak_activity_informsapi.setVisibility(View.GONE);
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
                String idkawanan;
                if(choosenindexkawanan == -1) {
                    idkawanan = KawananList.get(choosenindexkawanan).getId_kawanan();
                }else{
                    idkawanan = TernakList.get(choosenindex).getId_kawanan();
                }
                String idkandang;
                if(choosenindexkandang == -1) {
                    idkandang = KandangList.get(choosenindexkandang).getId_kandang();
                }else{
                    idkandang = TernakList.get(choosenindex).getId_kandang();
                }
                String status = "Aktif";
                String urlParameters = "idternak="+idternak
                        +"&idpeternakan="+idpeternakan
                        +"&idkawanan="+idkawanan
                        +"&idkandang="+idkandang
                        +"&statusaktif="+status;
                new InsertPengelompokkanTask().execute("http://ternaku.com/index.php/C_Ternak/insertinsertHistoryPengelompokan", urlParameters);
            }
        });

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.colorPrimary));


        input_pindahternak_activity_idternak=(AutoCompleteTextView) findViewById(R.id.input_pindahternak_activity_idternak);

        input_pindahternak_activity_idternak.setEnabled(false);
        ArrayAdapter<String> adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,namaList);

        input_pindahternak_activity_idternak.setAdapter(adp);


        input_pindahternak_activity_idternak.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(PindahTernak.this,namaList.get(i),Toast.LENGTH_LONG).show();
                String idter = namaList.get(i).trim();
                choosenindex = i;
                setDetailToTextView(idter);
            }
        });

        String urlParameters = "uid=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetAllTernak().execute("http://ternaku.com/index.php/C_Ternak/GetTernakForPengelompokkan", urlParameters);
    }

    private void setDetailToTextView(String idTernak) {
        for(ModelTernakPindahTernak t : TernakList){
            if(t.getId_ternak().contains(idTernak))
            {
                relative_layout_pindahternak_activity_informsapi.setVisibility(View.VISIBLE);
                txtID.setText(t.getId_ternak());
                txtNama.setText(t.getNama_ternak());
                txtStatFisik.setText(t.getAktivitas());
                txtKesuburan.setText(t.getStatus_kesuburan());
                txtJnsSapi.setText(t.getBreed());
                txtKehamilan.setText(t.getDiagnosis());
                txtKawanan.setText(t.getKawanan());
                txtKandang.setText(t.getKandang());
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
                AddKawananToList(result);
            }
        }
    }

    private class GetKandang extends AsyncTask<String,Integer,String> {
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
                AddKandangToList(result);
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
                t.setAktivitas(jObj.getString("aktivitas"));
                t.setTgl_subur(jObj.getString("tgl_perkiraan_subur"));
                t.setStatus_kesuburan(jObj.getString("NAMA_REF_KESUBURAN"));
                t.setDiagnosis(jObj.getString("diagnosis"));
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
                dialog.dismiss();
            }
        });
        list.setAdapter(adapter2);

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                PindahTernak.this.adapter2.getFilter().filter(charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        dialog.show();
    }

    private void ShowDialogKandang(final ArrayList<ModelKandangPindahTernak> kandang) {
        dialog2 = new Dialog(PindahTernak.this);
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

}

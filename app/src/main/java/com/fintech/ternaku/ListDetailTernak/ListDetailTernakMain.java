package com.fintech.ternaku.ListDetailTernak;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.ListDetailTernak.AdapterDetailTernakListDetailTernak;
import com.fintech.ternaku.ListDetailTernak.ModelDetailTernalListDetailTernak;
import com.fintech.ternaku.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListDetailTernakMain extends AppCompatActivity {
    AdapterDetailTernakListDetailTernak adapter;
    List<ModelDetailTernalListDetailTernak> ternakList = new ArrayList<ModelDetailTernalListDetailTernak>();
    ListView list;
    private String id_peternakan="FNT-P1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail_ternak_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                    | ActionBar.DISPLAY_SHOW_TITLE
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        list = (ListView)findViewById(R.id.list_listdetailternak_activity);
        String urlParameters = "idpeternakan="+ id_peternakan.trim();
        new getDataCekHariIni().execute("http://ternaku.com/index.php/C_Ternak/GetPeriksaHariIni",urlParameters);

    }

    private class getDataCekHariIni extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0], params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.trim().equals("404"))
            {
                Toast.makeText(getApplicationContext(),"Terjadi Kesalahan...",Toast.LENGTH_LONG).show();
            }else{
                showTernak(s);
            }
        }
    }

    private void showTernak(String result){
        Log.d("Ternak2",result);

        try {
            JSONArray jArray = new JSONArray(result);
            Log.d("COUNT", String.valueOf(jArray.length()));
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObj = jArray.getJSONObject(i);
                ModelDetailTernalListDetailTernak ter = new ModelDetailTernalListDetailTernak();
                ter.setId_ternak(jObj.getString("id_ternak"));
                ter.setNama_ternak(jObj.getString("nama_ternak"));
                ter.setBody_condition_score(jObj.getString("body_condition_score"));
                ter.setId_peternakan(jObj.getString("id_peternakan"));
                ter.setKesuburan(jObj.getString("nama_ref_kesuburan"));
                ter.setTgl_terakhir_check(jObj.getString("tglterakhirperiksa"));
                ternakList.add(ter);

                adapter.notifyDataSetChanged();
            }
            adapter = new AdapterDetailTernakListDetailTernak(ListDetailTernakMain.this, R.layout.layout_listdetailternak_list, ternakList);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getApplicationContext(),"Click!!" + i,Toast.LENGTH_LONG).show();
                    /*String id_ternak = ternakList.get(i).getId_ternak();
                    Bundle b = new Bundle();
                    b.putString("id_ternak",id_ternak);
                    Intent intent = new Intent(TernakListActivity.this,DetailTernakActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);*/
                }
            });
        } catch (JSONException e){e.printStackTrace();}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

package com.fintech.ternaku.TernakPotong.ADG;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.TernakPerah.Main.MainActivity;
import com.fintech.ternaku.UrlList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.wangyuwei.loadingview.LoadingView;

public class ViewADG extends AppCompatActivity {
    private ListView list_adg_activity;
    List<ModelAdg> list_adg_data = new ArrayList<ModelAdg>();
    AdapterListAdg adapterAdg;

    //Loading------------------------------------------------------------
    private LoadingView loading_view_adg;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_adg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                    | ActionBar.DISPLAY_SHOW_TITLE
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("Data ADG");
        }
        hideSoftKeyboard();


        list_adg_activity = (ListView) findViewById(R.id.list_adg_activity);
        loading_view_adg = (LoadingView)findViewById(R.id.loading_view_adg);

        //Initiate-----------------------------------------------------------
        InitiateFragment();

        //Get Data Task Ternak---------------------------------------------------
        String urlParameters_adg = "idpeternakan=" + getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan",null);
        new getDataADG().execute(url.getUrl_GetAdg(), urlParameters_adg);

    }

    //Initiate Fragment----------------------------------------------------
    private void InitiateFragment(){
        loading_view_adg.setVisibility(View.VISIBLE);
        list_adg_activity.setVisibility(View.GONE);
    }
    private void RefreshFragment(){
        loading_view_adg.setVisibility(View.GONE);
        list_adg_activity.setVisibility(View.VISIBLE);
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

    //Get Data Task-------------------------------------------------------
    private class getDataADG extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPreExecute() {
            loading_view_adg.start();
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0], params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String res) {
            Log.d("Adg_Pedaging_Json",res);
            loading_view_adg.stop();
            SetTernakTask(res);
            RefreshFragment();
        }
    }

    private void SetTernakTask(String result){
        list_adg_data.clear();
        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                ModelAdg mAdg = new ModelAdg();
                mAdg.setId_ternak(jObj.getString("id_ternak"));
                mAdg.setValue_adg(jObj.getString("ADG"));
                mAdg.setTgl_timbang_terakhir(jObj.getString("tgl_timbang_terakhir"));
                mAdg.setTgl_timbang_sebelum(jObj.getString("tgl_timbang_sebelumnya"));
                mAdg.setBerat_terakhir(jObj.getString("bb_terakhir"));
                mAdg.setBerat_sebelum(jObj.getString("bb_sebelumnya"));

                list_adg_data.add(mAdg);
            }
        }
        catch (JSONException e){e.printStackTrace();}
        adapterAdg = new AdapterListAdg(ViewADG.this, R.layout.holder_list_adg, list_adg_data);
        list_adg_activity.setAdapter(adapterAdg);
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

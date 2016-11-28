package com.fintech.ternaku.TernakPerah.Main.Laporan.ProduksiSusu;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class LaporanProduksiSusuGrafik extends AppCompatActivity {
    private ColumnChartView chart_laporanproduksisusu_activity_tahun;
    private ColumnChartData data_laporanproduksisusu_activity_tahun;


    public final static String[] months = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_produksi_susu_grafik);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Grafik Laporan Produksi Susu");
        }

        //Set Colomn Tahun-----------------------------------------------
        Log.d("Taguserid",getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null));
        chart_laporanproduksisusu_activity_tahun = (ColumnChartView) findViewById(R.id.chart_laporanproduksisusu_activity_tahun);
        String urlParameter_Get_Tahun = "uid="+getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetDataPerTahun().execute("http://ternaku.com/index.php/C_Laporan/TotalProduksiSusuPeternakan_TAHUN_TERTENTU",urlParameter_Get_Tahun);

    }

    //Get Data Per Tahun-----------------------------------
    private class GetDataPerTahun extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0],params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("Get Data Per Tahun",s);
            generateColumnData(s);
        }
    }


    //Get Coloum Tahun-------------------------------------
    private void generateColumnData(String result) {

        int numSubcolumns = 1;
        //int numColumns = 12;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue(Float.parseFloat(jObj.getString("Jumlah Produksi")), ChartUtils.pickColor()));
                }
                axisValues.add(new AxisValue(i).setLabel(jObj.getString("Masa Produksi").substring(0,3)));
                columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        data_laporanproduksisusu_activity_tahun = new ColumnChartData(columns);
        data_laporanproduksisusu_activity_tahun.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        data_laporanproduksisusu_activity_tahun.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));

        chart_laporanproduksisusu_activity_tahun.setColumnChartData(data_laporanproduksisusu_activity_tahun);



        // Set value touch listener that will trigger changes for chartTop.
        //chart_laporanpakan_activity_tahun.setOnValueTouchListener(new ValueTouchListener());

        // Set selection mode to keep selected month column highlighted.
        chart_laporanproduksisusu_activity_tahun.setValueSelectionEnabled(true);
        chart_laporanproduksisusu_activity_tahun.setZoomType(ZoomType.HORIZONTAL);

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

package com.fintech.ternaku.TernakPerah.Main.Laporan.Keuangan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;
import com.fintech.ternaku.UrlList;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LaporanKeuanganGrafik extends AppCompatActivity implements
        OnChartValueSelectedListener {
    private LineChart mChart;
    ProgressDialog progDialog;
    private int Jumlah_Bulan, Jumlah_Uang;
    String bln,thn,bln_angka,jenis_transaksi;
    String jsonMasuk, jsonKeluar;
    ArrayList<Float> uangListMasuk = new ArrayList<Float>();
    ArrayList<Float> uangListKeluar = new ArrayList<Float>();

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_keuangan_grafik);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("");
        }

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);

        //Get Bundle-------------------------------------------------------
        bln = getIntent().getExtras().getString("bln");
        thn = getIntent().getExtras().getString("thn");
        bln_angka = getIntent().getExtras().getString("bln_angka");
        jenis_transaksi = getIntent().getExtras().getString("jenis_transaksi");
        getSupportActionBar().setTitle("Grafik " + jenis_transaksi);

        //Get Data From Web Service------------------------------------------------
        String param = "uid="+getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null)
                +"&bulan=" + bln.trim()
                +"&tahun=" + thn.trim()
                +"&jenistransaksi=" + jenis_transaksi.trim();
        new GetDataKeuanganMasuk().execute(url.getUrlGetLaporanKeuanganGrafik_Masuk(),param);
        Log.d("Url",param);

        mChart.animateX(3500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(LegendForm.LINE);
        l.setTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        l.setTextSize(11f);
        l.setTextColor(Color.LTGRAY);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart,Integer.parseInt(bln_angka));
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(xAxisFormatter);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        mChart.getAxisRight().setEnabled(false);

    }

    //Get Data From Web Service---------------------------------------------------
    private class GetDataKeuanganMasuk extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(LaporanKeuanganGrafik.this);
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
            Log.d("RESGrafikKeuangan",result);
            jsonMasuk = result;
            String param = "uid="+getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null)
                    +"&bulan=" + bln.trim()
                    +"&tahun=" + thn.trim()
                    +"&jenistransaksi=" + jenis_transaksi.trim();
            new GetDataKeuanganKeluar().execute(url.getUrlGetLaporanKeuanganGrafik_Keluar(),param);
        }
    }
    private class GetDataKeuanganKeluar extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0],params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("RESGrafikKeuangan",result);
            jsonKeluar = result;
            SetList(jsonMasuk,jsonKeluar);
            progDialog.dismiss();

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void SetList(String JSONMasuk,String JSONKeluar){
        int total_data=0;

        try{
            JSONArray jArray = new JSONArray(JSONMasuk);
            for(int k=0;k<jArray.length();k++)
            {
                JSONObject jObj = jArray.getJSONObject(k);
                uangListMasuk.add((float) jObj.getDouble("JumlahUang"));
            }

            JSONArray jArray2 = new JSONArray(JSONKeluar);
            for(int k=0;k<jArray2.length();k++)
            {
                JSONObject jObj2 = jArray2.getJSONObject(k);
                uangListKeluar.add((float)jObj2.getDouble("JumlahUang"));
            }
            total_data = jArray.length();

            setData(total_data);

        }catch (JSONException e){e.printStackTrace();}
    }

    private void setData(int count) {
        float val = 0;
        LineDataSet set_uang_masuk, set_uang_keluar;


        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count+1; i++) {
            if(i!=0){
                val = uangListMasuk.get(i-1);
            }
            yVals1.add(new Entry(i, val));
        }

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        for (int i = 0; i < count+1; i++) {
            if(i!=0){
                val = uangListKeluar.get(i-1);
            }
            yVals2.add(new Entry(i, val));
        }

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set_uang_masuk = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set_uang_keluar = (LineDataSet) mChart.getData().getDataSetByIndex(1);
            set_uang_masuk.setValues(yVals1);
            set_uang_keluar.setValues(yVals2);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set_uang_masuk = new LineDataSet(yVals1, "Uang Masuk (Rupiah)");
            set_uang_masuk.setAxisDependency(AxisDependency.LEFT);
            set_uang_masuk.setColor(ColorTemplate.getHoloBlue());
            set_uang_masuk.setCircleColor(Color.BLACK);
            set_uang_masuk.setLineWidth(2f);
            set_uang_masuk.setCircleRadius(2f);
            set_uang_masuk.setFillAlpha(65);
            set_uang_masuk.setFillColor(ColorTemplate.getHoloBlue());
            set_uang_masuk.setHighLightColor(Color.rgb(244, 117, 117));
            set_uang_masuk.setDrawCircleHole(false);

            // create a dataset and give it a type
            set_uang_keluar = new LineDataSet(yVals2, "Uang Keluar (Rupiah)");
            set_uang_keluar.setAxisDependency(AxisDependency.LEFT);
            set_uang_keluar.setColor(Color.RED);
            set_uang_keluar.setCircleColor(Color.BLACK);
            set_uang_keluar.setLineWidth(2f);
            set_uang_keluar.setCircleRadius(2f);
            set_uang_keluar.setFillAlpha(65);
            set_uang_keluar.setFillColor(Color.RED);
            set_uang_keluar.setDrawCircleHole(false);
            set_uang_keluar.setHighLightColor(Color.rgb(244, 117, 117));

            // create a data object with the datasets
            LineData data = new LineData(set_uang_masuk, set_uang_keluar);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(9f);

            // set data
            mChart.setData(data);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());

        mChart.centerViewToAnimated(e.getX(), e.getY(), mChart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency(), 800);
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
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
package com.fintech.ternaku.TernakPerah.Main.Laporan.Keuangan;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class LaporanKeuangan extends AppCompatActivity {
    ProgressDialog progDialog;

    private LineChartView chart_laporankeuangan_activity;
    private LineChartData data_laporankeuangan_activity;

    private int numberOfLines = 2 ;
    private int maxNumberOfLines = 2;
    private int numberOfPoints = 0;
    ArrayList<Float> uangListMasuk = new ArrayList<Float>();
    ArrayList<Float> uangListKeluar = new ArrayList<Float>();
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;

    float totaluangmasuk=0, totaluangkeluar=0;
    String jsonMasuk, jsonKeluar;
    String bln,thn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_keuangan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Grafik Laporan Keuangan");
        }

        chart_laporankeuangan_activity = (LineChartView)findViewById(R.id.chart_laporankeuangan_activity);
        chart_laporankeuangan_activity.setOnValueTouchListener(new ValueTouchListener());

        //Get Bundle-------------------------------------------------------
        bln = getIntent().getExtras().getString("bln");
        thn = getIntent().getExtras().getString("thn");


        // Disable viewport recalculations, see toggleCubic() method for more info.
        chart_laporankeuangan_activity.setViewportCalculationEnabled(false);

        resetViewport();
        String param = "uid="+getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null)
                +"&bulan=" + bln.trim()
                +"&tahun=" + thn.trim();
        new GetDataKeuanganMasuk().execute("http://ternaku.com/index.php/C_Laporan/UangMasuk_PETERNAKAN_BULAN_TERTENTU",param);
        Log.d("Url",param);



    }
    private class GetDataKeuanganMasuk extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPreExecute() {
            progDialog = new ProgressDialog(LaporanKeuangan.this);
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
                    +"&tahun=" + thn.trim();
            new GetDataKeuanganKeluar().execute("http://ternaku.com/index.php/C_Laporan/UangKeluar_PETERNAKAN_BULAN_TERTENTU",param);
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
            addDataToList(jsonMasuk,jsonKeluar);
            progDialog.dismiss();
        }
    }

    private void addDataToList(String resultMasuk, String resultKeluar) {

        try{
            JSONArray jArray = new JSONArray(resultMasuk);
            for(int k=0;k<jArray.length();k++)
            {
                JSONObject jObj = jArray.getJSONObject(k);
                uangListMasuk.add((float)jObj.getDouble("JumlahUang"));
                totaluangmasuk+=(float)jObj.getDouble("JumlahUang");
            }

            JSONArray jArray2 = new JSONArray(resultKeluar);
            for(int k=0;k<jArray2.length();k++)
            {
                JSONObject jObj2 = jArray2.getJSONObject(k);
                uangListKeluar.add((float)jObj2.getDouble("JumlahUang"));
                totaluangkeluar+=(float)jObj2.getDouble("JumlahUang");
            }
            numberOfPoints = jArray.length();
            float[][] dataUangMasuk = new float[maxNumberOfLines][numberOfPoints];
            float[][] dataUangKeluar = new float[maxNumberOfLines][numberOfPoints];

            generateValues(dataUangMasuk,dataUangKeluar);
            generateData(dataUangMasuk,dataUangKeluar);

        }catch (JSONException e){e.printStackTrace();}

    }

    private void generateData(float[][] dataUangMasuk,float[][] dataUangKeluar) {
        List<Line> linesMasuk = new ArrayList<Line>();


        for (int i = 0; i < numberOfLines; ++i) {
            //===============START UANG MASUK======================
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                if(i==0) {
                    values.add(new PointValue(j, dataUangMasuk[i][j]));
                }else{
                    values.add(new PointValue(j, dataUangKeluar[i][j]));
                }
            }
            Line line = new Line(values);
            if(i==0){
                line.setColor(Color.parseColor("#2ecc71"));
            }else
            {
                line.setColor(Color.parseColor("#e74c3c"));
            }
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            if (pointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);

            }
            linesMasuk.add(line);
        }


        data_laporankeuangan_activity = new LineChartData(linesMasuk);

        //data = new LineChartData(linesKeluar);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Tanggal");
                axisY.setName("Rupiah");
            }
            data_laporankeuangan_activity.setAxisXBottom(axisX);
            data_laporankeuangan_activity.setAxisYLeft(axisY);
        } else {
            data_laporankeuangan_activity.setAxisXBottom(null);
            data_laporankeuangan_activity.setAxisYLeft(null);
        }

        data_laporankeuangan_activity.setBaseValue(Float.NEGATIVE_INFINITY);

        chart_laporankeuangan_activity.setLineChartData(data_laporankeuangan_activity);
    }

    private void generateValues(float[][] dataUangMasuk,float[][] dataUangKeluar) {

        final Viewport v = new Viewport(chart_laporankeuangan_activity.getMaximumViewport());
        v.bottom = 0;

        float maxkeluar = Collections.max(uangListKeluar);
        float maxmasuk = Collections.max(uangListMasuk);
        if (maxkeluar >= maxmasuk){
            v.top = maxkeluar /1000000f ;
        }
        else{
            v.top = maxmasuk /1000000f;
        }

        v.left = 1;
        v.right = numberOfPoints+1 ;
        chart_laporankeuangan_activity.setMaximumViewport(v);
        chart_laporankeuangan_activity.setCurrentViewport(v);

        for (int i = 0; i < numberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                // randomNumbersTab[i][j] = (float) Math.random() * 100f;
                try {
                    dataUangMasuk[i][j] = (float) uangListMasuk.get(j)/1000000f ;
                    dataUangKeluar[i][j] = (float) uangListKeluar.get(j)/1000000f ;
                }catch (IndexOutOfBoundsException e){e.printStackTrace();}
            }
        }
    }


    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart_laporankeuangan_activity.getMaximumViewport());
        v.bottom = 0;
        v.top = 10000;
        v.left = 0;
        v.right = numberOfPoints - 1;
        chart_laporankeuangan_activity.setMaximumViewport(v);
        chart_laporankeuangan_activity.setCurrentViewport(v);
    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(LaporanKeuangan.this, "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

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

}

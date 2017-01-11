package com.fintech.ternaku.TernakPerah.Main.Laporan.PenggunaanPakan;

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
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class LaporanPenggunaanPakan extends AppCompatActivity {
    private LineChartView chart_laporanpakan_activity_bulan;
    private ColumnChartView chart_laporanpakan_activity_tahun;
    private LineChartData data_laporanpakan_activity_bulan;
    private ColumnChartData data_laporanapakan_activity_tahun;

    public final static String[] months = new String[]{};
    public final static String[] days = new String[]{"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun",};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_penggunaan_pakan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("Grafik Laporan Penggunaan Pakan");
        }

        //Set Colomn Bulan-----------------------------------------------
        //chart_laporanpakan_activity_bulan = (LineChartView) findViewById(R.id.chart_laporanpakan_activity_bulan);
        //generateInitialLineData();

        //Set Colomn Tahun-----------------------------------------------
        chart_laporanpakan_activity_tahun = (ColumnChartView) findViewById(R.id.chart_laporanpakan_activity_tahun);
        String urlParameter_Get_Tahun = "uid="+getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);
        new GetDataPerTahun().execute("http://ternaku.com/index.php/C_Laporan/TotalPakan_PETERNAKAN_TAHUN_TERTENTU",urlParameter_Get_Tahun);

    }

    //Get Data Per Tahun-----------------------------------
    private class GetDataPerTahun extends AsyncTask<String,Integer,String>{
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
                        values.add(new SubcolumnValue(Float.parseFloat(jObj.getString("Biaya")),ChartUtils.pickColor()));
                    }
                    axisValues.add(new AxisValue(i).setLabel(jObj.getString("Tanggal").substring(0,3)));
                    columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        data_laporanapakan_activity_tahun = new ColumnChartData(columns);
        data_laporanapakan_activity_tahun.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        data_laporanapakan_activity_tahun.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));

        chart_laporanpakan_activity_tahun.setColumnChartData(data_laporanapakan_activity_tahun);



        // Set value touch listener that will trigger changes for chartTop.
        //chart_laporanpakan_activity_tahun.setOnValueTouchListener(new ValueTouchListener());

        // Set selection mode to keep selected month column highlighted.
        chart_laporanpakan_activity_tahun.setValueSelectionEnabled(true);
        chart_laporanpakan_activity_tahun.setZoomType(ZoomType.HORIZONTAL);

    }

/*
    private void generateInitialLineData() {
        int numValues = 7;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, 0));
            axisValues.add(new AxisValue(i).setLabel(days[i]));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_ORANGE).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        data_laporanpakan_activity_bulan = new LineChartData(lines);
        data_laporanpakan_activity_bulan.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        data_laporanpakan_activity_bulan.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        chart_laporanpakan_activity_bulan.setLineChartData(data_laporanpakan_activity_bulan);

        // For build-up animation you have to disable viewport recalculation.
        chart_laporanpakan_activity_bulan.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 110, 6, 0);
        chart_laporanpakan_activity_bulan.setMaximumViewport(v);
        chart_laporanpakan_activity_bulan.setCurrentViewport(v);
        chart_laporanpakan_activity_bulan.setZoomType(ZoomType.HORIZONTAL);
    }

    private void generateLineData(int color, float range) {
        // Cancel last animation if not finished.
        chart_laporanpakan_activity_bulan.cancelDataAnimation();

        // Modify data targets
        Line line = data_laporanpakan_activity_bulan.getLines().get(0);// For this example there is always only one line.
        line.setColor(color);
        for (PointValue value : line.getValues()) {
            // Change target only for Y value.
            value.setTarget(value.getX(), (float) Math.random() * range);
        }

        // Start new data animation with 300ms duration;
        chart_laporanpakan_activity_bulan.startDataAnimation(300);
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            generateLineData(value.getColor(), 100);
        }

        @Override
        public void onValueDeselected() {

            generateLineData(ChartUtils.COLOR_ORANGE, 0);

        }
    }
*/
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

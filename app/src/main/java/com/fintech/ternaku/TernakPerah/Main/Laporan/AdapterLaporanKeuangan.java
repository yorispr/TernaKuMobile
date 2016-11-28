package com.fintech.ternaku.TernakPerah.Main.Laporan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fintech.ternaku.TernakPerah.Main.Laporan.Keuangan.DayAxisValueFormatter;
import com.fintech.ternaku.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pandhu on 11/16/16.
 */

public class AdapterLaporanKeuangan extends ArrayAdapter<ModelLaporanKeuanganGrafik> {
    LayoutInflater inflater;
    private List<ModelLaporanKeuanganGrafik> data = new ArrayList<ModelLaporanKeuanganGrafik>();

    public AdapterLaporanKeuangan(Context context, ArrayList<ModelLaporanKeuanganGrafik> items) {
        super(context,0,items);
        this.data = items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ModelLaporanKeuanganGrafik data_list = getItem(position);
        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.holder_list_laporan, null);
            holder.txt_laporan_fragment_jenisgrafik = (TextView) convertView.findViewById(R.id.txt_laporan_fragment_jenisgrafik);
            holder.chart_laporan_list = (LineChart) convertView.findViewById(R.id.chart_laporan_list);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        //Set Grafik+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        holder.chart_laporan_list.getDescription().setEnabled(false);

        // enable touch gestures
        holder.chart_laporan_list.setTouchEnabled(true);
        holder.chart_laporan_list.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        holder.chart_laporan_list.setDragEnabled(true);
        holder.chart_laporan_list.setScaleEnabled(true);
        holder.chart_laporan_list.setDrawGridBackground(false);
        holder.chart_laporan_list.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        holder.chart_laporan_list.setPinchZoom(false);

        // set an alternative background color
        holder.chart_laporan_list.setBackgroundColor(Color.WHITE);
        // get the legend (only possible after setting data)
        Legend l = holder.chart_laporan_list.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf"));
        l.setTextSize(11f);
        l.setTextColor(Color.LTGRAY);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(holder.chart_laporan_list,Integer.parseInt(data_list.getBulan()));
        XAxis xAxis = holder.chart_laporan_list.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf"));
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(xAxisFormatter);


        YAxis leftAxis = holder.chart_laporan_list.getAxisLeft();
        leftAxis.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf"));
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        holder.chart_laporan_list.getAxisRight().setEnabled(false);

        // set data
        holder.chart_laporan_list.setData(data_list.getGrafik());
        holder.chart_laporan_list.animateX(500);

        //Set Keterangan+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        holder.txt_laporan_fragment_jenisgrafik.setText(data_list.getJudul());
        if(data_list.getJudul().trim().equalsIgnoreCase("Pembelian Pakan")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_laporan_keuangan_pakan, 0, 0, 0);
        }
        if(data_list.getJudul().trim().equalsIgnoreCase("Pembelian Obat")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_laporan_keuangan_obat, 0, 0, 0);
        }
        if(data_list.getJudul().trim().equalsIgnoreCase("Pembelian Semen")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_laporan_keuangan_semen, 0, 0, 0);
        }
        if(data_list.getJudul().trim().equalsIgnoreCase("Pembelian Vaksin")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_laporan_keungan_vaksin, 0, 0, 0);
        }
        if(data_list.getJudul().trim().equalsIgnoreCase("Biaya Pemeriksaan Kesehatan Sapi")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_laporan_periksakesehatan, 0, 0, 0);
        }
        if(data_list.getJudul().trim().equalsIgnoreCase("Pembelian Perlengkapan")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_laporan_keuangan_perlengkapan, 0, 0, 0);
        }
        if(data_list.getJudul().trim().equalsIgnoreCase("Pembelian Ternak")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_laporan_keuangan_ternak, 0, 0, 0);
        }
        if(data_list.getJudul().trim().equalsIgnoreCase("Pembayaran Listrik")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_laporan_keuangan_listrik, 0, 0, 0);
        }
        if(data_list.getJudul().trim().equalsIgnoreCase("Pembelian Lainnya")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_laporan_keuangan_lainnya, 0, 0, 0);
        }
        if(data_list.getJudul().trim().equalsIgnoreCase("Penjualan Ternak")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_laporan_keuangan_ternak, 0, 0, 0);
        }
        if(data_list.getJudul().trim().equalsIgnoreCase("Penjualan Susu")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_laporan_keuangan_susu, 0, 0, 0);
        }
        if(data_list.getJudul().trim().equalsIgnoreCase("Penjualan Kompos")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_laporan_keuangan_kompos, 0, 0, 0);
        }
        if(data_list.getJudul().trim().equalsIgnoreCase("Penjualan Lainnya")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_laporan_keuangan_lainnya, 0, 0, 0);
        }
        if(data_list.getJudul().trim().equalsIgnoreCase("Jumlah Ternak Birahi")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_listternak_heat, 0, 0, 0);
        }
        if(data_list.getJudul().trim().equalsIgnoreCase("Produksi Susu Peternakan")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_listternak_laktasi, 0, 0, 0);
        }
        if(data_list.getJudul().trim().equalsIgnoreCase("Penggunaan Pakan")){
            holder.txt_laporan_fragment_jenisgrafik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pakan_dashboard, 0, 0, 0);
        }


        return convertView;
    }

    public class ViewHolder {
        public TextView txt_laporan_fragment_jenisgrafik;
        public LineChart chart_laporan_list;
    }

}

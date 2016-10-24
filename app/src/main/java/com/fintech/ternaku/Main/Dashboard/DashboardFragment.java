package com.fintech.ternaku.Main.Dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fintech.ternaku.ListDetailTernak.ListDetailTernakMain;
import com.fintech.ternaku.Main.MainActivity;
import com.fintech.ternaku.UrlList;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.numetriclabz.numandroidcharts.ChartData;
import com.fintech.ternaku.Connection;
import com.fintech.ternaku.Main.Dashboard.GaugeView;
import com.fintech.ternaku.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;


import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;
import pl.pawelkleczkowski.customgauge.CustomGauge;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class DashboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    //Set Expander---------------------
    private ExpandableRelativeLayout expanderlayout_dashboard_fragment_produsisusu, expanderlayout_dashboard_fragment_pemeriksaanhariini,
            expanderlayout_dashboard_fragment_sapidalammasasubur, expanderlayout_dashboard_fragment_datakehamilan,
            expanderlayout_dashboard_fragment_datakawanan, expanderlayout_dashboard_fragment_totalpakan;
    private Button buttonexpander_dashboard_fragment_produksisusu,buttonexpander_dashboard_fragment_pemeriksaanhariini,
            buttonexpander_dashboard_fragment_sapidalammasasubur,buttonexpander_dashboard_fragment_datakehamilan,
            buttonexpander_dashboard_fragment_datakawanan,buttonexpander_dashboard_fragment_totalpakan;
    private Spinner spinner_dashboard_fragment_namapeternakan;

    //Set Peternakan--------------------
    public List<String> list_dashboard_fragment_peternakan = new ArrayList<String>();

    //Set Gauge Produksi Susu Hari Ini---------------------------------------
    private TextView txt_dashboard_fragment_produksisusuhariini;
    private GaugeView gauge_dashboard_fragment_produksisusuhariini;
    private float degree = -225;
    private float sweepAngleControl = 0;
    private float sweepAngleFirstChart = 1;
    private float sweepAngleSecondChart = 1;
    private float sweepAngleThirdChart = 1;
    private boolean isInProgress = false;
    private boolean resetMode = false;
    private boolean canReset = false;
    float totalsusu=0;

    //Set Pie Chart-----------------------------------------------------------
    private PieChartView chart_dashboard_fragment_pemeriksaanhariini;
    private PieChartView chart_dashboard_fragment_sedangdalammasasubur;
    private PieChartView chart_dashboard_fragment_datakehamilan;
    private PieChartView chart_dashboard_fragment_datakawanan;
    private PieChartData data_dashboard_fragment_pemeriksaanhariinia;
    private PieChartData data_dashboard_fragment_sedangdalammasasubur;
    private PieChartData data_dashboard_fragment_datakehamilan;
    private PieChartData data_dashboard_fragment_datakawanan;
    List<SliceValue> value_dashboard_fragment_pemeriksaanhariini;
    List<SliceValue> value_dashboard_fragment_sedangdalammasasubur;
    List<SliceValue> value_dashboard_fragment_datakehamilan;
    List<SliceValue> value_dashboard_fragment_datakawanan;
    private TextView txt_dashboard_fragment_biayapakan;
    private TextView txt_dashboard_fragment_jumlahpakan;
    private boolean hasLabels = true;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = true;
    private boolean hasCenterText1 = true;
    private boolean hasCenterText2 = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;

    //Gauge Penggunaan Pakan--------------------------------------------
    private CustomGauge gauge_dashboard_fragment_pembelianpakan;
    int jumlahpakan=0;
    int jumlahbiaya=0;

    int jumlahperiksa=0;
    int totsapi=0;
    int jumlahSubur=0;
    int totDewasa;
    int i;
    boolean flag_run=false;
    String temp_result;
    RelativeLayout relativeLayout_dashboard_fragment;
    View view;

    public DashboardFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        relativeLayout_dashboard_fragment = (RelativeLayout)view.findViewById(R.id.relativeLayout_dashboard_fragment);
        setHasOptionsMenu(true);

        //Set Expander--------------------------------------------------------
        buttonexpander_dashboard_fragment_produksisusu=(Button)view.findViewById(R.id.buttonexpander_dashboard_fragment_produksisusu);
        expanderlayout_dashboard_fragment_produsisusu = (ExpandableRelativeLayout) view.findViewById(R.id.expanderlayout_dashboard_fragment_produsisusu);
        buttonexpander_dashboard_fragment_produksisusu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanderlayout_dashboard_fragment_produsisusu.toggle();
            }
        });
        buttonexpander_dashboard_fragment_pemeriksaanhariini=(Button)view.findViewById(R.id.buttonexpander_dashboard_fragment_pemeriksaanhariini);
        expanderlayout_dashboard_fragment_pemeriksaanhariini = (ExpandableRelativeLayout) view.findViewById(R.id.expanderlayout_dashboard_fragment_pemeriksaanhariini);
        buttonexpander_dashboard_fragment_pemeriksaanhariini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanderlayout_dashboard_fragment_pemeriksaanhariini.toggle();
            }
        });
        buttonexpander_dashboard_fragment_sapidalammasasubur=(Button)view.findViewById(R.id.buttonexpander_dashboard_fragment_sapidalammasasubur);
        expanderlayout_dashboard_fragment_sapidalammasasubur = (ExpandableRelativeLayout) view.findViewById(R.id.expanderlayout_dashboard_fragment_sapidalammasasubur);
        buttonexpander_dashboard_fragment_sapidalammasasubur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanderlayout_dashboard_fragment_sapidalammasasubur.toggle();
            }
        });
        buttonexpander_dashboard_fragment_datakehamilan=(Button)view.findViewById(R.id.buttonexpander_dashboard_fragment_datakehamilan);
        expanderlayout_dashboard_fragment_datakehamilan = (ExpandableRelativeLayout) view.findViewById(R.id.expanderlayout_dashboard_fragment_datakehamilan);
        buttonexpander_dashboard_fragment_datakehamilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanderlayout_dashboard_fragment_datakehamilan.toggle();
            }
        });
        buttonexpander_dashboard_fragment_datakawanan=(Button)view.findViewById(R.id.buttonexpander_dashboard_fragment_datakawanan);
        expanderlayout_dashboard_fragment_datakawanan = (ExpandableRelativeLayout) view.findViewById(R.id.expanderlayout_dashboard_fragment_datakawanan);
        buttonexpander_dashboard_fragment_datakawanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanderlayout_dashboard_fragment_datakawanan.toggle();
            }
        });
        buttonexpander_dashboard_fragment_totalpakan=(Button)view.findViewById(R.id.buttonexpander_dashboard_fragment_totalpakan);
        expanderlayout_dashboard_fragment_totalpakan = (ExpandableRelativeLayout) view.findViewById(R.id.expanderlayout_dashboard_fragment_totalpakan);
        buttonexpander_dashboard_fragment_totalpakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanderlayout_dashboard_fragment_totalpakan.toggle();
            }
        });

        //Set Spinner Peternakan------------------------------------
        spinner_dashboard_fragment_namapeternakan = (Spinner) view.findViewById(R.id.spinner_dashboard_fragment_namapeternakan);
        String urlParameter_get_spinner_peternakan = "uid=" + getActivity().getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null);
        new GetPeternakan().execute(url.getUrl_GetPeternakan(), urlParameter_get_spinner_peternakan);

        //Set Gauge Produksi Susu Hari Ini--------------------------
        txt_dashboard_fragment_produksisusuhariini = (TextView)view.findViewById(R.id.txt_dashboard_fragment_produksisusuhariini);
        gauge_dashboard_fragment_produksisusuhariini = (GaugeView) view.findViewById(R.id.gauge_dashboard_fragment_produksisusuhariini);
        gauge_dashboard_fragment_produksisusuhariini.setRotateDegree(degree);

        //Set Pie Chart --------------------------------------------
        chart_dashboard_fragment_pemeriksaanhariini = (PieChartView) view.findViewById(R.id.chart_dashboard_fragment_pemeriksaanhariini);
        chart_dashboard_fragment_pemeriksaanhariini.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, SliceValue sliceValue) {
                if(i == 0) {
                    Intent act = new Intent(getActivity(), ListDetailTernakMain.class);
                    act.putExtra("periksa", "periksa");
                    startActivity(act);
                } else if(i == 1) {
                    Intent act = new Intent(getActivity(), ListDetailTernakMain.class);
                    act.putExtra("belumperiksa", "belumperiksa");
                    startActivity(act);
                }
            }

            @Override
            public void onValueDeselected() {

            }
        });
        chart_dashboard_fragment_sedangdalammasasubur = (PieChartView) view.findViewById(R.id.chart_dashboard_fragment_sedangdalammasasubur);
        chart_dashboard_fragment_sedangdalammasasubur.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, SliceValue sliceValue) {
                if(i == 0) {
                    Intent act = new Intent(getActivity(), ListDetailTernakMain.class);
                    act.putExtra("masasubur", "masasubur");
                    startActivity(act);
                }else if (i==1){
                    Intent act = new Intent(getActivity(), ListDetailTernakMain.class);
                    act.putExtra("tidakmasasubur", "tidakmasasubur");

                    startActivity(act);
                }
            }

            @Override
            public void onValueDeselected() {

            }
        });
        chart_dashboard_fragment_datakehamilan = (PieChartView) view.findViewById(R.id.chart_dashboard_fragment_datakehamilan);
        chart_dashboard_fragment_datakehamilan.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, SliceValue sliceValue) {
                Log.d("SLICE",String.valueOf(i));
                Intent act;
                switch (i){
                    case 0:
                        act = new Intent(getActivity(), ListDetailTernakMain.class);
                        act.putExtra("mengandung", "mengandung");
                        startActivity(act);
                        break;
                    case 1:
                        act = new Intent(getActivity(), ListDetailTernakMain.class);
                        act.putExtra("melahirkan", "melahirkan");
                        startActivity(act);
                        break;
                    case 2:
                        act = new Intent(getActivity(), ListDetailTernakMain.class);
                        act.putExtra("menyusui", "menyusui");
                        startActivity(act);
                        break;
                    case 3:
                        act = new Intent(getActivity(), ListDetailTernakMain.class);
                        act.putExtra("kehamilanlainnya", "kehamilanlainnya");
                        startActivity(act);
                        break;
                }
            }

            @Override
            public void onValueDeselected() {

            }
        });
        chart_dashboard_fragment_datakawanan = (PieChartView) view.findViewById(R.id.chart_dashboard_fragment_datakawanan);
        chart_dashboard_fragment_datakawanan.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, SliceValue sliceValue) {
                Log.d("SLICEKawanan",String.valueOf(i));
                Intent act;

                switch (i){
                    case 0:
                        act = new Intent(getActivity(), ListDetailTernakMain.class);
                        act.putExtra("dewasa", "dewasa");
                        startActivity(act);
                        break;
                    case 1:
                        act = new Intent(getActivity(), ListDetailTernakMain.class);
                        act.putExtra("muda", "muda");
                        startActivity(act);
                        break;
                    case 2:
                        act = new Intent(getActivity(), ListDetailTernakMain.class);
                        act.putExtra("bayi", "bayi");
                        startActivity(act);
                        break;
                    case 3:
                        act = new Intent(getActivity(), ListDetailTernakMain.class);
                        act.putExtra("kawananlainnya", "kawananlainnya");
                        startActivity(act);
                        break;
                }
            }

            @Override
            public void onValueDeselected() {

            }
        });


        //Set Gauge Pembelian Pakan----------------------------------
        txt_dashboard_fragment_jumlahpakan = (TextView)view.findViewById(R.id.txt_dashboard_fragment_jumlahpakan);
        txt_dashboard_fragment_biayapakan = (TextView)view.findViewById(R.id.txt_dashboard_fragment_biayapakan);
        ChartData cd = new ChartData(60f);
        List values = new ArrayList<>();
        values.add(cd);
        values.add(new ChartData(65f));
        values.add(new ChartData(55f));
        gauge_dashboard_fragment_pembelianpakan = (CustomGauge) view.findViewById(R.id.gauge_dashboard_fragment_pembelianpakan);

        return view;
    }

    //Get Data Dashboard---------------------------------------------------
    private class GetDashboardData extends AsyncTask<String, Integer, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Harap tunggu...");
            //mLoadingView.start();
            // pDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(urls[0], urls[1]);
            return json;
        }

        protected void onPostExecute(String result2) {
            temp_result = result2;
            Log.d("PET", result2);
            //mLoadingView.stop();
            SetDataDashboard();
            //pDialog.dismiss();
            //layout.setVisibility(View.VISIBLE);
            //SetDataDashboard(result);

        }
    }

    //Get Data Peternakan--------------------------------------------------
    private class GetPeternakan extends AsyncTask<String, Integer, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Harap tunggu...");
            //pDialog.show();
            //layout.setVisibility(View.INVISIBLE);
            //mLoadingView.start();

        }

        @Override
        protected String doInBackground(String... urls) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(urls[0], urls[1]);
            return json;
        }

        protected void onPostExecute(String result) {
            ShowPeternakan(result);
        }
    }

    //Set To Spinner---------------------------------
    private void ShowPeternakan(String result) {
        list_dashboard_fragment_peternakan.clear();

        Log.d("PET",result);
        try{
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jObj = jArray.getJSONObject(i);
                ModelGetPeternakanDashboard pet = new ModelGetPeternakanDashboard();
                pet.setId_peternakan(jObj.getString("ID_PETERNAKAN"));
                pet.setId_pengguna(jObj.getString("ID_PEMILIK"));
                pet.setNama_peternakan(jObj.getString("NAMA_PETERNAKAN"));
                pet.setAlamat(jObj.getString("ALAMAT_PETERNAKAN").trim()+"\n");
                pet.setTelpon(jObj.getString("TELPON_PETERNAKAN"));
                pet.setLatitude(jObj.getString("LAT"));
                pet.setLongitude(jObj.getString("LNG"));
                pet.setTernak_pedaging(jObj.getInt("TERNAK_PEDAGING"));
                pet.setTernak_perah(jObj.getInt("TERNAK_PERAH"));

                Log.d("RESP",jObj.getString("ID_PETERNAKAN"));

                list_dashboard_fragment_peternakan.add("("+pet.getId_peternakan()+") "+pet.getNama_peternakan());
            }
            //Execute Dashboard Data-------------------------------------------
            String urlParameters = "idpeternakan="+getActivity().getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null).trim();
            new GetDashboardData().execute(url.getUrl_GetDashboardInformation(),urlParameters);
            Log.d("IDP",url.getUrl_GetDashboardInformation());

            /*
            ArrayAdapter<String> adapter_peternakan = new ArrayAdapter<String>(
                    getContext(), android.R.layout.simple_spinner_item, list_dashboard_fragment_peternakan);
            adapter_peternakan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_dashboard_fragment_namapeternakan.setAdapter(adapter_peternakan);
            spinner_dashboard_fragment_namapeternakan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getActivity(),list_dashboard_fragment_peternakan.get(i),Toast.LENGTH_LONG).show();

                    String idpeternakan = list_dashboard_fragment_peternakan.get(i);
                    idpeternakan = idpeternakan.substring(idpeternakan.indexOf("(")+1);
                    idpeternakan = idpeternakan.substring(0,idpeternakan.indexOf(")"));

                    //Execute Dashboard Data-------------------------------------------
                    String urlParameters = "idpeternakan="+getActivity().getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null).trim();
                    new GetDashboardData().execute("http://ternaku.com/index.php/C_Ternak/GetDashboardData",urlParameters);
                    Log.d("IDP",urlParameters);

                    jumlahSubur = 0;
                    jumlahperiksa = 0;
                    totalsusu = 0;
                    txt_dashboard_fragment_produksisusuhariini.setText("0.0");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });*/
        }
        catch (JSONException e){e.printStackTrace();}
    }

    //Set Dashboard Data---------------------------------------------------------------
    private void SetDataDashboard(){
        int numValues = 6;
        value_dashboard_fragment_pemeriksaanhariini = new ArrayList<SliceValue>();
        value_dashboard_fragment_sedangdalammasasubur = new ArrayList<SliceValue>();
        value_dashboard_fragment_datakehamilan = new ArrayList<SliceValue>();
        value_dashboard_fragment_datakawanan = new ArrayList<SliceValue>();


        value_dashboard_fragment_pemeriksaanhariini.clear();
        value_dashboard_fragment_sedangdalammasasubur.clear();
        value_dashboard_fragment_datakehamilan.clear();
        value_dashboard_fragment_datakawanan.clear();

        try {
            JSONArray jArray = new JSONArray(temp_result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObj = jArray.getJSONObject(i);

                //Get Data Gauge Produksi Susu------------------------------------------
                if(nullHandle(jObj,"produksi_susu")==0){
                    totalsusu = 0;
                }else if(nullHandle(jObj,"produksi_susu")==1){
                    totalsusu = jObj.getLong("produksi_susu");
                }

                //Get Data Periksa Hari ini----------------------------------------------
                SliceValue valuePeriksaHariIni = new SliceValue(jObj.getInt("periksa"), Color.parseColor("#2ecc71"));
                int belumperiksa = jObj.getInt("total_sapi") - jObj.getInt("periksa");
                SliceValue valueBelumPeriksa = new SliceValue(belumperiksa, Color.parseColor("#e74c3c"));
                jumlahperiksa = jObj.getInt("periksa");
                totsapi = jObj.getInt("total_sapi");
                value_dashboard_fragment_pemeriksaanhariini.add(valuePeriksaHariIni);
                value_dashboard_fragment_pemeriksaanhariini.add(valueBelumPeriksa);

                //Get Data Sapi dalam Masa Subur----------------------------------------------
                SliceValue valueSubur = new SliceValue(jObj.getInt("subur"), Color.parseColor("#ff9ad7"));
                int belumSubur = jObj.getInt("sapi_dewasa") - jObj.getInt("subur");
                SliceValue valueBelumSubur = new SliceValue(belumSubur, Color.parseColor("#9cb6c1"));
                jumlahSubur = jObj.getInt("subur");
                totDewasa = jObj.getInt("sapi_dewasa");
                value_dashboard_fragment_sedangdalammasasubur.add(valueSubur);
                value_dashboard_fragment_sedangdalammasasubur.add(valueBelumSubur);

                if (!jObj.isNull("jumlah_pakan")) {
                    jumlahpakan = jObj.getInt("jumlah_pakan");
                }
                if (!jObj.isNull("harga")) {
                    jumlahbiaya = jObj.getInt("harga");
                }

                //Get Data Kehamilan----------------------------------------------------------
                SliceValue valuehamil = new SliceValue(jObj.getInt("jumlah_ternakhamil"), Color.parseColor("#d280f0"));
                SliceValue valuemelahirkan = new SliceValue(jObj.getInt("jumlah_ternakmelahirkan"), Color.parseColor("#8bdafc"));
                SliceValue valuemenyusui = new SliceValue(jObj.getInt("jumlah_ternakmenyusui"), Color.parseColor("#2ecc71"));
                int lainnya = jObj.getInt("jumlah_tidakhamilmenyusuimelahirkan");
                SliceValue valueLainnya = new SliceValue(lainnya, Color.parseColor("#bdc3c7"));
                value_dashboard_fragment_datakehamilan.add(valuehamil);
                value_dashboard_fragment_datakehamilan.add(valuemelahirkan);
                value_dashboard_fragment_datakehamilan.add(valuemenyusui);
                value_dashboard_fragment_datakehamilan.add(valueLainnya);

                //Get Data Kawanan----------------------------------------------------------
                SliceValue valuedewasa = new SliceValue(jObj.getInt("jumlah_dewasa"), Color.parseColor("#4183D7"));
                SliceValue valueanak = new SliceValue(jObj.getInt("jumlah_heifers"), Color.parseColor("#59ABE3"));
                SliceValue valuebayi = new SliceValue(jObj.getInt("jumlah_calv"), Color.parseColor("#8bdafc"));
                int lainnya2 = totsapi - jObj.getInt("jumlah_dewasa")-jObj.getInt("jumlah_heifers")-jObj.getInt("jumlah_calv");
                SliceValue valuelainnya2 = new SliceValue(lainnya2, Color.parseColor("#bdc3c7"));
                value_dashboard_fragment_datakawanan.add(valuedewasa);
                value_dashboard_fragment_datakawanan.add(valueanak);
                value_dashboard_fragment_datakawanan.add(valuebayi);
                value_dashboard_fragment_datakawanan.add(valuelainnya2);
            }
            //Start Gauge Produksi Susu-------------------------------------------
            startRunning(-255+(totalsusu*(float)15.1));
            txt_dashboard_fragment_produksisusuhariini.setText(String.valueOf((totalsusu)));
        }catch (JSONException e){e.printStackTrace();}

        new Thread() {
            public void run() {
                for (i = 0; i <= jumlahpakan; i++) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gauge_dashboard_fragment_pembelianpakan.setValue(i);
                            }
                        });
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        String currencyCode = "IDR";
        Currency currency = Currency.getInstance(currencyCode);
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(currency);
        String formattedAmount = format.format(jumlahbiaya);
        animateTextView(0, jumlahpakan, txt_dashboard_fragment_jumlahpakan);
        txt_dashboard_fragment_biayapakan.setText(""+formattedAmount);

        //Set Data Pemeriksaan Hari Ini--------------------------------------------
        data_dashboard_fragment_pemeriksaanhariinia = new PieChartData(value_dashboard_fragment_pemeriksaanhariini);
        data_dashboard_fragment_pemeriksaanhariinia.setHasLabels(hasLabels);
        data_dashboard_fragment_pemeriksaanhariinia.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data_dashboard_fragment_pemeriksaanhariinia.setHasLabelsOutside(hasLabelsOutside);
        data_dashboard_fragment_pemeriksaanhariinia.setHasCenterCircle(hasCenterCircle);

        data_dashboard_fragment_pemeriksaanhariinia.setCenterText1(String.valueOf(jumlahperiksa));
        data_dashboard_fragment_pemeriksaanhariinia.setCenterText2("Sudah diperiksa");
        data_dashboard_fragment_pemeriksaanhariinia.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));


        //Set Data dalam masa subur-----------------------------------------------
        data_dashboard_fragment_sedangdalammasasubur = new PieChartData(value_dashboard_fragment_sedangdalammasasubur);
        data_dashboard_fragment_sedangdalammasasubur.setHasLabels(hasLabels);
        data_dashboard_fragment_sedangdalammasasubur.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data_dashboard_fragment_sedangdalammasasubur.setHasLabelsOutside(hasLabelsOutside);
        data_dashboard_fragment_sedangdalammasasubur.setHasCenterCircle(hasCenterCircle);

        data_dashboard_fragment_sedangdalammasasubur.setCenterText1(String.valueOf(jumlahSubur));
        data_dashboard_fragment_sedangdalammasasubur.setCenterText2("Sedang Heat");
        data_dashboard_fragment_sedangdalammasasubur.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));

        //Set Data Kehamilan-----------------------------------------------
        data_dashboard_fragment_datakehamilan = new PieChartData(value_dashboard_fragment_datakehamilan);
        data_dashboard_fragment_datakehamilan.setHasLabels(hasLabels);
        data_dashboard_fragment_datakehamilan.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data_dashboard_fragment_datakehamilan.setHasLabelsOutside(hasLabelsOutside);

        data_dashboard_fragment_datakehamilan.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));

        //Set Data Kawanan-----------------------------------------------
        data_dashboard_fragment_datakawanan = new PieChartData(value_dashboard_fragment_datakawanan);
        data_dashboard_fragment_datakawanan.setHasLabels(hasLabels);
        data_dashboard_fragment_datakawanan.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data_dashboard_fragment_datakawanan.setHasLabelsOutside(hasLabelsOutside);

        data_dashboard_fragment_datakawanan.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));

        if (isExploded) {
            data_dashboard_fragment_pemeriksaanhariinia.setSlicesSpacing(24);
        }

        if (hasCenterText1) {
            //data.setCenterText1("Hello!");
            // Get roboto-italic font.
            //Typeface tf = Typeface.createFromAsset(MainActivity.this.getAssets(), "Roboto-Italic.ttf");
            //data.setCenterText1Typeface(tf);

            // Get font size from dimens.xml and convert it to sp(library uses sp values).
            data_dashboard_fragment_pemeriksaanhariinia.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
            data_dashboard_fragment_sedangdalammasasubur.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
        }

        if (hasCenterText2) {
            //data.setCenterText2("Charts (Roboto Italic)");

            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");

            data_dashboard_fragment_pemeriksaanhariinia.setCenterText2Typeface(tf);
            data_dashboard_fragment_pemeriksaanhariinia.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
        }

        //Set Chart To View---------------------------------------
        chart_dashboard_fragment_pemeriksaanhariini.setPieChartData(data_dashboard_fragment_pemeriksaanhariinia);
        chart_dashboard_fragment_sedangdalammasasubur.setPieChartData(data_dashboard_fragment_sedangdalammasasubur);
        chart_dashboard_fragment_datakehamilan.setPieChartData(data_dashboard_fragment_datakehamilan);
        chart_dashboard_fragment_datakawanan.setPieChartData(data_dashboard_fragment_datakawanan);

    }

    public static int nullHandle(JSONObject json, String key) throws JSONException {
        if (json.isNull(key))
            return 0;
        else{
            return 1;
        }
    }

    //Set Produksi Susu Hari Ini-------------------------------------------------------
    private void startRunning(final float total) {
        new Thread() {
            public void run() {
                for (int i = 0; i < 300; i++) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                degree++;
                                sweepAngleControl++;

                                if (degree < Math.round(total)) {
                                    gauge_dashboard_fragment_produksisusuhariini.setRotateDegree(degree);
                                }

                                /*if (total <=6) {
                                    //if(degree <= -45) {
                                        gaugeView.setRotateDegree(degree);
                                    //}
                                }
                                else if (total <= 12) {
                                   // if(degree <= -89) {
                                        gaugeView.setRotateDegree(degree);
                                   // }
                                }
                                else if (total <= 20 ) {
                                    //if(degree <= 0) {
                                        gaugeView.setRotateDegree(degree);
                                    //}
                                }*/

                                if (sweepAngleControl <= 90) {
                                    sweepAngleFirstChart++;
                                    gauge_dashboard_fragment_produksisusuhariini.setSweepAngleFirstChart(sweepAngleFirstChart);
                                    // gaugeView.setRotateDegree(total);


                                } else if (sweepAngleControl <= 180) {
                                    sweepAngleSecondChart++;
                                    gauge_dashboard_fragment_produksisusuhariini.setSweepAngleSecondChart(sweepAngleSecondChart);
                                    // gaugeView.setRotateDegree(total);

                                } else if (sweepAngleControl <= 270) {
                                    sweepAngleThirdChart++;
                                    gauge_dashboard_fragment_produksisusuhariini.setSweepAngleThirdChart(sweepAngleThirdChart);
                                    //gaugeView.setRotateDegree(total);

                                }

                            }
                        });
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (i == 299) {
                        isInProgress = false;
                        canReset = true;
                        ((MainActivity)getActivity()).setFlag_log_out(true);
                    }
                }
            }
        }.start();

    }

    private void resetGauges() {
        new Thread() {
            public void run() {
                for (int i = 0; i < 300; i++) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sweepAngleControl--;
                                sweepAngleFirstChart = 1;
                                sweepAngleSecondChart = 1;
                                sweepAngleThirdChart = 1;

                                degree--;
                                gauge_dashboard_fragment_produksisusuhariini.setSweepAngleFirstChart(0);
                                gauge_dashboard_fragment_produksisusuhariini.setSweepAngleSecondChart(0);
                                gauge_dashboard_fragment_produksisusuhariini.setSweepAngleThirdChart(0);
                                gauge_dashboard_fragment_produksisusuhariini.setRotateDegree(degree);

                            }
                        });
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (i == 299) {
                        resetMode = false;
                        canReset = false;
                    }

                }
            }
        }.start();
    }

    public void animateTextView(int initialValue, int finalValue, final TextView textview) {
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(0.8f);
        int start = Math.min(initialValue, finalValue);
        int end = Math.max(initialValue, finalValue);
        int difference = Math.abs(finalValue - initialValue);
        Handler handler = new Handler();
        for (int count = start; count <= end; count++) {
            int time = Math.round(decelerateInterpolator.getInterpolation((((float) count) / difference)) * 10) * count;
            final int finalCount = ((initialValue > finalValue) ? initialValue - count : count);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    txt_dashboard_fragment_jumlahpakan.setText(finalCount + " Kg");
                }
            }, time);
        }
    }

    public void animateTextBiaya(int initialValue, int finalValue, final TextView textview) {
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(0.8f);
        int start = Math.min(initialValue, finalValue);
        int end = Math.max(initialValue, finalValue);
        int difference = Math.abs(finalValue - initialValue);
        Handler handler = new Handler();
        for (int count = start; count <= end; count++) {
            int time = Math.round(decelerateInterpolator.getInterpolation((((float) count) / difference)) * 1) * count;
            final int finalCount = ((initialValue > finalValue) ? initialValue - count : count);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    txt_dashboard_fragment_biayapakan.setText("Rp "+finalCount);
                }
            }, time);
        }
    }
    private void explodeChart() {
        isExploded = !isExploded;
        //SetDataDashboard();

    }

    private void toggleLabelsOutside() {
        // has labels have to be true:P
        hasLabelsOutside = !hasLabelsOutside;
        if (hasLabelsOutside) {
            hasLabels = true;
            hasLabelForSelected = false;
            chart_dashboard_fragment_pemeriksaanhariini.setValueSelectionEnabled(hasLabelForSelected);
        }

        if (hasLabelsOutside) {
            chart_dashboard_fragment_pemeriksaanhariini.setCircleFillRatio(0.7f);
        } else {
            chart_dashboard_fragment_pemeriksaanhariini.setCircleFillRatio(1.0f);
        }

        SetDataDashboard();

    }

    private void toggleLabels() {
        hasLabels = !hasLabels;

        if (hasLabels) {
            hasLabelForSelected = false;
            chart_dashboard_fragment_pemeriksaanhariini.setValueSelectionEnabled(hasLabelForSelected);

            if (hasLabelsOutside) {
                chart_dashboard_fragment_pemeriksaanhariini.setCircleFillRatio(0.7f);
            } else {
                chart_dashboard_fragment_pemeriksaanhariini.setCircleFillRatio(1.0f);
            }
        }

        SetDataDashboard();
    }


    private void toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected;

        chart_dashboard_fragment_pemeriksaanhariini.setValueSelectionEnabled(hasLabelForSelected);

        if (hasLabelForSelected) {
            hasLabels = false;
            hasLabelsOutside = false;

            if (hasLabelsOutside) {
                chart_dashboard_fragment_pemeriksaanhariini.setCircleFillRatio(0.7f);
            } else {
                chart_dashboard_fragment_pemeriksaanhariini.setCircleFillRatio(1.0f);
            }
        }

        SetDataDashboard();
    }

    private void prepareDataAnimation() {
        for (SliceValue value : data_dashboard_fragment_pemeriksaanhariinia.getValues()) {
            value.setTarget((float) Math.random() * 30 + 15);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.refresh_menu:
                Log.d("Tes","tes");
                String urlParameters = "idpeternakan="+getActivity().getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null).trim();
                new GetDashboardData().execute(url.getUrl_GetDashboardInformation(),urlParameters);
                Log.d("IDP",urlParameters);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

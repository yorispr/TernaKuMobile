package com.fintech.ternaku.Main.Laporan;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.Main.Laporan.Keuangan.LaporanKeuangan;
import com.fintech.ternaku.Main.Laporan.Keuangan.LaporanKeuanganGrafik;
import com.fintech.ternaku.Main.Laporan.PenggunaanPakan.LaporanPenggunaanPakan;
import com.fintech.ternaku.Main.Laporan.ProduksiSusu.LaporanProduksiSusuGrafik;
import com.fintech.ternaku.R;
import com.fintech.ternaku.UrlList;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

public class LaporanFragment extends Fragment {
    private ProgressBar prog_laporan_fragment;
    private Spinner spinner_laporan_fragment_bulan,spinner_laporan_fragment_tahun;
    private int flag_tahun=0,flag_bulan=0;
    private TextView txt_laporan_fragment_pilihbulan;
    private LinearLayout linearLayout_laporan_fragment_pilihtgl;

    //Declare Laporan Keuangan----------------------------------------
    private LinearLayout linearLayout_laporan_fragment_laporankeuangan;
    private DialogPlus dialog_pilih_tanggal_laporan;
    private TextView txt_laporan_fragment_keuanganpembelianpakan,
            txt_laporan_fragment_keuanganpembelianobat,
            txt_laporan_fragment_keuanganpembelianvaksin,
            txt_laporan_fragment_keuanganpembeliansemen,
            txt_laporan_fragment_keuanganpemeriksaankesehatan,
            txt_laporan_fragment_keuanganpembelianperlengkapan,
            txt_laporan_fragment_keuanganpembelianternak,
            txt_laporan_fragment_keuanganpembelianlistrik,
            txt_laporan_fragment_keuanganpembelianlainnya,
            txt_laporan_fragment_keuanganpenjualanternak,
            txt_laporan_fragment_keuanganpenjualanpupuk,
            txt_laporan_fragment_keuanganpenjualansusu,
            txt_laporan_fragment_keuanganpenjualanlainnya;
    private Button button_laporan_fragment_grafikkeuangan;
    private String bln_selected,thn_selected,bln_selected_angka;

    //Get Url Link---------------------------------------------------------
    UrlList url = new UrlList();

    public LaporanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_laporan, container, false);

        //Set Layout------------------------------------------------------------
        txt_laporan_fragment_pilihbulan = (TextView) view.findViewById(R.id.txt_laporan_fragment_pilihbulan);
        linearLayout_laporan_fragment_laporankeuangan = (LinearLayout) view.findViewById(R.id.linearLayout_laporan_fragment_laporankeuangan);
        linearLayout_laporan_fragment_pilihtgl = (LinearLayout) view.findViewById(R.id.linearLayout_laporan_fragment_pilihtgl);


        //Declare Laporan Keuangan-----------------------------------------------
        txt_laporan_fragment_keuanganpembelianpakan = (TextView) view.findViewById(R.id.txt_laporan_fragment_keuanganpembelianpakan);
        txt_laporan_fragment_keuanganpembelianobat = (TextView) view.findViewById(R.id.txt_laporan_fragment_keuanganpembelianobat);
        txt_laporan_fragment_keuanganpembelianvaksin = (TextView) view.findViewById(R.id.txt_laporan_fragment_keuanganpembelianvaksin);
        txt_laporan_fragment_keuanganpembeliansemen = (TextView) view.findViewById(R.id.txt_laporan_fragment_keuanganpembeliansemen);
        txt_laporan_fragment_keuanganpemeriksaankesehatan = (TextView) view.findViewById(R.id.txt_laporan_fragment_keuanganpemeriksaankesehatan);
        txt_laporan_fragment_keuanganpembelianperlengkapan = (TextView) view.findViewById(R.id.txt_laporan_fragment_keuanganpembelianperlengkapan);
        txt_laporan_fragment_keuanganpembelianternak = (TextView) view.findViewById(R.id.txt_laporan_fragment_keuanganpembelianternak);
        txt_laporan_fragment_keuanganpembelianlistrik = (TextView) view.findViewById(R.id.txt_laporan_fragment_keuanganpembelianlistrik);
        txt_laporan_fragment_keuanganpembelianlainnya = (TextView) view.findViewById(R.id.txt_laporan_fragment_keuanganpembelianlainnya);
        txt_laporan_fragment_keuanganpenjualanternak = (TextView) view.findViewById(R.id.txt_laporan_fragment_keuanganpenjualanternak);
        txt_laporan_fragment_keuanganpenjualanpupuk = (TextView) view.findViewById(R.id.txt_laporan_fragment_keuanganpenjualanpupuk);
        txt_laporan_fragment_keuanganpenjualansusu = (TextView) view.findViewById(R.id.txt_laporan_fragment_keuanganpenjualansusu);
        txt_laporan_fragment_keuanganpenjualanlainnya = (TextView) view.findViewById(R.id.txt_laporan_fragment_keuanganpenjualanlainnya);

        //Declare Button Laporan Keuangan----------------------------------------
        button_laporan_fragment_grafikkeuangan = (Button) view.findViewById(R.id.button_laporan_fragment_grafikkeuangan);
        button_laporan_fragment_grafikkeuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("bln",bln_selected);
                b.putString("thn",thn_selected);
                b.putString("bln_angka",bln_selected_angka);
                Intent intent = new Intent(getActivity(),LaporanKeuanganGrafik.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        //Loading Bar------------------------------------------------------------
        prog_laporan_fragment = (ProgressBar) view.findViewById(R.id.prog_laporan_fragment);

        //Set All Action---------------------------------------------------------
        initiate();
        InitiateData();
        dialog_datepicker_laporan_initiate();
        linearLayout_laporan_fragment_pilihtgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_pilih_tanggal_laporan.show();
            }
        });



        return  view;
    }

    //Convert Currency---------------------------------------------------
    public String Convert(String jumlahbiaya)
    {
        String currencyCode = "IDR";
        Currency currency = Currency.getInstance(currencyCode);
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(currency);
        String formattedAmount = format.format(jumlahbiaya);

        return formattedAmount;
    }

    //Get Data Keuangan Laporan-------------------------------------------------
    private class GetDataKeuanganLaporan extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPreExecute() {
            prog_laporan_fragment.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0],params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("Laporan_Keuangan",s);
            ShowLaporanKeuangan(s);
            prog_laporan_fragment.setVisibility(View.GONE);
            linearLayout_laporan_fragment_laporankeuangan.setVisibility(View.VISIBLE);

        }
    }

    //Initiate------------------------------------------------------------------
    private void initiate(){
        prog_laporan_fragment.setVisibility(View.GONE);
        linearLayout_laporan_fragment_laporankeuangan.setVisibility(View.GONE);
    }
    private void InitiateData(){
        String[] bulan_array = {
                "Januari",
                "Februari",
                "Maret",
                "April",
                "Mei",
                "Juni",
                "Juli",
                "Agustus",
                "September",
                "Oktober",
                "November",
                "Desember"};
        Calendar cal = Calendar.getInstance();
        String bulan = bulan_array[cal.get(Calendar.MONTH)-1];
        String tahun = String.valueOf(cal.get(Calendar.YEAR));
        txt_laporan_fragment_pilihbulan.setText(bulan.trim()
                + ", " + String.valueOf(tahun.trim()));
        bln_selected = bulan_array[cal.get(Calendar.MONTH)-1];
        thn_selected = String.valueOf(cal.get(Calendar.YEAR));
        bln_selected_angka = String.valueOf(cal.get(Calendar.MONTH)-1);

        //Set Data------------------------------------------------------------
        String param = "uid=" + getActivity().getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                +"&bulan=" + bulan.trim()
                +"&tahun=" + tahun.trim();
        Log.d("CekUrl",param);
        new GetDataKeuanganLaporan().execute(url.getUrlGetLaporanKeuanganList(), param);
    }

    //DialogBox Initiate--------------------------------------------------------
    private void dialog_datepicker_laporan_initiate(){
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_picker_laporan,null);

        //Set Picker Bulan------------------------------
        final String[] bulan = {
                "Januari",
                "Februari",
                "Maret",
                "April",
                "Mei",
                "Juni",
                "Juli",
                "Agustus",
                "September",
                "Oktober",
                "November",
                "Desember"};
        final NumberPicker numberPicker_laporan_bulan = (NumberPicker) view.findViewById(R.id.numberPicker_laporan_bulan);
        numberPicker_laporan_bulan.setMinValue(0);
        numberPicker_laporan_bulan.setMaxValue(bulan.length-1);
        numberPicker_laporan_bulan.setDisplayedValues(bulan);
        numberPicker_laporan_bulan.setWrapSelectorWheel(true);
        setNumberPickerDividerColour(numberPicker_laporan_bulan,view);

        //Set Picker Tahun------------------------------
        final NumberPicker numberPicker_laporan_tahun = (NumberPicker) view.findViewById(R.id.numberPicker_laporan_tahun);
        numberPicker_laporan_tahun.setMinValue(2000);
        numberPicker_laporan_tahun.setMaxValue(2050);
        numberPicker_laporan_tahun.setWrapSelectorWheel(true);
        setNumberPickerDividerColour(numberPicker_laporan_tahun,view);

        dialog_pilih_tanggal_laporan = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(view))
                .setGravity(Gravity.BOTTOM)
                .setCancelable(false)
                .create();

        dialog_pilih_tanggal_laporan.findViewById(R.id.button_laporan_fragment_pickerset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiate();
                dialog_pilih_tanggal_laporan.dismiss();
                txt_laporan_fragment_pilihbulan.setText(bulan[numberPicker_laporan_bulan.getValue()].toString().trim()
                        + ", " + String.valueOf(numberPicker_laporan_tahun.getValue()).trim());
                bln_selected = bulan[numberPicker_laporan_bulan.getValue()].toString().trim();
                thn_selected = String.valueOf(numberPicker_laporan_tahun.getValue()).trim();
                bln_selected_angka = String.valueOf(numberPicker_laporan_bulan.getValue()+1).trim();

                //Set Data------------------------------------------------------------
                String param = "uid=" + getActivity().getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                        +"&bulan=" + bulan[numberPicker_laporan_bulan.getValue()].toString().trim()
                        +"&tahun=" + String.valueOf(numberPicker_laporan_tahun.getValue()).trim();
                Log.d("CekUrl",param);
                new GetDataKeuanganLaporan().execute(url.getUrlGetLaporanKeuanganList(), param);
            }
        });
        dialog_pilih_tanggal_laporan.findViewById(R.id.button_laporan_fragment_pickercancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_pilih_tanggal_laporan.dismiss();
            }
        });
    }

    private void setNumberPickerDividerColour(NumberPicker number_picker,View mContext){
        final int count = number_picker.getChildCount();

        for(int i = 0; i < count; i++){

            try{
                Field dividerField = number_picker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(mContext.getResources().getColor(R.color
                        .colorPrimary));
                dividerField.set(number_picker,colorDrawable);

                number_picker.invalidate();
            }
            catch(NoSuchFieldException e){
                Log.w("setNumberPickerTxtClr", e);
            }
            catch(IllegalAccessException e){
                Log.w("setNumberPickerTxtClr", e);
            }
            catch(IllegalArgumentException e){
                Log.w("setNumberPickerTxtClr", e);
            }
        }
    }

    //Show Laporan Keuangan-----------------------------------------------------
    private void ShowLaporanKeuangan(String result){
        float Pembelian_Pakan=0,
                Pembelian_Obat=0,
                Pembelian_Vaksin=0,
                Pembelian_Semen=0,
                Pemeriksaan_Kesehatan_Sapi=0,
                Pembelian_Perlengkapan=0,
                Pembelian_Ternak=0,
                Pembayaran_Listrik=0,
                Pembayaran_Lain_lain=0;
        float Penjualan_ternak=0,
                Penjualan_kompos=0,
                Penjualan_Susu=0,
                Pemasukan_Lain_lain=0;

        try {
            JSONArray jArray = new JSONArray(result);
            JSONObject jObj = jArray.getJSONObject(0);

            JSONArray getPengeluaran_arr = jObj.getJSONArray("UangKeluar");
            JSONArray getPemasukan_arr = jObj.getJSONArray("UangMasuk");

            for (int i = 0; i < getPengeluaran_arr.length(); i++) {
                JSONObject obj_getPengeluaran = getPengeluaran_arr.getJSONObject(i);

                if(obj_getPengeluaran.getString("jenis_transaksi").trim().equalsIgnoreCase("Pembelian Pakan")){
                    Pembelian_Pakan = Pembelian_Pakan + Float.valueOf(obj_getPengeluaran.getString("JumlahUangKeluar"));
                }
                if(obj_getPengeluaran.getString("jenis_transaksi").trim().equalsIgnoreCase("Pembelian Obat")){
                    Pembelian_Obat = Pembelian_Obat + Float.valueOf(obj_getPengeluaran.getString("JumlahUangKeluar"));
                }
                if(obj_getPengeluaran.getString("jenis_transaksi").trim().equalsIgnoreCase("Pembelian Vaksin")){
                    Pembelian_Vaksin = Pembelian_Vaksin + Float.valueOf(obj_getPengeluaran.getString("JumlahUangKeluar"));

                }
                if(obj_getPengeluaran.getString("jenis_transaksi").trim().equalsIgnoreCase("Pembelian Semen")){
                    Pembelian_Semen = Pembelian_Semen + Float.valueOf(obj_getPengeluaran.getString("JumlahUangKeluar"));

                }
                if(obj_getPengeluaran.getString("jenis_transaksi").trim().equalsIgnoreCase("Pemeriksaan Kesehatan Sapi")){
                    Pemeriksaan_Kesehatan_Sapi = Pemeriksaan_Kesehatan_Sapi + Float.valueOf(obj_getPengeluaran.getString("JumlahUangKeluar"));

                }
                if(obj_getPengeluaran.getString("jenis_transaksi").trim().equalsIgnoreCase("Pembelian Perlengkapan")){
                    Pembelian_Perlengkapan = Pembelian_Perlengkapan + Float.valueOf(obj_getPengeluaran.getString("JumlahUangKeluar"));

                }
                if(obj_getPengeluaran.getString("jenis_transaksi").trim().equalsIgnoreCase("Pembelian Ternak")){
                    Pembelian_Ternak = Pembelian_Ternak + Float.valueOf(obj_getPengeluaran.getString("JumlahUangKeluar"));

                }
                if(obj_getPengeluaran.getString("jenis_transaksi").trim().equalsIgnoreCase("Pembayaran Listrik")){
                    Pembayaran_Listrik = Pembayaran_Listrik + Float.valueOf(obj_getPengeluaran.getString("JumlahUangKeluar"));

                }
                if(obj_getPengeluaran.getString("jenis_transaksi").trim().equalsIgnoreCase("Pengeluaran Lainnya")){
                    Pembayaran_Lain_lain = Pembayaran_Lain_lain + Float.valueOf(obj_getPengeluaran.getString("JumlahUangKeluar"));

                }
            }

            for (int i = 0; i < getPemasukan_arr.length(); i++) {
                JSONObject obj_getPemasukan = getPemasukan_arr.getJSONObject(i);

                if(obj_getPemasukan.getString("jenis_transaksi").trim().equalsIgnoreCase("Penjualan Ternak")){
                    Penjualan_ternak = Penjualan_ternak + Float.valueOf(obj_getPemasukan.getString("JumlahUangKeluar"));
                }
                if(obj_getPemasukan.getString("jenis_transaksi").trim().equalsIgnoreCase("Penjualan Kompos")){
                    Penjualan_kompos = Penjualan_kompos + Float.valueOf(obj_getPemasukan.getString("JumlahUangKeluar"));
                }
                if(obj_getPemasukan.getString("jenis_transaksi").trim().equalsIgnoreCase("Penjualan Susu")){
                    Penjualan_Susu = Penjualan_Susu + Float.valueOf(obj_getPemasukan.getString("JumlahUangKeluar"));
                }
                if(obj_getPemasukan.getString("jenis_transaksi").trim().equalsIgnoreCase("Pemasukan Lainnya")){
                    Pemasukan_Lain_lain = Pemasukan_Lain_lain + Float.valueOf(obj_getPemasukan.getString("JumlahUangKeluar"));
                }
            }

            String currencyCode = "IDR";
            Currency currency = Currency.getInstance(currencyCode);
            NumberFormat format = NumberFormat.getCurrencyInstance();
            format.setMaximumFractionDigits(1);
            format.setCurrency(currency);
            //Uang Keluar-------------------------------------------------------
            txt_laporan_fragment_keuanganpembelianpakan.setText("Rp. " + SubString(String.valueOf(format.format(Pembelian_Pakan))) + ",00");
            txt_laporan_fragment_keuanganpembelianobat.setText("Rp. " + SubString(String.valueOf(format.format(Pembelian_Obat))) + ",00");
            txt_laporan_fragment_keuanganpembelianvaksin.setText("Rp. " + SubString(String.valueOf(format.format(Pembelian_Vaksin))) + ",00");
            txt_laporan_fragment_keuanganpembeliansemen.setText("Rp. " + SubString(String.valueOf(format.format(Pembelian_Semen))) + ",00");
            txt_laporan_fragment_keuanganpemeriksaankesehatan.setText("Rp. " + SubString(String.valueOf(format.format(Pemeriksaan_Kesehatan_Sapi))) + ",00");
            txt_laporan_fragment_keuanganpembelianperlengkapan.setText("Rp. " + SubString(String.valueOf(format.format(Pembelian_Perlengkapan))) + ",00");
            txt_laporan_fragment_keuanganpembelianternak.setText("Rp. " + SubString(String.valueOf(format.format(Pembelian_Ternak))) + ",00");
            txt_laporan_fragment_keuanganpembelianlistrik.setText("Rp. " + SubString(String.valueOf(format.format(Pembayaran_Listrik))) + ",00");
            txt_laporan_fragment_keuanganpembelianlainnya.setText("Rp. " + SubString(String.valueOf(format.format(Pembayaran_Lain_lain))) + ",00");
            //Uang Masuk-------------------------------------------------------
            txt_laporan_fragment_keuanganpenjualanternak.setText("Rp. " + SubString(String.valueOf(format.format(Penjualan_ternak))) + ",00");
            txt_laporan_fragment_keuanganpenjualanpupuk.setText("Rp. " + SubString(String.valueOf(format.format(Penjualan_kompos))) + ",00");
            txt_laporan_fragment_keuanganpenjualansusu.setText("Rp. " + SubString(String.valueOf(format.format(Penjualan_Susu))) + ",00");
            txt_laporan_fragment_keuanganpenjualanlainnya.setText("Rp. " + SubString(String.valueOf(format.format(Pemasukan_Lain_lain))) + ",00");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private String SubString(String value){
        //return value.substring(3, value.length()-2);
        return value;
    }



}

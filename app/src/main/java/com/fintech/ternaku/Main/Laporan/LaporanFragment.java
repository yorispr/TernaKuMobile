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
import com.fintech.ternaku.Main.Laporan.PenggunaanPakan.LaporanPenggunaanPakan;
import com.fintech.ternaku.Main.Laporan.ProduksiSusu.LaporanProduksiSusuGrafik;
import com.fintech.ternaku.R;
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
    private String bln_selected,thn_selected;


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
                Intent intent = new Intent(getActivity(),LaporanKeuangan.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        //Loading Bar------------------------------------------------------------
        prog_laporan_fragment = (ProgressBar) view.findViewById(R.id.prog_laporan_fragment);

        //Set All Action---------------------------------------------------------
        initiate();
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

                //Set Data------------------------------------------------------------
                String param = "uid=" + getActivity().getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna", null)
                        +"&bulan=" + bulan[numberPicker_laporan_bulan.getValue()].toString().trim()
                        +"&tahun=" + String.valueOf(numberPicker_laporan_tahun.getValue()).trim();
                Log.d("CekUrl",param);
                new GetDataKeuanganLaporan().execute("http://ternaku.com/index.php/C_Laporan/UangKeluarMasuk_PETERNAKAN_HARI", param);
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

            //Uang Keluar-------------------------------------------------------
            txt_laporan_fragment_keuanganpembelianpakan.setText("Rp. " + String.valueOf(Pembelian_Pakan) + "0");
            txt_laporan_fragment_keuanganpembelianobat.setText("Rp. " + String.valueOf(Pembelian_Obat) + "0");
            txt_laporan_fragment_keuanganpembelianvaksin.setText("Rp. " + String.valueOf(Pembelian_Vaksin) + "0");
            txt_laporan_fragment_keuanganpembeliansemen.setText("Rp. " + String.valueOf(Pembelian_Semen) + "0");
            txt_laporan_fragment_keuanganpemeriksaankesehatan.setText("Rp. " + String.valueOf(Pemeriksaan_Kesehatan_Sapi) + "0");
            txt_laporan_fragment_keuanganpembelianperlengkapan.setText("Rp. " + String.valueOf(Pembelian_Perlengkapan) + "0");
            txt_laporan_fragment_keuanganpembelianternak.setText("Rp. " + String.valueOf(Pembelian_Ternak) + "0");
            txt_laporan_fragment_keuanganpembelianlistrik.setText("Rp. " + String.valueOf(Pembayaran_Listrik) + "0");
            txt_laporan_fragment_keuanganpembelianlainnya.setText("Rp. " + String.valueOf(Pembayaran_Lain_lain) + "0");
            //Uang Masuk-------------------------------------------------------
            txt_laporan_fragment_keuanganpenjualanternak.setText("Rp. " + String.valueOf(Penjualan_ternak) + "0");
            txt_laporan_fragment_keuanganpenjualanpupuk.setText("Rp. " + String.valueOf(Penjualan_kompos) + "0");
            txt_laporan_fragment_keuanganpenjualansusu.setText("Rp. " + String.valueOf(Penjualan_Susu) + "0");
            txt_laporan_fragment_keuanganpenjualanlainnya.setText("Rp. " + String.valueOf(Pemasukan_Lain_lain) + "0");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

}

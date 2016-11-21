package com.fintech.ternaku.Main.TambahData;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fintech.ternaku.Main.TambahData.Kesehatan.AddCekKesehatan;
import com.fintech.ternaku.Main.TambahData.Kesehatan.ProtocolKesehatan.ShowProtokolKesehatan;
import com.fintech.ternaku.Main.TambahData.Kesuburan.InjeksiHormon.ShowInjeksiHormon;
import com.fintech.ternaku.Main.TambahData.PindahTernak.AddKandang;
import com.fintech.ternaku.Main.TambahData.PindahTernak.AddKawanan;
import com.fintech.ternaku.Main.TambahData.PindahTernak.PindahTernak;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.fintech.ternaku.Main.TambahData.Kesehatan.AddCekPenyakit;
import com.fintech.ternaku.Main.TambahData.Kesehatan.AddKarantina;
import com.fintech.ternaku.Main.TambahData.Kesehatan.AddVaksinasi;
import com.fintech.ternaku.Main.TambahData.Kesuburan.AddInseminasi;
import com.fintech.ternaku.Main.TambahData.Kesuburan.AddMasaSubur;
import com.fintech.ternaku.Main.TambahData.Kesuburan.AddMelahirkan;
import com.fintech.ternaku.Main.TambahData.Kesuburan.AddMengandung;
import com.fintech.ternaku.Main.TambahData.Kesuburan.AddMenyusui;
import com.fintech.ternaku.Main.TambahData.Kesuburan.AddPemeriksaanReproduksi;
import com.fintech.ternaku.Main.TambahData.Produksi.UpdateCulling;
import com.fintech.ternaku.Main.TambahData.Produksi.UpdateMasaKering;
import com.fintech.ternaku.Main.TambahData.Produksi.UpdateWeightActivity;
import com.fintech.ternaku.R;

public class AddDataFragment extends Fragment implements View.OnClickListener {
    //Button Main Tambah Data Fragment-----------------------------------
    private Button button_adddata_fragment_pindahternak,button_adddata_fragment_kesuburan,button_adddata_fragment_kesehatan,
            button_adddata_fragment_produksi;
    //Expander Maiin Tambah Data Fragment--------------------------------
    ExpandableRelativeLayout expander_adddata_fragment_kesuburan, expander_adddata_fragment_kesehatan,
            expander_adddata_fragment_produksi,expander_adddata_fragment_pindahternak;
    //Button Expander Pindah Ternak--------------------------------------
    private Button button_adddata_fragment_produksi_pindahternak,button_adddata_fragment_produksi_tambahkandang,
            button_adddata_fragment_produksi_tambahkawanan;

    //Button Expander Kesuburan------------------------------------------
    private Button button_adddata_fragment_kesuburan_mengandung,button_adddata_fragment_kesuburan_inseminasi,
            button_adddata_fragment_kesuburan_pemeriksaanreproduksi,button_adddata_fragment_kesuburan_masasubur,
            button_adddata_fragment_kesuburan_injeksihormon,button_adddata_fragment_kesuburan_kelahirankeguguran,
            button_adddata_fragment_kesuburan_tidakmenyusui;
    //Button Expander Kesehatan------------------------------------------
    private Button button_adddata_fragment_kesehatan_cekkesehatan,button_adddata_fragment_kesehatan_vaksinasi,
            button_adddata_fragment_kesehatan_cekpenyakit,button_adddata_fragment_kesehatan_karantina,
            button_adddata_fragment_kesehatan_tambahprotokol;
    //Button Expander Produksi------------------------------------------
    private Button button_adddata_fragment_produksi_masakering,button_adddata_fragment_produksi_beratbadan,
            button_adddata_fragment_produksi_perubahangeneral,button_adddata_fragment_produksi_culling;

    View view;
    public AddDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_data, container, false);


        //Pindah Ternak Action--------------------------------
        Button button_adddata_fragment_pindahternak =(Button)view.findViewById(R.id.button_adddata_fragment_pindahternak);
        expander_adddata_fragment_pindahternak = (ExpandableRelativeLayout) view.findViewById(R.id.expander_adddata_fragment_pindahternak);
        button_adddata_fragment_pindahternak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expander_adddata_fragment_pindahternak.toggle();
            }
        });

        //Kesuburan Ternak Action-----------------------------
        Button button_adddata_fragment_kesuburan =(Button)view.findViewById(R.id.button_adddata_fragment_kesuburan);
        expander_adddata_fragment_kesuburan = (ExpandableRelativeLayout) view.findViewById(R.id.expander_adddata_fragment_kesuburan);
        button_adddata_fragment_kesuburan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expander_adddata_fragment_kesuburan.toggle();
            }
        });

        //Kesehatan Ternak Action-----------------------------
        Button button_adddata_fragment_kesehatan =(Button)view.findViewById(R.id.button_adddata_fragment_kesehatan);
        expander_adddata_fragment_kesehatan = (ExpandableRelativeLayout) view.findViewById(R.id.expander_adddata_fragment_kesehatan);
        button_adddata_fragment_kesehatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expander_adddata_fragment_kesehatan.toggle();
            }
        });

        //Produksi Ternak Action-----------------------------
        Button button_adddata_fragment_produksi =(Button)view.findViewById(R.id.button_adddata_fragment_produksi);
        expander_adddata_fragment_produksi = (ExpandableRelativeLayout) view.findViewById(R.id.expander_adddata_fragment_produksi);
        button_adddata_fragment_produksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expander_adddata_fragment_produksi.toggle();
            }
        });

        //Declare Button Pindah Ternak-----------------------
        button_adddata_fragment_produksi_pindahternak = (Button)view.findViewById(R.id.button_adddata_fragment_produksi_pindahternak);
        button_adddata_fragment_produksi_pindahternak.setOnClickListener(this);

        button_adddata_fragment_produksi_tambahkandang = (Button)view.findViewById(R.id.button_adddata_fragment_produksi_tambahkandang);
        button_adddata_fragment_produksi_tambahkandang.setOnClickListener(this);

        button_adddata_fragment_produksi_tambahkawanan = (Button)view.findViewById(R.id.button_adddata_fragment_produksi_tambahkawanan);
        button_adddata_fragment_produksi_tambahkawanan.setOnClickListener(this);


        //Declare Button Expander Kesuburan------------------
        button_adddata_fragment_kesuburan_mengandung = (Button)view.findViewById(R.id.button_adddata_fragment_kesuburan_mengandung);
        button_adddata_fragment_kesuburan_mengandung.setOnClickListener(this);

        button_adddata_fragment_kesuburan_inseminasi = (Button)view.findViewById(R.id.button_adddata_fragment_kesuburan_inseminasi);
        button_adddata_fragment_kesuburan_inseminasi.setOnClickListener(this);

        button_adddata_fragment_kesuburan_pemeriksaanreproduksi = (Button)view.findViewById(R.id.button_adddata_fragment_kesuburan_pemeriksaanreproduksi);
        button_adddata_fragment_kesuburan_pemeriksaanreproduksi.setOnClickListener(this);

        button_adddata_fragment_kesuburan_masasubur = (Button)view.findViewById(R.id.button_adddata_fragment_kesuburan_masasubur);
        button_adddata_fragment_kesuburan_masasubur.setOnClickListener(this);

        button_adddata_fragment_kesuburan_injeksihormon = (Button)view.findViewById(R.id.button_adddata_fragment_kesuburan_injeksihormon);
        button_adddata_fragment_kesuburan_injeksihormon.setOnClickListener(this);

        button_adddata_fragment_kesuburan_kelahirankeguguran = (Button)view.findViewById(R.id.button_adddata_fragment_kesuburan_kelahirankeguguran);
        button_adddata_fragment_kesuburan_kelahirankeguguran.setOnClickListener(this);

        button_adddata_fragment_kesuburan_injeksihormon = (Button)view.findViewById(R.id.button_adddata_fragment_kesuburan_injeksihormon);
        button_adddata_fragment_kesuburan_injeksihormon.setOnClickListener(this);

        button_adddata_fragment_kesuburan_tidakmenyusui = (Button)view.findViewById(R.id.button_adddata_fragment_kesuburan_tidakmenyusui);
        button_adddata_fragment_kesuburan_tidakmenyusui.setOnClickListener(this);

        //Declare Button Expander Kesehatan------------------
        button_adddata_fragment_kesehatan_cekkesehatan = (Button)view.findViewById(R.id.button_adddata_fragment_kesehatan_cekkesehatan);
        button_adddata_fragment_kesehatan_cekkesehatan.setOnClickListener(this);

        button_adddata_fragment_kesehatan_vaksinasi = (Button)view.findViewById(R.id.button_adddata_fragment_kesehatan_vaksinasi);
        button_adddata_fragment_kesehatan_vaksinasi.setOnClickListener(this);

        button_adddata_fragment_kesehatan_cekpenyakit = (Button)view.findViewById(R.id.button_adddata_fragment_kesehatan_cekpenyakit);
        button_adddata_fragment_kesehatan_cekpenyakit.setOnClickListener(this);

        button_adddata_fragment_kesehatan_karantina = (Button)view.findViewById(R.id.button_adddata_fragment_kesehatan_karantina);
        button_adddata_fragment_kesehatan_karantina.setOnClickListener(this);

        button_adddata_fragment_kesuburan_injeksihormon = (Button)view.findViewById(R.id.button_adddata_fragment_kesuburan_injeksihormon);
        button_adddata_fragment_kesuburan_injeksihormon.setOnClickListener(this);

        button_adddata_fragment_kesehatan_tambahprotokol = (Button)view.findViewById(R.id.button_adddata_fragment_kesehatan_tambahprotokol);
        button_adddata_fragment_kesehatan_tambahprotokol.setOnClickListener(this);

        //Declare Button Expander Produksi------------------
        button_adddata_fragment_produksi_masakering = (Button)view.findViewById(R.id.button_adddata_fragment_produksi_masakering);
        button_adddata_fragment_produksi_masakering.setOnClickListener(this);

        button_adddata_fragment_produksi_beratbadan = (Button)view.findViewById(R.id.button_adddata_fragment_produksi_beratbadan);
        button_adddata_fragment_produksi_beratbadan.setOnClickListener(this);

        button_adddata_fragment_produksi_perubahangeneral = (Button)view.findViewById(R.id.button_adddata_fragment_produksi_perubahangeneral);
        button_adddata_fragment_produksi_perubahangeneral.setOnClickListener(this);

        button_adddata_fragment_produksi_culling = (Button)view.findViewById(R.id.button_adddata_fragment_produksi_culling);
        button_adddata_fragment_produksi_culling.setOnClickListener(this);

        button_adddata_fragment_kesuburan_injeksihormon = (Button)view.findViewById(R.id.button_adddata_fragment_kesuburan_injeksihormon);
        button_adddata_fragment_kesuburan_injeksihormon.setOnClickListener(this);

        button_adddata_fragment_kesehatan_tambahprotokol = (Button)view.findViewById(R.id.button_adddata_fragment_kesehatan_tambahprotokol);
        button_adddata_fragment_kesehatan_tambahprotokol.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        Intent i = new Intent();
        Log.d("ID",String.valueOf(id));
        switch (id){
            //Case For Expander Pindah Ternak-------------------------------------
            case R.id.button_adddata_fragment_produksi_pindahternak :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),PindahTernak.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_produksi_tambahkandang :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddKandang.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_produksi_tambahkawanan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddKawanan.class);
                startActivity(i);
                break;

            //Case For Expander Kesuburan-----------------------------------------
            case R.id.button_adddata_fragment_kesuburan_mengandung :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddMengandung.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_kesuburan_inseminasi :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddInseminasi.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_kesuburan_pemeriksaanreproduksi :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddPemeriksaanReproduksi.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_kesuburan_masasubur :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddMasaSubur.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_kesuburan_injeksihormon :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),ShowInjeksiHormon.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_kesuburan_kelahirankeguguran :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddMelahirkan.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_kesuburan_tidakmenyusui :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddMenyusui.class);
                startActivity(i);
                break;
            //Case For Expander Kesehatan-----------------------------------------
            case R.id.button_adddata_fragment_kesehatan_cekkesehatan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddCekKesehatan.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_kesehatan_vaksinasi :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddVaksinasi.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_kesehatan_cekpenyakit :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddCekPenyakit.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_kesehatan_karantina :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),AddKarantina.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_kesehatan_tambahprotokol :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),ShowProtokolKesehatan.class);
                startActivity(i);
                break;
            //Case For Expander Produksi-----------------------------------------
            case R.id.button_adddata_fragment_produksi_masakering :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),UpdateMasaKering.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_produksi_beratbadan :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),UpdateWeightActivity.class);
                startActivity(i);
                break;
            case R.id.button_adddata_fragment_produksi_perubahangeneral :
                Log.d("ID",String.valueOf(id));
                break;
            case R.id.button_adddata_fragment_produksi_culling :
                Log.d("ID",String.valueOf(id));
                i = new Intent(getActivity(),UpdateCulling.class);
                startActivity(i);
                break;
        }
    }
}

package com.fintech.ternaku.TernakPerah.ListDetailTernak;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fintech.ternaku.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pandhu on 9/28/16.
 */

public class AdapterDetailTernakListDetailTernak extends ArrayAdapter<ModelDetailTernalListDetailTernak> implements Filterable {
    private Activity activity;
    private List<ModelDetailTernalListDetailTernak> ternak;
    private static LayoutInflater inflater=null;
    private int layout;
    private List<ModelDetailTernalListDetailTernak> data = new ArrayList<ModelDetailTernalListDetailTernak>();
    private List<ModelDetailTernalListDetailTernak> dataorigin = new ArrayList<ModelDetailTernalListDetailTernak>();

    Activity act;

    public AdapterDetailTernakListDetailTernak(Activity a, int layout, List<ModelDetailTernalListDetailTernak>items) {
        super(a, layout, items);
        this.activity = a;
        this.layout = layout;
        data = items;
        dataorigin = items;
        act = a;
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
        View view = convertView;
        ViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);

        holder = new ViewHolder();
        view.setTag(holder);

        holder.linearlayout_produksi_susu = (LinearLayout)view.findViewById(R.id.linearlayout_listternak_activity_produksisusu);
        holder.id_ternak = (TextView)view.findViewById(R.id.txt_listdetailternak_activity_idternak);
        holder.nama_ternak = (TextView)view.findViewById(R.id.txt_listdetailternak_activity_nameternak);
        holder.kondisi_kesehatan = (TextView)view.findViewById(R.id.txt_listdetailternak_activity_kondisitubuh);
        holder.img_kondisi_tubuh = (ImageView) view.findViewById(R.id.img_listdetailternak_activity_kondisitubuh);
        holder.img_heat = (ImageView) view.findViewById(R.id.img_listdetailternak_activity_heatternak);
        holder.img_menyusui = (ImageView) view.findViewById(R.id.img_listdetailternak_activity_menyusuiternak);
        holder.img_kering = (ImageView) view.findViewById(R.id.img_listdetailternak_activity_keringternak);
        //holder.rfid = (TextView) view.findViewById(R.id.txt_listdetailternak_activity_rfid);
        holder.isheat = (TextView) view.findViewById(R.id.txt_listdetailternak_activity_heatternak);
        holder.ismenyusui = (TextView) view.findViewById(R.id.txt_listdetailternak_activity_menyusuiternak);
        holder.iskering = (TextView) view.findViewById(R.id.txt_listdetailternak_activity_keringternak);
        holder.berat = (TextView) view.findViewById(R.id.txt_listdetailternak_activity_beratbadan);
        holder.umur = (TextView) view.findViewById(R.id.txt_listdetailternak_activity_umurternak);
        holder.produksi_susu = (TextView) view.findViewById(R.id.txt_listdetailternak_activity_pruduksisusu);

        ModelDetailTernalListDetailTernak ternak = new ModelDetailTernalListDetailTernak();
        ternak = data.get(position);
        //Setting all values in listview

        holder.umur.setText(ternak.getUmur());

        holder.nama_ternak.setText(ternak.getNama_ternak());
        holder.id_ternak.setText(ternak.getId_ternak());
        //holder.rfid.setText(ternak.getRfid());

        holder.berat.setText(String.valueOf(ternak.getBerat()) + " Kg");

        if(ternak.getProduksisusu() != 0){
            holder.linearlayout_produksi_susu.setVisibility(View.VISIBLE);
            holder.produksi_susu.setText("Produksi Susu : " + String.valueOf(ternak.getProduksisusu()));
        }else{
            holder.linearlayout_produksi_susu.setVisibility(View.GONE);
        }

        String isheat = "";
        if(ternak.getIs_heat() == 1){
            isheat = "Heat";
            holder.img_heat.setBackgroundResource(R.drawable.ic_listternak_heat);
        }else{
            isheat = "Tidak Heat";
            holder.img_heat.setBackgroundResource(R.drawable.ic_listternak_tdkheat);
        }
        holder.isheat.setText(isheat);

        String ismenyusui = "";
        if(ternak.getIs_menyusui() == 1){
            ismenyusui = "Menyusui";
            holder.img_menyusui.setBackgroundResource(R.drawable.ic_listternak_menyusui);
        }else{
            ismenyusui = "Tidak Menyusui";
            holder.img_menyusui.setBackgroundResource(R.drawable.ic_listternak_tdkmenyusui);
        }
        holder.ismenyusui.setText(ismenyusui);

        String iskering = "";
        if(ternak.getIs_dry() == 1){
            iskering = "Kering";
            holder.img_kering.setBackgroundResource(R.drawable.ic_listternak_kering);
        }else{
            iskering = "Tidak Kering";
            holder.img_kering.setBackgroundResource(R.drawable.ic_listternak_laktasi);
        }
        holder.iskering.setText(iskering);


        int kondisi = Integer.parseInt(ternak.getBody_condition_score());

        if(kondisi >= 7) {
            holder.img_kondisi_tubuh.setImageResource(R.drawable.ic_body_condition_35);
        }
        else if(kondisi >= 5) {
            holder.img_kondisi_tubuh.setImageResource(R.drawable.ic_body_condition_30);

        }else if(kondisi >=0) {
            holder.img_kondisi_tubuh.setImageResource(R.drawable.ic_body_condition_25);
        }
        holder.kondisi_kesehatan.setText(getString_kondisi_tubuh(Integer.parseInt(ternak.getBody_condition_score())));

        return view;
    }

    public class ViewHolder {
        public TextView id_ternak,nama_ternak,kondisi_kesehatan, isheat,ismenyusui,iskering, berat, umur,produksi_susu;
        public ImageView img_kondisi_tubuh,img_heat,img_menyusui,img_kering;
        public LinearLayout linearlayout_produksi_susu;
    }

    public String getString_kondisi_tubuh(int kondisi){
        String string_kondisi="N/A";
        if(kondisi>=7){
            string_kondisi = "Baik";
        }else if(kondisi<7&&kondisi>=5){
            string_kondisi = "Cukup Baik";
        }else if(kondisi>0&&kondisi<5){
            string_kondisi = "Buruk";
        }

        return string_kondisi;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                data = (ArrayList<ModelDetailTernalListDetailTernak>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<ModelDetailTernalListDetailTernak> FilteredArrList = new ArrayList<ModelDetailTernalListDetailTernak>();

                if (dataorigin == null) {
                    dataorigin = new ArrayList<ModelDetailTernalListDetailTernak>(data); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                Log.d("constraint",constraint.toString());
                if(constraint.toString().equals("heat")){
                    for (int i = 0; i < dataorigin.size(); i++) {
                        if (dataorigin.get(i).getIs_heat() == 1) {
                            FilteredArrList.add(new ModelDetailTernalListDetailTernak(dataorigin.get(i).getId_ternak(),dataorigin.get(i).getNama_ternak(),dataorigin.get(i).getBody_condition_score(),dataorigin.get(i).getId_peternakan(),dataorigin.get(i).getTgl_lahir(),dataorigin.get(i).getRfid(),dataorigin.get(i).getIs_dry(),dataorigin.get(i).getIs_heat(), dataorigin.get(i).getIs_menyusui(),dataorigin.get(i).getBerat(),dataorigin.get(i).getUmur(),dataorigin.get(i).getProduksisusu()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                else if(constraint.toString().equals("menyusui")){
                    for (int i = 0; i < dataorigin.size(); i++) {
                        if (dataorigin.get(i).getIs_menyusui() == 1) {
                            FilteredArrList.add(new ModelDetailTernalListDetailTernak(dataorigin.get(i).getId_ternak(),dataorigin.get(i).getNama_ternak(),dataorigin.get(i).getBody_condition_score(),dataorigin.get(i).getId_peternakan(),dataorigin.get(i).getTgl_lahir(),dataorigin.get(i).getRfid(),dataorigin.get(i).getIs_dry(),dataorigin.get(i).getIs_heat(), dataorigin.get(i).getIs_menyusui(),dataorigin.get(i).getBerat(),dataorigin.get(i).getUmur(),dataorigin.get(i).getProduksisusu()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }

                else {

                    if (constraint == null || constraint.length() == 0) {
                        // set the Original result to return
                        results.count = dataorigin.size();
                        results.values = dataorigin;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < dataorigin.size(); i++) {
                            String filter_idternak = dataorigin.get(i).getId_ternak();
                            String filter_rfid = dataorigin.get(i).getRfid();

                            if (filter_idternak.toLowerCase().contains(constraint.toString()) || filter_rfid.toLowerCase().contains(constraint.toString())) {
                                FilteredArrList.add(new ModelDetailTernalListDetailTernak(dataorigin.get(i).getId_ternak(), dataorigin.get(i).getNama_ternak(), dataorigin.get(i).getBody_condition_score(), dataorigin.get(i).getId_peternakan(), dataorigin.get(i).getTgl_lahir(), dataorigin.get(i).getRfid(), dataorigin.get(i).getIs_dry(), dataorigin.get(i).getIs_heat(), dataorigin.get(i).getIs_menyusui(), dataorigin.get(i).getBerat(),dataorigin.get(i).getUmur(),dataorigin.get(i).getProduksisusu()));
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                }
                return results;
            }
        };
        return filter;
    }
}

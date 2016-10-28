package com.fintech.ternaku.ListDetailTernak;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.fintech.ternaku.Main.TambahData.PindahTernak.ModelKandangPindahTernak;
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

        holder.id_ternak = (TextView)view.findViewById(R.id.txt_listdetailternak_activity_idternak);
        holder.kondisi_kesehatan = (TextView)view.findViewById(R.id.txt_listdetailternak_activity_kondisitubuh);
        holder.img_kondisi_tubuh = (ImageView) view.findViewById(R.id.img_listdetailternak_activity_kondisitubuh);
        holder.rfid = (TextView) view.findViewById(R.id.txt_listdetailternak_activity_rfid);
        holder.isheat = (TextView) view.findViewById(R.id.txtIsHeat);
        holder.ismenyusui = (TextView) view.findViewById(R.id.txtIsMenyusui);
        holder.iskering = (TextView) view.findViewById(R.id.txtIsKering);
        holder.berat = (TextView) view.findViewById(R.id.txt_listdetailternak_activity_berat);
        holder.umur = (TextView) view.findViewById(R.id.txtUmur);


        ModelDetailTernalListDetailTernak ternak = new ModelDetailTernalListDetailTernak();
        ternak = data.get(position);
        // Setting all values in listview

        holder.umur.setText(ternak.getUmur());

        holder.id_ternak.setText(ternak.getId_ternak());
        holder.rfid.setText(ternak.getRfid());

        holder.berat.setText(String.valueOf(ternak.getBerat()) + " Kg");

        String isheat = "";
        holder.isheat.setTextColor(Color.parseColor("#e74c3c"));
        if(ternak.getIs_heat() == 1){
            isheat = "Heat";
            holder.isheat.setTextColor(Color.parseColor("#2ecc71"));

        }
        holder.isheat.setText(isheat);

        holder.ismenyusui.setTextColor(Color.parseColor("#e74c3c"));
        String ismenyusui = "";
        if(ternak.getIs_menyusui() == 1){
            ismenyusui = "Menyusui";
            holder.ismenyusui.setTextColor(Color.parseColor("#2ecc71"));
        }
        holder.ismenyusui.setText(ismenyusui);

        holder.iskering.setTextColor(Color.parseColor("#e74c3c"));
        String iskering = "";
        if(ternak.getIs_dry() == 1){
            iskering = "Kering";
            holder.iskering.setTextColor(Color.parseColor("#2ecc71"));
        }
        holder.iskering.setText(iskering);


        String kesuburan = "Sedang tidak heat";
        if(ternak.getIs_heat() == 1){
            kesuburan = "Sedang Heat";
        }

        int kondisi = Integer.parseInt(ternak.getBody_condition_score());

        if(kondisi >= 7) {
            holder.img_kondisi_tubuh.setImageResource(R.drawable.ic_cow_green);
            holder.kondisi_kesehatan.setTextColor(Color.parseColor("#2ecc71"));
        }
        else if(kondisi >= 5) {
            holder.img_kondisi_tubuh.setImageResource(R.drawable.ic_cow_orange);
            holder.kondisi_kesehatan.setTextColor(Color.parseColor("#e67e22"));

        }else if(kondisi >=0) {
            holder.img_kondisi_tubuh.setImageResource(R.drawable.ic_cow_red);
            holder.kondisi_kesehatan.setTextColor(Color.parseColor("#e74c3c"));
        }
        holder.kondisi_kesehatan.setText(getString_kondisi_tubuh(Integer.parseInt(ternak.getBody_condition_score())));

        return view;
    }

    public class ViewHolder {
        public TextView id_ternak,kondisi_kesehatan, rfid, isheat,ismenyusui,iskering, berat, umur;
        public ImageView img_kondisi_tubuh;
    }

    public String getString_kondisi_tubuh(int kondisi){
        String string_kondisi="Tidak Diketahui";
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
                            FilteredArrList.add(new ModelDetailTernalListDetailTernak(dataorigin.get(i).getId_ternak(),dataorigin.get(i).getNama_ternak(),dataorigin.get(i).getBody_condition_score(),dataorigin.get(i).getId_peternakan(),dataorigin.get(i).getTgl_lahir(),dataorigin.get(i).getRfid(),dataorigin.get(i).getIs_dry(),dataorigin.get(i).getIs_heat(), dataorigin.get(i).getIs_menyusui(),dataorigin.get(i).getBerat(),dataorigin.get(i).getUmur()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                else if(constraint.toString().equals("menyusui")){
                    for (int i = 0; i < dataorigin.size(); i++) {
                        if (dataorigin.get(i).getIs_menyusui() == 1) {
                            FilteredArrList.add(new ModelDetailTernalListDetailTernak(dataorigin.get(i).getId_ternak(),dataorigin.get(i).getNama_ternak(),dataorigin.get(i).getBody_condition_score(),dataorigin.get(i).getId_peternakan(),dataorigin.get(i).getTgl_lahir(),dataorigin.get(i).getRfid(),dataorigin.get(i).getIs_dry(),dataorigin.get(i).getIs_heat(), dataorigin.get(i).getIs_menyusui(),dataorigin.get(i).getBerat(),dataorigin.get(i).getUmur()));
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
                                FilteredArrList.add(new ModelDetailTernalListDetailTernak(dataorigin.get(i).getId_ternak(), dataorigin.get(i).getNama_ternak(), dataorigin.get(i).getBody_condition_score(), dataorigin.get(i).getId_peternakan(), dataorigin.get(i).getTgl_lahir(), dataorigin.get(i).getRfid(), dataorigin.get(i).getIs_dry(), dataorigin.get(i).getIs_heat(), dataorigin.get(i).getIs_menyusui(), dataorigin.get(i).getBerat(),dataorigin.get(i).getUmur()));
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

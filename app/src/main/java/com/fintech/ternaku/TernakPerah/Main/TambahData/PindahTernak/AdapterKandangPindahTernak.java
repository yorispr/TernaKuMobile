package com.fintech.ternaku.TernakPerah.Main.TambahData.PindahTernak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fintech.ternaku.R;

import java.util.ArrayList;

/**
 * Created by Pandhu on 10/4/16.
 */

public class AdapterKandangPindahTernak extends BaseAdapter implements Filterable {

    private ArrayList<ModelKandangPindahTernak> mOriginalValues; // Original Values
    private ArrayList<ModelKandangPindahTernak> mDisplayedValues;    // Values to be displayed
    LayoutInflater inflater;

    public AdapterKandangPindahTernak(Context context, ArrayList<ModelKandangPindahTernak> mKandangArrayList) {
        this.mOriginalValues = mKandangArrayList;
        this.mDisplayedValues = mKandangArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        RelativeLayout llContainer;
        TextView txtNamaKandang,txtKeteranganKandang;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.holder_list_kandang_pindah_ternak, null);
            holder.llContainer = (RelativeLayout)convertView.findViewById(R.id.llContainer);
            holder.txtKeteranganKandang = (TextView) convertView.findViewById(R.id.txtKeteranganKawanan);
            holder.txtNamaKandang = (TextView) convertView.findViewById(R.id.txtNamaKawanan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtNamaKandang.setText(mDisplayedValues.get(position).getNama_kandang());
        holder.txtKeteranganKandang.setText(mDisplayedValues.get(position).getLokasi_kandang());


        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,Filter.FilterResults results) {

                mDisplayedValues = (ArrayList<ModelKandangPindahTernak>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                Filter.FilterResults results = new Filter.FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<ModelKandangPindahTernak> FilteredArrList = new ArrayList<ModelKandangPindahTernak>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<ModelKandangPindahTernak>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getNama_kandang();
                        String data2 = mOriginalValues.get(i).getLokasi_kandang();

                        if (data.toLowerCase().contains(constraint.toString())||data2.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(new ModelKandangPindahTernak(mOriginalValues.get(i).getId_kandang(),mOriginalValues.get(i).getId_peternakan(),mOriginalValues.get(i).getNama_kandang(),mOriginalValues.get(i).getLokasi_kandang(),mOriginalValues.get(i).getKapasitas_kandang(),mOriginalValues.get(i).getStatus_aktif()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}



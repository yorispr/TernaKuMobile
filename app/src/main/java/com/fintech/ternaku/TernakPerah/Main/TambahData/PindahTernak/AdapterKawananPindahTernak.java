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

public class AdapterKawananPindahTernak extends BaseAdapter implements Filterable {

    private ArrayList<ModelKawananPindahTernak> mOriginalValues; // Original Values
    private ArrayList<ModelKawananPindahTernak> mDisplayedValues;    // Values to be displayed
    LayoutInflater inflater;

    public AdapterKawananPindahTernak(Context context, ArrayList<ModelKawananPindahTernak> mKawananArrayList) {
        this.mOriginalValues = mKawananArrayList;
        this.mDisplayedValues = mKawananArrayList;
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
        TextView txtNamaKawanan,txtKeteranganKawanan;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.holder_list_kawanan_pindah_ternak, null);
            holder.llContainer = (RelativeLayout)convertView.findViewById(R.id.llContainer);
            holder.txtNamaKawanan = (TextView) convertView.findViewById(R.id.txtNamaKawanan);
            holder.txtKeteranganKawanan = (TextView) convertView.findViewById(R.id.txtKeteranganKawanan);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtNamaKawanan.setText(mDisplayedValues.get(position).getNama_kawanan());
        holder.txtKeteranganKawanan.setText(mDisplayedValues.get(position).getKeterangan());


        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<ModelKawananPindahTernak>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<ModelKawananPindahTernak> FilteredArrList = new ArrayList<ModelKawananPindahTernak>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<ModelKawananPindahTernak>(mDisplayedValues); // saves the original data in mOriginalValues
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
                        String data = mOriginalValues.get(i).getNama_kawanan();
                        String data2 = mOriginalValues.get(i).getKeterangan();

                        if (data.toLowerCase().contains(constraint.toString())||data2.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(new ModelKawananPindahTernak(mOriginalValues.get(i).getId_kawanan(),mOriginalValues.get(i).getId_peternakan(),mOriginalValues.get(i).getNama_kawanan(),mOriginalValues.get(i).getUmur(),mOriginalValues.get(i).getKeterangan()));
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

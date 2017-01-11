package com.fintech.ternaku.TernakPotong.AddPakan;

import android.content.Context;
import android.util.Log;
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
 * Created by Pandhu on 12/15/16.
 */

public class AdapterAddPakanPedagingListKomposisi extends BaseAdapter implements Filterable {

    private ArrayList<ModelAddPakanPedagingListKomposisi> mOriginalValues; // Original Values
    private ArrayList<ModelAddPakanPedagingListKomposisi> mDisplayedValues;    // Values to be displayed
    LayoutInflater inflater;

    public AdapterAddPakanPedagingListKomposisi(Context context, ArrayList<ModelAddPakanPedagingListKomposisi> mKomposisiArrayList) {
        this.mOriginalValues = mKomposisiArrayList;
        this.mDisplayedValues = mKomposisiArrayList;
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
        TextView txtIdKomposisiListKomposisi,txtBatasUmurListKomposisi,txtIdJenisSapiListKomposisi;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.holder_list_komposisi_add_pakan_pedaging, null);
            holder.llContainer = (RelativeLayout)convertView.findViewById(R.id.llContainer);
            holder.txtIdKomposisiListKomposisi = (TextView) convertView.findViewById(R.id.txtIdKomposisiListKomposisi);
            holder.txtBatasUmurListKomposisi = (TextView) convertView.findViewById(R.id.txtBatasUmurListKomposisi);
            holder.txtIdJenisSapiListKomposisi = (TextView) convertView.findViewById(R.id.txtIdJenisSapiListKomposisi);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtIdKomposisiListKomposisi.setText(mDisplayedValues.get(position).getId_komposisi());
        holder.txtBatasUmurListKomposisi.setText(mDisplayedValues.get(position).getUmur_bawah() + " - " +mDisplayedValues.get(position).getUmur_atas() + " Tahun");
        holder.txtIdJenisSapiListKomposisi.setText(mDisplayedValues.get(position).getJenis_sapi());


        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,Filter.FilterResults results) {

                mDisplayedValues = (ArrayList<ModelAddPakanPedagingListKomposisi>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                Filter.FilterResults results = new Filter.FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<ModelAddPakanPedagingListKomposisi> FilteredArrList = new ArrayList<ModelAddPakanPedagingListKomposisi>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<ModelAddPakanPedagingListKomposisi>(mDisplayedValues); // saves the original data in mOriginalValues
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
                        String data = mOriginalValues.get(i).getId_komposisi();
                        String data2 = mOriginalValues.get(i).getJenis_sapi();

                        if (data.toLowerCase().contains(constraint.toString())||data2.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(new ModelAddPakanPedagingListKomposisi(mOriginalValues.get(i).getId_komposisi(),mOriginalValues.get(i).getUmur_bawah(),mOriginalValues.get(i).getUmur_atas(),mOriginalValues.get(i).getJenis_sapi(),mOriginalValues.get(i).getKg_konsentrat(),mOriginalValues.get(i).getId_komposisi(),mOriginalValues.get(i).getTotal_harga()));
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



package com.fintech.ternaku.TernakPerah.Main.TambahData.Kesehatan.ProtocolKesehatan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.fintech.ternaku.R;

import java.util.ArrayList;

/**
 * Created by Pandhu on 10/3/16.
 */

public class AdapterListProtokolKesehatan extends BaseAdapter implements Filterable {
    private ArrayList<ModelAddProtokolKesehatan> mOriginalValues; // Original Values
    private ArrayList<ModelAddProtokolKesehatan> mDisplayedValues;    // Values to be displayed
    LayoutInflater inflater;

    public AdapterListProtokolKesehatan(Context context, ArrayList<ModelAddProtokolKesehatan> mProtocolModelArrayList) {
        this.mOriginalValues = mProtocolModelArrayList;
        this.mDisplayedValues = mProtocolModelArrayList;
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
        TextView txtJudul,txtIsi,txtTgl,txtNama;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.holder_list_protocol, null);
            holder.txtJudul = (TextView) convertView.findViewById(R.id.txtJudul);
            holder.txtIsi = (TextView) convertView.findViewById(R.id.txtIsi);
            holder.txtTgl = (TextView)convertView.findViewById(R.id.txtTgl);
            holder.txtNama = (TextView)convertView.findViewById(R.id.txtNama);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtJudul.setText(mDisplayedValues.get(position).getJudul());
        holder.txtIsi.setText(mDisplayedValues.get(position).getIsi());

       /*try {
            String tgl = df.format(df.parse(mDisplayedValues.get(position).getTimestamp()));
            holder.txtTgl.setText(tgl.toString());
       }catch(ParseException pe){pe.printStackTrace();};*/
        holder.txtTgl.setText(mDisplayedValues.get(position).getTimestamp());

        holder.txtNama.setText(mDisplayedValues.get(position).getCreator());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<ModelAddProtokolKesehatan>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<ModelAddProtokolKesehatan> FilteredArrList = new ArrayList<ModelAddProtokolKesehatan>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<ModelAddProtokolKesehatan>(mDisplayedValues); // saves the original data in mOriginalValues
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
                        String data = mOriginalValues.get(i).getJudul();
                        String data2 = mOriginalValues.get(i).getIsi();

                        if (data.toLowerCase().startsWith(constraint.toString())||data2.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new ModelAddProtokolKesehatan(mOriginalValues.get(i).getId_protocol(),mOriginalValues.get(i).getJudul(),mOriginalValues.get(i).getIsi(),mOriginalValues.get(i).isImportant(),mOriginalValues.get(i).getCreator_id(),mOriginalValues.get(i).getCreator(),mOriginalValues.get(i).getTimestamp(),mOriginalValues.get(i).getIsread()));
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

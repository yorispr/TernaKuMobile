package com.fintech.ternaku.TernakPotong.ADG;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fintech.ternaku.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pandhu on 11/29/16.
 */

public class AdapterListAdg extends ArrayAdapter<ModelAdg> {
    private Activity activity;
    private List<ModelAdg> List_Adg;
    private static LayoutInflater inflater=null;
    private int layout;
    private List<ModelAdg> List_Adg_data = new ArrayList<ModelAdg>();
    Activity act;

    public AdapterListAdg(Activity a, int layout, List<ModelAdg>items) {
        super(a, layout, items);
        this.activity = a;
        this.layout = layout;
        List_Adg_data = items;
        act = a;
    }

    @Override
    public int getCount() {
        return List_Adg_data.size();
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

        holder.holder_list_adg_nilaiadg = (TextView)view.findViewById(R.id.holder_list_adg_nilaiadg);
        holder.holder_list_adg_idternak = (TextView)view.findViewById(R.id.holder_list_adg_idternak);
        holder.holder_list_adg_tglsebelum = (TextView)view.findViewById(R.id.holder_list_adg_tglsebelum);
        holder.holder_list_adg_beratsebelum = (TextView)view.findViewById(R.id.holder_list_adg_beratsebelum);
        holder.holder_list_adg_tglterakhir = (TextView)view.findViewById(R.id.holder_list_adg_tglterakhir);
        holder.holder_list_adg_beratterakhir = (TextView)view.findViewById(R.id.holder_list_adg_beratterakhir);


        ModelAdg Adg = new ModelAdg();
        Adg = List_Adg_data.get(position);
        // Setting all values in listview
        holder.holder_list_adg_nilaiadg.setText(Adg.getValue_adg());
        holder.holder_list_adg_idternak.setText(Adg.getId_ternak());
        holder.holder_list_adg_tglsebelum.setText(Adg.getTgl_timbang_sebelum());
        holder.holder_list_adg_beratsebelum.setText(Adg.getBerat_sebelum());
        holder.holder_list_adg_tglterakhir.setText(Adg.getTgl_timbang_terakhir());
        holder.holder_list_adg_beratterakhir.setText(Adg.getBerat_terakhir());
        return view;
    }

    public class ViewHolder {
        public TextView holder_list_adg_nilaiadg,holder_list_adg_idternak,holder_list_adg_tglsebelum,holder_list_adg_beratsebelum,
                holder_list_adg_tglterakhir,holder_list_adg_beratterakhir;
    }
}

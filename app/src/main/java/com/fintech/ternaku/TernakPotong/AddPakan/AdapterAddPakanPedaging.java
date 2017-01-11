package com.fintech.ternaku.TernakPotong.AddPakan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fintech.ternaku.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pandhu on 12/13/16.
 */

public class AdapterAddPakanPedaging extends ArrayAdapter<ModelAddPakanPedaging> implements OnClickListener {
    private Activity activity;
    private List<ModelAddPakanPedaging> List_AddPakan;
    private static LayoutInflater inflater=null;
    private int layout;
    private List<ModelAddPakanPedaging> List_AddPakan_data = new ArrayList<ModelAddPakanPedaging>();
    Activity act;

    public AdapterAddPakanPedaging(Activity a, int layout, List<ModelAddPakanPedaging>items) {
        super(a, layout, items);
        this.activity = a;
        this.layout = layout;
        List_AddPakan_data = items;
        act = a;
    }

    @Override
    public int getCount() {
        return List_AddPakan_data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);

        holder = new ViewHolder();
        view.setTag(holder);

        holder.holder_list_addpakanpedaging_idternak = (TextView)view.findViewById(R.id.holder_list_addpakanpedaging_idternak);
        holder.holder_list_addpakanpedaging_tglmakan = (TextView)view.findViewById(R.id.holder_list_addpakanpedaging_tglmakan);
        holder.holder_list_addpakanpedaging_konsentrat = (TextView)view.findViewById(R.id.holder_list_addpakanpedaging_konsentrat);
        holder.holder_list_addpakanpedaging_txtsesimakan = (TextView)view.findViewById(R.id.holder_list_addpakanpedaging_txtsesimakan);
        holder.holder_list_addpakanpedaging_imgsesimakan = (ImageView) view.findViewById(R.id.holder_list_addpakanpedaging_imgsesimakan);


        ModelAddPakanPedaging Pakan = new ModelAddPakanPedaging();
        Pakan = List_AddPakan_data.get(position);
        // Setting all values in listview
        holder.holder_list_addpakanpedaging_idternak.setText(" " + Pakan.getId_ternak());
        holder.holder_list_addpakanpedaging_tglmakan.setText(" " + Pakan.getTgl_makan());
        holder.holder_list_addpakanpedaging_konsentrat.setText(" " + Pakan.getJenis_konsentrat());
        holder.holder_list_addpakanpedaging_txtsesimakan.setText(" " + Pakan.getSesi_makan());
        if(Pakan.getSesi_makan().equalsIgnoreCase("Pagi")){
            holder.holder_list_addpakanpedaging_imgsesimakan.setImageResource(R.drawable.ic_addpakanpedaging_sun);
        }else if(Pakan.getSesi_makan().equalsIgnoreCase("Sore")){
            holder.holder_list_addpakanpedaging_imgsesimakan.setImageResource(R.drawable.ic_addpakanpedaging_moon);
        }

        return view;
    }

    @Override
    public void onClick(DialogPlus dialog, View view) {

    }

    public class ViewHolder {
        public TextView holder_list_addpakanpedaging_idternak,holder_list_addpakanpedaging_tglmakan,
                holder_list_addpakanpedaging_konsentrat,holder_list_addpakanpedaging_txtsesimakan;
        public ImageView holder_list_addpakanpedaging_imgsesimakan;
    }
}

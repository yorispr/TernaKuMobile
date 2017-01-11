package com.fintech.ternaku.TernakPerah.DetailTernak.Task;

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
 * Created by Pandhu on 10/26/16.
 */

public class AdapterDetailTernakTask extends ArrayAdapter<ModelDetailTernakTask>{
    private Activity activity;
    private List<ModelDetailTernakTask> List_TaskTernak;
    private static LayoutInflater inflater=null;
    private int layout;
    private List<ModelDetailTernakTask> List_TaskTernak_data = new ArrayList<ModelDetailTernakTask>();
    Activity act;

    public AdapterDetailTernakTask(Activity a, int layout, List<ModelDetailTernakTask>items) {
        super(a, layout, items);
        this.activity = a;
        this.layout = layout;
        List_TaskTernak_data = items;
        act = a;
    }

    @Override
    public int getCount() {
        return List_TaskTernak_data.size();
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

        holder.tgltask_taskdetail_activity_txt = (TextView)view.findViewById(R.id.tgltask_taskdetail_activity_txt);
        holder.isitask_taskdetail_activity_txt = (TextView)view.findViewById(R.id.isitask_taskdetail_activity_txt);


        ModelDetailTernakTask TaskTernak = new ModelDetailTernakTask();
        TaskTernak = List_TaskTernak_data.get(position);
        // Setting all values in listview

        holder.tgltask_taskdetail_activity_txt.setText(TaskTernak.getTgl_task_schedule());
        holder.isitask_taskdetail_activity_txt.setText(TaskTernak.getIsi_task());
        return view;
    }

    public class ViewHolder {
        public TextView tgltask_taskdetail_activity_txt,isitask_taskdetail_activity_txt;
    }
}

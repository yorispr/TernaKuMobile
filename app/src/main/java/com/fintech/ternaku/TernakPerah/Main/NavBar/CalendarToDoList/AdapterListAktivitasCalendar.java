package com.fintech.ternaku.TernakPerah.Main.NavBar.CalendarToDoList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fintech.ternaku.TernakPerah.Main.Pengingat.ReminderModel;
import com.fintech.ternaku.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pandhu on 10/25/16.
 */

public class AdapterListAktivitasCalendar extends ArrayAdapter<ReminderModel> {
    private Activity activity;
    private List<ReminderModel> ToDoListCalendar;
    private static LayoutInflater inflater=null;
    private int layout;
    private List<ReminderModel> ToDoListCalendar_data = new ArrayList<ReminderModel>();
    Activity act;

    public AdapterListAktivitasCalendar(Activity a, int layout, List<ReminderModel>items) {
        super(a, layout, items);
        this.activity = a;
        this.layout = layout;
        ToDoListCalendar_data = items;
        act = a;
    }

    @Override
    public int getCount() {
        return ToDoListCalendar_data.size();
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

        holder.txt_calendartodo_activity_holderjudul = (TextView)view.findViewById(R.id.txt_calendartodo_activity_holderjudul);
        holder.txt_calendartodo_activity_holderisi = (TextView)view.findViewById(R.id.txt_calendartodo_activity_holderisi);
        holder.txt_calendartodo_activity_holderwaktu = (TextView)view.findViewById(R.id.txt_calendartodo_activity_holderwaktu);


        ReminderModel CalendarToDoList = new ReminderModel();
        CalendarToDoList = ToDoListCalendar_data.get(position);
        // Setting all values in listview

        holder.txt_calendartodo_activity_holderjudul.setText(CalendarToDoList.getJudul());
        holder.txt_calendartodo_activity_holderisi.setText(CalendarToDoList.getIsi());
        //Convert Date-----------------------------------------------------
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
        Date Date = new Date();
        try {
            Date = sdf.parse(CalendarToDoList.getSchedule_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String Hour = (String) android.text.format.DateFormat.format("HH", Date);
        String Minute = (String) android.text.format.DateFormat.format("mm", Date);
        holder.txt_calendartodo_activity_holderwaktu.setText(Hour + " : " + Minute);
        return view;
    }

    public class ViewHolder {
        public TextView txt_calendartodo_activity_holderjudul,txt_calendartodo_activity_holderisi,txt_calendartodo_activity_holderwaktu;
    }
}
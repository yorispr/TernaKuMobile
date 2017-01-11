package com.fintech.ternaku.TernakPerah.Main.NavBar.CalendarToDoList;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fintech.ternaku.DatabaseHandler;
import com.fintech.ternaku.TernakPerah.Main.MainActivity;
import com.fintech.ternaku.TernakPerah.Main.Pengingat.ReminderModel;
import com.fintech.ternaku.R;
import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.entity.CalendarEvent;
import com.p_v.flexiblecalendar.view.BaseCellView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarToDoActivity extends AppCompatActivity {

    private TextView txt_calendartodo_activity_bulan,
            txt_calendartodo_activity_aktivitasbln,txt_calendartodo_activity_aktivitasthn;
    private ListView list_calendartodo_activity_aktivitas;
    String idpengguna;
    String idpeternakan;
    DatabaseHandler db;
    ArrayList<ReminderModel> ListCalendarToDo = new ArrayList<ReminderModel>();
    ArrayList<ReminderModel> ListCalendarToDo_Temp =  new ArrayList<ReminderModel>();;
    AdapterListAktivitasCalendar adapter;
    private Map<Integer,List<CustomEvent>> eventMap;
    ArrayList<String> day_temp = new ArrayList<String>();
    ArrayList<String> month_temp = new ArrayList<String>();
    ArrayList<String> year_temp = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_to_do);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                    | ActionBar.DISPLAY_SHOW_TITLE
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(" ");
        }
        idpeternakan = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan",null);
        idpengguna = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPengguna",null);

        //Set List Data---------------------------------------------
        list_calendartodo_activity_aktivitas = (ListView) findViewById(R.id.list_calendartodo_activity_aktivitas);
        db = new DatabaseHandler(this);
        ListCalendarToDo = db.getReminder();

        MoveToTemp();

        //Initiate Month-------------------------------------------
        txt_calendartodo_activity_bulan = (TextView)findViewById(R.id.txt_calendartodo_activity_bulan);
        final FlexibleCalendarView calendarView = (FlexibleCalendarView)findViewById(R.id.month_view);
        Calendar cal = Calendar.getInstance();
        txt_calendartodo_activity_bulan.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                + " " +cal.get(Calendar.YEAR));
        SetListNew(cal.get(Calendar.YEAR), cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()),
                cal.get(Calendar.DATE));

        //Initiate Detail Event-------------------------------------
        txt_calendartodo_activity_aktivitasbln = (TextView)findViewById(R.id.txt_calendartodo_activity_aktivitasbln);
        txt_calendartodo_activity_aktivitasthn = (TextView)findViewById(R.id.txt_calendartodo_activity_aktivitasthn);
        txt_calendartodo_activity_aktivitasbln.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        txt_calendartodo_activity_aktivitasthn.setText(String.valueOf(cal.get(Calendar.YEAR)));

        //Change Date Event-----------------------------------------
        calendarView.setOnDateClickListener(new FlexibleCalendarView.OnDateClickListener() {
            @Override
            public void onDateClick(int year, int month, int day) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day);
                txt_calendartodo_activity_aktivitasbln.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
                txt_calendartodo_activity_aktivitasthn.setText(String.valueOf(year));
                SetListNew(year, cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()), day);

            }
        });


        calendarView.selectDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
        getSupportActionBar().setTitle(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                +" " + cal.get(Calendar.YEAR));
        calendarView.setOnMonthChangeListener(new FlexibleCalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month, int direction) {
                Calendar cal = Calendar.getInstance();
                if(cal.get(Calendar.YEAR)==year&&cal.get(Calendar.MONTH)==month){
                    calendarView.selectDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
                    //txt_calendartodo_activity_bulan.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                    //        +" " + year);
                    getSupportActionBar().setTitle(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                            +" " + year);
                }else{
                    cal.set(year, month, 1);
                    //txt_calendartodo_activity_bulan.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                    //        +" " + year);
                    getSupportActionBar().setTitle(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                            +" " + year);
                }
            }
        });
        calendarView.setCalendarView(new FlexibleCalendarView.CalendarView() {
            @Override
            public BaseCellView getCellView(int position, View convertView, ViewGroup parent, int cellType) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(CalendarToDoActivity.this);
                    cellView = (BaseCellView) inflater.inflate(R.layout.activity_calendar_cell_view, null);
                }
                if (cellType == BaseCellView.TODAY) {
                    cellView.setTextColor(getResources().getColor(android.R.color.white));
                    cellView.setTextSize(16);
                } else {
                    cellView.setTextColor(getResources().getColor(android.R.color.black));
                    cellView.setTextSize(12);
                }
                return cellView;
            }

            @Override
            public BaseCellView getWeekdayCellView(int position, View convertView, ViewGroup parent) {
                BaseCellView cellView = (BaseCellView) convertView;
                if (cellView == null) {
                    LayoutInflater inflater = LayoutInflater.from(CalendarToDoActivity.this);
                    cellView = (BaseCellView) inflater.inflate(R.layout.activity_calendar_cell_view, null);
                    cellView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    cellView.setTextColor(getResources().getColor(android.R.color.white));
                }
                return cellView;
            }

            @Override
            public String getDayOfWeekDisplayValue(int dayOfWeek, String defaultValue) {
                return null;
            }
        });

        calendarView.setEventDataProvider(new FlexibleCalendarView.EventDataProvider() {
            @Override
            public List<CalendarEvent> getEventsForTheDay(int year, int month, int day) {

                int isiList = ListCalendarToDo.size();
                for(int i=0;i<isiList;i++){
                    if(Integer.valueOf(day_temp.get(i))==day && Integer.valueOf(month_temp.get(i))==month+1
                            && Integer.valueOf(year_temp.get(i))==year){
                        List<CalendarEvent> eventColors = new ArrayList<>(1);
                        eventColors.add(new CalendarEvent(android.R.color.holo_orange_light));
                        return eventColors;
                    }
                }
                return null;
            }
        });
    }

    private void MoveToTemp(){
        for(int i=0;i<ListCalendarToDo.size();i++) {
            //Convert Date-----------------------------------------------------
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
            Date Date = new Date();
            try {
                Date = sdf.parse(ListCalendarToDo.get(i).getSchedule_time());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String newFormat = formatter.format(Date);
            try {
                Date = formatter.parse(newFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String Month_new = (String) android.text.format.DateFormat.format("MM", Date);
            String Year_New = (String) android.text.format.DateFormat.format("yyyy", Date);
            String Day_New = (String) android.text.format.DateFormat.format("dd", Date);
            day_temp.add(Day_New);
            month_temp.add(Month_new);
            year_temp.add(Year_New);

        }
    }

    private void SetListNew(int Year, String Month, int Day){
        ListCalendarToDo_Temp.clear();

        for(int i=0;i<ListCalendarToDo.size();i++){
            //Convert Date-----------------------------------------------------
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
            Date Date = new Date();
            try {
                Date = sdf.parse(ListCalendarToDo.get(i).getSchedule_time());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String Month_new = (String) android.text.format.DateFormat.format("MMMM", Date);
            String Year_New = (String) android.text.format.DateFormat.format("yyyy", Date);
            String Day_New = (String) android.text.format.DateFormat.format("dd", Date);
            Log.d("Jumlah List",Month_new + Year_New +  Day_New);
            Log.d("Jumlah List", String.valueOf(Month) + String.valueOf(Year) +  String.valueOf(Day));
            if(Integer.valueOf(Day_New)==Day && Month_new.equalsIgnoreCase(Month) && Integer.valueOf(Year_New)==Year){
                ListCalendarToDo_Temp.add(ListCalendarToDo.get(i));
            }
        }
        Log.d("Jumlah List",String.valueOf(ListCalendarToDo_Temp.size()));
        adapter = new AdapterListAktivitasCalendar(CalendarToDoActivity.this, R.layout.layout_calendar_activity_holderlist, ListCalendarToDo_Temp);
        list_calendartodo_activity_aktivitas.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent i = new Intent(CalendarToDoActivity.this, MainActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

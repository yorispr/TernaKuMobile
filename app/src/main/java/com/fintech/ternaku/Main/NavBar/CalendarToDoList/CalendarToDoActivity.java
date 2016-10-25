package com.fintech.ternaku.Main.NavBar.CalendarToDoList;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cuboid.cuboidcirclebutton.CuboidButton;
import com.fintech.ternaku.R;
import com.p_v.flexiblecalendar.FlexibleCalendarView;
import com.p_v.flexiblecalendar.entity.CalendarEvent;
import com.p_v.flexiblecalendar.entity.Event;
import com.p_v.flexiblecalendar.view.BaseCellView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarToDoActivity extends AppCompatActivity {

    private TextView txt_calendartodo_activity_bulan,
            txt_calendartodo_activity_aktivitasbln,txt_calendartodo_activity_aktivitasthn;
    private CuboidButton txt_calendartodo_activity_aktivitastgl;
    private ListView list_calendartodo_activity_aktivitas;

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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(" ");
        }

        //Initiate Month-------------------------------------------
        txt_calendartodo_activity_bulan = (TextView)findViewById(R.id.txt_calendartodo_activity_bulan);
        final FlexibleCalendarView calendarView = (FlexibleCalendarView)findViewById(R.id.month_view);
        Calendar cal = Calendar.getInstance();
        txt_calendartodo_activity_bulan.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        + " " +cal.get(Calendar.YEAR));

        //Initiate Detail Event-------------------------------------
        txt_calendartodo_activity_aktivitastgl = (CuboidButton)findViewById(R.id.txt_calendartodo_activity_aktivitastgl);
        txt_calendartodo_activity_aktivitasbln = (TextView)findViewById(R.id.txt_calendartodo_activity_aktivitasbln);
        txt_calendartodo_activity_aktivitasthn = (TextView)findViewById(R.id.txt_calendartodo_activity_aktivitasthn);
        txt_calendartodo_activity_aktivitastgl.setText(String.valueOf(cal.get(Calendar.DATE)));
        txt_calendartodo_activity_aktivitasbln.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        txt_calendartodo_activity_aktivitasthn.setText(String.valueOf(cal.get(Calendar.YEAR)));

        //Change Date Event-----------------------------------------
        calendarView.setOnDateClickListener(new FlexibleCalendarView.OnDateClickListener() {
            @Override
            public void onDateClick(int year, int month, int day) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day);
                txt_calendartodo_activity_aktivitastgl.setText(String.valueOf(day));
                txt_calendartodo_activity_aktivitasbln.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
                txt_calendartodo_activity_aktivitasthn.setText(String.valueOf(year));
            }
        });

        calendarView.selectDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
        calendarView.setOnMonthChangeListener(new FlexibleCalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month, int direction) {
                Calendar cal = Calendar.getInstance();
                if(cal.get(Calendar.YEAR)==year&&cal.get(Calendar.MONTH)==month){
                    calendarView.selectDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
                    txt_calendartodo_activity_bulan.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                            +" " + year);
                }else{
                    cal.set(year, month, 1);
                    txt_calendartodo_activity_bulan.setText(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
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
                    cellView.setTextSize(15);
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
                if (year == 2016 && month == 10 && day == 12) {
                    List<CalendarEvent> eventColors = new ArrayList<>(2);
                    eventColors.add(new CalendarEvent(android.R.color.holo_blue_light));
                    eventColors.add(new CalendarEvent(android.R.color.holo_purple));
                    return eventColors;
                }
                if (year == 2016 && month == 10 && day == 7 ||
                        year == 2016 && month == 10 && day == 29 ||
                        year == 2016 && month == 10 && day == 5 ||
                        year == 2016 && month == 10 && day == 9) {
                    List<CalendarEvent> eventColors = new ArrayList<>(1);
                    eventColors.add(new CalendarEvent(android.R.color.holo_blue_light));
                    return eventColors;
                }

                if (year == 2016 && month == 10 && day == 31 ||
                        year == 2016 && month == 10 && day == 22 ||
                        year == 2016 && month == 10 && day == 18 ||
                        year == 2016 && month == 10 && day == 11) {
                    List<CalendarEvent> eventColors = new ArrayList<>(3);
                    eventColors.add(new CalendarEvent(android.R.color.holo_red_dark));
                    eventColors.add(new CalendarEvent(android.R.color.holo_orange_light));
                    eventColors.add(new CalendarEvent(android.R.color.holo_purple));
                    return eventColors;
                }

                return null;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

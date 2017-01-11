package com.fintech.ternaku.TernakPerah.Main.Pengingat;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.fintech.ternaku.DatabaseHandler;
import com.fintech.ternaku.R;

public class ViewReminderActivity extends AppCompatActivity {

    private TextView isi,txtTglBuat,txtNamaPembuat,txtTglEvent;
    private ImageView penting;
    String idreminder;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setTitle("");
        }
        db = new DatabaseHandler(this);

        if(!getIntent().getStringExtra("id").isEmpty()){
            idreminder = getIntent().getStringExtra("id");
        }

        ReminderModel reminder = db.GetReminderById(idreminder);


        isi = (TextView)findViewById(R.id.txtIsi);
        txtTglBuat = (TextView)findViewById(R.id.txtTglBuat);
        txtNamaPembuat = (TextView)findViewById(R.id.txtNamaPembuat);
        txtTglEvent = (TextView)findViewById(R.id.txtTglEvent);
        penting = (ImageView)findViewById(R.id.imgIsPenting);

        if(reminder != null){
            getSupportActionBar().setTitle(reminder.getJudul());
            isi.setText(reminder.getIsi());
            txtTglBuat.setText(reminder.getTimestamp());
            txtNamaPembuat.setText(reminder.getCreator());
            txtTglEvent.setText(reminder.getSchedule_time());
            penting.setBackgroundResource(R.drawable.ic_not_important_reminder);
            if(reminder.isImportant()) {
                penting.setBackgroundResource(R.drawable.ic_important_reminder);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

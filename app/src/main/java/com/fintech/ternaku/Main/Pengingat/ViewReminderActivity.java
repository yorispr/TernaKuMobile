package com.fintech.ternaku.Main.Pengingat;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fintech.ternaku.DatabaseHandler;
import com.fintech.ternaku.Main.TambahData.Kesuburan.InjeksiHormon.ModelAddProtokolInjeksi;
import com.fintech.ternaku.R;

public class ViewReminderActivity extends AppCompatActivity {

    private TextView judul,isi,penting;
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

        judul = (TextView)findViewById(R.id.txtJudul);
        isi = (TextView)findViewById(R.id.txtIsi);
        penting = (TextView)findViewById(R.id.txtIsPenting);
        penting.setVisibility(View.GONE);

        if(reminder != null){
            judul.setText(reminder.getJudul());
            isi.setText(reminder.getIsi());
            if(reminder.isImportant()) {
                penting.setVisibility(View.VISIBLE);
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

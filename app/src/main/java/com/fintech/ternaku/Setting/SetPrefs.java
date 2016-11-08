package com.fintech.ternaku.Setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fintech.ternaku.R;

import java.util.List;


public class SetPrefs extends PreferenceActivity {

    private AppCompatDelegate mDelegate;

    Preference chkreminder;
    Preference listpendadaran,listundangan,listmengawas;
    ListPreference listujian;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar)LayoutInflater.from(this).inflate(R.layout.activity_show_prefs, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addPreferencesFromResource(R.xml.preferences);


        Preference pref_change_peternakan = findPreference("change_peternakan");
        pref_change_peternakan.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent testIntent = new Intent(getApplicationContext(), UpdatePeternakanActivity.class);
                startActivity(testIntent);
                return true;
            }
        });

        SharedPreferences LoginShared = getApplicationContext().getSharedPreferences(getString(R.string.userpref), MODE_PRIVATE);
        if(LoginShared.getString("keyIdPengguna",null).equalsIgnoreCase("RL-1")){
            pref_change_peternakan.setEnabled(false);
        }else{
            pref_change_peternakan.setEnabled(true);
        }
        Preference pref = findPreference("change_password");
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent testIntent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(testIntent);
                return true;
            }
        });

        Preference pref_change_peternak = findPreference("change_peternak");
        pref_change_peternak.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent testIntent = new Intent(getApplicationContext(), UpdatePeternakActivity.class);
                startActivity(testIntent);
                return true;
            }
        });

        Preference pref_bluetooth = findPreference("bluetooth_setting");
        pref_bluetooth.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent testIntent = new Intent(getApplicationContext(), BluetoothAct.class);
                startActivity(testIntent);
                return true;
            }
        });

    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }




}

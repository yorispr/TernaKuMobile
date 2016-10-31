package com.fintech.ternaku.Main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.fintech.ternaku.ListDetailTernak.ListDetailTernakMain;
import com.fintech.ternaku.Main.NavBar.BatasProduksiSusu.AddBatasProduksi;
import com.fintech.ternaku.Main.NavBar.Peternak.AddPeternak;
import com.fintech.ternaku.Main.NavBar.ProduksiSusu.AddProduksiSusu;
import com.fintech.ternaku.Main.NavBar.CalendarToDoList.CalendarToDoActivity;
import com.fintech.ternaku.Main.NavBar.Keuangan.AddKeuangan;
import com.fintech.ternaku.Main.NavBar.Ternak.InsertTernak;
import com.fintech.ternaku.Main.Pengingat.ShowReminderFragment;
import com.fintech.ternaku.RequestTransactionActivity;
import com.fintech.ternaku.Setting.SetPrefs;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.fintech.ternaku.LoginAndRegister.LoginActivity;
import com.fintech.ternaku.Main.Dashboard.DashboardFragment;
import com.fintech.ternaku.Main.Laporan.LaporanFragment;
import com.fintech.ternaku.Main.NavBar.Pakan.AddPakanTernak;
import com.fintech.ternaku.Main.TambahData.AddDataFragment;
import com.fintech.ternaku.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager_main_activity;
    private TabLayout tabLayout_main_activity;
    private FloatingToolbar mFloatingToolbar;
    private int opentabs = 0;
    private boolean flag_log_out=false;
    NavigationTabBar navigationTabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        //Drawer and Toolbar-------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //Set Toolbar Floating Button-----------------------------
        navigationTabBar = (NavigationTabBar) findViewById(R.id.navigation_main_activity);
        InitUI();


        //Shared Preferences------------------------------------
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE);
        View hView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        TextView nav_user = (TextView) hView.findViewById(R.id.drawer_title);
        nav_user.setText(sharedpreferences.getString("keyNama", null));
        TextView nav_role = (TextView) hView.findViewById(R.id.drawer_subtitle);
        nav_role.setText(sharedpreferences.getString("keyNamaRole", null));

        FirebaseMessaging.getInstance().subscribeToTopic(sharedpreferences.getString("keyIdPeternakan", null));

        //ViewPager and TabLayout-----------------------------
        viewPager_main_activity = (ViewPager)findViewById(R.id.viewpager_main_activity);
        setupViewPager(viewPager_main_activity);
        viewPager_main_activity.setCurrentItem(opentabs);
        tabLayout_main_activity = (TabLayout)findViewById(R.id.tabs_main_activity);
        tabLayout_main_activity.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout_main_activity.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout_main_activity.setupWithViewPager(viewPager_main_activity);

        tabLayout_main_activity.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0)
                {
                    getSupportActionBar().setTitle("TernaKu");
                }
                else if(tab.getPosition() == 1)
                {
                    getSupportActionBar().setTitle("Tambah Data");
                }
                else if(tab.getPosition() == 2)
                {
                    getSupportActionBar().setTitle("Pengingat");
                }
                else if(tab.getPosition() == 3)
                {
                    getSupportActionBar().setTitle("Laporan");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    public boolean getStatusLogOut(){
        return flag_log_out;
    }

    public void setFlag_log_out(boolean flag_log_out) {
        this.flag_log_out = flag_log_out;
    }

    public void InitUI()
    {
        final String[] colors = getResources().getStringArray(R.array.default_preview);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_cow),
                        Color.parseColor(colors[0]))
                        .title("Daftar Ternak")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_add),
                        Color.parseColor(colors[0]))
                        .title("Ternak")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_restaurant),
                        Color.parseColor(colors[0]))
                        .title("Pakan")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_filter),
                        Color.parseColor(colors[0]))
                        .title("Produksi Susu")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_calendar_month),
                        Color.parseColor(colors[0]))
                        .title("Kalender")
                        .build()
        );
        /*models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_calculator),
                        Color.parseColor(colors[0]))
                        .title("Keuangan")
                        .build()
        );*/
        navigationTabBar.setModels(models);
        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
                Intent i = new Intent();
                switch (navigationTabBar.getModelIndex()){
                    case 0 :
                        i = new Intent(MainActivity.this,ListDetailTernakMain.class);
                        startActivity(i);
                        break;
                    case 1 :
                        i = new Intent(MainActivity.this,InsertTernak.class);
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent(MainActivity.this,AddPakanTernak.class);
                        startActivity(i);
                        break;
                    case 3:
                        i = new Intent(MainActivity.this,AddProduksiSusu.class);
                        startActivity(i);
                        break;
                    case 4:
                        i = new Intent(MainActivity.this,CalendarToDoActivity.class);
                        startActivity(i);
                        break;
                    case 5:
                        i = new Intent(MainActivity.this,AddKeuangan.class);
                        startActivity(i);
                        break;
                }
            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        navigationTabBar.deselect();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_batas_produksi) {
            startActivity(new Intent(MainActivity.this,AddBatasProduksi.class));
        }else if(id == R.id.nav_add_peternak){
            startActivity(new Intent(MainActivity.this,AddPeternak.class));
        }else if(id == R.id.nav_tambah_pambayaran){
            startActivity(new Intent(MainActivity.this,RequestTransactionActivity.class));
        }else if(id == R.id.nav_add_keuangan){
            startActivity(new Intent(MainActivity.this,AddKeuangan.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DashboardFragment(), "FARMBOARD");
        adapter.addFrag(new AddDataFragment(), "TAMBAH DATA");
        adapter.addFrag(new ShowReminderFragment(), "PENGINGAT");
        adapter.addFrag(new LaporanFragment(), "LAPORAN");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_BACK:
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("Apakah Anda Yakin Ingin Keluar Dari Aplikasi Ini?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                finish();
                                System.exit(0);
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        })
                        .show();
                return true;
            case KeyEvent.KEYCODE_MENU:
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Peringatan!")
                        .setContentText("Apakah Anda Yakin Ingin Keluar?")
                        .setConfirmText("Ya")
                        .setCancelText("Tidak")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                                finish();
                                System.exit(0);
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        })
                        .show();
                return true;
        }

        return super.onKeyDown(keycode, e);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, SetPrefs.class);
            startActivity(i);
            return true;
        } else if(id == R.id.action_logout){
                FirebaseMessaging.getInstance().unsubscribeFromTopic(getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE).getString("keyIdPeternakan", null));
                SharedPreferences preferences = getSharedPreferences(getString(R.string.userpref), 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                finish();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
        } else if(id == R.id.action_pembayaran){
            Intent i = new Intent(MainActivity.this, RequestTransactionActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}

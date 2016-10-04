package com.fintech.ternaku.Main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.fintech.ternaku.Main.NavBar.AddProduksiSusu;
import com.fintech.ternaku.Main.NavBar.Peternak.AddPeternak;
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.fintech.ternaku.LoginAndRegister.LoginActivity;
import com.fintech.ternaku.Main.Dashboard.DashboardFragment;
import com.fintech.ternaku.Main.Laporan.LaporanFragment;
import com.fintech.ternaku.Main.NavBar.Pakan.AddPakanTernak;
import com.fintech.ternaku.Main.Pengingat.TasksFragment;
import com.fintech.ternaku.Main.TambahData.AddDataFragment;
import com.fintech.ternaku.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,FloatingToolbar.ItemClickListener,
        Toolbar.OnMenuItemClickListener,  FloatingToolbar.MorphListener {

    private ViewPager viewPager_main_activity;
    private TabLayout tabLayout_main_activity;
    private FloatingToolbar mFloatingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.buttonfloat_main_activity);
        mFloatingToolbar = (FloatingToolbar) findViewById(R.id.toolbarfloating_main_activity);
        mFloatingToolbar.setClickListener(this);
        mFloatingToolbar.attachFab(fab);
        mFloatingToolbar.addMorphListener(this);
        mFloatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                Intent i = new Intent();
                switch (item.getItemId()){
                    case R.id.action_add_pakan :
                        i = new Intent(MainActivity.this,AddPakanTernak.class);
                        startActivity(i);
                        break;
                    case R.id.action_add_produksisusu :
                        i = new Intent(MainActivity.this,AddProduksiSusu.class);
                        startActivity(i);
                        break;
                    case R.id.ic_action_add_ternak:
                        i = new Intent(MainActivity.this,AddPeternak.class);
                        startActivity(i);
                        break;

                }
            }

            @Override
            public void onItemLongClick(MenuItem item) {

            }
        });


        //Shared Preferences------------------------------------
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(getString(R.string.userpref), Context.MODE_PRIVATE);
        View hView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        TextView nav_user = (TextView) hView.findViewById(R.id.drawer_title);
        nav_user.setText(sharedpreferences.getString("keyNama", null));
        TextView nav_role = (TextView) hView.findViewById(R.id.drawer_subtitle);
        nav_role.setText(sharedpreferences.getString("keyNamaRole", null));

        //ViewPager and TabLayout-----------------------------
        viewPager_main_activity = (ViewPager)findViewById(R.id.viewpager_main_activity);
        setupViewPager(viewPager_main_activity);
        tabLayout_main_activity = (TabLayout)findViewById(R.id.tabs_main_activity);
        tabLayout_main_activity.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout_main_activity.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout_main_activity.setupWithViewPager(viewPager_main_activity);

        tabLayout_main_activity.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0)
                {
                    getSupportActionBar().setTitle("Ternaku");
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DashboardFragment(), "DASHBOARD");
        adapter.addFrag(new AddDataFragment(), "TAMBAH DATA");
        adapter.addFrag(new TasksFragment(), "PENGINGAT");
        adapter.addFrag(new LaporanFragment(), "LAPORAN");

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onItemClick(MenuItem item) {

    }

    @Override
    public void onItemLongClick(MenuItem item) {

    }

    @Override
    public void onMorphEnd() {

    }

    @Override
    public void onMorphStart() {

    }

    @Override
    public void onUnmorphStart() {

    }

    @Override
    public void onUnmorphEnd() {

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
            return true;
        } else if(id == R.id.action_logout){
            SharedPreferences preferences = getSharedPreferences(getString(R.string.userpref), 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            Toast.makeText(getApplicationContext(),"Log Out",Toast.LENGTH_LONG).show();

            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
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

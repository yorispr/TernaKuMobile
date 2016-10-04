package com.fintech.ternaku.DetailTernak;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fintech.ternaku.Connection;
import com.fintech.ternaku.DetailTernak.Event.AdapterDetailTernakEvent;
import com.fintech.ternaku.DetailTernak.Event.ModelDetailTernakEvent;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.fintech.ternaku.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailTernakMain extends AppCompatActivity {
    ExpandableRelativeLayout expander_profile_layout_fertility, expander_profile_layout_production, expander_profile_layout_health,
            expander_profile_layout_general;
    //General Profile Layout-------------------------------------------
    public TextView txt_detailternak_activity_umur_general;
    public TextView txt_detailternak_activity_tgllahir_general;
    public TextView txt_detailternak_activity_namakandang_general;
    public TextView txt_detailternak_activity_namakawanan_general;
    public TextView txt_detailternak_activity_tglpengelompokan_general;

    //Kesehatan Profile Layout-------------------------------------------
    public TextView txt_detailternak_activity_milkwithhold_kesehatan;
    public TextView txt_detailternak_activity_status_kesehatan;
    public TextView txt_detailternak_activity_diagnosisterakhir_kesehatan;
        //Cek Kesehatan--------------------------------------------------
    public TextView txt_detailternak_activity_jumlahcekkesehatan_kesehatan;
    public TextView txt_detailternak_activity_tglperiksakesehatanterakhir_kesehatan;
    public TextView txt_detailternak_activity_jumlahharicekkesehatanterakhir_kesehatan;
        //Cek Mastitis--------------------------------------------------
    public TextView txt_detailternak_activity_jumlahcekmastitis_kesehatan;
    public TextView txt_detailternak_activity_tglperiksamastitisterakhir_kesehatan;
    public TextView txt_detailternak_activity_jumlahharicekmastitisterakhir_kesehatan;
        //Cek Lameness--------------------------------------------------
    public TextView txt_detailternak_activity_jumlahceklameness_kesehatan;
    public TextView txt_detailternak_activity_tglperiksalamenessterakhir_kesehatan;
    public TextView txt_detailternak_activity_jumlahhariceklamenessterakhir_kesehatan;
        //Cek Potong Kuku--------------------------------------------------
    public TextView txt_detailternak_activity_jumlahcekpotongkuku_kesehatan;
    public TextView txt_detailternak_activity_tglperiksapotongkukuterakhir_kesehatan;
    public TextView txt_detailternak_activity_jumlahharicekpotongkukuterakhir_kesehatan;

    private String id_ternak="TRK-3",id_peternakan="FNT-P1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ternak_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
                    | ActionBar.DISPLAY_SHOW_TITLE
                    | ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //GetProfileData--------------------------------------------
        TextView input_detailternak_activity_idternak = (TextView)findViewById(R.id.input_detailternak_activity_idternak);
        input_detailternak_activity_idternak.setText(id_ternak);
        //GetEventData--------------------------------------------

        initUI();

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


    private void initUI() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_detailternak_activity);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                /*final View view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.layout_profile, null, false);
                container.addView(view);
                return view;*/
                /*LayoutInflater inflater = (LayoutInflater) container.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                int resId = 0;
                switch (position) {
                    case 0:
                        resId = R.layout.layout_profile;
                        break;
                    case 1:
                        resId = R.layout.layout_event;
                        break;
                    case 2:
                        resId = R.layout.layout_profile;
                        break;
                    case 3:
                        resId = R.layout.layout_event;
                        break;
                }

                View view = inflater.inflate(resId, null);
                container.addView(view, 0);
                return view;*/
                View parent_view = null;

                switch (position) {
                    case 0:
                        parent_view = getpageOne();
                        break;
                    case 1:
                        parent_view = getpageTwo();
                        break;
                    case 2:
                        parent_view = getpageThree();
                        break;
                    case 3:
                        parent_view = getpageFour();
                        break;
                }
                container.addView(parent_view, 0);
                return parent_view;
            }
        });

        final String[] colors = getResources().getStringArray(R.array.default_preview);
        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.navigation_detailternak_activity);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_cow),
                        Color.parseColor(colors[0]))
                        .title("Profile")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_second),
                        Color.parseColor(colors[0]))
                        .title("Event")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_seventh),
                        Color.parseColor(colors[0]))
                        .title("Susu")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fourth),
                        Color.parseColor(colors[0]))
                        .title("Task")
                        .badgeTitle("*")
                        .build()
        );
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = navigationTabBar.getModels().size()-1; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }

    //Set Page Profile-------------------------------------------------------------
    public View getpageOne(){
        View view = LayoutInflater.from(
                getBaseContext()).inflate(R.layout.layout_profile, null, false);


        //Set General-----
        txt_detailternak_activity_umur_general = (TextView) view.findViewById(R.id.txt_detailternak_activity_umur_general);
        txt_detailternak_activity_tgllahir_general = (TextView) view.findViewById(R.id.txt_detailternak_activity_tgllahir_general);
        txt_detailternak_activity_namakandang_general = (TextView) view.findViewById(R.id.txt_detailternak_activity_namakandang_general);
        txt_detailternak_activity_namakawanan_general = (TextView) view.findViewById(R.id.txt_detailternak_activity_namakawanan_general);
        txt_detailternak_activity_tglpengelompokan_general = (TextView) view.findViewById(R.id.txt_detailternak_activity_tglpengelompokan_general);
        //Set Kesehatan-----
        txt_detailternak_activity_milkwithhold_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_milkwithhold_kesehatan);
        txt_detailternak_activity_status_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_status_kesehatan);
        txt_detailternak_activity_diagnosisterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_diagnosisterakhir_kesehatan);

        txt_detailternak_activity_jumlahcekkesehatan_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahcekkesehatan_kesehatan);
        txt_detailternak_activity_tglperiksakesehatanterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_tglperiksakesehatanterakhir_kesehatan);
        txt_detailternak_activity_jumlahharicekkesehatanterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahharicekkesehatanterakhir_kesehatan);

        txt_detailternak_activity_jumlahcekmastitis_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahcekmastitis_kesehatan);
        txt_detailternak_activity_tglperiksamastitisterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_tglperiksamastitisterakhir_kesehatan);
        txt_detailternak_activity_jumlahharicekmastitisterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahharicekmastitisterakhir_kesehatan);

        txt_detailternak_activity_jumlahceklameness_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahceklameness_kesehatan);
        txt_detailternak_activity_tglperiksalamenessterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_tglperiksalamenessterakhir_kesehatan);
        txt_detailternak_activity_jumlahhariceklamenessterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahhariceklamenessterakhir_kesehatan);

        txt_detailternak_activity_jumlahcekpotongkuku_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahcekpotongkuku_kesehatan);
        txt_detailternak_activity_tglperiksapotongkukuterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_tglperiksapotongkukuterakhir_kesehatan);
        txt_detailternak_activity_jumlahharicekpotongkukuterakhir_kesehatan = (TextView) view.findViewById(R.id.txt_detailternak_activity_jumlahharicekpotongkukuterakhir_kesehatan);

        //Get Data Profile--------------------------------------------
        String urlParameters = "id_peternakan=" + id_peternakan.trim() + "&id_ternak=" + id_ternak.trim();
        new getDataProfile().execute("http://ternaku.com/index.php/C_Ternak/GetTernakUmum", urlParameters);

        Button btnFertility=(Button)view.findViewById(R.id.btnFertility);
        expander_profile_layout_fertility = (ExpandableRelativeLayout) view.findViewById(R.id.fertilityLayout);

        btnFertility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expander_profile_layout_fertility.toggle();
            }
        });

        Button btnProduction=(Button)view.findViewById(R.id.btnProduction);
        expander_profile_layout_production = (ExpandableRelativeLayout) view.findViewById(R.id.productionLayout);

        btnProduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expander_profile_layout_production.toggle();
            }
        });

        Button btnHealth=(Button)view.findViewById(R.id.btnHealth);
        expander_profile_layout_health = (ExpandableRelativeLayout) view.findViewById(R.id.healthLayout);

        btnHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expander_profile_layout_health.toggle();
            }
        });

        Button btnGeneral=(Button)view.findViewById(R.id.btnGeneral);
        expander_profile_layout_general = (ExpandableRelativeLayout) view.findViewById(R.id.generalLayout);

        btnGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expander_profile_layout_general.toggle();

            }
        });

        return view;
    }

    private class getDataProfile extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Connection c = new Connection();
            String json = c.GetJSONfromURL(params[0], params[1]);
            return json;
        }

        @Override
        protected void onPostExecute(String res) {
            Log.d("Detail_Ternak_Json",res);
            SetTernakDetail(res);
        }
    }

    private void SetTernakDetail(String result){
        Log.d("Ternak2",result);

        try {
            JSONArray jArray = new JSONArray(result);
            JSONObject jObj = jArray.getJSONObject(0);

            JSONArray cekKehamilan_arr = jObj.getJSONArray("cekKehamilan");
            JSONArray getTernakUmum_arr = jObj.getJSONArray("getTernakUmum");
            JSONArray countTernakMastitis_arr = jObj.getJSONArray("countTernakMastitis");
            JSONArray countTernakLameness_arr = jObj.getJSONArray("countTernakLameness");
            JSONArray countTernakKuku_arr = jObj.getJSONArray("countTernakKuku");
            JSONArray countTernakSehat_arr = jObj.getJSONArray("countTernakSehat");

            JSONObject obj_cekKehamilan = cekKehamilan_arr.getJSONObject(0);
            JSONObject obj_getTernakUmum = getTernakUmum_arr.getJSONObject(0);
            JSONObject obj_countTernakMastitis = countTernakMastitis_arr.getJSONObject(0);
            JSONObject obj_countTernakLameness = countTernakLameness_arr.getJSONObject(0);
            JSONObject obj_countTernakKuku = countTernakKuku_arr.getJSONObject(0);
            JSONObject obj_countTernakSehat = countTernakSehat_arr.getJSONObject(0);

            //General Profile Layout-------------------------------------------
            if(nullHandle(obj_getTernakUmum, "year").equalsIgnoreCase("N/A")||
                    nullHandle(obj_getTernakUmum, "month").equalsIgnoreCase("N/A")||
                    nullHandle(obj_getTernakUmum, "day").equalsIgnoreCase("N/A")){
                txt_detailternak_activity_umur_general.setText("N/A");
            }else{
                txt_detailternak_activity_umur_general.setText(nullHandle(obj_getTernakUmum, "year") + " y" +
                        nullHandle(obj_getTernakUmum, "month") + " m" +
                        nullHandle(obj_getTernakUmum, "day") + " d");
            }
            txt_detailternak_activity_tgllahir_general.setText(nullHandle(obj_getTernakUmum, "tgl_lahir"));
            txt_detailternak_activity_namakandang_general.setText(nullHandle(obj_getTernakUmum, "kandang"));
            txt_detailternak_activity_namakawanan_general.setText(nullHandle(obj_getTernakUmum, "kawanan"));
            txt_detailternak_activity_tglpengelompokan_general.setText(nullHandle(obj_getTernakUmum, "Tglpengelompokan"));

            //Kesehatan Profile Layout-------------------------------------------
            txt_detailternak_activity_milkwithhold_kesehatan.setText("N/A");
            txt_detailternak_activity_status_kesehatan.setText("N/A");
            txt_detailternak_activity_diagnosisterakhir_kesehatan.setText("N/A");
            //Cek Kesehatan--------------------------------------------------
            txt_detailternak_activity_jumlahcekkesehatan_kesehatan.setText(nullHandle(obj_countTernakMastitis, "berapakali sakit mastitis"));
            txt_detailternak_activity_tglperiksakesehatanterakhir_kesehatan.setText(nullHandle(obj_countTernakMastitis, "tgl_menderita_terakhir"));
            txt_detailternak_activity_jumlahharicekkesehatanterakhir_kesehatan.setText(nullHandle(obj_countTernakMastitis, "lama menderita"));
            //Cek Mastitis--------------------------------------------------
            txt_detailternak_activity_jumlahcekmastitis_kesehatan.setText(nullHandle(obj_countTernakLameness, "berapakali sakit lamenes"));
            txt_detailternak_activity_tglperiksamastitisterakhir_kesehatan.setText(nullHandle(obj_countTernakLameness, "tgl_menderita_terakhir"));
            txt_detailternak_activity_jumlahharicekmastitisterakhir_kesehatan.setText(nullHandle(obj_countTernakLameness, "lama menderita"));
            //Cek Lameness--------------------------------------------------
            txt_detailternak_activity_jumlahceklameness_kesehatan.setText(nullHandle(obj_countTernakSehat, "berapakali dicek kesehatan"));
            txt_detailternak_activity_tglperiksalamenessterakhir_kesehatan.setText(nullHandle(obj_countTernakSehat, "tgl_periksa_terakhir"));
            txt_detailternak_activity_jumlahhariceklamenessterakhir_kesehatan.setText(nullHandle(obj_countTernakSehat, "lama pemeriksaan"));
            //Cek Potong Kuku--------------------------------------------------
            txt_detailternak_activity_jumlahcekpotongkuku_kesehatan.setText(nullHandle(obj_countTernakKuku, "berapakali sakit kuku"));
            txt_detailternak_activity_tglperiksapotongkukuterakhir_kesehatan.setText(nullHandle(obj_countTernakKuku, "tgl_menderita_terakhir"));
            txt_detailternak_activity_jumlahharicekpotongkukuterakhir_kesehatan.setText(nullHandle(obj_countTernakKuku, "lama menderita"));

        } catch (JSONException e){
            e.printStackTrace();
        }
    }
    public static String nullHandle(JSONObject json, String key) throws JSONException {
        if (json.isNull(key))
            return "N/A";
        else{
            return json.getString(key);
        }
    }
    public static int nullHandleArray(JSONArray json) {
        if (json.isNull(0))
            return 0;
        else{
            return 1;
        }
    }
    //Set Page Profile-------------------------------------------------------------

    //Set Page Event-------------------------------------------------------------
    public View getpageTwo() {
        View view = LayoutInflater.from(
                getBaseContext()).inflate(R.layout.layout_event, null, false);
        final ListView lvItems = (ListView) view.findViewById(R.id.lv_items);

        AdapterDetailTernakEvent adapter = getAdapter();

        lvItems.setAdapter(adapter);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdapterDetailTernakEvent adapter = (AdapterDetailTernakEvent) parent.getAdapter();

                ModelDetailTernakEvent item = (ModelDetailTernakEvent) adapter.getItem(position);
                if (item != null) {
                    if (item.isExpanded) {
                        item.isExpanded = false;
                    } else {
                        item.isExpanded = true;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }
    private AdapterDetailTernakEvent getAdapter(){

        List<ModelDetailTernakEvent> items = new ArrayList<>();

        for(int i = 0; i < 50; i++){
            ModelDetailTernakEvent item = new ModelDetailTernakEvent();
            item.title = "Title Item " + i;
            item.desciption = "Description for Title Item "+ i;
            item.isExpanded = false;

            items.add(item);
        }
        return new AdapterDetailTernakEvent(this, items);
    }
    //Set Page Event-------------------------------------------------------------

    //Set Page Milk-------------------------------------------------------------
    public View getpageThree() {
        View view = LayoutInflater.from(
                getBaseContext()).inflate(R.layout.layout_susu, null, false);
        return view;
    }
    //Set Page Milk-------------------------------------------------------------

    //Set Page Task-------------------------------------------------------------
    public View getpageFour() {
        View view = LayoutInflater.from(
                getBaseContext()).inflate(R.layout.layout_task, null, false);
        return view;
    }
    //Set Page Task-------------------------------------------------------------
}

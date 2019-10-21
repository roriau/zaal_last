package mn.zaal.zaal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import mn.zaal.zaal.adapters.TabAdapter;
import mn.zaal.zaal.models.zaalModel;
import mn.zaal.zaal.tuslah.DatabaseJson;
import mn.zaal.zaal.tuslah.Functions;
import mn.zaal.zaal.tuslah.SessionManager;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    TabAdapter adapter;
    SessionManager sessionManager;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.ic_assignment,
            R.drawable.ic_shopping
    };
    private DatabaseJson databaseJson;
    private ArrayList<zaalModel> modelsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        sessionManager = new SessionManager(this);
        databaseJson = new DatabaseJson(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        Intent intent = MainActivity.this.getIntent();
        int fragmentInt = intent.getIntExtra("Identifier", 0);
        if (fragmentInt >= 1){
            switch (fragmentInt){
                case 1:
                    viewPager.setCurrentItem(0, true);
                    break;
                case 2:
                    viewPager.setCurrentItem(1, true);
                    break;
                case 3:
                    viewPager.setCurrentItem(2, true);
                    break;
            }
        }
        tab();
        int dbCount = databaseJson.getRowCount();
        getCount(dbCount);
        if (sessionManager.isLoggedin()){
            createAccountHeader();
        } else {
            createGuestAccountHeader();
        }

        if (sessionManager.isLoggedin()) {
            result = new DrawerBuilder(this)
                    .withToolbar(toolbar)
                    .withAccountHeader(headerResult)
                    .withHeaderHeight(DimenHolder.fromDp(200))
                    .withHeaderDivider(false)
                    .withTranslucentStatusBar(false)
                    .withActionBarDrawerToggle(true)
                    .withDrawerWidthDp(250)
                    .withActionBarDrawerToggleAnimated(true)
                    .withInnerShadow(false)
                    .withSliderBackgroundColor(Color.WHITE)
                    .withActionBarDrawerToggle(true)
                    .withTranslucentNavigationBar(false)
                    .withOnDrawerListener(new Drawer.OnDrawerListener() {
                        @Override
                        public void onDrawerOpened(View drawerView) {

                        }

                        @Override
                        public void onDrawerClosed(View drawerView) {
                        }

                        @Override
                        public void onDrawerSlide(View drawerView, float slideOffset) {
                        }
                    })

                    .addDrawerItems(initDrawerItems())
                    .withSavedInstance(savedInstanceState)
                    .withSelectedItem(-1)
                    .withDrawerGravity(Gravity.START)
                    .addStickyDrawerItems(new SecondaryDrawerItem().withIdentifier(7).withName("Гарах").withIcon(FontAwesome.Icon.faw_lock).withSelectable(false))
                    .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                        if (drawerItem != null) {
                            switch ((int) drawerItem.getIdentifier()) {
                                case 1:
                                    Intent homeIntent = new Intent(MyApplication.getAppContext(), MainActivity.class);
                                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    MyApplication.getAppContext().startActivity(homeIntent);
                                    finish();
                                    break;
                                case 2:

                                    break;
                                case 3:
                                    if (sessionManager.isLoggedin()) {
                                        Intent intent1 = new Intent(MyApplication.getAppContext(), UserDetailActivity.class);
                                        MyApplication.getAppContext().startActivity(intent1);
                                    } else {
                                        Intent intent1 = new Intent(MyApplication.getAppContext(), LoginActivity.class);
                                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        MyApplication.getAppContext().startActivity(intent1);
                                    }
                                    break;
                                case 4:
                                    if (viewPager != null) {
                                        viewPager.setCurrentItem(2, true);
                                    }
                                    break;
                                case 5:

                                    break;
                                case 6:

                                    break;
                                case 7:
                                    sessionManager.setIsLoggedin(false);
                                    databaseJson.resetTables();
                                    Intent logoutIntent = new Intent(MyApplication.getAppContext(), MainActivity.class);
                                    logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getApplicationContext().startActivity(logoutIntent);
                                    break;
                            }
                        }
                        return false;
                    })
                    .build();
        } else {
            result = new DrawerBuilder(this)
                    .withToolbar(toolbar)
                    .withAccountHeader(headerResult)
                    .withHeaderHeight(DimenHolder.fromDp(200))
                    .withHeaderDivider(false)
                    .withTranslucentStatusBar(false)
                    .withActionBarDrawerToggle(true)
                    .withDrawerWidthDp(250)
                    .withActionBarDrawerToggleAnimated(true)
                    .withInnerShadow(false)
                    .withSliderBackgroundColor(Color.WHITE)
                    .withActionBarDrawerToggle(true)
                    .withTranslucentNavigationBar(false)
                    .withOnDrawerListener(new Drawer.OnDrawerListener() {
                        @Override
                        public void onDrawerOpened(View drawerView) {

                        }

                        @Override
                        public void onDrawerClosed(View drawerView) {
                        }

                        @Override
                        public void onDrawerSlide(View drawerView, float slideOffset) {
                        }
                    })

                    .addDrawerItems(initDrawerItems())
                    .withSavedInstance(savedInstanceState)
                    .withSelectedItem(-1)
                    .withDrawerGravity(Gravity.START)
                    .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                        if (drawerItem != null) {
                            switch ((int) drawerItem.getIdentifier()) {
                                case 1:
                                    if (viewPager != null) {
                                        viewPager.setCurrentItem(0, true);
                                    } else {
                                        Intent homeIntent = new Intent(MyApplication.getAppContext(), MainActivity.class);
                                        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        MyApplication.getAppContext().startActivity(homeIntent);
                                        finish();
                                    }

                                    break;
                                case 2:
                                    if (viewPager!=null) {
                                        viewPager.setCurrentItem(1,true);
                                    } else {
                                        Intent zaalIntent = new Intent(MyApplication.getAppContext(), MainActivity.class);
                                        zaalIntent.putExtra("FragmentIdentifier",1);
                                        MyApplication.getAppContext().startActivity(zaalIntent);
                                    }
                                    break;
                                case 3:
                                    if (sessionManager.isLoggedin()) {
                                        Intent intent1 = new Intent(MyApplication.getAppContext(), UserDetailActivity.class);
                                        MyApplication.getAppContext().startActivity(intent1);
                                    } else {
                                        Intent intent1 = new Intent(MyApplication.getAppContext(), LoginActivity.class);
                                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        MyApplication.getAppContext().startActivity(intent1);
                                    }
                                    break;
                                case 4:
                                    if (viewPager != null) {
                                        viewPager.setCurrentItem(2, true);
                                    }
                                    break;
                                case 5:

                                    break;
                                case 6:

                                    break;
                                case 7:
                                    sessionManager.setIsLoggedin(false);
                                    databaseJson.resetTables();
                                    Intent logoutIntent = new Intent(MyApplication.getAppContext(), MainActivity.class);
                                    logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getApplicationContext().startActivity(logoutIntent);
                                    break;
                            }
                        }
                        return false;
                    })
                    .build();
        }
    }

    private void createAccountHeader() {
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTextColorRes(R.color.material_drawer_dark_header_selection_text)
                .withHeaderBackground(R.drawable.header)
                .withDividerBelowHeader(true)
                .addProfiles(new ProfileDrawerItem().withName("Temuujin").withEmail("roriau@gmail.com").withIcon(getResources().getDrawable(R.drawable.ic_account_circle)))
                .build();
    }

    private void createGuestAccountHeader() {
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTextColorRes(R.color.material_drawer_dark_header_selection_text)
                .withHeaderBackground(R.drawable.header)
                .withDividerBelowHeader(true)
                .build();
    }

    @NonNull
    private IDrawerItem[] initDrawerItems() {
        if (sessionManager.isLoggedin()) {
            return new IDrawerItem[]{new PrimaryDrawerItem().withIdentifier(1).withName("Нүүр").withSetSelected(true).withIcon(GoogleMaterial.Icon.gmd_home),
                    new PrimaryDrawerItem().withIdentifier(2).withName("Заалны жагсаалт").withSetSelected(true).withIcon(GoogleMaterial.Icon.gmd_list),
                    new PrimaryDrawerItem().withIdentifier(3).withName("Миний Бүртгэл").withSetSelected(false).withIcon(GoogleMaterial.Icon.gmd_account_circle),
                    new PrimaryDrawerItem().withIdentifier(4).withName("Таны захиалга").withSetSelected(false).withIcon(GoogleMaterial.Icon.gmd_shopping_cart),
            };
        } else {
            return new IDrawerItem[]{new PrimaryDrawerItem().withIdentifier(1).withName("Нүүр").withSetSelected(true).withIcon(GoogleMaterial.Icon.gmd_home),
                    new PrimaryDrawerItem().withIdentifier(2).withName("Заалны жагсаалт").withSetSelected(true).withIcon(GoogleMaterial.Icon.gmd_list),
                    new PrimaryDrawerItem().withIdentifier(3).withName("Нэвтрэх").withSetSelected(false).withIcon(GoogleMaterial.Icon.gmd_account_circle)
            };
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            if (doubleBackToExitPressedOnce)
                super.onBackPressed();
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Дахин товшиж гарна уу", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
        }
    }
    public void tab(){
        tabLayout.setTabTextColors(getResources().getColor(R.color.md_grey_100),getResources().getColor(R.color.md_orange_300));
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(tabIcons[0]);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(tabIcons[1]);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(tabIcons[2]);

    }

    public void setupViewPager(ViewPager viewPager){
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragOne(),"Нүүр");
        adapter.addFragment(new FragTwo(), "Заал");
        adapter.addFragment(new FragThree(), "Захиалга");
        viewPager.setAdapter(adapter);
    }

    void getData() {
        StringRequest request2 = new StringRequest(Request.Method.GET, Functions.GET_ZAAL, (String response) -> {
            Timber.d(response);
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    zaalModel model;
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String name = obj.getString("name");
                    String description = obj.getString("description");
                    String city = obj.getString("city");
                    String district = obj.getString("district");
                    String khoroo = obj.getString("khoroo");
                    String address = obj.getString("address");
                    String price_tal = obj.getString("price_tal");
                    String price_buten = obj.getString("price_buten");
                    String phone = obj.getString("phone");
                    String map_lat = obj.getString("map_lat");
                    String map_lng = obj.getString("map_lng");
                    String zaal_pic = obj.getString("zaal_pic");
                    model = new zaalModel(name,description,city,district,khoroo,address,price_tal,price_buten,phone,map_lat,map_lng,zaal_pic);
                    modelsList.add(model);
                    databaseJson.addZaal(modelsList);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                }

        }, error -> Timber.e(error.toString())) {
            };
        MyApplication.getInstance().addToRequestQueue(request2);
    }

    void getCount(int dbCount) {
        StringRequest request2 = new StringRequest(Request.Method.GET, Functions.COUNTDB_URL, (String response) -> {
            Timber.d(response);
            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject obj = jsonArray.getJSONObject(0);
                int count = Integer.valueOf(obj.getString("count"));
                if (dbCount != count) {
                    databaseJson.resetTables();
                    getData();
                }
                System.out.println(dbCount + " WHO IS THE WINNER ? " + count);

            } catch (JSONException e) {
                e.printStackTrace();
                }

        }, error -> Timber.e(error.toString())) {
            };
        MyApplication.getInstance().addToRequestQueue(request2);
    }
}

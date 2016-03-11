package xyz.igorgee.imagecreator3d;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;

import static xyz.igorgee.utilities.UIUtilities.makeSnackbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MY_PREF_NAME = "MyPrefsFile";
    public static final String ACCESS_TOKEN_VALUE = "accessTokenValue";
    public static final String ACCESS_TOKEN_SECRET = "accessTokenSecret";

    @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;
    @Bind(R.id.rootLayout) CoordinatorLayout rootLayout;
    @Bind(R.id.toolBar) Toolbar toolbar;
    @Bind(R.id.navigation) NavigationView navigation;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initializeInstances();

        chooseAppropriateFragment();
    }

    private void chooseAppropriateFragment() {
        SharedPreferences preferences = getSharedPreferences(MY_PREF_NAME, MODE_PRIVATE);
        String accessTokenValue = preferences.getString(ACCESS_TOKEN_VALUE, null);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if (accessTokenValue == null) {
            fragmentTransaction.add(R.id.fragmentPlaceholder, new LogInFragment());
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            fragmentTransaction.add(R.id.fragmentPlaceholder, new HomePageFragment());
        }

        fragmentTransaction.commit();
    }

    private void initializeInstances() {
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,
                R.string.drawer_open_desc, R.string.drawer_close_desc);
        drawerLayout.setDrawerListener(drawerToggle);
        navigation.setNavigationItemSelectedListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.spades:
                makeSnackbar(rootLayout, R.string.spades_string);
                break;
            case R.id.hearts:
                makeSnackbar(rootLayout, R.string.hearts_string);
                break;
            case R.id.clovers:
                makeSnackbar(rootLayout, R.string.clovers_string);
                break;
            case R.id.diamonds:
                makeSnackbar(rootLayout, R.string.diamonds_string);
                break;
            case R.id.custom:
                makeSnackbar(rootLayout, R.string.custom_string);
                break;
        }

        return false;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Changes home icon from "back" to "hamburger"
        drawerToggle.syncState();
    }
}

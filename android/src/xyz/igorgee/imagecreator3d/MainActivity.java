package xyz.igorgee.imagecreator3d;

import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MY_PREF_NAME = "MyPrefsFile";
    public static final String ACCESS_TOKEN_VALUE = "accessTokenValue";
    public static final String ACCESS_TOKEN_SECRET = "accessTokenSecret";
    public static final CharSequence[] SOCIAL_MEDIA_PLATFORMS = new CharSequence[]{"Facebook"};

    @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;
    @Bind(R.id.rootLayout) CoordinatorLayout rootLayout;
    @Bind(R.id.toolBar) Toolbar toolbar;
    @Bind(R.id.navigation) NavigationView navigation;
    public static ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initializeInstances();

        chooseAppropriateFragment(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    private void chooseAppropriateFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragmentPlaceholder, new HomePageFragment(), "Home");
            fragmentTransaction.commit();
        } else {
            getFragmentManager().findFragmentByTag("Home");
        }
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
                if (drawerToggle.isDrawerIndicatorEnabled()) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    onBackPressed();
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.cart_browser:
                String urlString="https://shapeways.com/shops/carbonizr";
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    this.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    this.startActivity(intent);
                }
                break;
            case R.id.faq:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentPlaceholder, new FAQFragment(), "FAQ")
                        .addToBackStack(null)
                        .commit();
                drawerLayout.closeDrawers();
                drawerToggle.setDrawerIndicatorEnabled(false);
        }

        return false;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Changes home icon from "back" to "hamburger"
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}

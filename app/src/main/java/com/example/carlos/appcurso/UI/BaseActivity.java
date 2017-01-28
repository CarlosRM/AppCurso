package com.example.carlos.appcurso.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.carlos.appcurso.R;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    Toolbar toolbar;
    Fragment currentFragment;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initializeViews();

        if (savedInstanceState != null) {
            currentFragment = getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
        } else {
            Calculator calculator = new Calculator();
            currentFragment = calculator;
        }
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, currentFragment);
        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }

    public void initializeViews() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "fragment", currentFragment);
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
        getMenuInflater().inflate(R.menu.base, menu);
        settings = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        Log.d("PREMENU",Boolean.toString(settings.getBoolean("toast",false)));
        menu.getItem(0).setChecked(settings.getBoolean("toast",false));
        menu.getItem(1).setChecked(settings.getBoolean("snackbar",false));
        Log.d("PREMENU",Boolean.toString(settings.getBoolean("snackbar",false)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        settings = getSharedPreferences("Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_toast) {
            if (item.isChecked()) {
                item.setChecked(false);
                editor.putBoolean("toast",false);
            } else {
                item.setChecked(true);
                editor.putBoolean("toast",true);
            }
            editor.apply();
            return true;
        } else if (id == R.id.action_snackbar) {
            if (item.isChecked()) {
                item.setChecked(false);
                editor.putBoolean("snackbar",false);
            } else {
                item.setChecked(true);
                editor.putBoolean("snackbar",true);
            }
            editor.apply();
            Log.d("AfterSnack",Boolean.toString(settings.getBoolean("snackbar",false)));
            return true;
        } else if (id == R.id.browser_button) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

package com.example.carlos.appcurso.UI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.carlos.appcurso.Data.DBHelper;
import com.example.carlos.appcurso.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "AzDP9maJ0XkOKimPloA1KT9Rr";
    private static final String TWITTER_SECRET = "omJZYYmEXCyDb9z4i4sGWVMQOGIcBzddyDX0cau1MPmtqfzvk6";


    NavigationView navigationView;
    Toolbar toolbar;
    Fragment currentFragment;
    String currentTag;
    SharedPreferences settings;
    String currentUser;
    TextView headerText;
    DBHelper helper;
    SQLiteDatabase database;

    public String getCurrentUser() {
        return currentUser;
    }

    @Override
    public void onNewIntent(Intent intent) {
        /*String fromNotification = intent.getStringExtra("fragmentToOpen");
        if(fromNotification != null
        ) {
            Log.d("GOTINTENT2","GOTINTENT2");
            Fragment playerFragment = getSupportFragmentManager().findFragmentByTag("MusicPlayer");
            if(playerFragment!=null){
                currentFragment = playerFragment;
                currentTag = "MusicPlayer";
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer,currentFragment,currentTag);
                fragmentTransaction.addToBackStack(currentTag);
                fragmentTransaction.commit();
            }
        }*/
        //String fromNotification = intent.getStringExtra("fragmentToOpen");
        //Toast.makeText(this,fromNotification,Toast.LENGTH_SHORT).show();
        setIntent(intent);
    }

    @Override
    public  void onResume(){
        String fromNotification = getIntent().getStringExtra("fragmentToOpen");
        if(fromNotification!=null && fromNotification.equals("MusicPlayer")) {
            Fragment playerFragment = getSupportFragmentManager().findFragmentByTag("MusicPlayer");
            if(playerFragment!=null){
                currentFragment = playerFragment;
                currentTag = "MusicPlayer";
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer,currentFragment,currentTag);
                fragmentTransaction.addToBackStack(currentTag);
                fragmentTransaction.commit();
                navigationView.setCheckedItem(R.id.nav_music_player);
            } else {
                MusicPlayer musicF = new MusicPlayer();
                musicF.setCurrentIndex(getIntent().getIntExtra("currentIndex",0));
                musicF.setIsPlaying(getIntent().getBooleanExtra("isPlaying",false));
                currentFragment = musicF;
                currentTag = "MusicPlayer";
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer,currentFragment,currentTag);
                fragmentTransaction.addToBackStack(currentTag);
                fragmentTransaction.commit();
                navigationView.setCheckedItem(R.id.nav_music_player);
            }
            getIntent().removeExtra("fragmentToOpen");
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        if(Build.VERSION.SDK_INT >= 23) {

            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        0);
            }
        }
        setContentView(R.layout.activity_base);
        initializeViews();

        if (savedInstanceState != null) {
            currentFragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");
            currentTag = savedInstanceState.getString("currentTag");
        } else {
            Calculator calculator = new Calculator();
            currentFragment = calculator;
            currentTag = "Calculator";
        }

        /*String fromNotification = getIntent().getStringExtra("fragmentToOpen");
        if(fromNotification != null) {
            Log.d("GOTINTENT","GOTINTENT");
            Fragment playerFragment = getSupportFragmentManager().findFragmentByTag("MusicPlayer");
            if(playerFragment!=null){
                currentFragment = playerFragment;
                currentTag = "MusicPlayer";
            }
        }*/

        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, currentFragment, currentTag);
        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }

    public void initializeViews() {
        Intent intent = getIntent();
        currentUser = intent.getStringExtra("username");

        helper = new DBHelper(this);
        database = helper.getReadableDatabase();
        if(!helper.userExists(database,currentUser)) helper.insertUser(database,currentUser);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        headerText = (TextView) header.findViewById(R.id.header_text);
        headerText.setText(currentUser);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState,"currentFragment",currentFragment);
        outState.putString("currentTag",currentTag);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            //super.onBackPressed();
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

            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            Fragment calculatorFragment = getSupportFragmentManager().findFragmentByTag("Calculator");
            if(calculatorFragment!=null) {
                Log.d("FOUNDCALCULATOR","FOUNDCALCULATOR");
                currentFragment = calculatorFragment;
            } else {
                currentFragment = new Calculator();
            }
            currentTag = "Calculator";
            fragmentTransaction.replace(R.id.fragmentContainer,currentFragment,currentTag);
            fragmentTransaction.addToBackStack(currentTag);
            fragmentTransaction.commit();
        } else if(id == R.id.nav_music_player) {

            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            Fragment musicFragment = getSupportFragmentManager().findFragmentByTag("MusicPlayer");
            if(musicFragment != null){
                Log.d("FOUNDMUSIC","FOUNDMUSIC");
                currentFragment = musicFragment;
            } else {
                currentFragment = new MusicPlayer();
            }
            currentTag = "MusicPlayer";
            fragmentTransaction.replace(R.id.fragmentContainer,currentFragment,currentTag);
            fragmentTransaction.addToBackStack(currentTag);
            fragmentTransaction.commit();
        } else if(id == R.id.nav_ranking) {
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            /*Fragment rankingFragment = getSupportFragmentManager().findFragmentByTag("RankingContainer");
            if(rankingFragment != null){
                currentFragment = rankingFragment;
            } else {
                currentFragment = new RankingContainer();
            }
            currentTag = "RankingContainer";*/
            currentFragment = new RankingContainer();
            currentTag = "RankingContainer";
            fragmentTransaction.replace(R.id.fragmentContainer, currentFragment, currentTag);
            //fragmentTransaction.addToBackStack(currentTag);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_memory) {
            /*android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            currentFragment = new Memory6();
            currentTag = "Memory";
            fragmentTransaction.replace(R.id.fragmentContainer, currentFragment, currentTag);
            //fragmentTransaction.addToBackStack(currentTag);
            fragmentTransaction.commit();*/
            CharSequence colors[] = new CharSequence[] {"4x4", "6x6"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose size");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0) {
                        android.support.v4.app.FragmentTransaction fragmentTransaction =
                                getSupportFragmentManager().beginTransaction();
                        currentFragment = new Memory();
                        currentTag = "Memory";
                        fragmentTransaction.replace(R.id.fragmentContainer, currentFragment, currentTag);
                        fragmentTransaction.commit();
                    } else {
                        android.support.v4.app.FragmentTransaction fragmentTransaction =
                                getSupportFragmentManager().beginTransaction();
                        Memory6 mem = new Memory6();
                        currentFragment = mem;
                        currentTag = "Memory";
                        fragmentTransaction.replace(R.id.fragmentContainer, currentFragment, currentTag);
                        fragmentTransaction.commit();
                    }
                }
            });
            builder.show();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

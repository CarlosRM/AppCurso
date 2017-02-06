package com.example.carlos.appcurso.UI;

import android.Manifest;
import android.app.NotificationManager;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carlos.appcurso.Data.DBHelper;
import com.example.carlos.appcurso.Data.User;
import com.example.carlos.appcurso.Domain.MusicService;
import com.example.carlos.appcurso.R;
import com.squareup.picasso.Picasso;
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
    ImageView headerImage;

    public String getCurrentUser() {
        return currentUser;
    }

    @Override
    public void onNewIntent(Intent intent) {
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

        int permission;
        if(Build.VERSION.SDK_INT >= 23) {
            permission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if(permission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
        setContentView(R.layout.activity_base);
        initializeViews();

        if (savedInstanceState != null) {
            currentFragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");
            currentTag = savedInstanceState.getString("currentTag");
        } else {
            Help help = new Help();
            currentFragment = help;
            currentTag = "Help";
        }

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

        settings = getSharedPreferences("Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        User aux = helper.getUser(database,currentUser);
        editor.putBoolean("toast",aux.getToast());
        editor.putBoolean("status",aux.getStatus());
        editor.apply();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        headerText = (TextView) header.findViewById(R.id.header_text);
        headerText.setText(currentUser);
        headerImage = (ImageView) header.findViewById(R.id.headerImage);

        User auxuser = helper.getUser(database,currentUser);
        String auxuri = auxuser.getImage();
        if(auxuri == null) {
            Picasso.with(this).load(R.drawable.default_avatar).resize(100,100).centerCrop().into(headerImage);
        } else {
            Uri uri = Uri.parse(auxuri);
            if(Build.VERSION.SDK_INT >= 19) {
                this.getContentResolver().takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            Picasso.with(this).load(uri).resize(100,100).centerCrop().into(headerImage);
        }

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

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base, menu);
        settings = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        Log.d("PREMENU",Boolean.toString(settings.getBoolean("toast",false)));
        menu.getItem(0).setChecked(settings.getBoolean("toast",false));
        menu.getItem(1).setChecked(settings.getBoolean("status",false));
        Log.d("PREMENU",Boolean.toString(settings.getBoolean("status",false)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        settings = getSharedPreferences("Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_toast) {
            if (item.isChecked()) {
                item.setChecked(false);
                editor.putBoolean("toast",false);
                helper.updatePreference(database,currentUser,"toast",false);
            } else {
                item.setChecked(true);
                editor.putBoolean("toast",true);
                helper.updatePreference(database,currentUser,"toast",true);
            }
            editor.apply();
            return true;
        } else if (id == R.id.action_status) {
            if (item.isChecked()) {
                item.setChecked(false);
                editor.putBoolean("status",false);
                helper.updatePreference(database,currentUser,"status",false);
            } else {
                item.setChecked(true);
                editor.putBoolean("status",true);
                helper.updatePreference(database,currentUser,"status",true);
            }
            editor.apply();
            Log.d("AfterSnack",Boolean.toString(settings.getBoolean("status",false)));
            return true;
        } else if (id == R.id.browser_button) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
            startActivity(intent);
        } else if (id == R.id.logout_icon) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:

                            stopService(new Intent(getApplicationContext(), MusicService.class));
                            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.cancelAll();
                            SharedPreferences settings = getSharedPreferences("Preferences", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("logged", false);
                            editor.apply();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to log out?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            Fragment calculatorFragment = getSupportFragmentManager().findFragmentByTag("Calculator");
            if(calculatorFragment!=null) {
                currentFragment = calculatorFragment;
            } else {
                currentFragment = new Calculator();
            }
            currentTag = "Calculator";
            fragmentTransaction.replace(R.id.fragmentContainer,currentFragment,currentTag);
            fragmentTransaction.commit();
        } else if(id == R.id.nav_music_player) {

            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            Fragment musicFragment = getSupportFragmentManager().findFragmentByTag("MusicPlayer");
            if(musicFragment != null){
                currentFragment = musicFragment;
            } else {
                currentFragment = new MusicPlayer();
            }
            currentTag = "MusicPlayer";
            fragmentTransaction.replace(R.id.fragmentContainer,currentFragment,currentTag);
            fragmentTransaction.commit();
        } else if(id == R.id.nav_ranking) {
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            currentFragment = new RankingContainer();
            currentTag = "RankingContainer";
            fragmentTransaction.replace(R.id.fragmentContainer, currentFragment, currentTag);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_memory) {
            CharSequence colors[] = new CharSequence[]{"4x4", "6x6"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose size");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
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
        } else if (id == R.id.nav_profile) {
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            currentFragment = new Profile();
            currentTag = "Profile";
            fragmentTransaction.replace(R.id.fragmentContainer, currentFragment, currentTag);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_help) {
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            currentFragment = new Help();
            currentTag = "Help";
            fragmentTransaction.replace(R.id.fragmentContainer, currentFragment, currentTag);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

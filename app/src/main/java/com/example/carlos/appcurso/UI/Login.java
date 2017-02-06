package com.example.carlos.appcurso.UI;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.carlos.appcurso.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Carlos on 04/02/2017.
 */

public class Login extends AppCompatActivity implements View.OnClickListener{

    private TwitterLoginButton loginButton;
    private Button guestLoginButton;

    //private static final String TWITTER_KEY = "AzDP9maJ0XkOKimPloA1KT9Rr";
    //private static final String TWITTER_SECRET = "omJZYYmEXCyDb9z4i4sGWVMQOGIcBzddyDX0cau1MPmtqfzvk6";
    private static final String TWITTER_KEY = "4I7wnMigirauOim3rlEXePF6K";
    private static final String TWITTER_SECRET = "fuBGAfustzeyeIamhDixY6kmfrvkDgEgqZQKO9iM3To7pYbXt0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences("Preferences", 0);
        if(settings.getBoolean("logged",false)){
            Intent intent = new Intent(getApplicationContext(),BaseActivity.class);
            intent.putExtra("username",settings.getString("user","guest"));
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_login);
        initializeViews();
        setListeners();
    }

    private void initializeViews() {
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                TwitterSession session = result.data;
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                SharedPreferences settings = getSharedPreferences("Preferences", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.putBoolean("logged",true);
                editor.putString("user",session.getUserName());
                editor.apply();
                Intent intent = new Intent(getApplicationContext(),BaseActivity.class);
                intent.putExtra("username",session.getUserName());
                startActivity(intent);
                finish();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
        guestLoginButton = (Button) findViewById(R.id.guest_login_button);

    }

    private void setListeners(){
        guestLoginButton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.guest_login_button:
                SharedPreferences settings = getSharedPreferences("Preferences", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.putBoolean("logged",true);
                editor.putString("user","Guest");
                editor.apply();
                Intent intent = new Intent(getApplicationContext(),BaseActivity.class);
                intent.putExtra("username","Guest");
                startActivity(intent);
                finish();
                break;

        }
    }
}

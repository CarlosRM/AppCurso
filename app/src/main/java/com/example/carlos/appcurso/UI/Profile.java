package com.example.carlos.appcurso.UI;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carlos.appcurso.Data.DBHelper;
import com.example.carlos.appcurso.Data.User;
import com.example.carlos.appcurso.Domain.MusicService;
import com.example.carlos.appcurso.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Carlos on 05/02/2017.
 */

public class Profile extends Fragment implements View.OnClickListener{

    private int TAKE_PICTURE = 0;
    private int PICK_IMAGE = 1;

    View v;
    DBHelper helper;
    SQLiteDatabase database;
    ImageView profilePic;
    TextView username;
    TextView points4;
    TextView points6;
    TextView location;
    User currentUser;
    Button logoutButton;
    List<Address> l;
    LocationManager lm;
    LocationListener lis;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_profile,container,false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Profile");
        initializeViews();
        setViews();
        setListeners();
        setGPS();

        return v;
    }

    private void setGPS(){
        l = null;
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        lis = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                Geocoder gc = new Geocoder(getActivity());
                try {
                    l = gc.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < l.size(); ++i) {
                    Log.v("LOG", l.get(i).getAddressLine(0).toString());
                    TextView t = (TextView) v.findViewById(R.id.location);
                    if(i==0) t.setText("");
                    t.setText(t.getText()+"\n"+l.get(i).getAddressLine(0).toString());
                }
                Log.v("LOG", ((Double) location.getLatitude()).toString());
            }
        };
        if(Build.VERSION.SDK_INT >= 23) {

            int hasFineLocationPermission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }

            int hasCoarseLocationPermission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        0);
            }
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, lis);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, lis);
    }

    private void initializeViews(){
        profilePic = (ImageView) v.findViewById(R.id.profile_picture);
        username = (TextView) v.findViewById(R.id.profile_username);
        points4 = (TextView) v.findViewById(R.id.profile_4_score);
        points6 = (TextView) v.findViewById(R.id.profile_6_score);
        logoutButton = (Button) v.findViewById(R.id.logout_Button);
        location = (TextView) v.findViewById(R.id.location);
    }

    private void setViews(){
        helper = new DBHelper(getActivity());
        database = helper.getReadableDatabase();
        String user = ((BaseActivity) getActivity()).getCurrentUser();
        currentUser = helper.getUser(database,user);

        String auxImage = currentUser.getImage();
        if(auxImage == null) {
            Picasso.with(getActivity()).load(R.drawable.default_avatar).resize(300,300).centerCrop().into(profilePic);
        } else {
            Uri uri = Uri.parse(auxImage);
            if(Build.VERSION.SDK_INT >= 19) {
                getActivity().getContentResolver().takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            Picasso.with(getActivity()).load(uri).resize(300,300).centerCrop().into(profilePic);
        }

        username.setText(user);
        int aux = helper.getUserPoints(database,user,"points4");
        if(aux == -1) points4.setText("N/A");
        else points4.setText(Integer.toString(aux));

        aux = helper.getUserPoints(database,user,"points6");
        if(aux == -1) points6.setText("N/A");
        points6.setText(Integer.toString(aux));
    }

    private void setListeners(){
        profilePic.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.call_button);
        item.setVisible(false);
        item = menu.findItem(R.id.browser_button);
        item.setVisible(false);
        item = menu.findItem(R.id.restart_memory);
        item.setVisible(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.profile_picture) {
            CharSequence colors[] = new CharSequence[] {"Take a picture", "Choose from your gallery"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select picture");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, TAKE_PICTURE);//zero can be replaced with any action code
                            break;
                        case 1:
                            /*Intent intent = new Intent();
                            if(Build.VERSION.SDK_INT >= 19){
                                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            } else intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);*/
                            Intent pickIntent;
                            if(Build.VERSION.SDK_INT >= 19){
                                pickIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            } else pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            pickIntent.setType("image/*");
                            pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
                            startActivityForResult(Intent.createChooser(pickIntent,"Select picture"),PICK_IMAGE);
                            break;
                    }
                }
            });
            builder.show();
        } else if (v.getId() == R.id.logout_Button){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            getActivity().stopService(new Intent(getActivity(),MusicService.class));
                            NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.cancelAll();
                            SharedPreferences settings = getActivity().getSharedPreferences("Preferences", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("logged",false);
                            editor.apply();
                            Intent intent = new Intent(getActivity(),Login.class);
                            startActivity(intent);
                            getActivity().finish();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to log out?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_IMAGE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Uri imageUri = data.getData();
                Picasso.with(getActivity()).load(imageUri).resize(300,300).centerCrop().into(profilePic);
                helper.updateImage(database,currentUser.getName(),imageUri.toString());
                // Do something with the contact here (bigger example below)
            }
        } else if (requestCode == TAKE_PICTURE) {
            if(resultCode == RESULT_OK){
                Log.d("HERE","HERE");
                Uri imageUri = data.getData();
                Picasso.with(getActivity()).load(imageUri).resize(300,300).centerCrop().into(profilePic);
                helper.updateImage(database,currentUser.getName(),imageUri.toString());
            }
        }
    }

    @Override
    public void onPause(){
        if (lm != null) {
            if(Build.VERSION.SDK_INT >= 23) {
                if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                }
            }
            lm.removeUpdates(lis);
        }
        super.onPause();
    }

    @Override
    public void onStop(){
        if (lm != null) {
            if(Build.VERSION.SDK_INT >= 23) {
                if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                }
            }
            lm.removeUpdates(lis);
        }
        super.onStop();
    }
}

package com.example.carlos.appcurso.UI;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carlos.appcurso.Data.DBHelper;
import com.example.carlos.appcurso.Domain.CoolImageFlipper;
import com.example.carlos.appcurso.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Carlos on 02/02/2017.
 */

public class Memory extends Fragment implements View.OnClickListener {

    View v;
    TextView pointsTextView;

    boolean canFlip;
    boolean oneFlipped;
    int lastFlipped;
    int lastFlippedImage;
    int movements;
    int pairsRemaining;
    String username;

    ArrayList<ImageView> cardArray;
    int imageArray[] = new int[16];

    SQLiteDatabase database;
    DBHelper helper;
    CoolImageFlipper flipper;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_memory_4x4,container,false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Memory");
        createTable();
        initializeViews();
        setListeners();
        return v;
    }

    private void createTable() {
        ArrayList<Integer> images = new ArrayList<Integer>(Arrays.asList(R.drawable.robot,R.drawable.robot,R.drawable.robot_1,R.drawable.robot_1,
                R.drawable.robot_2,R.drawable.robot_2,R.drawable.robot_3,R.drawable.robot_3,
                R.drawable.robot_4,R.drawable.robot_4,R.drawable.robot_5,R.drawable.robot_5,
                R.drawable.robot_6,R.drawable.robot_6,R.drawable.robot_7,R.drawable.robot_7));

        for(int i = 0;i<16;++i) {
            Random r = new Random();
            int low = 0;
            int high = images.size();
            int result = r.nextInt(high-low) + low;
            imageArray[i] = images.get(result);
            images.remove(result);
        }

    }

    private void initializeViews() {
        flipper = new CoolImageFlipper(getActivity());
        username = ((BaseActivity) getActivity()).getCurrentUser();
        helper = new DBHelper(getActivity());
        database = helper.getReadableDatabase();

        pointsTextView = (TextView) v.findViewById(R.id.points);
        pointsTextView.setText("0");
        oneFlipped = false;
        movements = 0;
        pairsRemaining = 8;
        canFlip = true;

        cardArray = new ArrayList<>();

        cardArray.add((ImageView) v.findViewById(R.id.card00));
        cardArray.add((ImageView) v.findViewById(R.id.card01));
        cardArray.add((ImageView) v.findViewById(R.id.card02));
        cardArray.add((ImageView) v.findViewById(R.id.card03));
        cardArray.add((ImageView) v.findViewById(R.id.card10));
        cardArray.add((ImageView) v.findViewById(R.id.card11));
        cardArray.add((ImageView) v.findViewById(R.id.card12));
        cardArray.add((ImageView) v.findViewById(R.id.card13));
        cardArray.add((ImageView) v.findViewById(R.id.card20));
        cardArray.add((ImageView) v.findViewById(R.id.card21));
        cardArray.add((ImageView) v.findViewById(R.id.card22));
        cardArray.add((ImageView) v.findViewById(R.id.card23));
        cardArray.add((ImageView) v.findViewById(R.id.card30));
        cardArray.add((ImageView) v.findViewById(R.id.card31));
        cardArray.add((ImageView) v.findViewById(R.id.card32));
        cardArray.add((ImageView) v.findViewById(R.id.card33));

    }

    private void setListeners(){
        for(int i = 0; i < 16;++i) {
            cardArray.get(i).setOnClickListener(this);
        }
    }

    private int translate(int id) {
        if (id==R.id.card00) return 0;
        else if (id==R.id.card01) return 1;
        else if (id==R.id.card02) return 2;
        else if (id==R.id.card03) return 3;
        else if (id==R.id.card10) return 4;
        else if (id==R.id.card11) return 5;
        else if (id==R.id.card12) return 6;
        else if (id==R.id.card13) return 7;
        else if (id==R.id.card20) return 8;
        else if (id==R.id.card21) return 9;
        else if (id==R.id.card22) return 10;
        else if (id==R.id.card23) return 11;
        else if (id==R.id.card30) return 12;
        else if (id==R.id.card31) return 13;
        else if (id==R.id.card32) return 14;
        else if (id==R.id.card33) return 15;
        else return -1;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.browser_button);
        item.setVisible(false);
        item = menu.findItem(R.id.call_button);
        item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.restart_memory:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                for(int i = 0;i < 16;++i){
                                    flipper.flipImage(getResources().getDrawable(R.drawable.cardback),cardArray.get(i));
                                }
                                createTable();
                                initializeViews();
                                setListeners();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to restart?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        final int aux = translate(view.getId());
        if(canFlip){
            cardArray.get(aux).setOnClickListener(null);
            oneFlipped = !oneFlipped;
            flipper.flipImage(getResources().getDrawable(imageArray[aux]),cardArray.get(aux));
            if (oneFlipped) {
                lastFlipped = aux;
                lastFlippedImage = imageArray[aux];
            } else {
                movements++;
                if (aux != lastFlipped && lastFlippedImage == imageArray[aux]) {
                    cardArray.get(aux).setOnClickListener(null);
                    cardArray.get(lastFlipped).setOnClickListener(null);
                    --pairsRemaining;
                } else {
                    canFlip = false;
                    cardArray.get(aux).setOnClickListener(this);
                    cardArray.get(lastFlipped).setOnClickListener(this);
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            flipper.flipImage(getResources().getDrawable(R.drawable.cardback),cardArray.get(aux));
                            flipper.flipImage(getResources().getDrawable(R.drawable.cardback),cardArray.get(lastFlipped));
                            canFlip = true;
                        }
                    };
                    handler.postDelayed(runnable, 1000);

                }
                if (pairsRemaining == 0) {
                    int auxPoints = helper.getUserPoints(database,username,"points4");
                    if(auxPoints == -1 || movements < auxPoints) {
                        String query = "UPDATE " + DBHelper.USER_TABLE_NAME + " SET points4 = " + movements +
                                " WHERE user = '" + username + "'";
                        database.execSQL(query);
                    }
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    for(int i = 0;i < 16;++i){
                                        flipper.flipImage(getResources().getDrawable(R.drawable.cardback),cardArray.get(i));
                                    }
                                    createTable();
                                    initializeViews();
                                    setListeners();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("YOU WON!").setMessage("Do you want to restart?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            }

        }
        pointsTextView.setText(Integer.toString(movements));

    }
}

package com.example.carlos.appcurso.UI;

import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carlos.appcurso.Data.DBHelper;
import com.example.carlos.appcurso.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Carlos on 02/02/2017.
 */

public class Memory6 extends Fragment implements View.OnClickListener {

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
    int imageArray[] = new int[36];

    SQLiteDatabase database;
    DBHelper helper;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_calculator);
        v = inflater.inflate(R.layout.fragment_memory_6x6,container,false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Memory");
        createTable();
        initializeViews();
        setListeners();
        return v;
    }

    private void createTable() {
        ArrayList<Integer> images = new ArrayList<Integer>(Arrays.asList(
                R.drawable.robot,R.drawable.robot,R.drawable.robot_1,R.drawable.robot_1,
                R.drawable.robot_2,R.drawable.robot_2,R.drawable.robot_3,R.drawable.robot_3,
                R.drawable.robot_4,R.drawable.robot_4,R.drawable.robot_5,R.drawable.robot_5,
                R.drawable.robot_6,R.drawable.robot_6,R.drawable.robot_7,R.drawable.robot_7,
                R.drawable.robot_8,R.drawable.robot_8,R.drawable.robot_9,R.drawable.robot_9,
                R.drawable.robot_10,R.drawable.robot_10,R.drawable.robot_11,R.drawable.robot_11,
                R.drawable.robot_12,R.drawable.robot_12,R.drawable.robot_13,R.drawable.robot_13,
                R.drawable.robot_14,R.drawable.robot_14,R.drawable.robot_15,R.drawable.robot_15,
                R.drawable.robot_16,R.drawable.robot_16,R.drawable.robot_17,R.drawable.robot_17));

        for(int i = 0;i<36;++i) {
            Random r = new Random();
            int low = 0;
            int high = images.size();
            int result = r.nextInt(high-low) + low;
            imageArray[i] = images.get(result);
            images.remove(result);
        }

    }

    private void initializeViews() {
        username = ((BaseActivity) getActivity()).getCurrentUser();
        helper = new DBHelper(getActivity());
        database = helper.getReadableDatabase();

        pointsTextView = (TextView) v.findViewById(R.id.points);
        pointsTextView.setText("0");
        oneFlipped = false;
        movements = 0;
        pairsRemaining = 18;
        canFlip = true;

        cardArray = new ArrayList<>();

        cardArray.add((ImageView) v.findViewById(R.id.card00));
        cardArray.add((ImageView) v.findViewById(R.id.card01));
        cardArray.add((ImageView) v.findViewById(R.id.card02));
        cardArray.add((ImageView) v.findViewById(R.id.card03));
        cardArray.add((ImageView) v.findViewById(R.id.card04));
        cardArray.add((ImageView) v.findViewById(R.id.card05));
        cardArray.add((ImageView) v.findViewById(R.id.card10));
        cardArray.add((ImageView) v.findViewById(R.id.card11));
        cardArray.add((ImageView) v.findViewById(R.id.card12));
        cardArray.add((ImageView) v.findViewById(R.id.card13));
        cardArray.add((ImageView) v.findViewById(R.id.card14));
        cardArray.add((ImageView) v.findViewById(R.id.card15));
        cardArray.add((ImageView) v.findViewById(R.id.card20));
        cardArray.add((ImageView) v.findViewById(R.id.card21));
        cardArray.add((ImageView) v.findViewById(R.id.card22));
        cardArray.add((ImageView) v.findViewById(R.id.card23));
        cardArray.add((ImageView) v.findViewById(R.id.card24));
        cardArray.add((ImageView) v.findViewById(R.id.card25));
        cardArray.add((ImageView) v.findViewById(R.id.card30));
        cardArray.add((ImageView) v.findViewById(R.id.card31));
        cardArray.add((ImageView) v.findViewById(R.id.card32));
        cardArray.add((ImageView) v.findViewById(R.id.card33));
        cardArray.add((ImageView) v.findViewById(R.id.card34));
        cardArray.add((ImageView) v.findViewById(R.id.card35));
        cardArray.add((ImageView) v.findViewById(R.id.card40));
        cardArray.add((ImageView) v.findViewById(R.id.card41));
        cardArray.add((ImageView) v.findViewById(R.id.card42));
        cardArray.add((ImageView) v.findViewById(R.id.card43));
        cardArray.add((ImageView) v.findViewById(R.id.card44));
        cardArray.add((ImageView) v.findViewById(R.id.card45));
        cardArray.add((ImageView) v.findViewById(R.id.card50));
        cardArray.add((ImageView) v.findViewById(R.id.card51));
        cardArray.add((ImageView) v.findViewById(R.id.card52));
        cardArray.add((ImageView) v.findViewById(R.id.card53));
        cardArray.add((ImageView) v.findViewById(R.id.card54));
        cardArray.add((ImageView) v.findViewById(R.id.card55));

    }

    private void setListeners(){

        for(int i = 0; i < 36;++i) {
            cardArray.get(i).setOnClickListener(this);
        }
    }

    private int translate(int id) {
        if (id==R.id.card00) return 0;
        else if (id==R.id.card01) return 1;
        else if (id==R.id.card02) return 2;
        else if (id==R.id.card03) return 3;
        else if (id==R.id.card04) return 4;
        else if (id==R.id.card05) return 5;

        else if (id==R.id.card10) return 6;
        else if (id==R.id.card11) return 7;
        else if (id==R.id.card12) return 8;
        else if (id==R.id.card13) return 9;
        else if (id==R.id.card14) return 10;
        else if (id==R.id.card15) return 11;

        else if (id==R.id.card20) return 12;
        else if (id==R.id.card21) return 13;
        else if (id==R.id.card22) return 14;
        else if (id==R.id.card23) return 15;
        else if (id==R.id.card24) return 16;
        else if (id==R.id.card25) return 17;

        else if (id==R.id.card30) return 18;
        else if (id==R.id.card31) return 19;
        else if (id==R.id.card32) return 20;
        else if (id==R.id.card33) return 21;
        else if (id==R.id.card34) return 22;
        else if (id==R.id.card35) return 23;

        else if (id==R.id.card40) return 24;
        else if (id==R.id.card41) return 25;
        else if (id==R.id.card42) return 26;
        else if (id==R.id.card43) return 27;
        else if (id==R.id.card44) return 28;
        else if (id==R.id.card45) return 29;

        else if (id==R.id.card50) return 30;
        else if (id==R.id.card51) return 31;
        else if (id==R.id.card52) return 32;
        else if (id==R.id.card53) return 33;
        else if (id==R.id.card54) return 34;
        else if (id==R.id.card55) return 35;


        else return -1;
    }

    @Override
    public void onClick(View view) {
        final int aux = translate(view.getId());
        if(canFlip){
            oneFlipped = !oneFlipped;
            cardArray.get(aux).setImageResource(imageArray[aux]);
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
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            cardArray.get(aux).setImageResource(R.drawable.cardback);
                            cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                            canFlip = true;
                        }
                    };
                    handler.postDelayed(runnable, 1000);

                }
                if (pairsRemaining == 0) {
                    int auxPoints = helper.getUserPoints(database, username, "points6");
                    if (auxPoints == -1 || movements < auxPoints) {
                        String query = "UPDATE " + DBHelper.USER_TABLE_NAME + " SET points6 = " + movements +
                                " WHERE user = '" + username + "'";
                        database.execSQL(query);
                    }
                }
            }

        }
        pointsTextView.setText(Integer.toString(movements));

    }
}

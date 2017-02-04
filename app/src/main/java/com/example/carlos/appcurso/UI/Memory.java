package com.example.carlos.appcurso.UI;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_calculator);
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
        /*card00.setOnClickListener(this);
        card01.setOnClickListener(this);
        card02.setOnClickListener(this);
        card03.setOnClickListener(this);
        card10.setOnClickListener(this);
        card11.setOnClickListener(this);
        card12.setOnClickListener(this);
        card13.setOnClickListener(this);
        card20.setOnClickListener(this);
        card21.setOnClickListener(this);
        card22.setOnClickListener(this);
        card23.setOnClickListener(this);
        card30.setOnClickListener(this);
        card31.setOnClickListener(this);
        card32.setOnClickListener(this);
        card33.setOnClickListener(this);*/

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
    public void onClick(View view) {
        final int aux = translate(view.getId());
        if(canFlip){
            cardArray.get(aux).setOnClickListener(null);
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
                    cardArray.get(aux).setOnClickListener(this);
                    cardArray.get(lastFlipped).setOnClickListener(this);
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
                    int auxPoints = helper.getUserPoints(database,username,"points4");
                    if(auxPoints == -1 || movements < auxPoints) {
                        String query = "UPDATE " + DBHelper.USER_TABLE_NAME + " SET points4 = " + movements +
                                " WHERE user = '" + username + "'";
                        database.execSQL(query);
                    }
                }
            }

        }
        pointsTextView.setText(Integer.toString(movements));

    }
    /*@Override
    public void onClick(View view) {
        //view.setBackgroundColor(Color.WHITE);
        if (canFlip) {
            oneFlipped = !oneFlipped;
            switch (view.getId()) {
                case R.id.card00:
                    cardArray.get(0).setImageResource(imageArray[0]);
                    if (oneFlipped) {
                        lastFlipped = 0;
                        lastFlippedImage = imageArray[0];
                    } else {
                        movements++;
                        if (0 != lastFlipped && lastFlippedImage == imageArray[0]) {
                            cardArray.get(0).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(0).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);

                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.card01:
                    cardArray.get(1).setImageResource(imageArray[1]);
                    if (oneFlipped) {
                        lastFlipped = 1;
                        lastFlippedImage = imageArray[1];
                    } else {
                        movements++;
                        if (1 != lastFlipped && lastFlippedImage == imageArray[1]) {
                            cardArray.get(1).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(1).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);

                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.card02:
                    cardArray.get(2).setImageResource(imageArray[2]);
                    if (oneFlipped) {
                        lastFlipped = 2;
                        lastFlippedImage = imageArray[2];
                    } else {
                        movements++;
                        if (2 != lastFlipped && lastFlippedImage == imageArray[2]) {
                            cardArray.get(2).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(2).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);
                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.card03:
                    cardArray.get(3).setImageResource(imageArray[3]);
                    if (oneFlipped) {
                        lastFlipped = 3;
                        lastFlippedImage = imageArray[3];
                    } else {
                        movements++;
                        if (3 != lastFlipped && lastFlippedImage == imageArray[3]) {
                            cardArray.get(3).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(3).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);
                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.card10:
                    cardArray.get(4).setImageResource(imageArray[4]);
                    if (oneFlipped) {
                        lastFlipped = 4;
                        lastFlippedImage = imageArray[4];
                    } else {
                        movements++;
                        if (4 != lastFlipped && lastFlippedImage == imageArray[4]) {
                            cardArray.get(4).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(4).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);
                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.card11:
                    cardArray.get(5).setImageResource(imageArray[5]);
                    if (oneFlipped) {
                        lastFlipped = 5;
                        lastFlippedImage = imageArray[5];
                    } else {
                        movements++;
                        if (5 != lastFlipped && lastFlippedImage == imageArray[5]) {
                            cardArray.get(5).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(5).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);
                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.card12:
                    cardArray.get(6).setImageResource(imageArray[6]);
                    if (oneFlipped) {
                        lastFlipped = 6;
                        lastFlippedImage = imageArray[6];
                    } else {
                        movements++;
                        if (6 != lastFlipped && lastFlippedImage == imageArray[6]) {
                            cardArray.get(6).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(6).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);
                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.card13:
                    cardArray.get(7).setImageResource(imageArray[7]);
                    if (oneFlipped) {
                        lastFlipped = 7;
                        lastFlippedImage = imageArray[7];
                    } else {
                        movements++;
                        if (0 != lastFlipped && lastFlippedImage == imageArray[7]) {
                            cardArray.get(7).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(7).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);
                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.card20:
                    cardArray.get(8).setImageResource(imageArray[8]);
                    if (oneFlipped) {
                        lastFlipped = 8;
                        lastFlippedImage = imageArray[8];
                    } else {
                        movements++;
                        if (8 != lastFlipped && lastFlippedImage == imageArray[8]) {
                            cardArray.get(8).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(8).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);
                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.card21:
                    cardArray.get(9).setImageResource(imageArray[9]);
                    if (oneFlipped) {
                        lastFlipped = 9;
                        lastFlippedImage = imageArray[9];
                    } else {
                        movements++;
                        if (9 != lastFlipped && lastFlippedImage == imageArray[9]) {
                            cardArray.get(9).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(9).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);
                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.card22:
                    cardArray.get(10).setImageResource(imageArray[10]);
                    if (oneFlipped) {
                        lastFlipped = 10;
                        lastFlippedImage = imageArray[10];
                    } else {
                        movements++;
                        if (10 != lastFlipped && lastFlippedImage == imageArray[10]) {
                            cardArray.get(10).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(10).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);
                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.card23:
                    cardArray.get(11).setImageResource(imageArray[11]);
                    if (oneFlipped) {
                        lastFlipped = 11;
                        lastFlippedImage = imageArray[11];
                    } else {
                        movements++;
                        if (11 != lastFlipped && lastFlippedImage == imageArray[11]) {
                            cardArray.get(11).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(11).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);
                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.card30:
                    cardArray.get(12).setImageResource(imageArray[12]);
                    if (oneFlipped) {
                        lastFlipped = 12;
                        lastFlippedImage = imageArray[12];
                    } else {
                        movements++;
                        if (12 != lastFlipped && lastFlippedImage == imageArray[12]) {
                            cardArray.get(12).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(12).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);
                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.card31:
                    cardArray.get(13).setImageResource(imageArray[13]);
                    if (oneFlipped) {
                        lastFlipped = 13;
                        lastFlippedImage = imageArray[13];
                    } else {
                        movements++;
                        if (13 != lastFlipped && lastFlippedImage == imageArray[13]) {
                            cardArray.get(13).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(13).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);
                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.card32:
                    cardArray.get(14).setImageResource(imageArray[14]);
                    if (oneFlipped) {
                        lastFlipped = 14;
                        lastFlippedImage = imageArray[14];
                    } else {
                        movements++;
                        if (14 != lastFlipped && lastFlippedImage == imageArray[14]) {
                            cardArray.get(14).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(14).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);
                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.card33:
                    cardArray.get(15).setImageResource(imageArray[15]);
                    if (oneFlipped) {
                        lastFlipped = 15;
                        lastFlippedImage = imageArray[15];
                    } else {
                        movements++;
                        if (15 != lastFlipped && lastFlippedImage == imageArray[15]) {
                            cardArray.get(15).setOnClickListener(null);
                            cardArray.get(lastFlipped).setOnClickListener(null);
                            --pairsRemaining;
                        } else {
                            canFlip = false;
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    cardArray.get(15).setImageResource(R.drawable.cardback);
                                    cardArray.get(lastFlipped).setImageResource(R.drawable.cardback);
                                    canFlip = true;
                                }
                            };
                            handler.postDelayed(runnable, 1000);
                        }
                        if (pairsRemaining == 0)
                            Toast.makeText(getActivity(), "YOU WON", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
            pointsTextView.setText(Integer.toString(movements));
        }
    }*/
}

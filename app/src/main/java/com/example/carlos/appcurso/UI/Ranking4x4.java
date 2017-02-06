package com.example.carlos.appcurso.UI;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.carlos.appcurso.Data.DBHelper;
import com.example.carlos.appcurso.Data.User;
import com.example.carlos.appcurso.R;

import java.util.List;

/**
 * Created by Carlos on 31/01/2017.
 */

public class Ranking4x4 extends Fragment {

    RecyclerView rankingRecylerView;
    RankingAdapter rankingAdapter;
    View v;
    List<User> userDataList;
    DBHelper helper;
    SQLiteDatabase database;
    Button resetRankButton;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_ranking,container,false);
        helper = new DBHelper(getActivity());
        database = helper.getReadableDatabase();
        userDataList = helper.getUsers(database,DBHelper.COLUMN_POINTS_4);
        initializeViews();
        setListeners();
        return v;
    }

    private void setListeners() {
        resetRankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                helper.resetRanking(database,"points4");
                                rankingAdapter.removeAll();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to reset this ranking? All data will be lost.").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    private void initializeViews() {
        resetRankButton = (Button) v.findViewById(R.id.reset_ranking_button);

        rankingRecylerView = (RecyclerView) v.findViewById(R.id.rankingRecyclerView);
        rankingRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        rankingAdapter = new RankingAdapter(userDataList,getActivity(),4);
        rankingRecylerView.setAdapter(rankingAdapter);

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
}


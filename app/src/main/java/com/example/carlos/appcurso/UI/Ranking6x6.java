package com.example.carlos.appcurso.UI;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carlos.appcurso.Data.DBHelper;
import com.example.carlos.appcurso.Data.User;
import com.example.carlos.appcurso.R;

import java.util.List;

/**
 * Created by Carlos on 31/01/2017.
 */

public class Ranking6x6 extends Fragment {

    RecyclerView rankingRecylerView;
    RankingAdapter rankingAdapter;
    View v;
    List<User> userDataList;
    DBHelper helper;
    SQLiteDatabase database;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_ranking,container,false);
        helper = new DBHelper(getActivity());
        database = helper.getReadableDatabase();
        userDataList = helper.getUsers(database,DBHelper.COLUMN_POINTS_6);
        initializeViews();
        return v;
    }

    private void initializeViews() {
        rankingRecylerView = (RecyclerView) v.findViewById(R.id.rankingRecyclerView);
        rankingRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        rankingAdapter = new RankingAdapter(userDataList,getActivity(),6);
        rankingRecylerView.setAdapter(rankingAdapter);
    }
}


package com.example.carlos.appcurso.UI;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.carlos.appcurso.Data.DBHelper;
import com.example.carlos.appcurso.R;

/**
 * Created by Carlos on 01/02/2017.
 */

public class RankingContainer extends Fragment {

    View v;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_ranking_container,container,false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Ranking");

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPager.setAdapter(new RankingContainerAdapter(getActivity().getSupportFragmentManager(),
                getActivity()));

        // Give the TabLayout the ViewPager (material)
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setTabTextColors(Color.LTGRAY, Color.WHITE);
        tabLayout.setupWithViewPager(viewPager);


        return v;
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

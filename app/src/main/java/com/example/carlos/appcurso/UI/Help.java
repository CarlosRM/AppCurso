package com.example.carlos.appcurso.UI;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.carlos.appcurso.R;

/**
 * Created by Carlos on 06/02/2017.
 */

public class Help extends Fragment {

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v;

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_help,container,false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Help");
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

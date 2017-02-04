package com.example.carlos.appcurso.UI;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Carlos on 01/02/2017.
 */

public class RankingContainerAdapter extends FragmentStatePagerAdapter {


    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "4x4", "6x6" };
    private Context context;
    Fragment tab = null;

    //creadora
    public RankingContainerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    //crea las tabas, siempre tiene que retornar con el numero de tabs que queremos mostrar
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    //Lanza el fragment asociado con el numero de tab
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                tab = new Ranking4x4();
                break;
            case 1:
                tab = new Ranking6x6();
                break;
        }
        return tab;
    }

    //pone el nombre en cada tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}


package com.example.yd.douban.movie;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.yd.douban.base.BaseViewPagerAdapter;

/**
 * Created by YD on 2017/10/27.
 */

public class MovieDetailPagerAdapter extends BaseViewPagerAdapter {
    public MovieDetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }
}

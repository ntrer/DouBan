package com.example.yd.douban.book;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.yd.douban.base.BaseViewPagerAdapter;

/**
 * Created by YD on 2017/10/27.
 */

public class BookDetailPagerAdapter extends BaseViewPagerAdapter {
    public BookDetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }
}

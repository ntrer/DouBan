package com.example.yd.douban;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.yd.douban.base.BaseViewPagerAdapter;

/**
 * Created by YD on 2017/10/27.
 */

public class DouBanPagerAdapter extends BaseViewPagerAdapter {

    public DouBanPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * 添加页面
     * @param fragment
     * @param title
     */
    public void addFragment(Fragment fragment,String title){
        mFragments.add(fragment);
        mFragmentTitles.add(title);

    }
}

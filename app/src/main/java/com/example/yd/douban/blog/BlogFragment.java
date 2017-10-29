package com.example.yd.douban.blog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yd.douban.R;
import com.example.yd.douban.about.AboutFragment;

/**
 * Created by YD on 2017/10/28.
 */

public class BlogFragment extends Fragment {


    public BlogFragment() {
        // Required empty public constructor
    }

    public static BlogFragment newInstance() {
        return new BlogFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);

        return view;
    }
}

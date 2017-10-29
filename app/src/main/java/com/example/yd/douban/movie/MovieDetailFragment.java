package com.example.yd.douban.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yd.douban.R;
import com.example.yd.douban.utils.ConstContent;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = MovieDetailFragment.class.getSimpleName();

    private String mUrl;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    /**
     * 创建Fragment对象
     * @param info 数据
     * @param type  类型
     * @return
     */
    public static MovieDetailFragment createInstance(String info, int type) {
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putString(ConstContent.INTENT_EXTRA_FRAGMENT_INFO, info);
        args.putInt(ConstContent.INTENT_EXTRA_FRAGMENT_TYPE, type);//0: 影片信息Tab; 1: 简介Tab
        movieDetailFragment.setArguments(args);
        return movieDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        TextView textInfo = (TextView) view.findViewById(R.id.tvInfo);
        textInfo.setText(getArguments().getString(ConstContent.INTENT_EXTRA_FRAGMENT_INFO));

        //如果是详情界面，设置点击事件
        if(ConstContent.TYPE_MOVIE_WEBSITE == getArguments().getInt(ConstContent.INTENT_EXTRA_FRAGMENT_TYPE)) {
            textInfo.setOnClickListener(this);
            mUrl = textInfo.getText().toString();
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvInfo:
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(ConstContent.INTENT_EXTRA_WEBSITE_URL, mUrl);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}

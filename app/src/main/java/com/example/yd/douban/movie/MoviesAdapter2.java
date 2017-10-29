package com.example.yd.douban.movie;


import android.content.Context;
import android.view.View;

import com.example.yd.douban.R;
import com.example.yd.douban.base.BaseViewHolder;
import com.example.yd.douban.base.SimpleAdapter;
import com.example.yd.douban.bean.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by YD on 2017/10/26.
 */

public class MoviesAdapter2 extends SimpleAdapter<Movie> {


    public MoviesAdapter2(Context context, int layoutResId, List<Movie> datas) {
        super(context, layoutResId, datas);
    }

    @Override
    protected void bindData(BaseViewHolder viewHoder, Movie item) {
        double average = item.getRating().getAverage();
        Picasso.with(context).load(item.getImages().getLarge()).into(viewHoder.getImageView(R.id.movie_cover));
        viewHoder.getTextView(R.id.movie_title).setText(item.getTitle());
        if(average!=0)
        {
            viewHoder.getRatingBar(R.id.rating_star).setVisibility(View.VISIBLE);
            viewHoder.getTextView(R.id.movie_average).setText(String.valueOf(average));
            viewHoder.getRatingBar(R.id.rating_star).setStepSize(0.5f);
            viewHoder.getRatingBar(R.id.rating_star).setRating((float) (item.getRating().getAverage() / 2));
        }
        else {
            viewHoder.getRatingBar(R.id.rating_star).setVisibility(View.GONE);
            viewHoder.getTextView(R.id.movie_average).setText(String.valueOf(average));
            viewHoder.getTextView(R.id.movie_average).setText(context.getResources().getString(R.string.string_no_note));
        }

    }
}

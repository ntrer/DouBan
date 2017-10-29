package com.example.yd.douban.movie;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.yd.douban.R;
import com.example.yd.douban.bean.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by YD on 2017/10/26.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>{
    private List<Movie> mMovieList;
    private Context mContext;
    @LayoutRes
    private int LayoutResId;
    private Movie mMovie;
    private OnItemClickListener mOnItemClickListener = null;

    public MoviesAdapter(Context context, List<Movie> movieList, int layoutResId) {
        this.mContext = context;
        setData(movieList);
        this.LayoutResId = layoutResId;
    }

    //这里这样子来设置数据，是因为在onCreateView里先初始化了RecyclerView
    //和adapter,此时传入adapter里的数据为空，然后调用请求数据的方法后，会调用
    //showMovies方法，在showMovies里调用replaceData方法将数据传入了adapter
    public void setData(List<Movie> movieList)
    {
        this.mMovieList = movieList;
    }


    public void replaceData(List<Movie> movieList)
    {
        setData(movieList);
        notifyDataSetChanged();

    }

    /**
     * 实现监听点击事件的接口
     */
    public  interface OnItemClickListener {
        void onItemClick(View view, int position, ImageView imageView);
    }

    /**
     * 设置点击监听
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;

    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(mContext).inflate(LayoutResId, parent, false);
        return new MoviesViewHolder(itemview,mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {

        mMovie = mMovieList.get(position);
        if (mMovie != null) {
            Picasso.with(mContext).load(mMovie.getImages().getLarge()).into(holder.mMovieCover);
            holder.mMovieTitle.setText(mMovie.getTitle());
            double average = mMovie.getRating().getAverage();
            if (average != 0) {
                holder.mRatingStar.setVisibility(View.VISIBLE);
                holder.mMovieAverage.setText(String.valueOf(average));
                holder.mRatingStar.setStepSize(0.5f);
                holder.mRatingStar.setRating((float) (mMovie.getRating().getAverage() / 2));
            } else {
                holder.mRatingStar.setVisibility(View.GONE);
                holder.mMovieAverage.setText(mContext.getResources().getString(R.string.string_no_note));
            }
        }

    }

    /**
     * 得到对应位置的数据
     * @param position
     * @return
     */
    public Movie getItem(int position) {
        if (position >= mMovieList.size()) return null;
        return mMovieList.get(position);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();

    }



    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mMovieCover;
        TextView mMovieTitle;
        RatingBar mRatingStar;
        TextView mMovieAverage;
        private MoviesAdapter.OnItemClickListener mOnItemClickListener;
        public MoviesViewHolder(View itemView,MoviesAdapter.OnItemClickListener OnItemClickListener) {
            super(itemView);
            mMovieCover = (ImageView) itemView.findViewById(R.id.movie_cover);
            mMovieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            mRatingStar = (RatingBar) itemView.findViewById(R.id.rating_star);
            mMovieAverage = (TextView) itemView.findViewById(R.id.movie_average);
            this.mOnItemClickListener=OnItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemView == null) return;
            Context context = itemView.getContext();
            if (context == null) return;

            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view,getLayoutPosition(),mMovieCover);
            }
        }
    }

}

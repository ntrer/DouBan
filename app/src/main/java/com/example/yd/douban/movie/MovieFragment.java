package com.example.yd.douban.movie;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yd.douban.R;
import com.example.yd.douban.bean.Movie;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements MoviesContract.View {

    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_no_movies)
    LinearLayout mLlNoMovies;
    Unbinder unbinder;
    /**
     * SmartRefreshLayout控件
     */
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private MoviesAdapter2 mMoviesAdapter;
    private MoviesContract.Presenter mPresenter;
    private List<Movie> mAdapterData = new ArrayList<>();

    public MovieFragment() {
        // Required empty public constructor
    }

    /**
     * 返回MovieFragment的实例，调用这个方法相当于创建MovieFragment对象
     *
     * @return
     */
    public static MovieFragment newInstance() {
        return new MovieFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        unbinder = ButterKnife.bind(this, view);
        initRecyclerView();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
          refreshLayout.autoRefresh();
    }

    private void initRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            final GridLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
            mRecyclerView.setLayoutManager(layoutManager);
            mMoviesAdapter = new MoviesAdapter2(getContext(),  R.layout.recyclerview_movies_item,mAdapterData);
            mRecyclerView.setAdapter(mMoviesAdapter);
            mMoviesAdapter.setOnItemClickListener(new MoviesAdapter2.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //得到点击的item的数据
                    Movie item = mMoviesAdapter.getItem(position);
                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                    intent.putExtra("movie", item);
                    ImageView mMovieCover=view.findViewById(R.id.movie_cover);
                    //使用MD风格动画开启新界面
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), mMovieCover, "cover").toBundle();
                    ActivityCompat.startActivity(getActivity(), intent, bundle);
                }
            });
            initRefreshLayout();
        }
    }

    /**
     * 设置下拉刷新和上拉加载更多
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void initRefreshLayout() {
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new MaterialHeader(getActivity()));
        //设置 Footer 为 球脉冲
        //refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                // 下拉刷新
                mPresenter.loadRefreshedMovies(true);

            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                // 加载更多
                mPresenter.loadMoreMovies(mMoviesAdapter.getItemCount(),true);
            }
        });

    }


    /**
     * 显示数据
     *
     * @param movies
     */
    @Override
    public void showRefreshedMovies(List<Movie> movies) {
        //判断数据集合中是否有数据，如果有数据，且第一条数据的ID与集合中第一条一样，说明服务器数据没有更新
        //那么就并不用重新往适配器添加数据
        if (mAdapterData.size() != 0 && movies.get(0).getId().equals(mAdapterData.get(0).getId())) {
            return;
        }

        mAdapterData.clear();
        mAdapterData.addAll(movies);
        mMoviesAdapter.replaceData(mAdapterData);

    }

    /**
     * 显示更多数据
     * @param movies
     */
    @Override
    public void showLoadedMoreMovies(List<Movie> movies) {
        mAdapterData.addAll(movies);
        mMoviesAdapter.replaceData(mAdapterData);

    }

    @Override
    public void showNoMovies() {

    }

    @Override
    public void showNoLoadedMoreMovies() {
        Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void setRefreshedIndicator(boolean active) {
        if (active==false) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }

    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}

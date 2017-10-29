package com.example.yd.douban.movie;

import android.support.annotation.NonNull;

import com.example.yd.douban.api.DouBanService;
import com.example.yd.douban.bean.HotMoviesInfo;
import com.example.yd.douban.bean.Movie;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by YD on 2017/10/24.
 */

public class MoviesPresenter implements MoviesContract.Presenter {


    private final MoviesContract.View mMoviesView;

    private final DouBanService mDouBanService;
    //是否是第一次加载
    private boolean mFirstLoad = true;

    public MoviesPresenter(@NonNull DouBanService moviesService, @NonNull MoviesContract.View moviesView) {
        mDouBanService = checkNotNull(moviesService, "IDoubanServie cannot be null!");
        mMoviesView = checkNotNull(moviesView, "moviesView cannot be null!");
        //给view绑定Presenter
        mMoviesView.setPresenter(this);
    }

    /**
     * 请求数据
     */
    @Override
    public void request() {
        loadRefreshedMovies(false);
    }

    @Override
    public void requestWithText(String text) {

    }


    /**
     * 请求数据
     *
     * @param forceUpdate 判断是否要加载数据
     */
    @Override
    public void loadRefreshedMovies(boolean forceUpdate) {
        loadMovie(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     * 真正的请求数据方法
     *
     * @param forceUpdate
     * @param showLoadingUI
     */
    private void loadMovie(boolean forceUpdate, final boolean showLoadingUI) {

        if (forceUpdate) {
//            mDouBanService.searchHotMovies(0).enqueue(new Callback<HotMoviesInfo>() {
//                @Override
//                public void onResponse(Call<HotMoviesInfo> call, Response<HotMoviesInfo> response) {
//                    //得到解析好的数据
//                    List<Movie> movies = response.body().getMovies();
//
//                    if (showLoadingUI) {
//                        //显示加载进度条
//                        mMoviesView.setRefreshedIndicator(false);
//                    }
//                    //处理数据
//                    processMovies(movies);
//                }
//
//                @Override
//                public void onFailure(Call<HotMoviesInfo> call, Throwable t) {
//                    if (showLoadingUI) {
//                        //隐藏进度条
//                        mMoviesView.setRefreshedIndicator(false);
//                    }
//                }
//            });
            Observable<HotMoviesInfo> observable = mDouBanService.searchHotMoviesWithRxJava(0);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<HotMoviesInfo>() {
                        @Override
                        public void onCompleted() {
                            //隐藏进度条
                            mMoviesView.setRefreshedIndicator(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (showLoadingUI) {
                                //隐藏进度条
                                mMoviesView.setRefreshedIndicator(false);
                            }
                        }

                        @Override
                        public void onNext(HotMoviesInfo hotMoviesInfo) {
                            //得到解析好的数据
                            List<Movie> movies = hotMoviesInfo.getMovies();

                            if (showLoadingUI) {
                                //显示加载进度条
                                mMoviesView.setRefreshedIndicator(false);
                            }
                            //处理数据
                            processMovies(movies);
                        }
                    });


        }


    }

    /**
     * 先判空，不为空则显示数据
     *
     * @param movies
     */
    private void processMovies(List<Movie> movies) {

        if (movies == null) {

            showNoData();

        } else {
            mMoviesView.showRefreshedMovies(movies);

        }

    }

    /**
     * 显示没有数据界面
     */
    private void showNoData() {
        mMoviesView.showNoMovies();
    }


    /**
     * 加载更多数据
     *
     * @param movieStartIndex
     * @param showLoadingUI
     */
    @Override
    public void loadMoreMovies(int movieStartIndex, final boolean showLoadingUI) {
//        mDouBanService.searchHotMovies(movieStartIndex).enqueue(new Callback<HotMoviesInfo>() {
//            @Override
//            public void onResponse(Call<HotMoviesInfo> call, Response<HotMoviesInfo> response) {
//                //得到解析好的数据
//                List<Movie> movies = response.body().getMovies();
//
//                if (showLoadingUI) {
//                    //显示加载进度条
//                    mMoviesView.setRefreshedIndicator(false);
//                }
//                //处理数据
//                processLoadMoreMovies(movies);
//            }
//
//            @Override
//            public void onFailure(Call<HotMoviesInfo> call, Throwable t) {
//                if (showLoadingUI) {
//                    //隐藏进度条
//                    mMoviesView.setRefreshedIndicator(false);
//                    processLoadMoreEmptyBooks();
//                }
//            }
//        });
        Observable<HotMoviesInfo> observable = mDouBanService.searchHotMoviesWithRxJava(movieStartIndex);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HotMoviesInfo>() {
                    @Override
                    public void onCompleted() {
                        //隐藏进度条
                        mMoviesView.setRefreshedIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (showLoadingUI) {
                            //隐藏进度条
                            mMoviesView.setRefreshedIndicator(false);
                            processLoadMoreEmptyBooks();
                        }
                    }

                    @Override
                    public void onNext(HotMoviesInfo hotMoviesInfo) {
                        //得到解析好的数据
                        List<Movie> movies = hotMoviesInfo.getMovies();

                        if (showLoadingUI) {
                            //显示加载进度条
                            mMoviesView.setRefreshedIndicator(false);
                        }
                        //处理数据
                        processLoadMoreMovies(movies);
                    }
                });

    }

    /**
     * 显示更多数据
     *
     * @param movies
     */
    private void processLoadMoreMovies(List<Movie> movies) {
        if (movies.isEmpty()) processLoadMoreEmptyBooks();
        else mMoviesView.showLoadedMoreMovies(movies);

    }


    /**
     * 显示没有数据界面
     */
    private void processLoadMoreEmptyBooks() {
        mMoviesView.showNoLoadedMoreMovies();
    }

    @Override
    public void cancelRetrofitRequest() {

    }

    @Override
    public void unSubscribe() {

    }


}

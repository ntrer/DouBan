package com.example.yd.douban.movie;

import com.example.yd.douban.base.BasePresenter;
import com.example.yd.douban.base.BaseView;
import com.example.yd.douban.bean.Movie;
import java.util.List;

/**
 * Contract用来定义View和Presenter
 */
public interface MoviesContract {

    interface View extends BaseView<Presenter> {


        void showRefreshedMovies(List<Movie> movies);
        void showLoadedMoreMovies(List<Movie> movies);

        void showNoMovies();
        void showNoLoadedMoreMovies();

        void setRefreshedIndicator(boolean active);
    }

    interface Presenter extends BasePresenter {

        void loadRefreshedMovies(boolean forceUpdate);

        void loadMoreMovies(int movieStartIndex,boolean showLoadingUI);

        void cancelRetrofitRequest();

        void unSubscribe();
    }
}

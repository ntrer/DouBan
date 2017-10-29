package com.example.yd.douban.book;

import com.example.yd.douban.base.BasePresenter;
import com.example.yd.douban.base.BaseView;
import com.example.yd.douban.bean.Book;

import java.util.List;

/**
 * Created by Johnny Tam on 2017/3/21.
 */

public interface BooksContract {

    interface View extends BaseView<Presenter> {

        void showRefreshedBooks(List<Book> books);

        void showLoadedMoreBooks(List<Book> books);

        void showNoBooks();

        void showNoLoadedMoreBooks();

        void setRefreshedIndicator(boolean active);
    }

    interface Presenter extends BasePresenter {

        void loadRefreshedBooks(boolean forceUpdate,String text);


        void loadMoreBooks(int start,String text);

        void cancelRetrofitRequest();

        void unSubscribe();

    }


}

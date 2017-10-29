package com.example.yd.douban.book;

import android.support.annotation.NonNull;

import com.example.yd.douban.api.DouBanService;
import com.example.yd.douban.bean.Book;
import com.example.yd.douban.bean.BooksInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by YD on 2017/10/24.
 */

public class BooksPresenter implements BooksContract.Presenter {

    private final BooksContract.View mBooksView;

    private final DouBanService mDouBanService;
    //是否是第一次加载
    private boolean mFirstLoad = true;

    public BooksPresenter(@NonNull DouBanService moviesService, @NonNull BooksContract.View booksView) {
        mDouBanService = checkNotNull(moviesService, "IDoubanServie cannot be null!");
        mBooksView = checkNotNull(booksView, "moviesView cannot be null!");
        //给view绑定Presenter
        mBooksView.setPresenter(this);
    }




    /**
     * 请求数据
     */
    @Override
    public void requestWithText(String text) {
        loadRefreshedBooks(false,text);
    }



    /**
     * 请求数据
     *
     * @param forceUpdate 判断是否要加载数据
     */
    @Override
    public void loadRefreshedBooks(boolean forceUpdate,String text) {
        loadBooks(forceUpdate || mFirstLoad, true,text);
        mFirstLoad = false;
    }



    /**
     * 真正的请求数据方法
     *
     * @param forceUpdate
     * @param showLoadingUI
     */
    private void loadBooks(boolean forceUpdate, final boolean showLoadingUI,String text) {
        //显示加载进度条
        if (showLoadingUI) {
            mBooksView.setRefreshedIndicator(true);
        }

        if (forceUpdate) {
            mDouBanService.searchBooks(text,0).enqueue(new Callback<BooksInfo>() {
                @Override
                public void onResponse(Call<BooksInfo> call, Response<BooksInfo> response) {
                    //得到解析好的数据
                    List<Book> books = response.body().getBooks();

                    if (showLoadingUI) {
                        mBooksView.setRefreshedIndicator(false);
                    }
                    //处理数据
                    processMovies(books);
                }

                @Override
                public void onFailure(Call<BooksInfo> call, Throwable t) {
                    if (showLoadingUI) {
                        mBooksView.setRefreshedIndicator(false);

                    }
                }
            });


        }


    }


    /**
     * 先判空，不为空则显示数据
     *
     * @param books
     */
    private void processMovies(List<Book> books) {

        if (books == null) {

            showNoBooks();

        } else {
            mBooksView.showRefreshedBooks(books);

        }

    }


    /**
     * 显示没有数据界面
     */
    private void showNoBooks() {
        mBooksView.showNoBooks();
    }

    /**
     * 加载更多数据
     * @param start
     */
    @Override
    public void loadMoreBooks(int start,String text) {

        mDouBanService.searchBooks(text,start).enqueue(new Callback<BooksInfo>() {
            @Override
            public void onResponse(Call<BooksInfo> call, Response<BooksInfo> response) {
                //得到解析好的数据
                List<Book> moreBooks = response.body().getBooks();

                //处理数据
                processLoadMoreBooks(moreBooks);
            }

            @Override
            public void onFailure(Call<BooksInfo> call, Throwable t) {
                processLoadMoreEmptyBooks();
            }
        });

    }



    private void processLoadMoreBooks(List<Book> moreBooks) {
        if(moreBooks.isEmpty()) processLoadMoreEmptyBooks();
        else mBooksView.showLoadedMoreBooks(moreBooks);

    }

    private void processLoadMoreEmptyBooks() {

         mBooksView.showNoLoadedMoreBooks();
    }

    @Override
    public void cancelRetrofitRequest() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void request() {

    }


}

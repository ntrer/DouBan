package com.example.yd.douban.api;

import com.example.yd.douban.bean.BooksInfo;
import com.example.yd.douban.bean.HotMoviesInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by YD on 2017/10/23.
 */

public interface DouBanService {

    String BASE_URL = "https://api.douban.com/v2/";

    @GET("movie/in_theaters")
    Call<HotMoviesInfo>searchHotMovies(@Query("start") int startIndex);

    @GET("movie/in_theaters")
    Observable<HotMoviesInfo> searchHotMoviesWithRxJava(@Query("start") int startIndex);


    @GET("book/search")
    Call<BooksInfo> searchBooks(@Query("q") String name, @Query("start") int index);

}

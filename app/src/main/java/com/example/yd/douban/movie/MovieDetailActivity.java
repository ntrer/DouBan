package com.example.yd.douban.movie;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.yd.douban.R;
import com.example.yd.douban.bean.Movie;
import com.example.yd.douban.utils.ConstContent;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by YD on 2017/10/24.
 */

public class MovieDetailActivity extends AppCompatActivity {


    @BindView(R.id.movie_image)
    ImageView mMovieImage;
    @BindView(R.id.movie_toolbar)
    Toolbar mMovieToolbar;
    @BindView(R.id.movie_collapsing_toolbar)
    CollapsingToolbarLayout mMovieCollapsingToolbar;
    @BindView(R.id.movie_sliding_tabs)
    TabLayout mMovieSlidingTabs;
    @BindView(R.id.movie_viewpager)
    ViewPager mMovieViewpager;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.fab_movie_detail)
    FloatingActionButton mFabMovieDetail;
    private MovieDetailPagerAdapter mMovieDetailPagerAdapter;
    private String mMovieInfo = null;
    private String mMovieAlt = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusbar();
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        initData();
        initToolBar();
        initFab();
        SetUpViewPager(mMovieViewpager);
        mMovieSlidingTabs.setupWithViewPager(mMovieViewpager);
    }



    /**
     * //5.0以上设置状态栏透明
     */
    private void setStatusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //下面这一行呢是android4.0起推荐大家使用的将布局延伸到状态栏的代码，配合5.0的设置状态栏颜色可谓天作之合
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initToolBar() {
        setSupportActionBar(mMovieToolbar);
        //给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initFab() {

        mFabMovieDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieDetailActivity.this.showShare();
            }
        });

    }


    /**
     * 分享操作
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        //oks.setTitle("标题"); // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitleUrl(mMovieAlt); // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setText("我是分享文本"); // text是分享文本，所有平台都需要这个字段
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        //oks.setUrl("http://sharesdk.cn"); // url仅在微信（包括好友和朋友圈）中使用
        //oks.setComment("我是测试评论文本"); // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setSite(getString(R.string.app_name)); // site是分享此内容的网站名称，仅在QQ空间使用
        //oks.setSiteUrl("http://sharesdk.cn"); // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.show(this); // 启动分享GUI

    }


    private void initData() {
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");
        getData(movie);
        initPageView(movie);
    }

    private void getData(Movie movie) {
        //拼接影片信息, 导演， 主演，又名， 上映时间， 类型， 片长，地区， 语言，IMDB
        StringBuilder movieBuilder = new StringBuilder();
        Resources resources = this.getResources();

        movieBuilder.append(resources.getString(R.string.movie_directors));
        for (Movie.DirectorsBean director : movie.getDirectors()) {
            movieBuilder.append(director.getName());
            movieBuilder.append(" ");
        }
        movieBuilder.append("\n");

        //主演
        movieBuilder.append(resources.getString(R.string.movie_casts));
        for (Movie.CastsBean cast : movie.getCasts()) {
            movieBuilder.append(cast.getName());
            movieBuilder.append(" ");
        }
        movieBuilder.append("\n");

        //又名
        movieBuilder.append(resources.getString(R.string.movie_aka));
        movieBuilder.append(movie.getOriginal_title());
        movieBuilder.append("\n");

        movieBuilder.append(resources.getString(R.string.movie_year));
        movieBuilder.append(movie.getYear());
        movieBuilder.append("\n");

        movieBuilder.append(resources.getString(R.string.movie_genres));
        for (int index = 0; index < movie.getGenres().size(); index++) {
            movieBuilder.append(movie.getGenres().get(index));
            movieBuilder.append(" / ");
        }
        movieBuilder.append("\n");

        movieBuilder.append(resources.getString(R.string.movie_during));
        movieBuilder.append(resources.getString(R.string.movie_not_find));
        movieBuilder.append("\n");

        movieBuilder.append(resources.getString(R.string.movie_countries));
        movieBuilder.append(resources.getString(R.string.movie_not_find));
        movieBuilder.append("\n");

        movieBuilder.append(resources.getString(R.string.movie_languages));
        movieBuilder.append(resources.getString(R.string.movie_not_find));
        movieBuilder.append("\n");

        movieBuilder.append(resources.getString(R.string.movie_imdb));
        movieBuilder.append(resources.getString(R.string.movie_not_find));
        movieBuilder.append("\n");

        mMovieInfo = movieBuilder.toString();
        mMovieAlt = movie.getAlt();
    }

    /**
     * 初始化MD风格的界面
     *
     * @param movie
     */
    private void initPageView(Movie movie) {
        mMovieCollapsingToolbar.setTitle(movie.getTitle());
        Picasso.with(mMovieImage.getContext())
                .load(movie.getImages().getLarge())
                .into(mMovieImage);
    }

    private void SetUpViewPager(ViewPager movieViewpager) {

        MovieDetailFragment movieInfoFragment = MovieDetailFragment.createInstance(mMovieInfo, ConstContent.TYPE_MOVIE_INFO);
        MovieDetailFragment movieWebsiteFragment = MovieDetailFragment.createInstance(mMovieAlt, ConstContent.TYPE_MOVIE_WEBSITE);
        mMovieDetailPagerAdapter = new MovieDetailPagerAdapter(getSupportFragmentManager());
        mMovieDetailPagerAdapter.addFragment(movieInfoFragment, getString(R.string.movie_info));
        mMovieDetailPagerAdapter.addFragment(movieWebsiteFragment, getString(R.string.movie_description));
        movieViewpager.setAdapter(mMovieDetailPagerAdapter);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        //android.R.id.home对应应用程序图标的id
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

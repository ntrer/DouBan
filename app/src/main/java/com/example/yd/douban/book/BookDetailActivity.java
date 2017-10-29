package com.example.yd.douban.book;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.yd.douban.R;
import com.example.yd.douban.bean.Book;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookDetailActivity extends AppCompatActivity {

    @BindView(R.id.ivImage)
    ImageView mIvImage;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mBookCollapsingToolbar;
    @BindView(R.id.sliding_tabs)
    TabLayout mBookSlidingTabs;
    @BindView(R.id.viewpager)
    ViewPager mBookViewpager;

    private Book mBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusbar();
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        initData();
        SetUpViewPager(mBookViewpager);
        mBookSlidingTabs.setupWithViewPager(mBookViewpager);
    }


    /**
     * //5.0以上设置状态栏透明
     */
    private void setStatusbar()
    {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            //下面这一行呢是android4.0起推荐大家使用的将布局延伸到状态栏的代码，配合5.0的设置状态栏颜色可谓天作之合
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    private void initData() {
        mBook = (Book) getIntent().getSerializableExtra("book");
        initPageView(mBook);
    }


    /**
     * 初始化MD风格的界面
     * @param book
     */
    private void initPageView(Book book) {
        mBookCollapsingToolbar.setTitle(book.getTitle());
        Picasso.with(mIvImage.getContext())
                .load(book.getImages().getLarge())
                .into(mIvImage);

    }



    private void SetUpViewPager(ViewPager bookViewpager) {
        BookDetailPagerAdapter adapter = new BookDetailPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(BookDetailFragment.newInstance(mBook.getSummary()), getString(R.string.book_content_desc));
        adapter.addFragment(BookDetailFragment.newInstance(mBook.getAuthor_intro()), getString(R.string.book_author_desc));
        adapter.addFragment(BookDetailFragment.newInstance(mBook.getCatalog()), getString(R.string.book_catalog));
        bookViewpager.setAdapter(adapter);

    }






}

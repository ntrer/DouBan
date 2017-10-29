package com.example.yd.douban;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yd.douban.about.AboutFragment;
import com.example.yd.douban.api.DouBanManager;
import com.example.yd.douban.blog.BlogFragment;
import com.example.yd.douban.book.BookFragment;
import com.example.yd.douban.book.BooksContract;
import com.example.yd.douban.book.BooksPresenter;
import com.example.yd.douban.login.LoginActivity;
import com.example.yd.douban.movie.MovieFragment;
import com.example.yd.douban.movie.MoviesContract;
import com.example.yd.douban.movie.MoviesPresenter;
import com.example.yd.douban.utils.ACache;
import com.example.yd.douban.utils.ConstContent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_container)
    LinearLayout mTabContainer;
    @BindView(R.id.frame_container)
    FrameLayout mFrameContainer;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private CircleImageView mProfileView;
    private TextView mProfileName;
    private DouBanPagerAdapter mDouBanPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar();
        initDrawerLayout();
        initAndSetupNavigation();
        setupNavigationHeader();
        SetUpViewPager(mViewpager);
        mTabLayout.setupWithViewPager(mViewpager);
        initUser();
    }


    private void SetUpViewPager(ViewPager viewpager) {
        MovieFragment moviesFragment = MovieFragment.newInstance();
        BookFragment bookFragment = BookFragment.newInstance();
        mDouBanPagerAdapter = new DouBanPagerAdapter(getSupportFragmentManager());
        mDouBanPagerAdapter.addFragment(moviesFragment, getApplicationContext().getResources().getString(R.string.tab_movies_fragment));
        mDouBanPagerAdapter.addFragment(bookFragment, getApplicationContext().getResources().getString(R.string.tab_books_fragment));
        viewpager.setAdapter(mDouBanPagerAdapter);
        //在创建Fragment后，创建各自对应的Presenter
        CreatePresenter(moviesFragment, bookFragment);
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
    }

    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        mDrawerLayout.addDrawerListener(drawerToggle);

    }


    private void initAndSetupNavigation() {

        mNavigationView.setCheckedItem(R.id.navigation_item_movies);
        initOthersFragment();
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                boolean isClickLoginItem = false;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                //for show or hide fragment
                switch (item.getItemId()) {
                    case R.id.navigation_item_movies:
                    case R.id.navigation_item_book:
                        if (mTabContainer.getVisibility() == View.GONE) {
                            mTabContainer.setVisibility(View.VISIBLE);
                        }
                        if (mFrameContainer.getVisibility() == View.VISIBLE) {
                            mFrameContainer.setVisibility(View.GONE);
                        }

//                        getSupportFragmentManager().getFragments().forEach(new Consumer<Fragment>() {
//                            @Override
//                            public void accept(Fragment fragment) {
//                                if (fragment.getTag().equals(ConstContent.MENU_BLOG) ||
//                                        fragment.getTag().equals(ConstContent.MENU_ABOUT)) {
//                                    transaction.hide(fragment);
//                                } else {
//                                    transaction.show(fragment);
//                                }
//                            }
//                        });
                        List<Fragment> fragments = getSupportFragmentManager().getFragments();
                        for (Fragment fragment : fragments) {
                            if (fragment.getTag().equals(ConstContent.MENU_BLOG) ||
                                    fragment.getTag().equals(ConstContent.MENU_ABOUT)) {
                                transaction.hide(fragment);
                            } else {
                                transaction.show(fragment);
                            }
                        }

                        break;
                    case R.id.navigation_item_blog:
                    case R.id.navigation_item_about:
                        if (mTabContainer.getVisibility() == View.VISIBLE) {
                            mTabContainer.setVisibility(View.GONE);
                        }
                        if (mFrameContainer.getVisibility() == View.GONE) {
                            mFrameContainer.setVisibility(View.VISIBLE);
                        }
                        break;

                    default:
                        break;
                }

                //for all fragments
                switch (item.getItemId()) {

                    case R.id.navigation_item_movies:
                        mViewpager.setCurrentItem(ConstContent.TAB_INDEX_MOVIES);

                        break;
                    case R.id.navigation_item_book:
                        mViewpager.setCurrentItem(ConstContent.TAB_INDEX_BOOK);

                        break;
                    case R.id.navigation_item_about:
                    case R.id.navigation_item_blog:

//                       getSupportFragmentManager().getFragments().forEach(new Consumer<Fragment>() {
//                            @Override
//                            public void accept(Fragment fragment) {
//                                String fragmentTag = fragment.getTag();
//                                if ((item.getItemId() == R.id.navigation_item_blog && fragmentTag.equals(ConstContent.MENU_BLOG))
//                                        || (item.getItemId() == R.id.navigation_item_about && fragmentTag.equals(ConstContent.MENU_ABOUT))) {
//                                    transaction.show(fragment);
//                                } else {
//                                    transaction.hide(fragment);
//                                }
//
//                            }
//                        });

                        List<Fragment> fragments = getSupportFragmentManager().getFragments();
                        for (Fragment fragment : fragments) {
                            String fragmentTag = fragment.getTag();
                            if ((item.getItemId() == R.id.navigation_item_blog && fragmentTag.equals(ConstContent.MENU_BLOG))
                                    || (item.getItemId() == R.id.navigation_item_about && fragmentTag.equals(ConstContent.MENU_ABOUT))) {
                                transaction.show(fragment);
                            } else {
                                transaction.hide(fragment);
                            }
                        }


                        break;

                    case R.id.navigation_item_login:
                        isClickLoginItem = true;

                        break;

                    default:
                        break;
                }

                item.setCheckable(true);
                transaction.commit();
                mDrawerLayout.closeDrawers();

                return true;
            }
        });
    }


    private void setupNavigationHeader() {
        View headView = mNavigationView.inflateHeaderView(R.layout.navigation_header);
        mProfileView = (CircleImageView) headView.findViewById(R.id.img_profile_photo);
        mProfileName = (TextView) headView.findViewById(R.id.txt_profile_name);

    }

    private void initOthersFragment() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BlogFragment jianshuFragment = BlogFragment.newInstance();
        AboutFragment aboutFragment = AboutFragment.newInstance();
        transaction.add(R.id.frame_container, jianshuFragment, ConstContent.MENU_BLOG);
        transaction.add(R.id.frame_container, aboutFragment, ConstContent.MENU_ABOUT);
        transaction.commit();

    }


    /**
     * 创建Presenter
     *
     * @param moviesFragment
     */
    private void CreatePresenter(MoviesContract.View moviesFragment, BooksContract.View bookFragment) {
        MoviesPresenter moviesPresenter = new MoviesPresenter(DouBanManager.createDoubanService(), moviesFragment);
        BooksPresenter booksPresenter = new BooksPresenter(DouBanManager.createDoubanService(), bookFragment);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 得到用户信息后，进行显示
     * @param user
     */
//    private void initUserHeadView(User user){
//
//        Glide.with(this).load(user.getLogo_url()).transform(new GlideCircleTransform(this)).into(mUserHeadView);
//
//        mTextUserName.setText(user.getUsername());
//
//        headerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//    }

    /**
     * 初始化用户信息
     */
    private void initUser(){
        //得到用户信息缓存
        Object objUser= ACache.get(this).getAsObject(ConstContent.USER);
        //如果缓存为空，则重新登陆
        if(objUser ==null){
            mProfileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            });

        }
        //否则保持登陆状态
        else{
//            User user = (User) objUser;
//            initUserHeadView(user);
        }

    }




}

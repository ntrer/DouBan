package com.example.yd.douban.book;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.yd.douban.R;
import com.example.yd.douban.bean.Book;
import com.example.yd.douban.utils.EndlessRecyclerViewScrollListener;
import com.example.yd.douban.utils.ScrollChildSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookFragment extends Fragment implements BooksContract.View {


    @BindView(R.id.recycler_books)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_no_books)
    LinearLayout mLlNoBooks;
    Unbinder unbinder;
    @BindView(R.id.book_refresh_layout)
    RelativeLayout mBookRefreshLayout;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.search)
    EditText mSearch;
    private BooksAdapter2 mBooksAdapter;
    private BooksContract.Presenter mPresenter;
    private List<Book> mAdapterData = new ArrayList<>();
    private String searchText;
    //下拉刷新控件
    private ScrollChildSwipeRefreshLayout swipeRefreshLayout;
    //上拉加载更多监听
    private EndlessRecyclerViewScrollListener scrollListener;

    public BookFragment() {
        // Required empty public constructor
    }


    /**
     * 返回MovieFragment的实例，调用这个方法相当于创建MovieFragment对象
     *
     * @return
     */
    public static BookFragment newInstance() {
        return new BookFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_Layout);
        unbinder = ButterKnife.bind(this, view);
        search();
        initRecyclerView();
        return view;
    }


    private void search()
    {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPresenter != null) {
                    searchText = mSearch.getText().toString();
                    //请求数据
                    mPresenter.requestWithText(searchText);
                }

            }
        });
    }


    private void initRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            final GridLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
            mRecyclerView.setLayoutManager(layoutManager);
            mBooksAdapter = new BooksAdapter2(getContext(), R.layout.recyclerview_book_item, mAdapterData);
            mRecyclerView.setAdapter(mBooksAdapter);
            mBooksAdapter.setOnItemClickListener(new BooksAdapter2.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Book item = mBooksAdapter.getItem(position);
                    Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                    intent.putExtra("book", item);
                    ImageView mBookCover = view.findViewById(R.id.book_cover);
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), mBookCover, "cover").toBundle();
                    ActivityCompat.startActivity(getActivity(), intent, bundle);
                }
            });
            initSwipeRefreshLayout(layoutManager);
        }

    }


    private void initSwipeRefreshLayout(GridLayoutManager layoutManger) {
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        swipeRefreshLayout.setScrollUpChild(mRecyclerView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadRefreshedBooks(true,searchText);
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManger) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mPresenter.loadMoreBooks(totalItemsCount,searchText);
            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);

    }


    @Override
    public void setPresenter(BooksContract.Presenter presenter) {
        mPresenter = presenter;
    }


    /**
     * 显示数据
     *
     * @param books
     */
    @Override
    public void showRefreshedBooks(List<Book> books) {

        mRecyclerView.setVisibility(View.VISIBLE);
        mSearch.setVisibility(View.GONE);

        //判断数据集合中是否有数据，如果有数据，且第一条数据的ID与集合中第一条一样，说明服务器数据没有更新
        //那么就并不用重新往适配器添加数据
        if (mAdapterData.size() != 0 && books.get(0).getId().equals(mAdapterData.get(0).getId())) {
            return;
        }

        mAdapterData.clear();
        mAdapterData.addAll(books);
        mBooksAdapter.replaceData(mAdapterData);


    }

    @Override
    public void showLoadedMoreBooks(List<Book> books) {
        mAdapterData.addAll(books);
        mBooksAdapter.replaceData(mAdapterData);
    }

    @Override
    public void showNoBooks() {

    }

    @Override
    public void showNoLoadedMoreBooks() {
        Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void setRefreshedIndicator(final boolean active) {

        //final ScrollChildSwipeRefreshLayout swipeRefresh=getView().findViewById(R.id.book_refresh_layout);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(active);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

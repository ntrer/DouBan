package com.example.yd.douban.book;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yd.douban.R;
import com.example.yd.douban.bean.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by YD on 2017/10/26.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewHolder> {
    private List<Book> mBookList;
    private Context mContext;
    @LayoutRes
    private int LayoutResId;
    private Book mBook;
    private OnItemClickListener mOnItemClickListener = null;

    public BooksAdapter(Context context, List<Book> bookList, int layoutResId) {
        this.mContext = context;
        setData(bookList);
        this.LayoutResId = layoutResId;
    }

    //这里这样子来设置数据，是因为在onCreateView里先初始化了RecyclerView
    //和adapter,此时传入adapter里的数据为空，然后调用请求数据的方法后，会调用
    //showMovies方法，在showMovies里调用replaceData方法将数据传入了adapter
    public void setData(List<Book> bookList) {
        this.mBookList = bookList;
    }


    public void replaceData(List<Book> bookList) {
        setData(bookList);
        notifyDataSetChanged();

    }

    /**
     * 实现监听点击事件的接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position, ImageView imageView);
    }

    /**
     * 设置点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;

    }

    @Override
    public BooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(mContext).inflate(LayoutResId, parent, false);
        return new BooksViewHolder(itemview, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(BooksViewHolder holder, int position) {
        mBook = mBookList.get(position);
        if (mBook != null) {
            //get the prefix string
            String prefixSubTitle = mContext.getString(R.string.prefix_subtitle);
            String prefixAuthor = mContext.getString(R.string.prefix_author);
            String prefixPubDate = mContext.getString(R.string.prefix_pubdata);
            String prefixPages = mContext.getString(R.string.prefix_pages);
            String prefixPrice = mContext.getString(R.string.prefix_price);

            holder.bookTitle.setText(mBook.getTitle());
            holder.bookAuthor.setText(String.format(prefixAuthor, mBook.getAuthor()));
            holder.bookSubTitle.setText(String.format(prefixSubTitle, mBook.getSubtitle()));
            holder.bookPubDate.setText(String.format(prefixPubDate, mBook.getPubdate()));
            holder.bookPages.setText(String.format(prefixPages, mBook.getPages()));
            holder.bookPrice.setText(String.format(prefixPrice, mBook.getPrice()));

            Picasso.with(mContext)
                    .load(mBook.getImages().getLarge())
                    .placeholder(mContext.getResources().getDrawable(R.mipmap.ic_launcher))
                    .into(holder.bookImage);

        }

    }

    /**
     * 得到对应位置的数据
     *
     * @param position
     * @return
     */
    public Book getItem(int position) {
        if (position >= mBookList.size()) return null;
        return mBookList.get(position);
    }

    @Override
    public int getItemCount() {
        return mBookList.size();

    }


    class BooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        ImageView bookImage;
        TextView bookTitle;
        TextView bookAuthor;
        TextView bookSubTitle;
        TextView bookPubDate;
        TextView bookPages;
        TextView bookPrice;
        private BooksAdapter.OnItemClickListener mOnItemClickListener;

        public BooksViewHolder(View itemView, BooksAdapter.OnItemClickListener OnItemClickListener) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            bookImage = (ImageView) itemView.findViewById(R.id.book_cover);
            bookTitle = (TextView) itemView.findViewById(R.id.txt_title);
            bookAuthor = (TextView) itemView.findViewById(R.id.txt_author);
            bookSubTitle = (TextView) itemView.findViewById(R.id.txt_subTitle);
            bookPubDate = (TextView) itemView.findViewById(R.id.txt_pubDate);
            bookPrice = (TextView) itemView.findViewById(R.id.txt_prices);
            bookPages = (TextView) itemView.findViewById(R.id.txt_pages);
            this.mOnItemClickListener = OnItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemView == null) return;
            Context context = itemView.getContext();
            if (context == null) return;

            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, getLayoutPosition(), bookImage);
            }
        }
    }

}

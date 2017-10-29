package com.example.yd.douban.book;

import android.content.Context;

import com.example.yd.douban.R;
import com.example.yd.douban.base.BaseViewHolder;
import com.example.yd.douban.base.SimpleAdapter;
import com.example.yd.douban.bean.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by YD on 2017/10/26.
 */

public class BooksAdapter2 extends SimpleAdapter<Book> {


    public BooksAdapter2(Context context, int layoutResId, List<Book> datas) {
        super(context, layoutResId, datas);
    }

    @Override
    protected void bindData(BaseViewHolder viewHoder, Book item) {

        //get the prefix string
        String prefixSubTitle = context.getString(R.string.prefix_subtitle);
        String prefixAuthor = context.getString(R.string.prefix_author);
        String prefixPubDate = context.getString(R.string.prefix_pubdata);
        String prefixPages = context.getString(R.string.prefix_pages);
        String prefixPrice = context.getString(R.string.prefix_price);

        viewHoder.getTextView(R.id.txt_title).setText(item.getTitle());
        viewHoder.getTextView(R.id.txt_author).setText(String.format(prefixAuthor, item.getAuthor()));
        viewHoder.getTextView(R.id.txt_subTitle).setText(String.format(prefixSubTitle, item.getSubtitle()));
        viewHoder.getTextView(R.id.txt_pubDate).setText(String.format(prefixPubDate, item.getPubdate()));
        viewHoder.getTextView(R.id.txt_prices).setText(String.format(prefixPages, item.getPages()));
        viewHoder.getTextView(R.id.txt_pages).setText(String.format(prefixPrice, item.getPrice()));
        Picasso.with(context)
                .load(item.getImages().getLarge())
                .placeholder(context.getResources().getDrawable(R.mipmap.ic_launcher))
                .into(viewHoder.getImageView(R.id.book_cover));

    }
}

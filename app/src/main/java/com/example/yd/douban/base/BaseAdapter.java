package com.example.yd.douban.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *adapter的封装
 */
public abstract class BaseAdapter<T,H extends  BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder>{


    protected final Context context;

    protected final int layoutResId;

    protected List<T> datas;


    private OnItemClickListener mOnItemClickListener = null;


    /**
     * 实现监听点击事件的接口
     */
    public  interface OnItemClickListener {
        void onItemClick(View view, int position);
    }



    public BaseAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }


    public BaseAdapter(Context context, int layoutResId, List<T> datas) {
        setData(datas);
        this.context = context;
        this.layoutResId = layoutResId;
    }


    /**
     * 相当于getView 创建ViewHolder部分代码
     * 创建ViewHolder
     *
     * @param viewGroup
     * @param viewType 当前的类型
     * @return
     */
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, false);
        BaseViewHolder vh = new BaseViewHolder(view,mOnItemClickListener);
        return vh;
    }


    public void setData(List<T> data)
    {
        this.datas = data;
    }


    public void replaceData(List<T> data) {
        setData(data);
        notifyDataSetChanged();
    }

    /**
     * 相当于getview中的绑定数据模块
     *
     * @param viewHoder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseViewHolder viewHoder,  int position) {
        T item = getItem(position);
        bindData(viewHoder,item);
    }


    @Override
    public int getItemCount() {

        if(datas!=null && datas.size()>0)
            return datas.size();
        return 0;
    }

    /**
     * 得到对应位置的数据
     * @param position
     * @return
     */
    public T getItem(int position) {
        if (position >= datas.size()) return null;
        return datas.get(position);
    }


    /**
     * 得到所有数据
     * @return
     */
    public List<T> getDatas(){

        return  datas;
    }



    /**
     * Implement this method and use the helper to adapt the view to the given item.
     * @param viewHoder A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract void bindData(BaseViewHolder viewHoder, T item);


    /**
     * 设置点击监听
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;

    }




}

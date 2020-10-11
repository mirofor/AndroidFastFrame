package com.fast.library.banner.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.fast.library.R;
import com.fast.library.banner.holder.BannerHolderCreator;
import com.fast.library.banner.holder.Holder;
import com.fast.library.banner.view.BannerViewPager;
import java.util.List;

/**
 * 说明：BannerAdapter
 */
public class BannerAdapter<T> extends PagerAdapter {

    private BannerHolderCreator mHolderCreator;
    protected List<T> mDatas;
    private boolean canLoop;
    private BannerViewPager mViewPager;
    private final int MULTIPLE_COUNT = 3;

    public BannerAdapter(BannerHolderCreator holderCreator,List<T> data){
        this.mDatas = data;
        this.mHolderCreator = holderCreator;
    }

    @Override
    public int getCount() {
        return canLoop ? getRealCount() * MULTIPLE_COUNT : getRealCount();
    }

    public int toRealPosition(int position){
        int realCount = getRealCount();
        if (realCount == 0){
            return 0;
        }
        return position % realCount;
    }

    public int getRealCount(){
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getView(toRealPosition(position),null,container);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        container.removeView(view);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        int position = mViewPager.getCurrentItem();
        if (position == 0){
            position = mViewPager.getFirstItem();
        }else if (position == getCount() - 1){
            position = mViewPager.getLastItem();
        }
        try{
            mViewPager.setCurrentItem(position,false);
        }catch (Exception e){}
    }

    public View getView(int position,View view,ViewGroup container){
        Holder holder = null;
        if (view == null){
            holder = (Holder)mHolderCreator.createHolder();
            view = holder.createView(container.getContext());
            view.setTag(R.id.banner_item_tag,holder);
        }else {
            holder = (Holder)view.getTag(R.id.banner_item_tag);
        }
        if (mDatas != null && !mDatas.isEmpty()){
            holder.convert(container.getContext(),position,mDatas.get(position));
        }
        return view;
    }

    /**
     * 说明：设置是否可以轮播
     * @param canLoop
     */
    public void setCanLoop(boolean canLoop){
        this.canLoop = canLoop;
    }

    public void setViewPager(BannerViewPager viewPager){
        this.mViewPager = viewPager;
    }
}

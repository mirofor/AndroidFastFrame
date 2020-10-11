package com.fast.library.Adapter.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * 说明：ViewPagerAdapter
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<ViewPageInfo> infos;
    private Context context;

    public static ViewPagerAdapter create(AppCompatActivity activity,Class []clazz){
        return create(activity,null,clazz,null);
    }

    public static ViewPagerAdapter create(AppCompatActivity activity, String []title, Class []clazz, Bundle []bundles){
        if (clazz == null){
            return null;
        }else {
            ArrayList<ViewPageInfo> infos = new ArrayList<>(clazz.length);
            for (int i = 0;i < clazz.length;i++){
                String tempTitle = null;
                Bundle tempBundle = null;
                if (title != null && title.length >= clazz.length){
                    tempTitle = title[i];
                }
                if (bundles != null && bundles.length >= clazz.length){
                    tempBundle = bundles[i];
                }
                ViewPageInfo info = new ViewPageInfo(tempTitle,clazz[i],tempBundle);
                infos.add(info);
            }
            return new ViewPagerAdapter(activity.getSupportFragmentManager(),activity,infos);
        }
    }

    public ViewPagerAdapter(FragmentManager fm, Context context, List<ViewPageInfo> infos) {
        super(fm);
        this.context = context;
        this.infos = infos == null ? new ArrayList<ViewPageInfo>(0) : infos;
    }

    @Override
    public Fragment getItem(int position) {
        ViewPageInfo info = infos.get(position);
        return Fragment.instantiate(context,info.clazz.getName(),info.params);
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return infos.get(position).title;
    }
}

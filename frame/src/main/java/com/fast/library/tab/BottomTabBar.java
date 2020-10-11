package com.fast.library.tab;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fast.library.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

public class BottomTabBar extends LinearLayout implements ViewPager.OnPageChangeListener, OnClickListener {
    private Context context;
    private LinearLayout mLayout;
    private View mDivider;
    private float imgWidth = 0.0F;
    private float imgHeight = 0.0F;
    private float fontSize = 14.0F;
    private float fontImgPadding;
    private float paddingTop;
    private float paddingBottom;
    private int selectColor = Color.parseColor("#626262");
    private int unSelectColor = Color.parseColor("#F1453B");
    private float dividerHeight;
    private boolean isShowDivider = false;
    private int dividerBackgroundColor = Color.parseColor("#CCCCCC");
    private int tabBarBackgroundColor = Color.parseColor("#FFFFFF");
    private List<String> tabIdList = new ArrayList<>();
    private List<Class> FragmentList = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();
    private List<Drawable> selectdrawableList = new ArrayList<>();
    private List<Drawable> unselectdrawableList = new ArrayList<>();
    private ViewPager mViewpager;
    private FragmentManager mFragmentManager;
    private LinearLayout mTabContent;
    private LayoutParams tab_item_imgparams;
    private LayoutParams tab_item_tvparams;
    private int mReplaceLayout = 0;
    private BottomTabBar.OnTabChangeListener listener = null;
    View last = null;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        this.changeTab(position);
        if (this.listener !=null){
            this.listener.onViewPagerSelected(position);
        }
    }

    private void changeTab(int position) {
        if (position + 1 > this.mTabContent.getChildCount()) {
            throw new IndexOutOfBoundsException("onPageSelected:" + (position + 1) + "，of Max mTabContent ChildCount:" + this.mTabContent.getChildCount());
        } else {
            for (int i = 0; i < this.mTabContent.getChildCount(); ++i) {
                View TabItem = this.mTabContent.getChildAt(i);
                if (i == position) {
                    ((TextView) TabItem.findViewById(R.id.tab_item_tv)).setTextColor(this.selectColor);
                    if (!this.selectdrawableList.isEmpty()) {
                        TabItem.findViewById(R.id.tab_item_img).setBackground((Drawable) this.selectdrawableList.get(i));
                    }
                } else {
                    ((TextView) TabItem.findViewById(R.id.tab_item_tv)).setTextColor(this.unSelectColor);
                    if (!this.selectdrawableList.isEmpty()) {
                        TabItem.findViewById(R.id.tab_item_img).setBackground((Drawable) this.unselectdrawableList.get(i));
                    }
                }
            }

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onClick(View v) {
        if (this.last == v) {
            Log.e("BottomTabBar", "重复点击");
        } else {
            this.last = v;

            for (int i = 0; i < this.tabIdList.size(); ++i) {
                if (((String) this.tabIdList.get(i)).equals(v.getTag())) {
                    if (this.mViewpager != null) {
                        this.mViewpager.setCurrentItem(i);
                    }

                    if (this.mFragmentManager != null) {
                        if (this.mReplaceLayout == 0) {
                            throw new IllegalStateException("Must input ReplaceLayout of mReplaceLayout");
                        }

//                        this.relaceFrament(i);

                        this.showOrHideFragment(mReplaceLayout, i);
                        this.changeTab(i);
                    }

                    if (this.listener != null) {
                        this.listener.onTabChange(i, v);
                    }
                }
            }

        }
    }

    private Fragment currentSupportFragment;

    public void showOrHideFragment(int srcView, int position) {
        Fragment targetFragment = mFragments.get(position);
        if (targetFragment.equals(currentSupportFragment)) {
            return;
        }
        FragmentTransaction transaction = mFragmentManager
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(srcView, targetFragment, targetFragment.getClass()
                    .getName());
        }
        if (currentSupportFragment != null
                && currentSupportFragment.isVisible()) {
            transaction.hide(currentSupportFragment);
        }
        if (targetFragment.isHidden()) {
            transaction.show(targetFragment);

            if (this.listener != null) {
                this.listener.onFragmentShow(targetFragment);
            }
        }
        currentSupportFragment = targetFragment;
        transaction.commitAllowingStateLoss();
    }

    protected void baseAddFragment(int container, Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();
        if (fragment != null) {
            if (fragment.isAdded()) {
                fragmentTransaction.show(fragment);
            } else {
                fragmentTransaction.replace(container, fragment);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();

    }

    private void relaceFrament(int i) {
        Class aClass = (Class) this.FragmentList.get(i);
        Class clazz = null;

        try {
            clazz = Class.forName(aClass.getName());
            Fragment Fragment = (Fragment) clazz.newInstance();
            FragmentTransaction fragmentTransaction = this.mFragmentManager.beginTransaction();
            fragmentTransaction.replace(this.mReplaceLayout, Fragment);
            fragmentTransaction.commit();
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public BottomTabBar(Context context) {
        super(context);
        this.context = context;
    }

    public BottomTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.BottomTabBar);
        if (attributes != null) {
            this.imgWidth = attributes.getDimension(R.styleable.BottomTabBar_tab_img_width, (float) this.dp2px(30.0F));
            this.imgHeight = attributes.getDimension(R.styleable.BottomTabBar_tab_img_height, (float) this.dp2px(30.0F));
            this.fontSize = attributes.getDimension(R.styleable.BottomTabBar_tab_font_size, 14.0F);
            this.paddingTop = attributes.getDimension(R.styleable.BottomTabBar_tab_padding_top, (float) this.dp2px(2.0F));
            this.fontImgPadding = attributes.getDimension(R.styleable.BottomTabBar_tab_img_font_padding, (float) this.dp2px(3.0F));
            this.paddingBottom = attributes.getDimension(R.styleable.BottomTabBar_tab_padding_bottom, (float) this.dp2px(5.0F));
            this.dividerHeight = attributes.getDimension(R.styleable.BottomTabBar_tab_divider_height, (float) this.dp2px(1.0F));
            this.isShowDivider = attributes.getBoolean(R.styleable.BottomTabBar_tab_isshow_divider, false);
            this.selectColor = attributes.getColor(R.styleable.BottomTabBar_tab_selected_color, Color.parseColor("#F1453B"));
            this.unSelectColor = attributes.getColor(R.styleable.BottomTabBar_tab_unselected_color, Color.parseColor("#626262"));
            this.tabBarBackgroundColor = attributes.getColor(R.styleable.BottomTabBar_tab_bar_background, Color.parseColor("#FFFFFF"));
            this.dividerBackgroundColor = attributes.getColor(R.styleable.BottomTabBar_tab_divider_background, Color.parseColor("#CCCCCC"));
            attributes.recycle();
        }

    }

    public BottomTabBar initFragmentorViewPager(ViewPager viewPager) {
        if (viewPager == null) {
            throw new IllegalStateException("Must input Viewpager of initViewPager");
        } else {
            this.mViewpager = viewPager;
            this.mViewpager.addOnPageChangeListener(this);
            this.initView();
            return this;
        }
    }

    public BottomTabBar initFragmentorViewPager(FragmentManager FragmentManager) {
        if (FragmentManager == null) {
            throw new IllegalStateException("Must input FragmentManager of initFragment");
        } else {
            this.mFragmentManager = FragmentManager;
            this.initView();
            return this;
        }
    }

    private void initView() {
        this.mLayout = (LinearLayout) LayoutInflater.from(this.context).inflate(R.layout.fast_frame_bottom_tab_bar, (ViewGroup) null);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        this.mLayout.setLayoutParams(layoutParams);
        this.mLayout.setBackgroundColor(this.tabBarBackgroundColor);
        this.addView(this.mLayout);
        this.mDivider = this.mLayout.findViewById(R.id.mDivider);
        this.mTabContent = (LinearLayout) this.mLayout.findViewById(R.id.mTabContent);
        if (this.isShowDivider) {
            LayoutParams dividerParams = new LayoutParams(-1, (int) this.dividerHeight);
            this.mDivider.setLayoutParams(dividerParams);
            this.mDivider.setBackgroundColor(this.dividerBackgroundColor);
            this.mDivider.setVisibility(View.VISIBLE);
        } else {
            this.mDivider.setVisibility(View.GONE);
        }

    }

    public BottomTabBar addReplaceLayout(int mReplaceLayoutId) {
        this.mReplaceLayout = mReplaceLayoutId;
        return this;
    }

    public BottomTabBar addTabItem(String name, int imgId) {
        return this.addTabItem(name, ContextCompat.getDrawable(this.context, imgId));
    }

    public BottomTabBar addTabItem(String name, int selectimgid, int unselectimgid) {
        return this.addTabItem(name, ContextCompat.getDrawable(this.context, selectimgid), ContextCompat.getDrawable(this.context, unselectimgid));
    }

    public BottomTabBar addTabItem(String name, int imgId, Class Fragmentclazz) {
        return this.addTabItem(name, ContextCompat.getDrawable(this.context, imgId), Fragmentclazz);
    }

    public BottomTabBar addTabItem(String name, int selectimgid, int unselectimgid, Class Fragmentclazz) {
        return this.addTabItem(name, ContextCompat.getDrawable(this.context, selectimgid), ContextCompat.getDrawable(this.context, unselectimgid), Fragmentclazz);
    }

    public BottomTabBar addTabItem(String name, Drawable drawable) {
        LinearLayout TabItem = (LinearLayout) View.inflate(this.context, R.layout.fast_frame_bottom_tab_item, (ViewGroup) null);
        TabItem.setGravity(17);
        TabItem.setTag(name);
        this.tabIdList.add(String.valueOf(TabItem.getTag()));
        TabItem.setOnClickListener(this);
        ImageView tab_item_img = (ImageView) TabItem.findViewById(R.id.tab_item_img);
        this.tab_item_imgparams = new LayoutParams((int) this.imgWidth, (int) this.imgHeight);
        this.tab_item_imgparams.topMargin = (int) this.paddingTop;
        this.tab_item_imgparams.bottomMargin = (int) this.fontImgPadding;
        tab_item_img.setLayoutParams(this.tab_item_imgparams);
        tab_item_img.setBackground(drawable);
        TextView tab_item_tv = (TextView) TabItem.findViewById(R.id.tab_item_tv);
        this.tab_item_tvparams = new LayoutParams(-2, -2);
        this.tab_item_tvparams.bottomMargin = (int) this.paddingBottom;
        tab_item_tv.setLayoutParams(this.tab_item_tvparams);
        tab_item_tv.setTextSize(this.fontSize);
        tab_item_tv.setText(name);
        if (this.tabIdList.size() == 1) {
            this.last = TabItem;
            tab_item_tv.setTextColor(this.selectColor);
        } else {
            tab_item_tv.setTextColor(this.unSelectColor);
        }

        TabItem.setLayoutParams(new LayoutParams(-2, -2, 1.0F));
        this.mTabContent.addView(TabItem);
        return this;
    }

    public BottomTabBar addTabItem(String name, Drawable selectdrawable, Drawable unselectdrawable) {
        this.selectdrawableList.add(selectdrawable);
        this.unselectdrawableList.add(unselectdrawable);
        LinearLayout TabItem = (LinearLayout) View.inflate(this.context, R.layout.fast_frame_bottom_tab_item, (ViewGroup) null);
        TabItem.setGravity(17);
        TabItem.setTag(name);
        this.tabIdList.add(String.valueOf(TabItem.getTag()));
        TabItem.setOnClickListener(this);
        ImageView tab_item_img = (ImageView) TabItem.findViewById(R.id.tab_item_img);
        this.tab_item_imgparams = new LayoutParams((int) this.imgWidth, (int) this.imgHeight);
        this.tab_item_imgparams.topMargin = (int) this.paddingTop;
        this.tab_item_imgparams.bottomMargin = (int) this.fontImgPadding;
        tab_item_img.setLayoutParams(this.tab_item_imgparams);
        TextView tab_item_tv = (TextView) TabItem.findViewById(R.id.tab_item_tv);
        this.tab_item_tvparams = new LayoutParams(-2, -2);
        this.tab_item_tvparams.bottomMargin = (int) this.paddingBottom;
        tab_item_tv.setLayoutParams(this.tab_item_tvparams);
        tab_item_tv.setTextSize(this.fontSize);
        tab_item_tv.setText(name);
        if (this.tabIdList.size() == 1) {
            this.last = TabItem;
            tab_item_tv.setTextColor(this.selectColor);
            tab_item_img.setBackground(selectdrawable);
        } else {
            tab_item_tv.setTextColor(this.unSelectColor);
            tab_item_img.setBackground(unselectdrawable);
        }

        TabItem.setLayoutParams(new LayoutParams(-2, -2, 1.0F));
        this.mTabContent.addView(TabItem);
        return this;
    }

    public BottomTabBar addTabItem(String name, Drawable drawable, Class fragmentClass) {
        Class clazz = null;

        try {
//            clazz = Class.forName(fragmentClass.getName());
//            Fragment var5 = (Fragment) clazz.newInstance();
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        this.FragmentList.add(fragmentClass);
        LinearLayout TabItem = (LinearLayout) View.inflate(this.context, R.layout.fast_frame_bottom_tab_item, (ViewGroup) null);
        TabItem.setGravity(17);
        TabItem.setTag(name);
        this.tabIdList.add(String.valueOf(TabItem.getTag()));
        TabItem.setOnClickListener(this);
        ImageView tab_item_img = (ImageView) TabItem.findViewById(R.id.tab_item_img);
        this.tab_item_imgparams = new LayoutParams((int) this.imgWidth, (int) this.imgHeight);
        this.tab_item_imgparams.topMargin = (int) this.paddingTop;
        this.tab_item_imgparams.bottomMargin = (int) this.fontImgPadding;
        tab_item_img.setLayoutParams(this.tab_item_imgparams);
        tab_item_img.setBackground(drawable);
        TextView tab_item_tv = (TextView) TabItem.findViewById(R.id.tab_item_tv);
        this.tab_item_tvparams = new LayoutParams(-2, -2);
        this.tab_item_tvparams.bottomMargin = (int) this.paddingBottom;
        tab_item_tv.setLayoutParams(this.tab_item_tvparams);
        tab_item_tv.setTextSize(this.fontSize);
        tab_item_tv.setText(name);
        if (this.tabIdList.size() == 1) {
            this.last = TabItem;
            tab_item_tv.setTextColor(this.selectColor);
        } else {
            tab_item_tv.setTextColor(this.unSelectColor);
        }

        TabItem.setLayoutParams(new LayoutParams(-2, -2, 1.0F));
        this.mTabContent.addView(TabItem);
        return this;
    }

    public BottomTabBar addTabItem(String name, Drawable selectdrawable, Drawable unselectdrawable, Class fragmentClass) {
        this.selectdrawableList.add(selectdrawable);
        this.unselectdrawableList.add(unselectdrawable);
        Class clazz = null;

        try {
//            clazz = Class.forName(fragmentClass.getName());
//            Fragment var6 = (Fragment) clazz.newInstance();
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        this.FragmentList.add(fragmentClass);
        LinearLayout TabItem = (LinearLayout) View.inflate(this.context, R.layout.fast_frame_bottom_tab_item, (ViewGroup) null);
        TabItem.setGravity(17);
        TabItem.setTag(name);
        this.tabIdList.add(String.valueOf(TabItem.getTag()));
        TabItem.setOnClickListener(this);
        ImageView tab_item_img = (ImageView) TabItem.findViewById(R.id.tab_item_img);
        this.tab_item_imgparams = new LayoutParams((int) this.imgWidth, (int) this.imgHeight);
        this.tab_item_imgparams.topMargin = (int) this.paddingTop;
        this.tab_item_imgparams.bottomMargin = (int) this.fontImgPadding;
        tab_item_img.setLayoutParams(this.tab_item_imgparams);
        TextView tab_item_tv = (TextView) TabItem.findViewById(R.id.tab_item_tv);
        this.tab_item_tvparams = new LayoutParams(-2, -2);
        this.tab_item_tvparams.bottomMargin = (int) this.paddingBottom;
        tab_item_tv.setLayoutParams(this.tab_item_tvparams);
        tab_item_tv.setTextSize(this.fontSize);
        tab_item_tv.setText(name);
        if (this.tabIdList.size() == 1) {
            this.last = TabItem;
            tab_item_tv.setTextColor(this.selectColor);
            tab_item_img.setBackground(selectdrawable);
        } else {
            tab_item_tv.setTextColor(this.unSelectColor);
            tab_item_img.setBackground(unselectdrawable);
        }

        TabItem.setLayoutParams(new LayoutParams(-2, -2, 1.0F));
        this.mTabContent.addView(TabItem);
        return this;
    }

    public void commit() {
        if (this.mFragmentManager != null) {
            if (this.mReplaceLayout == 0) {
                throw new IllegalStateException("Must input ReplaceLayout of mReplaceLayout");
            }

            if (1 > this.mTabContent.getChildCount()) {
                throw new IndexOutOfBoundsException("onPageSelected:1，of Max mTabContent ChildCount:" + this.mTabContent.getChildCount());
            }

//            this.relaceFrament(0);
            try {
                for (int i = 0; i < this.FragmentList.size(); i++) {
                    mFragments.add((Fragment) this.FragmentList.get(i).newInstance());
                }
                this.showOrHideFragment(mReplaceLayout, 0);

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


            if (this.tabIdList.isEmpty()) {
                throw new IllegalStateException("You Mast addTabItem before commit");
            }
        }
    }

    public BottomTabBar setOnTabChangeListener(BottomTabBar.OnTabChangeListener listener) {
        if (listener != null) {
            this.listener = listener;
        }

        return this;
    }

    public BottomTabBar setImgSize(float width, float height) {
        this.imgWidth = width;
        this.imgHeight = height;
        return this;
    }

    public BottomTabBar setFontSize(float textSize) {
        this.fontSize = textSize;
        return this;
    }

    public BottomTabBar setTabPadding(float top, float middle, float bottom) {
        this.paddingTop = top;
        this.fontImgPadding = middle;
        this.paddingBottom = bottom;
        return this;
    }

    public BottomTabBar setChangeColor(@ColorInt int selectColor, @ColorInt int unSelectColor) {
        this.selectColor = selectColor;
        this.unSelectColor = unSelectColor;
        return this;
    }

    public BottomTabBar setTabBarBackgroundColor(@ColorInt int color) {
        this.tabBarBackgroundColor = color;
        this.mLayout.setBackgroundColor(color);
        return this;
    }

    public BottomTabBar setTabBarBackgroundResource(@DrawableRes int resid) {
        this.mLayout.setBackgroundResource(resid);
        return this;
    }

    @TargetApi(16)
    public BottomTabBar setTabBarBackgroundResource(Drawable drawable) {
        this.mLayout.setBackground(drawable);
        return this;
    }

    public BottomTabBar isShowDivider(boolean isShowDivider) {
        this.isShowDivider = isShowDivider;
        if (isShowDivider) {
            LayoutParams dividerParams = new LayoutParams(-1, (int) this.dividerHeight);
            this.mDivider.setLayoutParams(dividerParams);
            this.mDivider.setBackgroundColor(this.dividerBackgroundColor);
            this.mDivider.setVisibility(View.VISIBLE);
        } else {
            this.mDivider.setVisibility(View.GONE);
        }

        return this;
    }

    public BottomTabBar setDividerHeight(float height) {
        this.dividerHeight = height;
        if (this.isShowDivider) {
            LayoutParams dividerParams = new LayoutParams(-1, (int) this.dividerHeight);
            this.mDivider.setLayoutParams(dividerParams);
        }

        return this;
    }

    public BottomTabBar setDividerColor(@ColorInt int color) {
        this.dividerBackgroundColor = color;
        if (this.isShowDivider) {
            this.mDivider.setBackgroundColor(this.dividerBackgroundColor);
        }

        return this;
    }

    private int dp2px(float dpValue) {
        float scale = this.context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    public interface OnTabChangeListener {
        void onTabChange(int var1, View var2);
        void onFragmentShow(Fragment fragment);
        void onViewPagerSelected(int position);
    }
}

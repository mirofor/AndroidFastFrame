package com.fast.frame;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fast.frame.event.EventUtils;
import com.fast.frame.helper.ToolbarHelper;
import com.fast.frame.interrface.IActivityTitleBar;
import com.fast.frame.interrface.IFrameRegister;
import com.fast.frame.interrface.ILoadingDialog;
import com.fast.library.BaseActivity;
import com.fast.library.R;
import com.fast.library.tools.ViewTools;
import com.fast.library.utils.StringUtils;
import com.fast.library.utils.UIUtils;
import com.fast.mvp.presenter.MvpPresenter;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 说明：ActivityFrame
 */
public abstract class ActivityFrame<Presenter extends MvpPresenter> extends BaseActivity<Presenter>
        implements IFrameRegister,IActivityTitleBar {

    public ImmersionBar mImmersionBar;
    protected ILoadingDialog mLoadingDialog;
    private ToolbarHelper mToolbarHelper;
    private Toolbar mToolbar;
    private View mToolbarTopView;
    private View mCustomTitleView;
    protected TextView mTvTitle;
    Unbinder unbinder;


    @Override
    public void onInitCreate(Bundle bundle) {
        if (isBindButterKnife()){
            unbinder= ButterKnife.bind(this);
        }
        //初始化沉浸式
        if (isImmersionBarEnabled()){
            initImmersionBar();
        }
        if (isRegisterEventBus()){
            EventUtils.registerEventBus(this);
        }
//        初始化LoaderManager
        getSupportLoaderManager().initLoader(createLoaderID(),null,this);
    }

    @Override
    public void onInitStart() {

    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    /**
     * 设置状态栏
     */
    public void initImmersionBar(){
        if (mToolbarTopView !=null){
            mImmersionBar = ImmersionBar.with(this).statusBarView(mToolbarTopView);
        }else {
            mImmersionBar = ImmersionBar.with(this);
        }
        mImmersionBar.init();
    }

    /******************************************************************/

    @Override
    public boolean isRegisterEventBus(){
        return false;
    }

    @Override
    public boolean isBindButterKnife() {
        return true;
    }

    /**
     * 设置状态栏
     */
    public void initImmersionBar(ImmersionBar immersionBar){}
    /******************************************************************/

    @Override
    protected void onDestroy() {
        if (isRegisterEventBus()){
            EventUtils.unRegisterEventBus(this);
        }
        if (isBindButterKnife()){
            if (unbinder !=null){
                unbinder.unbind();
            }
        }
        super.onDestroy();
        if (mImmersionBar != null){
            mImmersionBar.destroy();
        }
    }

    protected ActivityFrame getActivity(){
        return this;
    }

    /****************************** 加载框 *********************************/

    public void showLoading(){
        showLoading(null);
    }

    public void showLoading(@StringRes int text){
        showLoading(UIUtils.getString(text));
    }

    public void showLoading(String text){
        showLoading(text,true);
    }

    public void showLoading(String text,boolean isCancel){
        dismissLoading();
        mLoadingDialog = setLoadingDialog();
        mLoadingDialog.setCancel(isCancel)
                .setText(text)
                .showDialog(getSupportFragmentManager());
    }

    public void dismissLoading(){
        if (mLoadingDialog != null){
            mLoadingDialog.dismissDialog();
        }
    }

    public ILoadingDialog setLoadingDialog(){
        return DialogFrame.loadingDialog();
    }

    /****************************** 加载框 *********************************/

    /****************************** Toolbar *********************************/

    /**
     * 是否显示Toolbar
     * @return
     */
    @Override
    public boolean isShowTitleBar(){
        return false;
    }

    /**
     * 显示返回
     * @return
     */
    @Override
    public boolean isShowTitleBarBack(){
        return false;
    }
    @Override
    public boolean isCustomCancelAction() {
        return false;
    }
    /**
     * 设置标题名称
     */
    @Override
    public void setTitleBarText(String titleText){
        ViewTools.setText(mTvTitle,titleText);
    }

    /**
     * 设置标题名称
     * @return
     */
    @Override
    public String bindTitleBarText(){
        return null;
    }


    /**
     * 设置标题名称
     * @return
     */
    @Override
    public int bindTitleBarTextRes(){
        return 0;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (isShowTitleBar()){
            mToolbarHelper = new ToolbarHelper(this,layoutResID);
            mToolbar = mToolbarHelper.getToolbar();
            mToolbarTopView = mToolbarHelper.getToolbarTopView();
            setContentView(mToolbarHelper.getView());
            if (bindTitleBar() > 0){
                ViewParent parent = mToolbar.getParent();
                if (parent != null && parent instanceof FrameLayout){
                    FrameLayout frameLayoutParent = (FrameLayout) parent;
                    frameLayoutParent.removeAllViews();
                    mCustomTitleView = UIUtils.inflate(bindTitleBar(),frameLayoutParent,true);
                    onCustomTitleBar(mCustomTitleView);
                }
            }else {
                customToolbar(mToolbar);
            }
        }else {
            super.setContentView(layoutResID);
        }
    }

    /**
     * 自定义TitleView
     * @return
     */
    @Override
    public int bindTitleBar(){
        return 0;
    }

    @Override
    public void onCustomToolBar(Toolbar toolbar) {}

    @Override
    public void onCustomTitleBar(View titlebar){}

    private void customToolbar(Toolbar toolbar){
        mTvTitle = bind(R.id.tv_toolbar_top_title);

        setSupportActionBar(mToolbar);
        toolbar.setTitleTextColor(UIUtils.getColor(R.color.white));
        toolbar.setTitle("");
        setTitle("");
        if (bindTitleBarTextRes() > 0){
            mTvTitle.setText(bindTitleBarTextRes());
        }
        if (!StringUtils.isEmpty(bindTitleBarText())){
            mTvTitle.setText(bindTitleBarText());
        }
        if (bindTitleBar() != 0){
            getLayoutInflater().inflate(bindTitleBar(),toolbar);
        }
        if (isShowTitleBarBack()){
            toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ActivityFrame#Toolbar ","clickTitleBack");
//                    onBackPressed();
                    onKeyCodeBackListener();
                }
            });
        }
        onCustomToolBar(toolbar);
    }
    /**
     * android系统返回键按下后，各页面独自处理方法
     */
    @Override
    public void onCancelButtonListener() {
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onKeyCodeBackListener();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void onKeyCodeBackListener() {
        // 子类如果不是直接finish（），想自己处理的话，首先赋值 mIsCancelAction=true
        if (isCustomCancelAction()) {
            // 子类Activity覆盖此方法可以拦截返回键监听，在自己的页面中处理操作
            onCancelButtonListener();
        }  else {// 其他情况，默认关闭页面
            onBackPressed();
        }
    }
    /****************************** Toolbar *********************************/

    /****************************** 菜单设置 *********************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (setMenu() != 0){
            getMenuInflater().inflate(setMenu(), menu);
            return true;
        }else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (setMenu() != 0){
            onMenuItemClick(item.getItemId());
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 说明：菜单点击处理
     * @param id
     */
    protected void onMenuItemClick(int id){}

    /**
     * 说明：设置菜单选项
     * @return
     */
    protected int setMenu(){
        return 0;
    }

    /****************************** 菜单设置 *********************************/

    public void topRightTextClickListener(){

    }
}
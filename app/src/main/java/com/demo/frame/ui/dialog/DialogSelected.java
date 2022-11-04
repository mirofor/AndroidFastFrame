package com.demo.frame.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.demo.frame.R;
import com.fast.library.tools.ViewTools;
import com.fast.library.utils.UIUtils;
import com.fast.library.view.RoundButton;
import java.util.ArrayList;

/**
 * 说明：DialogSelected
 */
public class DialogSelected extends DialogCommon{

    private int titleHeight = 44;
    private int cancelHeight = 54;
    private int cancelMarginTop = 10;
    private int titleCorner = 32;
    private SelectBuilder mBuilder;

    public interface SpanClickListener{
        void onClick(View view, Dialog dialog);
    }

    public static class SelectBuilder{

        public Context context;
        public String titleText;
        public int titleTextSize = 14;
        public int titleTextColor = R.color.c_929292;
        public int titleSolidColor = R.color.white;
        public ArrayList<SelectSpan> spanList;
        public String cancelText = "取消";
        public int cancelTextColor = R.color.c_007aff;
        public int cancelSolidColor = R.color.white;
        public int cancelPressedColor = R.color.c_bb_white;
        public int cancelTextSize = 18;
        public View.OnClickListener cancelClickListener;

        public SelectBuilder(Context context){
            this.context = context;
        }

        public SelectBuilder setTitleText(String titleText) {
            this.titleText = titleText;
            return this;
        }

        public SelectBuilder setTitleTextSize(int titleTextSize) {
            this.titleTextSize = titleTextSize;
            return this;
        }

        public SelectBuilder setTitleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public SelectBuilder setTitleSolidColor(int titleSolidColor) {
            this.titleSolidColor = titleSolidColor;
            return this;
        }

        public SelectBuilder setCancelText(String cancelText) {
            this.cancelText = cancelText;
            return this;
        }

        public SelectBuilder setCancelTextColor(int cancelTextColor) {
            this.cancelTextColor = cancelTextColor;
            return this;
        }

        public SelectBuilder setCancelSolidColor(int cancelSolidColor) {
            this.cancelSolidColor = cancelSolidColor;
            return this;
        }

        public SelectBuilder setCancelPressedColor(int cancelPressedColor) {
            this.cancelPressedColor = cancelPressedColor;
            return this;
        }

        public SelectBuilder setCancelTextSize(int cancelTextSize) {
            this.cancelTextSize = cancelTextSize;
            return this;
        }

        public SelectBuilder setCancelClickListener(View.OnClickListener cancelClickListener) {
            this.cancelClickListener = cancelClickListener;
            return this;
        }

        public SelectBuilder addSelectSpan(SelectSpan... span){
            if (span != null && span.length > 0){
                if (spanList == null){
                    spanList = new ArrayList<>();
                }
                for (int i = 0;i < span.length;i++){
                    spanList.add(span[i]);
                }
            }
            return this;
        }

        public DialogSelected build(){
            return new DialogSelected(context,this);
        }
    }

    public static class SelectSpan{
        public String content;
        public int textColor = R.color.c_007aff;
        public int fontSize = 18;
        public int solidColor = R.color.white;
        public int pressedColor = R.color.c_bb_white;
        public int spanHeight = 54;
        public SpanClickListener listener;
    }

    private DialogSelected(Context context, SelectBuilder builder) {
        super(context);
        this.mBuilder = builder;
    }

    public void showSelected(){
        initBuilder();
        show();
    }

    private void initBuilder(){
        LinearLayout llSelectSpan = ViewTools.find(getDialogView(),R.id.ll_select_span);
        LinearLayout llSelectSpanList = ViewTools.find(getDialogView(),R.id.ll_select_span_list);
        addTitle(llSelectSpanList);
        addSpanList(llSelectSpanList);
        addCancel(llSelectSpan);
    }

    private void addSpanList(LinearLayout llSelectSpan){
        int size = mBuilder.spanList == null ? 0 : mBuilder.spanList.size();
        for (int i = 0;i < size;i++){
            View view = new View(getContext());
            view.setBackgroundColor(UIUtils.getColor(R.color.line));
            ViewGroup.LayoutParams viewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2px(0.5f));
            view.setLayoutParams(viewParams);
            llSelectSpan.addView(view);
            final SelectSpan span = mBuilder.spanList.get(i);
            RoundButton roundButton = new RoundButton(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2px(span.spanHeight));
            roundButton.setLayoutParams(params);
            roundButton.setGravity(Gravity.CENTER);
            if (i == size-1){
                roundButton.setCorner(0,0,titleCorner,titleCorner);
            }
            roundButton.setText(span.content);
            roundButton.setSolidColor(span.solidColor);
            roundButton.setTextColor(UIUtils.getColor(span.textColor));
            roundButton.setPressedColor(span.pressedColor);
            roundButton.setTextSize(span.fontSize);
            if (span.listener != null){
                roundButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        span.listener.onClick(v,DialogSelected.this);
                    }
                });
            }
            llSelectSpan.addView(roundButton);
        }
    }

    private void addCancel(LinearLayout llSelectSpan){
        RoundButton roundButton = new RoundButton(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2px(cancelHeight));
        params.setMargins(0, UIUtils.dip2px(cancelMarginTop),0,0);
        roundButton.setLayoutParams(params);
        roundButton.setGravity(Gravity.CENTER);
        roundButton.setCorner(titleCorner,titleCorner,titleCorner,titleCorner);
        roundButton.setText(mBuilder.cancelText);
        roundButton.setSolidColor(mBuilder.cancelSolidColor);
        roundButton.setPressedColor(mBuilder.cancelPressedColor);
        roundButton.setTextColor(UIUtils.getColor(mBuilder.cancelTextColor));
        if (mBuilder.cancelClickListener != null){
            roundButton.setOnClickListener(mBuilder.cancelClickListener);
        }else {
            roundButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        llSelectSpan.addView(roundButton);
    }

    private void addTitle(LinearLayout llSelectSpan){
        RoundButton roundButton = new RoundButton(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2px(titleHeight));
        roundButton.setLayoutParams(params);
        roundButton.setGravity(Gravity.CENTER);
        roundButton.setCorner(titleCorner,titleCorner,0,0);
        roundButton.setText(mBuilder.titleText);
        roundButton.setSolidColor(mBuilder.titleSolidColor);
        roundButton.setTextColor(UIUtils.getColor(mBuilder.titleTextColor));
        roundButton.setTextSize(mBuilder.titleTextSize);
        llSelectSpan.addView(roundButton);
    }

    @Override
    public void onCreate() {
        getWindow().setGravity(Gravity.BOTTOM);
        getDialogView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public int setDialogView() {
        return R.layout.dialog_selected;
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }

    @Override
    public int addWindowAnimations() {
        return R.style.Dialog_Anim_Bottom;
    }

}

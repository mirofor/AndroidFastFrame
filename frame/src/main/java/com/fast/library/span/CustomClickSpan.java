package com.fast.library.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;

public class CustomClickSpan extends ClickableSpan implements OnClickListener {

	private OnClickSpan onClickSpan;
	private SpanSetting setting;
	private int position;
	
	public CustomClickSpan(SpanSetting setting, int position){
		this.setting = setting;
		this.onClickSpan = setting.getOnClickSpan();
		this.position = position;
	}
	
	@Override
	public void onClick(View widget) {
		if (onClickSpan != null) {
			onClickSpan.onClick(setting,position);
		}
	}
	
	@Override
	public void updateDrawState(TextPaint ds) {
		super.updateDrawState(ds);
		ds.setAntiAlias(true);
		ds.setColor(setting.getColor());
		ds.setStrikeThruText(setting.isStrikeThrough());
		ds.setUnderlineText(setting.isUnderLine());
		ds.setFakeBoldText(setting.isBold());
	}
	
}

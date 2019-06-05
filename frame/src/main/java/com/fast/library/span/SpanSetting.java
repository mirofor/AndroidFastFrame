package com.fast.library.span;

import android.graphics.Color;

public class SpanSetting {
	// 图片
	private int pic;
	// 字符内容
	private CharSequence charSequence;
	// 颜色
	private Integer color = Color.TRANSPARENT;
	// 字体大小
	private float fontSize = 0.0f;
	// 任意内容
	private Object what;
	// 加粗
	private boolean isbold = false;
	// 删除线
	private boolean strikethrough = false;
	// 下划线
	private boolean underline = false;

	private OnClickSpan onClickSpan;

	public SpanSetting(){
		this.charSequence = "";
	}

	public CharSequence getCharSequence() {
		return charSequence;
	}

	public SpanSetting setCharSequence(CharSequence charSequence) {
		this.charSequence = charSequence;
		return this;
	}

	public Integer getColor() {
		return color;
	}

	public SpanSetting setColor(Integer color) {
		this.color = color;
		return this;
	}

	public float getFontSize() {
		return fontSize;
	}

	public SpanSetting setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public OnClickSpan getOnClickSpan() {
		return onClickSpan;
	}

	public SpanSetting setOnClickSpan(OnClickSpan clickSpan) {
		this.onClickSpan = clickSpan;
		return this;
	}

	public Object getWhat() {
		return what;
	}

	public SpanSetting setWhat(Object what) {
		this.what = what;
		return this;
	}

	public boolean isStrikeThrough() {
		return strikethrough;
	}

	public SpanSetting setStrikeThrough(boolean strikethrough) {
		this.strikethrough = strikethrough;
		return this;
	}

	public boolean isUnderLine() {
		return underline;
	}

	public SpanSetting setUnderLine(boolean underline) {
		this.underline = underline;
		return this;
	}

	public boolean isBold() {
		return isbold;
	}

	public SpanSetting setBold(boolean isbold) {
		this.isbold = isbold;
		return this;
	}

	public int getPic() {
		return pic;
	}

	public SpanSetting setPic(int pic) {
		this.pic = pic;
		return this;
	}
}

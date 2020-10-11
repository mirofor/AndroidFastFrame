package com.fast.library.Adapter.refresh.tips;

import android.content.Context;

import com.fast.library.R;
public enum TipsType {

  LOADING(R.layout.fast_frame_tips_loading),
  LOADING_FAILED(R.layout.fast_frame_tips_loading_failed),
  EMPTY(R.layout.fast_frame_tips_empty);

  protected int mLayoutRes;

  TipsType(int layoutRes) {
    this.mLayoutRes = layoutRes;
  }

  protected Tips createTips(Context context) {
    return new Tips(context, mLayoutRes);
  }

}

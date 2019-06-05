package com.fast.library.view.switchlayout;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * 说明：BaseEffects
 */
public class BaseEffects {
//	加速
	public static Interpolator acceInter = new AccelerateInterpolator();
//	减速
	public static Interpolator deceInter = new DecelerateInterpolator();
//	先加速后减速
	public static Interpolator acceToDeceInter = new AccelerateDecelerateInterpolator();
//	开始的时候向后然后向前甩
	public static Interpolator anticInter = new AnticipateInterpolator();
//	向前甩一定值后再回到原来位置
	public static Interpolator overInter = new OvershootInterpolator();
//	开始的时候向后然后向前甩一定值后返回最后的值
	public static Interpolator anticOverInter = new AnticipateOvershootInterpolator();
//	动画结束的时候弹起
	public static Interpolator bounInter = new BounceInterpolator();
//	匀速
	public static Interpolator linearInter = new LinearInterpolator();

	public static Interpolator getMoreQuickEffect() {
		return (Interpolator) acceInter;
	}

	public static Interpolator getMoreSlowEffect() {
		return deceInter;
	}

	public static Interpolator getQuickToSlowEffect() {
		return acceToDeceInter;
	}

	public static Interpolator getBackQuickToForwardEffect() {
		return anticInter;
	}

	public static Interpolator getQuickReScrollEffect() {
		return overInter;
	}

	public static Interpolator getReScrollEffect() {
		return anticOverInter;
	}

	public static Interpolator getBounceEffect() {
		return bounInter;
	}

	public static Interpolator getLinearInterEffect() {
		return linearInter;
	}
}

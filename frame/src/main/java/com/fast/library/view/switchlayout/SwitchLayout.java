package com.fast.library.view.switchlayout;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * 说明：动画工具类
 */
public abstract class SwitchLayout {

	public static class Builder{

		public Activity activity;
		public View view;
		public long duration = 600;
		public Interpolator interpolator = new LinearInterpolator();
		public SwitchAnimationListener animationListener;
		public boolean isClose;
        public int repeatCount = 0;
        public boolean autoStart = true;
	}

    private static void bindAnim(Builder builder,Animation anim){
        anim.setInterpolator(builder.interpolator);
        anim.setDuration(builder.duration);
        anim.setRepeatCount(builder.repeatCount);
        anim.setAnimationListener(new CloseActivityAnimationListener(builder));
        if (builder.activity != null){
            builder.view = getRootView(builder.activity);
        }
        builder.view.setAnimation(anim);
        if (builder.autoStart){
            builder.view.startAnimation(anim);
        }
    }

	public static Animation getSlideFromBottom(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.SlideFromBottom(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
    }

	public static Animation getSlideToBottom(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.SlideToBottom(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
    }

	public static Animation getSlideFromTop(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.SlideFromTop(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
    }

	public static Animation getSlideToTop(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.SlideToTop(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
    }

	public static Animation getSlideFromLeft(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.SlideFromLeft(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
    }

	public static Animation getSlideToLeft(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.SlideToLeft(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
    }

	public static Animation getSlideFromRight(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.SlideFromRight(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
    }

	public static Animation getSlideToRight(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.SlideToRight(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
    }

	public static Animation getFadingIn(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.FadingIn();
        bindAnim(builder,animation);
        return animation;
    }

	public static Animation getFadingOut(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.FadingOut();
        bindAnim(builder,animation);
        return animation;
    }

	public static Animation get3DRotate(@NonNull Builder builder) {
        FlipAnimation rotate3dAnim = null;
        if (builder.activity != null){
            WindowManager wm = builder.activity.getWindowManager();
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            rotate3dAnim = new FlipAnimation(width / 2, height / 2,
                    false);
        }else if (builder.view != null){
            rotate3dAnim = new FlipAnimation(
                    (builder.view.getLeft() + (builder.view.getWidth() / 2)) / 2,
                    (builder.view.getTop() + (builder.view.getHeight() / 2)), false);
        }
        bindAnim(builder,rotate3dAnim);
        return rotate3dAnim;
	}

	public static ObjectAnimator FlipUpDown(@NonNull Builder builder) {
		if (builder.activity != null){
			builder.view = getRootView(builder.activity);
		}
		ObjectAnimator objAnim = ObjectAnimator.ofFloat(builder.view, "rotationX",
				-180, 0);
		objAnim.setInterpolator(builder.interpolator);
		objAnim.setDuration(builder.duration);
		if (builder.activity != null && builder.isClose) {
			objAnim.addListener(new CloseActivityAnimatorListener(builder));
		}
        if (builder.autoStart){
            AnimatorSet as = new AnimatorSet();
            as.play(objAnim);
            as.start();
        }
		return objAnim;
	}

	public static Animation RotateLeftCenterIn(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.RotaLeftCenterIn(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
	}

	public static Animation RotateLeftCenterOut(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.RotaLeftCenterOut(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
	}

	public static Animation RotateCenterIn(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.RotaCenterIn(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
	}

	public static Animation RotateCenterOut(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.RotaCenterOut(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
	}

	public static Animation RotateLeftTopIn(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.RotaLeftTopIn(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
	}

	public static Animation RotateLeftTopOut(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.RotaLeftTopOut(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
	}

	public static Animation ScaleBig(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.ScaleBig(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
	}

	public static Animation ScaleSmall(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.ScaleSmall(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
	}

	public static Animation ScaleBigLeftTop(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.ScaleBigLeftTop(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
	}

	public static Animation ScaleSmallLeftTop(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.ScaleSmallLeftTop(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
	}

	public static Animation getShakeMode(@NonNull Builder builder,Integer shakeCount) {
        Animation animation = BaseAnimViewS.ShakeMode(builder.interpolator,shakeCount);
        bindAnim(builder,animation);
        return animation;
	}

	public static Animation ScaleToBigHorizontalIn(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.ScaleToBigHorizontalIn(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
	}

	public static Animation ScaleToBigHorizontalOut(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.ScaleToBigHorizontalOut(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
	}

	public static Animation ScaleToBigVerticalIn(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.ScaleToBigVerticalIn(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
	}

	public static Animation ScaleToBigVerticalOut(@NonNull Builder builder) {
        Animation animation = BaseAnimViewS.ScaleToBigVerticalOut(builder.interpolator);
        bindAnim(builder,animation);
        return animation;
	}
	/***************************************************************************/

	public static View getRootView(Activity context) {
		return ((ViewGroup) context.findViewById(android.R.id.content))
				.getChildAt(0);
	}

	private static class CloseActivityAnimationListener implements AnimationListener{

		private Builder builder;

		public CloseActivityAnimationListener(Builder builder){
			this.builder = builder;
		}

		@Override
		public void onAnimationStart(Animation animation) {
			if (builder.animationListener != null){
                builder.animationListener.onAnimationStart();
			}
		}

		@Override
		public void onAnimationEnd(Animation animation) {
            if (builder.animationListener != null){
                builder.animationListener.onAnimationEnd();
            }
            if (builder.activity != null && builder.isClose){
                builder.activity.finish();
            }
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	}
	private static class CloseActivityAnimatorListener implements AnimatorListener{

        private Builder builder;

		public CloseActivityAnimatorListener(Builder builder){
			this.builder = builder;
		}

        @Override
        public void onAnimationStart(Animator animation) {
            if (builder.animationListener != null){
                builder.animationListener.onAnimationStart();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (builder.animationListener != null){
                builder.animationListener.onAnimationEnd();
            }
            if (builder.activity != null && builder.isClose){
                builder.activity.finish();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
	}

}

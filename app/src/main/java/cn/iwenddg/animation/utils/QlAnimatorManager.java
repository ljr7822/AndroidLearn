package cn.iwenddg.animation.utils;

import static android.animation.ObjectAnimator.ofFloat;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Collection;

/**
 * @author iwen大大怪
 * @create 2021/11/04 1:53
 */
public class QlAnimatorManager {
    public static ValueAnimator getMJsonAnimator() {
        return sMJsonAnimator;
    }

    private static ValueAnimator sMJsonAnimator;

    @NonNull
    public static ObjectAnimator getTranslateAnimator(final View view, float startFloat, float endFloat, int duration) {
        return ofFloat(view, "translationY", startFloat, endFloat).setDuration(duration);
    }

    @NonNull
    public static ObjectAnimator getTranslateXAnimator(final View view, float startFloat, float endFloat, int duration) {
        return ofFloat(view, "translationX", startFloat, endFloat).setDuration(duration);
    }
    @NonNull
    public static ObjectAnimator getTranslateXAnimator(final View view, float startFloat, float endFloat, int duration, TimeInterpolator interpolator) {
        ObjectAnimator objectAnimator = ofFloat(view, "translationX", startFloat, endFloat).setDuration(duration);
        if(interpolator!=null){
            objectAnimator.setInterpolator(interpolator);
        }
        return objectAnimator;
    }

    public static ObjectAnimator getTranslateAnimator(final View view, float startFloat, float endFloat, int duration, TimeInterpolator interpolator) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", startFloat, endFloat).setDuration(duration);
        if(interpolator!=null){
            objectAnimator.setInterpolator(interpolator);
        }
        return objectAnimator;
    }
    public static ObjectAnimator getTranslateAnimator(final View view, float startFloat, float endFloat, int duration, TimeInterpolator interpolator, Animator.AnimatorListener listener) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", startFloat, endFloat).setDuration(duration);
        if (interpolator != null) {
            objectAnimator.setInterpolator(interpolator);
        }
        objectAnimator.addListener(listener);
        return objectAnimator;
    }

    public static ObjectAnimator getTranslateAnimator(final View view, float startFloat, float endFloat, int duration, Animator.AnimatorListener listener) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", startFloat, endFloat).setDuration(duration);
        objectAnimator.addListener(listener);
        return objectAnimator;
    }

    @NonNull
    public static ObjectAnimator getRotateAnimator(final View view, float startFloat, float endFloat, int duration) {
        return ofFloat(view, "rotation", startFloat, endFloat).setDuration(duration);
    }
    @NonNull
    public static ObjectAnimator getRotateAnimatorInfinite(final View view, float startFloat, float endFloat, int duration) {
        ObjectAnimator objectAnimator = ofFloat(view, "rotation", startFloat, endFloat).setDuration(duration);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setInterpolator(new LinearInterpolator());
        return objectAnimator;
    }
    @NonNull
    public static ObjectAnimator getRotateAnimator(final View view, float startFloat, float endFloat, int duration, TimeInterpolator interpolator) {
        ObjectAnimator objectAnimator = ofFloat(view, "rotation", startFloat, endFloat).setDuration(duration);
        objectAnimator.setInterpolator(interpolator);
        return objectAnimator;
    }

    @NonNull
    public static ObjectAnimator getAlphaAnimator(final View view, float startFloat, float endFloat, int duration) {

        return ofFloat(view, "alpha", startFloat, endFloat).setDuration(duration);
    }

    @NonNull
    public static ObjectAnimator getScaleXAnimator(final View view, float startFloat, float endFloat, int duration) {

        return ofFloat(view, "scaleX", startFloat, endFloat).setDuration(duration);
    }

    @NonNull
    public static ObjectAnimator getScaleYAnimator(final View view, float startFloat, float endFloat, int duration) {

        return ofFloat(view, "scaleY", startFloat, endFloat).setDuration(duration);
    }

    @NonNull
    public static AnimatorSet getAnimatorSet(Animator... items) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(items);
        return animatorSet;
    }

    @NonNull
    public static AnimatorSet getAnimatorSet(Collection<Animator> items) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(items);
        return animatorSet;
    }

    public static void playJsonAnim(final LottieAnimationView animationView, Animator.AnimatorListener listener) {
        if (sMJsonAnimator != null) {
//            sMJsonAnimator.cancel();
            sMJsonAnimator.removeAllUpdateListeners();
            sMJsonAnimator.removeAllListeners();
            sMJsonAnimator = null;
        }
        sMJsonAnimator = ValueAnimator.ofFloat(0f, 1f);
        sMJsonAnimator.setDuration(2000);
        sMJsonAnimator.setRepeatCount(1);
        sMJsonAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animationView.setProgress((Float) animation.getAnimatedValue());
            }
        });
        sMJsonAnimator.addListener(listener);
        sMJsonAnimator.start();
    }

    public static void destroySMJsonAnimator() {
        if (sMJsonAnimator != null) {
            sMJsonAnimator.removeAllUpdateListeners();
            sMJsonAnimator.removeAllListeners();
            sMJsonAnimator.cancel();
            sMJsonAnimator = null;
        }
    }
}

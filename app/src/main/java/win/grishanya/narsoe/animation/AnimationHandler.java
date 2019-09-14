package win.grishanya.narsoe.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import win.grishanya.narsoe.R;

public class AnimationHandler {
    public static void slide_down(Context context, View v) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void slide_up(Context context, View v) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void slide_down_with_callback(Context context, View v, final AnimationHandlerCallbacks animationHandlerCallbacks) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        if (a != null) {
            a.reset();
            if (v != null) {
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        animationHandlerCallbacks.OnAnimationEnded();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void slide_up_with_callback(Context context, View v, final AnimationHandlerCallbacks animationHandlerCallbacks) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        if (a != null) {
            a.reset();
            if (v != null) {
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        animationHandlerCallbacks.OnAnimationEnded();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public interface AnimationHandlerCallbacks {
        public void OnAnimationEnded();
    }
}

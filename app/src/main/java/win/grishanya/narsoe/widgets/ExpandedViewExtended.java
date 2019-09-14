package win.grishanya.narsoe.widgets;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import win.grishanya.narsoe.animation.AnimationHandler;

public class ExpandedViewExtended {
    private final Context context;
    private Boolean ViewExpanded = true;
    public ExpandedViewExtended(Context context){
        this.context = context;
    }

    public void setSideUpSideDownOnClickListenerForView(View ClickAbleView,final View AnimatedView){
        ClickAbleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AnimatedView.isShown()){
                    AnimationHandler.slide_up(context, AnimatedView);
                    AnimatedView.setVisibility(View.GONE);
                }
                else{
                    AnimatedView.setVisibility(View.VISIBLE);
                    AnimationHandler.slide_down(context, AnimatedView);
                }
            }
        });
    }

    public Boolean getViewExpanded(){
        return ViewExpanded;
    }

    public void setViewExpanded(Boolean expanded){
        ViewExpanded = expanded;
    }

    public void setSideUpSideDownOnClickListenerForViewWithDefaultImagesIndication(View ClickAbleView, final View AnimatedView, final ImageView imageWrapper){
        setViewExpanded(AnimatedView.getVisibility() == View.VISIBLE);
        ClickAbleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getViewExpanded()){
                    imageWrapper.setImageResource(android.R.drawable.arrow_down_float);
                    AnimationHandler.slide_up_with_callback(context, AnimatedView, new AnimationHandler.AnimationHandlerCallbacks() {
                        @Override
                        public void OnAnimationEnded() {
                            if(AnimatedView instanceof LinearLayout){
                                int children = ((LinearLayout) AnimatedView).getChildCount();
                                for (int i=0;i<children;++i) {
                                    View childView = ((LinearLayout) AnimatedView).getChildAt(i);
                                    childView.setVisibility(View.GONE);
                                }

                            }
                            setViewExpanded(false);
                        }
                    });

                }
                else{
                    imageWrapper.setImageResource(android.R.drawable.arrow_up_float);
                    setViewExpanded(true);
                    if(AnimatedView instanceof LinearLayout){
                        int children = ((LinearLayout) AnimatedView).getChildCount();
                        for (int i=0;i<children;++i) {
                            View childView = ((LinearLayout) AnimatedView).getChildAt(i);
                            childView.setVisibility(View.VISIBLE);
                        }

                    }
                    AnimationHandler.slide_down_with_callback(context, AnimatedView, new AnimationHandler.AnimationHandlerCallbacks() {
                        @Override
                        public void OnAnimationEnded() {

                        }
                    });
                }
            }
        });
    }

    public void HideViewExtended(View animatedView,ImageView indicatorImageView){
        setViewExpanded(false);
        indicatorImageView.setImageResource(android.R.drawable.arrow_down_float);
        if(animatedView instanceof LinearLayout){
            int children = ((LinearLayout) animatedView).getChildCount();
            for (int i=0;i<children;++i) {
                View childView = ((LinearLayout) animatedView).getChildAt(i);
                childView.setVisibility(View.GONE);
            }
        }else {
            animatedView.setVisibility(View.GONE);
        }
    }

    public void ExpandViewExtended(View animatedView,ImageView indicatorImageView){
        setViewExpanded(true);
        indicatorImageView.setImageResource(android.R.drawable.arrow_up_float);
        if(animatedView instanceof LinearLayout){
            int children = ((LinearLayout) animatedView).getChildCount();
            for (int i=0;i<children;++i) {
                View childView = ((LinearLayout) animatedView).getChildAt(i);
                childView.setVisibility(View.VISIBLE);
            }
        }else {
            animatedView.setVisibility(View.VISIBLE);
        }
    }
}

package win.grishanya.narsoe.widgets;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import win.grishanya.narsoe.animation.AnimationHandler;

public class ExpandedView {
    private final Context context;

    public ExpandedView (Context context){
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

    public void setSideUpSideDownOnClickListenerForViewWithDefaultImagesIndication(View ClickAbleView, final View AnimatedView, final ImageView imageWrapper){
        ClickAbleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AnimatedView.isShown()){
                    AnimationHandler.slide_up(context, AnimatedView);
                    imageWrapper.setImageResource(android.R.drawable.arrow_down_float);
                    AnimatedView.setVisibility(View.GONE);
                }
                else{
                    AnimatedView.setVisibility(View.VISIBLE);
                    imageWrapper.setImageResource(android.R.drawable.arrow_up_float);
                    AnimationHandler.slide_down(context, AnimatedView);
                }
            }
        });
    }

    public void ExpandView (View goneView){
        goneView.setVisibility(View.GONE);
    }


}

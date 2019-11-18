package win.grishanya.narsoe.widgets;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ExpandedViewBasic extends ExpandedViewExtended {
    private View animatedView;
    private ImageView indicatorImageView;

    public ExpandedViewBasic(View clickAbleView, View animatedViev, ImageView indicatorImageView, Context context) {
        super(context);
        this.animatedView = animatedViev;
        this.indicatorImageView = indicatorImageView;
        setSideUpSideDownOnClickListenerForViewWithDefaultImagesIndication(clickAbleView,animatedViev,indicatorImageView);
    }

    public void HideView(){
        HideViewExtended(animatedView,indicatorImageView);
    }

    public void ExpandView(){
        ExpandViewExtended(animatedView,indicatorImageView);
    }
}

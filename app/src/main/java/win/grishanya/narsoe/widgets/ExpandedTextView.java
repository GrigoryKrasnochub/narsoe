package win.grishanya.narsoe.widgets;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpandedTextView extends ExpandedView {
    public ExpandedTextView(LinearLayout linearLayout, TextView headerTextView, TextView infoTextView, ImageView headerImage, Context context) {
        super(context);
        setSideUpSideDownOnClickListenerForViewWithDefaultImagesIndication(linearLayout,infoTextView,headerImage);
    }
}

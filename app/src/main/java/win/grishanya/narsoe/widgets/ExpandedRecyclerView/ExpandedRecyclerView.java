package win.grishanya.narsoe.widgets.ExpandedRecyclerView;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import win.grishanya.narsoe.dataClasses.ExpandedRecyclerViewData;
import win.grishanya.narsoe.widgets.ExpandedViewBasic;

public class ExpandedRecyclerView extends ExpandedViewBasic {

    TextView headerTextView;
    ImageView indicatorImageView;
    RecyclerView contentWrapperRecyclerView;
    Context context;

    public ExpandedRecyclerView(View baseLayout,final View contentWrapper, TextView headerTextView, ImageView indicatorImageView,final RecyclerView contentWrapperRecyclerView, ArrayList<ExpandedRecyclerViewData> expandedRecyclerViewData, Context context) {
        super(baseLayout,contentWrapper,indicatorImageView,context);
        this.headerTextView = headerTextView;
        this.indicatorImageView = indicatorImageView;
        this.contentWrapperRecyclerView = contentWrapperRecyclerView;
        this.context = context;

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        this.contentWrapperRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(this.contentWrapperRecyclerView.getContext(),
                layoutManager.getOrientation());
        this.contentWrapperRecyclerView.addItemDecoration(mDividerItemDecoration);

        ExpandedViewRecyclerViewAdapter.ItemClickListener itemClickListener = new ExpandedViewRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick() {
                if (getViewExpanded()){
                    HideView();
                }else{
                    ExpandView();
                }
            }
        };

        ExpandedViewRecyclerViewAdapter expandedViewRecyclerViewAdapter = new ExpandedViewRecyclerViewAdapter(expandedRecyclerViewData,itemClickListener);
        this.contentWrapperRecyclerView.setAdapter(expandedViewRecyclerViewAdapter);
    }
}

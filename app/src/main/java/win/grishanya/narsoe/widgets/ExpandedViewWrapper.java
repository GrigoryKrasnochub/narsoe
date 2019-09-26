package win.grishanya.narsoe.widgets;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import win.grishanya.narsoe.animation.AnimationHandler;
import win.grishanya.narsoe.dataClasses.ExpandedRecyclerViewData;
import win.grishanya.narsoe.widgets.ExpandedRecyclerView.ExpandedRecyclerView;
import win.grishanya.narsoe.widgets.ExpandedRecyclerView.ExpandedViewRecyclerViewAdapter;

public class ExpandedViewWrapper {

    private  View baseLayout;
    private  View contentWrapper;
    private  TextView headerTextView;
    private  ImageView indicatorImageView;
    private  RecyclerView contentWrapperRecyclerView;
    private  ArrayList<ExpandedRecyclerViewData> expandedRecyclerViewData;
    private  RecyclerView rootRecyclerView;
    private  int position;
    private  WrapperChanged wrapperChanged;
    private  Context context;
    private  Boolean ViewExpanded;

    public interface WrapperChanged {
        public void OnWrapperChanged(int position, boolean show);
    }

    public ExpandedViewWrapper(boolean isViewExpanded,int position, View baseLayout, final View contentWrapper, TextView headerTextView, ImageView indicatorImageView, final RecyclerView contentWrapperRecyclerView, ArrayList<ExpandedRecyclerViewData> expandedRecyclerViewData, Context context, WrapperChanged wrapperChanged) {
        setViewExpanded(isViewExpanded);
        this.baseLayout = baseLayout;
        this.contentWrapper = contentWrapper;
        this.headerTextView = headerTextView;
        this.indicatorImageView = indicatorImageView;
        this.contentWrapperRecyclerView = contentWrapperRecyclerView;
        this.expandedRecyclerViewData = expandedRecyclerViewData;
        this.position = position;
        this.wrapperChanged = wrapperChanged;
        this.context = context;

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        this.contentWrapperRecyclerView.setLayoutManager(layoutManager);

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

        setSideUpSideDownOnClickListenerForViewWithDefaultImagesIndication();
    }

    public Boolean getViewExpanded(){
        return ViewExpanded;
    }

    public void setViewExpanded(Boolean expanded){
        ViewExpanded = expanded;
    }

    public void setSideUpSideDownOnClickListenerForViewWithDefaultImagesIndication(){
        baseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getViewExpanded()){
                    if(contentWrapper instanceof LinearLayout){
                        int children = ((LinearLayout) contentWrapper).getChildCount();
                        for (int i=0;i<children;++i) {
                            View childView = ((LinearLayout) contentWrapper).getChildAt(i);
                            childView.setVisibility(View.GONE);
                        }
                        setViewExpanded(false);
                        indicatorImageView.setImageResource(android.R.drawable.arrow_down_float);
                        wrapperChanged.OnWrapperChanged(position, getViewExpanded());
                    }
                }
                else{
                    if(contentWrapper instanceof LinearLayout){
                        setViewExpanded(true);
                        int children = ((LinearLayout) contentWrapper).getChildCount();
                        for (int i=0;i<children;++i) {
                            View childView = ((LinearLayout) contentWrapper).getChildAt(i);
                            childView.setVisibility(View.VISIBLE);
                        }
                        indicatorImageView.setImageResource(android.R.drawable.arrow_up_float);
                        wrapperChanged.OnWrapperChanged(position, getViewExpanded());
                    }
                }
            }
        });
    }

    public void HideView(){
        if(contentWrapper instanceof LinearLayout){
            setViewExpanded(false);
            int children = ((LinearLayout) contentWrapper).getChildCount();
            for (int i=0;i<children;++i) {
                View childView = ((LinearLayout) contentWrapper).getChildAt(i);
                childView.setVisibility(View.GONE);
            }
            indicatorImageView.setImageResource(android.R.drawable.arrow_down_float);
            wrapperChanged.OnWrapperChanged(position, getViewExpanded());
        }
    }

    public void ExpandView(){
        if(contentWrapper instanceof LinearLayout){
            setViewExpanded(true);
            int children = ((LinearLayout) contentWrapper).getChildCount();
            for (int i=0;i<children;++i) {
                View childView = ((LinearLayout) contentWrapper).getChildAt(i);
                childView.setVisibility(View.VISIBLE);
            }
            indicatorImageView.setImageResource(android.R.drawable.arrow_up_float);
            wrapperChanged.OnWrapperChanged(position, getViewExpanded());
        }
    }
}

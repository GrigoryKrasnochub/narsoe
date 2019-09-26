package win.grishanya.narsoe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import win.grishanya.narsoe.dataClasses.ExpandedViewsWrapper;
import win.grishanya.narsoe.widgets.ExpandedRecyclerView.ExpandedRecyclerView;
import win.grishanya.narsoe.widgets.ExpandedRecyclerView.ExpandedViewRecyclerViewAdapter;
import win.grishanya.narsoe.widgets.ExpandedViewWrapper;

public class NumberInfoWrapperRecyclerView extends RecyclerView.Adapter<NumberInfoWrapperRecyclerView.NumberInfoWrapperRecyclerViewHolder> {

    private ArrayList<ExpandedViewsWrapper> expandedViewWrapper = new ArrayList<>();
    private static Boolean [] ExpandedViewsMap = new Boolean[]{true,false};

    public class NumberInfoWrapperRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView infoBlockHeader;
        public TextView infoBlockDescription;
        public LinearLayout infoBlockContentWrapper;
        public LinearLayout infoBlockWrapper;
        public RecyclerView infoBlockInfoItemWrapper;
        public ImageView imageIndicator;
        public Context context;

        public NumberInfoWrapperRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            infoBlockHeader = (TextView) itemView.findViewById(R.id.expandedViewItemHeader);
            infoBlockDescription = (TextView) itemView.findViewById(R.id.expandedViewWrapperDescription);
            infoBlockContentWrapper = (LinearLayout) itemView.findViewById(R.id.expandedViewWrapperContentWrapper);
            infoBlockWrapper = (LinearLayout) itemView.findViewById(R.id.expandedViewWrapperWrapper);
            infoBlockInfoItemWrapper = (RecyclerView) itemView.findViewById(R.id.expandedViewWrapperInfoItemWrapper);
            imageIndicator = (ImageView) itemView.findViewById(R.id.expandedViewWrapperTitleImageImageView);
            context = itemView.getContext();
        }

    }

    public NumberInfoWrapperRecyclerView(ArrayList<ExpandedViewsWrapper> expandedViewsWrapper) {
        super();
        this.expandedViewWrapper.addAll(expandedViewsWrapper);
    }

    @NonNull
    @Override
    public NumberInfoWrapperRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expanded_view_wrapper_item,viewGroup,false);
        NumberInfoWrapperRecyclerViewHolder viewHolder = new NumberInfoWrapperRecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NumberInfoWrapperRecyclerViewHolder numberInfoWrapperRecyclerViewHolder, int i) {
        ExpandedViewsWrapper item = expandedViewWrapper.get(i);
        numberInfoWrapperRecyclerViewHolder.infoBlockHeader.setText(item.Header);
        numberInfoWrapperRecyclerViewHolder.infoBlockDescription.setText(item.Description);
        int visibility = ExpandedViewsMap[i] ? View.VISIBLE : View.GONE;
        numberInfoWrapperRecyclerViewHolder.infoBlockInfoItemWrapper.setVisibility(visibility);
        numberInfoWrapperRecyclerViewHolder.infoBlockDescription.setVisibility(visibility);
        ExpandedViewWrapper expandedRecyclerView = new ExpandedViewWrapper(ExpandedViewsMap[i],i,numberInfoWrapperRecyclerViewHolder.infoBlockWrapper, numberInfoWrapperRecyclerViewHolder.infoBlockContentWrapper,
                numberInfoWrapperRecyclerViewHolder.infoBlockHeader, numberInfoWrapperRecyclerViewHolder.imageIndicator, numberInfoWrapperRecyclerViewHolder.infoBlockInfoItemWrapper, item.data, numberInfoWrapperRecyclerViewHolder.context, new ExpandedViewWrapper.WrapperChanged() {
            @Override
            public void OnWrapperChanged(int position, boolean isViewExpanded) {
                ExpandedViewsMap[position] = isViewExpanded;
                notifyItemChanged(numberInfoWrapperRecyclerViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return expandedViewWrapper.size();
    }
}

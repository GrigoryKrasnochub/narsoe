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

import java.util.ArrayList;

import win.grishanya.narsoe.dataClasses.ExpandedViewsWrapperData;
import win.grishanya.narsoe.widgets.ExpandedViewWrapper;

public class NumberInfoWrapperRecyclerView extends RecyclerView.Adapter<NumberInfoWrapperRecyclerView.NumberInfoWrapperRecyclerViewHolder> {

    private ArrayList<ExpandedViewsWrapperData> expandedViewWrapper = new ArrayList<>();

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

    public NumberInfoWrapperRecyclerView(ArrayList<ExpandedViewsWrapperData> expandedViewsWrapperData) {
        super();
        this.expandedViewWrapper.addAll(expandedViewsWrapperData);
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
        final ExpandedViewsWrapperData item = expandedViewWrapper.get(i);
        numberInfoWrapperRecyclerViewHolder.infoBlockHeader.setText(item.Header);
        numberInfoWrapperRecyclerViewHolder.infoBlockDescription.setText(item.Description);
        ExpandedViewWrapper expandedRecyclerView = new ExpandedViewWrapper(item.Expanded,i,numberInfoWrapperRecyclerViewHolder.infoBlockWrapper, numberInfoWrapperRecyclerViewHolder.infoBlockContentWrapper,
                numberInfoWrapperRecyclerViewHolder.infoBlockHeader, numberInfoWrapperRecyclerViewHolder.imageIndicator, numberInfoWrapperRecyclerViewHolder.infoBlockInfoItemWrapper, item.data, numberInfoWrapperRecyclerViewHolder.context, new ExpandedViewWrapper.WrapperChanged() {
            @Override
            public void OnWrapperChanged(int position, boolean isViewExpanded) {
                item.Expanded = isViewExpanded;
                notifyItemChanged(numberInfoWrapperRecyclerViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return expandedViewWrapper.size();
    }
}

package win.grishanya.narsoe.widgets.ExpandedRecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import win.grishanya.narsoe.R;
import win.grishanya.narsoe.dataClasses.ExpandedRecyclerViewData;

public class ExpandedViewRecyclerViewAdapter extends RecyclerView.Adapter<ExpandedViewRecyclerViewAdapter.ExpandedViewRecyclerViewHolder> {

    private ArrayList<ExpandedRecyclerViewData> expandedViewData = new ArrayList<>();
    private ItemClickListener itemClickListener;

    public class ExpandedViewRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView headerTextView;
        TextView infoTextView;
        private ItemClickListener itemClickListener;

        public ExpandedViewRecyclerViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            headerTextView = (TextView) itemView.findViewById(R.id.headerTextView);
            infoTextView = (TextView) itemView.findViewById(R.id.infoTextView);
            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick();
        }
    }

    public ExpandedViewRecyclerViewAdapter(ArrayList<ExpandedRecyclerViewData> expandedViewData, ItemClickListener itemClickListener) {
        this.expandedViewData.addAll(expandedViewData);
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ExpandedViewRecyclerViewAdapter.ExpandedViewRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expanded_view_recycler_view_item,viewGroup,false);
        ExpandedViewRecyclerViewHolder viewHolder = new ExpandedViewRecyclerViewHolder(v,itemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpandedViewRecyclerViewHolder expandedViewRecyclerViewHolder, int i) {
        ExpandedRecyclerViewData expandedRecyclerViewData = this.expandedViewData.get(i);
        expandedViewRecyclerViewHolder.headerTextView.setText(expandedRecyclerViewData.Header);
        expandedViewRecyclerViewHolder.infoTextView.setText(expandedRecyclerViewData.Info);
    }

    @Override
    public int getItemCount() {
        return expandedViewData.size();
    }

    public interface ItemClickListener {
        void onItemClick();
    }
}
